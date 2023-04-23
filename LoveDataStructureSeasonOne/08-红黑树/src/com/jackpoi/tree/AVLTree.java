package com.jackpoi.tree;

import java.util.Comparator;

/**
 * @author beastars
 */
public class AVLTree<E> extends BBST<E> {
    public AVLTree(Comparator<E> comparator) {
        super(comparator);
    }

    public AVLTree() {
        this(null);
    }

    /**
     * 处理加入节点后，恢复平衡
     *
     * @param node 新添加的节点
     */
    @Override
    protected void afterAdd(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalance(node)) {
                // 更新高度
                updateHeight(node);
            } else {
                // 恢复平衡
                reBalance(node);
                // 此处恢复平衡后，整棵树恢复平衡，直接退出
                break;
            }
        }
    }

    /**
     * 处理删除节点后，恢复平衡
     *
     * @param node 新删除的节点
     */
    @Override
    protected void afterRemove(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalance(node)) {
                // 更新高度
                updateHeight(node);
            } else {
                // 恢复平衡
                reBalance(node);
            }
        }
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<>(element, parent);
    }

    private void reBalance(Node<E> grand) {
        Node<E> parent = ((AVLNode<E>) grand).tallerChild(); // p
        Node<E> node = ((AVLNode<E>) parent).tallerChild(); // n
        if (parent.isLeftChild()) { // L
            if (node.isLeftChild()) { // L
                // LL
                rotateRight(grand);
            } else { // R
                // LR
                rotateLeft(parent);
                rotateRight(grand);
            }
        } else { // R
            if (node.isLeftChild()) { // L
                // RL
                rotateRight(parent);
                rotateLeft(grand);
            } else { // R
                // RR
                rotateLeft(grand);
            }
        }
    }

    private void reBalance2(Node<E> grand) {
        Node<E> parent = ((AVLNode<E>) grand).tallerChild(); // p
        Node<E> node = ((AVLNode<E>) parent).tallerChild(); // n
        if (parent.isLeftChild()) { // L
            if (node.isLeftChild()) { // L
                // LL
                rotate(grand, node, node.right, parent, parent.right, grand);
            } else { // R
                // LR
                rotate(grand, parent, node.left, node, node.right, grand);
            }
        } else { // R
            if (node.isLeftChild()) { // L
                // RL
                rotate(grand, grand, node.left, node, node.right, parent);
            } else { // R
                // RR
                rotate(grand, grand, parent.left, parent, node.left, node);
            }
        }
    }

    /**
     * 统一旋转，旋转的规律：由于是二叉搜索树，中序遍历为升序，则grand、parent、node三个升序排列后，一定是中间的是根节点，最小的是左子树，最大的是右子树
     * 同时，子树的最左边节点和最右边节点是不变的，因此不需要传入
     * 传入的参数是：第一个是子树的根节点，后面的是按中序遍历并且除去第一个和最后一个
     * <p>
     * 原理图：https://gitee.com/beastars1/blog-images/raw/master/images/20200706130826.png
     *
     * @param r 根节点，grand
     * @param b 中序遍历第一个，根节点一，一定是左子树
     * @param c 中序遍历第二个
     * @param d 中序遍历第三个，根节点二，一定是他是最后的根节点
     * @param e 中序遍历第四个
     * @param f 中序遍历第五个，根节点三，一定是右子树
     */
    @Override
    protected void rotate(Node<E> r,
                          Node<E> b, Node<E> c,
                          Node<E> d,
                          Node<E> e, Node<E> f) {
        super.rotate(r, b, c, d, e, f);

        // 更新高度
        updateHeight(b);
        updateHeight(f);
        updateHeight(d);
    }

    protected void rotateLeft(Node<E> grand) {
        // 左旋转，对应的是RR
        Node<E> parent = grand.right;
        Node<E> child = parent.left; // parent的左子树，需要更新父节点，提取出来
        // 左旋转
//        grand.right = parent.left;
        grand.right = child;
        parent.left = grand; // parent的left指向grand
        afterRotate(grand, parent, child);

    }

    protected void rotateRight(Node<E> grand) {
        // 右旋转，对应LL
        Node<E> parent = grand.left;
        Node<E> child = parent.right;
        grand.left = child;
        parent.right = grand;

        afterRotate(grand, parent, child);
    }

    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        super.afterRotate(grand, parent, child);

        // 更新parent和grand的高度，先更新下边的节点
        updateHeight(grand);
        updateHeight(parent);
    }

    private boolean isBalance(Node<E> node) {
        return Math.abs(((AVLNode<E>) node).balanceFactor()) <= 1;
    }

    private void updateHeight(Node<E> node) {
        ((AVLNode<E>) node).updateHeight();
    }

    private static class AVLNode<E> extends Node<E> {
        int height = 1;

        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }

        public int balanceFactor() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            return leftHeight - rightHeight;
        }

        public void updateHeight() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            height = 1 + Math.max(leftHeight, rightHeight);
        }

        /**
         * 判断左右子树哪个更高，并将其返回
         */
        public Node<E> tallerChild() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            if (leftHeight > rightHeight)
                return left;
            if (leftHeight < rightHeight)
                return right;
            // 如果子树高度相同，判断当前节点是父节点的左子树还是右子树，如果是左子树，那么就返回该节点的左子树，反之亦然
            return isLeftChild() ? left : right;
        }
    }
}

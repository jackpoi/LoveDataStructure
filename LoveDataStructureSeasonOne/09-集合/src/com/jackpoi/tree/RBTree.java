package com.jackpoi.tree;

import java.util.Comparator;

/**
 * @author beastars
 */
public class RBTree<E> extends BBST<E> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RBTree(Comparator<E> comparator) {
        super(comparator);
    }

    public RBTree() {
        this(null);
    }

    @Override
    protected void afterAdd(Node<E> node) {
        Node<E> parent = node.parent;

        // 添加的是根节点 或是上溢到达了根节点
        if (parent == null) {
            black(node);
            return;
        }

        // 如果父节点是黑色，直接返回
        if (isBlack(parent))
            return;

        // 获取叔父节点
        Node<E> uncle = parent.sibling();
        // 获取祖父节点
        Node<E> grand = red(parent.parent);
//        Node<E> grand = parent.parent;
        if (isRed(uncle)) { // 如果叔父节点是红色，说明上溢
            /*
            判定条件：uncle 是 RED
            1. parent、uncle 染成 BLACK
            2. grand 向上合并
            染成 RED，当做是新添加的节点进行处理
             */
            black(parent);
            black(uncle);
            // 祖父节点当作是新添加的节点，向上合并
//            red(grand);
            /*
            grand 向上合并时，可能继续发生上溢
            若上溢持续到根节点，只需将根节点染成 BLACK
             */
            afterAdd(grand);
            return;
        }

        // 叔父节点不是红色，没有上溢
            /*
            LL\RR
            判定条件：uncle 不是 RED
            1. parent 染成 BLACK，grand 染成 RED
            2. grand 进行单旋操作
            LL：右旋转
            RR：左旋转

            LR\RL
             判定条件：uncle 不是 RED
            1. 自己染成 BLACK，grand 染成 RED
            2. 进行双旋操作
            LR：parent 左旋转， grand 右旋转
            RL：parent 右旋转， grand 左旋转
             */
        if (parent.isLeftChild()) { // L
//            red(grand);
            if (node.isLeftChild()) { // LL
                black(parent);
            } else { // LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        } else { // R
//            red(grand);
            if (node.isLeftChild()) { // RL
                black(node);
                rotateRight(parent);
            } else { // RR
                black(parent);
            }
            rotateLeft(grand);
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        //

        // 删除 – 拥有1个RED子节点的BLACK节点
        // 判定条件：用以替代的子节点是 RED 或者 删除的节点是红色，直接删除
        if (isRed(node)) {
            // 将替代的子节点染成 BLACK 即可保持红黑树性质
            black(node);
            return;
        }

        Node<E> parent = node.parent;
        // 删除的是根节点
        if (parent == null)
            return;

        // 删除的是黑色叶子节点【下溢】
        boolean left = parent.left == null || node.isLeftChild(); // 判断被删除的节点是左还是右
        // 判断删除节点的兄弟节点，如果node是左，那么兄弟节点就是右
        Node<E> sibling = left ? parent.right : parent.left;
        if (left) { // 如果兄弟节点在右边，那么被删除的节点在左边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }

            // 此时，兄弟节点一定是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) { // 兄弟节点的两个子节点都是黑色
                boolean parentBlack = isBlack(parent); // 判断父节点的颜色
                red(sibling);
                black(parent);
                if (parentBlack) { // 如果父节点是黑色
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有一个红色子节点，向兄弟节点借元素
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right; // 更换兄弟节点
                }

                // 先进行染色操作
                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                // 在进行旋转
                rotateLeft(parent);
            }
        } else { // 如果兄弟节点在左边，那么被删除的节点在右边
            if (isRed(sibling)) { // 兄弟节点是红色
                /*
                如果 sibling 是 RED
                sibling 染成 BLACK，parent 染成 RED，进行旋转
                于是又回到 sibling 是 BLACK 的情况
                 */
                black(sibling);
                red(parent);
                // 兄弟节点在左边，进行右旋转
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }

            // 此时，兄弟节点一定是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) { // 兄弟节点的两个子节点都是黑色
                    /*
                    sibling 没有 1 个 RED 子节点，父节点要向下跟兄弟节点合并
                    将 sibling 染成 RED、parent 染成 BLACK 即可修复红黑树性质
                     */
                boolean parentBlack = isBlack(parent); // 判断父节点的颜色
                red(sibling);
                black(parent);
                if (parentBlack) { // 如果父节点是黑色
                        /*
                        如果 parent 是 BLACK
                        会导致 parent 也下溢
                        这时只需要把 parent 当做被删除的节点处理即可
                         */
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有一个红色子节点，向兄弟节点借元素
                    /*
                    BLACK 叶子节点被删除后，会导致B树节点下溢
                    如果 sibling 至少有 1 个 RED 子节点
                    进行旋转操作
                    旋转之后的 中心节点继承 parent 的颜色
                    旋转之后的 左右节点染为 BLACK
                     */

                // 如果兄弟节点的左节点是黑色，兄弟节点先进性旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left; // 更换兄弟节点
                }

                // 先进性染色操作
                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                // 在进行旋转
                rotateRight(parent);
            }
        }
    }

    private Node<E> color(Node<E> node, boolean color) {
        if (node != null)
            ((RBNode<E>) node).color = color;
        return node;
    }

    private Node<E> red(Node<E> node) {
        return color(node, RED);
    }

    private Node<E> black(Node<E> node) {
        return color(node, BLACK);
    }

    private boolean colorOf(Node<E> node) {
        return node == null ? BLACK : ((RBNode<E>) node).color;
    }

    private boolean isRed(Node<E> node) {
        return colorOf(node) == RED;
    }

    private boolean isBlack(Node<E> node) {
        return colorOf(node) == BLACK;
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new RBNode<>(element, parent);
    }

    private static class RBNode<E> extends Node<E> {
        boolean color = RED;

        public RBNode(E element, Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            String str = "";
            if (color == RED) {
                str = "R_";
            }
            return str + element.toString();
        }
    }
}

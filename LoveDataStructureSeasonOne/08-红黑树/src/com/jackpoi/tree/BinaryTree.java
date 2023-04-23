package com.jackpoi.tree;

import com.jackpoi.printer.BinaryTreeInfo;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author beastars
 */
public class BinaryTree<E> implements BinaryTreeInfo {
    protected int size;
    protected Node<E> root;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * 前序遍历
     */
    public void preorder(Visitor<E> visitor) {
        if (visitor == null)
            return;
        preorder(root, visitor);
    }

    private void preorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop)
            return;
        visitor.stop = visitor.visitor(node.element);
        preorder(node.left, visitor);
        preorder(node.right, visitor);
    }

    /**
     * 中序遍历
     */
    public void inorder(Visitor<E> visitor) {
        if (visitor == null)
            return;
        inorder(root, visitor);
    }

    private void inorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop)
            return;
        inorder(node.left, visitor);
        if (visitor.stop)
            return;
        visitor.stop = visitor.visitor(node.element);
        inorder(node.right, visitor);
    }

    /**
     * 后序遍历
     */
    public void postorder(Visitor<E> visitor) {
        if (visitor == null)
            return;
        postorder(root, visitor);
    }

    private void postorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop)
            return;
        postorder(node.left, visitor);
        postorder(node.right, visitor);
        if (visitor.stop)
            return;
        visitor.stop = visitor.visitor(node.element);
    }

    /**
     * 层序遍历
     */
    public void levelOrder(Visitor<E> visitor) {
        if (root == null || visitor == null)
            return;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (visitor.visitor(node.element))
                return;

            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    /**
     * 节点的高度（height）：从当前节点到最远叶子节点的路径上的节点总数
     *
     * @return 返回二叉树的最大高度
     */
    public int height() {
//        return height2(root); // 递归方式

        // 迭代方式
        if (root == null)
            return 0;
        // 高度
        int height = 0;
        // 每一层的节点数，每遍历完一层，queue中的元素就是下一层的所有节点，初始为1，为第一层根节点
        int levelSize = 1;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            levelSize--; // 每弹出一个元素，就减1，为0时代表该层节点遍历结束

            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }

            if (levelSize == 0) {
                // 当为0时代表该层节点遍历结束，queue中的元素就是下一层的所有节点，将高度加1
                levelSize = queue.size();
                height++;
            }
        }

        return height;
    }

    /**
     * 递归方式 节点的高度
     */
    private int height2(Node<E> node) {
        if (node == null)
            return 0;
        return 1 + Math.max(height2(node.left), height2(node.right));
    }

    protected Node<E> createNode(E element, Node<E> parent) {
        return new Node<>(element, parent);
    }

    /**
     * 是否为完全二叉树
     */
    public boolean isComplete() {
        if (root == null)
            return false;

        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false; // 如果为true，则后续节点应全为叶子节点
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (leaf && !node.isLeaf()) {
                // 如果后续节点应全为叶子节点，但是该节点又不是叶子节点，说明不是完全二叉树
                return false;
            }

            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) {
                return false;
            }

            if (node.right != null) {
                queue.offer(node.right);
            } else {
                leaf = true;
            }
        }

        return true;
    }

    /**
     * 前驱节点
     */
    protected Node<E> predecessor(Node<E> node) {
        if (node == null)
            return null;

        // 前驱节点在左子树当中
        Node<E> p = node.left;

        // 如果当前节点的左子树不为空
        if (p != null) {
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }

        // 如果当前节点的左子树为空，就向上找父节点，直到节点为父节点的右子树，或者一直到根节点
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }

        // 如果node.parent == null，说明没有左子树，祖父节点也没有左子树是他，因此没有前驱节点
        // return null;
        // return node.parent;
        // 如果node == node.parent.right，说明这个节点是他父节点的右子树，即父节点就是前驱节点
        return node.parent;
    }

    /**
     * 后继节点
     */
    protected Node<E> successor(Node<E> node) {
        if (node == null)
            return null;

        Node<E> p = node.right;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }

        while (node.parent != null && node.parent.left != node) {
            node = node.parent;
        }
        return node.parent;
    }

    public static abstract class Visitor<E> {
        boolean stop;

        /**
         * @return 如果返回true，就代表停止遍历
         */
        public abstract boolean visitor(E element);
    }

    protected static class Node<E> {
        E element;
        Node<E> parent;
        Node<E> left;
        Node<E> right;

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        public Node<E> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }
            if (isRightChild()) {
                return parent.left;
            }
            return null;
        }
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>) node).right;
    }

    @Override
    public Object string(Object node) {
        return node;
    }
}

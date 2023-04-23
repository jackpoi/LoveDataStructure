package com.jackpoi;

import com.jackpoi.printer.BinaryTreeInfo;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author beastars
 */
public class BinarySearchTree<E> implements BinaryTreeInfo {
    private int size;
    private Node<E> root;
    private Comparator<E> comparator;

    public BinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public BinarySearchTree() {
        this(null);
    }

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

    public void add(E element) {
        // 检查传入的元素是否为空
        elementNotNullCheck(element);

        if (root == null) { // 如果没有元素，传入根节点
            root = new Node<>(element, null);
            size++;
            return;
        }

        // 初始化父节点和当前节点都是根节点
        Node<E> parent = root;
        Node<E> node = root;
        int cmp = 0;
        while (node != null) {
            // 比较传入的元素和当前节点的大小
            cmp = compare(element, node.element);
            // 将当前节点作为父节点
            parent = node;
            if (cmp > 0) {
                // 大于就接着比较当前节点的右子树
                node = node.right;
            } else if (cmp < 0) {
                // 小于就接着比较当前节点的左子树
                node = node.left;
            } else {
                // 相等
                node.element = element;
                return;
            }
        }

        // 生成以空节点的父节点为父节点的节点。。
        Node<E> newNode = new Node<>(element, parent);
        if (cmp > 0) {
            // 大于就是放到右子树
            parent.right = newNode;
        } else {
            // 小于就是放到左子树
            parent.left = newNode;
        }

        size++;
    }

    public void remove(E element) {
        remove(node(element));
    }

    public boolean contains(E element) {
        return node(element) != null;
    }

    private void remove(Node<E> node) {
        if (node == null)
            return;

        size--;

        if (node.left != null && node.right != null) { // 度为2的节点
            // 找到前驱节点或后继节点
            Node<E> pre = predecessor(node);
            // 将找到的前驱节点或后继节点覆盖掉原来度为2的节点
            node.element = pre.element;
            // 删除找到的前驱节点或后继节点
            node = pre;
        }

        // 删除node节点 (该节点的度必定为0或1）
        // 因为如果原来节点的度为2，经过上一步node = pre;已经成为前驱节点或后继节点，度必定为0或1)
        // 如果度不为2，则必定为0或1
        Node<E> replacement = node.left != null ? node.left : node.right;
        if (replacement != null) { // 说明node的度为1
            // 更改parent
            replacement.parent = node.parent;
            // 更改parent的left或right的指向
            if (node.parent == null) { // node是度为1的节点，且是根节点
                root = replacement;
            } else if (node.parent.left == node) { // node是度为1的节点，且是父节点的左子树
                node.parent.left = replacement;
            } else { // node.parent.right == node
                node.parent.right = replacement;
            }
        } else if (node.parent == null) { // node是叶子节点(度为0)并且是根节点
            root = null;
        } else { // node是叶子节点，但不是根节点
            // 判断叶子节点是左还是右
            if (node.parent.left == node) {
                node.parent.left = null;
            } else { // node.parent.right == node
                node.parent.right = null;
            }
        }
    }

    private Node<E> node(E element) {
        Node<E> node = root;
        while (node != null) {
            int cmp = compare(element, node.element);
            if (cmp == 0)
                return node;
            if (cmp > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return null;
    }

    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<E>) e1).compareTo(e2);
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
     * 递归方式
     */
    private int height2(Node<E> node) {
        if (node == null)
            return 0;
        return 1 + Math.max(height2(node.left), height2(node.right));
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
    private Node<E> predecessor(Node<E> node) {
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
    private Node<E> successor(Node<E> node) {
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

//        if (node.parent == null) {
//            return null;
//        }
//        return node.parent;
        return node.parent;
    }

//    /**
//     * 是否为完全二叉树
//     */
//    public boolean isComplete() {
//        if (root == null)
//            return false;
//        Queue<Node<E>> queue = new LinkedList<>();
//        queue.offer(root);
//
//        boolean leaf = false; // 如果为true，代表后面的节点必须时叶子节点
//        while (!queue.isEmpty()) {
//            Node<E> node = queue.poll();
//
//            if (leaf && !node.isLeaf())
//                return false;
//
//            if (node.left != null && node.right != null) {
//                queue.offer(node.left);
//                queue.offer(node.right);
//            } else if (node.left == null && node.right != null) {
//                return false;
//            } else {
//                leaf = true;
//                if (node.left != null) {
//                    queue.offer(node.left);
//                }
//            }
//        }
//        return true;
//    }

    /*
     */
/**
 * 前序遍历
 *//*

    public void preorderTraversal() {
        preorderTraversal(root);
    }

    private void preorderTraversal(Node<E> node) {
        if (node == null)
            return;
        System.out.println(node.element);
        preorderTraversal(node.left);
        preorderTraversal(node.right);
    }

    */
/**
 * 中序遍历
 *//*

    public void inorderTraversal() {
        inorderTraversal(root);
    }

    private void inorderTraversal(Node<E> node) {
        if (node == null)
            return;
        inorderTraversal(node.left);
        System.out.println(node.element);
        inorderTraversal(node.right);
    }

    */
/**
 * 后序遍历
 *//*

    public void postorderTraversal() {
        postorderTraversal(root);
    }

    private void postorderTraversal(Node<E> node) {
        if (node == null)
            return;
        postorderTraversal(node.left);
        postorderTraversal(node.right);
        System.out.println(node.element);
    }

    */

    /**
     * 层序遍历
     *//*

    public void levelOrderTraversal() {
        if (root == null)
            return;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            System.out.println(node.element);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }
*/

    public static abstract class Visitor<E> {
        boolean stop;

        /**
         * @return 如果返回true，就代表停止遍历
         */
        public abstract boolean visitor(E element);
    }

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
    }

    private static class Node<E> {
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
        return ((Node<E>) node).element;
    }
}

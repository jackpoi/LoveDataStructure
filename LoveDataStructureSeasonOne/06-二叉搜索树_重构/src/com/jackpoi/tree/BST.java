package com.jackpoi.tree;

import java.util.Comparator;

/**
 * @author beastars
 */
public class BST<E> extends BinaryTree<E> {

    private Comparator<E> comparator;

    public BST(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public BST() {
        this(null);
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

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
    }
}

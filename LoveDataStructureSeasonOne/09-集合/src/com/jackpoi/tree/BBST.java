package com.jackpoi.tree;

import java.util.Comparator;

/**
 * @author beastars
 */
public class BBST<E> extends BST<E> {
    public BBST(Comparator<E> comparator) {
        super(comparator);
    }

    public BBST() {
        this(null);
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
        // 更新parent的父节点
        // 让parent的父节点成为子树的根节点，即parent的父节点指向原grand的父节点
        parent.parent = grand.parent; // parent.parent -> grand.parent

        // 判断原grand是左子树还是右子树，将原grand的父节点指向parent
        if (grand.isLeftChild()) {
            grand.parent.left = parent; // grand.parent.left -> parent
        } else if (grand.isRightChild()) {
            grand.parent.right = parent; // grand.parent.right -> parent
        } else {
            // grand为根节点root
            root = parent;
        }

        // 更新child的父节点
        if (child != null) {
            child.parent = grand;
        }

        // 更新grand的父节点
        grand.parent = parent;
    }

    protected void rotate(Node<E> r,
                          Node<E> b, Node<E> c,
                          Node<E> d,
                          Node<E> e, Node<E> f) {
        // 让d成为根节点
        d.parent = r.parent;
        if (r.isLeftChild()) {
            r.parent.left = d;
        } else if (r.isRightChild()) {
            r.parent.right = d;
        } else {
            root = d;
        }

        // b - c
        b.right = c;
        if (c != null) {
            c.parent = b;
        }

        // e - f
        f.left = e;
        if (e != null) {
            e.parent = f;
        }

        // b - d - f
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;
    }
}

package com.jackpoi.set;

import com.jackpoi.tree.BinaryTree;
import com.jackpoi.tree.RBTree;

import java.util.Comparator;

/**
 * @author beastars
 */
public class TreeSet<E> implements Set<E> {
    private RBTree<E> tree;

    public TreeSet(Comparator<E> comparator) {
        tree = new RBTree<>(comparator);
    }

    public TreeSet() {
        this(null);
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public boolean contains(E element) {
        return tree.contains(element);
    }

    @Override
    public void add(E element) {
        tree.add(element);
    }

    @Override
    public void remove(E element) {
        tree.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        tree.inorder(new BinaryTree.Visitor<E>() {
            @Override
            public boolean visitor(E element) {
                return visitor.visit(element);
            }
        });
    }
}

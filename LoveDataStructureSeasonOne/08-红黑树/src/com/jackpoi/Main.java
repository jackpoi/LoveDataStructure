package com.jackpoi;

import com.jackpoi.tree.AVLTree;
import com.jackpoi.tree.RBTree;
import com.jackpoi.printer.BinaryTrees;

import java.util.ArrayList;
import java.util.List;

/**
 * @author beastars
 */
public class Main {
    public static void main(String[] args) {
        test5();
    }

    static void test5() {
        Integer[] data = new Integer[]{
                55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50
        };

        RBTree<Integer> rb = new RBTree<>();
        for (Integer datum : data) {
            rb.add(datum);
        }

        BinaryTrees.println(rb);

        for (Integer datum : data) {
            rb.remove(datum);
            System.out.println("------------------------------");
            System.out.println("【" + datum + "】");
            BinaryTrees.println(rb);
        }
    }

    static void test4() {
        Integer[] data = new Integer[]{
                55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50
        };

        RBTree<Integer> rb = new RBTree<>();
        for (Integer datum : data) {
            rb.add(datum);
        }

        BinaryTrees.println(rb);
    }

    static void test3() {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            data.add((int) (Math.random() * 1000000));
        }

        long start = System.currentTimeMillis();

//        BST<Integer> bst = new BST<>();
//        for (int i = 0; i < data.size(); i++) {
//            bst.add(data.get(i));
//        }
//        for (int i = 0; i < data.size(); i++) {
//            bst.contains(data.get(i));
//        }
//        for (int i = 0; i < data.size(); i++) {
//            bst.remove(data.get(i));
//        }

        AVLTree<Integer> avl = new AVLTree<>();
        for (int i = 0; i < data.size(); i++) {
            avl.add(data.get(i));
        }
        for (int i = 0; i < data.size(); i++) {
            avl.contains(data.get(i));
        }
//        for (int i = 0; i < data.size(); i++) {
//            avl.remove(data.get(i));
//        }

        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }

    static void test2() {
        Integer[] data = new Integer[]{
                85, 19, 69, 3, 7, 99, 95, 2, 1, 70, 44, 58, 11, 21, 14, 93, 57, 4, 56
        };

        AVLTree<Integer> avl = new AVLTree<>();
        for (int i = 0; i < data.length; i++) {
            avl.add(data[i]);
        }

        for (int i = 0; i < data.length; i++) {
            avl.remove(data[i]);
            BinaryTrees.println(avl);
        }
    }

    static void test1() {
        Integer[] data = new Integer[]{
                85, 19, 69, 3, 7, 99, 95, 2, 1, 70, 44, 58, 11, 21, 14, 93, 57, 4, 56
        };

        AVLTree<Integer> avl = new AVLTree<>();
        for (int i = 0; i < data.length; i++) {
            avl.add(data[i]);
        }

        BinaryTrees.println(avl);
    }
}

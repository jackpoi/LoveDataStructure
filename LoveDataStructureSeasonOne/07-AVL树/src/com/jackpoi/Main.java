package com.jackpoi;

import com.jackpoi.printer.BinaryTrees;
import com.jackpoi.tree.AVLTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author beastars
 */
public class Main {
    public static void main(String[] args) {
        test2();
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
        BinaryTrees.println(avl);

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

package com.jackpoi;

import com.jackpoi.file.Files;
import com.jackpoi.printer.BinaryTrees;

import java.util.Comparator;

/**
 * @author beastars
 */
public class Main {
    public static void main(String[] args) {
        test5();
    }

    static void test5() {
        Integer[] data = new Integer[]{
                7, 4, 9, 2, 5, 8, 11, 3, 1, 13
        };

        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (Integer datum : data) {
            bst.add(datum);
        }

        BinaryTrees.println(bst);

        bst.remove(9);

        BinaryTrees.println(bst);
    }

    static void test4() {
        Integer[] data = new Integer[]{
                7, 4, 9, 2, 1
        };

        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (Integer datum : data) {
            bst.add(datum);
        }

        BinaryTrees.println(bst);
        System.out.println(bst.isComplete());
    }

    static void test3() {
        Integer[] data = new Integer[]{
                7, 4, 9, 2, 5, 8, 11, 3, 1
        };

        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (Integer datum : data) {
            bst.add(datum);
        }

        BinaryTrees.println(bst);
        System.out.println(bst.isComplete());
    }

    static void test2() {
        Integer[] data = new Integer[]{
                7, 4, 9, 2, 5, 8, 11, 3, 12, 1
        };

        BinarySearchTree<Integer> bst3 = new BinarySearchTree<>();
        for (Integer datum : data) {
            bst3.add(datum);
        }

        BinaryTrees.println(bst3);
//        bst3.preorderTraversal();
//        bst3.inorderTraversal();
//        bst3.postorderTraversal();
        bst3.inorder(new BinarySearchTree.Visitor<Integer>() {
            @Override
            public boolean visitor(Integer element) {
//                System.out.println(element);
                return element == 9;
            }
        });
        System.out.println(bst3.height());
        System.out.println(bst3.isComplete());
    }

    static void test1() {
        Integer[] data = new Integer[]{
                20, 4, 9, 2, 5, 8, 11, 3, 12, 1
        };

        BinarySearchTree<Integer> bst3 = new BinarySearchTree<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        for (int i = 1; i < 2; i++) {
            for (Integer datum : data) {
                bst3.add(datum * i);
            }
        }

        BinaryTrees.println(bst3);
        String s = BinaryTrees.printString(bst3);
        s += "\n\n";
        Files.writeToFile("E:/素材/图包/恋上数据结构与算法（第一季）/代码/06-二叉搜索树/src/com/mj/1.txt", s, true);

    }
}

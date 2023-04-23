package com.jackpoi;

import com.jackpoi.printer.BinaryTrees;
import com.jackpoi.tree.BST;

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

        BST<Integer> bst = new BST<>();

        BinaryTrees.println(bst);

        for (Integer datum : data) {
            bst.add(datum);
        }

        for (Integer datum : data) {
            bst.remove(datum);
            BinaryTrees.println(bst);
        }
    }
}

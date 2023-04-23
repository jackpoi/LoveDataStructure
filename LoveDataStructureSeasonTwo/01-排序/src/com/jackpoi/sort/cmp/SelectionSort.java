package com.jackpoi.sort.cmp;

import com.jackpoi.sort.Sort;

/**
 * @author beastars
 */
public class SelectionSort<T extends Comparable<T>> extends Sort<T> {

    @Override
    protected void sort() {
        int len = array.length;
        while (len > 1) {
            int index = 0;
            for (int i = 1; i < len; i++) {
//                if (array[index] <= array[i]) {
                if (cmp(index, i) < 0) {
                    index = i;
                }
            }
            swap(len - 1, index);
            len--;
        }
    }

    /*@Override
    protected void sort() {
        int len = array.length;
        while (len > 1) {
            int index = 0;
            for (int i = 1; i < len; i++) {
//                if (array[index] <= array[i]) {
                if (cmp(index, i) <= 0) {
                    index = i;
                }
            }
            swap(len - 1, index);
            len--;
        }
    }*/
}

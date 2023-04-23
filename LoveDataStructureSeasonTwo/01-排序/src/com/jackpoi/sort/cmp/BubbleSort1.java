package com.jackpoi.sort.cmp;

import com.jackpoi.sort.Sort;

/**
 * @author beastars
 */
public class BubbleSort1<T extends Comparable<T>> extends Sort<T> {
    @Override
    protected void sort() {
        int len = array.length;
        while (len > 1) {
            for (int i = 0; i < len - 1; i++) {
//                if (array[i] > array[i + 1]) {
                if (cmp(i, i + 1) > 0) {
                    swap(i, i + 1);
                }
            }
            len--;
        }
    }
}

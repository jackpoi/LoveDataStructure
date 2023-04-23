package com.jackpoi.sort.cmp;

import com.jackpoi.sort.Sort;

/**
 * @author beastars
 */
public class InsertionSort1<T extends Comparable<T>> extends Sort<T> {
    @Override
    protected void sort() {
        for (int begin = 1; begin < array.length; begin++) {
            int curr = begin;
            while (curr > 0 && cmp(curr, curr - 1) < 0) {
                swap(curr, curr - 1);
                curr--;
            }
        }
    }
}

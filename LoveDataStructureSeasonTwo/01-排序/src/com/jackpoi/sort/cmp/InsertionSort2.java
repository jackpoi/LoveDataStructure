package com.jackpoi.sort.cmp;

import com.jackpoi.sort.Sort;

/**
 * @author beastars
 */
public class InsertionSort2<T extends Comparable<T>> extends Sort<T> {
    @Override
    protected void sort() {
        for (int begin = 1; begin < array.length; begin++) {
            int curr = begin;
            T backup = array[curr];
            while (curr > 0 && cmp(backup, array[curr - 1]) < 0) {
                array[curr] = array[curr - 1];
                curr--;
            }
            array[curr] = backup;
        }
    }
}

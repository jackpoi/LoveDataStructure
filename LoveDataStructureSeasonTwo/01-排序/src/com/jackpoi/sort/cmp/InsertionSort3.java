package com.jackpoi.sort.cmp;

import com.jackpoi.sort.Sort;

/**
 * @author beastars
 */
public class InsertionSort3<T extends Comparable<T>> extends Sort<T> {
    @Override
    protected void sort() {
        for (int begin = 1; begin < array.length; begin++) {
            T backup = array[begin];
            int index = search(array, begin);
            for (int i = begin - 1; i >= index; i--) {
                // [index, begin) 向右挪动一个单位
                array[i + 1] = array[i];
            }
            array[index] = backup;
        }
    }

    /**
     * 使用二分搜索找到 index 位置的元素在有序数组中的待插入位置
     * [0, index) 的区间范围的数组已经排好序了
     */
    private int search(T[] array, int index) {
        T value = array[index];
        int begin = 0;
        int end = index;
        while (begin < end) {
            int mid = (begin + end) >> 1;
            if (cmp(value, array[mid]) < 0) {
                end = mid;
            } else {
                begin = mid + 1;
            }
        }
        return begin;
    }
}

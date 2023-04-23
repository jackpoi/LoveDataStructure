package com.jackpoi.sort.cmp;

import com.jackpoi.sort.Sort;

/**
 * @author beastars
 */
public class BubbleSort2<T extends Comparable<T>> extends Sort<T> {
    @Override
    protected void sort() {
        // 如果序列已经完全有序，可以提前终止冒泡排序
        int len = array.length;
        while (len > 1) {
            boolean sorted = true;
            for (int i = 0; i < len - 1; i++) {
                if (cmp(i, i + 1) > 0) {
                    swap(i, i + 1);
                    sorted = false;
                }
            }
            if (sorted)
                break;
            len--;
        }
    }
}

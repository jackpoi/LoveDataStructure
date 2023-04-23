package com.jackpoi.sort.cmp;

import com.jackpoi.sort.Sort;

/**
 * @author beastars
 */
public class BubbleSort3<T extends Comparable<T>> extends Sort<T> {
    @Override
    protected void sort() {
        // 如果序列尾部已经局部有序，可以记录最后1次交换的位置，减少比较次数
        int len = array.length;
        while (len > 1) {
            int sortIndex = 0;
            for (int i = 0; i < len - 1; i++) {
                if (cmp(i, i + 1) > 0) {
                    swap(i, i + 1);
                    sortIndex = i + 1;
                }
            }
            len = sortIndex;
        }
    }
}

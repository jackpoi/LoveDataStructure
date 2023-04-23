package com.jackpoi.sort;

/**
 * @author beastars
 */
public class CountingSort1 extends Sort<Integer> {
    @Override
    protected void sort() {
        // 找出最值
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        // 开辟内存空间，存储次数
        int[] count = new int[max + 1];
        for (int i = 0; i < array.length; i++) {
            count[array[i]]++;
        }

        // 遍历元素，放到有序数组中的合适位置
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            while (count[i]-- > 0) {
                array[index++] = i;
            }
        }
    }
}

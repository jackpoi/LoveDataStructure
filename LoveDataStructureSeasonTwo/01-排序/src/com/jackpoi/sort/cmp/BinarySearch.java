package com.jackpoi.sort.cmp;

/**
 * @author beastars
 */
public class BinarySearch {
    /**
     * 使用二分搜索找到对应的索引值
     */
    public static int indexOf(int[] array, int value) {
        if (array == null || array.length == 0)
            return -1;
        int begin = 0;
        int end = array.length;
        while (end > begin) {
            int mid = (end + begin) >> 1;
            if (value > array[mid]) {
                begin = mid + 1;
            } else if (value < array[mid]) {
                end = mid;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 使用二分搜索找到value在有序数组中的待插入位置
     */
    public static int search(int[] array, int value) {
        if (array == null || array.length == 0)
            return -1;
        int begin = 0;
        int end = array.length;
        while (begin < end) {
            int mid = (begin + end) >> 1;
            if (value < array[mid]) {
                end = mid;
            } else {
                begin = mid + 1;
            }
        }
        return begin;
    }
}

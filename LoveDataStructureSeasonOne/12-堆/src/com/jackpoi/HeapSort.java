package com.jackpoi;

import java.util.Arrays;

/**
 * @author beastars
 */
public class HeapSort {
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length == 0)
            return;

        int len = arr.length;
        buildHeap(arr, len);

        // 交换堆顶和当前末尾的节点，重置大顶堆
        for (int i = len - 1; i > 0; i--) {
            swap(arr, 0, i);
            len--;
            heapify(arr, 0, len);
        }
    }

    public static void buildHeap(int[] arr, int len) {
        for (int i = ((len >> 1) - 1); i >= 0; i--) {
            heapify(arr, i, len);
        }
    }

    private static void heapify(int[] arr, int index, int len) {
        // 先根据堆性质，找出它左右节点的索引
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        // 默认当前节点（父节点）是最大值。
        int largestIndex = index;
        if (left < len && arr[left] > arr[largestIndex]) {
            // 如果有左节点，并且左节点的值更大，更新最大值的索引
            largestIndex = left;
        }
        if (right < len && arr[right] > arr[largestIndex]) {
            // 如果有右节点，并且右节点的值更大，更新最大值的索引
            largestIndex = right;
        }

        if (largestIndex != index) {
            // 如果最大值不是当前非叶子节点的值，那么就把当前节点和最大值的子节点值互换
            swap(arr, index, largestIndex);
            // 因为互换之后，子节点的值变了，如果该子节点也有自己的子节点，仍需要再次调整。
            heapify(arr, largestIndex, len);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        int[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
        System.out.println(Arrays.toString(data));
        heapSort(data);
        System.out.println(Arrays.toString(data));
    }
}

package com.jackpoi.sort.cmp;

import com.jackpoi.sort.Sort;

/**
 * @author beastars
 */
public class HeapSort<T extends Comparable<T>> extends Sort<T> {
    private int heapSize;

    @Override
    protected void sort() {
        // 原地建堆
        heapSize = array.length;
        for (int i = (heapSize >> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }

        while (heapSize > 1) {
            // 交换堆顶元素和尾部元素
            swap(0, heapSize - 1);
            heapSize--;

            // 对0位置进行下滤，恢复堆的性质
            siftDown(0);
        }
    }

    private void siftDown(int index) {
        T element = array[index];
        int half = heapSize >> 1;
        while (index < half) { // index需要是非叶子节点
            // 默认是左边和父节点比较
            int childIndex = (index << 1) + 1;
            T child = array[childIndex];

            int rightIndex = childIndex + 1;
            // 如果右节点比左节点大
            if (rightIndex < heapSize && cmp(array[rightIndex], child) > 0) {
                child = array[rightIndex];
                childIndex = rightIndex;
            }

            // 如果大于等于子节点，跳出
            if (cmp(element, child) >= 0)
                break;

            array[index] = child;
            index = childIndex;
        }
        array[index] = element;
    }
}

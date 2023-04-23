package com.jackpoi;

import com.jackpoi.sort.*;
import com.jackpoi.sort.cmp.*;
import com.jackpoi.tools.Asserts;
import com.jackpoi.tools.Integers;

import java.util.Arrays;

/**
 * @author beastars
 */
@SuppressWarnings({"rawtypes"})
public class Main {
    public static void main(String[] args) {
        Integer[] array = Integers.random(5000, 1, 300000);

//        int[] array = {-1, 1, 3, 5, 8, 9, 17, 24, 35};
//        System.out.println(BinarySearch.search(array, 3));
//        System.out.println(BinarySearch.search(array, 8));
//        System.out.println(BinarySearch.search(array, 10));
//        System.out.println(BinarySearch.search(array, 100));
//        System.out.println(BinarySearch.search(array, -100));

//        Integers.println(array);
//        Integer[] array = new Integer[100000];
//        for (int i = 0; i < 100000; i++) {
//            array[i] = 1;
//        }

//        Integer[] array2 = Integers.copy(array);
//        Integer[] array3 = Integers.copy(array);

        testSorts(array,
//                new BubbleSort1<>(),
//                new BubbleSort2<>(),
//                new InsertionSort1(),
//                new InsertionSort2(),
//                new BubbleSort3<>(),
//                new SelectionSort<>(),
//                new HeapSort<>(),
//                new InsertionSort3(),
                new MergeSort()
//                new QuickSort(),
//                new ShellSort(),
//                new CountingSort1(),
//                new CountingSort2(),
//                new RadixSort()
        );

//        testSorts(array, new InsertionSort());
    }

    static void testSorts(Integer[] array, Sort... sorts) {
        for (Sort sort : sorts) {
            Integer[] newArray = Integers.copy(array);
            sort.sort(newArray);
//            Integers.println(newArray);
            Asserts.test(Integers.isAscOrder(newArray));
        }

        Arrays.sort(sorts);

        for (Sort sort : sorts) {
            System.out.println(sort);
        }
    }

    private static void selectionSort(Integer[] array) {
        int len = array.length;
        while (len > 1) {
            int index = 0;
            for (int i = 1; i < len; i++) {
                if (array[index] <= array[i]) {
                    index = i;
                }
            }
            swap(array, len - 1, index);
            len--;
        }
    }

    private static void bubbleSort1(Integer[] array) {
        int len = array.length;
        while (len > 1) {
            for (int i = 0; i < len - 1; i++) {
                if (array[i] > array[i + 1]) {
                    swap(array, i, i + 1);
                }
            }
            len--;
        }
    }

    private static void bubbleSort2(Integer[] array) {
        // 如果序列已经完全有序，可以提前终止冒泡排序
        int len = array.length;
        while (len > 1) {
            boolean sorted = true;
            for (int i = 0; i < len - 1; i++) {
                if (array[i] > array[i + 1]) {
                    swap(array, i, i + 1);
                    sorted = false;
                }
            }
            if (sorted)
                break;
            len--;
        }
    }

    private static void bubbleSort3(Integer[] array) {
        // 如果序列尾部已经局部有序，可以记录最后1次交换的位置，减少比较次数
        int len = array.length;
        while (len > 1) {
            int sortIndex = 0;
            for (int i = 0; i < len - 1; i++) {
                if (array[i] > array[i + 1]) {
                    swap(array, i, i + 1);
                    sortIndex = i + 1;
                }
            }
            len = sortIndex;
        }
    }

    private static void swap(Integer[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}

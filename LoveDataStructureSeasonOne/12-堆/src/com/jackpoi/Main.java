package com.jackpoi;

import com.jackpoi.heap.BinaryHeap;
import com.jackpoi.printer.BinaryTrees;

import java.util.Comparator;

/**
 * @author beastars
 */
public class Main {
    public static void main(String[] args) {
        test2();
//        test3();
//        topK();
    }

    static void topK() {
        BinaryHeap<Integer> heap = new BinaryHeap<>((o1, o2) -> o2 - o1);

        Integer[] data = {51, 30, 39, 92, 74, 25, 16, 93,
                91, 19, 54, 47, 73, 62, 76, 63, 35, 18,
                90, 6, 65, 49, 3, 26, 61, 21, 48};

        // 获取数据中前 k 个最大的数
        int k = 5;
        for (int i = 0; i < data.length; i++) {
            if (i < k) {
                heap.add(data[i]);
            } else if (data[i] > heap.get()) {
                heap.replace(data[i]);
            }
        }

        BinaryTrees.println(heap);
    }

    static void test3() {
        Integer[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
        BinaryHeap<Integer> heap = new BinaryHeap<>(data, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        BinaryTrees.println(heap);

        data[0] = 10;
        data[1] = 20;
        BinaryTrees.println(heap);
    }

    static void test2() {
        Integer[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
        BinaryHeap<Integer> heap = new BinaryHeap<>(data);
        BinaryTrees.println(heap);

        data[0] = 10;
        data[1] = 20;
        BinaryTrees.println(heap);
    }

    static void test1() {
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.add(68);
        heap.add(43);
        heap.add(50);
        heap.add(38);
        heap.add(78);
        heap.add(10);
        heap.add(20);
        heap.add(160);
        heap.add(90);
        heap.add(72);
        heap.add(90999);
//        for (int i = 0; i < 9999; i++) {
//            heap.add(i);
//        }

        BinaryTrees.println(heap);
        heap.remove();
        heap.replace(666);
        BinaryTrees.println(heap);
//        System.out.println(heap.get());
    }
}

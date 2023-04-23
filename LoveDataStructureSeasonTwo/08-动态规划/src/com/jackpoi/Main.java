package com.jackpoi;

import java.util.Arrays;

/**
 * @author beastars
 */
public class Main {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 13, 5, 29, 5, 10};

        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int index = search(arr, arr[i], i);
            for (int j = i - 1; j >= index; j--) {
                arr[j + 1] = arr[j];
            }
            arr[index] = tmp;
        }

        System.out.println(Arrays.toString(arr));
    }

    private static int search(int[] arr, int num, int end) {
        int begin = 0;

        while (begin < end) {
            int mid = (begin + end) >> 1;
            if (num < arr[mid]) {
                end = mid;
            } else {
                begin = mid + 1;
            }
        }

        return begin;
    }
}

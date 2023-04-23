package com.jackpoi.sort.cmp;

import com.jackpoi.sort.Sort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author beastars
 */
public class ShellSort<T extends Comparable<T>> extends Sort<T> {
    @Override
    protected void sort() {
        List<Integer> stepSequence = sedgewickStepSequence();
        for (Integer step : stepSequence) {
            sort(step);
        }
    }

    /**
     * 分成 step 列进行排序
     */
    private void sort(int step) {
        for (int col = 0; col < step; col++) {
            for (int begin = col + step; begin < array.length; begin += step) {
                int curr = begin;
                T backup = array[begin];
                while (curr > col && cmp(backup, array[curr - step]) < 0) {
                    array[curr] = array[curr - step];
                    curr -= step;
                }
                array[curr] = backup;
            }
        }
    }

    private List<Integer> shellStepSequence() {
        List<Integer> stepSequence = new ArrayList<>();

        int step = array.length;
        while ((step = step >> 1) > 0) {
            stepSequence.add(step);
        }

        return stepSequence;
    }

    private List<Integer> sedgewickStepSequence() {
        List<Integer> stepSequence = new LinkedList<>();
        int k = 0, step = 0;
        while (true) {
            if (k % 2 == 0) {
                int pow = (int) Math.pow(2, k >> 1);
                step = 1 + 9 * (pow * pow - pow);
            } else {
                int pow1 = (int) Math.pow(2, (k - 1) >> 1);
                int pow2 = (int) Math.pow(2, (k + 1) >> 1);
                step = 1 + 8 * pow1 * pow2 - 6 * pow2;
            }
            if (step >= array.length) break;
            stepSequence.add(0, step);
            k++;
        }
        return stepSequence;
    }
}

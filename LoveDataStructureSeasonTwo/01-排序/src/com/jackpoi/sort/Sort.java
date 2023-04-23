package com.jackpoi.sort;

import com.jackpoi.Student;
import com.jackpoi.sort.cmp.SelectionSort;
import com.jackpoi.sort.cmp.ShellSort;

import java.text.DecimalFormat;

/**
 * @author beastars
 */
public abstract class Sort<T extends Comparable<T>> implements Comparable<Sort<T>> {
    protected T[] array;
    private int cmpCount;
    private int swapCount;
    private long time;
    private DecimalFormat fmt = new DecimalFormat("#.00");

    public void sort(T[] array) {
        if (array == null || array.length < 2)
            return;

        this.array = array;

        long begin = System.currentTimeMillis();
        sort();
        long end = System.currentTimeMillis();
        time = end - begin;
    }

    protected abstract void sort();

    @Override
    public int compareTo(Sort<T> o) {
        int result = (int) (time - o.time);
        if (result != 0)
            return result;

        result = cmpCount - o.cmpCount;
        if (result != 0)
            return result;

        return swapCount - o.swapCount;
    }

    /**
     * 交换
     */
    protected void swap(int i, int j) {
        swapCount++;
        T tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    /**
     * 比较
     *
     * @return 返回值等于0，表示array[i] == array[j]，
     * 大于0表示array[i]较大，
     * 小于0表示array[j]较大
     */
    protected int cmp(int i, int j) {
        cmpCount++;
        return array[i].compareTo(array[j]);
    }

    protected int cmp(T v1, T v2) {
        cmpCount++;
        return v1.compareTo(v2);
    }

    @Override
    public String toString() {
        String timeStr = "耗时：" + (time / 1000.0) + "s(" + time + "ms)";
        String compareCountStr = "比较：" + numberString(cmpCount);
        String swapCountStr = "交换：" + numberString(swapCount);
        String stableStr = "稳定性：" + isStable();
        return "【" + getClass().getSimpleName() + "】\n"
                + stableStr + " \t"
                + timeStr + " \t"
                + compareCountStr + "\t "
                + swapCountStr + "\n"
                + "------------------------------------------------------------------";
    }

    private String numberString(int number) {
        if (number < 10000) return "" + number;

        if (number < 100000000) return fmt.format(number / 10000.0) + "万";
        return fmt.format(number / 100000000.0) + "亿";
    }

    private boolean isStable() {
        if (this instanceof ShellSort)
            return false;
        if (this instanceof SelectionSort)
            return false;
        if (this instanceof CountingSort1)
            return false;
        if (this instanceof CountingSort2)
            return true;
        if (this instanceof RadixSort)
            return true;

        Student[] students = new Student[20];
        for (int i = 0; i < 20; i++) {
            students[i] = new Student(i * 10, 18);
        }

        sort((T[]) students);

        for (int i = 1; i < students.length; i++) {
            int score = students[i].score;
            int preScore = students[i - 1].score;
            if (score - preScore != 10)
                return false;
        }

        return true;
    }
}

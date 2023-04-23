package com.jackpoi;

/**
 * @author beastars
 */
public class Fib {
    public static void main(String[] args) {
        Fib fib = new Fib();
        int n = 45;
//        Times.test("fib0", () -> System.out.println(fib.fib0(n)));
        Times.test("fib1", () -> System.out.println(fib.fib1(n)));
        Times.test("fib2", () -> System.out.println(fib.fib2(n)));
        Times.test("fib3", () -> System.out.println(fib.fib3(n)));
        Times.test("fib4", () -> System.out.println(fib.fib4(n)));
        Times.test("fib", () -> System.out.println(fib.fib(n)));
    }

    /**
     * 尾递归
     */
    public int fib(int n) {
        return fib(n, 1, 1);
    }

    private int fib(int n, int first, int second) {
        if (n <= 1)
            return first;
        return fib(n - 1, second, first + second);
    }

    /**
     * 时间复杂度：O(2n)
     * 空间复杂度：O(n)
     */
    public int fib0(int n) {
        if (n <= 2)
            return 1;
        return fib0(n - 1) + fib0(n - 2);
    }

    /**
     * fib优化1 – 记忆化
     * 用数组存放计算过的结果，避免重复计算
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int fib1(int n) {
        if (n <= 2)
            return 1;
        int[] arr = new int[n + 1];
        arr[1] = arr[2] = 1;
        return fib1(n, arr);
    }

    private int fib1(int n, int[] arr) {
        if (arr[n] == 0) {
            arr[n] = fib1(n - 1, arr) + fib1(n - 2, arr);
        }
        return arr[n];
    }

    /**
     * 去除递归调用，自底向上
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int fib2(int n) {
        if (n <= 2)
            return 1;
        int[] arr = new int[n + 1];
        arr[1] = arr[2] = 1;
        for (int i = 3; i <= n; i++) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }
        return arr[n];
    }

    /**
     * 由于每次运算只需要用到数组中的 2 个元素，所以可以使用 滚动数组 来优化
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public int fib3(int n) {
        if (n <= 2)
            return 1;
        int[] curr = new int[2];
        curr[0] = curr[1] = 1;
        for (int i = 2; i < n; i++) {
            int tmp = curr[1];
            curr[1] = curr[0] + curr[1];
            curr[0] = tmp;
        }
        return curr[1];
    }

    public int fib4(int n) {
        if (n <= 2)
            return 1;
        int first = 1;
        int second = 1;
        for (int i = 2; i < n; i++) {
            second = first + second;
            first = second - first;
        }
        return second;
    }
}

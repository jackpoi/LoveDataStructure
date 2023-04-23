package com.jackpoi;

public class Main {
    public static void main(String[] args) {
        System.out.println(fib2(10));
    }

    public static int fib1(int n) {
        if (n <= 1) {
            return n;
        }
        return fib1(n - 1) + fib1(n - 2);
    }

    public static int fib2(int n) {
        if (n <= 1)
            return n;

        int first = 0;
        int second = 1;
        for (int i = 0; i < n - 1; i++) {
            second += first;
            first = second - first;
        }
        return second;
    }
}

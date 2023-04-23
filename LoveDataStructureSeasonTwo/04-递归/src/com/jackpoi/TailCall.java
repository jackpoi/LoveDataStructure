package com.jackpoi;

/**
 * @author beastars
 */
public class TailCall {
    public static void main(String[] args) {
        System.out.println(factorial(4));
    }

    /**
     * 尾递归
     */
    static int factorial(int n) {
        return factorial(n, 1);
    }

    static int factorial(int n, int result) {
        if (n <= 1)
            return result;
        return factorial(n - 1, result * n);
    }

//    static int factorial(int n) {
//        if (n <= 2)
//            return n;
//        return n * factorial(n - 1);
//    }
}

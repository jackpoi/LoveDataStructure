package com.jackpoi;

import java.util.Stack;

/**
 * @author beastars
 */
public class Main {
    public static void main(String[] args) {
        log2(10);
    }

    static class Frame {
        int n;
        int v;

        Frame(int n, int v) {
            this.n = n;
            this.v = v;
        }
    }

    static void log2(int n) {
        Stack<Frame> frames = new Stack<>();
        while (n > 0) {
            frames.push(new Frame(n, n + 10));
            n--;
        }
        while (!frames.isEmpty()) {
            Frame frame = frames.pop();
            System.out.println(frame.v);
        }
    }

    static void log(int n) {
        if (n < 1) return;
        log(n - 1);
        int v = n + 10;
        System.out.println(v);
    }
}

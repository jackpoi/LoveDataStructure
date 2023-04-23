package com.jackpoi;

/**
 * @author beastars
 */
public class KMP {
    public static int indexOf(String text, String pattern) {
        if (text == null || pattern == null)
            return -1;
        int tLen = text.length();
        int pLen = pattern.length();
        if (tLen == 0 || pLen == 0 || tLen - pLen < 0)
            return -1;

        int[] next = next(pattern);
        int pi = 0, ti = 0;
        int tMax = tLen - pLen; // 匹配时最长经过的长度

        while (pi < pLen && ti - pi <= tMax) {
            if (pi < 0 || pattern.charAt(pi) == text.charAt(ti)) {
                pi++;
                ti++;
            } else {
                pi = next[pi];
            }
        }

        return pi == pLen ? ti - pi : -1;
    }

    private static int[] next(String pattern) {
        int[] next = new int[pattern.length()];
        next[0] = -1;
        int n = next[0];
        int i = 0;
        int iMax = pattern.length() - 1;

        while (i < iMax) {
            if (n < 0 || pattern.charAt(i) == pattern.charAt(n)) {
//                next[++i] = (n = n + 1);
                i++;
                n++;
                if (pattern.charAt(i) == pattern.charAt(n)) {
                    next[i] = next[n];
                } else {
                    next[i] = n;
                }
            } else {
                n = next[n];
            }
        }

        return next;
    }
}

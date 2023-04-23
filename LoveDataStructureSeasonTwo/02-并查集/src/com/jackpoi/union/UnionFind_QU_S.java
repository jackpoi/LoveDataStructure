package com.jackpoi.union;

/**
 * Quick Union - 基于 size 的优化
 *
 * @author beastars
 */
public class UnionFind_QU_S extends UnionFind {
    private int[] sizes;

    public UnionFind_QU_S(int capacity) {
        super(capacity);

        sizes = new int[capacity];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = 1;
        }
    }

    @Override
    public int find(int v) {
        rangeCheck(v);

        while (v != parents[v]) {
            v = parents[v];
        }

        return v;
    }

    /**
     * 将 v1 的根节点嫁接到 v2 的根节点上
     */
    @Override
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2)
            return;

        if (sizes[p1] < sizes[p2]) {
            parents[p1] = p2; // p1 指向 p2
            sizes[p2] += sizes[p1];
        } else {
            parents[p2] = p1; // p2 指向 p1
            sizes[p1] += sizes[p2];
        }
    }
}

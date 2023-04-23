package com.jackpoi.union;

/**
 * Quick Union - 基于 rank 优化 - 基于 路径减半（Path Halving） 优化
 * 路径减半：使路径上每隔一个节点就指向其祖父节点（parent的parent）
 *
 * @author beastars
 */
public class UnionFind_QU_R_PH extends UnionFind_QU_R {

    public UnionFind_QU_R_PH(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);

        while (v != parents[v]) {
            parents[v] = parents[parents[v]];
            v = parents[v];
        }

        return v;
    }
}

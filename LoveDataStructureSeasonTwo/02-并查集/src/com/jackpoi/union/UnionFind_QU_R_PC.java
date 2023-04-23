package com.jackpoi.union;

/**
 * Quick Union - 基于 rank 优化 - 基于 路径压缩（Path Compression） 优化
 * 在find时使路径上的所有节点都指向根节点，从而降低树的高度
 *
 * @author beastars
 */
public class UnionFind_QU_R_PC extends UnionFind_QU_R {

    public UnionFind_QU_R_PC(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        rangeCheck(v);

        if (parents[v] != v) {
            parents[v] = find(parents[v]);
        }

        return parents[v];
    }
}

package com.jackpoi.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author beastars
 */
public abstract class Graph<V, E> {
    protected WeightManager<E> weightManager;

    public Graph() {
    }

    public Graph(WeightManager<E> weightManager) {
        this.weightManager = weightManager;
    }

    public abstract int edgeSize();

    public abstract int verticesSize();

    public abstract void addVertex(V v);

    public abstract void addEdge(V from, V to);

    public abstract void addEdge(V from, V to, E weight);

    public abstract void removeVertex(V v);

    public abstract void removeEdge(V from, V to);

    /**
     * 最小生成树
     */
    public abstract Set<EdgeInfo<V, E>> mst();

    /**
     * 拓扑排序
     */
    public abstract List<V> topologicalSort();

    /**
     * 单源最小路径
     * 单源：求出以一个顶点为源点的到其他顶点的最短路径
     */
    public abstract Map<V, PathInfo<V, E>> shortestPath(V begin);
//    public abstract Map<V, E> shortestPath(V begin);

    /**
     * 多源最小路径
     * 多源：求出以每个顶点为源点的到其他顶点的最短路径
     */
    public abstract Map<V, Map<V, PathInfo<V, E>>> shortestPath();

    public abstract void bfs(V begin, VertexVisitor<V> visitor);

    public abstract void dfs(V begin, VertexVisitor<V> visitor);

    public interface VertexVisitor<V> {
        boolean visit(V v);
    }

    public interface WeightManager<E> {
        int compare(E w1, E w2);

        E add(E w1, E w2);

        E zero();
    }

    public static class PathInfo<V, E> {
        protected E weight;
        protected List<EdgeInfo<V, E>> edgeInfos = new LinkedList<>();

        public PathInfo() {
        }

        public PathInfo(E weight) {
            this.weight = weight;
        }

        public E getWeight() {
            return weight;
        }

        public void setWeight(E weight) {
            this.weight = weight;
        }

        public List<EdgeInfo<V, E>> getEdgeInfos() {
            return edgeInfos;
        }

        public void setEdgeInfos(List<EdgeInfo<V, E>> edgeInfos) {
            this.edgeInfos = edgeInfos;
        }

        @Override
        public String toString() {
            return "PathInfo{" +
                    "weight=" + weight +
                    ", edgeInfos=" + edgeInfos +
                    '}';
        }
    }

    public static class EdgeInfo<V, E> {
        private V form;
        private V to;
        private E weight;

        public EdgeInfo(V form, V to, E weight) {
            this.form = form;
            this.to = to;
            this.weight = weight;
        }

        public V getForm() {
            return form;
        }

        public void setForm(V form) {
            this.form = form;
        }

        public V getTo() {
            return to;
        }

        public void setTo(V to) {
            this.to = to;
        }

        public E getWeight() {
            return weight;
        }

        public void setWeight(E weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "EdgeInfo{" +
                    "form=" + form +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }
}

package com.jackpoi;

import com.jackpoi.graph.Graph;
import com.jackpoi.graph.ListGraph;

import java.util.Map;
import java.util.Set;

/**
 * @author beastars
 */
public class Main {
    static Graph.WeightManager<Double> weightManager = new Graph.WeightManager<Double>() {
        @Override
        public int compare(Double w1, Double w2) {
            return w1.compareTo(w2);
        }

        @Override
        public Double add(Double w1, Double w2) {
            return w1 + w2;
        }

        @Override
        public Double zero() {
            return 0.0;
        }
    };

    public static void main(String[] args) {
        testMultiSp();
    }

    static void testMultiSp() {
        Graph<Object, Double> graph = directedGraph(Data.NEGATIVE_WEIGHT1);
        Map<Object, Map<Object, Graph.PathInfo<Object, Double>>> paths = graph.shortestPath();
        paths.forEach((from, path) -> {
            System.out.println(from + "--------------------");
            path.forEach((to, info) -> {
                System.out.println(to + "_" + info);
            });
        });
    }

    static void testSp() {
        Graph<Object, Double> graph = directedGraph(Data.NEGATIVE_WEIGHT1);
        Map<Object, Graph.PathInfo<Object, Double>> path = graph.shortestPath("A");
        path.forEach((obj, info) -> {
            System.out.println(obj + "_" + info);
        });
    }

    static void testMst() {
        Graph<Object, Double> graph = undirectedGraph(Data.MST_01);
        Set<Graph.EdgeInfo<Object, Double>> mst = graph.mst();
        for (Graph.EdgeInfo<Object, Double> info : mst) {
            System.out.println(info);
        }
    }

    static void testTopo() {
        Graph<Object, Double> graph = directedGraph(Data.TOPO);
        System.out.println(graph.topologicalSort());
    }

    static void testDfs() {
        Graph<Object, Double> graph = directedGraph(Data.DFS_02);
        graph.dfs("a", o -> {
            System.out.println(o + "^_^");
            return o.equals("e");
        });
    }

    static void testBfs() {
        Graph<Object, Double> graph = directedGraph(Data.BFS_02);
        graph.bfs(0, null);
    }

    /**
     * 有向图
     */
    private static Graph<Object, Double> directedGraph(Object[][] data) {
        Graph<Object, Double> graph = new ListGraph<>(weightManager);
        for (Object[] edge : data) {
            if (edge.length == 1) {
                graph.addVertex(edge[0]);
            } else if (edge.length == 2) {
                graph.addEdge(edge[0], edge[1]);
            } else if (edge.length == 3) {
                double weight = Double.parseDouble(edge[2].toString());
                graph.addEdge(edge[0], edge[1], weight);
            }
        }
        return graph;
    }

    /**
     * 无向图
     */
    private static Graph<Object, Double> undirectedGraph(Object[][] data) {
        Graph<Object, Double> graph = new ListGraph<>(weightManager);
        for (Object[] edge : data) {
            if (edge.length == 1) {
                graph.addVertex(edge[0]);
            } else if (edge.length == 2) {
                graph.addEdge(edge[0], edge[1]);
                graph.addEdge(edge[1], edge[0]);
            } else if (edge.length == 3) {
                double weight = Double.parseDouble(edge[2].toString());
                graph.addEdge(edge[0], edge[1], weight);
                graph.addEdge(edge[1], edge[0], weight);
            }
        }
        return graph;
    }
}

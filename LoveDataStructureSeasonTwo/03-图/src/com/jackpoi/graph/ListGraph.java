package com.jackpoi.graph;

import com.jackpoi.MinHeap;
import com.jackpoi.UnionFind;

import java.util.*;

/**
 * @author beastars
 */
public class ListGraph<V, E> extends Graph<V, E> {
    private Map<V, Vertex<V, E>> vertices = new HashMap<>();
    private Set<Edge<V, E>> edges = new HashSet<>();
    private Comparator<Edge<V, E>> edgeComparator =
            (e1, e2) -> weightManager.compare(e1.weight, e2.weight);

    public ListGraph() {
    }

    public ListGraph(WeightManager<E> weightManager) {
        super(weightManager);
    }

    public void print() {
        System.out.println("[顶点]-------------------");
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            System.out.println(v);
            System.out.println("out-----------");
            System.out.println(vertex.outEdges);
            System.out.println("in-----------");
            System.out.println(vertex.inEdges);
        });

        System.out.println("[边]-------------------");
        edges.forEach(System.out::println);
    }

    @Override
    public int edgeSize() {
        return edges.size();
    }

    @Override
    public int verticesSize() {
        return vertices.size();
    }

    @Override
    public void addVertex(V v) {
        if (vertices.containsKey(v))
            return;
        vertices.put(v, new Vertex<>(v));
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    @Override
    public void addEdge(V from, V to, E weight) {
        // 判断 from to 顶点是否存在
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) {
            fromVertex = new Vertex<>(from);
            vertices.put(from, fromVertex);
        }

        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) {
            toVertex = new Vertex<>(to);
            vertices.put(to, toVertex);
        }

        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        edge.weight = weight;

        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }

        fromVertex.outEdges.add(edge);
        toVertex.inEdges.add(edge);
        edges.add(edge);
    }

    @Override
    public void removeVertex(V v) {
        Vertex<V, E> vertex = vertices.remove(v);
        if (vertex == null)
            return;

        Iterator<Edge<V, E>> inIterator = vertex.inEdges.iterator();
        while (inIterator.hasNext()) {
            Edge<V, E> edge = inIterator.next();
            edge.from.outEdges.remove(edge);
            inIterator.remove();
            edges.remove(edge);
        }

        Iterator<Edge<V, E>> outIterator = vertex.outEdges.iterator();
        while (outIterator.hasNext()) {
            Edge<V, E> edge = outIterator.next();
            edge.to.inEdges.remove(edge);
            outIterator.remove();
            edges.remove(edge);
        }
    }

    @Override
    public void removeEdge(V from, V to) {
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null)
            return;
        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null)
            return;
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
    }

    @Override
    public List<V> topologicalSort() { // 卡恩算法
        List<V> list = new ArrayList<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        Map<Vertex<V, E>, Integer> map = new HashMap<>(); // 存储顶点的入度

        vertices.forEach((v, vertex) -> {
            int in = vertex.inEdges.size();
            if (in == 0) {
                queue.offer(vertex); // 入度为0，存到队列
            } else {
                map.put(vertex, in); // 入度不为零，存入map
            }
        });

        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            list.add(vertex.value); // 入度为0，弹出结果，放到list
            for (Edge<V, E> outEdge : vertex.outEdges) {
                // 将出去的边对应的点的度数减一
                int toIn = map.get(outEdge.to) - 1;
                if (toIn == 0) {
                    queue.offer(outEdge.to);
                } else {
                    map.put(outEdge.to, toIn);
                }
            }
        }

        return list;
    }

    /**
     * 多元最短路径
     *
     * @return 每个顶点到其他顶点的最短路径
     */
    @Override
    public Map<V, Map<V, PathInfo<V, E>>> shortestPath() {
        Map<V, Map<V, PathInfo<V, E>>> paths = new HashMap<>();

        // 初始化map
        for (Edge<V, E> edge : edges) {
            Map<V, PathInfo<V, E>> map = paths.get(edge.from.value);
            if (map == null) {
                map = new HashMap<>();
                paths.put(edge.from.value, map); // 将起点信息加入
            }
            PathInfo<V, E> pathInfo = new PathInfo<>(edge.weight);
            pathInfo.edgeInfos.add(edge.info());
            map.put(edge.to.value, pathInfo); // 将终点信息加入
        }

        vertices.forEach((v2, vertex2) -> {
            vertices.forEach((v1, vertex1) -> {
                vertices.forEach((v3, vertex3) -> {
                    if (v1.equals(v2) || v2.equals(v3) || v3.equals(v1))
                        return;
                    // v1 -> v2
                    PathInfo<V, E> pathInfo12 = getPathInfo(v1, v2, paths);
                    if (pathInfo12 == null)
                        return;
                    // v2 -> v3
                    PathInfo<V, E> pathInfo23 = getPathInfo(v2, v3, paths);
                    if (pathInfo23 == null)
                        return;
                    // v1 -> v3
                    PathInfo<V, E> pathInfo13 = getPathInfo(v1, v3, paths);

                    E newWeight = weightManager.add(pathInfo12.weight, pathInfo23.weight);
                    if (pathInfo13 != null && weightManager.compare(newWeight, pathInfo13.weight) >= 0)
                        return;
                    if (pathInfo13 == null) {
                        pathInfo13 = new PathInfo<>();
                        paths.get(v1).put(v3, pathInfo13);
                    } else {
                        pathInfo13.edgeInfos.clear();
                    }

                    pathInfo13.weight = newWeight;
                    pathInfo13.edgeInfos.addAll(pathInfo12.edgeInfos);
                    pathInfo13.edgeInfos.addAll(pathInfo23.edgeInfos);
                });
            });
        });

        return paths;
    }

    private PathInfo<V, E> getPathInfo(V from, V to, Map<V, Map<V, PathInfo<V, E>>> paths) {
        Map<V, PathInfo<V, E>> map = paths.get(from);
        return map == null ? null : map.get(to);
    }

    /**
     * 单源最短路径
     *
     * @param begin 以begin为顶点
     * @return 以begin为顶点到其他顶点的最短路径
     */
    @Override
    public Map<V, PathInfo<V, E>> shortestPath(V begin) {
//        return dijkstra(begin);
        return bellmanFord(begin);
    }

    /**
     * bellmanFord求最短路径,可以有负权边,还可以检测出负权环
     *
     * @param begin 源点
     */
    private Map<V, PathInfo<V, E>> bellmanFord(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null)
            return null;

        Map<V, PathInfo<V, E>> selectedPaths = new HashMap<>();
        PathInfo<V, E> beginPath = new PathInfo<>(weightManager.zero());
        selectedPaths.put(begin, beginPath);

        int count = vertices.size() - 1;
        for (int i = 0; i < count; i++) { // v - 1次
            for (Edge<V, E> edge : edges) {
                PathInfo<V, E> fromPath = selectedPaths.get(edge.from.value);
                if (fromPath == null)
                    continue;
                relax(edge, fromPath, selectedPaths);
            }
        }

        for (Edge<V, E> edge : edges) {
            PathInfo<V, E> fromPath = selectedPaths.get(edge.from.value);
            if (fromPath == null)
                continue;
            if (relax(edge, fromPath, selectedPaths)) {
                System.out.println("有负权环");
                return null;
            }
        }

        selectedPaths.remove(begin);
        return selectedPaths;
    }

    /**
     * dijkstra求最短路径,不可以有负权边
     *
     * @param begin 源点
     */
    private Map<V, PathInfo<V, E>> dijkstra(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null)
            return null;

        Map<V, PathInfo<V, E>> selectedPaths = new HashMap<>();
        Map<Vertex<V, E>, PathInfo<V, E>> paths = new HashMap<>();
        // 初始化paths
        paths.put(beginVertex, new PathInfo<>(weightManager.zero()));
        /*for (Edge<V, E> outEdge : beginVertex.outEdges) {
            PathInfo<V, E> pathInfo = new PathInfo<>();
            pathInfo.weight = outEdge.weight;
            pathInfo.edgeInfos.add(outEdge.info());
            paths.put(outEdge.to, pathInfo);
        }*/

        while (!paths.isEmpty()) {
            Map.Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = getMinPath(paths);
            Vertex<V, E> minVertex = minEntry.getKey();
            selectedPaths.put(minVertex.value, minEntry.getValue());
            paths.remove(minVertex);
            // 对这个minVertex的outEdges进行松弛操作
            for (Edge<V, E> outEdge : minVertex.outEdges) {
                // 如果outEdge.to已经找到了最短路径，就跳过，不需要在进行松弛操作
                if (selectedPaths.containsKey(outEdge.to.value))
                    continue;
                relaxForDijkstra(outEdge, minEntry.getValue(), paths);
            }
        }

        selectedPaths.remove(beginVertex.value);
        return selectedPaths;
    }

    /**
     * 松弛操作（Relaxation）：更新2个顶点之间的最短路径
     * 这里一般是指：更新源点到另一个点的最短路径
     * 松弛操作的意义：尝试找出更短的最短路径
     *
     * @param outEdge 需要进行松弛的边
     * @param minPath 当前选择的最小边的路径信息
     * @param paths   存放其他点（对于dijkstra来说，是还没有找到最短路径的点）的最短路径信息
     */
    private void relaxForDijkstra(Edge<V, E> outEdge, PathInfo<V, E> minPath, Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        // 新的可选择的最短的路径：beginVertex到outEdge.from的最短路径 + outEdge.weight
        E newWeight = weightManager.add(minPath.weight, outEdge.weight);
        // 旧的最短路径：beginVertex到outEdge.to的路径
        PathInfo<V, E> oldPathInfo = paths.get(outEdge.to);
        if (oldPathInfo != null && weightManager.compare(newWeight, oldPathInfo.weight) >= 0)
            return;

        if (oldPathInfo == null) {
            oldPathInfo = new PathInfo<>();
            paths.put(outEdge.to, oldPathInfo);
        } else {
            oldPathInfo.edgeInfos.clear();
        }

        oldPathInfo.weight = newWeight;
        // 更新路径信息，如果进行到此步，说明有了更小权值的路径，也就是这次找到的最小路径和当前边，将其路径更新
        oldPathInfo.edgeInfos.addAll(minPath.edgeInfos);
        oldPathInfo.edgeInfos.add(outEdge.info());
    }

    /**
     * @param outEdge 需要进行松弛的边
     * @param minPath 当前选择的最小边的路径信息
     * @param paths   存放其他点（对于dijkstra来说，是还没有找到最短路径的点）的最短路径信息
     */
    private boolean relax(Edge<V, E> outEdge, PathInfo<V, E> minPath, Map<V, PathInfo<V, E>> paths) {
        // 新的可选择的最短的路径：beginVertex到outEdge.from的最短路径 + outEdge.weight
        E newWeight = weightManager.add(minPath.weight, outEdge.weight);
        // 旧的最短路径：beginVertex到outEdge.to的路径
        PathInfo<V, E> oldPathInfo = paths.get(outEdge.to.value);
        if (oldPathInfo != null && weightManager.compare(newWeight, oldPathInfo.weight) >= 0)
            return false;

        if (oldPathInfo == null) {
            oldPathInfo = new PathInfo<>();
            paths.put(outEdge.to.value, oldPathInfo);
        } else {
            oldPathInfo.edgeInfos.clear();
        }

        oldPathInfo.weight = newWeight;
        // 更新路径信息，如果进行到此步，说明有了更小权值的路径，也就是这次找到的最小路径和当前边，将其路径更新
        oldPathInfo.edgeInfos.addAll(minPath.edgeInfos);
        oldPathInfo.edgeInfos.add(outEdge.info());
        return true;
    }

    /**
     * 从paths中找出一个最小的路径
     *
     * @param paths 还没有找出最短路径的顶点
     * @return Entry<Vertex < V, E>, PathInfo<V, E>>，最小路径，key是该最短路径的终点顶点，value是该最短路径的路径信息
     */
    private Map.Entry<Vertex<V, E>, PathInfo<V, E>> getMinPath(Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        Iterator<Map.Entry<Vertex<V, E>, PathInfo<V, E>>> iterator = paths.entrySet().iterator();
        Map.Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = iterator.next();
        while (iterator.hasNext()) {
            Map.Entry<Vertex<V, E>, PathInfo<V, E>> entry = iterator.next();
            if (weightManager.compare(entry.getValue().weight, minEntry.getValue().weight) < 0) {
                minEntry = entry;
            }
        }
        return minEntry;
    }

    /*@Override
    public Map<V, E> shortestPath(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null)
            return null;

        Map<V, E> selectedPaths = new HashMap<>();
        Map<Vertex<V, E>, E> paths = new HashMap<>();
        // 初始化paths
        for (Edge<V, E> outEdge : beginVertex.outEdges) {
            paths.put(outEdge.to, outEdge.weight);
        }

        while (!paths.isEmpty()) {
            Map.Entry<Vertex<V, E>, E> minEntry = getMinPath(paths);
            Vertex<V, E> minVertex = minEntry.getKey();
            selectedPaths.put(minVertex.value, minEntry.getValue());
            paths.remove(minVertex);
            // 对这个minVertex的outEdges进行松弛操作
            for (Edge<V, E> outEdge : minVertex.outEdges) {
                // 如果outEdge.to已经找到了最短路径，就跳过，不需要在进行松弛操作
                if (selectedPaths.containsKey(outEdge.to.value))
                    continue;
                // 新的可选择的最短的路径：beginVertex到outEdge.from的最短路径 + outEdge.weight
                E newWeight = weightManager.add(minEntry.getValue(), outEdge.weight);
                // 旧的最短路径：beginVertex到outEdge.to的路径
                E oldWeight = paths.get(outEdge.to);
                if (oldWeight == null || weightManager.compare(newWeight, oldWeight) < 0) {
                    paths.put(outEdge.to, newWeight);
                }
            }
        }

        selectedPaths.remove(beginVertex.value);
        return selectedPaths;
    }*/

    /**
     * 计算最小生成树，最小生成树有n个顶点，n-1条边
     */
    @Override
    public Set<EdgeInfo<V, E>> mst() {
//        return prim();
        return kruskal();
    }

    /**
     * prim 算法 最小生成树
     * ◼ 假设 G = (V，E) 是有权的连通图（无向），A 是 G 中最小生成树的边集
     * 算法从 S = { u0 }（u0 ∈ V），A = { } 开始，重复执行下述操作，直到 S = V 为止
     * ✓找到切分 C = (S，V – S) 的最小横切边 (u0，v0) 并入集合 A，同时将 v0 并入集合 S
     */
    private Set<EdgeInfo<V, E>> prim() {
        Iterator<Vertex<V, E>> iterator = vertices.values().iterator();
        if (!iterator.hasNext())
            return null;
        Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>(); // 用来返回信息
        Set<Vertex<V, E>> addedVertices = new HashSet<>(); // 判断是否已经添加过顶点
        Vertex<V, E> vertex = iterator.next(); // 随机获取一个顶点，从该顶点开始进行
        addedVertices.add(vertex);

        MinHeap<Edge<V, E>> heap = new MinHeap<>(vertex.outEdges, edgeComparator);

        int verticesSize = vertices.size(); // 最小生成树有n个顶点，n-1条边
        while (!heap.isEmpty() && addedVertices.size() < verticesSize) {
            Edge<V, E> edge = heap.remove();
            if (addedVertices.contains(edge.to)) // 如果已经遍历过该顶点，跳过
                continue;

            edgeInfos.add(edge.info());
            addedVertices.add(edge.to);
            heap.addAll(edge.to.outEdges);
        }

        return edgeInfos;
    }

    /**
     * kruskal 算法 最小生成树
     * ◼ 按照边的权重顺序（从小到大）将边加入生成树中，直到生成树中含有 V – 1 条边为止（ V 是顶点数量）
     * 若加入该边会与生成树形成环，则不加入该边
     * 从第3条边开始，可能会与生成树形成环
     */
    private Set<EdgeInfo<V, E>> kruskal() {
        int edgesSize = edges.size() - 1;
        if (edgesSize == -1)
            return null;

        Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
        MinHeap<Edge<V, E>> heap = new MinHeap<>(edges, edgeComparator);
        UnionFind<Vertex<V, E>> uf = new UnionFind<>(); // 使用并查集来判断两个顶点是否形成环
        vertices.forEach((v, vertex) -> uf.makeSet(vertex)); // 将顶点放入并查集

        while (!heap.isEmpty() && edgeInfos.size() < edgesSize) { // 最小生成树有n个顶点，n-1条边
            Edge<V, E> edge = heap.remove();
            if (uf.isSame(edge.from, edge.to)) // 如果两个顶点在一个集合，说明形成环，跳过
                continue;

            edgeInfos.add(edge.info());
            uf.union(edge.from, edge.to);
        }

        return edgeInfos;
    }

    @Override
    public void bfs(V begin, VertexVisitor<V> visitor) {
        if (visitor == null)
            return;
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null)
            return;

        Set<Vertex<V, E>> visited = new HashSet<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        queue.offer(beginVertex);
        visited.add(beginVertex);
        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            if (visitor.visit(vertex.value))
                return;

            for (Edge<V, E> outEdge : vertex.outEdges) {
                if (visited.contains(outEdge.to))
                    continue;
                queue.offer(outEdge.to);
                visited.add(outEdge.to);
            }
        }
    }

    @Override
    public void dfs(V begin, VertexVisitor<V> visitor) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null)
            return;

        Stack<Vertex<V, E>> stack = new Stack<>();
        Set<Vertex<V, E>> visited = new HashSet<>();
        stack.push(beginVertex);
        visited.add(beginVertex);
        if (visitor.visit(beginVertex.value))
            return;

        while (!stack.isEmpty()) {
            Vertex<V, E> vertex = stack.pop();
            for (Edge<V, E> outEdge : vertex.outEdges) {
                if (visited.contains(outEdge.to))
                    continue;
                stack.push(outEdge.from);
                stack.push(outEdge.to);
                visited.add(outEdge.to);
                if (visitor.visit(outEdge.to.value))
                    return;
                break;
            }
        }
    }

    /*@Override
    public void dfs(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null)
            return;

        dfs(beginVertex, new HashSet<>());
    }

    private void dfs(Vertex<V, E> vertex, Set<Vertex<V, E>> visited) {
        System.out.println(vertex.value);
        visited.add(vertex);

        for (Edge<V, E> outEdge : vertex.outEdges) {
            if (visited.contains(outEdge.to))
                continue;
            dfs(outEdge.to, visited);
        }
    }*/

    private static class Vertex<V, E> {
        V value;
        Set<Edge<V, E>> inEdges = new HashSet<>();
        Set<Edge<V, E>> outEdges = new HashSet<>();

        public Vertex(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            return Objects.equals(value, ((Vertex<V, E>) o).value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    private static class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        E weight;

        Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        EdgeInfo<V, E> info() {
            return new EdgeInfo<>(from.value, to.value, weight);
        }

        @Override
        public boolean equals(Object o) {
            Edge<V, E> edge = (Edge<V, E>) o;
            return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }
}

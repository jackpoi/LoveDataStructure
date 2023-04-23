package com.jackpoi;

import java.util.Comparator;

/**
 * @author beastars
 */
public class SkipList<K, V> {
    /**
     * 最大层数
     */
    private static final int MAX_LEVEL = 32;
    private static final double P = 0.25;
    private int size;
    private Comparator<K> comparator;
    /**
     * 有效层数
     */
    private int level;
    /**
     * 不存放任何K-V
     */
    private Node<K, V> first;

    public SkipList(Comparator<K> comparator) {
        this.comparator = comparator;
        first = new Node<>(null, null, MAX_LEVEL);
        first.nexts = new Node[MAX_LEVEL];
    }

    public SkipList() {
        this(null);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V put(K key, V value) {
        keyCheck(key);

        Node<K, V>[] prevs = new Node[level]; // 要插入节点每一层的前驱节点
        Node<K, V> node = first;
        for (int i = level - 1; i >= 0; i--) {
            int cmp = -1;
            while (node.nexts[i] != null && (cmp = compare(key, node.nexts[i].key)) > 0) {
                node = node.nexts[i];
            }
            if (cmp == 0) {
                V oldValue = node.nexts[i].value;
                node.nexts[i].value = value;
                return oldValue;
            }
            prevs[i] = node;
        }

        int newLevel = randomLevel();
        Node<K, V> newNode = new Node<>(key, value, newLevel);
        for (int i = 0; i < newLevel; i++) {
            if (i >= level) {
                first.nexts[i] = newNode;
            } else {
                newNode.nexts[i] = prevs[i].nexts[i];
                prevs[i].nexts[i] = newNode;
            }
        }

        size++;
        level = Math.max(level, newLevel);

        return null;
    }

    public V get(K key) {
        keyCheck(key);

        Node<K, V> node = first;
        for (int i = level - 1; i >= 0; i--) {
            int cmp = -1;
            // key > node.nexts[i].key)
            while (node.nexts[i] != null && (cmp = compare(key, node.nexts[i].key)) > 0) {
                node = node.nexts[i];
            }
            // key = node.nexts[i].key)
            if (cmp == 0)
                return node.nexts[i].value;
            // key < node.nexts[i].key) : continue;
        }

        return null;
    }

    public V remove(K key) {
        keyCheck(key);

        Node<K, V> node = first;
        Node<K, V>[] prevs = new Node[level];
        boolean exist = false;
        for (int i = level - 1; i >= 0; i--) {
            int cmp = -1;
            while (node.nexts[i] != null && (cmp = compare(key, node.nexts[i].key)) > 0) {
                node = node.nexts[i];
            }
            prevs[i] = node;
            if (cmp == 0) {
                exist = true;
            }
        }

        if (!exist) {
            return null;
        }

        // 需要被删除的节点
        Node<K, V> removeNode = node.nexts[0];

        // 设置后继
        for (int i = 0; i < removeNode.nexts.length; i++) {
            prevs[i].nexts[i] = removeNode.nexts[i];
        }

        // 更新层数
        int newLevel = level;
        while (--newLevel >= 0 && first.nexts[newLevel] == null) {
            level = newLevel;
        }

        size--;

        return removeNode.value;
    }

    private int randomLevel() {
        int level = 1;
        while (Math.random() < P && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }

    private int compare(K k1, K k2) {
        return comparator == null
                ? ((Comparable) k1).compareTo(k2)
                : comparator.compare(k1, k2);
    }

    private void keyCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V>[] nexts;

        public Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            nexts = new Node[level];
        }

        @Override
        public String toString() {
            return key + ":" + value + "_" + nexts.length;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("一共" + level + "层").append("\n");
        for (int i = level - 1; i >= 0; i--) {
            Node<K, V> node = first;
            while (node.nexts[i] != null) {
                sb.append(node.nexts[i]);
                sb.append(" ");
                node = node.nexts[i];
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

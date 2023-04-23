package com.jackpoi.map;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author beastars
 */
public class TreeMap<K, V> implements Map<K, V> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private int size;
    private Node<K, V> root;

    private Comparator<K> comparator;

    public TreeMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    public TreeMap() {
        this(null);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public V put(K key, V value) {
        // 检查传入的元素是否为空
        keyNotNullCheck(key);

        if (root == null) { // 如果没有元素，传入根节点
            root = new Node<>(key, value, null);
            size++;

            // 新添加节点之后的处理
            afterPut(root);
            return null;
        }

        // 初始化父节点和当前节点都是根节点
        Node<K, V> parent = root;
        Node<K, V> node = root;
        int cmp = 0;
        while (node != null) {
            // 比较传入的元素和当前节点的大小
            cmp = compare(key, node.key);
            // 将当前节点作为父节点
            parent = node;
            if (cmp > 0) {
                // 大于就接着比较当前节点的右子树
                node = node.right;
            } else if (cmp < 0) {
                // 小于就接着比较当前节点的左子树
                node = node.left;
            } else {
                // 相等
                node.key = key;
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }

        // 如果没有找到相等的节点
        // 生成以空节点的父节点为父节点的节点。。
        Node<K, V> newNode = new Node<>(key, value, parent);
        if (cmp > 0) {
            // 大于就是放到右子树
            parent.right = newNode;
        } else {
            // 小于就是放到左子树
            parent.left = newNode;
        }

        size++;

        // 新添加节点之后的处理
        afterPut(newNode);
        return null;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node != null ? node.value : null;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (root == null)
            return false;

        Queue<Node<K, V>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node<K, V> node = queue.poll();

            if (valEquals(node.value, value))
                return true;

            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }

        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        traversal(root, visitor);
    }

    private void traversal(Node<K, V> node, Visitor<K, V> visitor) {
        if (node == null || visitor.stop)
            return;

        traversal(node.left, visitor);

        if (visitor.stop)
            return;
        visitor.visit(node.key, node.value);

        traversal(node.right, visitor);
    }

    private boolean valEquals(V v1, V v2) {
        return v1 == null ? v2 == null : v1.equals(v2);
    }

    private void afterPut(Node<K, V> node) {
        Node<K, V> parent = node.parent;

        // 添加的是根节点 或是上溢到达了根节点
        if (parent == null) {
            black(node);
            return;
        }

        // 如果父节点是黑色，直接返回
        if (isBlack(parent))
            return;

        // 获取叔父节点
        Node<K, V> uncle = parent.sibling();
        // 获取祖父节点
        Node<K, V> grand = red(parent.parent);
//        Node<K, V> grand = parent.parent;
        if (isRed(uncle)) { // 如果叔父节点是红色，说明上溢

            black(parent);
            black(uncle);

            afterPut(grand);
            return;
        }

        if (parent.isLeftChild()) { // L
//            red(grand);
            if (node.isLeftChild()) { // LL
                black(parent);
            } else { // LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        } else { // R
//            red(grand);
            if (node.isLeftChild()) { // RL
                black(node);
                rotateRight(parent);
            } else { // RR
                black(parent);
            }
            rotateLeft(grand);
        }
    }

    private void afterRemove(Node<K, V> node) {

        // 删除 – 拥有1个RED子节点的BLACK节点
        // 判定条件：用以替代的子节点是 RED 或者 删除的节点是红色，直接删除
        if (isRed(node)) {
            // 将替代的子节点染成 BLACK 即可保持红黑树性质
            black(node);
            return;
        }

        Node<K, V> parent = node.parent;
        // 删除的是根节点
        if (parent == null)
            return;

        // 删除的是黑色叶子节点【下溢】
        boolean left = parent.left == null || node.isLeftChild(); // 判断被删除的节点是左还是右
        // 判断删除节点的兄弟节点，如果node是左，那么兄弟节点就是右
        Node<K, V> sibling = left ? parent.right : parent.left;
        if (left) { // 如果兄弟节点在右边，那么被删除的节点在左边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }

            // 此时，兄弟节点一定是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) { // 兄弟节点的两个子节点都是黑色
                boolean parentBlack = isBlack(parent); // 判断父节点的颜色
                red(sibling);
                black(parent);
                if (parentBlack) { // 如果父节点是黑色
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有一个红色子节点，向兄弟节点借元素
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right; // 更换兄弟节点
                }

                // 先进行染色操作
                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                // 在进行旋转
                rotateLeft(parent);
            }
        } else { // 如果兄弟节点在左边，那么被删除的节点在右边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                // 兄弟节点在左边，进行右旋转
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }

            // 此时，兄弟节点一定是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) { // 兄弟节点的两个子节点都是黑色
                boolean parentBlack = isBlack(parent); // 判断父节点的颜色
                red(sibling);
                black(parent);
                if (parentBlack) { // 如果父节点是黑色
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有一个红色子节点，向兄弟节点借元素

                // 如果兄弟节点的左节点是黑色，兄弟节点先进性旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left; // 更换兄弟节点
                }

                // 先进性染色操作
                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                // 在进行旋转
                rotateRight(parent);
            }
        }
    }

    private V remove(Node<K, V> node) {
        if (node == null) return null;

        size--;

        V oldValue = node.value;

        if (node.left != null && node.right != null) { // 度为2的节点
            // 找到后继节点
            Node<K, V> s = successor(node);
            // 用后继节点的值覆盖度为2的节点的值
            node.key = s.key;
            node.value = s.value;
            // 删除后继节点
            node = s;
        }


        // 删除node节点（node的度必然是1或者0）
        Node<K, V> replacement = node.left != null ? node.left : node.right;

        if (replacement != null) { // node是度为1的节点
            // 更改parent
            replacement.parent = node.parent;
            // 更改parent的left、right的指向
            if (node.parent == null) { // node是度为1的节点并且是根节点
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else { // node == node.parent.right
                node.parent.right = replacement;
            }

            // 删除节点之后的处理
            afterRemove(replacement);
        } else if (node.parent == null) { // node是叶子节点并且是根节点
            root = null;

            // 删除节点之后的处理
            afterRemove(node);
        } else { // node是叶子节点，但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else { // node == node.parent.right
                node.parent.right = null;
            }

            // 删除节点之后的处理
            afterRemove(node);
        }

        return oldValue;
    }

    private void rotateLeft(Node<K, V> grand) {
        // 左旋转，对应的是RR
        Node<K, V> parent = grand.right;
        Node<K, V> child = parent.left; // parent的左子树，需要更新父节点，提取出来
        // 左旋转
//        grand.right = parent.left;
        grand.right = child;
        parent.left = grand; // parent的left指向grand
        afterRotate(grand, parent, child);

    }

    private void rotateRight(Node<K, V> grand) {
        // 右旋转，对应LL
        Node<K, V> parent = grand.left;
        Node<K, V> child = parent.right;
        grand.left = child;
        parent.right = grand;

        afterRotate(grand, parent, child);
    }

    private void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {
        // 更新parent的父节点
        // 让parent的父节点成为子树的根节点，即parent的父节点指向原grand的父节点
        parent.parent = grand.parent; // parent.parent -> grand.parent

        // 判断原grand是左子树还是右子树，将原grand的父节点指向parent
        if (grand.isLeftChild()) {
            grand.parent.left = parent; // grand.parent.left -> parent
        } else if (grand.isRightChild()) {
            grand.parent.right = parent; // grand.parent.right -> parent
        } else {
            // grand为根节点root
            root = parent;
        }

        // 更新child的父节点
        if (child != null) {
            child.parent = grand;
        }

        // 更新grand的父节点
        grand.parent = parent;
    }


    /**
     * 前驱节点
     */
    private Node<K, V> predecessor(Node<K, V> node) {
        if (node == null)
            return null;

        // 前驱节点在左子树当中
        Node<K, V> p = node.left;

        // 如果当前节点的左子树不为空
        if (p != null) {
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }

        // 如果当前节点的左子树为空，就向上找父节点，直到节点为父节点的右子树，或者一直到根节点
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }

        // 如果node.parent == null，说明没有左子树，祖父节点也没有左子树是他，因此没有前驱节点
        // return null;
        // return node.parent;
        // 如果node == node.parent.right，说明这个节点是他父节点的右子树，即父节点就是前驱节点
        return node.parent;
    }

    /**
     * 后继节点
     */
    private Node<K, V> successor(Node<K, V> node) {
        if (node == null)
            return null;

        Node<K, V> p = node.right;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }

        while (node.parent != null && node.parent.left != node) {
            node = node.parent;
        }
        return node.parent;
    }

    private Node<K, V> color(Node<K, V> node, boolean color) {
        if (node != null)
            node.color = color;
        return node;
    }

    private Node<K, V> red(Node<K, V> node) {
        return color(node, RED);
    }

    private Node<K, V> black(Node<K, V> node) {
        return color(node, BLACK);
    }

    private boolean colorOf(Node<K, V> node) {
        return node == null ? BLACK : node.color;
    }

    private boolean isRed(Node<K, V> node) {
        return colorOf(node) == RED;
    }

    private boolean isBlack(Node<K, V> node) {
        return colorOf(node) == BLACK;
    }

    private Node<K, V> node(K key) {
        Node<K, V> node = root;
        while (node != null) {
            int cmp = compare(key, node.key);
            if (cmp == 0)
                return node;
            if (cmp > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return null;
    }

    private void keyNotNullCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
    }

    private int compare(K k1, K k2) {
        if (comparator != null) {
            return comparator.compare(k1, k2);
        }
        return ((Comparable<K>) k1).compareTo(k2);
    }

    private static class Node<K, V> {
        boolean color = RED;
        K key;
        V value;

        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean hasTwoChildren() {
            return parent.left != null && parent.right != null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        public Node<K, V> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }
            if (isRightChild()) {
                return parent.left;
            }
            return null;
        }
    }
}

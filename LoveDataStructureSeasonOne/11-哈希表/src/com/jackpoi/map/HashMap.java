package com.jackpoi.map;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * @author beastars
 */
public class HashMap<K, V> implements Map<K, V> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private Node<K, V>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public HashMap() {
//        table = new Node[16];
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        if (size == 0)
            return;

        size = 0;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    @Override
    public V put(K key, V value) {
        resize();

        int index = index(key);
        // 取出index位置的红黑树根节点
        Node<K, V> root = table[index];
        if (root == null) {
            // 对应索引的根节点为空，新建一个红黑树根节点
            root = createNode(key, value, null);
            table[index] = root;
            size++;
            fixAfterPut(root);
            return null;
        }

        // 对应索引有红黑树根节点，将新的节点添加到红黑树上面
        Node<K, V> parent = root;
        Node<K, V> node = root;
        int cmp = 0;
        K k1 = key;
        int h1 = hash(k1);
        Node<K, V> result = null;
        // 是否已经搜索过key
        boolean searched = false;
        do {
            // 将当前节点作为父节点
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2) {
                // 大于就接着比较当前节点的右子树
                cmp = 1;
            } else if (h1 < h2) {
                // 大于就接着比较当前节点的左子树
                cmp = -1;
            } else if (Objects.equals(k1, k2)) {
                cmp = 0;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (cmp = ((Comparable) k1).compareTo(k2)) != 0) {
                // 什么都不做
            } else if (searched) {
                // searched == true
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            } else {
                // searched == false
                // 还没有扫描，然后再根据内存地址大小决定左右
                if ((node.left != null && (result = node(node.left, k1)) != null)
                        || node.right != null && (result = node(node.right, k1)) != null) {
                    // key已经存在
                    node = result;
                    cmp = 0;
                } else {
                    // key不存在
                    searched = true;
                    cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
                }
            }

            if (cmp > 0) {
                // 大于就接着比较当前节点的右子树
                node = node.right;
            } else if (cmp < 0) {
                // 大于就接着比较当前节点的左子树
                node = node.left;
            } else {
                // 相等
                node.key = key;
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        } while (node != null);

        // 看看插入到父节点的哪个位置
        Node<K, V> newNode = createNode(key, value, parent);
        if (cmp > 0) {
            // 大于就是放到右子树
            parent.right = newNode;
        } else {
            // 小于就是放到左子树
            parent.left = newNode;
        }

        size++;

        // 新添加节点之后的处理
        fixAfterPut(newNode);
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
        if (size == 0)
            return false;

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null)
                continue;

            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (Objects.equals(value, node.value))
                    return true;

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }

        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (size == 0 || visitor == null)
            return;

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null)
                continue;

            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (visitor.visit(node.key, node.value))
                    return;

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
    }

    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        return new Node<>(key, value, parent);
    }

    protected void afterRemove(Node<K, V> willNode, Node<K, V> removedNode) {
    }

    protected V remove(Node<K, V> node) {
        if (node == null) return null;

        Node<K, V> willNode = node;

        size--;

        V oldValue = node.value;

        if (node.left != null && node.right != null) { // 度为2的节点
            // 找到后继节点
            Node<K, V> s = successor(node);
            // 用后继节点的值覆盖度为2的节点的值
            node.key = s.key;
            node.value = s.value;
            node.hash = s.hash;
            // 删除后继节点
            node = s;
        }

        // 删除node节点（node的度必然是1或者0）
        Node<K, V> replacement = node.left != null ? node.left : node.right;
        int index = index(node);

        if (replacement != null) { // node是度为1的节点
            // 更改parent
            replacement.parent = node.parent;
            // 更改parent的left、right的指向
            if (node.parent == null) { // node是度为1的节点并且是根节点
                table[index] = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else { // node == node.parent.right
                node.parent.right = replacement;
            }

            // 删除节点之后的处理
            fixAfterRemove(replacement);
        } else if (node.parent == null) { // node是叶子节点并且是根节点
            table[index] = null;

            // 删除节点之后的处理
            fixAfterRemove(node);
        } else { // node是叶子节点，但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else { // node == node.parent.right
                node.parent.right = null;
            }

            // 删除节点之后的处理
            fixAfterRemove(node);
        }

        // 交给子类处理
        afterRemove(willNode, node);

        return oldValue;
    }

    /**
     * 根据key生成对应的索引（在桶数组中的位置）
     */
    private int index(K key) {
        return hash(key) & (table.length - 1);
    }

    private int index(Node<K, V> node) {
        return node.hash & (table.length - 1);
    }

    private void resize() {
        // 装填因子 <= 0.75
        if (size / table.length <= DEFAULT_LOAD_FACTOR)
            return;

        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] == null)
                continue;

            queue.offer(oldTable[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }

                // 挪动节点
                moveNode(node);
            }
        }
    }

    private void moveNode(Node<K, V> newNode) {
        // 重置
        newNode.parent = null;
        newNode.left = null;
        newNode.right = null;
        newNode.color = RED;

        int index = index(newNode);
        // 取出index位置的红黑树根节点
        Node<K, V> root = table[index];
        if (root == null) {
            root = newNode;
            table[index] = root;
            fixAfterPut(root);
            return;
        }

        // 对应索引有红黑树根节点，将新的节点添加到红黑树上面
        Node<K, V> parent = root;
        Node<K, V> node = root;
        int cmp = 0;
        K k1 = newNode.key;
        int h1 = newNode.hash;

        do {
            // 将当前节点作为父节点
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2) {
                // 大于就接着比较当前节点的右子树
                cmp = 1;
            } else if (h1 < h2) {
                // 大于就接着比较当前节点的左子树
                cmp = -1;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (cmp = ((Comparable) k1).compareTo(k2)) != 0) {
                // 什么都不做
            } else {
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            }

            if (cmp > 0) {
                // 大于就接着比较当前节点的右子树
                node = node.right;
            } else if (cmp < 0) {
                // 大于就接着比较当前节点的左子树
                node = node.left;
            }
        } while (node != null);

        // 看看插入到父节点的哪个位置
        newNode.parent = parent;
        if (cmp > 0) {
            // 大于就是放到右子树
            parent.right = newNode;
        } else {
            // 小于就是放到左子树
            parent.left = newNode;
        }

        // 新添加节点之后的处理
        fixAfterPut(newNode);
    }

    /**
     * 对key进行扰动计算
     */
    private int hash(K key) {
        if (key == null)
            return 0;
        int hash = key.hashCode();
        return hash ^ (hash >>> 16);
    }

    private Node<K, V> node(K key) {
        Node<K, V> root = table[index(key)];

        return root == null ? null : node(root, key);
    }

    private Node<K, V> node(Node<K, V> node, K k1) {
        int h1 = hash(k1);
        // 存储查找结果
        Node<K, V> result = null;
        int cmp = 0;
        while (node != null) {
            K k2 = node.key;
            int h2 = node.hash;
            // 先比较哈希值
            if (h1 > h2) {
                // 向右边找
                node = node.right;
            } else if (h1 < h2) {
                // 向左边找
                node = node.left;
            } else if (Objects.equals(k1, k2)) {
                // 哈希值相等，equals又相等
                return node;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (cmp = ((Comparable) k1).compareTo(k2)) != 0) {
                // k1 k2为同一类型，且具备比较性
                if (cmp > 0) {
                    // 向右边找
                    node = node.right;
                } else {
                    // 向左边找
                    node = node.left;
                }
            } else if (node.right != null && (result = node(node.right, k1)) != null) {
                // 哈希值相等，equals不等，不具备可比较性，对左右子树进行遍历查找，看是否有相同的节点
                return result;
            } else {
                // 只能向左边找
                node = node.left;
            }

//            else if (node.left != null && (result = node(node.left, k1)) != null) {
//                // 哈希值相等，equals不等，不具备可比较性，对左右子树进行遍历查找，看是否有相同的节点
//                return result;
//            } else {
//                // 没有对应的节点
//                return null;
//            }
        }
        return null;
    }

    private void fixAfterPut(Node<K, V> node) {
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

            fixAfterPut(grand);
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

    private void fixAfterRemove(Node<K, V> node) {

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
                    fixAfterRemove(parent);
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
                    fixAfterRemove(parent);
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

    /**
     * 比较key的大小
     *
     * @param k1
     * @param k2
     * @param h1 k1的hashCode
     * @param h2 k2的hashCode
     * @return
     */
    private int compare(K k1, K k2, int h1, int h2) {
        // 比较哈希值
        int result = h1 - h2;
        if (result != 0)
            return result;

        // 比较equals
        if (Objects.equals(k1, k2))
            return 0;

        // 哈希值相等，但是equals不相等
        // 比较类名
        if (k1 != null && k2 != null) {
            String k1Class = k1.getClass().getName();
            String k2Class = k2.getClass().getName();
            result = k1Class.compareTo(k2Class);
            // 类型不同
            if (result != 0)
                return result;

            // 同一类型且具备可比较性
            if (k1 instanceof Comparable) {
                return ((Comparable) k1).compareTo(k2);
            }
        }

        // 哈希值相同，且为同一类型，equals不相等，但是不具备可比较性
        // k1不为null，k2为null
        // k1为null，k2不为null
        return System.identityHashCode(k1) - System.identityHashCode(k2);
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
            table[index(grand)] = parent;
        }

        // 更新child的父节点
        if (child != null) {
            child.parent = grand;
        }

        // 更新grand的父节点
        grand.parent = parent;
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


    protected static class Node<K, V> {
        boolean color = RED;
        K key;
        V value;
        int hash;

        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            int hash = key == null ? 0 : key.hashCode();
            this.hash = hash ^ (hash >>> 16);
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

package com.jackpoi.heap;

import com.jackpoi.printer.BinaryTreeInfo;

import java.util.Comparator;

/**
 * @author beastars
 */
public class BinaryHeap<E> extends AbstractHeap<E> implements BinaryTreeInfo {
    private E[] elements;
    private static final int DEFAULT_CAPACITY = 10;

    public BinaryHeap(Comparator<E> comparator) {
        super(comparator);
        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    public BinaryHeap(E[] elements, Comparator comparator) {
        super(comparator);

        if (elements == null || elements.length == 0) {
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        } else {
            int capacity = Math.max(elements.length, DEFAULT_CAPACITY);
            this.elements = (E[]) new Object[capacity];
            for (int i = 0; i < elements.length; i++) {
                this.elements[i] = elements[i];
                size++;
            }
            heapify();
        }
    }

    public BinaryHeap(E[] elements) {
        this(elements, null);
    }

    public BinaryHeap() {
        this(null, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public void add(E element) {
        elementNotNullCheck(element);
        ensureCapacity(size + 1);

        elements[size++] = element;

        siftUp(size - 1);
    }

    @Override
    public E get() {
        emptyCheck();
        return elements[0];
    }

    @Override
    public E remove() {
        emptyCheck();

        // 用最后一个节点覆盖根节点
        // 删除最后一个节点
        int lastIndex = --size;
        E root = elements[0];
        elements[0] = elements[lastIndex];
        elements[lastIndex] = null;

        // 将根节点下滤
        siftDown(0);

        return root;
    }

    @Override
    public E replace(E element) {
        elementNotNullCheck(element);

        E root = elements[0];
        if (size == 0) {
            // 没有元素可以删除，直接添加
            elements[0] = element;
            size = 1;
        } else {
            // 将新元素直接覆盖堆顶元素，然后对堆顶元素进行下滤
            elements[0] = element;
            siftDown(0);
        }

        return root;
    }

    /**
     * 批量建堆
     */
    private void heapify() {
        // 自上而下的上滤
//        for (int i = 1; i < size; i++) {
//            siftUp(i);
//        }

        // 自下而上的下滤
        for (int i = ((size >> 1) - 1); i >= 0; i--) {
            siftDown(i);
        }
    }

    /**
     * 让index位置的元素上滤
     *
     * @param index 需要上滤的元素的索引
     */
    private void siftUp(int index) {
//        E element = elements[index];
//
//        while (index > 0) {
//            // parent index : 如果 i > 0 ，它的父节点的索引为 floor( (i – 1) / 2 )
//            int parentIndex = (index - 1) >> 1;
//            E parent = elements[parentIndex];
//            if (compare(element, parent) <= 0) // element <= parent
//                return;
//
//            // element > parent, 交换 index 和 parentIndex 位置的内容
//            E tmp = elements[index];
//            elements[index] = elements[parentIndex];
//            elements[parentIndex] = tmp;
//            // 重新赋值index
//            index = parentIndex;
//        }

        E element = elements[index];
        while (index > 0) {
            // parent index : 如果 i > 0 ，它的父节点的索引为 floor( (i – 1) / 2 )
            int parentIndex = (index - 1) >> 1;
            E parent = elements[parentIndex];
            if (compare(element, parent) <= 0) // element <= parent
                break;

            // 将父元素parent存储在index位置
            elements[index] = parent;

            // 重新赋值index
            index = parentIndex;
        }
        elements[index] = element;
    }

    /**
     * 让index位置的元素下滤
     *
     * @param index 需要下滤的元素的索引
     */
    private void siftDown(int index) {
        E element = elements[index];

        // 完全二叉树的叶子节点个数为 floor((n + 1) / 2)
        // 完全二叉树的非叶子节点个数为 floor(n / 2)
        int half = size >> 1;
        // 第一个叶子节点的索引 == 非叶子节点的个数
        // 必须保证index的位置是非叶子节点
        while (index < half) { // index < 第一个叶子节点的索引
            // 1.只有左子节点
            // 2.同时有左右子节点

            // 默认为左子节点跟他进行比较
            // 如果 2i + 1 ≤ n – 1，它的左子节点的索引为 2i + 1
            int childIndex = (index << 1) + 1;
            E child = elements[childIndex];

            // 右子节点
            int rightIndex = childIndex + 1;

            // 选出左右子节点最大的那个
            if (rightIndex < size && compare(elements[rightIndex], child) > 0) {
                child = elements[childIndex = rightIndex];
            }

            if (compare(element, child) >= 0)
                break;

            // 将子节点存放到index位置
            elements[index] = child;
            // 重新设置index
            index = childIndex;
        }
        elements[index] = element;
    }

    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity)
            return;

        // 新容量为旧容量的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    private void emptyCheck() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Heap is empty");
        }
    }

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null");
        }
    }

    @Override
    public Object root() {
        return 0;
    }

    @Override
    public Object left(Object node) {
        // 如果 2i + 1 ≤ n – 1，它的左子节点的索引为 2i + 1
        // 如果 2i + 1 > n – 1 ，它无左子节点
        int index = ((int) node << 1) + 1;
        return index >= size ? null : index;
    }

    @Override
    public Object right(Object node) {
        // 如果 2i + 2 ≤ n – 1 ，它的右子节点的索引为 2i + 2
        // 如果 2i + 2 > n – 1 ，它无右子节点
        int index = ((int) node << 1) + 2;
        return index >= size ? null : index;
    }

    @Override
    public Object string(Object node) {
        return elements[(int) node];
    }
}

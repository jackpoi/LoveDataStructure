package com.jackpoi.circle;

/**
 * 循环队列
 *
 * @author beastars
 */
public class CircleQueue<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;
    private int size;
    private int front;

    public CircleQueue() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[index(i)] = null;
        }
        size = 0;
        front = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enQueue(E element) {
        ensureCapacity(size + 1);
        elements[index(size)] = element;
        size++;
    }

    public E deQueue() {
        E oldElement = elements[front];
        elements[front] = null;
        front = index(1);
        size--;
        return oldElement;
    }

    public E front() {
        return elements[front];
    }

    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity)
            return;
        // 新容量为老容量的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[index(i)];
        }
        elements = newElements;
        front = 0;
    }

    private int index(int index) {
        index += front;
        return index - (index >= elements.length ? elements.length : 0);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("capacity=").append(elements.length)
                .append(" size=").append(size)
                .append(" front=").append(front)
                .append(", [");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(elements[i]);
        }
        string.append("]");
        return string.toString();
    }
}

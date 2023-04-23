package com.jackpoi.circle;

/**
 * 循环双端队列
 *
 * @author beastars
 */
public class CircleDeque<E> {
    private E[] elements;
    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private int front;

    public CircleDeque() {
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

    public void enQueueRear(E element) {
        ensureCapacity(size + 1);
        elements[index(size)] = element;
        size++;
    }

    public void enQueueFront(E element) {
        ensureCapacity(size + 1);
        front = index(-1);
        elements[front] = element;
        size++;
    }

    public E deQueueRear() {
        int rearIndex = index(size - 1);
        E oldElement = elements[rearIndex];
        elements[rearIndex] = null;
        size--;
        return oldElement;
    }

    public E deQueueFront() {
        E oldElement = elements[front];
        elements[front] = null;
        front = index(1);
        size--;
        return oldElement;
    }

    public E front() {
        return elements[front];
    }

    public E rear() {
        return elements[index(size - 1)];
    }

    private int index(int index) {
        index += front;
        if (index < 0) {
            return index + elements.length;
        } else {
            return index - (index >= elements.length ? elements.length : 0);
        }
    }

    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (capacity <= oldCapacity)
            return;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[index(i)];
        }
        elements = newElements;
        front = 0;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("capacity=").append(elements.length)
                .append(" size=").append(size)
                .append(" front=").append(front)
                .append(", [");
        for (int i = 0; i < elements.length; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(elements[i]);
        }
        string.append("]");
        return string.toString();
    }
}

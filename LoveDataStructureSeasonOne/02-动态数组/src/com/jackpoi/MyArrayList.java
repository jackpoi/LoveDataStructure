package com.jackpoi;

public class MyArrayList<E> {

    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;

    public MyArrayList(int capacity) {
        capacity = Math.max(capacity, DEFAULT_CAPACITY);
        elements = (E[]) new Object[capacity];
    }

    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public void clear() {
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(E element) {
        return indexOf(element) >= 0;
    }

    public void add(E element) {
        elements[size++] = element;
    }

    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("size=" + size + "， index=" + index);
        return elements[index];
    }

    public E set(int index, E element) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("size=" + size + "， index=" + index);
        elements[index] = element;
        return element;
    }

    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("size=" + size + "， index=" + index);
        E oldElement = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size--] = null;
        return oldElement;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("size=" + size + "， index=" + index);
        for (int i = size; i >= index; i--) {
            elements[i] = elements[i - 1];
        }
        size++;
    }

    public int indexOf(E element) {
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null)
                    return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(elements[i]))
                    return i;
            }
        }
        return -1;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("size=").append(size).append(", [");
        for (int i = 0; i < size; i++) {
            if (i != 0)
                string.append(", ");
            string.append(elements[i]);
        }
        string.append("]");
        return string.toString();
    }
}

package com.jackpoi;

import com.jackpoi.list.LinkedList;
import com.jackpoi.list.List;

/**
 * @author beastars
 */
public class Queue<E> {
    private List<E> list = new LinkedList<>();

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void enQueue(E element) {
        list.add(element);
    }

    public E deQueue() {
        return list.remove(0);
    }

    public E front() {
        return list.get(0);
    }
}

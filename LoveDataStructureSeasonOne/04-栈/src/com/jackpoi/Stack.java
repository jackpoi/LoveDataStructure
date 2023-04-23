package com.jackpoi;

import com.jackpoi.list.ArrayList;
import com.jackpoi.list.List;

/**
 * @author beastars
 */
public class Stack<E> {
    private List<E> list = new ArrayList<>();

    public void clear() {
        list.clear();
    }

    public void push(E element) {
        list.add(element);
    }

    public E pop() {
        return list.remove(list.size() - 1);
    }

    public E peek() {
        return list.get(list.size() - 1);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

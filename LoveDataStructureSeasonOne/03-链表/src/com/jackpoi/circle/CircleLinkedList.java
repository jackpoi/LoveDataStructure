package com.jackpoi.circle;

import com.jackpoi.AbstractList;

import java.util.LinkedList;

/**
 * @author beastars
 */
public class CircleLinkedList<E> extends AbstractList<E> {

    private Node<E> first;
    private Node<E> last;
    private Node<E> current;

    static class Node<E> {
        Node<E> prev;
        E val;
        Node<E> next;

        public Node(Node<E> prev, E val, Node<E> next) {
            this.prev = prev;
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder string = new StringBuilder();
            if (prev != null) {
                string.append(prev.val);
            } else {
                string.append("null");
            }
            string.append("_").append(val).append("_");
            if (next != null) {
                string.append(next.val);
            } else {
                string.append("null");
            }
            return string.toString();
        }
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
    }

    @Override
    public E get(int index) {
        return node(index).val;
    }

    @Override
    public E set(int index, E element) {
        Node<E> node = node(index);
        E old = node.val;
        node.val = element;
        return old;
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        if (index == size) { // 链表为空或者添加到末尾时
            Node<E> oldLast = last;
            last = new Node<>(oldLast, element, first);
            if (first == null) { // 链表为空
                first = last;
                first.next = first;
            } else { // 添加到末尾时
                oldLast.next = last;
            }
            first.prev = last;
        } else { // 添加到首位或者中间时
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> node = new Node<>(prev, element, next);
            next.prev = node;
            prev.next = node;
            if (index == 0) { // 添加到首位
                first = node;
            }
        }
        size++;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        return remove(node(index));
    }

    private E remove(Node<E> node) {
        if (size == 1) {
            first = null;
            last = null;
        } else {
            Node<E> perv = node.prev;
            Node<E> next = node.next;
            perv.next = next;
            next.prev = perv;
            if (node == first) { // 删除的是首节点时,index == 0
                first = next;
            }

            if (node == last) { // 删除的是尾节点时,index == size-1
                last = perv;
            }
        }

        size--;
        return node.val;
    }

    public void reset() {
        current = first;
    }

    public E next() {
        if (current == null)
            return null;
        current = current.next;
        return current.val;
    }

    public E remove() {
        if (current == null)
            return null;
        Node<E> next = current.next;
        E element = remove(current);
        if (size == 0) {
            current = null;
        } else {
            current = next;
        }
        return element;
    }

    @Override
    public int indexOf(E element) {
        Node<E> node = first;
        if (element == null) {
            for (int i = 0; i < size; i++) {
                if (node.val == null)
                    return i;
                node = node.next;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (element.equals(node.val))
                    return i;
                node = node.next;
            }
        }
        return -1;
    }

    private Node<E> node(int index) {
        rangeCheck(index);
        if (index < (size >> 1)) {
            Node<E> node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        } else {
            Node<E> node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
            return node;
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        Node<E> node = first;

        string.append("size=").append(size).append(", [");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                string.append(", ");
            }
            string.append(node);
            node = node.next;
        }
        string.append("]");

        return string.toString();
    }
}

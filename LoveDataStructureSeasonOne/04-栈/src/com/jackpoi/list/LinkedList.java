package com.jackpoi.list;

/**
 * @author beastars
 */
public class LinkedList<E> extends AbstractList<E> {

    private Node<E> first;
    private Node<E> last;

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
    public E remove(int index) {
        rangeCheck(index);
        Node<E> node = node(index);
        Node<E> perv = node.prev;
        Node<E> next = node.next;
        if (perv == null) { // 删除的是首节点时,index == 0
            first = next;
        } else {
            perv.next = next;
        }

        if (next == null) { // 删除的是尾节点时,index == size-1
            last = perv;
        } else {
            next.prev = perv;
        }
        size--;
        return node.val;
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        if (index == size) { // 链表为空或者添加到末尾时
            Node<E> oldLast = last;
            last = new Node<>(oldLast, element, null);
            if (oldLast == null) { // 链表为空
                first = last;
            } else { // 添加到末尾
                oldLast.next = last;
            }
        } else { // 添加到首位或者中间时
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> node = new Node<>(prev, element, next);
            next.prev = node;
            if (prev == null) { // 添加到首位
                first = node;
            } else { // 添加到中间
                prev.next = node;
            }
        }
        size++;
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

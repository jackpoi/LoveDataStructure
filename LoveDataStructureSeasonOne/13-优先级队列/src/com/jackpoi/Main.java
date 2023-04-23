package com.jackpoi;

import com.jackpoi.queue.PriorityQueue;

/**
 * @author beastars
 */
public class Main {
    public static void main(String[] args) {
        PriorityQueue<Person> queue = new PriorityQueue<>();
        queue.enQueue(new Person("Jack", 99));
        queue.enQueue(new Person("Rose", 10));
        queue.enQueue(new Person("Jake", 5));
        queue.enQueue(new Person("James", 15));

        while (!queue.isEmpty()) {
            Person person = queue.deQueue();
            System.out.println(person);
        }
    }
}

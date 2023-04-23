package com.jackpoi;

/**
 * @author beastars
 */
public class Person implements Comparable<Person> {
    public int age;
    public int height;

    public Person(int age, int height) {
        this.age = age;
        this.height = height;
    }

    @Override
    public int compareTo(Person o) {
        return this.age - o.age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", height=" + height +
                '}';
    }
}

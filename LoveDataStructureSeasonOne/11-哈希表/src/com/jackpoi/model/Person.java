package com.jackpoi.model;

import java.util.Objects;

/**
 * @author beastars
 */
public class Person {
    int age;
    String name;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(age, name);
//    }

    @Override
    public int hashCode() {
        int hashCode = Integer.hashCode(age);
        hashCode = hashCode * 31 + (name != null ? name.hashCode() : 0);
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        // 内存地址相同
        if (this == o) return true;
        // 比较的对象为null，或者类型不同
        if (o == null || getClass() != o.getClass()) return false;
        // 比较成员变量
        Person person = (Person) o;
        return age == person.age
                && Objects.equals(name, person.name);
    }
}

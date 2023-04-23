package com.jackpoi.model;

import java.util.Objects;

/**
 * @author beastars
 */
public class Cat {
    int age;

    public Cat(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(age);
    }
}

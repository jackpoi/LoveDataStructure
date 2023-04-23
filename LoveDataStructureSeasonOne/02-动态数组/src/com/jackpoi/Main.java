package com.jackpoi;

public class Main {
    public static void main(String[] args) {

        ArrayList<Person> persons = new ArrayList<>(100);

        persons.add(new Person(10, "Jack"));
        persons.add(new Person(11, "Rose"));
        persons.add(new Person(15, "Tom"));
        persons.add(null);
        System.out.println(persons);
        persons.clear();

        ArrayList<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(2);
        list.add(null);
        list.add(6);
        System.out.println(list);
        list.clear();

        System.gc();

        MyArrayList<Integer> a = new MyArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        System.out.println(a);
    }
}

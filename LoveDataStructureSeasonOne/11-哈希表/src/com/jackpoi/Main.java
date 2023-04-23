package com.jackpoi;

import com.jackpoi.map.HashMap;
import com.jackpoi.map.Map;
import com.jackpoi.model.Person;

/**
 * @author beastars
 */
public class Main {
    public static void main(String[] args) {
//        test3();
        Map<Object, Integer> map = new HashMap<>();
        map.put("qqq", 20);
    }

    static void test3() {
        Person p1 = new Person(10, "jack");
        Person p2 = new Person(10, "jack");

        Map<Object, Integer> map = new HashMap<>();
        map.put(p1, 1);
        map.put(p2, 2);
        map.put("jack", 3);
        map.put("rose", 4);
        map.put("jack", 5);
        map.put(null, 6);

        System.out.println(map.size());
        System.out.println(map.get(p1));
        System.out.println(map.get("jack"));
        System.out.println(map.get("rose"));
//        System.out.println(map.get(null));
        System.out.println(map.containsKey(p2));
        System.out.println(map.containsKey("tom"));

        System.out.println(map.remove("rose"));
        System.out.println(map.containsKey("rose"));
        System.out.println(map.containsKey(null));
        System.out.println(map.get("rose"));
        System.out.println(map.size());

        map.traversal(new Map.Visitor<Object, Integer>() {
            @Override
            public boolean visit(Object key, Integer value) {
                System.out.println(key + "_" + value);
                return false;
            }
        });

        System.out.println(map.containsValue(5));
        System.out.println(map.containsValue(9));
    }

    static void test2() {
        Person p1 = new Person(10, "jack");
        Person p2 = new Person(10, "jack");

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p1 == p2);
        System.out.println(p1.equals(p2));
        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());

        Map<Person, Integer> map = new HashMap<>();
        map.put(p1, 1);
        map.put(p2, 2);

        System.out.println(map.size());
        System.out.println(map);
    }

    static void test1() {
        String s = "abc";
        int len = s.length();
        int hashCode = 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            hashCode = hashCode * 31 + c;
        }
        // hashCode = [(j ∗ n + a) ∗ n + c] ∗ n + k;

        System.out.println(Integer.toBinaryString(hashCode));
        System.out.println(hashCode);
        System.out.println(s.hashCode());
    }
}

package com.jackpoi;

import com.jackpoi.file.FileInfo;
import com.jackpoi.file.Files;
import com.jackpoi.map.Map;
import com.jackpoi.map.TreeMap;
import com.jackpoi.set.Set;
import com.jackpoi.set.TreeSet;

/**
 * @author beastars
 */
public class Main {
    public static void main(String[] args) {
        test3();
    }

    static void test3() {
        com.jackpoi.set.Set<Integer> set = new TreeSet<>();

        set.add(10);
        set.add(13);
        set.add(11);
        set.add(11);
        set.add(10);

        set.traversal(new Set.Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.println(element);
                return false;
            }
        });
    }

    static void test2() {
        FileInfo fileInfo = Files.read("E:\\素材\\图包\\恋上数据结构与算法（第一季）\\代码",
                new String[]{"java"});

        System.out.println("文件数量：" + fileInfo.getFiles());
        System.out.println("代码行数：" + fileInfo.getLines());
        String[] words = fileInfo.words();
        System.out.println("单词数量：" + words.length);

        Map<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < words.length; i++) {
            Integer count = map.get(words[i]);
            count = count == null ? 1 : count + 1;
            map.put(words[i], count);
        }

        System.out.println(map.size());
        map.traversal(new Map.Visitor<String, Integer>() {
            @Override
            public boolean visit(String key, Integer value) {
                System.out.println(key + "_" + value);
                return false;
            }
        });

        Times.test("TreeMap:", new Times.Task() {
            @Override
            public void execute() {
                testMap(new TreeMap<>(), words);
            }
        });
    }

    static void testMap(Map<String, Integer> map, String[] words) {
        for (int i = 0; i < words.length; i++) {
            Integer count = map.get(words[i]);
            count = count == null ? 1 : count + 1;
            map.put(words[i], count);
        }
        System.out.println("单词数量：" + map.size());
        for (int i = 0; i < words.length; i++) {
            map.containsKey(words[i]);
        }
        for (int i = 0; i < words.length; i++) {
            map.remove(words[i]);
        }
    }

    static void test1() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("c", 2);
        map.put("b", 6);
        map.put("a", 8);
        map.put("a", 5);

        map.traversal(new Map.Visitor<String, Integer>() {
            public boolean visit(String key, Integer value) {
                System.out.println(key + "_" + value);
                return false;
            }
        });
    }
}

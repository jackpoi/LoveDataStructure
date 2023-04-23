package com.jackpoi;

import com.jackpoi.file.FileInfo;
import com.jackpoi.file.Files;
import com.jackpoi.set.ListSet;
import com.jackpoi.set.Set;
import com.jackpoi.set.TreeSet;

/**
 * @author beastars
 */
public class Main {
    public static void main(String[] args) {
        test2();
    }

    static void test2() {
        FileInfo fileInfo = Files.read("F:\\JavaProject\\IdeaProjects",
                new String[]{"java"});

        System.out.println("文件数量：" + fileInfo.getFiles());
        System.out.println("代码行数：" + fileInfo.getLines());
        String[] words = fileInfo.words();
        System.out.println("单词数量：" + words.length);

        Times.test("ListSet:", new Times.Task() {
            @Override
            public void execute() {
                testSet(new ListSet<>(), words);
            }
        });

        Times.test("TreeSet:", new Times.Task() {
            @Override
            public void execute() {
                testSet(new TreeSet<>(), words);
            }
        });
    }

    static void testSet(Set<String> set, String[] words) {
        for (int i = 0; i < words.length; i++) {
            set.add(words[i]);
        }
        System.out.println("单词数量：" + set.size());
        for (int i = 0; i < words.length; i++) {
            set.contains(words[i]);
        }
        for (int i = 0; i < words.length; i++) {
            set.remove(words[i]);
        }
    }

    static void test1() {
//        Set<Integer> set = new ListSet<>();
        Set<Integer> set = new TreeSet<>();

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
}

package com.jackpoi;

import com.jackpoi.tools.Asserts;
import com.jackpoi.tools.Times;
import com.jackpoi.union.*;

/**
 * @author beastars
 */
public class Main {
    static final int count = 3000000;

    public static void main(String[] args) {
//        testTime(new UnionFind_QF(count));
//        testTime(new UnionFind_QU(count));
//        testTime(new UnionFind_QU_S(count));
//        testTime(new UnionFind_QU_R(count));
//        testTime(new UnionFind_QU_R_PC(count));
//        testTime(new UnionFind_QU_R_PS(count));
//        testTime(new UnionFind_QU_R_PH(count));

        GenericUnionFind<Student> uf = new GenericUnionFind<>();
        Student stu1 = new Student(10, "tom");
        Student stu2 = new Student(15, "jack");
        Student stu3 = new Student(25, "rose");
        Student stu4 = new Student(55, "faker");
        uf.makeSet(stu1);
        uf.makeSet(stu2);
        uf.makeSet(stu3);
        uf.makeSet(stu4);

        uf.union(stu1, stu2);
        uf.union(stu3, stu4);
//        uf.union(stu1, stu4);

        System.out.println("uf.isSame(stu1, stu2) = " + uf.isSame(stu1, stu2));
        System.out.println("uf.isSame(stu3, stu4) = " + uf.isSame(stu3, stu4));
        System.out.println("uf.isSame(stu1, stu4) = " + uf.isSame(stu1, stu4));
    }

    static void testTime(UnionFind uf) {
        uf.union(0, 1);
        uf.union(0, 3);
        uf.union(0, 4);
        uf.union(2, 3);
        uf.union(2, 5);

        uf.union(6, 7);

        uf.union(8, 10);
        uf.union(9, 10);
        uf.union(9, 11);

        Asserts.test(!uf.isSame(2, 7));

        uf.union(4, 6);

        Asserts.test(uf.isSame(2, 7));

        Times.test(uf.getClass().getSimpleName(), () -> {
            for (int i = 0; i < count; i++) {
                uf.union((int) (Math.random() * count),
                        (int) (Math.random() * count));
            }

            for (int i = 0; i < count; i++) {
                uf.isSame((int) (Math.random() * count),
                        (int) (Math.random() * count));
            }
        });
    }
}

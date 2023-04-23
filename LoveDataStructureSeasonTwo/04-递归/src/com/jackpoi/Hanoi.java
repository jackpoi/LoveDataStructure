package com.jackpoi;

/**
 * @author beastars
 */
public class Hanoi {
    public static void main(String[] args) {
        new Hanoi().hanoi(3, "A", "B", "C");
    }

    /**
     * 将 n 个盘子从p1挪动到p3，并打印过程
     *
     * @param n  n个盘子
     * @param p1 第一个柱子的名称
     * @param p2 第二个柱子的名称
     * @param p3 第三个柱子的名称
     */
    public void hanoi(int n, String p1, String p2, String p3) {
        if (n == 1) {
            move(1, p1, p3);
            return;
        }
        hanoi(n - 1, p1, p3, p2); // 将上面的n-1个盘子移动到中间柱子
        move(n, p1, p3); // 将剩下的第n个盘子移到目标柱子上
        hanoi(n - 1, p2, p3, p1); // 将剩下的n-1个盘子移动到目标柱子上
    }

    /**
     * 将no号盘子从from移动到to
     */
    private void move(int no, String from, String to) {
        System.out.println("第" + no + "个盘子从" + from + "移动到了" + to);
    }
}

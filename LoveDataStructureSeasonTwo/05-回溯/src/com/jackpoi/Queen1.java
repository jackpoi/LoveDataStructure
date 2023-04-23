package com.jackpoi;

/**
 * @author beastars
 */
public class Queen1 {
    public static void main(String[] args) {
        new Queen1().placeQueens(8);
    }

    int[] cols; // 索引是行，值是列，cols[2,3]代表第二行第三列有皇后
    int ways;

    void placeQueens(int n) {
        if (n < 1)
            return;
        cols = new int[n];
        place(0);
        System.out.println("共有 " + ways + " 种摆法");
    }

    /**
     * 从第row行开始摆放皇后
     */
    void place(int row) {
        if (row == cols.length) {
            ways++;
            show();
            return;
        }

        for (int col = 0; col < cols.length; col++) {
            if (isValid(row, col)) {
                // 在第row行第col列摆放皇后
                cols[row] = col;
                place(row + 1);
            }
        }
    }

    /**
     * 判断第row行第col列是否可以摆放皇后
     */
    boolean isValid(int row, int col) {
        for (int i = 0; i < row; i++) {
            if (cols[i] == col)
                return false;
            if (row - i == Math.abs(col - cols[i]))
                return false;
        }
        return true;
    }

    void show() {
        for (int row = 0; row < cols.length; row++) {
            for (int col = 0; col < cols.length; col++) {
                if (cols[row] == col) {
                    System.out.print("Q ");
                } else {
                    System.out.print("· ");
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------------");
    }
}

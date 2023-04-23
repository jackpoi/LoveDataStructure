package com.jackpoi;

/**
 * @author beastars
 */
public class Queen2 {
    public static void main(String[] args) {
        new Queen2().placeQueens(8);
    }

    /**
     * 数组索引是行号，数组元素是列号
     */
    int[] queens;
    /**
     * 标记着某一列是否有皇后
     */
    boolean[] cols;
    /**
     * 标记着某一斜线上是否有皇后（左上角 -> 右下角）
     * 左上角 -> 右下角的对角线索引：row – col + 7
     */
    boolean[] leftTop;
    /**
     * 标记着某一斜线上是否有皇后（右上角 -> 左下角）
     * 右上角 -> 左下角的对角线索引：row + col
     */
    boolean[] rightTop;

    int ways;

    void placeQueens(int n) {
        if (n < 1)
            return;
        queens = new int[n];
        cols = new boolean[n];
        leftTop = new boolean[(n << 1) - 1]; // 有多少个斜线
        rightTop = new boolean[leftTop.length];
        place(0);
        System.out.println("共有 " + ways + " 种摆法");
    }

    /**
     * 从第row行开始摆放皇后
     */
    void place(int row) {
        if (row == queens.length) {
            ways++;
            show();
            return;
        }

        for (int col = 0; col < queens.length; col++) {
            if (cols[col])
                continue;
            // 左上角 -> 右下角的对角线索引：row – col + 7
            int ltIdx = row - col + cols.length - 1;
            if (leftTop[ltIdx])
                continue;
            // 右上角 -> 左下角的对角线索引：row + col
            int rtIdx = row + col;
            if (rightTop[rtIdx])
                continue;

            queens[row] = col;
            cols[col] = true;
            leftTop[ltIdx] = true;
            rightTop[rtIdx] = true;
            place(row + 1);
            // 还原现场
            cols[col] = false;
            leftTop[ltIdx] = false;
            rightTop[rtIdx] = false;
        }
    }

    void show() {
        for (int row = 0; row < cols.length; row++) {
            for (int col = 0; col < cols.length; col++) {
                if (queens[row] == col) {
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

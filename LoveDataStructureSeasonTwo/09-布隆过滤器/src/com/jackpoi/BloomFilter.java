package com.jackpoi;

/**
 * @author beastars
 */
public class BloomFilter<T> {
    /**
     * 二进制向量的长度（一共有多少个二进制位）
     */
    private int bitSize;
    /**
     * 二进制向量
     */
    private long[] bits;
    /**
     * 哈希函数的个数
     */
    private int hashSize;

    /**
     * @param n 数据规模
     * @param p 误判率，取值范围为(0, 1)
     */
    public BloomFilter(int n, double p) {
        if (p >= 1 || p <= 0 || n <= 0) {
            throw new IllegalArgumentException("wrong n or p");
        }

        double ln2 = Math.log(2);
        // 求出二进制向量的长度
        bitSize = (int) (-(n * Math.log(p)) / (ln2 * ln2));
        // 哈希函数的个数
        hashSize = (int) (bitSize * ln2 / n);
        // bits数组的长度
        bits = new long[(bitSize + Long.SIZE - 1) / Long.SIZE];
    }

    /**
     * 添加元素
     */
    public boolean put(T value) {
        nullCheck(value);

        // 利用value生成两个整数
        int hash1 = value.hashCode();
        int hash2 = hash1 >>> 16;

        boolean noChanged = false;
        for (int i = 1; i <= hashSize; i++) {
            int combineHash = hash1 + (i * hash2);
            if (combineHash < 0) {
                combineHash = ~combineHash;
            }
            // 生成一个二进位的索引
            int index = combineHash % bitSize;
            // 将index位置的二进位设置为1 (1 << index)
            if (set(index))
                noChanged = true;
        }

        return noChanged;
    }

    /**
     * 判断一个元素是否存在
     */
    public boolean contains(T value) {
        nullCheck(value);

        // 利用value生成两个整数
        int hash1 = value.hashCode();
        int hash2 = hash1 >>> 16;

        for (int i = 1; i <= hashSize; i++) {
            int combineHash = hash1 + (i * hash2);
            if (combineHash < 0) {
                combineHash = ~combineHash;
            }
            // 生成一个二进位的索引
            int index = combineHash % bitSize;
            // 判断index位置的二进位是否为1，只要有一个不为1，就不存在
            if (!get(index))
                return false;
        }
        return true;
    }

    /**
     * 判断index位置的二进位是否为1
     * <br>
     * 101010101010010101<br>
     * & 000000000000000100     (1 << index)<br>
     * ------------------------<br>
     * 000000000000000100
     * <br>与上后判断是否为0，为0说明不是1，不为0说明为1
     *
     * @return true代表1, false代表0
     */
    private boolean get(int index) {
        // 对应的long值
        long value = bits[index / Long.SIZE];
        // 要或上的值，或上后将index位置的二进位设为1
        long bitValue = 1 << (index % Long.SIZE);
        return (value & bitValue) != 0;
    }

    /**
     * 设置index位置的二进位为1
     * <br>
     * 101010101010010101<br>
     * | 000000000000000100     (1 << index)<br>
     * ------------------------<br>
     * 101010111010010101
     *
     * @return true表示bit没有发生改变
     */
    private boolean set(int index) {
        // 对应的long值
        long value = bits[index / Long.SIZE];
        // 要或上的值，或上后将index位置的二进位设为1
        long bitValue = 1 << (index % Long.SIZE);
        bits[index / Long.SIZE] = value | bitValue;
        return (value & bitValue) == 0;
    }

    private void nullCheck(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must be not null.");
        }
    }
}

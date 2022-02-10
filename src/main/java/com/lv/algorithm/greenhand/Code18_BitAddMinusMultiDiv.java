package com.lv.algorithm.greenhand;

/**
 * 位数进行加减乘除
 *
 * @author： lvjiangning
 * @Date 2022/1/2 17:49
 */
public class Code18_BitAddMinusMultiDiv {
    /**
     *  考虑两部分，1、a与b相加的值，不考虑进位信息，2、a与b相加的进位
     * @param a 00000000000000000000000000000101
     * @param b 11111111111111111111111111111000
     * @return
     */
    public static int add(int a, int b) {
        int c=a;
        int sum = a;
        while (b != 0) { // 当进位信息为0后，计算结束，返回sum的值
            sum = a ^ b; //异或运算 两数相同为0，不同为1, 无进位相加 11111111111111111111111111111101
            b = (a & b) << 1; //与 两数都为1 取1，其他取0，进位信息 0000000000000000000000000000000
            a = sum;
        }
        return sum;
    }

    public static int negNum(int n) {
        return add(~n, 1);
    }

    /**
     * 减法
     * 法则:减去一个数，等于加上这个数的相反数。
     * @param a
     * @param b
     * @return
     */
    public static int minus(int a, int b) {
        // 5 , -8
        return add(a, negNum(b));
    }

    /**
     * 乘法：
     *  被乘数 分别与 二进制的乘数相差，然后相加
     *  如 100 * 1100
     *      0000
     *     0000
     *    100
     *   100
     * ----------
     *   110000
     * @param a
     * @param b
     * @return
     */
    public static int multi(int a, int b) {
        int res = 0;
        while (b != 0) { // 乘数不等于0，则需要计算
            if ((b & 1) != 0) { //与 两数都为1 取1，其他取0，进位信息，与1 则知道最右是0或1
                res = add(res, a);
            }
            a <<= 1; //左移
            b >>>= 1; //无符号 右移
        }
        return res;
    }

    public static boolean isNeg(int n) {
        return n < 0;
    }

    public static int div(int a, int b) {
        int x = isNeg(a) ? negNum(a) : a;
        int y = isNeg(b) ? negNum(b) : b;
        int res = 0;
        for (int i = 30; i >= 0; i = minus(i, 1)) {
            if ((x >> i) >= y) {
                res |= (1 << i);
                x = minus(x, y << i);
            }
        }
        return isNeg(a) ^ isNeg(b) ? negNum(res) : res;
    }

    public static int divide(int a, int b) {
        if (a == Integer.MIN_VALUE && b == Integer.MIN_VALUE) {
            return 1;
        } else if (b == Integer.MIN_VALUE) {
            return 0;
        } else if (a == Integer.MIN_VALUE) {
            if (b == negNum(1)) {
                return Integer.MAX_VALUE;
            } else {
                int c = div(add(a, 1), b);
                return add(c, div(minus(a, multi(c, b)), b));
            }
        } else {
            return div(a, b);
        }
    }

    public static void main(String[] args) {
        System.out.println(multi(5,-8));
    }
}

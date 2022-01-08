package com.lv.algorithm.greenhand;

/**
 * 阶乘
 *
 * @author： lvjiangning
 * @Date 2021/12/23 21:03
 */
public class Code02_SumOfFactorial {
    //方式1 时间复杂度O（n2）
    public static long f1(int N) {
        long ans = 0; //结果
        for (int i = 1; i <= N; i++) {
            ans += factorial(i);
        }
        return ans;
    }

    public static long factorial(int N) {
        long ans = 1;
        for (int i = 1; i <= N; i++) {
            ans *= i;
        }
        return ans;
    }
    //方法2

    /**
     * 1*2*3*4
     * 阶乘数结果=上一个结果*当前数
     * @param N
     * @return
     */
    public static long f2(int N) {
        long ans = 0;// 结果
        long cur = 1; //当前阶乘结果
        for (int i = 1; i <= N; i++) {
            cur = cur * i;
            ans += cur;
        }
        return ans;
    }

    public static void main(String[] args) {
        int N = 10;
        System.out.println(f1(N));
        System.out.println(f2(N));
    }
}

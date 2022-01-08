package com.lv.algorithm.greenhand;

/**
 * math.random等概率
 *
 * @author： lvjiangning
 * @Date 2021/12/26 16:52
 */
public class Code07_RandToRand {
    /**
     * 测试案例
     * @param args
     */
    public static void test(String[] args) {
        int testTimes = 1000000;
        int count = 0;
        //循环1百万次，如果随机数的值小于0.75则记录
        //math.random是[0,1)
        for (int i = 0; i < testTimes; i++) {
            if (Math.random() < 0.75) {
                count++;
            }
        }
        System.out.println("随机数在一百万次生成时，有多少概率是生成在0.75以下的："+(double) count / (double) testTimes);
        System.out.println("结论：随机数是等概率的");

        System.out.println("==========================");
        count = 0;
        for (int i = 0; i < testTimes; i++) {
            //随机数乘以8 ，安装左闭右开的原则，值在[0,8)之间
            if (Math.random() * 8 < 5) {
                count++;
            }
        }
        System.out.println((double) count / (double) testTimes);
        System.out.println((double) 5 / (double) 8);
        System.out.println("结论：随机数是等概率的");

        System.out.println("==========================");
        int K = 9;
        // [0,K) -> [0,8]
        int[] counts = new int[9];
        for (int i = 0; i < testTimes; i++) {
            //记录0-8之间，随机数转int后出现的次数
            int ans = (int) (Math.random() * K); // [0,K-1]
            counts[ans]++; //记录次数
        }
        for (int i = 0; i < K; i++) {
            System.out.println(i + "这个数，出现了 " + counts[i] + " 次");
        }

        System.out.println("========================");
        count = 0;
        double x = 0.17; //出现的概率
        for (int i = 0; i < testTimes; i++) {
            if (xToXPower2() < x) {
                count++;
            }
        }
        System.out.println((double) count / (double) testTimes);
        //Math.pow 得到a的b次幂
        System.out.println((double) 1 - Math.pow((double) 1 - x, 2));
    }

    // 返回[0,1)的一个小数
    // 任意的x，x属于[0,1)，[0,x)范围上的数出现概率由原来的x调整成x平方
    public static double xToXPower2() {
        //两次随机数的有一次小于x,则返回x,所以出现的概率加大
        return Math.min(Math.random(), Math.random());
    }

    public static void main(String[] args) {
         //取一百万个随机数
         int testTimes = 1000000;
         int count = 0;
        for (int i = 0; i < testTimes; i++) {
            if (f2() == 0) {
                count++;
            }
        }
        System.out.println((double) count / (double) testTimes); //出现0的概率是百分之50

        System.out.println("==========");
        //要等概率返回1-7
        int[]  counts = new int[8];
        for (int i = 0; i < testTimes; i++) {
            int num = g();
            counts[num]++;
        }
        for (int i = 0; i < 8; i++) {
            System.out.println(i + "这个数，出现了 " + counts[i] + " 次");
        }
    }

    // 随机机制，只能用f1，
    // 等概率返回0和1
    public static int f2() {
        int ans = 0;
        do {
            ans = f1();//得到返回结果
        } while (ans == 3); //如果返回值的为1，则继续循环
        return ans < 3 ? 0 : 1; //如果1-2 为0,4-5为1
    }

    /**
     * 这是一个黑盒方法，假如无法更改，只能取得返回值
     * @return 0-5
     */
    public static int f1() {
        return (int) (Math.random() * 5) + 1;
    }
    //0-6+1
    public static int g() {
        return f4() + 1;
    }

    // 0 ~ 6等概率返回一个
    public static int f4() {
        int ans = 0;
        do {
            ans = f3();
        } while (ans == 7);
        return ans;
    }

    /**
     * 得到000 ~ 111 做到等概率 0 ~ 7等概率返回一个
     * 将f2()方法返回的0与1，拼成一个二进制数，
     *  111 = 7
     *  110 = 6
     *  101 =5
     *  100 =  4
     *  011 =3
     *  001 =1
     *  000  =0
     * @return
     */
    public static int f3() {
        return (f2() << 2) + (f2() << 1) + f2();
    }

}

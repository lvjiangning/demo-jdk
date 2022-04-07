package com.lv.algorithm.system.com.lv.algorithm.system;

/**
 * @author： lvjiangning
 * @Date 2021/12/23 19:58
 */
public class Code01_Binary {
    /**
     * 打印一个数的二进制表示形式
     *
     * @param num
     */
    public static void print(int num) {
        for (int i = 31; i >= 0; i--) {
            // $ 与运算，两数对比 如果两数为1则为1，其他情况则为0,1 << i 表示左移1位
            System.out.print((num & (1 << i)) == 0 ? "0" : "1");
        }
        System.out.println();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        //  基础的二进制
        //  basicBinary();

//        两数交换
//        xorSwapNum();

//        int[] arr1 = {1, 2, 3, 3, 2, 1, 8};
//        xorPrintArrayOdd(arr1);

//        xorRight0ne(10);


//        int[] arr2 = {1, 2, 3, 3, 2, 1, 8, 1, 1, 9};
//        xorPrintTwoOddNum(arr2);

        System.out.println(Integer.toBinaryString((1<<16)-1) );

    }

    /**
     * 异或运算例子1
     * 一个数组中有一种数出现了奇数次，其他数都出现了偶数次，怎么找到并打印这种数
     */
    private static void xorPrintArrayOdd(int[] arr) {
        int answer = 0;
        for (int i = 0; i < arr.length; i++) {
            answer ^= arr[i];
        }
        System.out.println(answer);
    }

    /**
     * 异或运算例子2，
     * 怎么把一个int类型的数，提取出最右测的1来
     * 0
     *
     * @param num
     */
    private static void xorRight0ne(int num) {
     /*
         if (num ==1){
            System.out.println("最右边的1在第1位");
        }
        for (int i = 0; i < 32; i++) {
            if (((1 << i) & num) != 0) {
                System.out.println("最右边的1在下标第"+i+"位");
                break;
            }
        }*/

        print(num & -num);
    }

    /**
     * 异或运算，
     * 一个数组中有两种数出现了奇数次，其他数都出现了偶数次，怎么找到并打印这两种数
     * 1、先异或数组中所有的数，得到一个结果，结果1=奇数a^奇数b
     * 2、因为两个奇数不可能相等，则取结果1 最右侧等于1 的数作为刷选数，（因为不相等，奇数a,与奇数b只可能有一个数在n位有1）
     * 3、通过刷选数遍历数组 异或数组元素不等0,说明此元素是奇数a的候选者，与奇数异或，因为异或的特性，一个数异或偶数次会等于0 ，所以可以刷选出奇数a
     * 4、奇数a与结构1 异或能得到奇数b
     *
     * @param arr
     */
    private static void xorPrintTwoOddNum(int[] arr) {
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }

        int rightOne = eor & -eor;

        int numa = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] & rightOne) != 0) {
                numa ^= arr[i];
            }
        }

        System.out.println("第一个奇数是" + numa);
        System.out.println("第二奇数是" + (numa ^ eor));
    }

    /**
     * 异或运算
     * 一个数组中有一种数出现K次，其他数都出现了M次 ，M>1M,K<M ,找到出现了k次的数
     * 1、将数组中所有的数都转换成一个32位的（temp）int数组，模拟该数的二进制表现
     * 2、循环arr数组，将数分解成为二进制表现，分别如果1位是1，则将1加入到temp数组的0位上。
     * 3、遍历temp数组，从0位开始取，如果%m ！=0 ，则认为k位数的二进制在此位是1
     * 4、如果最后answer的结果是0，则需要判断是否0真的有k位，或者说是计算有问题
     *
     * @param arr
     * @param k
     * @param m
     */
    private static int xorPrintK(int[] arr, int k, int m) {
        int[] temp = new int[32];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < 32; j++) {
                    temp[j] += (arr[i] >> j) & 1;
            }
        }

        int answer = 0; //结果
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] % m == 0){
                continue;
            }
            if (temp[i] % m == k){ //只有一个数 是k次
               answer |= 1 << i;
            }else {
                return -1;//没有k次这个数
            }
        }
        if (answer == 0){ //k次那个数可能是0
            int count=0;
            for(int num :arr){
                if (num == 0){
                    count++;
                }
            }
            if (count != k){
                return -1;
            }
        }
        return answer;

        }



    /**
     * 异或运算 :交换两数
     * 如果引用类型（同一个地址值）进行异或预算会导致值为0
     */
    private static void xorSwapNum() {
        int a = 10, b = 10;
        a ^= b;
        b ^= a;
        a ^= b;
        System.out.println(a);
        System.out.println(b);


        int[] arr = {3};
        int i = 0, j = 0;
        arr[i] ^= arr[j];
        arr[j] ^= arr[i];
        arr[i] ^= arr[j];
        System.out.println(arr[i]);
        System.out.println(arr[j]);
        System.out.println("结论：如果引用类型（同一个地址值）进行异或预算会导致值为0");
    }

    /**
     * 二进制基础
     */
    private static void basicBinary() {


//        print(-1); //11111111111111111111111111111111
        System.out.println("结论：-1的二进制表示形式是【11111111111111111111111111111111】");

        System.out.println("=============================");


//        print(Integer.MIN_VALUE);//10000000000000000000000000000000 取反+1,最高位不管了
        System.out.println("结论：int类型的最小数【" + Integer.MIN_VALUE + "】的二进制表现【10000000000000000000000000000000】");

        System.out.println("=============================");

//        print(Integer.MAX_VALUE);
        System.out.println("结论：int类型的最大数【" + Integer.MAX_VALUE + "】的二进制表现【01111111111111111111111111111111】");

        System.out.println("=============================");

        int a = 32232;
        int c = ~a; //a取反
//        print(a); //00000000000000000111110111101000
//        print(c); //11111111111111111000001000010111
//        System.out.println(c); //-32233   所以结论： ~n+1=-n
        System.out.println("结论：正整数n的负数等于 ~n+1");

        System.out.println("=============================");
        System.out.println(~-23232);
        System.out.println("结论：负整数n的正整数等于n的绝对值-1");
        System.out.println("=============================");

        System.out.println(Integer.MIN_VALUE);
        System.out.println(~Integer.MIN_VALUE + 1);
        System.out.println("结论：Integer.MIN_VALUE 取反+1 等于自己");

        System.out.println("=============================");

    }

}

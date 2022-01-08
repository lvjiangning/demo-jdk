package com.lv.algorithm.greenhand;

/**
 * 打印二进制
 * 2进制：int 类型下，最高位为0则表示此数为一个非负数
 *                 如果为1则表示，则是一个负数
 * int 类型二进制表示：-2的31次方~+2的31次方-1
 *
 * @author： lvjiangning
 * @Date 2021/12/23 19:58
 */
public class Code01_PrintBinary {
    public static void print(int num) {
        for (int i = 31; i >= 0; i--) {
            // $ 与运算，两数对比 如果两数为1则为1，其他情况则为0,1 << i 表示左移1位
            System.out.print((num & (1 << i)) == 0 ? "0" : "1");
        }
        System.out.println();
    }

    /**
     *  ~:取反
     *  |: 或 两数其一有1取1，其他取0
     *  & :与 两数都为1 取1，其他取0
     *  ^ ：异或运算 两数相同为0，不同为1
     *  num>>> 1 ：无符号右移,相当于num除以2，1表示2的次方，都补0
     *  num>> 1 :相当于num除以2，1表示2的次方，若操作的值为正，则在高位插入0；若值为负，则在高位插入1。
     *  1 << num 左移,相当于num乘以2，1表示2的次方，
     * @param args
     */
    public static void main(String[] args) {
        //int 类型是32位的字节
        /* int i=-1; // 第32位为1，后面的数取反+1
         print(i); //11111111111111111111111111111111

         print(Integer.MIN_VALUE);//10000000000000000000000000000000 取反+1,最高位不管了
         */

         int a=32232;
         int c=~a; //a取反
         print(a ); //00000000000000000111110111101000
         print(c); //11111111111111111000001000010111
         System.out.println(c); //-32233   所以结论： ~n+1=-n
         System.out.println("结论：~n+1=-n");

         System.out.println("=============================");

         System.out.println(Integer.MIN_VALUE);
         System.out.println(~Integer.MIN_VALUE+1);
         System.out.println("结论：Integer.MIN_VALUE 取反+1 等于自己");

         System.out.println("=============================");
         int b=32512;
         print(a );
         print(b);
         print(a | b);//或 两数其一有1取1，其他取0
  	     print(a & b);//两数都为1 取1，其他取0
  		 print(a ^ b); //两数相同为0，不同为1


    }

}

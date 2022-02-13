package com.lv.algorithm.greenhand;

import java.util.Map;

/**
 * 位图
 *  实现简单的添加，删除 判断
 *
 * @author： lvjiangning
 * @Date 2022/1/2 16:34
 */
public class Code17_BitMap {
    private static Long[] bitMap;

    public Code17_BitMap(int max){
        //根据最大数创建一个位图
        bitMap=new Long[(max+64) >> 6];
    }

    public static void  add(int num){
        // num >> 6 == num /64
        // num % 64 = num & 63
        bitMap[num >> 6] |=(1L<<(num & 63));
    }

    public static void  del(int num){
        //取反
        bitMap[num >> 6] &=~(1L<<(num & 63));
    }

    public static boolean  contain(int num){
     return (bitMap[num >> 6] & (1L<<(num & 63))) != 0;
    }

    public static void main(String[] args) {

    }
}

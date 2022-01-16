package com.lv.jvm.RuntimeDataArea;

/**
 * @author lvjn
 * @version 1.0
 * @description: Increment 指令,结果的差异
 * @date 2022/1/16 14:56
 */
public class Increment {
    public static void main(String[] args) {
        int i = 8;
        //i = i++;
        i = ++i;
        System.out.println(i);
    }
}

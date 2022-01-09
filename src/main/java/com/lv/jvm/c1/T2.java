package com.lv.jvm.c1;

/**
 * 查看字节码信息
 */
public class T2 {
    public int foo() {
        int x;
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
        }
    }
}
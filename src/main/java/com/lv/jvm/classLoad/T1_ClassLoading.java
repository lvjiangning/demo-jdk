package com.lv.jvm.classLoad;

/**
 * 当Class被加载到内存中时，分别经过了
 * loading
 * linking
 * Verification:验证文件是否符合JVM规范
 * Preparation:静态变量赋初始值
 * Resolution: 将类、方法、属性等符号引用解析为直接引用
 * Initializing ：调用类初始化代码，给成员变量赋初始值
 * <p>
 * public static T t = new T()  在第一行时结果为 2
 * <p>
 * public static int count = 2; 在第一行时结果为 3
 */
public class T1_ClassLoading {
    public static void main(String[] args) {
        System.out.println(T.count);
    }
}


class T {
    public static T t = new T(); // class 加载时是默认值是null
    public static int count = 2; // count 默认为0


    private T() {
        count++;
        System.out.println("--" + count);
    }
}
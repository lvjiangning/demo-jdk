package com.lv.multithread;

/**
 * 双重检测锁
 */
public class Volatile_DCL {
    public static  volatile Volatile_DCL t5_dcl; //防止new T5_DCL时重排序 ，第2步与第三步调换

    private Volatile_DCL(){};

    public static Volatile_DCL getInstance(){
        if (t5_dcl == null){
            synchronized (Volatile_DCL.class){
                if (t5_dcl == null) {
                    /**
                     * new一个对象分为三个步骤
                     * 1、开辟内存空间
                     * 2、初始化对象
                     * 3、将对象的引用地址赋值给变量
                     */
                    /**
                     * volatile关键字会在此变量的前后添加jvm的内存屏障
                     */
                    t5_dcl = new Volatile_DCL();
                }
            }
        }
        return t5_dcl;
    }
}

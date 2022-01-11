package com.lv.jvm.classLoad;

/**
 * @author lvjn
 * @version 1.0
 * @description: Class不同类型的加载器
 * @date 2022/1/10 21:48
 */
public class T2_ClassLoading extends ClassLoader {
    /**
     * 如果类的类加载器是BootStrap，则会显示为null
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(String.class.getClassLoader());
        System.out.println(sun.awt.HKSCS.class.getClassLoader());
        System.out.println(sun.net.spi.nameservice.dns.DNSNameService.class.getClassLoader());
        System.out.println(T2_ClassLoading.class.getClassLoader());

        System.out.println(sun.net.spi.nameservice.dns.DNSNameService.class.getClassLoader().getClass().getClassLoader());
        System.out.println(T2_ClassLoading.class.getClassLoader().getClass().getClassLoader());

        System.out.println(new T2_ClassLoading().getParent());
        System.out.println(ClassLoader.getSystemClassLoader());
    }
}

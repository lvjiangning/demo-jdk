package com.lv.jvm.classLoad;

/**
 * @author lvjn
 * @version 1.0
 * @description: 类加载加载的范围
 * @date 2022/1/10 21:53
 */
public class T3_ClassLoading_Scope {
    public static void main(String[] args) {
        //bootstrap 负责加载的jar路径
        String pathBoot = System.getProperty("sun.boot.class.path");
        System.out.println(pathBoot.replaceAll(";", System.lineSeparator()));
        //Extension 加载器负责加载的路径
        System.out.println("--------------------");
        String pathExt = System.getProperty("java.ext.dirs");
        System.out.println(pathExt.replaceAll(";", System.lineSeparator()));
        //application //负责加载的路径
        System.out.println("--------------------");
        String pathApp = System.getProperty("java.class.path");
        System.out.println(pathApp.replaceAll(";", System.lineSeparator()));
    }
}

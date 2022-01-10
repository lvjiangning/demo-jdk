package com.lv.jvm.classLoad;

/**
 * @author lvjn
 * @version 1.0
 * @description: 类加载的关系
 * @date 2022/1/10 21:56
 */
public class T4_ClassLoading_ParentAndChild {
    /**
     * app.parent=ext ,ext.parent=bootstrap
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(T4_ClassLoading_ParentAndChild.class.getClassLoader());
        System.out.println(T4_ClassLoading_ParentAndChild.class.getClassLoader().getClass().getClassLoader());
        System.out.println(T4_ClassLoading_ParentAndChild.class.getClassLoader().getParent());
        System.out.println(T4_ClassLoading_ParentAndChild.class.getClassLoader().getParent().getParent());
        //  System.out.println(ClassLoading_ParentAndChild.class.getClassLoader().getParent().getParent().getParent());

    }
}

package com.lv.jvm.classLoad;

import com.lv.jdk.jdk8.Hello_jvm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author lvjn
 * @version 1.0
 * @description: 自定义 ClassLoader
 * @date 2022/1/10 21:59
 */
public class T5_ClassLoading_Custom extends ClassLoader {
    /**
     * 将包路径添加到jvm中
     *
     * @param name 包路径
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File f = new File("G:\\Code\\demo-jdk\\target\\classes/", name.replace(".", "/").concat(".class"));
        try {
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;

            while ((b = fis.read()) != 0) {
                baos.write(b);
            }

            byte[] bytes = baos.toByteArray();
            baos.close();
            fis.close();//可以写的更加严谨
            //加载字节流
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.findClass(name); //throws ClassNotFoundException
    }

    /**
     * 1、这里传递的文件名需要是类的全限定性名称，即com.pdai.jvm.classloader.Test2格式的，因为 defineClass 方法是按这种格式进行处理的。
     * 2、最好不要重写loadClass方法，因为这样容易破坏双亲委托模式。
     * 3、这类ClassLoading_Custom 类本身可以被 AppClassLoader 类加载，因此我们不能把Hello_jvm.class 放在类路径下。否则，由于双亲委托机制的存在，会直接导致该类由 AppClassLoader 加载，而不会通过我们自定义类加载器来加载。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ClassLoader l = new T5_ClassLoading_Custom();
        Class clazz = l.loadClass("com.lv.jvm.classLoad.Hello_jvm");
//        Class clazz1 = l.loadClass("com.lv.jvm.c1.Hello_jvm");

//        System.out.println(clazz == clazz1);
        //
        Hello_jvm h = (Hello_jvm) clazz.newInstance();
        h.m();

        System.out.println(l.getClass().getClassLoader());
        System.out.println(l.getParent());

        System.out.println(getSystemClassLoader());
    }
}

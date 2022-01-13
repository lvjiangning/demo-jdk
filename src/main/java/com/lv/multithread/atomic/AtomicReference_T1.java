package com.lv.multithread.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 引用地址不变，值改变，可以进行交换，
 * 此类只保证引用线程安全
 */
public class AtomicReference_T1 {

    public static void main(String[] args) {
        Student s1 = new Student(1, "张三");
        AtomicReference<Student> atomicReference=new AtomicReference<>(s1);
        System.out.println(atomicReference);
        s1=new Student(1, "张三");
        atomicReference.compareAndSet(s1,new Student(2, "张三"));
        System.out.println(atomicReference);

    }

    static class Student{
        private int age;
        private String name;

        public Student(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}

package com.lv.jdk.jdk8;

public class MyConsumer<T> {

    public void accept(T obj) {
        Person person = (Person) obj;
        System.out.println("Hello, " + person.firstName);
    }

}

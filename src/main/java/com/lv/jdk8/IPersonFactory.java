package com.lv.jdk8;

@FunctionalInterface
public interface IPersonFactory<P extends Person> {

    P create(String firstName, String lastName);

}

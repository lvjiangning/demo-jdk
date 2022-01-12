package com.lv.multithread.synchroized;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author lvjn
 * @version 1.0
 * @description: TODO
 * @date 2022/1/12 20:24
 */
public class Synchroized {
    public static void main(String[] args) {
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }
}

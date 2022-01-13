package com.lv.multithread.atomic;

import java.util.concurrent.atomic.DoubleAdder;

public class DoubleAdder_T1 {
   static DoubleAdder doubleAdder=new DoubleAdder();

    public static void main(String[] args) {
        doubleAdder.add(1); //加1
        System.out.println(doubleAdder);
        doubleAdder.reset(); //重置为0
        System.out.println(doubleAdder);
        doubleAdder.add(1); //加1
        System.out.println(doubleAdder.sumThenReset()); //返回合计后进行重置为0
    }
}

package com.lv.jvm.bytecode;

/**
 * 熟悉字节码指令集
 */
public class T2 {
    public int foo() {
        int x;
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
        }
    }
}
/*
*
 0 iconst_1  -- int类型1 入栈，栈顶=1
 1 istore_1 --  将栈顶的1出栈 赋值存入到本地变量表下标为1的变量 局部 e=1
 2 iload_1  -- 将局部变量表下标为1的入栈；栈顶=1
 3 istore_2 -- 将栈顶的1出栈 赋值给本地变量表下标为2的变量 ；局部x=1
 4 iconst_3 --
 5 istore_1
 6 iload_2
 7 ireturn
 8 astore_2
 9 iconst_2
10 istore_1
11 iload_1
12 istore_3
13 iconst_3
14 istore_1
15 iload_3
16 ireturn
17 astore 4
19 iconst_3
20 istore_1
21 aload 4
23 athrow

*
* */
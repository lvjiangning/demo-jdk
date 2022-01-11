# 工具

1. 字节码16进制查看器，IDEA插件：Bined,
1. 使用方法：File->Open As Binary->选择Class 文件，或者使用NotePad++等
2. 字节码结构查看IDEA插件：jclasslib
    1. 使用方法：选中文件（编译过的java）->View->Show Bytecode with Jclasslib

# JVM

## JVM的基础知识

### 什么是JVM？

> Java Virtual Machine，Java虚拟机，用于编译.Java文件，以及运行管理的Java程序的系统，java程序能够在不同平台运行只需要安装相对的JVM版本即可。

### 常见的JVM

1. Oracle 的Hotspot
2. IBM的J9,
3. zing vm,商业收费的，据说垃圾回收做得很牛
4. JRockit:  后被Oracle收购，其特点被吸纳如Hotspot中
5. Taobao VM:淘宝深度定制版
6. .....

## JVM整体结构

![img](.\Image\java-jvm-overview.png)

## java从编码到执行

![image-20220109203503443](.\Image\image-20220109203503443.png)

## JDK 、JRE、JVM的关系

![image-20220109203349936](.\Image\image-20220109203349936.png)

# Class文件格式

> Class文件本质上是一个以8位字节为基础单位的二进制流，各个数据项目严格按照顺序紧凑的排序在Class文件中，JVM根据其特定的规则解析该二进制数据从而得到相关信息

### Class文件16进制表现

<img src=".\Image\image-20220109205639274.png" alt="image-20220109205639274" style="zoom:150%;" />

### Class文件字节排列顺序

![image-20220109210429708](C:\Users\lvjn\AppData\Roaming\Typora\typora-user-images\image-20220109210429708.png)

> 具体信息查看思维导图 **jvmCLASS文件结构 （Java1.8）.xmind**

### Class文件字节码工具解析

![image-20220109212222910](.\Image\image-20220109212222910.png)

#### 主要属性

code内的主要属性为:

- **LineNumberTable**: 该属性的作用是描述源码行号与字节码行号(字节码偏移量)之间的对应关系。可以使用 -g:none 或-g:
  lines选项来取消或要求生成这项信息，如果选择不生成LineNumberTable，当程序运行异常时将无法获取到发生异常的源码行号，也无法按照源码的行数来调试程序。
- **LocalVariableTable**: 该属性的作用是描述帧栈中局部变量与源码中定义的变量之间的关系。
- **StackMapTable**

同理可以分析Main类中的另一个方法"inc()":

方法体内的内容是：将this入栈，获取字段#2并置于栈顶, 将int类型的1入栈，将栈内顶部的两个数值相加，返回一个int类型的值。

### 常量池标识介绍

![image-20220109212526773](.\Image\image-20220109212526773.png)

#  

# Class加载过程

## Class文件加载过程图示

![image-20220109214033650](.\Image\image-20220109214033650.png)

### 双亲委派机制图示

## ![image-20220109214334437](.\Image\image-20220109214334437.png)

### 加载过程细节描述

1. Loading

    1. 双亲委派，主要出于安全来考虑

    2. LazyLoading 五种情况

        1. –new getstatic putstatic invokestatic指令，访问final变量除外

           –java.lang.reflect对类进行反射调用时

           –初始化子类的时候，父类首先初始化

           –虚拟机启动时，被执行的主类必须初始化

           –动态语言支持java.lang.invoke.MethodHandle解析的结果为REF_getstatic REF_putstatic REF_invokestatic的方法句柄时，该类必须初始化

    3. ClassLoader的源码

        1. findInCache -> parent.loadClass -> findClass()

    4. 自定义类加载器

        1. extends ClassLoader
        2. overwrite findClass() -> defineClass(byte[] -> Class clazz)
        3. 加密
        4. parent是如何指定的，打破双亲委派，
            1. 用super(parent)指定
            2. 双亲委派的打破
                1. 如何打破：重写loadClass（）
                2. 何时打破过？
                    1. JDK1.2之前，自定义ClassLoader都必须重写loadClass()
                    2. ThreadContextClassLoader可以实现基础类调用实现类代码，通过thread.setContextClassLoader指定
                    3. 热启动，热部署
                        1. osgi tomcat 都有自己的模块指定classloader（可以加载同一类库的不同版本）

    5. 混合执行 编译执行 解释执行

        1. -Xmixed 混合执行、默认
        2. -Xint 使用编译模式，启动很快，执行稍慢
        3. -Xcomp使用纯编译模式，执行很快，启动很慢
        4. 检测热点代码：-XX:CompileThreshold = 10000
            1. 多次被调用的方法（方法技术器，监测方法执行频率）
            2. 多次被调用的循环（循环计数器，检测循环执行频率）
            3. 进行编译

2. Linking

    1. Verification
        1. 验证文件是否符合JVM规定
    2. Preparation
        1. 静态成员变量赋默认值
    3. Resolution
        1. 将类、方法、属性等符号引用解析为直接引用 常量池中的各种符号引用解析为指针、偏移量等内存地址的直接引用

3. Initializing

    1. 调用类初始化代码 ，给静态成员变量赋初始值

# Java内存模型

![image-20220109215251816](G:\Code\demo-jdk\src\resources\note\jvm\Image\image-20220109215251816.png)

## 各级缓存处理时间对比

![image-20220109215353166](.\Image\image-20220109215353166.png)

## 硬件层数据一致性

协议很多、本文主要介绍Intel使用的MESI

> 参考：https://www.cnblogs.com/z00377750/p/9180644.html

现代CPU的数据一致性实现 = 缓存锁(MESI ...) + 总线锁

如果数据在cache line 内，则使用缓存锁，如果超出64字节则使用总线锁，总线索很影响效率

## cache line

<img src=".\Image\image-20220109215809297.png" alt="image-20220109215809297" align="left" style="zoom:80%;" />

> 读取缓存以cache line为基本单位，目前64bytes
>
> 位于同一缓存行的两个不同数据，被两个不同CPU锁定，产生互相影响的伪共享问题
>
> 伪共享问题：JUC/c_028_FalseSharing
>
> 使用缓存行的对齐能够提高效率

# 内存屏障

## 乱序问题

CPU为了提高指令执行效率，会在一条指令执行过程中（比如去内存读数据（慢100倍）），去同时执行另一条指令，前提是，两条指令没有依赖关系

https://www.cnblogs.com/liushaodong/p/4777308.html

写操作也可以进行合并

https://www.cnblogs.com/liushaodong/p/4777308.html

JUC/029_WriteCombining

乱序执行的证明：JVM/jmm/Disorder.java

原始参考：https://preshing.com/20120515/memory-reordering-caught-in-the-act/

## 如何保证特定情况下不乱序

硬件内存屏障 X86

> sfence: store| 在sfence指令前的写操作当必须在sfence指令后的写操作前完成。
>
>  lfence：load | 在lfence指令前的读操作当必须在lfence指令后的读操作前完成。
>
>  mfence：modify/mix | 在mfence指令前的读写操作当必须在mfence指令后的读写操作前完成。

> 原子指令，如x86上的”lock …” 指令是一个Full Barrier，执行时会锁住内存子系统来确保执行顺序，甚至跨多个CPU。Software Locks通常使用了内存屏障或原子指令来实现变量可见性和保持程序顺序

JVM级别如何规范（JSR133）

​    **JVM的内存屏障实现是基于硬件屏障做的封装**

| **内存屏障**    | **说明**                                                    |
| --------------- | ----------------------------------------------------------- |
| StoreStore 屏障 | 禁止上面的普通写和下面的 volatile 写重排序。                |
| StoreLoad 屏障  | 防止上面的 volatile 写与下面可能有的 volatile 读/写重排序。 |
| LoadLoad 屏障   | 禁止下面所有的普通读操作和上面的 volatile 读重排序。        |
| LoadStore 屏障  | 禁止下面所有的普通写操作和上面的 volatile 读重排序。        |




## synchronized实现细节

1. 字节码层面 ACC_SYNCHRONIZED monitorenter monitorexit
2. JVM层面 C C++ 调用了操作系统提供的同步机制
3. OS和硬件层面 X86 : lock cmpxchg /
   xxx [https](https://blog.csdn.net/21aspnet/article/details/88571740)[://blog.csdn.net/21aspnet/article/details/](https://blog.csdn.net/21aspnet/article/details/88571740)[88571740](https://blog.csdn.net/21aspnet/article/details/88571740)

# JVM指令集

# JVM运行时整体图解

<img src=".\Image\0082zybply1gc6fz21n8kj30u00wpn5v.jpg" alt="jvm-framework" align="left" style="zoom:80%;" />

> - **线程私有**：程序计数器、虚拟机栈、本地方法区
> - **线程共享**：堆、方法区, 堆外内存（Java7的永久代或JDK8的元空间、代码缓存）
> - https://www.pdai.tech/md/java/jvm/java-jvm-struct.html

# 常用指令

# 问题

![image-20220109220745993](.\Image\image-20220109220745993.png)


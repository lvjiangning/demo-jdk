# 工具

1. 字节码16进制查看器，IDEA插件：Bined,
1. 使用方法：File->Open As Binary->选择Class 文件，或者使用NotePad++等
2. 字节码结构查看IDEA插件：jclasslib
    1. 使用方法：选中文件（编译过的java）->View->Show Bytecode with Jclasslib
4. jvm博客：https://www.cnblogs.com/kelthuzadx/

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





# JVM运行时图解

![image-20220116141826260](.\Image\image-20220116141826260.png)



> - **线程私有**：程序计数器、虚拟机栈、本地方法区
> - **线程共享**：堆、方法区, 堆外内存（Java7的永久代或JDK8的元空间、代码缓存）
> - https://www.pdai.tech/md/java/jvm/java-jvm-struct.html

## PC 

> 程序计数寄存器（Program Counter Register），程序计数器是一块较小的内存空间，可以看作是当前线程所执行的字节码的**行号指示器**
>

![jvm-pc-counter](.\Image\0082zybply1gc5kmznm1sj31m50u0wph.jpg)



- **使用PC寄存器存储字节码指令地址有什么用呢？为什么使用PC寄存器记录当前线程的执行地址呢？**

因为CPU需要不停的切换各个线程，这时候切换回来以后，就得知道接着从哪开始继续执行。JVM的字节码解释器就需要通过改变PC寄存器的值来明确下一条应该执行什么样的字节码指令。

- **PC寄存器为什么会被设定为线程私有的？**

多线程在一个特定的时间段内只会执行其中某一个线程方法，CPU会不停的做任务切换，这样必然会导致经常中断或恢复。为了能够准确的记录各个线程正在执行的当前字节码指令地址，所以为每个线程都分配了一个PC寄存器，每个线程都独立计算，不会互相影响。

PC总结

- 它是一块很小的内存空间，几乎可以忽略不计。也是运行速度最快的存储区域
- 在 JVM 规范中，每个线程都有它自己的程序计数器，是线程私有的，生命周期与线程的生命周期一致
- 任何时间一个线程都只有一个方法在执行，也就是所谓的**当前方法**。如果当前线程正在执行的是 Java 方法，程序计数器记录的是 JVM 字节码指令地址，如果是执行 native 方法，则是未指定值（undefined）
- 它是程序控制流的指示器，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成
- 字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令
- **它是唯一一个在 JVM 规范中没有规定任何 `OutOfMemoryError` 情况的区域**

JVM Stack

> Java 虚拟机栈(Java Virtual Machine Stacks)，早期也叫 Java 栈。每个线程在创建的时候都会创建一个虚拟机栈，其内部保存一个个的栈帧(Stack Frame），对应着一次次 Java 方法调用，是线程私有的，生命周期和线程一致。

1. Frame - 每个方法对应一个栈帧，每个栈帧中包含四项内容
   1. Local Variable Table ：局部变量表
   2. Operand Stack ：操作数栈
      对于long的处理（store and load），多数虚拟机的实现都是原子的
      jls 17.7，没必要加volatile
   3. Dynamic Linking
      https://blog.csdn.net/qq_41813060/article/details/88379473 
   4. return address
      a() -> b()，方法a调用了方法b, b方法的返回值放在什么地方

Heap

Method Area

> 实现方式有两种 

1. Perm Space (<1.8)
   字符串常量位于PermSpace
   FGC不会清理
   大小启动的时候指定，不能变
2. Meta Space (>=1.8)
   字符串常量位于堆
   会触发FGC清理
   不设定的话，最大就是物理内存

Runtime Constant Pool

> ​	运行时常量池

Native Method Stack

Direct Memory

> JVM可以直接访问的内核空间的内存 (OS 管理的内存)
>
> NIO ， 提高效率，实现zero copy

思考：

> 如何证明1.7字符串常量位于Perm，而1.8位于Heap？
>
> 提示：结合GC， 一直创建字符串常量，观察堆，和Metaspace

# 常用指令

invoke

1. InvokeStatic

2. InvokeVirtual

   > 自带多态，压栈是谁，就调用是谁 

3. InvokeInterface

4. InovkeSpecial
   可以直接定位，不需要多态的方法
   private 方法 ， 构造方法

5. InvokeDynamic
   JVM最难的指令
   lambda表达式或者反射或者其他动态语言scala kotlin，或者CGLib ASM，动态产生的class，会用到的指令

# 垃圾回收

 

# 问题

![image-20220109220745993](.\Image\image-20220109220745993.png)


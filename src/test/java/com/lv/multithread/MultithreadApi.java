package com.lv.multithread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@DisplayName("多线程相关测试")
public class MultithreadApi {

    /**
     * 对象不可变情况下，是绝对不会出现线程安全问题的
     *
     * final 关键字修饰的基本数据类型
     * String  内存地址指向本身不可以，改变String的值，是内存地址指向其他地址
     * 枚举类型  构造方法私有话
     * Number 部分子类，如 Long 和 Double 等数值包装类型，BigInteger 和 BigDecimal 等大数据类型。但同为 Number 的原子类 AtomicInteger 和 AtomicLong 则是可变的。
     */
    @DisplayName("不可变集合")
    @Test
    public void test1(){
        HashMap<String,String> map=new HashMap<>();
        map.put("1","11");
        Map<String, String> stringStringMap = Collections.unmodifiableMap(map);
        System.out.println(stringStringMap.get("1"));
        stringStringMap.put("2","2"); //直接抛异常

    }

    @Test
    public void test2(){
      int COUNT_BITS = Integer.SIZE - 3;
        System.out.println(  COUNT_BITS);

        System.out.println( -1 << 29);
    }

}

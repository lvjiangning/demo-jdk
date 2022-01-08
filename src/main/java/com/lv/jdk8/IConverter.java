package com.lv.jdk8;

/**
 * 函数式接口
 * 一个接口中只存在一个抽象方法
 * 且可以存在default方法，和object的public方法
 * https://zyc88.blog.csdn.net/article/details/86649994?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-3.no_search_link&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-3.no_search_link
 * @param <F>
 * @param <T>
 */
@FunctionalInterface
public interface IConverter<F, T> {
    /**
     * 函数式接口，将一个类型转换为另外一个类型
     * @param from
     * @return
     */
    T convert(F from);

    default String defaultMethod(){
        return "";
    }




}

package com.lv.algorithm.greenhand;

import java.util.Arrays;

/**
 * 选择排序
 *
 * @author： lvjiangning
 * @Date 2021/12/23 21:18
 */
public class Code03_SelectionSort {

    public static void main(String[] args) {
        //自定义测试
      /*  int[] sort = {5, 5, 12, 3, 5, 1, 0, 31, 35, 13, 6};
        printArray(sort);
        //selectionSort(sort);
        //
        comparator(sort);
        printArray(sort);*/

       //对数器测试
        int testTime = 500000; //测试50w次
        int maxSize = 100;
        int maxValue = 100;
        boolean succeed = true; //成功标识
        for (int i = 0; i < testTime; i++) {
            int[] arr1=generateRandomArray(maxSize,maxValue);
            int[] arr2=copyArray(arr1);
            selectionSort(arr1); //自己写的排序
            comparator(arr2); //系统的自带的排序
            if (!isEqual(arr1,arr2)){
                succeed=false;//不成功
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println(succeed ? "Nice!" : "Fucking fucked!");
        int[] arr = generateRandomArray(maxSize, maxValue);
        printArray(arr);
        selectionSort(arr);
        printArray(arr);
    }

    /**
     * 选择排序
     * 从左到右拿,拿第一个和每一个进行对比，如果第一个与第二个大于则互换
     *
     * @param arr
     */
    public static void selectionSort(int[] arr) {
        //两数双双对比，对比次数为数组长度-1
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length - 1; i++) { //只需要对比数组length-1轮
            int minIndex = i; //记录最小数的下标
            for (int j = i + 1; j < arr.length; j++) { //每一轮都已确定i之前的数，已是最小数
                if (arr[j] < arr[minIndex]) { //如果当前数小于最小数，则最小数下标更变为当前数
                    minIndex = j;
                }
            }
            swap(arr, i, minIndex);
        }
    }

    /**
     * 数组下标中的值互换
     *
     * @param array
     * @param i     互换下标的A
     * @param j     互换下标的B
     */
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * 打印数组
     *
     * @param sort
     */
    public static void printArray(int[] sort) {
        if (sort == null) {
            return;
        }
        for (int i = 0; i < sort.length; i++) {
            System.out.print(sort[i]);
            System.out.print(" ");
        }
        System.out.println();
    }

    //使用arrays对象工具排序
    public static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    //=========对数器=============

    /**
     * 数组生成器
     *
     * @param MaxSize  数组的最大长度
     * @param MaxValue 数组的最大值
     * @return
     */
    public static int[] generateRandomArray(int MaxSize, int MaxValue) {
        //假如 maxSize= 8 ,math.random是[0,1) 是左闭右开的数，不会等于1，所以8乘以随机数，只会是8以下的，所以+1，最大数才可能会是8
        int[] arr = new int[(int) ((MaxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            //允许值是正负数。
            arr[i] = (int) ((MaxValue + 1) * Math.random()) - (int) (MaxValue * Math.random());
        }
        return arr;
    }
    //复制数组
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }
    //循环对比
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }
}

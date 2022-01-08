package com.lv.algorithm.greenhand;

/**
 * 插入排序
 * while
 * for
 * 两种实现方式
 *
 * @author： lvjiangning
 * @Date 2021/12/23 21:30
 */
public class Code05_InsertionSort {
    public static void main(String[] args) {
        int[] sort = {5, 5, 12, 3, 5, 1, 0, 31, 35, 13, 6};
        printArray(sort);
        insertionSort(sort);
        printArray(sort);
    }

    /**
     * 插入排序，在有序数组中插入一个数
     *
     * @param sort
     */
    private static void insertionSort(int[] sort) {
        if (sort == null || sort.length == 1) {
            return;
        }
        for (int i = 1; i < sort.length; i++) { //0-i保证有序
            /**
             *  j = i - 1 :当前需要排序的数
             *
             */
            for (int j = i - 1; j >= 0 && sort[j] > sort[j + 1]; j--) {
                    swap(sort,j,j+1);
            }
        }
    }

    /**
     * 通过while循环实现插入排序
     * @param arr
     */
    public static void insertSort1(int[] arr){
        if (arr == null || arr.length <2){
            return;
        }

        for (int end = 1; end <arr.length ; end++) {
            int newNumIndex=end;   //end 当前已经有序的下标
            while (newNumIndex -1>=0 && arr[newNumIndex-1]> arr[newNumIndex]){
                swap(arr,newNumIndex-1,newNumIndex);
                newNumIndex--;
            }
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
    private static void printArray(int[] sort) {
        if (sort == null) {
            return;
        }
        for (int i = 0; i < sort.length; i++) {
            System.out.print(sort[i]);
            System.out.print(" ");
        }
        System.out.println();
    }
}

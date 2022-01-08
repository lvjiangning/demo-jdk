package com.lv.algorithm.greenhand;

/**
 * 冒泡排序
 *
 * @author： lvjiangning
 * @Date 2021/12/23 21:29
 */
public class Code04_BubbleSort {
    public static void main(String[] args) {
        int[] sort={5,5,12,3,5,1,0,31,35,13,6};
        printArray(sort);
        bubbleSort(sort);
        printArray(sort);
    }

    /**
     * 冒泡排序
     *
     * @param sort
     */
    private static void bubbleSort(int[] sort) {
        if (sort== null || sort.length ==1){
            return;
        }
        //循环N-1次,冒泡，最大的在后面,所以每经过一轮，最大的一个数已经确定
        for (int end = sort.length-1; end > 0; end--) {
            //只需要循环 0-end 范围内的数
            for (int j = 0; j < end; j++) {
                if (sort[j] > sort[j+1]){
                    swap(sort,j,j+1);
                }
            }

        }
    }

    /**
     * 数组下标中的值互换
     * @param array
     * @param i 互换下标的A
     * @param j 互换下标的B
     */
    private static void  swap(int[] array,int i,int j){
        int temp=array[i];
        array[i]=array[j];
        array[j]=temp;
    }

    /**
     * 打印数组
     * @param sort
     */
    private static void printArray(int[] sort) {
        if (sort== null){
            return;
        }
        for (int i = 0; i < sort.length; i++) {
            System.out.print(sort[i]);
            System.out.print(" ");
        }
        System.out.println();
    }
}

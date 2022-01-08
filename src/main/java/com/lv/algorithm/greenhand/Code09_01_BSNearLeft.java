package com.lv.algorithm.greenhand;

/**
 * 2、有序数组中找到>=num最左的位置
 *
 * @author： lvjiangning
 * @Date 2022/1/3 18:27
 */
public class Code09_01_BSNearLeft {
    public static void main(String[] args) {
        int[] array = Code03_SelectionSort.generateRandomArray(20, 20);
        Code03_SelectionSort.selectionSort(array);
        Code03_SelectionSort.printArray(array);
        int i = mostLeftNoLessNumIndex(array, 2);
        System.out.println(i);
    }

    public static int mostLeftNoLessNumIndex(int[] arr, int num) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int L = 0;
        int R = arr.length - 1;
        int ans = -1;
        while (L <= R) {
            int mid = (L + R) / 2;
            if (arr[mid] >= num) {
                ans = mid;
                R = mid - 1;
            } else {
                L = mid + 1;
            }
        }
        return ans;
    }
}

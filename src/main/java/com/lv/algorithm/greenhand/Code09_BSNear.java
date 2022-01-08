package com.lv.algorithm.greenhand;

/**
 * 1、有序数组中找到num
 *
 * 3、有序数组中找到<=num最右的位置
 *
 * @author： lvjiangning
 * @Date 2021/12/26 20:08
 */
public class Code09_BSNear {

    public static void main(String[] args) {
        //对数器进行验证二分法
        System.out.println(testFind());
    }

    public static boolean testFind() {
        int testTime = 1000;
        int maxSize = 100;
        int maxValue = 100;
        boolean result = true;
        for (int i = 0; i < testTime; i++) {
            int[] array = Code03_SelectionSort.generateRandomArray(maxSize, maxValue);
            int num = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
            Code03_SelectionSort.selectionSort(array);
            boolean b = find(array, num) == -1 ? false : true;
            if (test(array, num) != b) {
                System.out.println("出错了！");
                result = false;
                break;
            }
        }
        return result;
    }

    public static boolean test(int[] sortedArr, int num) {
        for (int cur : sortedArr) {
            if (cur == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * 1、有序数组中找到num ,二分查找法
     *
     * @param array
     * @param num
     * @return 返回下标
     * -1 表示不存在
     */
    public static int find(int[] array, int num) {
        int index = -1;
        if (array == null || array.length == 0) {
            return -1;
        }
        if (array.length == 1) {
            return array[0] == num ? 0 : index;
        }
        //得到数组的边界
        int left = 0;
        int right = array.length - 1;
        while (left <= right) { // 只要左边界没有超过有边界就要判断
            int middle = (right + left) / 2;
            if (array[middle] == num) {
                index = middle;
                break;
            }
            if (array[middle] > num) { //分割边界
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return index;
    }
}

package com.lv.algorithm.greenhand;

/**
 * 局部最小，三个数为局部，不一定要全局最小
 * 数组无序，且相邻数不相等
 *
 * @author： lvjiangning
 * @Date 2021/12/26 21:01
 */
public class Code10_BSAwesome {
    public static void main(String[] args) {
        int maxLen = 100;
        int maxValue = 200;
        int testTime = 1000000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int[] arr = randomArray(maxLen, maxValue);
            int ans = oneMinIndex(arr);
            if (!check(arr, ans)) {
                printArray(arr);
                System.out.println(ans);
                break;
            }
        }
        System.out.println("测试结束");
    }

    // 生成随机数组，且相邻数不相等
    public static int[] randomArray(int maxLen, int maxValue) {
        int len = (int) (Math.random() * maxLen);
        int[] arr = new int[len];
        if (len > 0) {
            arr[0] = (int) (Math.random() * maxValue);
            for (int i = 1; i < len; i++) {
                do {
                    arr[i] = (int) (Math.random() * maxValue);
                } while (arr[i] == arr[i - 1]);
            }
        }
        return arr;
    }

    // 也用于测试
    public static boolean check(int[] arr, int minIndex) {
        if (arr.length == 0) {
            return minIndex == -1;
        }
       try{
           int left = minIndex - 1;
           int right = minIndex + 1;
           boolean leftBigger = left >= 0 ? arr[left] > arr[minIndex] : true;
           boolean rightBigger = right < arr.length ? arr[right] > arr[minIndex] : true;
           return leftBigger && rightBigger;
       }catch (Exception e){
           System.out.println("minIndex="+minIndex);
           printArray(arr);
       }
       return false;
    }

    public static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    /**
     * 求局部最小的下标
     *
     * @param arr
     * @return
     */
    public static int oneMinIndex(int[] arr) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        int n = arr.length;
        if (n == 1) { //如果数组中只有一位，则这一位就是最小数
            return n;
        }
        if (arr[0] < arr[1]) { //如果第一位小于第二位，则直接返回第一位
            return 0;
        }
        if (arr[n - 1] < arr[n - 2]) { //判断最后一位 小于 倒数第二位
            return n - 1;
        }
        int l = 0; //最左边下标
        int r = n - 1; //最右边下标
        while (l < r - 1) {
            int mid = (l + r) / 2;
            if (arr[mid] < arr[mid - 1] && arr[mid] < arr[mid + 1]) { //中间数小于两边
                return mid;
            } else {
                if (arr[mid] > arr[mid - 1]) { //重新定义边界
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
        }
        return arr[l] < arr[r] ? l : r;

    }
}

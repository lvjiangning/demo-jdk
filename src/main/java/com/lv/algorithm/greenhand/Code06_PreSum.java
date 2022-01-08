package com.lv.algorithm.greenhand;

/**
 * 前缀和指一个数组的某下标之前的所有数组元素的和（包含其自身），数组无序有序
 *
 * @author： lvjiangning
 * @Date 2021/12/26 15:19
 */
public class Code06_PreSum {
    public static void main(String[] args) {
        int[] sort = {5, 5, 12, 3, 5, 1, 0, 31, 35, 13, 6};
        RangeSum1 rangeSum1 = new RangeSum1(sort);
        int i = rangeSum1.rangeSum(2, 4);
        System.out.println(i);
    }

    /**
     * 每次获取值都需要从L开始到R 计算一次，效率很低
     */
    public static class RangeSum1 {
        private int[] arr;

        public RangeSum1(int[] arr) {
            this.arr = arr;
        }

        //计算前缀和
        public int rangeSum(int L, int R) {
            int sum = 0;
            //确认边界
            L = L < 0 ? 0 : L;
            R = R > arr.length ? arr.length : R;
            for (int i = L; i <= R; i++) {
                sum += arr[i];
            }
            return sum;
        }
    }

    /**
     * 预处理，先处理成一个一维数组，提高效率
     */
    public static class RangeSum2 {
        //预处理的值
        private int[] preSum;

        public RangeSum2(int[] arr) {
            int N = arr.length;
            preSum = new int[N];
            preSum[0] = arr[0];
            for (int i = 1; i < N; i++) {
                preSum[i] = preSum[i - 1] + arr[i];
            }

        }

        //计算前缀和
        public int rangeSum(int L, int R) {
            return L == 0 ? preSum[R] : preSum[R] - preSum[L - 1];
        }
    }
}

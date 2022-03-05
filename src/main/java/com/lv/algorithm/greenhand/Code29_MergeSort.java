package com.lv.algorithm.greenhand;

/**
 *
 */
public class Code29_MergeSort {

	// 方法1：递归方法实现归并排序

	/**
	 * 将数组对半分，拆分分到只有1位数，到达递归结束条件 return, 2位数就开始双双merge、然后是4位 、8位
	 * @param arr
	 */
	public static void mergeSort1(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		process(arr, 0, arr.length - 1);
	}

	// arr[L...R]范围上，请让这个范围上的数，有序！
	public static void process(int[] arr, int L, int R) {
		if (L == R) { //左下标 == 右下标，表示无法再分解，结束返回此次递归
			return;
		}
		// int mid = (L + R) / 2
		int mid = L + ((R - L) >> 1); //得中间位
		process(arr, L, mid); //算中间位左边
		process(arr, mid + 1, R); //算中间位右边
		merge(arr, L, mid, R); //合并左右两边
	}

	/**
	 *
	 * @param arr 原数组
	 * @param L 左边下标
	 * @param M  中间下标
	 * @param R  右边下标
	 */
	public static void merge(int[] arr, int L, int M, int R) {
		//建一个临时数组，长度是此次合并的长度
		int[] help = new int[R - L + 1];
		int i = 0; //help的当前下标
		int p1 = L; //原数组左边开始下标
		int p2 = M + 1; //原数组右边开始下标
		while (p1 <= M && p2 <= R) {  //数组位置交换
			//每交换一个位置，p1 或者p2 都要想后移动一位
			help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
		}
		// 要么p1越界，要么p2越界
		// 不可能出现：共同越界
		while (p1 <= M) {
			help[i++] = arr[p1++];
		}
		while (p2 <= R) {
			help[i++] = arr[p2++];
		}
		//把临时数组的排序结果覆盖到原数据
		for (i = 0; i < help.length; i++) {
			arr[L + i] = help[i];
		}
	}

	/**
	 * 方式二、非递归实现
	 * 按照 2位数对比，4位数对比，8位数对比，。。。直至右边无数据
	 * @param arr
	 */
	public static void mergeSort2(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		//开始步长为1
		int step = 1;
		int N = arr.length; //数组长度，
		while (step < N) { // 步长大于数组长度则退出循环
			int L = 0; //每次重设步长进行合并时 左下标都为0
			while (L < N) { //左下标不能超过数组长度，禁止数组越界
				int M = 0; //中间位
				if (N - L >= step) {  // 如果左下标开始位至结束位够step个数，则正常取中间位
					M = L + step - 1; //取中间位
				} else {
					M = N - 1;// 如果左下标开始位至结束位不够step个数，直接取最后一位为中间位
				}
				if (M == N - 1) { //如果中间位等于最后一位，则不需要再进行合并，退出循环
					break;
				}
				int R = 0;  //每轮循环 重新计算右边下标位
				if (N - 1 - M >= step) { //如果结束位至中间位还有step的位数，够m-r
					R = M + step; //正常取右边界
				} else {
					R = N - 1; //中间位至结束位 凑不齐step位,则右下表为r
				}
				merge(arr, L, M, R); //合并
				if (R == N - 1) { //如果整个数组已经是最后一轮，则退出循环
					break;
				} else {
					L = R + 1; // 进行下一轮
				}
			}
			if (step > N / 2) {
				break;
			}
			step *= 2; //重设步长  （1 1）【2,2】【4,4】  【8,8】
		}

	}

	// 非递归方法实现
//	public static void mergeSort2(int[] arr) {
//		if (arr == null || arr.length < 2) {
//			return;
//		}
//		int N = arr.length;
//		int mergeSize = 1;
//		while (mergeSize < N) {
//			int L = 0;
//			while (L < N) {
//				if (mergeSize >= N - L) {
//					break;
//				}
//				int M = L + mergeSize - 1;
//				int R = M + Math.min(mergeSize, N - M - 1);
//				merge(arr, L, M, R);
//				L = R + 1;
//			}
//			if (mergeSize > N / 2) {
//				break;
//			}
//			mergeSize <<= 1;
//		}
//	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
		}
		return arr;
	}

	// for test
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

	// for test
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

	// for test
	public static void printArray(int[] arr) {
		if (arr == null) {
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// for test
	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 100;
		int maxValue = 100;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			mergeSort1(arr1);
			mergeSort2(arr2);
			if (!isEqual(arr1, arr2)) {
				System.out.println("出错了！");
				printArray(arr1);
				printArray(arr2);
				break;
			}
		}
		System.out.println("测试结束");
	}

}

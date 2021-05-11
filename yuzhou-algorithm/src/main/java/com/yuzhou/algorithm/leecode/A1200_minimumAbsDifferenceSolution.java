package com.yuzhou.algorithm.leecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 给你个整数数组 arr，其中每个元素都 不相同。
 * <p>
 * 请你找到所有具有最小绝对差的元素对，并且按升序的顺序返回。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：arr = [4,2,1,3]
 * 输出：[[1,2],[2,3],[3,4]]
 * 示例 2：
 * <p>
 * 输入：arr = [1,3,6,10,15]
 * 输出：[[1,3]]
 * 示例 3：
 * <p>
 * 输入：arr = [3,8,-10,23,19,-4,-14,27]
 * 输出：[[-14,-10],[19,23],[23,27]]
 *  
 * <p>
 * 提示：
 * <p>
 * 2 <= arr.length <= 10^5
 * -10^6 <= arr[i] <= 10^6
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/minimum-absolute-difference
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class A1200_minimumAbsDifferenceSolution {
    public static List<List<Integer>> minimumAbsDifference(int[] arr) {
        List<List<Integer>> result = new ArrayList<>();

        Arrays.sort(arr, 0, arr.length);
        int min = findMin(arr);

        for (int i = 0; i < arr.length - 1; i++) {
            int i1 = arr[i];
            int j = i + 1;
            int i2 = arr[j];
            if (Math.abs(i2 - i1) == min) {
                List<Integer> mList = Arrays.asList(i2, i1);
                Collections.sort(mList);
                result.add(mList);
            }
            j = 0;
        }

        return result;
    }

    private static int findMin(int[] arr) {
        int minVal = Integer.MAX_VALUE;
        for (int x = 0; x < arr.length - 1; x++) {
            int x1 = arr[x];
            int y = x + 1;
            int x2 = arr[y];
            int temp = Math.abs(x2 - x1);
            if (temp < minVal) {
                minVal = temp;
            }

        }

        return minVal;
    }

    public static void main(String[] args) {
        int[] a = {4, 2, 1, 3};
        System.out.println(findMin(a));

        List<List<Integer>> lists = minimumAbsDifference(a);
        System.out.println(lists);
    }
}
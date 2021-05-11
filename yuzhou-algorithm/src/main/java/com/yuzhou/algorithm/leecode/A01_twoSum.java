package com.yuzhou.algorithm.leecode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 * <p>
 * 示例:
 * <p>
 * 给定 nums = [2, 7, 11, 15], target = 9
 * <p>
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/two-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class A01_twoSum {
    public static int[] twoSum(int[] nums, int target) {
        //空间换时间
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }

        for (int i = 0; i < nums.length; i++) {
            int a = nums[i];
            Integer bi = map.get(target - a);
            //不能是同一个数
            if (bi != null && bi != i) {
                return new int[]{i, bi};
            }
        }
        return nums;
    }

    public static void main(String[] args) {
        int[] twoSum = twoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println(twoSum[0] + "," + twoSum[1]);
    }
}
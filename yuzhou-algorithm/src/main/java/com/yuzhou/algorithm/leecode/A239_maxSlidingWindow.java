package com.yuzhou.algorithm.leecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO
public class A239_maxSlidingWindow {
    public static Integer[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        int left = 0;
        int right = 0;

        List<Integer> result = new ArrayList<>();
        while ((right = left + k) < nums.length) {
            int max = 0;
            for (int i = left+1; i < right; i++) {
                int cur = nums[i];
                int next = nums[i - 1];
                max = Math.max(max, Math.max(cur, next));
            }
            result.add(max);

            left++;
        }
        return (Integer[]) result.toArray(new Integer[result.size()]);
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        Integer[] integers = maxSlidingWindow(nums, 3);
        System.out.println(Arrays.asList(integers));
    }
}

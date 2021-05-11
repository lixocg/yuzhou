package com.yuzhou.algorithm.leecode;

import java.util.*;

class A15_threeSum {
    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return ans;
        }
        Arrays.sort(nums);
        if (nums[0] > 0) {
            return ans;
        }

        Map<Integer, Boolean> map = new HashMap<>();
        for (int i = 0; i < nums.length - 2; i++) {
            int c = nums[i];
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                if (c > 0) {
                    break;
                }
                if (nums[left] + nums[right] + c == 0) {
                    int key = nums[left] - nums[right] - c;
                    if (!map.getOrDefault(key, false)) {
                        ans.add(Arrays.asList(c, nums[left], nums[right]));
                        map.put(key, true);
                    }
                    right--;
                    left++;
                } else {
                    if (nums[left] + nums[right] + c > 0) {
                        right--;
                    } else {
                        left++;
                    }
                }
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
//        int[] nums = new int[]{-2,0,1,1,2};
        Arrays.sort(nums);
        System.out.println(Arrays.toString(nums));
        List<List<Integer>> lists = threeSum(nums);
        System.out.println(Arrays.toString(lists.toArray()));
    }
}
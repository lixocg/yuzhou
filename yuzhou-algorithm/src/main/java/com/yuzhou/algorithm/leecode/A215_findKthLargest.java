package com.yuzhou.algorithm.leecode;

/**
 * 在未排序的数组中找到第 k 个最大的元素。请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 * <p>
 * 示例 1:
 * <p>
 * 输入: [3,2,1,5,6,4] 和 k = 2
 * 输出: 5
 * 示例 2:
 * <p>
 * 输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
 * 输出: 4
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/kth-largest-element-in-an-array
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class A215_findKthLargest {
    public static int findKthLargest(int[] nums, int k) {
        if (k > nums.length) {
            return 0;
        }
        return quickFind(nums, 0, nums.length - 1, k);
    }

    /**
     * 降序快排，递归左边部分
     */
    public static int quickFind(int[] nums, int low, int high, int k) {
        if (low > high) {
            return 0;
        }

        int left = low;
        int right = high;

        int povit = nums[left];

        while (left < right) {
            while (left < right && nums[right] <= povit) {
                right--;
            }

            while (left < right && nums[left] >= povit) {
                left++;
            }

            swap(nums, left, right);
        }

        swap(nums, low, left);
        print(nums);

        if (left == k - 1) {
            return povit;
        } else if (left > k - 1) {
            return quickFind(nums, low, left - 1, k);
        } else {
            return quickFind(nums, right + 1, high, k);
        }
    }

    public static void swap(int[] num, int i, int j) {
        int temp = num[i];
        num[i] = num[j];
        num[j] = temp;
    }

    public static void print(int[] num) {
        String s = "";
        for (int i = 0; i < num.length; i++) {
            s += num[i] + ",";
        }
        System.out.println(s);
    }

    public static void main(String[] args) {
//        int[] num = {5, 7, 8, 6, 3, 2};
        int[] num = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int res = findKthLargest(num, 4);

        System.out.println(res);

//        for (int i = 0; i < num.length; i++) {
//            System.out.println(num[i]);
//        }
    }
}

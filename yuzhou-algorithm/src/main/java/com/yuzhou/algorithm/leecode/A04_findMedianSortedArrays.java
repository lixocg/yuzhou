package com.yuzhou.algorithm.leecode;

class A04_findMedianSortedArrays {
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int left = (m + n + 1) / 2;
        int right = (m + n + 2) / 2;
        System.out.println("left=" + left + ",rigth=" + right);
        int kth = findKth(nums1, 0, nums2, 0, left);
        System.out.println(left + "th=" + kth);
        int kth1 = findKth(nums1, 0, nums2, 0, right);
        System.out.println(right + "th=" + kth1);
        return (kth + kth1) / 2.0;
    }

    static int findKth(int[] nums1, int i, int[] nums2, int j, int k) {
        if (i >= nums1.length)
            return nums2[j + k - 1];
        if (j >= nums2.length)
            return nums1[i + k - 1];
        if (k == 1)
            return Math.min(nums1[i], nums2[j]);


        int midVal1 = (i + k / 2 - 1 < nums1.length) ? nums1[i + k / 2 - 1] : Integer.MAX_VALUE;
        int midVal2 = (j + k / 2 - 1 < nums2.length) ? nums2[j + k / 2 - 1] : Integer.MAX_VALUE;
        if (midVal1 < midVal2) {
            return findKth(nums1, i + k / 2, nums2, j, k - k / 2);
        } else {
            return findKth(nums1, i, nums2, j + k / 2, k - k / 2);
        }
    }

    public static void main(String[] args) {
        int[] nums1 = new int[]{1, 2, 3, 5};
        int[] nums2 = new int[]{3, 4, 5, 6, 7};

        double medianSortedArrays = findMedianSortedArrays(nums1, nums2);
        System.out.println(medianSortedArrays);
    }
}
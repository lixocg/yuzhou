package com.yuzhou.algorithm.leecode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 给你一个整数数组 arr，请你帮忙统计数组中每个数的出现次数。
 * <p>
 * 如果每个数的出现次数都是独一无二的，就返回 true；否则返回 false。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：arr = [1,2,2,1,1,3]
 * 输出：true
 * 解释：在该数组中，1 出现了 3 次，2 出现了 2 次，3 只出现了 1 次。没有两个数的出现次数相同。
 * 示例 2：
 * <p>
 * 输入：arr = [1,2]
 * 输出：false
 * 示例 3：
 * <p>
 * 输入：arr = [-3,0,1,-3,1,1,1,-3,10,0]
 * 输出：true
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/unique-number-of-occurrences
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class A1207_uniqueOccurrences {
    public static void main(String[] args) {
        int[] arr = {26, 2, 16, 16, 5, 5, 26, 2, 5, 20, 20, 5, 2, 20, 2, 2, 20, 2, 16, 20, 16, 17, 16, 2, 16, 20, 26, 16};
        System.out.println(uniqueOccurrences(arr));
    }

    public static boolean uniqueOccurrences(int[] arr) {
        Arrays.sort(arr);
        int curIndex = 0;

        Set<Integer> existLengthSet = new HashSet<>();
        while (curIndex < arr.length) {
            int sameNumLength = findSameNumLength(curIndex, arr);
            if (existLengthSet.contains(sameNumLength)) {
                return false;
            }
            existLengthSet.add(sameNumLength);
            //从sameNumLength处开始找后面长度为sameNumLength的元素是否相同，如果相同直接返回false，不同在继续寻找
            curIndex += sameNumLength;
        }

        return true;
    }

    private static int findSameNumLength(int index, int[] arr) {
        int sameNumLength = 1;
        for (int i = index; i < arr.length; i++) {
            if (i != index) {
                int curNum = arr[i];
                int preNum = arr[i - 1];
                if (curNum != preNum) {
                    break;
                }
                sameNumLength++;
            }
        }
        return sameNumLength;
    }
}
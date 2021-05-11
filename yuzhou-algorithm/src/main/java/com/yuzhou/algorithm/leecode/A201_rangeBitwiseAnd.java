package com.yuzhou.algorithm.leecode;

/**
 * 给定范围 [m, n]，其中 0 <= m <= n <= 2147483647，返回此范围内所有数字的按位与（包含 m, n 两端点）。
 * <p>
 * 示例 1: 
 * <p>
 * 输入: [5,7]
 * 输出: 4
 * 示例 2:
 * <p>
 * 输入: [0,1]
 * 输出: 0
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/bitwise-and-of-numbers-range
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * <p>
 * <p>
 * <p>
 * ********************讲解部分：https://www.bilibili.com/video/BV1wp4y1X768
 */
public class A201_rangeBitwiseAnd {
    /**
     * 暴力算法:n 比较大时直接超时
     */
    public static int rangeBitwiseAndByForce(int m, int n) {
        int result = m;
        for (int i = m; i <= n; i++) {
            result = result & i;
        }
        return result;
    }

    ///////////////////////巧妙////////
    public static int rangeBitwiseAnd(int m, int n) {
        //先获取m，n最高位bit值
        int msb1 = msb(m);
        int msb2 = msb(n);

        //不相等 说明序列中间有可逆变动，如从011->100,011&100=0，只要序列中有一个0，整个序列&之后就0
        if (msb1 != msb2) {
            return 0;
        }

        int result = 0;

        int msb = msb1;
        while (msb >= 0) {
            int x = getBit(m, msb);
            int y = getBit(n, msb);
            //从高位开始找到第一次出现bit位不相等，此时result高位部分为相同的高位，低位全为0，直接返回
            if (x != y) {
                return result;
            } else if (x == 1) {
                //如果相同且为1，把result对应bit位置为1
                result = setBit(result, msb);
            }
            msb--;
        }
        return result;
    }

    /**
     * 求正整数m二进制最高位位置
     * 比如： 4 -> 100 --> 2
     */
    public static int msb(int m) {
        int index = 0;
        while (m > 0) {
            m = (m >> 1);
            index++;
        }
        return index - 1;
    }

    /**
     * 将正整数m二进制的第i位置为1
     * 比如 m=4,i=2
     * 4---> 100
     * i=2,1<<2---->100|100 = 100
     */
    public static int setBit(int m, int i) {
        return (1 << i) | m;
    }

    /**
     * 获取正整数m二进制第i位的bit值
     * m = 4，i = 2
     * 4----> 100
     * get(4,2) = 1
     */
    public static int getBit(int m, int i) {
        return (m >> i) & 1;
    }



    //////////////////////////另一种解法
    public static int rangeBitwiseAnd2(int m, int n) {
        int count = 0;
        while (m < n){
            m >>= 1;
            n >>= 1;
            count ++;
        }
        m <<= count;
        return m;
    }


    public static void main(String[] args) {
        System.out.println(rangeBitwiseAnd2(5, 7));
        System.out.println(rangeBitwiseAnd(5, 2147483647));
    }
}

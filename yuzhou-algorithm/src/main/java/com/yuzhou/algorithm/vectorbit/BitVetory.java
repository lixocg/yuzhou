package com.yuzhou.algorithm.vectorbit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zejian on 2017/5/13.
 * Blog : http://blog.csdn.net/javazejian [原文地址,请尊重原创]
 * 位向量存储数据
 */
public class BitVetory {
    private int count;
    private int[] a; //数组
    private int BIT_LENGTH = 32;//默认使用int类型
    private int P; //整数部分
    private int S; //余数
    private int MASK = 0x1F;// 2^5 - 1
    private int SHIFT = 5; // 2^n SHIFT=n=5 表示2^5=32 即bit位长度32

    /**
     * 初始化位向量
     *
     * @param count
     */
    public BitVetory(int count) {
        this.count = count;
        a = new int[(count - 1) / BIT_LENGTH + 1];
        init();
    }

    /**
     * 将数组中元素bit位设置为0
     */
    public void init() {
        for (int i = 0; i < count; i++) {
            clear(i);
        }
    }

    /**
     * 获取排序后的数组
     *
     * @return
     */
    public List<Integer> getSortedArray() {
        List<Integer> sortedArray = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            if (get(i) == 1) {//判断i是否存在
                sortedArray.add(i);
            }
        }
        return sortedArray;
    }

    /**
     * 置位操作,设置元素
     *
     * @param i
     */
    public void set(int i) {
        P = i >> SHIFT; //P = i / BIT_LENGTH; 取整数
        S = i & MASK; //S = i % BIT_LENGTH; 取余数
        a[P] |= 1 << S;
        //将int型变量j的第k个比特位设置为1， 即j=j|(1<<k),上述3句合并为一句
        //a[i >> SHIFT ] |= (1 << (i & MASK));
    }

    /**
     * 置0操作，相当于清除元素
     *
     * @param i
     */
    public void clear(int i) {
        P = i >> SHIFT; //计算位于数组中第？个元素 P = i / BIT_LENGTH;
        S = i & MASK;   //计算余数  S = i % BIT_LENGTH;
        a[P] &= ~(1 << S);
        //更优写法
        //将int型变量j的第k个比特位设置为0，即j= j&~(1<<k)
        //a[i>>SHIFT] &= ~(1<<(i &MASK));
    }

    /**
     * 读取操作，返回1代表该bit位有值，返回0代表该bit位没值
     *
     * @param i
     * @return
     */
    public int get(int i) {
        //a[i>>SHIFT] & (1<<(i&MASK));
        P = i >> SHIFT;
        S = i & MASK;
        return Integer.bitCount(a[P] & (1 << S));
    }

    //测试
    public static void main(String[] args) {
        int count = 25;
        List<Integer> randoms = getRandomsList(count);
        System.out.println("排序前：");
        BitVetory bitVetory = new BitVetory(count);
        for (Integer e : randoms) {
            System.out.print(e + ",");
            bitVetory.set(e);
        }
        List<Integer> sortedArray = bitVetory.getSortedArray();
        System.out.println();
        System.out.println("排序后：");
        for (Integer e : sortedArray) {
            System.out.print(e + ",");
        }
        /**
         输出结果:
         排序前：
         6,3,20,10,18,15,19,16,13,4,21,22,24,2,14,5,12,7,23,8,1,17,9,11,
         排序后：
         1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,
         */
    }

    private static List<Integer> getRandomsList(int count) {
        Random random = new Random();
        List<Integer> randomsList = new ArrayList<Integer>();
        while (randomsList.size() < (count - 1)) {
            int element = random.nextInt(count - 1) + 1;//element ∈  [1,count)
            if (!randomsList.contains(element)) {
                randomsList.add(element);
            }
        }
        return randomsList;
    }
}
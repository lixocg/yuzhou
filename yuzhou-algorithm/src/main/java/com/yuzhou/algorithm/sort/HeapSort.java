package com.yuzhou.algorithm.sort;

import com.yuzhou.Utils;

public class HeapSort {
    public static void heapAdjust(int[] arr, int parentIndex, int lenth) {
        //temp保存当前父节点
        int temp = arr[parentIndex];
        //得到左孩子(完全二叉树性质)
        int childIndex = 2 * parentIndex + 1;

        while (childIndex < lenth) {
            //如果parent有右孩子，则要判断左孩子是否小于右孩子
            if (childIndex + 1 < lenth && arr[childIndex] < arr[childIndex + 1]) {
                //拿到右孩子
                childIndex++;
            }
            //父亲节点大于子节点，就不用做交换
            if (temp >= arr[childIndex]) {
                break;
            }
            //将较大子节点的值赋给父亲节点
            arr[parentIndex] = arr[childIndex];
            //然后将子节点做为父亲节点，已防止是否破坏根堆时重新构造
            parentIndex = childIndex;
            //找到该父亲节点较小的左孩子节点
            childIndex = 2 * parentIndex + 1;
        }
        //最后将temp值赋给较大的子节点，以形成两值交换
        arr[parentIndex] = temp;
    }

    public static void heapSort(int[] arr) {
        //arr.length / 2 - 1:就是堆中父节点的个数
        for (int i = arr.length / 2 - 1; i > 0; i--) {
            heapAdjust(arr, i, arr.length);
        }
        //最后输出堆元素
        for (int i = arr.length - 1; i > 0; i--) {
            //堆顶与当前堆的第i个元素进行值对调
            Utils.swap(arr, 0, i);
            //因为两值交换，可能破坏根堆，所以必须重新构造
            heapAdjust(arr, 0, i);
        }
    }

    public static void main(String[] args) {
        int[] arr = Utils.random(20);
        heapSort(arr);
        Utils.print(arr);
    }
}

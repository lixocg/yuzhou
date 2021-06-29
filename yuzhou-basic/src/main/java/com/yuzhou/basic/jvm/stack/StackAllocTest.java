package com.yuzhou.basic.jvm.stack;

/**
 * 逃逸分析测试
 * 逃逸分析: -Xmx256M -Xms256M -XX:-DoEscapeAnalysis -XX:+PrintGCDetails
 * 标量替换：-XX:-EliminateAllocations
 * 默认是开启逃逸分析和标量替换的，可以直接将对象分配到栈上，可减少堆分配耗时和堆的GC，能大大的提高运行效率
 */
public class StackAllocTest {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        for(int i=0;i<10000000;i++){
            alloc();
        }

        long end = System.currentTimeMillis();

        System.out.println("耗时:"+(end - start));

        Thread.sleep(Integer.MAX_VALUE);
    }

    private static void alloc() {
        User u = new User();
    }

    static class User{

    }
}

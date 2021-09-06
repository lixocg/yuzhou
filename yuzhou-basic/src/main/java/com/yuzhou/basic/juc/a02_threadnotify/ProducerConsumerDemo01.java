package com.yuzhou.basic.juc.a02_threadnotify;

/**
 * 生产者消费者模式
 * 线程交替执行，A，B两个线程操作同一个变量 num = 0
 * A : if num == 0 ,num++
 * B : if num == 1, num--
 * <p>
 * 注:此代码如果超过两个线程执行时，存在线程不安全问题。存在虚假唤醒问题，所以等待需要放到while判断中
 */
public class ProducerConsumerDemo01 {
    public static void main(String[] args) {
        Data1 data = new Data1();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.incr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.decr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
    }
}

/**
 * 资源类
 * 消费者-生产者加锁模式：判断等待->执行业务->通知
 */
class Data1 {
    private int num;

    public synchronized void incr() throws Exception {
        if (num != 0) {
            //等待
            this.wait();
        }
        num++;
        System.out.println(String.format("当前+线程%s,number=%d", Thread.currentThread().getName(), num));
        //通知其他线程+1完毕
        this.notifyAll();
    }

    public synchronized void decr() throws Exception {
        if (num == 0) {
            //等待
            this.wait();
        }
        num--;
        System.out.println(String.format("当前-线程%s,number=%d", Thread.currentThread().getName(), num));
        //通知其他线程-1完毕
        this.notifyAll();
    }
}

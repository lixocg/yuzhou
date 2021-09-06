package com.yuzhou.basic.juc.a02_threadnotify;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程顺序交替执行
 */
public class ProducerConsumerDemo04 {
    public static void main(String[] args) {
        Data4 data = new Data4();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    data.printA();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();


        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    data.printB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    data.printC();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();
    }

}

class Data4 {
    private int num = 1;

    final Lock lock = new ReentrantLock();
    final Condition conA = lock.newCondition();
    final Condition conB = lock.newCondition();
    final Condition conC = lock.newCondition();

    public void printA() throws Exception {
        try {
            lock.lock();
            while (num != 1) {
                conA.await();
            }
            System.out.println(String.format("当前线程%s,number=%d", Thread.currentThread().getName(), num));
            num = 2;
            //通知其他线程+1完毕
            conB.signal();
        } finally {
            lock.unlock();
        }
    }

    public void printB() throws Exception {
        try {
            lock.lock();
            while (num != 2) {
                conB.await();
            }
            System.out.println(String.format("当前线程%s,number=%d", Thread.currentThread().getName(), num));
            num = 3;
            //通知其他线程+1完毕
            conC.signal();
        } finally {
            lock.unlock();
        }
    }

    public void printC() throws Exception {
        try {
            lock.lock();
            while (num != 3) {
                conC.await();
            }
            System.out.println(String.format("当前线程%s,number=%d", Thread.currentThread().getName(), num));
            num = 1;
            //通知其他线程+1完毕
            conA.signal();
        } finally {
            lock.unlock();
        }
    }

}

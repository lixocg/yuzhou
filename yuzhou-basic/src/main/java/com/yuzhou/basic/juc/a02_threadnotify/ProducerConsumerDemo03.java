package com.yuzhou.basic.juc.a02_threadnotify;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * JUC版本线程交替
 */
public class ProducerConsumerDemo03 {
    public static void main(String[] args) {
        Data3 data = new Data3();

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

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    data.incr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.decr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();
    }
}

class Data3 {
    private int num;

    final Lock lock = new ReentrantLock();
    final Condition condition = lock.newCondition();

    public void incr() throws Exception {
        try {
            lock.lock();
            //while循序间等待
            while (num != 0) {
                //等待
                condition.await();
            }
            num++;
            System.out.println(String.format("当前+线程%s,number=%d", Thread.currentThread().getName(), num));
            //通知其他线程+1完毕
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void decr() throws Exception {
        try {
            lock.lock();
            while (num == 0) {
                //等待
                condition.await();
            }
            num--;
            System.out.println(String.format("当前-线程%s,number=%d", Thread.currentThread().getName(), num));
            //通知其他线程-1完毕
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

package com.yuzhou.basic.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReEntrantLockDemo {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Thread t1 = new Thread(counter::add);
        Thread t2 = new Thread(counter::add);

        t1.start();
        t2.start();

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    static class Counter{

        private int count;

        Lock lock = new ReentrantLock(true);

        public int add(){
              try{
                  lock.lock();
                  count++;
                  System.out.println("获取锁执行中....");
                  try {
                      TimeUnit.SECONDS.sleep(500);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  return count;
              }finally {
                  lock.unlock();
              }
        }
    }
}

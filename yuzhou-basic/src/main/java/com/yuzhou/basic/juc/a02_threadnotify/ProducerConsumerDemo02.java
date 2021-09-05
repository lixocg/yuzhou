package com.yuzhou.basic.juc.a02_threadnotify;

/**
 * 解决虚假唤醒问题
 */
public class ProducerConsumerDemo02 {
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

//        new Thread(()->{
//            for(int i=0;i<5;i++){
//                try {
//                    data.incr();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        },"C").start();


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

class Data1 {
    private int num;

    public synchronized void incr() throws Exception {
        //while循序间等待
        while (num != 0) {
            //等待
            this.wait();
        }
        num++;
        System.out.println(String.format("当前+线程%s,number=%d", Thread.currentThread().getName(), num));
        //通知其他线程+1完毕
        this.notifyAll();
    }

    public synchronized void decr() throws Exception {
        while (num == 0) {
            //等待
            this.wait();
        }
        num--;
        System.out.println(String.format("当前-线程%s,number=%d", Thread.currentThread().getName(), num));
        //通知其他线程-1完毕
        this.notifyAll();
    }
}

package com.yuzhou.basic.juc;

import java.util.concurrent.TimeUnit;

public class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();

    public void left() {
        synchronized (left) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (right) {
                System.out.println("left....");
            }
        }
    }

    public void right() {
        synchronized (right) {
            synchronized (left) {
                System.out.println("right.....");
            }
        }
    }

    public static void main(String[] args) {
        final LeftRightDeadlock leftRightDeadlock = new LeftRightDeadlock();

        new Thread(leftRightDeadlock::left).start();

        new Thread(leftRightDeadlock::right).start();
    }
}

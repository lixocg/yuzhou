package com.yuzhou.basic.juc.a01_ticket;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicketDemo02 {

    public static void main(String[] args) {
        Ticket1 ticket = new Ticket1(50);

        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                ticket.sale();
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                ticket.sale();
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                ticket.sale();
            }
        }, "C").start();
    }
}

class Ticket1 {
    private int number;

    Lock lock = new ReentrantLock();

    public Ticket1(int number) {
        this.number = number;
    }

    public void sale() {
        try {
            lock.lock();
            if (number > 0) {
                System.out.println(String.format("售货员%s卖出第%d张票，余下%d张票", Thread.currentThread().getName(), number--, number));
            }
        } finally {
            lock.unlock();
        }
    }
}

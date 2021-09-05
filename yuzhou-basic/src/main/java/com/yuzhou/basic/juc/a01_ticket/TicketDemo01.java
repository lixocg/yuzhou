package com.yuzhou.basic.juc.a01_ticket;


public class TicketDemo01 {

    public static void main(String[] args) {
        Ticket ticket = new Ticket(50);

        new Thread(() -> {
            for(int i =0;i<50;i++) {
                ticket.sale();
            }
        }, "A").start();

        new Thread(() -> {
            for(int i =0;i<50;i++) {
                ticket.sale();
            }
        }, "B").start();

        new Thread(() -> {
            for(int i =0;i<50;i++) {
                ticket.sale();
            }
        }, "C").start();
    }


}

class Ticket {
    private int number;

    public Ticket(int number) {
        this.number = number;
    }

    public  void sale() {
        if (number > 0) {
            System.out.println(String.format("售货员%s卖出第%d张票，余下%d张票", Thread.currentThread().getName(), number--, number));
        }
    }
}

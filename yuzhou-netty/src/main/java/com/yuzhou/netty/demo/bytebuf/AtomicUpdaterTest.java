package com.yuzhou.netty.demo.bytebuf;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicUpdaterTest {
    @Test
    public void test01() throws InterruptedException {
        Person p = new Person();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(p.age++);
            });
        }

        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void test02() throws InterruptedException {
        Person p = new Person();

        final AtomicIntegerFieldUpdater<Person> updater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int andIncrement = updater.getAndIncrement(p);
                System.out.println(andIncrement);
            });
        }

        Thread.sleep(Integer.MAX_VALUE);
    }
}

class Person {
    volatile int age = 1;
}

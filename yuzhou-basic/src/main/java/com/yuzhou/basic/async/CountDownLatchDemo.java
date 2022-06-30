package com.yuzhou.basic.async;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CountDownLatchDemo {

    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {
        int num = 5;
        CountDownLatch latch = new CountDownLatch(num);

        List<Future> res = Lists.newArrayList();
        for (int i = 0; i < num; i++) {
            Future future = executorService.submit(new Task(latch));
            res.add(future);
        }

        res.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        System.out.println("-------await....");
        latch.await();
        System.out.println("end....");

    }

    static class Task implements Callable {

        private CountDownLatch latch;

        public Task(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public Object call() throws Exception {
            Thread.sleep(2000);
            latch.countDown();
            return Thread.currentThread().getName() + "-" + "hello";
        }
    }

}

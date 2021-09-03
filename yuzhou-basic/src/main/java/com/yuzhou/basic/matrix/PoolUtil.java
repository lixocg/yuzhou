package com.yuzhou.basic.matrix;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class PoolUtil {
    private final static ForkJoinPool pool = new ForkJoinPool();

    static {
        //预热
        invoke(new WarmUpTask(20));
    }

    public static <T> T invoke(ForkJoinTask<T> task) {
        return pool.invoke(task);
    }


    static class WarmUpTask extends RecursiveAction {

        int size;

        public WarmUpTask(int size) {
            this.size = size;
        }

        @Override
        protected void compute() {
            if (size < 2) {
                return;
            } else {
                size = size / 2;
                invokeAll(new WarmUpTask(size));
            }
        }
    }
}

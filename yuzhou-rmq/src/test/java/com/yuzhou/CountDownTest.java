package com.yuzhou;

import com.yuzhou.rmq.concurrent.CountDownLatch2;
import com.yuzhou.rmq.concurrent.ThreadUtils;
import com.yuzhou.rmq.utils.DateUtil;
import org.junit.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-03
 * Time: 上午1:09
 */
public class CountDownTest {

    static CountDownLatch2 latch2 = new CountDownLatch2(1);

    @Test
    public void test() throws InterruptedException {

        ScheduledExecutorService executorService = ThreadUtils.newFixedThreadScheduledPool(2, "test", false);

        executorService.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().isInterrupted());
            System.out.println(Thread.currentThread().getName() + "-----" + DateUtil.nowStr());
            System.out.println(Thread.currentThread().isAlive());
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().isAlive());
            System.out.println(Thread.currentThread().isInterrupted());
            System.out.println();
        }, 2, 3, TimeUnit.SECONDS);

        Thread.sleep(Integer.MAX_VALUE);
    }


}

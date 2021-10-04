package com.yuzhou;

import com.yuzhou.rmq.concurrent.CountDownLatch2;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-03
 * Time: 上午1:09
 */
public class CountDownTest {

    CountDownLatch2 latch2 =new CountDownLatch2(1);

    @Test
    public void test() throws InterruptedException {

        latch2.await();

        System.out.println("end");
    }

}

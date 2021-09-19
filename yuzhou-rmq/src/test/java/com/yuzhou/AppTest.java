package com.yuzhou;

import com.yuzhou.rmq.client.impl.DefaultMQConsumer;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.utils.DateUtil;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void testCg1() {
        DefaultMQConsumer consumer = new DefaultMQConsumer("mygroup", "mytopic");
        consumer.setPullBatchSize(2);
        consumer.setPullInterval(1 * 1000);
        consumer.registerMessageListener((msgs, context) -> {
            msgs.forEach(msg -> {
                System.out.println(String.format("time=%s,msgId=%s,data=%s", DateUtil.nowStr(), msg.getMsgId(), msg.getContent()));
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ConsumeStatus.CONSUME_SUCCESS;
        });
        consumer.start();

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testcg2() {
        DefaultMQConsumer consumer = new DefaultMQConsumer("cg2", "mytopic");
        consumer.setPullBatchSize(2);
        consumer.registerMessageListener((msgs, context) -> {
            msgs.forEach(msg -> {
                System.out.println(String.format("msgId=%s,data=%s", msg.getMsgId(), msg.getContent()));
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ConsumeStatus.CONSUME_SUCCESS;
        });
        consumer.start();

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

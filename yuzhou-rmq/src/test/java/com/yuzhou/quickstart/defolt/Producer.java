package com.yuzhou.quickstart.defolt;

import com.yuzhou.rmq.client.impl.DefaultMQProducer;
import com.yuzhou.rmq.common.SendResult;
import com.yuzhou.rmq.connection.SingleRedisConn;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-11
 * Time: 下午9:12
 */
public class Producer {
    public static void main(String[] args) throws InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setConnection(new SingleRedisConn());
        producer.start();

        AtomicInteger count = new AtomicInteger(1);

        IntStream.rangeClosed(1, 1).parallel().forEach(i -> {

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String, String> map = new HashMap<>();
            map.put("name", "zs" + i);
            map.put("age", i + "");
            SendResult result = null;
//            result = producer.send("mytopics1", map, i * 1000);
            result = producer.send("mytopics1", map);
            System.out.println(result + "-------" + map.get("name") + "count=" + count.getAndIncrement());
        });

    }
}

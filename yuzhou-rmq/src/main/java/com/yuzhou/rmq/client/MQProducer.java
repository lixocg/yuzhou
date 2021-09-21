package com.yuzhou.rmq.client;

import com.yuzhou.rmq.common.SendResult;

import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-17
 * Time: 下午8:03
 */
public interface MQProducer {
     void start();

    void shutdown();

    SendResult send(String topic, Map<String,String> msg);

    SendResult send(String topic, Map<String, String> msg, long delay);
}

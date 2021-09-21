package com.yuzhou.rmq.client.impl;

import com.yuzhou.rmq.Exception.IllegalMsgException;
import com.yuzhou.rmq.client.MQProducer;
import com.yuzhou.rmq.common.SendResult;
import com.yuzhou.rmq.remoting.JedisRemotingInstance;
import com.yuzhou.rmq.remoting.MQRemotingInstance;
import com.yuzhou.rmq.remoting.PutResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-18
 * Time: 下午9:46
 */
public class DefaultMQProducer implements MQProducer {

    private MQRemotingInstance<?> remotingInstance;

    /**
     * 数据key保留关键字
     */
    public static final String DELAY_RESERVED_KEY = "_delay";


    private void checkMsg(Map<String, String> msg) {
        if (msg == null || msg.size() == 0) {
            throw new IllegalMsgException("消息为空");
        }

        if (msg.containsKey(DELAY_RESERVED_KEY)) {
            throw new IllegalMsgException(String.format("%s为保留字，请更换", DELAY_RESERVED_KEY));
        }
    }

    @Override
    public void start() {
        remotingInstance = new JedisRemotingInstance();
        remotingInstance.start();
    }

    @Override
    public void shutdown() {
    }

    @Override
    public SendResult send(String topic, Map<String, String> msg) {
        checkMsg(msg);
        PutResult putResult = remotingInstance.putMsg(topic, msg);
        if (putResult != null && putResult.isSuccess()) {
            return SendResult.ok(putResult.getMsgId());
        }
        return SendResult.notOk();
    }

    @Override
    public SendResult send(String topic, Map<String, String> msg, long delay) {
        checkMsg(msg);
        msg.put("delay", String.valueOf(delay));
        PutResult putResult = remotingInstance.putDelayMsg(topic, msg, delay);
        if (putResult != null && putResult.isSuccess()) {
            return SendResult.ok(putResult.getMsgId());
        }
        return SendResult.notOk();
    }


    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.start();

        for (int i = 0; i < 100; i = i + 10) {
            Map<String, String> map = new HashMap<>();
            map.put("name", "zs" + i);
            map.put("age", i + "");
            SendResult result = null;
            if (i > 50) {
                result = producer.send("mytopic", map);
            } else {
                result = producer.send("mytopic", map, System.currentTimeMillis() + i * 1000);
            }
            System.out.println(result);
        }
    }
}

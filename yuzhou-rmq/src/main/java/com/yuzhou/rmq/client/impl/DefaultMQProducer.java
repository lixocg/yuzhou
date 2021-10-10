package com.yuzhou.rmq.client.impl;

import com.yuzhou.rmq.client.ClientConfig;
import com.yuzhou.rmq.client.MQProducer;
import com.yuzhou.rmq.common.Message;
import com.yuzhou.rmq.common.PutResult;
import com.yuzhou.rmq.common.SendResult;
import com.yuzhou.rmq.connection.Connection;
import com.yuzhou.rmq.connection.SingleRedisConn;
import com.yuzhou.rmq.exception.IllegalMsgException;
import com.yuzhou.rmq.exception.RmqException;
import com.yuzhou.rmq.factory.MQClientInstance;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 消息生产者
 * User: lixiongcheng
 * Date: 2021-09-18
 * Time: 下午9:46
 */
public class DefaultMQProducer extends ClientConfig implements MQProducer {

    private MQClientInstance mqClientInstance;

    private Connection conn;

    @Override
    public void start() {
        if (conn == null) {
            throw new RmqException("缺少conn信息");
        }
        mqClientInstance = new MQClientInstance(this,conn);
        mqClientInstance.start();
    }

    @Override
    public void shutdown() {
        mqClientInstance.shutdown();
    }

    @Override
    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    @Override
    public SendResult send(String topic, Map<String, String> msg) {
        Message message = new Message();
        message.setContent(msg);
        message.setTopic(topic);
        return send(message);
    }

    @Override
    public SendResult send(String topic, Map<String, String> msg, long delay) {
        if (delay <= 0) {
            throw new IllegalMsgException("延迟时间不能小于等于0");
        }
        Message message = new Message();
        message.setContent(msg);
        message.setTopic(topic);
        message.setDelayTime(delay);
        return send(message);
    }

    @Override
    public SendResult send(String topic, String tag, Map<String, String> msg) {
        if (StringUtils.isBlank(tag)) {
            throw new IllegalMsgException("tag不能为空");
        }
        Message message = new Message();
        message.setContent(msg);
        message.setTopic(topic);
        message.setTag(tag);
        return send(message);
    }


    @Override
    public SendResult send(String topic, String tag, Map<String, String> msg, long delay) {
        if (StringUtils.isBlank(tag)) {
            throw new IllegalMsgException("tag不能为空");
        }
        if (delay <= 0) {
            throw new IllegalMsgException("延迟时间不能小于等于0");
        }
        Message message = new Message();
        message.setContent(msg);
        message.setTopic(topic);
        message.setDelayTime(delay);
        message.setTag(tag);
        return send(message);
    }


    @Override
    public SendResult send(Message message) {
        checkMsg(message);
        PutResult putResult;

        //topic名包装一下
        message.setTopic(message.getTopic());
        if (StringUtils.isNotBlank(message.getTag())) {
            //tag标记
            message.getContent().put(ReservedKey.TAG_KEY.val, message.getTag());
        }

        if (message.getDelayTime() > 0) {
            //发送延迟消息
            message.getContent().put(ReservedKey.DELAY_KEY.val,String.valueOf(message.getDelayTime()));
            putResult = mqClientInstance.putDelayMsg(message.getTopic(), message.getContent(), message.getDelayTime());
        } else {
            //发送普通消息
            putResult = mqClientInstance.putMsg(message.getTopic(), message.getContent());
        }
        if (putResult != null && putResult.isSuccess()) {
            return SendResult.ok(putResult.getMsgId());
        }
        return SendResult.notOk();
    }


    public static void main(String[] args) throws InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setConnection(new SingleRedisConn());
        producer.start();

        AtomicInteger count = new AtomicInteger(1);

        IntStream.rangeClosed(1, 2000).parallel().forEach(i -> {

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String, String> map = new HashMap<>();
            map.put("name", "zs" + i);
            map.put("age", i + "");
            SendResult result = null;
//            result = producer.send("mytopic", map, i * 1000);
            result = producer.send("mytopics", map);
            System.out.println(result + "-------" + map.get("name") + "count=" + count.getAndIncrement());
        });

    }
}

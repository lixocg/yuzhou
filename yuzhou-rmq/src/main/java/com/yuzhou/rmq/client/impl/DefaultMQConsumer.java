package com.yuzhou.rmq.client.impl;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.consumer.DefaultMQConsumerService;
import com.yuzhou.rmq.remoting.JedisRemotingInstance;
import com.yuzhou.rmq.remoting.MQRemotingInstance;
import com.yuzhou.rmq.utils.DateUtil;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-17
 * Time: 下午8:12
 */
public class DefaultMQConsumer implements MQConfigConsumer {

    private final String topic;

    private final String group;

    private int pullBatchSize = 1;

    private MessageListener messageListener;

    private DefaultMQConsumerService defaultMQConsumerService;

    private MQRemotingInstance<?> remotingInstance;

    private long pullInterval;

    public DefaultMQConsumer(String group, String topic) {
        this.group = group;
        this.topic = topic;
    }

    @Override
    public void start() {
        //启动jedis通信实例
        remotingInstance = new JedisRemotingInstance();
        remotingInstance.start();


        //启动拉消息线程
//        pullMessageService = new PullMessageService(this, remotingInstance);
//        pullMessageService.start();

        //启动消费线程
        defaultMQConsumerService = new DefaultMQConsumerService(this, remotingInstance);
        defaultMQConsumerService.start();

    }

    @Override
    public void shutdown() {
        defaultMQConsumerService.shutdown();
    }

    @Override
    public void registerMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public void setPullBatchSize(int pullBatchSize) {
        this.pullBatchSize = pullBatchSize;
    }

    @Override
    public int pullBatchSize() {
        return this.pullBatchSize;
    }

    @Override
    public long pullInterval() {
        return pullInterval;
    }

    @Override
    public void setPullInterval(long pullInterval) {
        this.pullInterval = pullInterval;
    }

    @Override
    public MessageListener messageListener() {
        return messageListener;
    }

    @Override
    public String topic() {
        return topic;
    }

    @Override
    public String group() {
        return group;
    }

    public static void main(String[] args) {

        DefaultMQConsumer consumer = new DefaultMQConsumer("mygroup", "mytopic");
        consumer.setPullBatchSize(5);
//        consumer.setPullInterval(3 * 1000);
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
    }
}

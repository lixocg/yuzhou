package com.yuzhou.rmq.client.impl;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.consumer.DefaultMQConsumerService;
import com.yuzhou.rmq.log.InnerLog;
import com.yuzhou.rmq.remoting.DefaultPullMsgService;
import com.yuzhou.rmq.remoting.PullService;
import com.yuzhou.rmq.utils.DateUtil;
import org.slf4j.Logger;

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

    private int pullBatchSize = 32;

    private MessageListener messageListener;

    private DefaultMQConsumerService defaultMQConsumerService;

    private PullService remotingInstance;

    private long pullInterval;

    public DefaultMQConsumer(String group, String topic) {
        this.group = group;
        this.topic = topic;
    }

    @Override
    public void start() {
        //启动jedis通信实例
        remotingInstance = new DefaultPullMsgService("127.0.0.1",6379);
        remotingInstance.start();

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


   static Logger logger = InnerLog.getLogger(DefaultMQConsumer.class);

    public static void main(String[] args) {
        logger.info("000000");
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
//            return ConsumeStatus.CONSUME_SUCCESS;
            return ConsumeStatus.CONSUME_LATER;
        });
        consumer.start();
    }
}

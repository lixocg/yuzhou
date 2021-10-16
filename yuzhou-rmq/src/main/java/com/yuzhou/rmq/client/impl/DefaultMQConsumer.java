package com.yuzhou.rmq.client.impl;

import com.yuzhou.rmq.client.ClientConfig;
import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeFromWhere;
import com.yuzhou.rmq.connection.Connection;
import com.yuzhou.rmq.consumer.DefaultMQConsumerService;
import com.yuzhou.rmq.factory.MQClientInstance;
import com.yuzhou.rmq.rc.ManageCenter;
import com.yuzhou.rmq.utils.MixUtil;

import static com.yuzhou.rmq.utils.MixUtil.wrap;

/**
 * 默认消费实现
 * User: lixiongcheng
 * Date: 2021-09-17
 * Time: 下午8:12
 */
public class DefaultMQConsumer extends ClientConfig implements MQConfigConsumer {

    private final String topic;

    private final String group;

    private int pullBatchSize = 32;

    private MessageListener messageListener;

    private DefaultMQConsumerService defaultMQConsumerService;

    private MQClientInstance mqClientInstance;

    private long pullInterval;

    private Connection conn;

    private int cosumePoolCoreSize;

    private int cosumePoolMaxCoreSize;

    private ConsumeFromWhere consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET;

    public DefaultMQConsumer(String group, String topic) {
        this.topic = wrap(topic);
        this.group = wrap(group);
    }

    @Override
    public void start() {
        mqClientInstance = new MQClientInstance(this, conn);
        mqClientInstance.start();

        //启动消费线程
        defaultMQConsumerService = new DefaultMQConsumerService(this, mqClientInstance);
        defaultMQConsumerService.start();

        ManageCenter manageCenter = new ManageCenter(this, mqClientInstance);
        manageCenter.start();

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
    public ConsumeFromWhere getConsumeFromWhere() {
        return consumeFromWhere;
    }

    @Override
    public void setConsumeFromWhere(ConsumeFromWhere consumeFromWhere) {
        this.consumeFromWhere = consumeFromWhere;
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
    public int consumePoolCoreSize() {
        return cosumePoolCoreSize;
    }

    @Override
    public int consumePoolMaxCoreSize() {
        return cosumePoolMaxCoreSize;
    }


    @Override
    public void setCosumePoolCoreSize(int cosumePoolCoreSize) {
        this.cosumePoolCoreSize = cosumePoolCoreSize;
    }

    @Override
    public void setCosumePoolMaxCoreSize(int cosumePoolMaxCoreSize) {
        this.cosumePoolMaxCoreSize = cosumePoolMaxCoreSize;
    }

    @Override
    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    @Override
    public String topic() {
        return topic;
    }

    @Override
    public String group() {
        return group;
    }

    @Override
    public String name() {
        return MixUtil.getConsumerName();
    }
}

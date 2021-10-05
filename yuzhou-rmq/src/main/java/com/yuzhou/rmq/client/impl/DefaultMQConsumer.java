package com.yuzhou.rmq.client.impl;

import com.alibaba.fastjson.JSON;
import com.yuzhou.rmq.client.ClientConfig;
import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.connection.Connection;
import com.yuzhou.rmq.connection.SingleRedisConn;
import com.yuzhou.rmq.consumer.DefaultMQConsumerService;
import com.yuzhou.rmq.factory.MQClientInstance;
import com.yuzhou.rmq.log.InnerLog;
import com.yuzhou.rmq.rc.ManageCenter;
import com.yuzhou.rmq.utils.DateUtil;
import com.yuzhou.rmq.utils.MixUtil;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public DefaultMQConsumer(String group, String topic) {
        this.topic = wrap(topic);
        this.group = wrap(group);
    }

    @Override
    public void start() {
        //启动jedis通信实例
        mqClientInstance = new MQClientInstance(conn);
        mqClientInstance.start();

        //启动消费线程
        defaultMQConsumerService = new DefaultMQConsumerService(this, mqClientInstance);
        defaultMQConsumerService.start();

        ManageCenter manageCenter = new ManageCenter(this, mqClientInstance);
        manageCenter.registerConsumer(this);
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


    static Logger logger = InnerLog.getLogger(DefaultMQConsumer.class);

     static  AtomicInteger count  = new AtomicInteger(1);
    public static void main(String[] args) {
        DefaultMQConsumer consumer = new DefaultMQConsumer("mygroup", "mytopic");
        consumer.setConnection(new SingleRedisConn());
        consumer.setPullBatchSize(5);
//        consumer.setPullInterval(3 * 1000);
        consumer.registerMessageListener(new MessageListener() {
            @Override
            public ConsumeStatus onMessage(List<MessageExt> msgs, ConsumeContext context) {
                msgs.forEach(msg -> {
                    System.out.println(String.format("topic=%s,time=%s,msgId=%s,data=%s,msgCount=%d",
                            context.getTopic(),
                            DateUtil.nowStr(), msg.getMsgId(), msg.getContent(),count.getAndIncrement()));
                });
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ConsumeStatus.CONSUME_SUCCESS;
//                return ConsumeStatus.CONSUME_LATER;
            }

            @Override
            public void onMaxRetryFailMessage(List<MessageExt> msgs, ConsumeContext consumeContext) {
                System.out.println("最大重试失败:" + JSON.toJSONString(msgs));
            }
        });
        consumer.start();
    }
}

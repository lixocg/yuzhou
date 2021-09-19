package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.ServiceThread;
import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.remoting.MQRemotingInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 上午1:04
 */
public class DefaultMQConsumerService extends ServiceThread {

    Logger log = LoggerFactory.getLogger(DefaultMQConsumerService.class);

    private final MessageListener messageListener;

    private final ExecutorService consumePool = Executors.newFixedThreadPool(10);

    private final ScheduledExecutorService intervalPullMsgExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("ConsumeMessageScheduledThread_"));

    private final MQConfigConsumer mqConfigConsumer;

    private final MQRemotingInstance<?> mqRemotingInstance;

    public DefaultMQConsumerService(MQConfigConsumer mqConfigConsumer, MQRemotingInstance<?> mqRemotingInstance) {
        this.mqConfigConsumer = mqConfigConsumer;
        this.messageListener = mqConfigConsumer.messageListener();
        this.mqRemotingInstance = mqRemotingInstance;
    }

    @Override
    public void run() {
        log.info(this.getServiceName() + " service started");

        long putInterval = mqConfigConsumer.pullInterval();
        if (putInterval > 0) {
            this.intervalPullMsgExecutor.scheduleAtFixedRate(this::pullMsgHandle,
                    0,
                    mqConfigConsumer.pullInterval(), TimeUnit.MILLISECONDS);
            return;
        }

        while (!this.isStopped()) {
            try {
                pullMsgHandle();
            } catch (Exception e) {
                log.error("Pull Message Service Run Method exception", e);
            }
        }

        log.info(this.getServiceName() + " service end");
    }

    private String consumerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "UNKOWN-CONSUMER";
    }

    private void pullMsgHandle() {
        System.out.println("拉取消息中....");
        //拉取消息,Redis队列没有消息阻塞
        List<MessageExt> messageExtList = mqRemotingInstance.readMsgList(
                mqConfigConsumer.group(),
                consumerName(),
                mqConfigConsumer.topic(),
                mqConfigConsumer.pullBatchSize());

        consumePool.execute(() -> {
            try {
                ConsumeContext context = new ConsumeContext();
                ConsumeStatus consumeStatus = messageListener.consumeMessage(messageExtList, context);
                switch (consumeStatus) {
                    case CONSUME_SUCCESS:
//                                jedis.xack(topic, group, streamEntryIDS);
                        break;
                    case CONSUME_LATER:
                        //放入重试队列中
                        break;
                    default:
                        throw new RuntimeException("指定返回");
                }
            } catch (Exception e) {

            }
        });

    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        this.intervalPullMsgExecutor.shutdown();
    }

    @Override
    public String getServiceName() {
        return this.getClass().getName();
    }

    public String tryTopicName(String topic) {
        return "%retry%_" + topic;
    }


    public static void main(String[] args) {
        ScheduledExecutorService intervalPullMsgExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("ConsumeMessageScheduledThread_"));
    }
}

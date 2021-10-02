package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.common.ServiceThread;
import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.factory.MQClientInstance;
import com.yuzhou.rmq.utils.DateUtil;
import com.yuzhou.rmq.utils.MixUtil;

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

    /**
     * 间隔消息拉取定时
     */
    private final ScheduledExecutorService intervalPullMsgExecutor =
            Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("ConsumeMessageIntervalThread_"));

    /**
     * 延迟消息拉取定时
     */
    private final ScheduledExecutorService delayPullMsgExecutor =
            Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("ConsumeMessageDelayThread_"));

    private final MQConfigConsumer mqConfigConsumer;

    private final MQClientInstance mqClientInstance;

    private final boolean openIntervalPull;

    private final String topic;

    private final String group;

    private final MessageListener messageListener;

    private final String consumerName;

    private final int pullBatchSize;


    public DefaultMQConsumerService(MQConfigConsumer mqConfigConsumer, MQClientInstance mqClientInstance) {
        this.mqConfigConsumer = mqConfigConsumer;
        this.mqClientInstance = mqClientInstance;

        this.openIntervalPull = mqConfigConsumer.pullInterval() > 0;
        this.topic = mqConfigConsumer.topic();
        this.group = mqConfigConsumer.group();
        this.messageListener = mqConfigConsumer.messageListener();
        this.pullBatchSize = mqConfigConsumer.pullBatchSize();

        this.consumerName = mqConfigConsumer.name();

        this.createGroupIfNecessary();
    }

    private void createGroupIfNecessary() {
        mqClientInstance.createGroup(this.topic,this.group);
    }


    @Override
    public void run() {
        CommonMsgHandler commonMsgHandler = new CommonMsgHandler(this.messageListener);
        //启动普通消息拉取,非间隔拉取才启动该处理器
        while (!this.isStopped() && !openIntervalPull) {
            System.out.println("拉取普通消息中....");
            //拉取消息,Redis队列没有消息时阻塞
            PullResult pullResult = mqClientInstance.blockedReadMsgs(group, consumerName, topic, pullBatchSize);
            commonMsgHandler.handle(pullResult);
        }
    }

    @Override
    public String getServiceName() {
        return this.getClass().getName();
    }

    public void start() {
        super.start();

        //启动间隔消息拉取定时
        if (openIntervalPull) {
            final IntervalMsgHandler intervalMsgHandler = new IntervalMsgHandler(mqConfigConsumer.messageListener());
            this.intervalPullMsgExecutor.scheduleAtFixedRate(
                    () -> {
                        System.out.println(String.format("%s ----间隔拉取消息中", DateUtil.nowStr()));
                        PullResult pullResult = mqClientInstance.blockedReadMsgs(group, consumerName, topic, pullBatchSize);
                        intervalMsgHandler.handle(pullResult);
                    },
                    3000,
                    mqConfigConsumer.pullInterval(), TimeUnit.MILLISECONDS);
        }

        //启动定时消息拉取定时
        DelayMsgHandler delayMsgHandler = new DelayMsgHandler(this.messageListener);
        this.delayPullMsgExecutor.scheduleWithFixedDelay(
                () -> {
//                    System.out.println(String.format("%s ----定时拉取消息中,topic=%s", DateUtil.nowStr(), MixUtil.delayScoreTopic(topic)));
                    PullResult pullResult = mqClientInstance.readDelayMsgBeforeNow(group, topic);
                    delayMsgHandler.handle(pullResult);
                }
                , 2, 1, TimeUnit.SECONDS);
    }


    public void shutdown() {
        this.intervalPullMsgExecutor.shutdown();
        this.delayPullMsgExecutor.shutdown();
    }
}

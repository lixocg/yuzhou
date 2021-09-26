package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ServiceThread;
import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.remoting.PullService;

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

    private final MessageListener messageListener;

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

    private final PullService pullService;

    private final boolean openIntervalPull;

    public DefaultMQConsumerService(MQConfigConsumer mqConfigConsumer, PullService pullService) {
        this.mqConfigConsumer = mqConfigConsumer;
        this.messageListener = mqConfigConsumer.messageListener();
        this.pullService = pullService;
        this.openIntervalPull = mqConfigConsumer.pullInterval() > 0;
    }

    @Override
    public void run() {
        //启动普通消息拉取,非间隔拉取才启动该处理器
        while (!this.isStopped() && !openIntervalPull) {
            new CommonMsgHandler(mqConfigConsumer, pullService, messageListener).run();
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
            this.intervalPullMsgExecutor.scheduleAtFixedRate(
                    new DelayMsgHandler(mqConfigConsumer, pullService, messageListener),
                    3000,
                    mqConfigConsumer.pullInterval(), TimeUnit.MILLISECONDS);
        }

        //启动定时消息拉取定时
        this.delayPullMsgExecutor.scheduleWithFixedDelay(
                new DelayMsgHandler(mqConfigConsumer, pullService, messageListener)
                , 2, 1, TimeUnit.SECONDS);
    }


    public void shutdown() {
        this.intervalPullMsgExecutor.shutdown();
        this.delayPullMsgExecutor.shutdown();
    }
}

package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.log.InnerLog;
import com.yuzhou.rmq.remoting.MQRemotingInstance;
import org.slf4j.Logger;

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
public class DefaultMQConsumerService {

    Logger log = InnerLog.getLogger();

    private final MessageListener messageListener;

    private final ExecutorService consumePool = Executors.newFixedThreadPool(10);

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

    private final MQRemotingInstance mqRemotingInstance;

    private boolean openIntervalPull;

    public DefaultMQConsumerService(MQConfigConsumer mqConfigConsumer, MQRemotingInstance mqRemotingInstance) {
        this.mqConfigConsumer = mqConfigConsumer;
        this.messageListener = mqConfigConsumer.messageListener();
        this.mqRemotingInstance = mqRemotingInstance;
        this.openIntervalPull = mqConfigConsumer.pullInterval() > 0;
    }

    public void start() {
        //启动普通消息拉取,非间隔拉取才启动该处理器
        if (!openIntervalPull) {
            new CommonMsgHandler(mqConfigConsumer, mqRemotingInstance, messageListener).start();
        }

        //启动间隔消息拉取定时
        if (openIntervalPull) {
            this.intervalPullMsgExecutor.scheduleAtFixedRate(
                    new DelayMsgHandler(mqConfigConsumer, mqRemotingInstance, messageListener),
                    3000,
                    mqConfigConsumer.pullInterval(), TimeUnit.MILLISECONDS);
        }

        //启动定时消息拉取定时
        this.delayPullMsgExecutor.scheduleAtFixedRate(
                new DelayMsgHandler(mqConfigConsumer, mqRemotingInstance, messageListener)
                , 3, 2, TimeUnit.SECONDS);
    }


    public void shutdown() {
        this.intervalPullMsgExecutor.shutdown();
        this.delayPullMsgExecutor.shutdown();
    }
}

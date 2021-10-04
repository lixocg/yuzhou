package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.concurrent.ThreadUtils;
import com.yuzhou.rmq.factory.MQClientInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 上午1:04
 */
public class DefaultMQConsumerService extends AbstractMQConsumerService {
    Logger logger = LoggerFactory.getLogger(DefaultMQConsumerService.class);

    /**
     * 间隔消息拉取定时
     */
    private final ScheduledExecutorService intervalPullMsgExecutor =
            ThreadUtils.newSingleThreadScheduledExecutor("ConsumeMessageIntervalThread");

    /**
     * 延迟消息拉取定时
     */
    private final ScheduledExecutorService delayPullMsgExecutor =
            ThreadUtils.newSingleThreadScheduledExecutor("ConsumeMessageDelayThread");



    public DefaultMQConsumerService(MQConfigConsumer mqConfigConsumer, MQClientInstance mqClientInstance) {
        super(mqConfigConsumer, mqClientInstance);
        this.createGroupIfNecessary();
    }



    @Override
    public void start() {
        if (!openIntervalPull) {
            super.start();
        }

        //启动间隔消息拉取定时
        if (openIntervalPull) {
            IntervalMsgConsumerService intervalMsgConsumerService =
                    new IntervalMsgConsumerService(mqConfigConsumer, mqClientInstance);
            this.intervalPullMsgExecutor.scheduleAtFixedRate(
                    intervalMsgConsumerService,
                    3000,
                    mqConfigConsumer.pullInterval(), TimeUnit.MILLISECONDS);
        }

        //启动定时消息拉取定时
        this.delayPullMsgExecutor.scheduleWithFixedDelay(
                new DelayMsgConsumerService(mqConfigConsumer, mqClientInstance), 2, 1, TimeUnit.SECONDS);
    }

    @Override
    public String getServiceName() {
        return this.getClass().getSimpleName();
    }
}

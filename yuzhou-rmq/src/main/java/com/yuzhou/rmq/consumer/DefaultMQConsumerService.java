package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.concurrent.ThreadUtils;
import com.yuzhou.rmq.factory.MQClientInstance;
import com.yuzhou.rmq.log.InnerLog;
import org.slf4j.Logger;

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

    Logger logger = InnerLog.getLogger(DefaultMQConsumerService.class);

    /**
     * 间隔消息拉取定时
     */
    private final ScheduledExecutorService intervalPullMsgExecutor =
            ThreadUtils.newSingleThreadScheduledExecutor("IntervalConsumeMessageThread");

    /**
     * 延迟消息拉取定时
     */
    private final ScheduledExecutorService delayPullMsgExecutor =
            ThreadUtils.newSingleThreadScheduledExecutor("DelayConsumeMessageThread");

    /**
     * pendding消息拉取定时
     */
    private final ScheduledExecutorService pendingPullMsgExecutor =
            ThreadUtils.newSingleThreadScheduledExecutor("ConsumePendingMessageThread");


    public DefaultMQConsumerService(MQConfigConsumer mqConfigConsumer, MQClientInstance mqClientInstance) {
        super(mqConfigConsumer, mqClientInstance);
        this.createGroupIfNecessary();
    }

    @Override
    public void run() {
        //启动普通消息拉取,非间隔拉取才启动该处理器
        logger.info("启动普通消息拉取服务,服务名;{},服务状态:{}", this.getServiceName(), this.isRunning());
        while (!this.isStopped() && !openIntervalPull) {
            run0();
        }
        logger.info("停止普通消息拉取服务,服务名;{},服务状态:{}", this.getServiceName(), this.isRunning());
    }

    @Override
    protected PullResult pullResult() {
        return mqClientInstance.blockedReadMsgs(group, consumerName, topic, pullBatchSize);
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

        //启动处理pending消息定时
        this.pendingPullMsgExecutor.scheduleAtFixedRate(
                new PendingMsgConsumerService(mqConfigConsumer, mqClientInstance), 5, 20, TimeUnit.SECONDS);
    }

    @Override
    public String getServiceName() {
        return this.getClass().getSimpleName();
    }
}

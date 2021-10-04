package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.exception.RmqException;
import com.yuzhou.rmq.factory.MQClientInstance;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 上午1:04
 */
public class IntervalMsgConsumerService extends AbstractMQConsumerService {

    public IntervalMsgConsumerService(MQConfigConsumer mqConfigConsumer, MQClientInstance mqClientInstance) {
        super(mqConfigConsumer, mqClientInstance);
        this.createGroupIfNecessary();
    }

    @Override
    public void run() {
        //启动普通消息拉取,非间隔拉取才启动该处理器
        if (msgHandler.isBusy()) {
            logger.warn("消费线程池处于繁忙中，等待任务处理中.....");
            waitForRunning(Long.MAX_VALUE);
        }

        if (this.isStopped()) {
            logger.warn("拉取服务已停止...服务名:" + this.getServiceName());
            return;
        }

//        logger.info("拉取消息中,服务名;{},服务状态:{}", this.getServiceName(), this.isStopped());
        PullResult pullResult = mqClientInstance.blockedReadMsgs(group, consumerName, topic, pullBatchSize);
        msgHandler.handle(pullResult);
    }

    @Override
    public String getServiceName() {
        return this.getClass().getName();
    }

    @Override
    public void start() {
        throw new RmqException("不需要启动");
    }
}

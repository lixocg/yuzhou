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
        run0();
    }

    @Override
    protected PullResult pullResult() {
        return mqClientInstance.blockedReadMsgs(group, consumerName, topic, pullBatchSize);
    }

    @Override
    public String getServiceName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void start() {
        throw new RmqException("不需要启动");
    }
}

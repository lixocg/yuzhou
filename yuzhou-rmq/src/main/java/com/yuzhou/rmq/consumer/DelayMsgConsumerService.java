package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.exception.RmqException;
import com.yuzhou.rmq.factory.MQClientInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 上午1:04
 */
public class DelayMsgConsumerService extends AbstractMQConsumerService {

    public DelayMsgConsumerService(MQConfigConsumer mqConfigConsumer, MQClientInstance mqClientInstance) {
        super(mqConfigConsumer, mqClientInstance);
    }


    @Override
    public void run() {
        run0();
    }

    @Override
    protected PullResult pullResult() {
        return mqClientInstance.readDelayMsgBeforeNow(group, topic);
    }

    @Override
    public void start() {
        throw new RmqException("不需要启动");
    }

    @Override
    public String getServiceName() {
        return this.getClass().getSimpleName();
    }

}

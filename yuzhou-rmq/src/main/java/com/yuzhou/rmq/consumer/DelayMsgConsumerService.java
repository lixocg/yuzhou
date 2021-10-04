package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.exception.RmqException;
import com.yuzhou.rmq.factory.MQClientInstance;
import com.yuzhou.rmq.utils.DateUtil;
import com.yuzhou.rmq.utils.MixUtil;
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

    Logger logger = LoggerFactory.getLogger(DelayMsgConsumerService.class);

    public DelayMsgConsumerService(MQConfigConsumer mqConfigConsumer, MQClientInstance mqClientInstance) {
        super(mqConfigConsumer, mqClientInstance);
    }


    @Override
    public void run() {
        if (msgHandler.isBusy()) {
            logger.warn("消费线程池处于繁忙中，等待任务处理中.....");
            waitForRunning(Long.MAX_VALUE);
        }

        if (this.isStopped()) {
            logger.warn("拉取服务已停止...服务名:" + this.getServiceName());
            return;
        }

        logger.info("定时拉取消息中,topic={}", MixUtil.delayScoreTopic(topic));
        PullResult pullResult = mqClientInstance.readDelayMsgBeforeNow(group, topic);
        msgHandler.handle(pullResult);
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

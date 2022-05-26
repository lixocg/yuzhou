package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.concurrent.ServiceThread;
import com.yuzhou.rmq.consumer.handler.DefaultMsgHandler;
import com.yuzhou.rmq.consumer.handler.MsgHandler;
import com.yuzhou.rmq.exception.RmqException;
import com.yuzhou.rmq.factory.MQClientInstance;
import com.yuzhou.rmq.log.InnerLog;
import org.slf4j.Logger;

/**
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 上午1:04
 */
public abstract class AbstractMQConsumerService extends ServiceThread implements MQConsumerService {

    Logger logger = InnerLog.getLogger(AbstractMQConsumerService.class);

    protected final MQConfigConsumer mqConfigConsumer;

    protected final MQClientInstance mqClientInstance;

    protected final boolean openIntervalPull;

    protected String topic;

    protected final String group;

    protected final MessageListener messageListener;

    protected final String consumerName;

    protected final int pullBatchSize;

    protected final MsgHandler msgHandler;

    public AbstractMQConsumerService(MQConfigConsumer mqConfigConsumer,
                                     MQClientInstance mqClientInstance) {
        this.mqConfigConsumer = mqConfigConsumer;
        this.mqClientInstance = mqClientInstance;

        this.openIntervalPull = mqConfigConsumer.pullInterval() > 0;

        this.topic = mqConfigConsumer.topic();
        this.group = mqConfigConsumer.group();

        this.messageListener = mqConfigConsumer.messageListener();
        this.pullBatchSize = mqConfigConsumer.pullBatchSize();

        this.consumerName = mqConfigConsumer.name();

        msgHandler = new DefaultMsgHandler(this, messageListener);
    }

    protected void createGroupIfNecessary() {
        synchronized (AbstractMQConsumerService.class) {
            if (!mqClientInstance.createGroup(this.topic, this.group)) {
                throw new RmqException(String.format("消费组创建失败，topic=%s,group=%s", topic, group));
            }
        }
    }


    @Override
    public abstract void run();

    protected void run0() {
        try {
            if (msgHandler.isBusy()) {
                logger.info("消费线程池处于繁忙中，等待任务处理中.....");
                waitForRunning();
            }

            if (this.isStopped()) {
                logger.info("拉取服务已停止...服务名:" + this.getServiceName());
                return;
            }

            //拉取消息,Redis队列没有消息时阻塞
            logger.info("拉取消息中,服务名;{},服务状态:{}", this.getServiceName(), this.isRunning());
            PullResult pullResult = pullResult();
            msgHandler.handle(pullResult);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    protected abstract PullResult pullResult();


    @Override
    public void shutdown() {
        super.shutdown();
        mqClientInstance.shutdown();
    }

    @Override
    public int poolSize() {
        if (mqConfigConsumer.consumePoolCoreSize() == 0) {
            return Runtime.getRuntime().availableProcessors();
        }
        return mqConfigConsumer.consumePoolCoreSize();
    }

    @Override
    public int maxPoolSize() {
        if (mqConfigConsumer.consumePoolMaxCoreSize() == 0) {
            return Runtime.getRuntime().availableProcessors();
        }
        return mqConfigConsumer.consumePoolMaxCoreSize();
    }
}

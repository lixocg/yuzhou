package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.common.ServiceThread;
import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.consumer.handler.DefaultMsgHandler;
import com.yuzhou.rmq.consumer.handler.MsgHandler;
import com.yuzhou.rmq.factory.MQClientInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class AbstractMQConsumerService extends ServiceThread implements MQConsumerService {

    Logger logger = LoggerFactory.getLogger(AbstractMQConsumerService.class);


    protected final MQConfigConsumer mqConfigConsumer;

    protected final MQClientInstance mqClientInstance;

    protected final boolean openIntervalPull;

    protected final String topic;

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
        mqClientInstance.createGroup(this.topic, this.group);
    }


    @Override
    public void run() {
        //启动普通消息拉取,非间隔拉取才启动该处理器
        while (!this.isStopped() && !openIntervalPull) {
            if (msgHandler.isBusy()) {
                logger.warn("消费线程池处于繁忙中，等待任务处理中.....");
                waitForRunning(Long.MAX_VALUE);
            }

            if (this.isStopped()) {
                logger.warn("拉取服务已停止...服务名:" + this.getServiceName());
                return;
            }

            //拉取消息,Redis队列没有消息时阻塞
            logger.info("拉取消息中,服务名;{},服务状态:{}", this.getServiceName(), this.isStopped());
            PullResult pullResult = mqClientInstance.blockedReadMsgs(group, consumerName, topic, pullBatchSize);
            msgHandler.handle(pullResult);
        }
        logger.info("stop..........");
    }


    @Override
    public void shutdown() {
        super.shutdown();
        mqClientInstance.shutdown();
    }

    @Override
    public int poolSize() {
        return 0;
    }

    @Override
    public int maxPoolSize() {
        return 0;
    }
}
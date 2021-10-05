package com.yuzhou.rmq.consumer.handler;

import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.concurrent.ThreadUtils;
import com.yuzhou.rmq.consumer.MQConsumerService;
import com.yuzhou.rmq.factory.ProcessCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象消息处理器，优雅关闭：保证消息不丢，线程繁忙检测：保障消息不被拒绝处理
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:12
 */
public abstract class AbstractMsgHandler implements MsgHandler {

    Logger logger = LoggerFactory.getLogger(AbstractMsgHandler.class);

    private final static int CONSUMER_QUEUE_SIZE = 10;

    public final BlockingQueue<Runnable> consumeMsgPoolQueue;

    private final ExecutorService consumeMsgExecutor;

    public final MessageListener messageListener;

    /**
     * 处理器处理任务状态，繁忙/空闲
     */
    public final AtomicBoolean isBusy = new AtomicBoolean(false);

    public final MQConsumerService mqConsumerService;

    public AbstractMsgHandler(MQConsumerService mqConsumerService, MessageListener messageListener) {
        this.mqConsumerService = mqConsumerService;
        this.messageListener = messageListener;
        this.consumeMsgPoolQueue = new ArrayBlockingQueue<>(CONSUMER_QUEUE_SIZE);
        this.consumeMsgExecutor = ThreadUtils.newThreadPoolExecutor(
                mqConsumerService.poolSize(),
                mqConsumerService.maxPoolSize(),
                1000 * 60,
                TimeUnit.MILLISECONDS,
                this.consumeMsgPoolQueue,
                "defaultConsumeMsgExecutor_");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("shutdown hook execute........");
            //关闭拉取消息线程，不拉取新的消息
            mqConsumerService.makeStop();
            logger.info("mqConsumerService name={}, status={}", mqConsumerService.getServiceName(), mqConsumerService.isRunning());
            //优雅关闭
            ThreadUtils.shutdownGracefully(consumeMsgExecutor, 30, TimeUnit.SECONDS);
        }));
    }

    @Override
    public boolean isBusy() {
        return this.isBusy.get();
    }

    @Override
    public boolean markBusy() {
        return isBusy.compareAndSet(false, true);
    }

    @Override
    public boolean markIdle() {
        return isBusy.compareAndSet(true, false);
    }


    @Override
    public ExecutorService executorService() {
        return consumeMsgExecutor;
    }

    @Override
    public void handle(PullResult pullResult) {
        List<MessageExt> messageExts = pullResult.messageExts();

        if (messageExts == null || messageExts.size() == 0) {
            return;
        }

        if (consumeMsgPoolQueue.size() == CONSUMER_QUEUE_SIZE - 1) {
            markBusy();
        }

        ProcessCallback processCallback = pullResult.processCallback();
        ProcessCallback.Context processCallbackCxt = new ProcessCallback.Context();
        processCallbackCxt.setTopic(pullResult.topic());
        processCallbackCxt.setMessageExts(pullResult.messageExts());
        processCallbackCxt.setGroup(pullResult.group());
        processCallbackCxt.setMessageListener(this.messageListener);
        this.consumeMsgExecutor.execute(() -> {
            try {
                ConsumeContext context = new ConsumeContext();
                context.setTopic(pullResult.topic());
                ConsumeStatus consumeStatus = messageListener.onMessage(messageExts, context);
                if (consumeStatus == ConsumeStatus.CONSUME_LATER) {
                    processCallback.onFail(processCallbackCxt);
                } else {
                    processCallback.onSuccess(processCallbackCxt);
                }
                while (isBusy() && consumeMsgPoolQueue.isEmpty() && markIdle()) {
                    logger.warn("任务处理完成，唤醒.....");
                    this.mqConsumerService.wakeup();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

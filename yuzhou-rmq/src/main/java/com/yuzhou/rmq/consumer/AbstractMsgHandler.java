package com.yuzhou.rmq.consumer;

import com.sun.jmx.snmp.tasks.ThreadService;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.CountDownLatch2;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.common.ServiceThread;
import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.factory.ProcessCallback;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:12
 */
public abstract class AbstractMsgHandler implements MsgHandler {

    private final BlockingQueue<Runnable> defaultConsumeMsgPoolQueue;

    public final ExecutorService defaultConsumeMsgExecutor;

    public final MessageListener messageListener;

    private final static int MAX_CONSUMER_TASK_SIZE = 100;

    public final AtomicBoolean waiting = new AtomicBoolean(false);

    public final  DefaultMQConsumerService mqConsumerService;

    public AbstractMsgHandler(DefaultMQConsumerService mqConsumerService, MessageListener messageListener) {
        this.mqConsumerService = mqConsumerService;
        this.messageListener = messageListener;
        this.defaultConsumeMsgPoolQueue = new ArrayBlockingQueue<>(MAX_CONSUMER_TASK_SIZE);
        this.defaultConsumeMsgExecutor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                1000 * 60,
                TimeUnit.MILLISECONDS,
                this.defaultConsumeMsgPoolQueue,
                new ThreadFactoryImpl("defaultConsumeMsgExecutor_"));
    }


    @Override
    public void handle(PullResult pullResult) {
        List<MessageExt> messageExts = pullResult.messageExts();

        if (messageExts == null || messageExts.size() == 0) {
            return;
        }

        System.out.println("size======" + this.defaultConsumeMsgPoolQueue.size());
        if (defaultConsumeMsgPoolQueue.size() == MAX_CONSUMER_TASK_SIZE-1) {
            waiting.compareAndSet(false, true);
        }

        ProcessCallback processCallback = pullResult.processCallback();
        ProcessCallback.Context processCallbackCxt = new ProcessCallback.Context();
        processCallbackCxt.setTopic(pullResult.topic());
        processCallbackCxt.setMessageExts(pullResult.messageExts());
        processCallbackCxt.setGroup(pullResult.group());
        processCallbackCxt.setMessageListener(this.messageListener);
        this.defaultConsumeMsgExecutor.execute(() -> {
            try {
                ConsumeContext context = new ConsumeContext();
                ConsumeStatus consumeStatus = messageListener.onMessage(messageExts, context);
                if (consumeStatus == ConsumeStatus.CONSUME_LATER) {
                    processCallback.onFail(processCallbackCxt);
                } else {
                    processCallback.onSuccess(processCallbackCxt);
                }
                if (defaultConsumeMsgPoolQueue.isEmpty() && waiting.get()) {
                    waiting.compareAndSet(false, true);
                    this.mqConsumerService.wakeup();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

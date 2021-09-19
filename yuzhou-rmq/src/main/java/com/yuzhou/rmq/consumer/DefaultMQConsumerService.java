package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.Message;
import com.yuzhou.rmq.common.ServiceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 上午1:04
 */
public class DefaultMQConsumerService extends ServiceThread {

    Logger log = LoggerFactory.getLogger(DefaultMQConsumerService.class);

    private final PullMessageService pullMessageService;

    private final MessageListener messageListener;

    private final ExecutorService consumePool = Executors.newFixedThreadPool(10);

    private final ScheduledExecutorService peddingMsgExecutorService = Executors
            .newSingleThreadScheduledExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "PullMessageServiceScheduledThread");
                }
            });

    public DefaultMQConsumerService(PullMessageService pullMessageService, MessageListener listener) {
        this.pullMessageService = pullMessageService;
        this.messageListener = listener;
    }

    @Override
    public void run() {
        log.info(this.getServiceName() + " service started");

        while (!this.isStopped()) {
            try {
                try {
                    LinkedBlockingQueue<List<Message>> messageQueue = pullMessageService.messageQueue;
                    List<Message> messageList = messageQueue.take();

                    consumePool.execute(() -> {
                        try {
                            ConsumeContext context = new ConsumeContext();
                            ConsumeStatus consumeStatus = messageListener.consumeMessage(messageList, context);
                            switch (consumeStatus) {
                                case CONSUME_SUCCESS:
//                                jedis.xack(topic, group, streamEntryIDS);
                                    break;
                                case CONSUME_LATER:
                                    //放入重试队列中
                                    break;
                                default:
                                    throw new RuntimeException("指定返回");
                            }
                        } catch (Exception e) {

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                log.error("Pull Message Service Run Method exception", e);
            }
        }

        log.info(this.getServiceName() + " service end");
    }


    @Override
    public String getServiceName() {
        return this.getClass().getName();
    }
}

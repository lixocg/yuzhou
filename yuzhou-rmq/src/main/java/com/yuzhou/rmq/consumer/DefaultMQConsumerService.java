package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.MsgReservedKey;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.MsgRetryLevel;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.common.ServiceThread;
import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.remoting.MQRemotingInstance;
import com.yuzhou.rmq.remoting.ProcessCallback;
import com.yuzhou.rmq.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
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
public class DefaultMQConsumerService extends ServiceThread {

    Logger log = LoggerFactory.getLogger(DefaultMQConsumerService.class);

    private final MessageListener messageListener;

    private final ExecutorService consumePool = Executors.newFixedThreadPool(10);

    /**
     * 间隔消息拉取定时
     */
    private final ScheduledExecutorService intervalPullMsgExecutor =
            Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("ConsumeMessageIntervalThread_"));

    /**
     * 延迟消息拉取定时
     */
    private final ScheduledExecutorService delayPullMsgExecutor =
            Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("ConsumeMessageDelayThread_"));

    private final MQConfigConsumer mqConfigConsumer;

    private final MQRemotingInstance mqRemotingInstance;

    private boolean openIntervalPull = false;

    public DefaultMQConsumerService(MQConfigConsumer mqConfigConsumer, MQRemotingInstance mqRemotingInstance) {
        this.mqConfigConsumer = mqConfigConsumer;
        this.messageListener = mqConfigConsumer.messageListener();
        this.mqRemotingInstance = mqRemotingInstance;
        this.openIntervalPull = mqConfigConsumer.pullInterval() > 0;
    }

    @Override
    public void run() {
        log.info(this.getServiceName() + " service started");

        //非间隔拉取才启动该处理器
        while (!this.isStopped() && !openIntervalPull) {
            try {
                pullMsgHandle();
            } catch (Exception e) {
                log.error("Pull Message Service Run Method exception", e);
            }
        }

        log.info(this.getServiceName() + " service end");
    }

    private String consumerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "UNKOWN-CONSUMER";
    }

    private void pullMsgHandle() {
        System.out.println("拉取消息中....");
        //拉取消息,Redis队列没有消息阻塞
        PullResult pullResult = mqRemotingInstance.blockedReadMsgs(
                mqConfigConsumer.group(),
                consumerName(),
                mqConfigConsumer.topic(),
                mqConfigConsumer.pullBatchSize());

        callback(pullResult);
    }

    public void callback(PullResult pullResult) {
        List<MessageExt> messageExts = pullResult.getMessageExts();
        ProcessCallback processCallback = pullResult.getProcessCallback();
        consumePool.execute(() -> {
            try {
                ConsumeContext context = new ConsumeContext();
                ConsumeStatus consumeStatus = messageListener.consumeMessage(messageExts, context);
                switch (consumeStatus) {
                    case CONSUME_SUCCESS:
                        processCallback.onSuccess();
                        break;
                    case CONSUME_LATER:
                        processCallback.onFail();
                        break;
                    default:
                        throw new RuntimeException("指定返回");
                }
            } catch (Exception e) {

            }
        });
    }

    @Override
    public void start() {
        super.start();

        //启动间隔消息拉取定时
        if (mqConfigConsumer.pullInterval() > 0) {
            this.intervalPullMsgExecutor.scheduleAtFixedRate(this::pullMsgHandle,
                    3000,
                    mqConfigConsumer.pullInterval(), TimeUnit.MILLISECONDS);
        }

        //启动定时消息拉取定时
        this.delayPullMsgExecutor.scheduleAtFixedRate(this::pullDelayMsgHandle, 3, 2, TimeUnit.SECONDS);
    }

    private void pullDelayMsgHandle() {
        System.out.println(String.format("%s ----定时拉取消息中", DateUtil.nowStr()));
        PullResult pullResult = mqRemotingInstance.readDelayMsgBeforeNow(mqConfigConsumer.topic());
        if (pullResult.getMessageExts() == null) {
            return;
        }
        callback(pullResult);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        this.intervalPullMsgExecutor.shutdown();
        this.delayPullMsgExecutor.shutdown();
    }

    @Override
    public String getServiceName() {
        return this.getClass().getName();
    }

}

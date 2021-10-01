package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.factory.ProcessCallback;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:12
 */
public abstract class AbstractMsgHandler implements MsgHandler {

    private final BlockingQueue<Runnable> defaultConsumeMsgPoolQueue;

    private final ExecutorService defaultConsumeMsgExecutor;

    public MessageListener messageListener;

    public AbstractMsgHandler(MessageListener messageListener) {
        this.messageListener = messageListener;
        this.defaultConsumeMsgPoolQueue = new LinkedBlockingQueue<>(10000);
        this.defaultConsumeMsgExecutor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                1000 * 60,
                TimeUnit.MILLISECONDS,
                this.defaultConsumeMsgPoolQueue,
                new ThreadFactoryImpl("default-consumeMsg-executor")
                );
    }


    public String consumerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "UNKOWN-CONSUMER";
    }

    @Override
    public void handle(PullResult pullResult) {
        List<MessageExt> messageExts = pullResult.messageExts();

        if(messageExts == null || messageExts.size() == 0){
            return;
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
                switch (consumeStatus) {
                    case CONSUME_SUCCESS:
                        processCallback.onSuccess(processCallbackCxt);
                        break;
                    case CONSUME_LATER:
                        processCallback.onFail(processCallbackCxt);
                        break;
                    default:
                        throw new RuntimeException("指定返回");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

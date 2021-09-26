package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.CountDownLatch2;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.remoting.MQRemotingInstance;
import com.yuzhou.rmq.remoting.ProcessCallback;
import com.yuzhou.rmq.utils.DateUtil;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:03
 */
public class DelayMsgHandler extends AbstractMsgHandler{

    private MQConfigConsumer configConsumer;

    private MQRemotingInstance mqRemotingInstance;

    public DelayMsgHandler(MQConfigConsumer configConsumer,
                           MQRemotingInstance mqRemotingInstance,
                           MessageListener messageListener){
        super(messageListener);
        this.configConsumer = configConsumer;
        this.mqRemotingInstance = mqRemotingInstance;
    }

    CountDownLatch2 latch = new CountDownLatch2(1);

    @Override
    public void run() {
        System.out.println(String.format("%s ----定时拉取消息中", DateUtil.nowStr()));
        PullResult pullResult = mqRemotingInstance.readDelayMsgBeforeNow(configConsumer.topic());
        if (pullResult.getMessageExts() == null) {
            return;
        }
        handle(pullResult);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(PullResult pullResult) {
        List<MessageExt> messageExts = pullResult.getMessageExts();
        ProcessCallback processCallback = pullResult.getProcessCallback();

        ProcessCallback.Context processCallbackCxt = new ProcessCallback.Context();
        processCallbackCxt.latch = latch;
        consumePool.execute(() -> {
            try {
                ConsumeContext context = new ConsumeContext();
                ConsumeStatus consumeStatus = messageListener.consumeMessage(messageExts, context);
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
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

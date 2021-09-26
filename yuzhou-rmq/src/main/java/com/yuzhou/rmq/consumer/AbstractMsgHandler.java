package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.common.ServiceThread;
import com.yuzhou.rmq.remoting.ProcessCallback;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:12
 */
public abstract class AbstractMsgHandler implements MsgHandler {

    protected final ExecutorService consumePool = Executors.newFixedThreadPool(10);

    public MessageListener messageListener;

    public AbstractMsgHandler(MessageListener messageListener) {
        this.messageListener = messageListener;
    }


    @Override
    public void handle(PullResult pullResult) {
        List<MessageExt> messageExts = pullResult.getMessageExts();
        ProcessCallback processCallback = pullResult.getProcessCallback();
        ProcessCallback.Context processCallbackCxt = new ProcessCallback.Context();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

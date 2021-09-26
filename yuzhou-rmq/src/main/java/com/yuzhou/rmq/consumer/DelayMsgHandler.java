package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.remoting.PullService;
import com.yuzhou.rmq.remoting.ProcessCallback;
import com.yuzhou.rmq.utils.DateUtil;
import com.yuzhou.rmq.utils.MixUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:03
 */
public class DelayMsgHandler extends AbstractMsgHandler {

    private MQConfigConsumer configConsumer;

    private PullService pullService;

    private final String topic;

    private final String group;

    public DelayMsgHandler(MQConfigConsumer configConsumer,
                           PullService pullService,
                           MessageListener messageListener) {
        super(messageListener);
        this.configConsumer = configConsumer;
        this.pullService = pullService;
        this.topic = configConsumer.topic();
        this.group = configConsumer.group();
    }


    @Override
    public void run() {
        System.out.println(String.format("%s ----定时拉取消息中,topic=%s", DateUtil.nowStr(), MixUtil.delayScoreTopic(topic)));
        PullResult pullResult = pullService.readDelayMsgBeforeNow(group, configConsumer.topic());
        if (pullResult.messageExts() == null) {
            return;
        }
        handle(pullResult);
    }

    @Override
    public void handle(PullResult pullResult) {
        List<MessageExt> messageExts = pullResult.messageExts();
        ProcessCallback processCallback = pullResult.processCallback();

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

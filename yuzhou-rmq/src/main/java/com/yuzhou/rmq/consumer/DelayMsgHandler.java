package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.factory.MQClientInstance;
import com.yuzhou.rmq.utils.DateUtil;
import com.yuzhou.rmq.utils.MixUtil;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:03
 */
public class DelayMsgHandler extends AbstractMsgHandler {

    private MQConfigConsumer configConsumer;

    private MQClientInstance mqClientInstance;

    private final String topic;

    private final String group;

    public DelayMsgHandler(MQConfigConsumer configConsumer,
                           MQClientInstance mqClientInstance,
                           MessageListener messageListener) {
        super(messageListener);
        this.configConsumer = configConsumer;
        this.mqClientInstance = mqClientInstance;
        this.topic = configConsumer.topic();
        this.group = configConsumer.group();
    }


    @Override
    public void run() {
        System.out.println(String.format("%s ----定时拉取消息中,topic=%s", DateUtil.nowStr(), MixUtil.delayScoreTopic(topic)));
        PullResult pullResult = mqClientInstance.readDelayMsgBeforeNow(group, configConsumer.topic());
        if (pullResult.messageExts() == null) {
            return;
        }
        handle(pullResult);
    }
}

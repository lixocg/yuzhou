package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.remoting.MQRemotingInstance;
import com.yuzhou.rmq.utils.DateUtil;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:03
 */
public class IntervalMsgHandler extends AbstractMsgHandler{

    private MQConfigConsumer configConsumer;

    private MQRemotingInstance mqRemotingInstance;

    public IntervalMsgHandler(MQConfigConsumer configConsumer,
                              MQRemotingInstance mqRemotingInstance,
                              MessageListener messageListener){
        super(messageListener);
        this.configConsumer = configConsumer;
        this.mqRemotingInstance = mqRemotingInstance;
    }

    public IntervalMsgHandler(MessageListener messageListener) {
        super(messageListener);
    }



    @Override
    public String getServiceName() {
        return this.getClass().getName();
    }

    @Override
    public void run() {
        System.out.println(String.format("%s ----间隔拉取消息中", DateUtil.nowStr()));
        PullResult pullResult = mqRemotingInstance.readDelayMsgBeforeNow(configConsumer.topic());
        if (pullResult.getMessageExts() == null) {
            return;
        }
        handle(pullResult);
    }
}

package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.remoting.PullService;
import com.yuzhou.rmq.utils.DateUtil;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:03
 */
public class IntervalMsgHandler extends AbstractMsgHandler {

    private MQConfigConsumer configConsumer;

    private PullService pullService;

    private final String topic;

    private final String group;

    private final int pullBatchSize;

    public IntervalMsgHandler(MQConfigConsumer configConsumer,
                              PullService pullService,
                              MessageListener messageListener) {
        super(messageListener);
        this.configConsumer = configConsumer;
        this.pullService = pullService;
        this.topic = configConsumer.topic();
        this.group = configConsumer.group();
        this.pullBatchSize = configConsumer.pullBatchSize();
    }


    @Override
    public void run() {
        System.out.println(String.format("%s ----间隔拉取消息中", DateUtil.nowStr()));
        PullResult pullResult = pullService.blockedReadMsgs(group, topic, consumerName(), pullBatchSize);
        if (pullResult.messageExts() == null) {
            return;
        }
        handle(pullResult);
    }
}

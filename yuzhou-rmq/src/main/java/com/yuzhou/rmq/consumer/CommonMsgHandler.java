package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.factory.MQClientInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:03
 */
public class CommonMsgHandler extends AbstractMsgHandler {

    Logger log = LoggerFactory.getLogger(DefaultMQConsumerService.class);

    private MQConfigConsumer mqConfigConsumer;

    private MQClientInstance mqClientInstance;

    public CommonMsgHandler(MQConfigConsumer configConsumer,
                            MQClientInstance mqClientInstance,
                            MessageListener messageListener) {
        super(messageListener);
        this.mqConfigConsumer = configConsumer;
        this.mqClientInstance = mqClientInstance;
    }

    public CommonMsgHandler(MessageListener messageListener) {
        super(messageListener);
    }

    @Override
    public void run() {
        try {
            System.out.println("拉取普通消息中....");
            //拉取消息,Redis队列没有消息时阻塞
            PullResult pullResult = mqClientInstance.blockedReadMsgs(
                    mqConfigConsumer.group(),
                    consumerName(),
                    mqConfigConsumer.topic(),
                    mqConfigConsumer.pullBatchSize());

            handle(pullResult);
        } catch (Exception e) {
            log.error("Pull Message Service Run Method exception", e);
        }
    }



}

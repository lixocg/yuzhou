package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.factory.MQClientInstance;
import com.yuzhou.rmq.utils.DateUtil;
import com.yuzhou.rmq.utils.MixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-24
 * Time: 上午12:03
 */
public class DelayMsgHandler extends AbstractMsgHandler {

    Logger logger = LoggerFactory.getLogger(DelayMsgHandler.class);

    public DelayMsgHandler(DefaultMQConsumerService mqConsumerService,MessageListener messageListener) {
        super(mqConsumerService,messageListener);
    }


    @Override
    public void handle(PullResult pullResult) {
        try {
            super.handle(pullResult);
        } catch (Exception e) {
            logger.error("Pull Message Service Run Method exception", e);
        }
    }
}

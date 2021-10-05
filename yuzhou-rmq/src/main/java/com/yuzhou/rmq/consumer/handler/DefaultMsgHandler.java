package com.yuzhou.rmq.consumer.handler;

import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.consumer.MQConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-04
 * Time: 上午10:20
 */
public class DefaultMsgHandler extends AbstractMsgHandler {

    Logger logger = LoggerFactory.getLogger(DefaultMsgHandler.class);


    public DefaultMsgHandler(MQConsumerService mqConsumerService,
                             MessageListener messageListener) {
        super(mqConsumerService, messageListener);
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

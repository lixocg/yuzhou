package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.common.PullResult;
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


    public CommonMsgHandler(DefaultMQConsumerService mqConsumerService,
            MessageListener messageListener) {
        super(mqConsumerService,messageListener);
    }


    @Override
    public void handle(PullResult pullResult) {
        try {
            super.handle(pullResult);
        } catch (Exception e) {
            log.error("Pull Message Service Run Method exception", e);
        }
    }


}

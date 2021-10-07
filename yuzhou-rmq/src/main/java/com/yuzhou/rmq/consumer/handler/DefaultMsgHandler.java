package com.yuzhou.rmq.consumer.handler;

import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.consumer.MQConsumerService;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-04
 * Time: 上午10:20
 */
public class DefaultMsgHandler extends AbstractMsgHandler {

    public DefaultMsgHandler(MQConsumerService mqConsumerService,
                             MessageListener messageListener) {
        super(mqConsumerService, messageListener);
    }

}

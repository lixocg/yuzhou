package com.yuzhou.rmq.client;

import com.yuzhou.rmq.common.Message;
import com.yuzhou.rmq.common.SendResult;

import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-17
 * Time: 下午8:03
 */
public interface MQProducer extends MQAdmin{


    SendResult send(String topic, Map<String,String> msg);

    SendResult send(String topic, Map<String, String> msg, long delay);

    SendResult send(String topic, String tag, Map<String, String> msg);

    SendResult send(String topic, String tag, Map<String, String> msg, long delay);

    SendResult send(Message message);
}

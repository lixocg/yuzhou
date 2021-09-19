package com.yuzhou.rmq.client.impl;

import com.yuzhou.rmq.client.MQProducer;
import com.yuzhou.rmq.common.SendResult;
import com.yuzhou.rmq.remoting.JedisRemotingInstance;
import com.yuzhou.rmq.remoting.MQRemotingInstance;
import com.yuzhou.rmq.remoting.PutResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-18
 * Time: 下午9:46
 */
public class DefaultMQProducer implements MQProducer {

    private MQRemotingInstance<?> remotingInstance;

    @Override
    public void start() {
        remotingInstance = new JedisRemotingInstance();
        remotingInstance.start();
    }

    @Override
    public void shutdown() {
    }

    @Override
    public SendResult send(String topic, Map<String, String> msg) {
        PutResult putResult = remotingInstance.putMsg(topic, msg);
        if(putResult != null && putResult.isSuccess()){
            return SendResult.ok(putResult.getMsgId());
        }
        return SendResult.notOk();
    }


    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.start();

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 200; i++) {
            map.put("name", "zs" + i);
            map.put("age", i + "");
            SendResult result = producer.send("mytopic", map);
            System.out.println(result);
        }
    }
}

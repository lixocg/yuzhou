package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.exception.RmqException;
import com.yuzhou.rmq.factory.MQClientInstance;
import com.yuzhou.rmq.utils.MixUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 上午1:04
 */
public class DelayMsgConsumerService extends AbstractMQConsumerService {

    public DelayMsgConsumerService(MQConfigConsumer mqConfigConsumer, MQClientInstance mqClientInstance) {
        super(mqConfigConsumer, mqClientInstance);
    }


    @Override
    public void run() {
        try {
            Set<Map<String, String>> delayMsg = mqClientInstance.readDelayMsg(MixUtil.delayScoreTopic(topic),
                    0, System.currentTimeMillis());
            if(delayMsg == null){
                return;
            }
            delayMsg.forEach(content -> mqClientInstance.putMsg(topic, content));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected PullResult pullResult() {
        throw new RmqException("no result");
    }

    @Override
    public void start() {
        throw new RmqException("不需要启动");
    }

    @Override
    public String getServiceName() {
        return this.getClass().getSimpleName();
    }

}

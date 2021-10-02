package com.yuzhou.rmq.client;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-17
 * Time: 下午8:07
 */
public interface MQConsumer extends MQAdmin{

    void registerMessageListener(MessageListener messageListener);


}

package com.yuzhou.rmq.client;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-17
 * Time: 下午8:07
 */
public interface MQConsumer {
    void start();

    void shutdown();

    void registerMessageListener(MessageListener messageListener);
}

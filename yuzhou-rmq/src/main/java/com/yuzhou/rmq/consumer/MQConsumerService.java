package com.yuzhou.rmq.consumer;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午1:56
 */
public interface MQConsumerService {

    int poolSize();

    int maxPoolSize();

}

package com.yuzhou.rmq.consumer;

/**
 * 消费服务
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午1:56
 */
public interface MQConsumerService {

    /**
     * 消费线程池核心线程数
     * @return
     */
    int poolSize();

    /**
     * 先飞线程池最大线程数
     * @return
     */
    int maxPoolSize();

    /**
     * 使消费服务停止
     */
    void makeStop();

    /**
     * 唤醒消费服务
     */
    void wakeup();

    /**
     * 消费服务是否运行中
     * @return
     */
    boolean isRunning();

    /**
     * 服务名
     * @return
     */
    String getServiceName();
}

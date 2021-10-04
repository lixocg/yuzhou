package com.yuzhou.rmq.client;

/**
 * 消费者配置
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午2:05
 */
public interface MQConfigConsumer extends MQConsumer {

    void setCosumePoolCoreSize(int cosumePoolCoreSize);

    void setCosumePoolMaxCoreSize(int cosumePoolMaxCoreSize);

    String topic();

    String group();

    String name();

    void setPullBatchSize(int size);

    int pullBatchSize();

    long pullInterval();

    void setPullInterval(long pullInterval);

    MessageListener messageListener();

    int consumePoolCoreSize();

    int consumePoolMaxCoreSize();
}

package com.yuzhou.rmq.client.impl;

import com.yuzhou.rmq.client.MQConsumer;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午2:05
 */
public interface MQConfigConsumer extends MQConsumer {
     String topic();

    String group();

    void setPullBatchSize(int size);

    void setOrderly(boolean isOrderly);

    boolean orderly();

    int pullBatchSize();
}

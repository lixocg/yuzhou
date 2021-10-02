package com.yuzhou.rmq.client;

import com.yuzhou.rmq.connection.Connection;

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

    String name();

    void setPullBatchSize(int size);

    int pullBatchSize();

    long pullInterval();

    void setPullInterval(long pullInterval);

    MessageListener messageListener();
}

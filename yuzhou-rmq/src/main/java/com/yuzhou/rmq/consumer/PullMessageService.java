package com.yuzhou.rmq.consumer;

import com.yuzhou.rmq.client.impl.MQConfigConsumer;
import com.yuzhou.rmq.common.Message;
import com.yuzhou.rmq.common.ServiceThread;
import com.yuzhou.rmq.remoting.MQRemotingInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 上午12:23
 */
public class PullMessageService extends ServiceThread {

    Logger log = LoggerFactory.getLogger(PullMessageService.class);

    private final MQConfigConsumer mqConsumer;

    private final MQRemotingInstance<?> mqRemotingInstance;

    public PullMessageService(MQConfigConsumer mqConsumer, MQRemotingInstance<?> instance) {
        this.mqRemotingInstance = instance;
        this.mqConsumer = mqConsumer;
    }

    public final LinkedBlockingQueue<List<Message>> messageQueue = new LinkedBlockingQueue<>();


    @Override
    public void run() {
        log.info(this.getServiceName() + " service started");

        //创建消费组
        mqRemotingInstance.createGroup(mqConsumer.topic(), mqConsumer.group());

        while (!this.isStopped()) {
            try {
                try {
                    //拉取消息
                    List<Message> messages = mqRemotingInstance.readMsg(mqConsumer.group(),
                            mqConsumer.topic(),
                            mqConsumer.pullBatchSize());

                    //放到待消费队列
                    messageQueue.put(messages);

//                        StreamEntryID[] streamEntryIDS = streamEntries.stream().map(StreamEntry::getID).toArray(StreamEntryID[]::new);

                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            } catch (Exception e) {
                log.error("Pull Message Service Run Method exception", e);
            }
        }

        log.info(this.getServiceName() + " service end");
    }

    @Override
    public String getServiceName() {
        return PullMessageService.class.getSimpleName();
    }
}

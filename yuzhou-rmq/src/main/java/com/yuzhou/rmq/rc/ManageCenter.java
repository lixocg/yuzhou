package com.yuzhou.rmq.rc;

import com.alibaba.fastjson.JSON;
import com.yuzhou.rmq.client.MQConfigConsumer;
import com.yuzhou.rmq.concurrent.ThreadUtils;
import com.yuzhou.rmq.factory.MQClientInstance;
import com.yuzhou.rmq.log.InnerLog;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-02
 * Time: 上午10:34
 */
public class ManageCenter {

    Logger logger = InnerLog.getLogger(ManageCenter.class);

    private final MQClientInstance mqClientInstance;

    private MQConfigConsumer mqConfigConsumer;

    /**
     * 消费者心跳
     */
    private final ScheduledExecutorService consumerStatExecutor =
            ThreadUtils.newSingleThreadScheduledExecutor("ConsumeHeartBeatThread");

    public ManageCenter(MQClientInstance mqClientInstance) {
        this.mqClientInstance = mqClientInstance;
    }

    public void start() {
        consumerStatExecutor.scheduleAtFixedRate(this::stat, 3, 10, TimeUnit.SECONDS);
    }

    private void stat() {
        System.out.println("===============stat-=======");

        TopicInfo topicInfo = mqClientInstance.topicInfo(mqConfigConsumer.topic());
        System.out.println(JSON.toJSONString(topicInfo));
    }

    public ManageCenter(MQConfigConsumer configConsumer, MQClientInstance mqClientInstance) {
        this(mqClientInstance);
        this.mqConfigConsumer = configConsumer;
    }

    public ConsumerGroup loadCurGroup(String groupName) {
        return mqClientInstance.loadConsumeGroup(groupName);
    }

    public synchronized void registerConsumer(MQConfigConsumer mqConfigConsumer) {
        ConsumerGroup consumerGroup = loadCurGroup(mqConfigConsumer.group());
        if(consumerGroup == null){
            consumerGroup = new ConsumerGroup();
            consumerGroup.setTopic(mqConfigConsumer.topic());
            consumerGroup.setGroupName(mqConfigConsumer.group());
        }
        Set<ConsumerInstance> consumers = consumerGroup.getConsumers();
        if (consumers == null) {
            consumers = new HashSet<>();
            consumerGroup.setConsumers(consumers);
        }
        ConsumerInstance consumer = new ConsumerInstance();
        consumer.setActive(Boolean.TRUE);
        consumer.setLastUpdated(System.currentTimeMillis());
        consumer.setName(mqConfigConsumer.name());
        consumers.add(consumer);
        mqClientInstance.registerConsumer(consumerGroup);
    }


}

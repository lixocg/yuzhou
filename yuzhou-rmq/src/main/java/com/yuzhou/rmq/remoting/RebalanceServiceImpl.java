package com.yuzhou.rmq.remoting;

import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.common.TopicGroup;
import com.yuzhou.rmq.exception.BalanceException;
import com.yuzhou.rmq.utils.MixUtil;
import com.yuzhou.rmq.utils.SerializeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.yuzhou.rmq.utils.SerializeUtils.serialize;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-01
 * Time: 上午10:54
 */
public class RebalanceServiceImpl {

    private final Remoting remoting;

    private static TopicGroup topicGroup;

    private String topicGroupName;

    ReentrantLock balanceLock = new ReentrantLock();

    private final ScheduledExecutorService rebalanceExecutor =
            Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("rebalanceThread_"));


    public RebalanceServiceImpl(Remoting remoting) {
        this.remoting = remoting;
    }

    public void start() {
        //加载rebalance信息
        load();
        //启动rebalance定时
        rebalanceExecutor.scheduleWithFixedDelay(this::rebalance, 1, 10, TimeUnit.SECONDS);
    }

    private void load() {

    }

    private void rebalance() {
        try {
            balanceLock.lock();
            //获取当前主题组信息
            TopicGroup topicGroup = remoting.hmget(topicGroupName);
            if (topicGroup == null || topicGroup.isChanged()) {
                return;
            }
            //rebalance
            //消费者数量
            List<String> consumers = topicGroup.getConsumers();
            //keys数量
            List<String> streams = topicGroup.getStreams();
            if (consumers == null || streams == null || consumers.size() == 0 || streams.size() == 0) {
                return;
            }
            List<List<String>> averageAssignList = MixUtil.averageAssign(streams, consumers.size());
            if (averageAssignList.size() > consumers.size()) {
                throw new BalanceException("rebalance error");
            }
            //把分配的stream分配给consumer
            Map<String,List<String>> consumerStreamsMap = new HashMap<>(averageAssignList.size());
            for (int i = 0; i < averageAssignList.size(); i++) {
                String consumer = consumers.get(i);
                List<String> assaignStreams = averageAssignList.get(i);
                consumerStreamsMap.put(consumer,assaignStreams);
            }
            topicGroup.setConsumerStreams(consumerStreamsMap);
            topicGroup.setChanged(false);
            remoting.hmset(serialize(topicGroupName,String.class),
                    serialize(topicGroup,TopicGroup.class));
        } finally {
            balanceLock.unlock();
        }
    }


}

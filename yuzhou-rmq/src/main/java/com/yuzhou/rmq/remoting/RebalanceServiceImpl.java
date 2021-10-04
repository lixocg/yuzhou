//package com.yuzhou.rmq.remoting;
//
//import com.yuzhou.rmq.concurrent.ThreadFactoryImpl;
//import com.yuzhou.rmq.common.TopicGroup;
//import com.yuzhou.rmq.exception.BalanceException;
//import com.yuzhou.rmq.utils.MixUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * Created with IntelliJ IDEA
// * Description:
// * User: lixiongcheng
// * Date: 2021-10-01
// * Time: 上午10:54
// */
//public class RebalanceServiceImpl {
//
//    private final static Logger logger = LoggerFactory.getLogger(RebalanceServiceImpl.class);
//
//    private final Remoting remoting;
//
//    private TopicGroup topicGroup;
//
//    private final String topicGroupName;
//
//    ReentrantLock balanceLock = new ReentrantLock();
//
//    private final ScheduledExecutorService rebalanceExecutor =
//            Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("rebalanceThread_"));
//
//
//    public RebalanceServiceImpl(String topicGroupName,Remoting remoting) {
//        this.topicGroupName = topicGroupName;
//        this.remoting = remoting;
//    }
//
//    public void start() {
//        //加载rebalance信息
//        load();
//        //启动rebalance定时
//        rebalanceExecutor.scheduleWithFixedDelay(this::rebalance, 1, 10, TimeUnit.SECONDS);
//    }
//
//    private void load() {
////        this.topicGroup = remoting.hmget(topicGroupName);
//    }
//
//    private void rebalance() {
//        logger.info("rebalance start.....");
//        try {
//            balanceLock.lock();
//            //获取当前主题组信息
//            TopicGroup topicGroup = remoting.hmget(topicGroupName);
//            if (topicGroup == null || topicGroup.isChanged()) {
//                logger.info("rebalance,but no change .....");
//                return;
//            }
//            //rebalance
//            //消费者数量
//            List<String> consumers = topicGroup.getConsumers();
//            //keys数量
//            List<String> streams = topicGroup.getStreams();
//            if (consumers == null || streams == null || consumers.size() == 0 || streams.size() == 0) {
//                return;
//            }
//            List<List<String>> averageAssignList = MixUtil.averageAssign(streams, consumers.size());
//            if (averageAssignList.size() > consumers.size()) {
//                throw new BalanceException("rebalance error");
//            }
//            //把分配的stream分配给consumer
//            Map<String, List<String>> consumerStreamsMap = new HashMap<>(averageAssignList.size());
//            for (int i = 0; i < averageAssignList.size(); i++) {
//                String consumer = consumers.get(i);
//                List<String> assaignStreams = averageAssignList.get(i);
//                consumerStreamsMap.put(consumer, assaignStreams);
//            }
//            topicGroup.setConsumerStreams(consumerStreamsMap);
//            topicGroup.setChanged(false);
//            remoting.hmset(topicGroupName, MixUtil.toMap(topicGroup));
//            logger.info("rebalance end.....");
//        } finally {
//            balanceLock.unlock();
//        }
//    }
//
//
//}

package com.yuzhou.rmq.remoting.redis;

import com.yuzhou.rmq.common.MessageExt;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-23
 * Time: 下午10:19
 */
public interface Remoting {
    void start();
    void shutdown();


    /**
     * 创建stream消费组
     * @param stream
     * @param groupName
     * @return
     */
    boolean xgroupCreate(String stream, String groupName);

    /**
     * 读取stream指定消息
     * @param stream
     * @param msgId
     * @return
     */
    MessageExt xRead(String stream, String msgId);

    /**
     * stream消费者阻塞试读取数据
     * @param groupName
     * @param consumer
     * @param stream
     * @param count
     * @return
     */
    List<MessageExt> xreadGroup(String groupName, String consumer, String stream, int count);

    /**
     * stream中新增数据
     * @param stream
     * @param msg
     * @return
     */
    String xadd(String stream, Map<String, String> msg);

    long xack(String stream, String group, List<String> msgIds);

    long xdel(String stream, List<String> msgIds);

    long zadd(String key, String msgId, double score);

    Set<String> zrangeByScore(String topic, long start, long end);

    long zremrangeByScore(String key, long start, long end);

    long zrem(String key, List<String> msgIds);
}

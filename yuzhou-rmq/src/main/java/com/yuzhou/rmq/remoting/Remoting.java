package com.yuzhou.rmq.remoting;

import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.TopicGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis 操作
 * User: lixiongcheng
 * Date: 2021-09-23
 * Time: 下午10:19
 */
public interface Remoting {
    void start();
    void shutdown();


    /**
     * 创建stream消费组
     * @param stream stream key
     * @param groupName stream 组
     * @return
     */
    boolean xgroupCreate(String stream, String groupName);

    /**
     * 非阻塞式读取stream指定消息
     * @param stream key
     * @param msgId 消息id
     * @return
     */
    MessageExt xRead(String stream, String msgId);

    /**
     * stream消费者阻塞试读取数据
     * @param groupName 组名
     * @param consumer 消费者
     * @param stream stream key
     * @param count 读取条数
     * @return
     */
    List<MessageExt> xreadGroup(String groupName, String consumer, String stream, int count);

    /**
     * stream中新增数据
     * @param stream stream key
     * @param msg 消息
     * @return
     */
    String xadd(String stream, Map<String, String> msg);

    /**
     * ack消息
     * @param stream stream key
     * @param group 组
     * @param msgIds 消息列表
     * @return
     */
    long xack(String stream, String group, List<String> msgIds);

    long xdel(String stream, List<String> msgIds);

    long zadd(String key, String msgId, double score);

    Set<String> zrangeByScore(String topic, long start, long end);

    /**
     * zset获取并删除
     * @param key zset key
     * @param start 开始元素
     * @param end 结束元素
     * @return
     */
    Set<String> zrangeAndRemByScore(String key, long start, long end);

    long zremrangeByScore(String key, long start, long end);

    long zrem(String key, List<String> msgIds);

    TopicGroup hmget(String key);

    boolean hmset(String key, Map<String, String> data);

    boolean hmset(byte[] key, Map<byte[], byte[]> data);
}

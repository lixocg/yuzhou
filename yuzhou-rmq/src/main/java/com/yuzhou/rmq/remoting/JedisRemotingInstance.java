package com.yuzhou.rmq.remoting;

import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.utils.MixUtil;
import com.yuzhou.rmq.utils.TypeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Response;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.StreamGroupInfo;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.XReadGroupParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午2:46
 */
public class JedisRemotingInstance implements MQRemotingInstance<Jedis> {

    public static final String GROUP_CREATE_SUCCESS = "ok";


    public boolean existGroup(String stream, String group) {
        Jedis jedis = jedisPool.getResource();
        try {
            List<StreamGroupInfo> streamGroupInfos = jedis.xinfoGroup(stream);
            if (streamGroupInfos == null || streamGroupInfos.size() == 0) {
                return false;
            }
            return streamGroupInfos.stream().anyMatch(
                    streamGroupInfo -> streamGroupInfo.getName().equals(group));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
        return false;
    }


    @Override
    public boolean createGroup(String stream, String groupName) {
        Jedis jedis = jedisPool.getResource();
        try {
            if (existGroup(stream, groupName)) {
                System.out.println("消费组已存在");
                return true;
            }
            //创建消费组从队列未读取
            String ok = jedis.xgroupCreate(stream, groupName, StreamEntryID.LAST_ENTRY, true);
            if (GROUP_CREATE_SUCCESS.equals(ok)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
        return false;
    }

    public MessageExt readMsg(String stream, String msgId) {
        Jedis jedis = jedisPool.getResource();
        try {
            StreamEntryID start = new StreamEntryID(msgId);
            List<StreamEntry> entries = jedis.xrange(stream, start, start);
            if (entries == null || entries.size() == 0) {
                return null;
            }
            //只有一个key取列表第一个
            StreamEntry streamEntry = entries.get(0);
            MessageExt messageExt = new MessageExt();
            messageExt.setContent(streamEntry.getFields());
            messageExt.setMsgId(streamEntry.getID().toString());
            return messageExt;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 阻塞试读取未被其他同组其他消费者消费的数据
     *
     * @param groupName
     * @param consumer
     * @param stream
     * @param count
     * @return
     */
    @Override
    public List<MessageExt> blockedReadMsgs(String groupName, String consumer, String stream, int count) {
        Jedis jedis = jedisPool.getResource();
        try {
            Map<String, StreamEntryID> streamMap = new HashMap<>();
            streamMap.put(stream, StreamEntryID.UNRECEIVED_ENTRY);
            Map.Entry<String, StreamEntryID> streamEntryIDEntry = streamMap.entrySet().iterator().next();

            //只读取当前topic的stream key,没数据阻塞
            List<Map.Entry<String/*key*/, List<StreamEntry>>> entries =
                    jedis.xreadGroup(groupName,
                            consumer,
                            count, Long.MAX_VALUE, true, streamEntryIDEntry);

            Map.Entry<String, List<StreamEntry>> streamEntryList = entries.get(0);
            List<MessageExt> messageExtList = streamEntryList.getValue().stream().map(streamEntry -> {
                MessageExt messageExt = new MessageExt();
                messageExt.setMsgId(streamEntry.getID().toString());
                messageExt.setContent(streamEntry.getFields());
                return messageExt;
            }).collect(Collectors.toList());

            return messageExtList;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public PutResult putMsg(String topic, Map<String, String> msg) {
        Jedis jedis = jedisPool.getResource();
        try {
            StreamEntryID streamEntryID = jedis.xadd(topic, StreamEntryID.NEW_ENTRY, msg);
            return PutResult.id(streamEntryID.getTime(), streamEntryID.getSequence());
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * O(log(N)
     *
     * @param key
     * @param msgId
     * @param timestamp
     */
    public void putMsgWithScore(String key, String msgId, long timestamp) {
        Jedis jedis = jedisPool.getResource();
        try {
            Long zadd = jedis.zadd(key, TypeUtil.l2d(timestamp), msgId);
            System.out.println(zadd);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 消息投入到延迟队列中
     *
     * @param topic
     * @param msg
     * @param score 延迟时间，单位毫秒
     * @return
     */
    @Override
    public PutResult putDelayMsg(String topic, Map<String, String> msg, long score) {
        //1.往延迟topic stream队列中存入元数据,%DELAY%_topic
        PutResult putResult = putMsg(MixUtil.delayTopic(topic), msg);

        if (!putResult.isSuccess()) {
            return PutResult.err("放入延迟队列失败");
        }
        //2.往zset队列中存入,%DELAYSCORE%_topic
        putMsgWithScore(MixUtil.delayScoreTopic(topic), putResult.getMsgId(), score);
        return putResult;
    }

    /**
     * 读取延迟队列中小于当前时间消息列表
     * 获取到数据需要删除数据
     *
     * @return
     */
    @Override
    public List<MessageExt> readDelayMsgBeforeNow(String topic) {
        return readDelayMsg(topic, 0, System.currentTimeMillis());
    }

    @Override
    public void delDelayMsgBeforeNow(String topic) {
        remDelayMsg(topic, 0, System.currentTimeMillis());
    }

    public void remDelayMsg(String topic, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.zremrangeByScore(MixUtil.delayScoreTopic(topic), start, end);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 读取延迟队列指定范围数据
     * 获取到数据需要删除数据
     *
     * @param topic
     * @return
     */
    public List<MessageExt> readDelayMsg(String topic, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        try {
            Set<String> msgIds = jedis.zrangeByScore(MixUtil.delayScoreTopic(topic), start, end);
            if (msgIds == null || msgIds.size() == 0) {
                return null;
            }

            //并行到重试队列中获取并返回
            return msgIds.parallelStream()
                    .map(msgId -> readMsg(MixUtil.delayTopic(topic), msgId))
                    .collect(Collectors.toList());
        } finally {
            jedisPool.returnResource(jedis);
        }
    }


    public JedisPool jedisPool;

    @Override
    public void start() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxIdle(2000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 3000);
    }

    @Override
    public void shutdown() {
        jedisPool.close();
    }

}

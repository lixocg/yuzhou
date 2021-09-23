package com.yuzhou.rmq.remoting.redis;

import com.yuzhou.rmq.common.MessageExt;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.StreamGroupInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-23
 * Time: 下午10:17
 */
public class SingleRedisClient implements Remoting {

    public static final String GROUP_CREATE_SUCCESS = "ok";

    public JedisPool jedisPool;

    private String host;

    private int port;

    public SingleRedisClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void start() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxIdle(2000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(jedisPoolConfig, host, port, 3000);
    }

    @Override
    public void shutdown() {
        jedisPool.close();
    }

    @Override
    public boolean xgroupCreate(String stream, String groupName) {
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

    public MessageExt xrange(String stream, String startId, String endIs) {
        Jedis jedis = jedisPool.getResource();
        try {
            StreamEntryID start = new StreamEntryID(startId);
            StreamEntryID end = new StreamEntryID(endIs);
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

    @Override
    public MessageExt xRead(String stream, String msgId) {
        return xrange(stream, msgId, msgId);
    }

    @Override
    public List<MessageExt> xreadGroup(String groupName, String consumer, String stream, int count) {
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
            return streamEntryList.getValue().stream().map(streamEntry -> {
                MessageExt messageExt = new MessageExt();
                messageExt.setMsgId(streamEntry.getID().toString());
                messageExt.setContent(streamEntry.getFields());
                return messageExt;
            }).collect(Collectors.toList());

        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public String xadd(String stream, Map<String, String> msg) {
        Jedis jedis = jedisPool.getResource();
        try {
            StreamEntryID streamEntryID = jedis.xadd(stream, StreamEntryID.NEW_ENTRY, msg);
            return streamEntryID.toString();
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public long xack(String key,String group,List<String> msgIds){
        Jedis jedis = jedisPool.getResource();
        try{
            StreamEntryID[] streamEntryIDS = msgIds.stream().map(StreamEntryID::new).toArray(StreamEntryID[]::new);
            return jedis.xack(key,group,streamEntryIDS);
        }finally {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * O(log(N)
     *
     * @param key
     * @param msgId
     * @param score
     */
    @Override
    public void zadd(String key, String msgId, double score) {
        Jedis jedis = jedisPool.getResource();
        try {
            Long zadd = jedis.zadd(key, score, msgId);
            System.out.println(zadd);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        try {
            Set<String> msgIds = jedis.zrangeByScore(key, start, end);
            if (msgIds == null || msgIds.size() == 0) {
                return null;
            }
            return msgIds;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public long zremrangeByScore(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        try {
            Long res = jedis.zremrangeByScore(key, start, end);
            return res == null ? 0 : res;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

}

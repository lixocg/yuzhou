package com.yuzhou.rmq.remoting.redis;

import com.alibaba.fastjson.JSON;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.StreamIDEntry;
import com.yuzhou.rmq.common.TopicGroup;
import com.yuzhou.rmq.factory.stat.ConsumerInfo;
import com.yuzhou.rmq.remoting.Remoting;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Response;
import redis.clients.jedis.StreamConsumersInfo;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.StreamGroupInfo;
import redis.clients.jedis.Transaction;

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

    public static final String SUCCESS = "ok";

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
            //创建消费组从队列未读取，$标识从最大消息id消费，即消费新加入的消息,$标识最大ID
            String ok = jedis.xgroupCreate(stream, groupName, StreamEntryID.LAST_ENTRY, true);
            if (SUCCESS.equals(ok)) {
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
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public MessageExt xrange(String stream, String startId, String endIs) {
        Jedis jedis = jedisPool.getResource();
        try {
            StreamEntryID start = new StreamEntryID(startId);
            StreamEntryID end = new StreamEntryID(endIs);
            List<StreamEntry> entries = jedis.xrange(stream, start, end);
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
            //>读取未被ack的消息
            StreamIDEntry<String,StreamEntryID> streamEntryIDEntry =
                    new StreamIDEntry<>(stream,StreamEntryID.UNRECEIVED_ENTRY);

            //只读取当前topic的stream key,没数据阻塞
            List<Map.Entry<String/*key*/, List<StreamEntry>>> entries =
                    jedis.xreadGroup(groupName,
                            consumer,
                            count, Long.MAX_VALUE, true, streamEntryIDEntry);

            Map.Entry<String, List<StreamEntry>> streamEntryList = entries.get(0);
            return streamEntryList.getValue().stream().map(streamEntry -> {
                MessageExt messageExt = new MessageExt();
                messageExt.setMsgId(streamEntry.getID().toString());
                Map<String, String> content = streamEntry.getFields();
                messageExt.setContent(content);
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
    public long xack(String stream, String group, List<String> msgIds) {
        Jedis jedis = jedisPool.getResource();
        try {
            StreamEntryID[] streamEntryIDS = msgIds.stream().map(StreamEntryID::new).toArray(StreamEntryID[]::new);
            return jedis.xack(stream, group, streamEntryIDS);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public long xdel(String stream, List<String> msgIds){
        Jedis jedis = jedisPool.getResource();
        try {
            StreamEntryID[] streamEntryIDS = msgIds.stream().map(StreamEntryID::new).toArray(StreamEntryID[]::new);
            return jedis.xdel(stream, streamEntryIDS);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public List<ConsumerInfo> xinfoConsumers(String stream, String group){
        Jedis jedis = jedisPool.getResource();
        try {
            List<StreamConsumersInfo> streamConsumersInfos = jedis.xinfoConsumers(stream, group);
            System.out.println("=====>"+ JSON.toJSONString(streamConsumersInfos));
            return null;
        } finally {
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
    public long zadd(String key, String msgId, double score) {
        Jedis jedis = jedisPool.getResource();
        try {
            Long zadd = jedis.zadd(key, score, msgId);
            return zadd == null ? 0 : zadd;
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
    public Set<String> zrangeAndRemByScore(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        try {
            Transaction multi = jedis.multi();
            Response<Set<String>> zrangeByScore = multi.zrangeByScore(key, start, end);
            Response<Long> longResponse = multi.zremrangeByScore(key, start, end);
            multi.exec();
            Set<String> msgIds = zrangeByScore.get();
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
        return 0;
    }

    @Override
    public long zrem(String key, List<String> msgIds) {
        Jedis jedis = jedisPool.getResource();
        try {
            Long res = jedis.zrem(key, msgIds.toArray(new String[msgIds.size()]));
            return res == null ? 0 : res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
        return 0;
    }

    @Override
    public List<String> hmget(String key,List<String> fields) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hmget(key, fields.toArray(new String[fields.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
        return null;
    }

    @Override
    public boolean hmset(String key, Map<String, String> data){
        Jedis jedis = jedisPool.getResource();
        try {
            String hmset = jedis.hmset(key, data);
            if(SUCCESS.equals(hmset)){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
        return false;
    }

    @Override
    public boolean hmset(byte[] key,Map<byte[],byte[]> data){
        Jedis jedis = jedisPool.getResource();
        try {
            String hmset = jedis.hmset(key, data);
            if(SUCCESS.equals(hmset)){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
        return false;
    }
}

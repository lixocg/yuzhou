package com.yuzhou.rmq.remoting;

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
        try {
            List<StreamGroupInfo> streamGroupInfos = jedis.xinfoGroup(stream);
            if (streamGroupInfos == null || streamGroupInfos.size() == 0) {
                return false;
            }
            return streamGroupInfos.stream().anyMatch(
                    streamGroupInfo -> streamGroupInfo.getName().equals(group));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean createGroup(String stream, String groupName) {
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
        }
        return false;
    }

    public MessageExt readMsg(String stream, String msgId) {
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
    }

    /**
     * TODO 需要修改方法名
     * 阻塞试读取未被其他同组其他消费者消费的数据
     *
     * @param groupName
     * @param consumer
     * @param stream
     * @param count
     * @return
     */
    @Override
    public List<MessageExt> readMsgList(String groupName, String consumer, String stream, int count) {

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
    }

    @Override
    public PutResult putMsg(String topic, Map<String, String> msg) {
        StreamEntryID streamEntryID = jedis.xadd(topic, StreamEntryID.NEW_ENTRY, msg);
        return PutResult.id(streamEntryID.getTime(), streamEntryID.getSequence());
    }

    private JedisPool jedisPool;

    private Jedis jedis;

    @Override
    public void start() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxIdle(2000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 3000);
        jedis = jedisPool.getResource();
    }

    @Override
    public void shutdown(Jedis jedis) {
        jedisPool.returnResource(jedis);
    }

}

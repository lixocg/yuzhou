package com.yuzhou.rmq.remoting;

import com.yuzhou.rmq.common.Message;
import com.yuzhou.rmq.common.SendResult;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

    private String consumerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "UNKOWN-CONSUMER";
    }

    public static final String GROUP_CREATE_SUCCESS = "ok";


    @Override
    public boolean createGroup(String topic, String groupName) {
        try {
            //创建消费组从队列未读取
            String ok = jedis.xgroupCreate(topic, groupName, StreamEntryID.LAST_ENTRY, true);
            if(GROUP_CREATE_SUCCESS.equals(ok)){
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    @Override
    public List<Message> readMsg(String groupName, String topic, int count) {

        Map<String, StreamEntryID> streamMap = new HashMap<>();
        streamMap.put(topic, StreamEntryID.UNRECEIVED_ENTRY);
        Map.Entry<String, StreamEntryID> stream = streamMap.entrySet().iterator().next();

        //只读取当前topic的stream key,没数据阻塞
        List<Map.Entry<String/*key*/, List<StreamEntry>>> entries =
                jedis.xreadGroup(groupName,
                        consumerName(),
                        count, Long.MAX_VALUE, false, stream);

        Map.Entry<String, List<StreamEntry>> streamEntryList = entries.get(0);
        List<Message> messageList = streamEntryList.getValue().stream().map(streamEntry -> {
            Message message = new Message();
            message.setMsgId(streamEntry.getID().toString());
            message.setContent(streamEntry.getFields());
            return message;
        }).collect(Collectors.toList());

        return messageList;
    }

    @Override
    public SendResult putMsg(String topic, Map<String, String> msg) {
        StreamEntryID streamEntryID = jedis.xadd(topic, StreamEntryID.NEW_ENTRY, msg);
        return SendResult.id(streamEntryID.getTime(), streamEntryID.getSequence());
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

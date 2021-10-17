package com.yuzhou;

import com.alibaba.fastjson.JSON;
import com.yuzhou.rmq.common.Message;
import com.yuzhou.rmq.common.StreamIDEntry;
import com.yuzhou.rmq.connection.SingleRedisConn;
import com.yuzhou.rmq.remoting.redis.SingleRedisClient;
import com.yuzhou.rmq.utils.SerializeUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XReadParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-28
 * Time: 下午11:58
 */
public class SingleRedisClientTest {

    SingleRedisClient client;


    @Before
    public void before() {
        client = new SingleRedisClient(new SingleRedisConn());
        client.start();
    }

    @Test
    public void testdeSerialize() {
        Jedis jedis = client.jedisPool.getResource();
        XReadParams params = new XReadParams();
        params.count(2);

        StreamEntryID streamEntryID = new StreamEntryID("112123-0");
        StreamIDEntry<String, StreamEntryID> streamIDEntry = new StreamIDEntry("ss1", streamEntryID);

        Map<String, StreamEntryID> map = new HashMap<>();
        map.put(streamIDEntry.getKey(), streamIDEntry.getValue());
//        List<Map.Entry<String, List<StreamEntry>>> xread = jedis.xread(params, map);
//        System.out.println(JSON.toJSONString(xread));


        StreamIDEntry<byte[], byte[]> streamIDEntryBytes = new StreamIDEntry<>(
                SerializeUtils.serialize(streamIDEntry.getKey()),
                SerializeUtils.serialize(streamIDEntry.getValue())
        );
        List<byte[]> xread1 = jedis.xread(params, streamIDEntryBytes);
        Object deserialize = SerializationUtils.deserialize(xread1.get(0));
        System.out.println(deserialize);

    }

    @Test
    public void tesetMutilStream() {
        Jedis jedis = client.jedisPool.getResource();
    }


    @Test
    public void testzadd() {
        Message message = new Message();
        Map<String, String> map = new HashMap<>();
        map.put("name", "zs");
        map.put("_count", "1");
        message.setTopic("test");
        message.setContent(map);

        Jedis jedis = client.jedisPool.getResource();
        jedis.zadd(message.getTopic().getBytes(), 1, SerializeUtils.serialize(message));
        Set<byte[]> bytes = jedis.zrangeByScore(message.getTopic().getBytes(), 0, 1000);

        bytes.forEach(bytes1 -> {
            System.out.println(SerializeUtils.deserialize(bytes1,Message.class));
        });
    }

}

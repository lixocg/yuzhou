package com.yuzhou.demo.redis;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-15
 * Time: 下午2:35
 */
@Configuration
@ComponentScan
public class JedisStreamMain {

    private Jedis jedis;


    @Before
    public void before() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(JedisStreamMain.class);
        jedis = applicationContext.getBean(Jedis.class);
    }

    @Test
    public void testAdd() {
        Map<String, String> data = new HashMap<>();
        data.put("name", "zs");
        data.put("sex", "男");
        StreamEntryID addResult = jedis.xadd("stream2", null, data);
        System.out.println("添加结果:" + JSON.toJSONString(addResult));
    }

    @Test
    public void testCreateGroup() {
        String s = jedis.xgroupCreate("stream2", "group001", StreamEntryID.LAST_ENTRY, true);
        System.out.println(s);
    }

    @Test
    public void testGroupRead() {

        Map<String, StreamEntryID> t = new HashMap<>();
        t.put("stream2", null);//null 则为 > 重头读起，也可以为$接受新消息，还可以是上一次未读完的消息id
        Map.Entry<String, StreamEntryID> stream = t.entrySet().iterator().next();
        List<Map.Entry<String, List<StreamEntry>>> entries = jedis.xreadGroup("group001", "consumer01", 1, 0, true, stream);
        entries.forEach(entry -> {
            String key = entry.getKey();
            List<StreamEntry> value = entry.getValue();
            System.out.println("key---->" + key);
            System.out.println("value---->" + JSON.toJSONString(value));
        });
    }

}

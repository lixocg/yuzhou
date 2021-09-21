package com.yuzhou;

import com.alibaba.fastjson.JSON;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.remoting.JedisRemotingInstance;
import com.yuzhou.rmq.remoting.PutResult;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午11:31
 */
public class JedisRemotingInstanceTest {

    JedisRemotingInstance instance;

    String topic = "testTopic1";

    String group = "testGroup1";

    String consumer = "testC1";

    @Before
    public void before() {
        instance = new JedisRemotingInstance();
        instance.start();

        System.out.println(instance.jedisPool.getResource());
        System.out.println(instance.jedisPool.getResource());
    }

    @Test
    public void testCreateGroup() {
        boolean group = instance.createGroup(topic, this.group);
        System.out.println("创建group:" + group);
    }

    @Test
    public void testAddAndRead() {
        Map<String, String> msg = new HashMap<>();
        msg.put("name", "zs");
        PutResult result = instance.putMsg(topic, msg);
        System.out.println(result);


        MessageExt messageExt = instance.readMsg(topic, result.getMsgId());
        System.out.println(JSON.toJSONString(messageExt));
    }

    @Test
    public void testReadMsg() {
    }

    @Test
    public void testPutMsgWithScore() {
        instance.putMsgWithScore("scoreTestKey", "id2", System.currentTimeMillis());
    }

    @Test
    public void testDelayMsg() {
        String topic = "topic1";

        Map<String, String> msg = new HashMap<>();
        msg.put("name", "lisi");
        PutResult result = instance.putDelayMsg(topic, msg, System.currentTimeMillis() - 5 * 60 * 1000);
        System.out.println(result);

        List<MessageExt> messageExts = instance.readDelayMsgBeforeNow(topic);
        System.out.println(JSON.toJSONString(messageExts));
    }

    @Test
    public void testReadDelayMsg(){
         final ScheduledExecutorService delayPullMsgExecutor =
                Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("ConsumeMessageDelayThread_"));

        delayPullMsgExecutor.scheduleAtFixedRate(()->{
            List<MessageExt> messageExts = instance.readDelayMsgBeforeNow("mytopic");
            System.out.println(JSON.toJSONString(messageExts));
        }, 3, 2, TimeUnit.SECONDS);

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

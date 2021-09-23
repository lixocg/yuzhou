package com.yuzhou;

import com.yuzhou.rmq.common.ThreadFactoryImpl;
import com.yuzhou.rmq.remoting.JedisRemotingInstance;
import com.yuzhou.rmq.common.PutResult;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
        instance = new JedisRemotingInstance("127.0.0.1",6379);
        instance.start();

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


//        MessageExt messageExt = instance.(topic, result.getMsgId());
//        System.out.println(JSON.toJSONString(messageExt));
    }

    @Test
    public void testReadMsg() {
    }

    @Test
    public void testPutMsgWithScore() {
    }

    @Test
    public void testDelayMsg() {
        String topic = "topic1";

        Map<String, String> msg = new HashMap<>();
        msg.put("name", "lisi");
        PutResult result = instance.putDelayMsg(topic, msg, System.currentTimeMillis() - 5 * 60 * 1000);
        System.out.println(result);

    }

    @Test
    public void testReadDelayMsg(){
         final ScheduledExecutorService delayPullMsgExecutor =
                Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("ConsumeMessageDelayThread_"));

    }
}

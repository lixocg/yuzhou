package com.yuzhou;

import com.yuzhou.rmq.common.PutResult;
import com.yuzhou.rmq.factory.MQClientInstance;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午11:31
 */
public class MQClientInstanceTest {

    MQClientInstance instance;

    String topic = "testTopic1";

    String group = "testGroup1";

    String consumer = "testC1";

    @Before
    public void before() {
        instance = new MQClientInstance("127.0.0.1",6379);
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

    }
}

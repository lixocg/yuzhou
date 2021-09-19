package com.yuzhou;

import com.alibaba.fastjson.JSON;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.SendResult;
import com.yuzhou.rmq.remoting.JedisRemotingInstance;
import com.yuzhou.rmq.remoting.PutResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void testReadMsg(){
    }
}

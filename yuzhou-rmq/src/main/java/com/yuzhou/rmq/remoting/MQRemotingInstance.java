package com.yuzhou.rmq.remoting;

import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.SendResult;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 上午9:35
 */
public interface MQRemotingInstance<RC> {

    PutResult putDelayMsg(String topic, Map<String, String> msg, long score);

    List<MessageExt> readDelayMsgBeforeNow(String topic);

    void start();

    void shutdown();

    boolean createGroup(String topic, String groupName);

    List<MessageExt> blockedReadMsgs(String groupName,String consumer, String topic, int count);

    PutResult putMsg(String topic, Map<String,String> msg);

}

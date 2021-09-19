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
    void start();

    void shutdown(RC rc);

    boolean createGroup(String topic, String groupName);

    List<MessageExt> readMsgList(String groupName,String consumer, String topic, int count);

    PutResult putMsg(String topic, Map<String,String> msg);

}

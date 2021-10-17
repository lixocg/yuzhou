package com.yuzhou.quickstart.defolt;

import com.alibaba.fastjson.JSON;
import com.yuzhou.rmq.client.MessageListener;
import com.yuzhou.rmq.client.impl.DefaultMQConsumer;
import com.yuzhou.rmq.common.ConsumeContext;
import com.yuzhou.rmq.common.ConsumeFromWhere;
import com.yuzhou.rmq.common.ConsumeStatus;
import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.connection.SingleRedisConn;
import com.yuzhou.rmq.log.InnerLog;
import com.yuzhou.rmq.utils.DateUtil;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-11
 * Time: 下午9:12
 */
public class Consumer {
    static Logger logger = InnerLog.getLogger(DefaultMQConsumer.class);

    static AtomicInteger count = new AtomicInteger(1);


    public static void main(String[] args) {
        DefaultMQConsumer consumer = new DefaultMQConsumer("delayGroup", "mytopics2");
        consumer.setConnection(new SingleRedisConn());
        consumer.setPullBatchSize(2);
//        consumer.setConsumeFromWhere(new ConsumeFromWhere(1633855596020L));
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//        consumer.setPullInterval(3 * 1000);
        consumer.registerMessageListener(new MessageListener() {
            @Override
            public ConsumeStatus onMessage(List<MessageExt> msgs, ConsumeContext context) {
                msgs.forEach(msg -> {
                    System.out.println(String.format("topic=%s,time=%s,msgId=%s,offsetMsgId=%s,msgIdTime=%s,data=%s,msgCount=%d",
                            context.getTopic(),
                            DateUtil.nowStr(), msg.getMsgId(),msg.getOffsetMsgId(), msgId(msg.getOffsetMsgId()), msg.getContent(), count.getAndIncrement()));
                });
                try {
                    Thread.sleep(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ConsumeStatus.CONSUME_SUCCESS;
//                return ConsumeStatus.CONSUME_LATER;
            }

            private String msgId(String msgId) {
                String[] split = msgId.split("-");
                return DateUtil.toStr(Long.parseLong(split[0])) + "-" + split[1];
            }

            @Override
            public void onMaxRetryFailMessage(List<MessageExt> msgs, ConsumeContext consumeContext) {
                System.out.println("最大重试失败:" + JSON.toJSONString(msgs));
            }
        });
        consumer.start();


    }
}

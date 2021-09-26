package com.yuzhou.rmq.remoting;

import com.yuzhou.rmq.common.MessageExt;
import com.yuzhou.rmq.common.MsgReservedKey;
import com.yuzhou.rmq.common.MsgRetryLevel;
import com.yuzhou.rmq.common.PullResult;
import com.yuzhou.rmq.common.PutResult;
import com.yuzhou.rmq.consumer.DefaultMQConsumerService;
import com.yuzhou.rmq.remoting.redis.Remoting;
import com.yuzhou.rmq.remoting.redis.SingleRedisClient;
import com.yuzhou.rmq.utils.MixUtil;
import com.yuzhou.rmq.utils.TypeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午2:46
 */
public class DefaultPullMsgService implements PullService {

    Logger log = LoggerFactory.getLogger(DefaultMQConsumerService.class);

    private Remoting remoting;

    private String host;

    private int ip;

    public DefaultPullMsgService(String host, int ip) {
        this.host = host;
        this.ip = ip;
    }

    @Override
    public boolean createGroup(String stream, String groupName) {
        return remoting.xgroupCreate(stream, groupName);
    }


    /**
     * 阻塞试读取未被其他同组其他消费者消费的数据
     *
     * @param groupName
     * @param consumer
     * @param topic
     * @param count
     * @return
     */
    @Override
    public PullResult blockedReadMsgs(String groupName, String consumer, String topic, int count) {
        List<MessageExt> messageExts = remoting.xreadGroup(groupName, consumer, topic, count);

        return PullResult.builder()
                .messageExts(messageExts)
                .topic(topic)
                .processCallback(new DefaultProcessCallback())
                .build();
    }

    @Override
    public PutResult putMsg(String topic, Map<String, String> msg) {
        return PutResult.id(remoting.xadd(topic, msg));
    }


    /**
     * 消息投入到延迟队列中
     *
     * @param topic
     * @param msg
     * @param timestamp 延迟时间，单位毫秒
     * @return
     */
    @Override
    public PutResult putDelayMsg(String topic, Map<String, String> msg, long timestamp) {
        //1.往延迟topic stream队列中存入元数据,%DELAY%_topic
        String msgId = remoting.xadd(MixUtil.delayTopic(topic), msg);
        if (StringUtils.isBlank(msgId)) {
            return PutResult.err("投入stream失败");
        }
        //2.往zset队列中存入,%DELAYSCORE%_topic  TODO 这两个操作需要事物保证
        long zadd = remoting.zadd(MixUtil.delayScoreTopic(topic), msgId, TypeUtil.l2d(timestamp));
        return PutResult.id(msgId);
    }

    /**
     * 读取延迟队列中小于当前时间消息列表
     * 获取到数据需要删除数据
     *
     * @return
     */
    @Override
    public PullResult readDelayMsgBeforeNow(String groupName,String topic) {
        long currentTimeMillis = System.currentTimeMillis();
        List<MessageExt> messageExts = readDelayMsg(topic, 0, currentTimeMillis);

        return PullResult.builder()
                .messageExts(messageExts)
                .topic(topic)
                .processCallback(new DefaultProcessCallback())
                .build();
    }

    private void onFail(List<MessageExt> messageExts, String topic) {
        try {
            //投入重试队列
            messageExts.parallelStream().forEach(messageExt -> {
                Map<String, String> content = messageExt.getContent();
                int count = Integer.parseInt(content.getOrDefault(MsgReservedKey.RETRY_COUNT.getKey(), "0"));
                if (count == MsgRetryLevel.MAX_RETRY_COUNT) {
                    //放入死信队列
                    //TODO
                    long zrem = remoting.zrem(MixUtil.delayScoreTopic(topic), Collections.singletonList(messageExt.getMsgId()));
                    System.out.println("del----:" + zrem);
                    return;
                }
                MsgRetryLevel msgRetryLevel = MsgRetryLevel.getByCount(count);
                if (msgRetryLevel == null) {
                    log.error("无效重试次数,msgId={},count={}", messageExt.getMsgId(), count);
                    return;
                }
                if (count == 0) {
                    //第一次放重试队列和权重zset
                    content.put(MsgReservedKey.RETRY_COUNT.getKey(), String.valueOf(++count));
                    putDelayMsg(topic, content, System.currentTimeMillis() + (msgRetryLevel.getDelay() * 1000));
                } else {
                    content.put(MsgReservedKey.RETRY_COUNT.getKey(), String.valueOf(++count));
                    putDelayMsg(topic, content, System.currentTimeMillis() + (msgRetryLevel.getDelay() * 1000));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 读取延迟队列指定范围数据
     * 获取到数据需要删除数据
     *
     * @param topic
     * @return
     */
    public List<MessageExt> readDelayMsg(String topic, long start, long end) {
        Set<String> msgIds = remoting.zrangeAndRemByScore(MixUtil.delayScoreTopic(topic), start, end);

        if (msgIds == null) {
            return null;
        }
        //并行到重试队列中获取并返回
        return msgIds.parallelStream()
                .map(msgId -> remoting.xRead(MixUtil.delayTopic(topic), msgId))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Override
    public void start() {
        remoting = new SingleRedisClient(host, ip);
        remoting.start();
    }

    @Override
    public void shutdown() {
        remoting.shutdown();
    }


    class DefaultProcessCallback implements ProcessCallback{
        @Override
        public void onSuccess(Context context) {
            //ack 消息
            List<String> msgIds = context.getMessageExts().stream().map(MessageExt::getMsgId).collect(Collectors.toList());
            remoting.xack(context.getTopic(), context.getGroup(), msgIds);
        }

        @Override
        public void onFail(Context context) {
            DefaultPullMsgService.this.onFail(context.getMessageExts(), context.getTopic());
        }
    }
}

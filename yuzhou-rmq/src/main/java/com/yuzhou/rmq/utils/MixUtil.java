package com.yuzhou.rmq.utils;

import com.yuzhou.rmq.common.TopicGroup;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yuzhou.rmq.utils.SerializeUtils.serialize;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-20
 * Time: 下午3:45
 */
public class MixUtil {

    private static final char DELIMITER = '_';

    private static final String DELAY_PREFIX = "%DELAY%" + DELIMITER;

    private static final String DELAY_SCORE_PREFIX = "%DELAYSCORE%" + DELIMITER;

    /**
     * 延迟队列名：%DELAY%_topic
     * @param topic
     * @return
     */
    public static String delayTopic(String topic) {
        return DELAY_PREFIX + topic;
    }

    /**
     * 延迟队列消息id对应score的队列名：%DELAYSCORE%_topic
     * @param topic
     * @return
     */
    public static String delayScoreTopic(String topic) {
        return DELAY_SCORE_PREFIX + topic;
    }


    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @param source
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<>();
        int remaider = source.size() % n;
        int number = source.size() / n;
        int offset = 0;
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            if(value.size() > 0) {
                result.add(value);
            }
        }
        return result;
    }

    public Map<byte[],byte[]> toMap(TopicGroup topicGroup){
        if(topicGroup == null){
            return null;
        }

        Map<byte[], byte[]> map = new HashMap<>();


        Class<? extends TopicGroup> topicGroupClass = topicGroup.getClass();
        for (Field field : topicGroupClass.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object val = field.get(topicGroup);
            map.put(serialize(fieldName,fieldName.getClass()), serialize(val,val.getClass()));
        }
        return map;
    }
}

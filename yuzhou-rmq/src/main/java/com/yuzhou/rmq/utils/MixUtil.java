package com.yuzhou.rmq.utils;

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
}

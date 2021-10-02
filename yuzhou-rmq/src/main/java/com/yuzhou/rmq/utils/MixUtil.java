package com.yuzhou.rmq.utils;

import com.alibaba.fastjson.JSON;
import com.yuzhou.rmq.common.TopicGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-20
 * Time: 下午3:45
 */
public class MixUtil {

    private final static Logger logger = LoggerFactory.getLogger(MixUtil.class);

    private static final char DELIMITER = '_';

    private static final String DELAY_PREFIX = "%DELAY%" + DELIMITER;

    private static final String DELAY_SCORE_PREFIX = "%DELAYSCORE%" + DELIMITER;

    private static final String MQ_PREFIX = "%RMQ%" + DELIMITER;

    public static String wrap(String name) {
        return MQ_PREFIX + name;
    }

    /**
     * 延迟队列名：%DELAY%_topic
     *
     * @param topic
     * @return
     */
    public static String delayTopic(String topic) {
        return DELAY_PREFIX + topic;
    }

    /**
     * 延迟队列消息id对应score的队列名：%DELAYSCORE%_topic
     *
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
            if (value.size() > 0) {
                result.add(value);
            }
        }
        return result;
    }

    public static Map<String, String> toMap(TopicGroup topicGroup) {
        try {
            if (topicGroup == null) {
                return null;
            }

            Map<String, String> map = new HashMap<>();


            Class<? extends TopicGroup> topicGroupClass = topicGroup.getClass();
            for (Field field : topicGroupClass.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object val = field.get(topicGroup);
                map.put(fieldName, JSON.toJSONString(val));
            }
            return map;
        } catch (Exception e) {
            logger.error("字段转换错误", e);
        }
        return null;
    }

    public static String getConsumerName() {
        String localAddress = getLocalAddress();
        if (localAddress == null) {
            return "UNKOWN-NAME";
        }
        return localAddress;
    }


    private static String getLocalAddress() {
        try {
            // Traversal Network interface to get the first non-loopback and non-private address
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            ArrayList<String> ipv4Result = new ArrayList<String>();
            ArrayList<String> ipv6Result = new ArrayList<String>();
            while (enumeration.hasMoreElements()) {
                final NetworkInterface networkInterface = enumeration.nextElement();
                final Enumeration<InetAddress> en = networkInterface.getInetAddresses();
                while (en.hasMoreElements()) {
                    final InetAddress address = en.nextElement();
                    if (!address.isLoopbackAddress()) {
                        if (address instanceof Inet6Address) {
                            ipv6Result.add(normalizeHostAddress(address));
                        } else {
                            ipv4Result.add(normalizeHostAddress(address));
                        }
                    }
                }
            }

            // prefer ipv4
            if (!ipv4Result.isEmpty()) {
                for (String ip : ipv4Result) {
                    if (ip.startsWith("127.0") || ip.startsWith("192.168")) {
                        continue;
                    }

                    return ip;
                }

                return ipv4Result.get(ipv4Result.size() - 1);
            } else if (!ipv6Result.isEmpty()) {
                return ipv6Result.get(0);
            }
            //If failed to find,fall back to localhost
            final InetAddress localHost = InetAddress.getLocalHost();
            return normalizeHostAddress(localHost);
        } catch (Exception e) {
            logger.error("Failed to obtain local address", e);
        }

        return null;
    }


    private static String normalizeHostAddress(final InetAddress localHost) {
        if (localHost instanceof Inet6Address) {
            return "[" + localHost.getHostAddress() + "]";
        } else {
            return localHost.getHostAddress();
        }
    }

}

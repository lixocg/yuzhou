package com.yuzhou.rmq.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午4:27
 */
public class DateUtil {

    public final static String FULL = "yyyy-MM-dd HH:mm:ss.SSS";

    public final static String STANDARD = "yyyy-MM-dd HH:mm:ss";

    public static String nowStr() {
        return FastDateFormat.getInstance(STANDARD).format(new Date());
    }

    public static String toStr(long timestamp) {
        return FastDateFormat.getInstance(FULL).format(timestamp);
    }
}

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

    public static String nowStr() {
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}

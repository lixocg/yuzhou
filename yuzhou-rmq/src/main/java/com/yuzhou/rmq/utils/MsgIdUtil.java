package com.yuzhou.rmq.utils;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-19
 * Time: 下午11:39
 */
public class MsgIdUtil {

    public static String id(long time, long seq) {
        return time + "-" + seq;
    }

}

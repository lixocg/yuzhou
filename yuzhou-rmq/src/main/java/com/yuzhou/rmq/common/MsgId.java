package com.yuzhou.rmq.common;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-10
 * Time: 下午4:20
 */
public class MsgId implements Serializable {

    private static final long serialVersionUID = 7989816700853372740L;

    private static final String DELIMITER = "-";

    private long time;

    private long seq;

    private MsgId(long time, long seq) {
        this.time = time;
        this.seq = seq;
    }

    public static MsgId of(long time, long seq) {
        return new MsgId(time, seq);
    }

    public static MsgId split(String msgId) {
        String[] split = msgId.split(DELIMITER);
        return MsgId.of(Long.parseLong(split[0]), Long.parseLong(split[1]));
    }

    public static String id(long time, long seq) {
        return time + DELIMITER + seq;
    }

    public long getTime() {
        return time;
    }

    public long getSeq() {
        return seq;
    }
}

package com.yuzhou.rmq.client.id;

import com.yuzhou.rmq.utils.MixUtil;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: lixiongcheng
 * @date: 2021/10/16 下午3:31
 */
public class MessageId {

    private static final int LEN;
    private static final String FIX_STRING;
    private static final AtomicInteger COUNTER;
    private static long startTime;
    private static long nextStartTime;

    static {
        LEN = 4 + 2 + 4 + 2;//12=4(ip)+2(pid)+4(时间差)+2(自增)
        ByteBuffer tempBuffer = ByteBuffer.allocate(6);
        tempBuffer.position(2);
        tempBuffer.putInt(MixUtil.getPid());
        tempBuffer.position(0);
        try {
            tempBuffer.put(MixUtil.getIP());
        } catch (Exception e) {
            tempBuffer.put(createFakeIP());
        }
        FIX_STRING = MixUtil.bytes2string(tempBuffer.array());
        setStartTime(System.currentTimeMillis());
        COUNTER = new AtomicInteger(0);
    }

    private synchronized static void setStartTime(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        startTime = cal.getTimeInMillis();//millis本月初
        cal.add(Calendar.MONTH, 1);
        nextStartTime = cal.getTimeInMillis();////millis下月初
    }

    public static byte[] createFakeIP() {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(System.currentTimeMillis());
        bb.position(4);
        byte[] fakeIP = new byte[4];
        bb.get(fakeIP);
        return fakeIP;
    }

    public static String getIPStrFromID(String msgID) {
        byte[] ipBytes = getIPFromID(msgID);
        return MixUtil.ipToIPv4Str(ipBytes);
    }

    public static byte[] getIPFromID(String msgID) {
        byte[] result = new byte[4];
        byte[] bytes = MixUtil.string2bytes(msgID);
        System.arraycopy(bytes, 0, result, 0, 4);
        return result;
    }

    public static String createUniqID() {
        StringBuilder sb = new StringBuilder(LEN * 2);
        sb.append(FIX_STRING);
        sb.append(MixUtil.bytes2string(createUniqIDBuffer()));
        return sb.toString();
    }

    private static byte[] createUniqIDBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(4 + 2);
        long current = System.currentTimeMillis();
        if (current >= nextStartTime) {
            setStartTime(current);
        }
        buffer.position(0);
        buffer.putInt((int) (System.currentTimeMillis() - startTime));
        buffer.putShort((short) COUNTER.getAndIncrement());
        return buffer.array();
    }

}

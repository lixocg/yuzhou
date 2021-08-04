package com.yuzhou.log.util;


public class EagleEyeUtil {
    /**
     * 全链路压测的t标记，当前有两个值，两个均为压测时使用，如下所示，一个用于走全链路压测逻辑，一个用于日志回放
     (1) t=1，为全链路压测逻辑的绕过
     (2) t=2，为在进行日志回放时，绕过一些业务埋点等（避免对线上的业务数据统计造成影响）
     */

    private static final String PRESSURE_TEST_FLAG = "1";
    private static final String PRESSURE_TEST_FLAG2 = "2";

    private static final String PRESSURE_TEST_KEY = "t";

    /**
     * 是否压测请求
     * @return true or fasle
     */
    public static boolean isPressureTestRequest() {
//        return PRESSURE_TEST_FLAG.equals(EagleEye.getUserData(PRESSURE_TEST_KEY)) || PRESSURE_TEST_FLAG2.equals(EagleEye.getUserData(PRESSURE_TEST_KEY));
        return false;
    }

    public static String getTraceId(){
//        return EagleEye.getTraceId();
        return null;
    }

    public static String remotePressureTestFlag() {
//        return EagleEye.removeUserData(PRESSURE_TEST_KEY);
        return null;
    }

    public static String storePressureTestFlag(String flag) {
//        return EagleEye.putUserData(PRESSURE_TEST_KEY, flag);
        return null;
    }
}
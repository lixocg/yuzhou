package com.yuzhou.log.common;

/**
 * 可支持的log类型,需要实现接口{@link com.yuzhou.log.log.LogService},或者继承抽象类{@link com.yuzhou.log.log.LogServiceAdapter}
 * @author xiongcheng.lxch
 * @see com.yuzhou.log.log.LogService
 * @see com.yuzhou.log.log.LogServiceAdapter
 */
public enum LogType {

    SCHEDULERX,
    HSF_PROVIDER,
    METAQ_CONSUMER,
    ;

}

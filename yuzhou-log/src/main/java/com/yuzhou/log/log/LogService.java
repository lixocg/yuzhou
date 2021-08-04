package com.yuzhou.log.log;

import com.yuzhou.log.annotation.CommonLog;
import com.yuzhou.log.common.LogType;
import com.yuzhou.log.common.MethodWrapper;
import com.yuzhou.log.common.OutResultInParams;
import com.yuzhou.log.common.ResultWrapper;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <p>
 * 日志服务接口。
 * <p>
 * 实现一种新的日志服务，需要在{@link LogType}中增加一个日志类型，对应实现方法{@link #logType()}返回此类型。
 * <p>
 * 提供{@link LogServiceAdapter}适配类，该类实现了日志格式定义{@link #logFormat()}和通用日志答应{@link #logInfo(MethodWrapper, ResultWrapper)}，
 * 开发者可直接继承该类。
 *
 * <p>
 * 注意：实现方不要覆盖默认构造方法，因为在{@link LogServiceFactory}初始化时，需要通过默认构造方法创建日志服务实例。
 * <p>
 * 测试demo可参见 com.alihealth.easysdk.test.javaconfig.MainTest
 * <p>
 *
 * @author xiongcheng.lxch
 * @see LogServiceFactory
 */
public interface LogService {
    /**
     * log INFO 日志打印
     *
     * @param methodWrapper 目标方法调用包装
     * @param resultWrapper    目标方法执行结果包装
     */
    void logInfo(MethodWrapper methodWrapper, ResultWrapper resultWrapper);

    /**
     * 获取标注方法的CommonLog注解
     *
     * @param methodInvocation 目标方法调用
     * @return commonLog
     */
    CommonLog commonLog(MethodInvocation methodInvocation);

    /**
     * 日志格式
     *
     * @return
     */
    String logFormat();

    /**
     * 日志类型,定义在{@link LogType}
     *
     * @return
     */
    LogType logType();


    /**
     * 参数信息
     * @param methodInvocation
     * @param resultWrapper
     * @return
     */
    OutResultInParams outResultParams(MethodInvocation methodInvocation, ResultWrapper resultWrapper);
}

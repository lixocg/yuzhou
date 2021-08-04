package com.yuzhou.log.annotation;

import com.yuzhou.log.concurrent.DefaultRejectExecuteHandler;
import com.yuzhou.log.config.CommonLogConfigurationSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * 启用Spring注解驱动日志管理能力
 *
 * @author lixiongcheng
 * @see com.yuzhou.log.config.CommonLogConfiguration
 * @see CommonLog
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CommonLogConfigurationSelector.class)
public @interface EnableCommonLog {
    /**
     * 是否使用CGLIB代理
     *
     * @return
     */
    boolean proxyTargetClass() default true;

    /**
     * 指定全局的代理模式，默认为{@link AdviceMode#PROXY}；
     * 备注：这个设置和{@link #proxyTargetClass()} 一样，会影响全局设置，也需要保持一致；
     */
    AdviceMode mode() default AdviceMode.PROXY;

    /**
     * 设置自动代理类的优先级
     */
    int order() default Ordered.LOWEST_PRECEDENCE;

    /**
     * 日志框架打印日志耗时开关
     * @return
     */
    boolean isPrintConsume() default false;

    /**
     * 同步/异步打印日志，默认异步
     * @return
     */
    boolean async() default true;

    /**
     * 异步线程池核心线程数
     * @return
     */
    int corePoolSize() default 20;

    /**
     * 异步线程池最大线程数
     * @return
     */
    int maxPoolSize() default 20;

    /**
     * 异步线程池队列大小
     * @return
     */
    int queueSize() default 2000;

    /**
     * 异步线程池拒绝策略
     * @return
     */
    Class<? extends RejectedExecutionHandler> rejectHandler() default  DefaultRejectExecuteHandler.class;

}

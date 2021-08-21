package com.yuzhou.log.annotation;


import com.yuzhou.log.common.LogType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志服务打印标记
 * @author xiongcheng.lxch
 * @see com.yuzhou.log.log.LogService
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonLog {
    /**
     * 日志服务类型
     */
    LogType type();

    /**
     * 服务成功标志字段，框架自动根据默认值查找
     */
    String successField() default "success";

    /**
     * 服务异常错误码字段，框架自动根据默认值查找
     */
    String errCodeField() default "errCode,msgCode";

    /**
     * 服务异常错误信息字段，框架自动根据默认值查找
     */
    String errMessageField() default "errMessage,msgInfo";

    /**
     * 服务成功数据字段，框架自动根据默认值查找
     */
    String retDataField() default "model,data";


    /**
     * 自定义入参
     */
    Param[] inputParams() default {};

    /**
     * 自定义出参
     */
    Param outputParam() default @Param();


    @Retention(RetentionPolicy.RUNTIME)
    @Target({})
    @interface Param {
        @AliasFor("names")
        String[] value() default {};
        /**
         * 参数列表索引，从0开始
         */
        int index() default 0;

        /**
         * 参数名
         */
        @AliasFor("value")
        String[] names() default {};
    }
}

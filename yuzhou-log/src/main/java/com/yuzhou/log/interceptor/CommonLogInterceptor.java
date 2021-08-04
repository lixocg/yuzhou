package com.yuzhou.log.interceptor;

import com.yuzhou.log.common.MethodWrapper;
import com.yuzhou.log.common.ResultWrapper;
import org.aopalliance.intercept.MethodInterceptor;

import java.io.Serializable;

/**
 * 日志切面增加
 *
 * @author xiongcheng.lxch
 */
public interface CommonLogInterceptor extends MethodInterceptor, Serializable {
    void printLog(MethodWrapper methodWrapper, ResultWrapper resultWrapper);

    void setPrintConsume(boolean printConsume);
}

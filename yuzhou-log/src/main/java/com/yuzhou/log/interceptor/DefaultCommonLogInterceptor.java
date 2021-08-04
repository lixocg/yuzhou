package com.yuzhou.log.interceptor;

import com.yuzhou.log.common.MethodWrapper;
import com.yuzhou.log.common.ResultWrapper;
import com.yuzhou.log.log.InnerLog;
import org.slf4j.Logger;

/**
 * AOP 方法拦截日志增强
 *
 * @author xiongcheng.lxch
 * @see CommonLogAspectSupport
 */
public class DefaultCommonLogInterceptor extends CommonLogAspectSupport {
    private Logger logger = InnerLog.getLogger();


    @Override
    public void printLog(MethodWrapper methodWrapper, ResultWrapper resultWrapper) {
        try {
            logInfo(methodWrapper, resultWrapper);
        } catch (Throwable e) {
            logger.error("printLog occur error", e);
        }
    }

}

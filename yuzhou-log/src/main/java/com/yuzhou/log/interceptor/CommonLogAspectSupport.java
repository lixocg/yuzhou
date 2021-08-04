package com.yuzhou.log.interceptor;

import com.yuzhou.log.annotation.CommonLog;
import com.yuzhou.log.common.LogType;
import com.yuzhou.log.common.MethodWrapper;
import com.yuzhou.log.common.ResultWrapper;
import com.yuzhou.log.log.InnerLog;
import com.yuzhou.log.log.LogService;
import com.yuzhou.log.log.LogServiceFactory;
import com.yuzhou.log.util.EagleEyeUtil;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 日志切面基类
 *
 * @author xiongcheng.lxch
 * @see DefaultCommonLogInterceptor 默认实现
 */
public abstract class CommonLogAspectSupport implements CommonLogInterceptor {
    Logger logger = InnerLog.getLogger();

    private boolean isPrintConsume;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        CommonLog annotation = AnnotationUtils.findAnnotation(invocation.getMethod(), CommonLog.class);
        //没有标注CommonLog和压测流量 不做增强日志打印
        if (annotation == null || EagleEyeUtil.isPressureTestRequest()) {
            return invocation.proceed();
        }
        long start = System.currentTimeMillis();
        try {
            Object proceed = invocation.proceed();
            printLog(new MethodWrapper(invocation, System.currentTimeMillis() - start), new ResultWrapper(proceed, null));
            return proceed;
        } catch (Throwable e) {
            printLog(new MethodWrapper(invocation, System.currentTimeMillis() - start), new ResultWrapper(null, e));
            throw e;
        }
    }

    protected void logInfo(MethodWrapper methodWrapper, ResultWrapper resultWrapper) {
        long start = System.currentTimeMillis();
        CommonLog annotation = AnnotationUtils.findAnnotation(methodWrapper.getMethodInvocation().getMethod(), CommonLog.class);
        if (annotation == null) {
            return;
        }
        LogType logType = annotation.type();
        if (logType == null) {
            return;
        }

        LogService logService = LogServiceFactory.getLogService(logType);
        if (logService != null) {
            logService.logInfo(methodWrapper, resultWrapper);
        }
        if (isPrintConsume) {
            logger.info("日志打印耗时|method={},traceId={},consume={} ms",
                    methodWrapper.getMethodInvocation().getMethod().getName(),
                    methodWrapper.getTraceId(), (System.currentTimeMillis() - start));
        }
    }


    public boolean isPrintConsume() {
        return isPrintConsume;
    }

    @Override
    public void setPrintConsume(boolean printConsume) {
        isPrintConsume = printConsume;
    }
}

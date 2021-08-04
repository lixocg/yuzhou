package com.yuzhou.log.concurrent;

import com.yuzhou.log.interceptor.AsyncCommonLogInterceptor;
import com.yuzhou.log.log.InnerLog;
import org.slf4j.Logger;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class DefaultRejectExecuteHandler implements RejectedExecutionHandler {
    Logger logger = InnerLog.getLogger();

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (r instanceof AsyncCommonLogInterceptor.PrintLogTask) {
            AsyncCommonLogInterceptor.PrintLogTask printLogTask = (AsyncCommonLogInterceptor.PrintLogTask) r;
            String traceId = printLogTask.traceId();
            logger.warn("common-log线程池已满，日志被抛弃，traceId={}", traceId);
        }
    }
}

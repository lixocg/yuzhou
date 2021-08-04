package com.yuzhou.log.interceptor;

import com.yuzhou.log.common.MethodWrapper;
import com.yuzhou.log.common.ResultWrapper;
import com.yuzhou.log.log.InnerLog;
import org.slf4j.Logger;

import java.util.concurrent.Executor;

/**
 * 异步AOP方法拦截日志增强，同步方式参考 {@link DefaultCommonLogInterceptor}
 *
 * @author xiongcheng.lxch
 * @see CommonLogAspectSupport
 */
public class AsyncCommonLogInterceptor extends CommonLogAspectSupport {

    private Executor executor;

    public AsyncCommonLogInterceptor(Executor executor) {
        this.executor = executor;
    }


    @Override
    public void printLog(MethodWrapper methodWrapper, ResultWrapper resultWrapper) {
        executor.execute(new PrintLogTask(methodWrapper,resultWrapper,this));
    }

    public static class PrintLogTask implements Runnable {
        private Logger logger = InnerLog.getLogger();

        private ResultWrapper resultWrapper;
        private MethodWrapper methodWrapper;
        private AsyncCommonLogInterceptor logInterceptor;



        public PrintLogTask(MethodWrapper methodWrapper,
                            ResultWrapper resultWrapper,
                            AsyncCommonLogInterceptor logInterceptor) {
            this.resultWrapper = resultWrapper;
            this.methodWrapper = methodWrapper;
            this.logInterceptor = logInterceptor;
        }


        @Override
        public void run() {
            try {
                logInterceptor.logInfo(methodWrapper, resultWrapper);
            } catch (Throwable e) {
                logger.error("printLog occur error", e);
            }
        }

        public String traceId(){
            if(this.methodWrapper != null){
                return methodWrapper.getTraceId();
            }
            return null;
        }
    }

}

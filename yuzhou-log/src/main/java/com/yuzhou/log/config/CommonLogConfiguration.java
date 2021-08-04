package com.yuzhou.log.config;

import com.yuzhou.log.annotation.EnableCommonLog;
import com.yuzhou.log.concurrent.DefaultLogThreadFactory;
import com.yuzhou.log.interceptor.AsyncCommonLogInterceptor;
import com.yuzhou.log.interceptor.CommonLogInterceptor;
import com.yuzhou.log.interceptor.DefaultCommonLogInterceptor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 开启注解驱动日志管理需要注册的spring基础设施组件配置{@code @Configuration}
 *
 * @author xiongcheng.lxch
 * @see EnableCommonLog
 * @see CommonLogConfigurationSelector
 */
@Configuration
public class CommonLogConfiguration extends ProxyLogConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Conditional(BeanTypeNotPresentCondition.class)
    public CommonLogInterceptor interceptor() throws Exception {
        AnnotationAttributes enableCommonLog = this.enableCommonLog;
        if(enableCommonLog == null){
            throw new RuntimeException("初始化异常");
        }
        CommonLogInterceptor interceptor;

        boolean async = enableCommonLog.getBoolean("async");
        int corePoolSize = enableCommonLog.getNumber("corePoolSize");
        int maxPoolSize = enableCommonLog.getNumber("maxPoolSize");
        int queueSize = enableCommonLog.getNumber("queueSize");
        Class<?> rejectHandler = enableCommonLog.getClass("rejectHandler");
        if(async){
            Executor executor = new ThreadPoolExecutor(corePoolSize,
                    maxPoolSize,5, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(queueSize),
                    new DefaultLogThreadFactory(),
                    (RejectedExecutionHandler) rejectHandler.newInstance());
            interceptor = new AsyncCommonLogInterceptor(executor);
        }else {
            interceptor = new DefaultCommonLogInterceptor();
        }
        interceptor.setPrintConsume(enableCommonLog.getBoolean("isPrintConsume"));
        return interceptor;
    }

    @Override
    public Class getAnnotationClass() {
        return EnableCommonLog.class;
    }
}

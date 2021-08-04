package com.yuzhou.log.config;

import com.yuzhou.log.advisor.CommonLogAdvisor;
import com.yuzhou.log.interceptor.CommonLogInterceptor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
public abstract class ProxyLogConfiguration implements ImportAware {
    protected AnnotationAttributes enableCommonLog;


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CommonLogAdvisor advisor(CommonLogInterceptor interceptor) {
        CommonLogAdvisor commonLogAdvisor = new CommonLogAdvisor();
        commonLogAdvisor.setAdvice(interceptor);
        return commonLogAdvisor;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableCommonLog =
                AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(getAnnotationClass().getName(), false));
        if (this.enableCommonLog == null) {
            throw new IllegalArgumentException(
                    "@EnableCommonLog is not present on importing class " + importMetadata.getClassName());
        }
    }

    abstract Class getAnnotationClass();
}

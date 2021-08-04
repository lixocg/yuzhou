package com.yuzhou.log.config;

import com.yuzhou.log.log.InnerLog;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/**
 * spring初始化时，判断一个类定义是否需要被加载，判断条件：是否已存在同一种类型的bean定义
 *
 */
public class BeanTypeNotPresentCondition implements ConfigurationCondition {
    Logger logger = InnerLog.getLogger();


    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (metadata instanceof MethodMetadata && metadata.isAnnotated(Bean.class.getName())) {
            final MethodMetadata methodMetadata = (MethodMetadata)metadata;
            try {
                String beanTypeName = methodMetadata.getReturnTypeName();
                Class beanType = ClassUtils.forName(beanTypeName, context.getClassLoader());
                ListableBeanFactory factory = context.getBeanFactory();

                String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(factory, beanType, false, false);
                if (ObjectUtils.isEmpty(names)) {
                    logger.debug("No bean of type [" + beanType + "]. Conditional configuration applies.");
                    return true;
                } else {
                    logger.debug("Found bean of type [" + beanType + "]. Conditional configuration does not apply.");
                    return false;
                }
            } catch (ClassNotFoundException e) {
                logger.error("BeanTypeNotPresentCondition error", e);
            }

        }
        return false;
    }

}
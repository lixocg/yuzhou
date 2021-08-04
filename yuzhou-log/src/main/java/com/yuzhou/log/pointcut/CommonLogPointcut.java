package com.yuzhou.log.pointcut;

import com.yuzhou.log.annotation.CommonLog;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 增强点匹配
 *
 * @author xiongcheng.lxch
 * @see com.yuzhou.log.advisor.CommonLogAdvisor
 */
public class CommonLogPointcut extends StaticMethodMatcherPointcut implements Serializable {
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        // 目标类可能是被CGLIB代理后的子类，需要获取原class
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        // 如果method是接口方法，取实现类的method
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        // 处理带泛型参数的方法，取原方法
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        CommonLog annotation = specificMethod.getAnnotation(CommonLog.class);
        return annotation != null;
    }
}

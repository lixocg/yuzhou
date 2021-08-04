package com.yuzhou.log.advisor;

import com.yuzhou.log.pointcut.CommonLogPointcut;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * 日志增强器
 *
 * @author xiongcheng.lxch
 */
public class CommonLogAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    private final CommonLogPointcut pointcut = new CommonLogPointcut();

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }
}

package com.yuzhou.demo.bytebuddy.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class DemoAgent {

    /**
     * 在主线程启动之前进行处理
     *
     * @param agentArgs       代理请求参数
     * @param instrumentation 插桩
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        handleInstrument(instrumentation);
    }

    /**
     * 进行插桩处理
     *
     * @param instrumentation 待处理桩
     */
    private static void handleInstrument(Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .type(ElementMatchers.nameEndsWith("App"))
                .transform((builder, type, classLoader, module, protectionDomain) -> builder.method(ElementMatchers.any()).intercept(MethodDelegation.to(TimeInterceptor.class)))
                .installOn(instrumentation);
    }

}
package com.yuzhou.demo.bytebuddy.secured;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class SecurityAgent {

//    public static void premain(String arg, Instrumentation inst) {
//        new AgentBuilder.Default()
//                .type(ElementMatchers.any())
//                .transform((builder, type) -> builder
//                        .method(ElementMatchers.isAnnotatedWith(Secured.class)
//                                .intercept(MethodDelegation.to(SecurityInterceptor.class)
//                                        .andThen(SuperMethodCall.INSTANCE))))
//                .installOn(inst);
//    }
}

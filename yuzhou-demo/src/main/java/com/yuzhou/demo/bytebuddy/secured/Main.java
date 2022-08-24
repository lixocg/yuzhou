package com.yuzhou.demo.bytebuddy.secured;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

public class Main {

    public static void main(String[] args) throws Exception {
        new ByteBuddy()
                .subclass(Service.class)
                .method(ElementMatchers.isAnnotatedWith(Secured.class))
                .intercept(MethodDelegation.to(SecurityInterceptor.class).andThen(SuperMethodCall.INSTANCE))
                .make()
                .load(Main.class.getClassLoader(),
                        ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded()
                .newInstance()
                .doAction();
    }

}

package com.yuzhou.demo.bytebuddy.secured;

import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;

public class SecurityInterceptor {

    public static String user = "ANONYMOUS";

    public static void intercept(@Origin Method method) {
        if (!method.getAnnotation(Secured.class).user().equals(user)) {
            throw new IllegalStateException("Wrong user");
        }
    }
}

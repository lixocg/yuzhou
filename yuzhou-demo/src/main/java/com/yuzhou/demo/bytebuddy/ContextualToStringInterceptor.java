package com.yuzhou.demo.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;

public class ContextualToStringInterceptor {
    public static String intercept(@Origin Method m) {
        return "Hello World from " + m.getName() + "!";
    }
}

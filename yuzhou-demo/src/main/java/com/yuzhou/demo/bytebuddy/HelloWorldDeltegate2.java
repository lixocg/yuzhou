package com.yuzhou.demo.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class HelloWorldDeltegate2 {

    public static void main(String[] args) throws Exception{
        Class<?> dynamicType = new ByteBuddy()
                // Object子类
                .subclass(Object.class)
                //重写toString()
                .method(ElementMatchers.named("toString"))
                // 最佳匹配
                .intercept(MethodDelegation.to(ContextualToStringInterceptor.class))
                //生成类
                .make()
                .load(HelloWorldDeltegate2.class.getClassLoader())
                .getLoaded();

        Object instance = dynamicType.newInstance();
        String toString = instance.toString();
        System.out.println(toString);
        System.out.println(instance.getClass().getCanonicalName());
    }

}

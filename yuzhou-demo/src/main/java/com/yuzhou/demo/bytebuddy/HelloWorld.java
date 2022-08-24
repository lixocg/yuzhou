package com.yuzhou.demo.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

public class HelloWorld {

    public static void main(String[] args) throws Exception{
        Class<?> dynamicType = new ByteBuddy()
                // Object子类
                .subclass(Object.class)
                //重写toString()
                .method(ElementMatchers.named("toString"))
                // 拦截到指令提供的实现
                .intercept(FixedValue.value("Hello World"))
                //生成类
                .make()
                .load(HelloWorld.class.getClassLoader())
                .getLoaded();

        Object instance = dynamicType.newInstance();
        String toString = instance.toString();
        System.out.println(toString);
        System.out.println(instance.getClass().getCanonicalName());
    }

}

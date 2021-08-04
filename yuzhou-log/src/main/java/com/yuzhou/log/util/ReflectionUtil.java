package com.yuzhou.log.util;

import com.yuzhou.log.log.InnerLog;
import org.slf4j.Logger;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionUtil {
    static Logger logger = InnerLog.getLogger();

    private final static Map<Class, Field> fieldMapCache = new ConcurrentHashMap<>();

    public static Object getField(String fieldName, Object target) {
        try {
            Field field = ReflectionUtils.findField(target.getClass(), fieldName);
            if (field == null) {
                return null;
            }
            ReflectionUtils.makeAccessible(field);
            return ReflectionUtils.getField(field, target);
        } catch (Throwable e) {
            logger.error("反射getField异常，field={},class={}", fieldName, target.getClass(), e);
            return null;
        }
    }

    public static Object getField(Field field, Object target) {
        try {
            ReflectionUtils.makeAccessible(field);
            return ReflectionUtils.getField(field, target);
        } catch (Throwable e) {
            logger.error("反射getField异常，field={},class={}", field, target.getClass(), e);
            return null;
        }
    }

    public static Field findField(String fieldName, Class targetClass) {
        try {
            Field field = ReflectionUtils.findField(targetClass, fieldName);
            return field;
        } catch (Throwable e) {
            return null;
        }
    }

    public static Object tryField(String fields, Object result) {
        Object fieldVal = null;
        String[] fieldsArr = fields.split(",");
        if (fieldsArr.length > 1) {
            //尝试解析返回对象数据字段
            for (int i = 0; i < fieldsArr.length; i++) {
                Field field = ReflectionUtil.findField(fieldsArr[i], result.getClass());
                if (field != null) {
                    fieldVal = ReflectionUtil.getField(field, result);
                    break;
                }
            }
        } else {
            fieldVal = ReflectionUtil.getField(fields, result);
        }
        return fieldVal;
    }
}

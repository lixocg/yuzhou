package com.yuzhou.log.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * spring ObjectUtils 4.2才开始，兼容低版本spring
 */
public class ObjectUtil {
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // else
        return false;
    }
}

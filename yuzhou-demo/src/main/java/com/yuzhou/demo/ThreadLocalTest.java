package com.yuzhou.demo;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @description:
 * @author: lixiongcheng
 * @date: 2022/3/30 下午4:57
 */
public class ThreadLocalTest {


    public static String rs(int i){
        Random random = new Random(i);
        StringBuilder sb = new StringBuilder();
        while (true){
            int k = random.nextInt(27);
            if(k == 0){
                break;
            }
            sb.append((char)('`'+k));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(rs(-229985453));
    }

    public <T> T get(Supplier<T> t,Map<String,T> map,String key){
        T t1 = map.get(key);
        if(t1 == null){
            T t2 = t.get();
            if(t2 != null){
                map.put(key,t2);
                return t2;
            }
        }
        return t1;
    }

}

class Cache{
    ThreadLocal<Map<String,String>> cache = ThreadLocal.withInitial(Maps::newHashMap);


    public String get(String p){
        String val = cache.get().get(p);
        if(val == null){
            System.out.println("设置缓存...");
            val = p + "--123--"+Thread.currentThread().getName();
            cache.get().put(p,val);
            return val;
        }
        return val;
    }

    public void remove(){
        cache.remove();
    }
}

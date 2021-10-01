package com.yuzhou.rmq.common;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-29
 * Time: 上午11:29
 */
public class StreamIDEntry<K, V> implements Map.Entry<K, V>, Serializable {

    private static final long serialVersionUID = 1886695724803658391L;

    K key;

    V val;

    public StreamIDEntry(K key, V val) {
        this.key = key;
        this.val = val;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.val;
    }

    @Override
    public V setValue(V value) {
        V ori = this.val;
        this.val = value;
        return ori;
    }

}

package com.yuzhou;

import com.yuzhou.rmq.utils.SerializeUtils;
import redis.clients.jedis.StreamEntryID;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-29
 * Time: 上午10:58
 */
public class SerializeTest {

    public static void main(String[] args) {
        StreamEntryID entryID = new StreamEntryID("11-111");
        byte[] data = SerializeUtils.serialize(entryID);

        StreamEntryID streamEntryID = SerializeUtils.deserialize(data, StreamEntryID.class);
        System.out.println(streamEntryID);


        TestEntry<byte[],byte[]> streamEntry = new TestEntry<>();
        streamEntry.k = SerializeUtils.serialize("ss1");
        streamEntry.v = SerializeUtils.serialize(entryID);

        System.out.println(SerializeUtils.deserialize(streamEntry.k,String.class));
        System.out.println(SerializeUtils.deserialize(streamEntry.v,StreamEntryID.class));
    }


    static class TestEntry<K,V> implements Map.Entry<K,V>, Serializable {

        private static final long serialVersionUID = 1886695724803658391L;

        K k;
        V v;

        TestEntry(){}

        TestEntry(K k,V v){
            this.k = k;
            this.v = v;
        }

        @Override
        public K getKey() {
            return this.k;
        }

        @Override
        public V getValue() {
            return this.v;
        }

        @Override
        public V setValue(V value) {
            this.v = value;
            return this.v;
        }
    }

}

package com.yuzhou.algorithm.greedy;

import java.util.*;

/**
 * 假设存在如下表的需要付费的广播台，以及广播台信号可以覆盖的地区。
 * <p>
 * 广播台     覆盖地区
 * K1     北京,上海,天津
 * K2     广州,北京,深圳
 * K3     成都,上海,杭州
 * K4     上海,天津
 * K5     杭州,大连
 * <p>
 * 如何选择最少的广播台，让所有的地区都可以接收到信号
 */
public class BroadcastCover {
    public static void main(String[] args) {
        System.out.println(broadcasts());
    }

    public static List<String> broadcasts() {
        String[] areas = {"北京", "上海", "天津", "广州", "深圳", "成都", "杭州", "大连"};
        Set<String> allAreas = new HashSet<>(Arrays.asList(areas));

        Map<String, Set<String>> broadcasts = new HashMap<>();
        broadcasts.put("K1", new HashSet<>(Arrays.asList("北京", "上海", "天津")));
        broadcasts.put("K2", new HashSet<>(Arrays.asList("广州", "北京", "深圳")));
        broadcasts.put("K3", new HashSet<>(Arrays.asList("成都", "上海", "杭州")));
        broadcasts.put("K4", new HashSet<>(Arrays.asList("上海", "天津")));
        broadcasts.put("K5", new HashSet<>(Arrays.asList("杭州", "大连")));

        List<String> selects = new ArrayList<>();

        while (!allAreas.isEmpty()) {

            String maxKey = null;
            //辅助求交集
            Set<String> tmpsBroadcast = new HashSet<>();

            for (String key : broadcasts.keySet()) {
                tmpsBroadcast.clear();

                Set<String> curBroadcast = broadcasts.get(key);

                tmpsBroadcast.addAll(curBroadcast);

                tmpsBroadcast.retainAll(allAreas);

                //找到覆盖最多地区的广播站
                if (tmpsBroadcast.size() > 0 &&
                        (maxKey == null || tmpsBroadcast.size() > broadcasts.get(maxKey).size())) {
                    maxKey = key;
                }
            }

            if (maxKey != null) {
                selects.add(maxKey);
                allAreas.removeAll(broadcasts.get(maxKey));
            }
        }

        return selects;
    }
}

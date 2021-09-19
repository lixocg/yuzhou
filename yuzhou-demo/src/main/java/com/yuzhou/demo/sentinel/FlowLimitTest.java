package com.yuzhou.demo.sentinel;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-09-16
 * Time: 上午10:44
 */
public class FlowLimitTest {

    /**
     * 语速排队
     */
    public static void main(String[] arg) {
        loadRules();
        // 使用并行流模仿并发
        IntStream stream = IntStream.range(0, 100000);
//        Stopwatch started1 = Stopwatch.createStarted();
        System.out.println("main-thread start:");
        stream.parallel().forEach(value -> {
            try {
//                Stopwatch stopwatch = Stopwatch.createStarted();
                SphU.entry("test", EntryType.IN);
                System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
//                System.out.println("do something,请求" + value + ",时间:" + stopwatch.stop().toString());
            } catch (BlockException e) {
                System.out.println("block exp");
            }
        });
        System.out.println("main-thread end:");
    }

    /**
     * 加载规则
     */
    private static void loadRules() {
        FlowRule flowRule = new FlowRule();
        flowRule.setResource("test");
        flowRule.setCount(4);
        flowRule.setGrade(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
//        flowRule.setMaxQueueingTimeMs(10000);
        FlowRuleManager.loadRules(Lists.newArrayList(flowRule));
    }

}

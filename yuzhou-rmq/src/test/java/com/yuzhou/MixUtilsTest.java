package com.yuzhou;

import com.yuzhou.rmq.utils.MixUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: lixiongcheng
 * Date: 2021-10-01
 * Time: 下午1:41
 */
public class MixUtilsTest {

    @Test
    public void test(){
        List<String> list = Arrays.asList("1","2","3","4","5");
        List<List<String>> lists = MixUtil.averageAssign(list, 7);
        System.out.println(lists);
    }

}

package com.yuzhou;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.omg.CORBA.INTERNAL;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }


    public List<Integer> merge(List<Integer> l1, List<Integer> l2) {
        //边界
        if (l1 == null && l2 == null) {
            return null;
        }
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }

        int fast = 0;
        int slow = 0;

        List<Integer> res = new ArrayList<>();
        while (fast < l1.size() || slow < l2.size()) {
            int c1 = l1.get(fast);
            int c2 = l2.get(slow);

            if (fast == l1.size() - 1) {
                res.addAll(l2.subList(slow, l2.size()));
                return res;
            }

            if (c1 < c2) {
                res.add(c1);
                fast++;
            } else {
                res.add(c2);
                slow++;
            }
        }
        return res;
    }
}

package com.yuzhou.basic.juc.a03_stream;

import java.util.Arrays;
import java.util.List;

/**
 * 找出满足以下条件的数据：
 * 1.id为偶数
 * 2.年龄大于22
 * 3.用户名转成大学
 * 4.用户名倒序
 * 5.取一条
 */
public class StreamDemo01 {
    public static void main(String[] args) {
        User u1 = new User(1, 20, "a");
        User u2 = new User(2, 21, "b");
        User u3 = new User(3, 22, "c");
        User u4 = new User(4, 23, "d");
        User u5 = new User(5, 24, "e");
        User u6 = new User(6, 25, "f");

        List<User> list = Arrays.asList(u1, u2, u3, u4, u5, u6);

        list.stream().filter(u -> u.getId() % 2 == 0)
                .filter(u -> u.getAge() > 22)
                .peek(u -> u.setName(u.getName().toUpperCase()))
                .sorted((o1, o2) -> o2.getName().compareTo(o1.getName()))
                .limit(1)
                .forEach(System.out::println);
    }
}

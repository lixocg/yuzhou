//package com.yuzhou.basic.unsafe;
//
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//
//public class UnsafeDemo {
//    public static void main(String[] args) throws Exception {
//        Unsafe unsafe = getUnsafe();
//        User u = (User) unsafe.allocateInstance(User.class);
//        System.out.println(u);
//    }
//
//    /**
//     * 获取unsafe对象方式
//     */
//    public static Unsafe getUnsafe() throws Exception {
//        Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
//        theUnsafeInstance.setAccessible(true);
//        Unsafe unsafe = (Unsafe) theUnsafeInstance.get(Unsafe.class);
//        return unsafe;
//    }
//}

package com.yuzhou.demo.threadlocal;


import com.alibaba.ttl.TransmittableThreadLocal;

public final class Namespaces {

    public static final String SHARED_NAMESPACE_NAME = "__SNN";

//    private static final ThreadLocal<String> NAMESPACE = new InheritableThreadLocal<>();
    private static final TransmittableThreadLocal<String> NAMESPACE = new TransmittableThreadLocal<>();


    /**
     * 私有的构造器.
     */
    private Namespaces() {
    }

    /**
     * 设置当前线程的命名空间.
     *
     * @param namespace 命名空间
     */
    public static void set(final String namespace) {
        NAMESPACE.set(namespace);
    }

    /**
     * 设置当前线程的命名空间.
     *
     * @param namespace 命名空间
     */
    public static void set(final long namespace) {
        NAMESPACE.set("" + namespace);
    }

    /**
     * 获取当前线程的命名空间.
     *
     * @return 命名空间
     * @throws IllegalStateException 如果没有设置命名空间（值为 {@code null}）
     */
    public static String get() throws IllegalStateException {
        final String ret = NAMESPACE.get();

        if (null == ret) {
            return Namespaces.SHARED_NAMESPACE_NAME;
        }

        return ret;
    }

    /**
     * 判断当前线程是否设置了命名空间
     *
     * @return Boolean 是否设置了命名空间
     */
    public static Boolean isSetNameSpace() {
        final String ret = NAMESPACE.get();

        if (null == ret) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * @return 如果name非空，返回 <b>Namespaces.get()+"__"+name</b>， 否则返回name本身
     */
    public static String prefix(final String name) {
        if (name == null || name.trim().length() == 0) {
            return name;
        }
        return get() + "__" + name;
    }

    /**
     * @return 如果name非空，则返回 <b>name+"__"+Namespaces.get()</b>， 否则返回name本身
     */
    public static String suffix(final String name) {
        if (name == null || name.trim().length() == 0) {
            return name;
        }
        return name + "__" + get().toLowerCase();
    }

    public static void unload() {
        NAMESPACE.remove();
    }
}

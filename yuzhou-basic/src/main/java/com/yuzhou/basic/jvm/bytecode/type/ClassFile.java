package com.yuzhou.basic.jvm.bytecode.type;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClassFile {

    /**
     * 魔数
     */
    private U4 magic;

    /**
     * 副版本号
     */
    private U2 minor_version;

    /**
     * 主版本号
     */
    private U2 major_version;

    /**
     * 常量池计数器
     */
    private U2 constant_pool_count;

    /**
     * 常量池
     */
    private CpInfo[] constant_pool;

    /**
     * 访问标志
     */
    private U2 access_flags;

    /**
     * 类索引
     */
    private U2 this_class;

    /**
     * 父类索引
     */
    private U2 super_class;

    /**
     * 接口总数
     */
    private U2 interface_count;

    /**
     * 接口数组
     */
    private U2[] inferfaces;

    /**
     * 字段总数
     */
    private U2 fields_count;

    /**
     * 字段表
     */
    private FieldInfo[] fields;

    /**
     * 方法总数
     */
    private U2 methods_count;

    /**
     * 方法表
     */
    private MethodInfo[] methods;

    /**
     * 属性总数
     */
    private U2 attributes_count;

    /**
     * 属性表
     */
    private AttributeInfo[] attributes;

}

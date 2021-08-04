package com.yuzhou.log.javaconfig;

import java.io.Serializable;
import java.util.List;

public class TestDTO implements Serializable {
    private Integer age;

    private String name;

    private List<String> tooth;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTooth() {
        return tooth;
    }

    public void setTooth(List<String> tooth) {
        this.tooth = tooth;
    }
}

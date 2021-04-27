package com.xiu.dubbo.domain;

import com.xiu.dubbo.common.BaseEntity;

import java.io.Serializable;

/**
 * @ClassName DemoEntity
 * @Desc 实体对象
 * @Author xieqx
 * @Date 2021/4/21 19:35
 **/
public class DemoEntity extends BaseEntity implements Serializable {


    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

package com.xiu.dubbo.common;


import java.io.Serializable;

/**
 * @ClassName BaseEntity
 * @Desc TODO
 * @Author xieqx
 * @Date 2021/4/21 19:37
 **/
public class BaseEntity implements Serializable{
    private static final long serialVersionUID = 2249325746951841661L;
    private transient ThreadLocal<BaseEntity> visitor = new ThreadLocal<BaseEntity>() {
        protected BaseEntity initialValue() {
            return null;
        }
    };
    private String simpleName = this.getClass().getSimpleName();

    public BaseEntity() {
    }

    public String toString() {
       return super.toString();
    }
}

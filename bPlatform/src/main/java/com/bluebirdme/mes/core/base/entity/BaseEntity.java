package com.bluebirdme.mes.core.base.entity;

import javax.persistence.*;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private Object t_1;
    @Transient
    private Object t_2;
    @Transient
    private Object t_3;

    public BaseEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getT_1() {
        return this.t_1;
    }

    public void setT_1(Object t_1) {
        this.t_1 = t_1;
    }

    public Object getT_2() {
        return this.t_2;
    }

    public void setT_2(Object t_2) {
        this.t_2 = t_2;
    }

    public Object getT_3() {
        return this.t_3;
    }

    public void setT_3(Object t_3) {
        this.t_3 = t_3;
    }
}

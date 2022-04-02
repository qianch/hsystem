package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Entity
@Table(name = "platform_role")
@Desc("角色")
public class Role extends BaseEntity {
    @Desc("角色名称")
    @Column(name = "name", nullable = false)
    public String name;

    public Role() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

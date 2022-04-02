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
@Table(name = "platform_permission")
@Desc("权限")
public class Permission extends BaseEntity {
    @Desc("角色ID")
    @Column(name = "rid")
    public Long rid;

    @Desc("菜单ID")
    @Column(name = "mid")
    public Long mid;

    public Permission() {
    }

    public Long getRid() {
        return this.rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Long getMid() {
        return this.mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }
}

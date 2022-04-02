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
@Table(name = "platform_user_role")
@Desc("人员-角色")
public class UserRole extends BaseEntity {
    @Desc("用户ID")
    @Column(nullable = false)
    public Long userId;

    @Desc("角色ID")
    @Column(nullable = false)
    public Long roleId;

    public UserRole() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}

package com.bluebirdme.mes.audit.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 审核人员设置
 *
 * @author Goofy
 * @Date 2016年10月24日 下午2:40:07
 */
@Desc("流程人员设置")
@Entity
@Table(name = "HS_Audit_USERS")
public class AuditUsers extends BaseEntity {

    /**
     * 1,2
     */
    @Desc("等级")
    @Column(nullable = false)
    private Integer auditLevel;

    @Desc("流程代码")
    @Column(nullable = false)
    private String auditCode;

    @Desc("用户ID")
    @Column(nullable = false)
    private Long userId;

    public Integer getAuditLevel() {
        return auditLevel;
    }

    public void setAuditLevel(Integer auditLevel) {
        this.auditLevel = auditLevel;
    }

    public String getAuditCode() {
        return auditCode;
    }

    public void setAuditCode(String auditCode) {
        this.auditCode = auditCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

package com.bluebirdme.mes.audit.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.core.dev.DevHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 流程设置
 *
 * @author Goofy
 * @Date 2016年10月11日 下午4:17:21
 */
@Desc("流程设置")
@Entity
@Table(name = "HS_Audit_Process_Setting")
public class AuditProcessSetting extends BaseEntity {
    @Desc("流程代码")
    @Column(nullable = false)
    private String auditCode;

    @Desc("流程名称")
    @Column(nullable = false)
    private String auditName;

    @Desc("流程等级")
    @Column(nullable = false)
    private Integer auditLevel;

    @Desc("一级审核人员")
    @Column(nullable = true)
    private String auditFirstLevelUsers;

    @Desc("二级审核人员")
    @Column(nullable = true)
    private String auditSecondLevelUsers;


    @Transient
    private Long[] firstLevelUsers;

    @Transient
    private Long[] secondLevelUsers;

    public String getAuditCode() {
        return auditCode;
    }

    public void setAuditCode(String auditCode) {
        this.auditCode = auditCode;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public Integer getAuditLevel() {
        return auditLevel;
    }

    public void setAuditLevel(Integer auditLevel) {
        this.auditLevel = auditLevel;
    }

    public String getAuditFirstLevelUsers() {
        return auditFirstLevelUsers;
    }

    public void setAuditFirstLevelUsers(String auditFirstLevelUsers) {
        this.auditFirstLevelUsers = auditFirstLevelUsers;
    }

    public String getAuditSecondLevelUsers() {
        return auditSecondLevelUsers;
    }

    public void setAuditSecondLevelUsers(String auditSecondLevelUsers) {
        this.auditSecondLevelUsers = auditSecondLevelUsers;
    }


    public Long[] getFirstLevelUsers() {
        return firstLevelUsers;
    }

    public void setFirstLevelUsers(Long[] firstLevelUsers) {
        this.firstLevelUsers = firstLevelUsers;
    }

    public Long[] getSecondLevelUsers() {
        return secondLevelUsers;
    }

    public void setSecondLevelUsers(Long[] secondLevelUsers) {
        this.secondLevelUsers = secondLevelUsers;
    }

    public static void main(String[] args) throws Exception {
        DevHelper.genAll(AuditProcessSetting.class, "高飞");
    }
}

package com.bluebirdme.mes.audit.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 审核步骤
 *
 * @author Goofy
 * @Date 2016年10月25日 下午1:40:48
 */
@Desc("流程步骤")
@Entity
@Table(name = "hs_audit_steps")
public class AuditSteps extends BaseEntity {
    @Desc("流程实例ID")
    @Column(nullable = false)
    private Long auditProcessInstanceId;
    @Desc("审核时间")
    @Column(nullable = false)
    private Date auditTime;
    @Desc("审核人员")
    @Column(nullable = false)
    private Long auditUserId;
    @Desc("审核结果")
    @Column(nullable = false)
    private Integer auditResult;
    @Desc("审核消息")
    @Column(length = 2500)
    private String auditMsg;

    public Long getAuditProcessInstanceId() {
        return auditProcessInstanceId;
    }

    public void setAuditProcessInstanceId(Long auditProcessInstanceId) {
        this.auditProcessInstanceId = auditProcessInstanceId;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Long getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(Long auditUserId) {
        this.auditUserId = auditUserId;
    }

    public Integer getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(Integer auditResult) {
        this.auditResult = auditResult;
    }

    public String getAuditMsg() {
        return auditMsg;
    }

    public void setAuditMsg(String auditMsg) {
        this.auditMsg = auditMsg;
    }

}

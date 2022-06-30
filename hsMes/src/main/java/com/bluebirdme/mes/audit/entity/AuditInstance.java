package com.bluebirdme.mes.audit.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 流程实例
 *
 * @author Goofy
 * @Date 2016年10月25日 下午1:16:46
 */
@Desc("流程实例")
@Entity
@Table(name = "hs_audit_instance")
public class AuditInstance extends BaseEntity {

    @Desc("流程标题")
    @Column(nullable = false)
    private String auditTitle;

    @Desc("流程代码")
    @Column(nullable = false)
    private String auditCode;

    @Desc("Java实体类名")
    @Column(nullable = false)
    private String entityJavaClass;

    @Desc("提审人员")
    @Column(nullable = false)
    private Long createUserId;

    @Desc("创建时间")
    @Column(nullable = false)
    private Date createTime;

    @Desc("一级实际审核人员")
    @Column()
    private Long firstRealAuditUserId;

    @Desc("二级实际审核人员")
    @Column()
    private Long secondRealAuditUserId;

    @Desc("一级审核时间")
    @Column()
    private Date firstAuditTime;

    @Desc("二级审核时间")
    @Column()
    private Date secondAuditTime;

    @Desc("一级审核结果")
    @Column()
    private Integer firstAuditResult;

    @Desc("一级审核消息")
    @Column(length = 2500)
    private String firstAuditMsg;

    @Desc("二级审核结果")
    @Column()
    private Integer secondAuditResult;

    @Desc("二级审核消息")
    @Column(length = 2500)
    private String secondAuditMsg;

    @Desc("当前节点")
    // 1,2级
    @Column(nullable = false)
    private Integer currentAuditProcessNode;

    /**
     * 1：已结束，0|null：未结束
     */
    @Desc("流程是否结束")
    @Column()
    private Integer isCompleted;

    @Desc("最终结果")
    @Column
    private Integer finalResult;

    @Desc("表单地址")
    @Column(nullable = false)
    private String formUrl;

    @Desc("表单ID")
    @Column(nullable = false)
    private Long formId;


    public String getAuditTitle() {
        return auditTitle;
    }

    public void setAuditTitle(String auditTitle) {
        this.auditTitle = auditTitle;
    }

    public String getAuditCode() {
        return auditCode;
    }

    public void setAuditCode(String auditCode) {
        this.auditCode = auditCode;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getFirstRealAuditUserId() {
        return firstRealAuditUserId;
    }

    public void setFirstRealAuditUserId(Long firstRealAuditUserId) {
        this.firstRealAuditUserId = firstRealAuditUserId;
    }

    public Long getSecondRealAuditUserId() {
        return secondRealAuditUserId;
    }

    public void setSecondRealAuditUserId(Long secondRealAuditUserId) {
        this.secondRealAuditUserId = secondRealAuditUserId;
    }

    public Date getFirstAuditTime() {
        return firstAuditTime;
    }

    public void setFirstAuditTime(Date firstAuditTime) {
        this.firstAuditTime = firstAuditTime;
    }

    public Date getSecondAuditTime() {
        return secondAuditTime;
    }

    public void setSecondAuditTime(Date secondAuditTime) {
        this.secondAuditTime = secondAuditTime;
    }

    public Integer getFirstAuditResult() {
        return firstAuditResult;
    }

    public void setFirstAuditResult(Integer firstAuditResult) {
        this.firstAuditResult = firstAuditResult;
    }

    public String getFirstAuditMsg() {
        return firstAuditMsg;
    }

    public void setFirstAuditMsg(String firstAuditMsg) {
        this.firstAuditMsg = firstAuditMsg;
    }


    public Integer getSecondAuditResult() {
        return secondAuditResult;
    }

    public void setSecondAuditResult(Integer secondAuditResult) {
        this.secondAuditResult = secondAuditResult;
    }

    public String getSecondAuditMsg() {
        return secondAuditMsg;
    }

    public void setSecondAuditMsg(String secondAuditMsg) {
        this.secondAuditMsg = secondAuditMsg;
    }

    public Integer getCurrentAuditProcessNode() {
        return currentAuditProcessNode;
    }

    public void setCurrentAuditProcessNode(Integer currentAuditProcessNode) {
        this.currentAuditProcessNode = currentAuditProcessNode;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Integer isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Integer getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(Integer finalResult) {
        this.finalResult = finalResult;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getEntityJavaClass() {
        return entityJavaClass;
    }

    public void setEntityJavaClass(String entityJavaClass) {
        this.entityJavaClass = entityJavaClass;
    }


}

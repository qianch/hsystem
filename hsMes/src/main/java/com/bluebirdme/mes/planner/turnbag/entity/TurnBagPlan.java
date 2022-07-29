package com.bluebirdme.mes.planner.turnbag.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 翻包计划
 *
 * @author 高飞
 * @Date 2016年10月20日 上午13:02:34
 */
@Desc("翻包计划")
@Entity
@Table(name = "HS_TurnBag_Plan")
public class TurnBagPlan extends BaseEntity {
    @Desc("翻包任务单号")
    @Column(nullable = false)
    @Index(name = "trunBagCode")
    private String trunBagCode;

    @Index(name = "newSalesOrderDetailsId")
    @Desc("新订单")
    @Column(nullable = false)
    private Long newSalesOrderDetailsId;

    @Index(name = "newSalesOrderDetailsId")
    @Desc("新订单号")
    @Column(nullable = false)
    private String newSalesOrderCode;

    @Index(name = "newBatchCode")
    @Desc("新批次号")
    @Column(nullable = false)
    private String newBatchCode;

    @Desc("新客户")
    @Column(nullable = false)
    private String newConsumer;

    @Desc("新产品型号")
    @Column(nullable = false)
    private String newProductModel;

    @Index(name = "createTime")
    @Desc("下单时间")
    @Column(nullable = false)
    private String createTime;

    @Index(name = "finishTime")
    @Desc("完成截止时间")
    @Column(nullable = true)
    private String finishTime;

    @Desc("翻包订单交期")
    @Column(nullable = false)
    private Date deliveryDate;

    @Index(name = "userName")
    @Desc("下单人")
    @Column(nullable = false)
    private String userName;

    @Index(name = "departmentName")
    @Desc("翻包执行部门")
    @Column(nullable = false)
    private String departmentName;

    @Index(name = "memo")
    @Desc("说明")
    @Column(nullable = true)
    private String memo;

    @Desc("审核状态")
    @Column(nullable = false)
    private Integer auditState;

    @Desc("翻包数量")
    @Column
    private Double trunBagCount;

    /**
     * 0:未完成，1：已完成
     */
    @Desc("完成状态")
    @Column(nullable = false)
    private Integer isCompleted;

    @Transient
    private List<TurnBagPlanDetails> details;

    public String getTrunBagCode() {
        return trunBagCode;
    }

    public void setTrunBagCode(String trunBagCode) {
        this.trunBagCode = trunBagCode;
    }

    public Long getNewSalesOrderDetailsId() {
        return newSalesOrderDetailsId;
    }

    public void setNewSalesOrderDetailsId(Long newSalesOrderDetailsId) {
        this.newSalesOrderDetailsId = newSalesOrderDetailsId;
    }

    public String getNewBatchCode() {
        return newBatchCode;
    }

    public void setNewBatchCode(String newBatchCode) {
        this.newBatchCode = newBatchCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Integer isCompleted) {
        this.isCompleted = isCompleted;
    }

    public List<TurnBagPlanDetails> getDetails() {
        return details;
    }

    public void setDetails(List<TurnBagPlanDetails> details) {
        this.details = details;
    }

    public Double getTrunBagCount() {
        return trunBagCount;
    }

    public void setTrunBagCount(Double trunBagCount) {
        this.trunBagCount = trunBagCount;
    }

    public String getNewSalesOrderCode() {
        return newSalesOrderCode;
    }

    public void setNewSalesOrderCode(String newSalesOrderCode) {
        this.newSalesOrderCode = newSalesOrderCode;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getNewConsumer() {
        return newConsumer;
    }

    public void setNewConsumer(String newConsumer) {
        this.newConsumer = newConsumer;
    }

    public String getNewProductModel() {
        return newProductModel;
    }

    public void setNewProductModel(String newProductModel) {
        this.newProductModel = newProductModel;
    }
}

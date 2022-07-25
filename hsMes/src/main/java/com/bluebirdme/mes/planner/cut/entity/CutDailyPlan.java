package com.bluebirdme.mes.planner.cut.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("裁剪日计划")
@Entity
@Table(name = "hs_cut_Daily_Plan")
public class CutDailyPlan extends BaseEntity {
    @Desc("操作人")
    @Column(nullable = false)
    private Long operator;

    @Desc("日计划计划时间")
    @Column(nullable = false)
    @Index(name = "planDate")
    private String planDate;

    @Desc("审核状态")
    @Column
    @Index(name = "auditState")
    private Integer auditState;

    @Desc("车间")
    @Column(nullable = false)
    @Index(name = "workShop")
    private String workShop;
    @Desc("是否关闭")
    @Column
    @Index(name = "isClosed")
    private Integer isClosed;


    @Desc("车间代码")
    @Column(nullable = false)
    @Index(name = "workShopCode")
    private String workShopCode;

    public String getWorkShopCode() {
        return workShopCode;
    }

    public void setWorkShopCode(String workShopCode) {
        this.workShopCode = workShopCode;
    }

    public Integer getIsClosed() {
        if (isClosed == null) {
            isClosed = 0;
        }
        return isClosed;
    }

    public void setIsClosed(Integer isClosed) {
        this.isClosed = isClosed;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditstate) {
        this.auditState = auditstate;
    }

    public String getWorkShop() {
        return workShop;
    }

    public void setWorkShop(String workShop) {
        this.workShop = workShop;
    }


}

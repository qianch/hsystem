package com.bluebirdme.mes.planner.produce.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 计划明细部件数量
 *
 * @author Goofy
 * @Date 2017年3月3日 下午3:18:26
 */
@Entity
@Table(name = "Hs_Produce_Plan_Detail_Part_Count")
public class ProducePlanDetailPartCount extends BaseEntity {
    @Desc("生产计划明细ID")
    @Column(nullable = false)
    private Long planDetailId;

    @Desc("订单ID")
    @Column(nullable = false)
    private Long salesOrderId;

    @Desc("订单明细ID")
    @Index(name = "salesOrderDetailId")
    @Column(nullable = false)
    private Long salesOrderDetailId;

    @Desc("部件ID")
    @Column(nullable = false)
    private Long partId;

    @Desc("部件BOM数量")
    @Column(nullable = false)
    private Integer partBomCount;

    @Desc("计划部件数量")
    @Column(nullable = false)
    private Integer planPartCount;

    @Desc("订单部件数量")
    @Column(nullable = false)
    private Integer partCount;

    @Desc("部件名称")
    @Column(nullable = false)
    private String partName;

    @Desc("部件类型")
    @Column(nullable = false)
    private String partType;
    @Desc("部件重量")
    private String partWeight;

    public String getPartWeight() {
        return partWeight;
    }

    public void setPartWeight(String partWeight) {
        this.partWeight = partWeight;
    }

    /**
     * 0，null:未创建，1：已创建
     */
    @Desc("创建任务单")
    @Column(nullable = true)
    private Integer createCutTask;

    @Desc("镜像部件ID")
    @Column
    private Long mirrorPartId;

    public Long getMirrorPartId() {
        return mirrorPartId;
    }

    public void setMirrorPartId(Long mirrorPartId) {
        this.mirrorPartId = mirrorPartId;
    }

    public Long getPlanDetailId() {
        return planDetailId;
    }

    public void setPlanDetailId(Long planDetailId) {
        this.planDetailId = planDetailId;
    }

    public Long getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(Long salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public Long getSalesOrderDetailId() {
        return salesOrderDetailId;
    }

    public void setSalesOrderDetailId(Long salesOrderDetailId) {
        this.salesOrderDetailId = salesOrderDetailId;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Integer getPartBomCount() {
        return partBomCount;
    }

    public void setPartBomCount(Integer partBomCount) {
        this.partBomCount = partBomCount;
    }

    public Integer getPlanPartCount() {
        return planPartCount;
    }

    public void setPlanPartCount(Integer planPartCount) {
        this.planPartCount = planPartCount;
    }

    public Integer getPartCount() {
        return partCount;
    }

    public void setPartCount(Integer partCount) {
        this.partCount = partCount;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public Integer getCreateCutTask() {
        return createCutTask;
    }

    public void setCreateCutTask(Integer createCutTask) {
        this.createCutTask = createCutTask;
    }


}

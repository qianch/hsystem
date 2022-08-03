package com.bluebirdme.mes.sales.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 销售订单明细中，套材的部件数量
 *
 * @author Goofy
 * @Date 2017年2月28日 上午10:49:42
 */
@SuppressWarnings("deprecation")
@Entity
@Table(name = "HS_Sales_Order_Detail_Parts_Count")
public class SalesOrderDetailPartsCount extends BaseEntity {

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
    private Double partWeight;

    @Desc("镜像部件ID")
    @Column
    private Long mirrorPartId;

    @Desc("镜像版本id")
    @Column
    private Long mirrorTcProcBomVersionId;

    public Long getMirrorTcProcBomVersionId() {
        return mirrorTcProcBomVersionId;
    }

    public void setMirrorTcProcBomVersionId(Long mirrorTcProcBomVersionId) {
        this.mirrorTcProcBomVersionId = mirrorTcProcBomVersionId;
    }

    public Long getMirrorPartId() {
        return mirrorPartId;
    }

    public void setMirrorPartId(Long mirrorPartId) {
        this.mirrorPartId = mirrorPartId;
    }

    public Double getPartWeight() {
        return partWeight;
    }

    public void setPartWeight(Double partWeight) {
        this.partWeight = partWeight;
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

    public Long getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(Long salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

}

package com.bluebirdme.mes.cut.cutSalesOrder.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("派工单订单表")
@Entity
@Table(name = "hs_cut_sales_order")
@DynamicInsert
public class CutSalesOrder extends BaseEntity {


    @Desc("生产计划明细Id")
    private long producePlanDetailId;

    @Desc("订单明细明细Id")
    private long salesOrderDetailId;

    @Desc("批次号")
    @Index(name = "batchCode")
    private String batchCode;

    @Desc("订单号")
    @Index(name = "salesOrderCode")
    private String salesOrderCode;

    @Desc("客户编码")
    private String customerCode;

    @Desc("客户名称")
    private String customerName;

    @Desc("裁剪bomId")
    private long tcBomMainId;


    @Desc("叶型名称")
    @Index(name = "bladeTypeName")
    private String bladeTypeName;


    @Desc("订单数量")
    @Column
    private Integer orderNum;

    @Desc("备注")
    private String remark;

    @Desc("状态")
    @Column
    private Integer auditState;

    @Desc("创建人")
    @Column(nullable = true)
    private String creater;

    @Desc("创建时间")
    @Column
    private Date createTime;

    @Desc("修改人")
    @Column
    private String modifyUser;

    @Desc("修改时间")
    @Column
    private Date modifyTime;

    @Desc("审核人")
    @Column
    private String shrNo;

    @Desc("审核时间")
    @Column
    private Date shrTime;

    public long getProducePlanDetailId() {
        return producePlanDetailId;
    }

    public void setProducePlanDetailId(long producePlanDetailId) {
        this.producePlanDetailId = producePlanDetailId;
    }

    public long getSalesOrderDetailId() {return salesOrderDetailId;}

    public void setSalesOrderDetailId(long salesOrderDetailId) {this.salesOrderDetailId = salesOrderDetailId; }


    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getSalesOrderCode() {
        return salesOrderCode;
    }

    public void setSalesOrderCode(String salesOrderCode) {
        this.salesOrderCode = salesOrderCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getTcBomMainId() {
        return tcBomMainId;
    }

    public void setTcBomMainId(long tcBomMainId) {
        this.tcBomMainId = tcBomMainId;
    }

    public String getBladeTypeName() {
        return bladeTypeName;
    }

    public void setBladeTypeName(String bladeTypeName) {
        this.bladeTypeName = bladeTypeName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getShrNo() {
        return shrNo;
    }

    public void setShrNo(String shrNo) {
        this.shrNo = shrNo;
    }

    public Date getShrTime() {
        return shrTime;
    }

    public void setShrTime(Date shrTime) {
        this.shrTime = shrTime;
    }
}

package com.bluebirdme.mes.planner.cut.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 裁剪计划
 *
 * @author 宋黎明
 * @Date 2016年10月17日 上午17:02:34
 */

@Desc("裁剪计划")
@Entity
@Table(name = "HS_Cut_Plan")
@DynamicInsert
public class CutPlan extends BaseEntity implements Iplan {
    @Desc("产品id")
    @Column
    @Index(name = "productId")
    private Long productId;
    @Desc("任务单号")
    @Column
    @Index(name = "planCode")
    private String planCode;
    @Desc("客户id")
    @Column
    private Long consumerId;
    @Desc("客户名称")
    @Column
    @Index(name = "consumerName")
    private String consumerName;
    @Desc("订单号")
    @Column
    @Index(name = "salesOrderCode")
    private String salesOrderCode;
    @Desc("批次号")
    @Column
    @Index(name = "batchCode")
    private String batchCode;
    @Desc("产品规格")
    @Column
    @Index(name = "productModel")
    private String productModel;
    @Desc("产品名称")
    @Column
    private String productName;
    @Desc("门幅")
    @Column
    private Double productWidth;
    @Desc("卷重")
    @Column
    private Double productWeight;

    @Desc("卷长")
    @Column
    private Double productLength;

    /**
     * 套材为套，非套材为kg
     */
    @Desc("订单数量(kg/套)")
    @Column
    private Double orderCount;

    @Column
    @Desc("工艺版本")
    private String processBomVersion;
    @Column
    @Desc("工艺代码")
    private String processBomCode;
    @Column
    @Desc("包装版本")
    private String bcBomVersion;
    @Column
    @Desc("包装代码")
    private String bcBomCode;

    @Column
    @Desc("总卷数")
    private Integer totalRollCount;

    @Column
    @Desc("总托数")
    private Integer totalTrayCount;

    @Desc("需要生产数量(kg/套)")
    @Column
    private Double requirementCount;

    @Desc("已生产数量(kg/套)")
    @Column
    private Double producedCount;
    @Desc("出货日期")
    @Column
    private Date deleveryDate;
    @Desc("机台号")
    @Column
    private String deviceCode;

    @Desc("备注")
    @Column
    private String comment;
    @Desc("是否已完成")
    @Column
    private Integer isFinished;
    @Desc("排序")
    @Column
    private Long sort;
    @Desc("产品属性")
    @Column
    private Integer productType;
    @Desc("生产计划明细Id")
    @Column
    private Long producePlanDetailId;

    @Desc("打包数量")
    @Column
    private Integer packagedCount;

    @Desc("生产卷数")
    @Column
    private Integer producedRolls;

    @Desc("来自套材")
    @Column
    private Long fromTcId;
    @Desc("来自套材名称")
    @Column
    private String fromTcName;

    @Desc("来自的订单明细ID")
    @Column
    private Long fromSalesOrderDetailId;

    @Desc("部件id")
    @Column
    private Long partId;

    @Transient
    private String partName;

    @Desc("是否已排产")
    @Column
    private Integer isSettled;
    @Desc("工艺版本ID")
    private Long procBomId;
    @Desc("包装版本ID")
    private Long packBomId;
    /**
     * 0，null:已关闭
     * 1：未关闭
     */
    @Desc("是否已关闭")
    @Column
    private Integer closed;
    @Desc("是否已生成编织任务")
    @Column
    //0未生成，1已生成
    private Integer isCreatWeave;
    @Desc("生成的订单的ID")
    @Column
    private Long weaveSalesOrderId;


    @Desc("镜像版本id")
    @Column
    private Long mirrorProcBomId;


    @Override
    public Long getMirrorProcBomId() {
        return mirrorProcBomId;
    }

    public void setMirrorProcBomId(Long mirrorProcBomId) {
        this.mirrorProcBomId = mirrorProcBomId;
    }


    public Long getWeaveSalesOrderId() {
        return weaveSalesOrderId;
    }

    public void setWeaveSalesOrderId(Long weaveSalesOrderId) {
        this.weaveSalesOrderId = weaveSalesOrderId;
    }

    public Integer getIsCreatWeave() {
        return isCreatWeave;
    }

    public void setIsCreatWeave(Integer isCreatWeave) {
        this.isCreatWeave = isCreatWeave;
    }

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
    }

    public Long getProcBomId() {
        return procBomId;
    }

    public void setProcBomId(Long procBomId) {
        this.procBomId = procBomId;
    }

    public Long getPackBomId() {
        return packBomId;
    }

    public void setPackBomId(Long packBomId) {
        this.packBomId = packBomId;
    }

    public Integer getIsSettled() {
        return isSettled;
    }

    public void setIsSettled(Integer isSettled) {
        this.isSettled = isSettled;
    }


    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    /**
     * **get**
     */
    public Long getFromSalesOrderDetailId() {
        return fromSalesOrderDetailId;
    }

    /**
     * **set**
     */
    public void setFromSalesOrderDetailId(Long fromSalesOrderDetailId) {
        this.fromSalesOrderDetailId = fromSalesOrderDetailId;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Long getFromTcId() {
        return fromTcId;
    }

    public void setFromTcId(Long fromTcId) {
        this.fromTcId = fromTcId;
    }

    public String getFromTcName() {
        return fromTcName;
    }

    public void setFromTcName(String fromTcName) {
        this.fromTcName = fromTcName;
    }

    public Integer getPackagedCount() {
        return packagedCount;
    }

    public void setPackagedCount(Integer packagedCount) {
        this.packagedCount = packagedCount;
    }

    public Long getProducePlanDetailId() {
        return producePlanDetailId;
    }

    public void setProducePlanDetailId(Long producePlanDetailId) {
        this.producePlanDetailId = producePlanDetailId;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getSalesOrderCode() {
        return salesOrderCode;
    }

    public void setSalesOrderCode(String salesOrderCode) {
        this.salesOrderCode = salesOrderCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductWidth() {
        return productWidth;
    }

    public void setProductWidth(Double productWidth) {
        this.productWidth = productWidth;
    }

    public Double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(Double productWeight) {
        this.productWeight = productWeight;
    }

    public Double getProductLength() {
        return productLength;
    }

    public void setProductLength(Double productLength) {
        this.productLength = productLength;
    }

    public Double getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Double orderCount) {
        this.orderCount = orderCount;
    }


    public String getBcBomVersion() {
        return bcBomVersion;
    }

    public void setBcBomVersion(String bcBomVersion) {
        this.bcBomVersion = bcBomVersion;
    }

    public String getBcBomCode() {
        return bcBomCode;
    }

    public void setBcBomCode(String bcBomCode) {
        this.bcBomCode = bcBomCode;
    }


    /**
     * **get**
     */
    public String getProcessBomVersion() {
        return processBomVersion;
    }

    /**
     * **set**
     */
    public void setProcessBomVersion(String processBomVersion) {
        this.processBomVersion = processBomVersion;
    }

    /**
     * **get**
     */
    public String getProcessBomCode() {
        return processBomCode;
    }

    /**
     * **set**
     */
    public void setProcessBomCode(String processBomCode) {
        this.processBomCode = processBomCode;
    }

    /**
     * **get**
     */
    public Integer getTotalRollCount() {
        return totalRollCount;
    }

    /**
     * **set**
     */
    public void setTotalRollCount(Integer totalRollCount) {
        this.totalRollCount = totalRollCount;
    }

    /**
     * **get**
     */
    public Integer getTotalTrayCount() {
        return totalTrayCount;
    }

    /**
     * **set**
     */
    public void setTotalTrayCount(Integer totalTrayCount) {
        this.totalTrayCount = totalTrayCount;
    }

    public Double getRequirementCount() {
        return requirementCount;
    }

    public void setRequirementCount(Double requirementCount) {
        this.requirementCount = requirementCount;
    }

    public Double getProducedCount() {
        return producedCount;
    }

    public void setProducedCount(Double producedCount) {
        this.producedCount = producedCount;
    }

    public Date getDeleveryDate() {
        return deleveryDate;
    }

    public void setDeleveryDate(Date deleveryDate) {
        this.deleveryDate = deleveryDate;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getProducedRolls() {
        return producedRolls;
    }

    public void setProducedRolls(Integer producedRolls) {
        this.producedRolls = producedRolls;
    }


}

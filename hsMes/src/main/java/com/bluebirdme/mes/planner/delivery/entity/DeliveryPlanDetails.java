package com.bluebirdme.mes.planner.delivery.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("出货详情列表")
@Entity
@Table(name = "HS_Delivery_Plan_Details")
public class DeliveryPlanDetails extends BaseEntity {
    @Desc("装箱号")
    private String pn;

    @Desc("装箱号uuid")
    @Column(nullable = false)

    @Index(name = "packingNumber")
    private String packingNumber;

    @Desc("订单号")
    @Column(nullable = false)
    @Index(name = "salesOrderSubCode")
    private String salesOrderSubCode;

    @Desc("订单ID")
    @Column(nullable = false)
    @Index(name = "salesOrderDetailId")
    private Long salesOrderDetailId;

    @Desc("批次号")
    @Column(nullable = false)
    @Index(name = "batchCode")
    private String batchCode;

    @Desc("成品ID")
    @Column(nullable = false)
    private Long productId;

    @Desc("备注")
    @Column
    private String memo;

    @Desc("出货计划ID")
    @Column(nullable = false)
    @Index(name = "deliveryId")
    private Long deliveryId;

    @Desc("出库托数")
    @Column(nullable = false)
    private Integer deliveryCount;

    @Desc("产品部件")
    @Column
    private String partName;

    @Desc("产品ID")
    @Column
    private Long partID;

    public Long getPartID() {
        return partID;
    }

    public void setPartID(Long partID) {
        this.partID = partID;
    }

    @Desc("客户产品名称")
    @Column(nullable = false)
    @Index(name = "consumerProductName")
    private String consumerProductName;

    @Desc("厂内名称")
    @Column(nullable = false)
    @Index(name = "factoryProductName")
    private String factoryProductName;

    @Desc("产品规格")
    @Column
    @Index(name = "productModel")
    private String productModel;

    @Desc("出库托数(套)")
    private Integer deliverySuitCount;

    @Desc("客户物料号")
    @Column
    private String customerMaterialCodeOfFP;

    public String getCustomerMaterialCodeOfFP() {
        return customerMaterialCodeOfFP;
    }

    public void setCustomerMaterialCodeOfFP(String customerMaterialCodeOfFP) {
        this.customerMaterialCodeOfFP = customerMaterialCodeOfFP;
    }

    public Integer getDeliverySuitCount() {
        return deliverySuitCount;
    }

    public void setDeliverySuitCount(Integer deliverySuitCount) {
        this.deliverySuitCount = deliverySuitCount;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getPackingNumber() {
        return packingNumber;
    }

    public void setPackingNumber(String packingNumber) {
        this.packingNumber = packingNumber;
    }

    public Long getSalesOrderDetailId() {
        return salesOrderDetailId;
    }

    public void setSalesOrderDetailId(Long salesOrderDetailId) {
        this.salesOrderDetailId = salesOrderDetailId;
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

    public String getConsumerProductName() {
        return consumerProductName;
    }

    public void setConsumerProductName(String consumerProductName) {
        this.consumerProductName = consumerProductName;
    }

    public String getFactoryProductName() {
        return factoryProductName;
    }

    public void setFactoryProductName(String factoryProductName) {
        this.factoryProductName = factoryProductName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getSalesOrderSubCode() {
        return salesOrderSubCode;
    }

    public void setSalesOrderSubCode(String salesOrderSubCode) {
        this.salesOrderSubCode = salesOrderSubCode;
    }

    public Integer getDeliveryCount() {
        return deliveryCount;
    }

    public void setDeliveryCount(Integer deliveryCount) {
        this.deliveryCount = deliveryCount;
    }
}

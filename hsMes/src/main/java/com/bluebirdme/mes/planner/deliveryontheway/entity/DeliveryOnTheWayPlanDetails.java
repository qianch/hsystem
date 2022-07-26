package com.bluebirdme.mes.planner.deliveryontheway.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("在途计划详情列表")
@Entity
@Table(name = "hs_delivery_ontheway_plan_detail")
public class DeliveryOnTheWayPlanDetails extends BaseEntity {
    @Desc("出货计划ID")
    @Column(nullable = false)
    @Index(name = "deliveryId")
    private Long deliveryId;

    @Desc("条码编号")
    @Column(nullable = false)
    @Index(name = "barcode")
    private String barcode;

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

    @Desc("产品部件")
    @Column
    private String partName;

    @Desc("客户产品名称")
    @Column(nullable = false)
    @Index(name = "consumerProductName")
    private String consumerProductName;

    @Desc("厂内名称")
    @Column(nullable = false)
    @Index(name = "factoryProductName")
    private String factoryProductName;

    @Desc("重量")
    @Column
    @Index(name = "weight")
    private double weight;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


}

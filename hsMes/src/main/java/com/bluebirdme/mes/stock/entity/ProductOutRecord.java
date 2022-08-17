package com.bluebirdme.mes.stock.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("成品出库记录表")
@Entity
@Table(name = "HS_Product_Out_Record")
public class ProductOutRecord extends BaseEntity {

    @Desc("出库单")
    @Column(nullable = false)
    @Index(name = "deliveryCode")
    private String deliveryCode;

    @Desc("出库Id")
    @Column(nullable = false)
    @Index(name = "deliveryId")
    private Integer deliveryId;

    @Desc("装箱号")
    @Column(nullable = false)
    @Index(name = "packingNumber")
    private String packingNumber;

    @Desc("库位编码")
    @Column(nullable = false)
    @Index(name = "warehousePosCode")
    private String warehousePosCode;

    @Desc("仓库编码")
    @Column(nullable = false)
    @Index(name = "warehouseCode")
    private String warehouseCode;


    @Desc("条码")
    @Column
    @Index(name = "barCode")
    private String barCode;
    @Desc("厂内名称")
    @Column
    @Index(name = "productFactoryName")
    private String productFactoryName;
    @Desc("客户名称")
    @Column
    @Index(name = "productConsumerName")
    private String productConsumerName;
    @Desc("产品型号")
    @Column
    @Index(name = "productModel")
    private String productModel;

    @Desc("门幅")
    @Column
    private Double width;
    @Desc("米长")
    @Column
    private Double length;

    @Desc("出库时间")
    @Column(nullable = false)
    private Date outTime;

    @Desc("重量")
    private Double weight;

    @Desc("同步状态")
    private Integer syncState = 0;

    @Desc("操作人")
    private Long operateUserId;


    @Desc("出库单ID")
    private Long outOrderId;

    public String getProductFactoryName() {
        return productFactoryName;
    }

    public void setProductFactoryName(String productFactoryName) {
        this.productFactoryName = productFactoryName;
    }

    public String getProductConsumerName() {
        return productConsumerName;
    }

    public void setProductConsumerName(String productConsumerName) {
        this.productConsumerName = productConsumerName;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getPackingNumber() {
        return packingNumber;
    }

    public void setPackingNumber(String packingNumber) {
        this.packingNumber = packingNumber;
    }


    /**
     * **get**
     */
    public String getWarehousePosCode() {
        return warehousePosCode;
    }

    /**
     * **set**
     */
    public void setWarehousePosCode(String warehousePosCode) {
        this.warehousePosCode = warehousePosCode;
    }

    /**
     * **get**
     */
    public String getWarehouseCode() {
        return warehouseCode;
    }

    /**
     * **set**
     */
    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }


    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * **get**
     */
    public Date getOutTime() {
        return outTime;
    }

    /**
     * **set**
     */
    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    /**
     * **get**
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * **set**
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * **get**
     */
    public Integer getSyncState() {
        return syncState;
    }

    /**
     * **set**
     */
    public void setSyncState(Integer syncState) {
        this.syncState = syncState;
    }

    /**
     * **get**
     */
    public Long getOperateUserId() {
        return operateUserId;
    }

    /**
     * **set**
     */
    public void setOperateUserId(Long operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }


    public Long getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(Long outOrderId) {
        this.outOrderId = outOrderId;
    }

}

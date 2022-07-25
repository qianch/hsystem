package com.bluebirdme.mes.planner.delivery.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Desc("出货订单关联")
@Entity
@Table(name = "HS_Delivery_Plan_SalesOrders")
public class DeliveryPlanSalesOrders extends BaseEntity {
    @Desc("出货计划ID")
    @Column(nullable = false)
    @Index(name = "deliveryId")
    private Long deliveryId;

    @Desc("装箱号")
    private String pn;

    @Desc("装箱号uuid")
    @Column(nullable = false)
    private String packingNumber;

    @Desc("提单号")
    @Column
    private String ladingCode;

    @Desc("车牌号")
    @Column
    private String plate;

    @Desc("箱号")
    @Column
    private String boxNumber;

    @Desc("实际箱号")
    @Column
    private String realBoxNumber;

    @Desc("封号")
    @Column
    private String serialNumber;

    @Desc("件数")
    @Column
    private Double count;

    @Desc("毛重")
    @Column
    private Double weight;

    @Desc("尺码")
    @Column
    private Double size;

    @Transient
    private String pids;

    @Desc("是否完成")
    @Column
    private Integer isFinished;

    @Desc("PDA标识")
    @Column(nullable = true)
    private String pdaID;

    @Desc("当前操作人")
    private String optUser;

    public String getRealBoxNumber() {
        return realBoxNumber;
    }

    public void setRealBoxNumber(String realBoxNumber) {
        this.realBoxNumber = realBoxNumber;
    }

    public Integer getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }

    public String getPackingNumber() {
        return packingNumber;
    }

    public void setPackingNumber(String packingNumber) {
        this.packingNumber = packingNumber;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }

    public String getLadingCode() {
        return ladingCode;
    }

    public void setLadingCode(String ladingCode) {
        this.ladingCode = ladingCode;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getPdaID() {
        return pdaID;
    }

    public void setPdaID(String pdaID) {
        this.pdaID = pdaID;
    }

    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }
}

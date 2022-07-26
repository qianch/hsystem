package com.bluebirdme.mes.planner.deliveryontheway.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Desc("在途计划表")
@Entity
@Table(name = "hs_delivery_ontheway_plan")
@DynamicInsert
public class DeliveryOnTheWayPlan extends BaseEntity {
    @Desc("出库调拨单号")
    @Column(nullable = false)
    @Index(name = "deliveryCode")
    private String deliveryCode;

    @Desc("出货人")
    @Column
    private Long deliveryBizUserId;

    @Desc("终点仓库")
    @Column
    private String wareHouseCode;

    @Desc("车牌")
    @Column
    private String plate;

    @Desc("在途时间")
    @Column
    private Date deliveryDate;

    @Desc("审核状态")
    @Column(nullable = false, columnDefinition = "int default 0")
    @Index(name = "auditState")
    private Integer auditState = 0;

    @Transient
    private List<DeliveryOnTheWayPlanDetails> productDatas;

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getWareHouseCode() {
        return wareHouseCode;
    }

    public void setWareHouseCode(String wareHoseCode) {
        this.wareHouseCode = wareHoseCode;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Long getDeliveryBizUserId() {
        return deliveryBizUserId;
    }

    public void setDeliveryBizUserId(Long deliveryBizUserId) {
        this.deliveryBizUserId = deliveryBizUserId;
    }


    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }


    public List<DeliveryOnTheWayPlanDetails> getProductDatas() {
        return productDatas;
    }

    public void setProductDatas(List<DeliveryOnTheWayPlanDetails> productDatas) {
        this.productDatas = productDatas;
    }

}

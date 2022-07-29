package com.bluebirdme.mes.planner.turnbag.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 翻包明细
 *
 * @author Goofy
 * @Date 2017年3月22日 下午3:24:19
 */
@Entity
@Table(name = "hs_turnbag_plan_details")
public class TurnBagPlanDetails extends BaseEntity {
    @Column
    private Long producePlanDetailId;

    @Column
    private Long fromProducePlanDetailId;

    @Column
    private Long salesOrderDetailId;
    @Column
    private String batchCode;
    @Column
    private Integer turnBagCount;

    @Desc("领出数量")
    @Column(columnDefinition = "int(5) default 0")
    private Integer takeOutCount;

    @Desc("仓库领出数量")
    @Column
    private Integer takeOutCountFromWareHouse;

    @Desc("翻包备注")
    @Column
    private String memo;

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

    public Integer getTurnBagCount() {
        return turnBagCount;
    }

    public void setTurnBagCount(Integer turnBagCount) {
        this.turnBagCount = turnBagCount;
    }

    public Long getProducePlanDetailId() {
        return producePlanDetailId;
    }

    public void setProducePlanDetailId(Long producePlanDetailId) {
        this.producePlanDetailId = producePlanDetailId;
    }

    public Integer getTakeOutCount() {
        return takeOutCount == null ? 0 : takeOutCount;
    }

    public void setTakeOutCount(Integer takeOutCount) {
        this.takeOutCount = takeOutCount;
    }

    public Integer getTakeOutCountFromWareHouse() {
        return takeOutCountFromWareHouse;
    }

    public void setTakeOutCountFromWareHouse(Integer takeOutCountFromWareHouse) {
        this.takeOutCountFromWareHouse = takeOutCountFromWareHouse;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getFromProducePlanDetailId() {
        return fromProducePlanDetailId;
    }

    public void setFromProducePlanDetailId(Long fromProducePlanDetailId) {
        this.fromProducePlanDetailId = fromProducePlanDetailId;
    }
}

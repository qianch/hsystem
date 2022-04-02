package com.bluebirdme.mes.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("库位管理")
@Entity
@Table(name = "hs_warehouse_pos")
@DynamicInsert
public class WarehosePos extends BaseEntity {
    @Desc("库位代码")
    @Column(nullable = false)
    @Index(name = "warehousePosCode")
    private String warehousePosCode;

    @Desc("库位名称")
    @Column(nullable = false)
    private String warehousePosName;

    @Desc("备注")
    @Column(nullable = true)
    private String warehousePosMemo;

    @Desc("所属仓库")
    @Column(nullable = false)
    @Index(name = "warehouseId")
    private Long warehouseId;
    @Desc("是否作废")
    private Integer isCancellation;

    public String getWarehousePosCode() {
        return warehousePosCode;
    }

    public void setWarehousePosCode(String warehousePosCode) {
        this.warehousePosCode = warehousePosCode;
    }

    public String getWarehousePosName() {
        return warehousePosName;
    }

    public void setWarehousePosName(String warehousePosName) {
        this.warehousePosName = warehousePosName;
    }

    public String getWarehousePosMemo() {
        return warehousePosMemo;
    }

    public void setWarehousePosMemo(String warehousePosMemo) {
        this.warehousePosMemo = warehousePosMemo;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getIsCancellation() {
        return isCancellation;
    }

    public void setIsCancellation(Integer isCancellation) {
        this.isCancellation = isCancellation;
    }
}

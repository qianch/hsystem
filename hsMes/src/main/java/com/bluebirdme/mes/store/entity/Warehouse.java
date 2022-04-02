package com.bluebirdme.mes.store.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("仓库管理")
@Entity
@Table(name = "hs_warehouse")
@DynamicInsert
public class Warehouse extends BaseEntity {
    @Desc("仓库代码")
    @Column(nullable = true)
    @Index(name = "warehouseCode")
    private String warehouseCode;

    @Desc("仓库名称")
    @Column(nullable = false)
    private String warehouseName;

    @Desc("仓库类型")
    private String wareType;

    @Desc("是否作废")
    private Integer isCancellation;
    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWareType() {
        return wareType;
    }

    public void setWareType(String wareType) {
        this.wareType = wareType;
    }

    public Integer getIsCancellation() {
        return isCancellation;
    }

    public void setIsCancellation(Integer isCancellation) {
        this.isCancellation = isCancellation;
    }
}

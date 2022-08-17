package com.bluebirdme.mes.stock.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("胚布移库表")
@Entity
@Table(name = "hs_stock_fabric_move")
public class StockFabricMove extends BaseEntity {
    @Desc("条码号")
    @Column(nullable = false)
    @Index(name = "barcode")
    private String barcode;

    @Desc("原库位")
    @Column(nullable = false)
    private String originWarehousePosCode;

    @Desc("原库房")
    @Column(nullable = false)
    private String originWarehouseCode;

    @Desc("新库位")
    @Column(nullable = false)
    private String newWarehousePosCode;

    @Desc("新库房")
    @Column(nullable = false)
    private String newWarehouseCode;

    @Desc("移库时间")
    @Column(nullable = false)
    private Date moveTime;

    @Desc("操作人")
    @Column(nullable = false)
    private Long moveUserId;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getOriginWarehousePosCode() {
        return originWarehousePosCode;
    }


    public void setOriginWarehousePosCode(String originWarehousePosCode) {
        this.originWarehousePosCode = originWarehousePosCode;
    }


    public String getOriginWarehouseCode() {
        return originWarehouseCode;
    }


    public void setOriginWarehouseCode(String originWarehouseCode) {
        this.originWarehouseCode = originWarehouseCode;
    }


    public String getNewWarehousePosCode() {
        return newWarehousePosCode;
    }


    public void setNewWarehousePosCode(String newWarehousePosCode) {
        this.newWarehousePosCode = newWarehousePosCode;
    }

    public String getNewWarehouseCode() {
        return newWarehouseCode;
    }

    public void setNewWarehouseCode(String newWarehouseCode) {
        this.newWarehouseCode = newWarehouseCode;
    }

    public Date getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(Date moveTime) {
        this.moveTime = moveTime;
    }

    public Long getMoveUserId() {
        return moveUserId;
    }

    public void setMoveUserId(Long moveUserId) {
        this.moveUserId = moveUserId;
    }
}

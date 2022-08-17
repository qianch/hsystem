package com.bluebirdme.mes.stock.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("成品入库事务表")
@Entity
@Table(name = "hs_product_stock_tran")
public class ProductStockTran extends BaseEntity {

    @Desc("原库位")
    @Index(name = "originWarehousePosCode")
    private String originWarehousePosCode;

    @Desc("原库房")
    @Index(name = "originWarehouseCode")
    private String originWarehouseCode;

    @Desc("库位编码")
    @Column(nullable = false)
    @Index(name = "newWarehousePosCode")
    private String newWarehousePosCode;

    @Desc("仓库编码")
    @Column(nullable = false)
    @Index(name = "newWarehouseCode")
    private String newWarehouseCode;

    @Desc("条码")
    @Column
    @Index(name = "barCode")
    private String barCode;

    @Desc("入库时间")
    @Column(nullable = false)
    private Date inTime;

    @Desc("操作人")
    private Long operateUserId;

    @Desc("状态")
    private int status;

    /**
     * **get**
     */
    public String getOriginWarehousePosCode() {
        return originWarehousePosCode;
    }

    /**
     * **set**
     */
    public void setOriginWarehousePosCode(String originWarehousePosCode) {
        this.originWarehousePosCode = originWarehousePosCode;
    }

    /**
     * **get**
     */
    public String getOriginWarehouseCode() {
        return originWarehouseCode;
    }

    /**
     * **set**
     */
    public void setOriginWarehouseCode(String originWarehouseCode) {
        this.originWarehouseCode = originWarehouseCode;
    }


    /**
     * **get**
     */
    public String getNewWarehousePosCode() {
        return newWarehousePosCode;
    }

    /**
     * **set**
     */
    public void setNewWarehousePosCode(String newWarehousePosCode) {
        this.newWarehousePosCode = newWarehousePosCode;
    }

    /**
     * **get**
     */
    public String getNewWarehouseCode() {
        return newWarehouseCode;
    }

    /**
     * **set**
     */
    public void setNewWarehouseCode(String newWarehouseCode) {
        this.newWarehouseCode = newWarehouseCode;
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
    public Date getInTime() {
        return inTime;
    }

    /**
     * **set**
     */
    public void setInTime(Date inTime) {
        this.inTime = inTime;
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

    /**
     * **get**
     */
    public int getStatus() {
        return status;
    }

    /**
     * **set**
     */
    public void setStatus(int status) {
        this.status = status;
    }


}

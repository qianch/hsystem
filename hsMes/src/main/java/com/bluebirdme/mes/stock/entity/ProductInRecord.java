package com.bluebirdme.mes.stock.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("成品入库记录表")
@Entity
@Table(name = "HS_Product_In_Record")
public class ProductInRecord extends BaseEntity {
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

    @Desc("入库时间")
    @Column(nullable = false)
    private Date inTime;

    @Desc("重量")
    private Double weight;

    @Desc("同步状态")
    private Integer syncState;

    @Desc("操作人")
    private Long operateUserId;

    @Desc("入库来源")
    @Column(nullable = false, columnDefinition = "varchar(100) default ''")
    private String inBankSource;

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

    public String getInBankSource() {
        return inBankSource;
    }

    public void setInBankSource(String inBankSource) {
        this.inBankSource = inBankSource;
    }


}

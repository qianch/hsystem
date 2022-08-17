package com.bluebirdme.mes.stock.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("原料入库记录表")
@Entity
@Table(name = "HS_Material_In_Record")
public class MaterialInRecord extends BaseEntity {
    @Desc("库位编码")
    @Column(nullable = false)
    @Index(name = "warehousePosCode")
    private String warehousePosCode;

    @Desc("仓库编码")
    @Column(nullable = false)
    @Index(name = "warehouseCode")
    private String warehouseCode;

    @Desc("托盘ID")
    @Column(nullable = false)
    @Index(name = "mssId")
    private Long mssId;

    @Desc("入库时间")
    @Column(nullable = false)
    @Index(name = "inTime")
    private Long inTime;

    @Desc("操作人")
    @Column(nullable = true)
    @Index(name = "optUser")
    private String optUser;

    @Desc("k3同步状态")
    @Column(nullable = false)
    @Index(name = "syncState")
    private Integer syncState = 0;

    /**
     * 0：入库，-1：退库
     */
    @Desc("入库类型")
    @Column(nullable = true, columnDefinition = "int default 0")
    @Index(name = "inBankType")
    private Integer inBankType = 0;

    @Desc("入库来源")
    @Column(nullable = false, columnDefinition = "varchar(100) default ''")
    private String inBankSource;

    public String getWarehousePosCode() {
        return warehousePosCode;
    }

    public void setWarehousePosCode(String warehousePosCode) {
        this.warehousePosCode = warehousePosCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Long getMssId() {
        return mssId;
    }

    public void setMssId(Long mssId) {
        this.mssId = mssId;
    }

    public Long getInTime() {
        return inTime;
    }

    public void setInTime(Long inTime) {
        this.inTime = inTime;
    }

    public String getOptUser() {
        return optUser;
    }

    public void setOptUser(String optUser) {
        this.optUser = optUser;
    }

    public Integer getSyncState() {
        return syncState;
    }

    public void setSyncState(Integer syncState) {
        this.syncState = syncState;
    }

    public Integer getInBankType() {
        return inBankType;
    }

    public void setInBankType(Integer inBankType) {
        this.inBankType = inBankType;
    }

    public String getInBankSource() {
        return inBankSource;
    }

    public void setInBankSource(String inBankSource) {
        this.inBankSource = inBankSource;
    }

}

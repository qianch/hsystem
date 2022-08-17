package com.bluebirdme.mes.stock.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("原料出库单")
@Entity
@Table(name = "HS_Material_Stock_Out")
public class MaterialStockOut extends BaseEntity {
    @Desc("出库单号")
    @Column(nullable = false, unique = true)
    @Index(name = "outOrderCode")
    private String outOrderCode;

    @Desc("出库客户名称")
    @Column(nullable = false)
    private String cunsumerName;

    @Desc("出库时间")
    private Long outTime;

    @Desc("操作人")
    private String outOptUser;

    @Desc("领料车间")
    @Column(nullable = false)
    private String workShop;

    @Desc("领料车间编码")
    private String workShopCode;


    public String getOutOrderCode() {
        return outOrderCode;
    }

    public void setOutOrderCode(String outOrderCode) {
        this.outOrderCode = outOrderCode;
    }


    public String getCunsumerName() {
        return cunsumerName;
    }

    public void setCunsumerName(String cunsumerName) {
        this.cunsumerName = cunsumerName;
    }


    public Long getOutTime() {
        return outTime;
    }

    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }


    public String getOutOptUser() {
        return outOptUser;
    }

    public void setOutOptUser(String outOptUser) {
        this.outOptUser = outOptUser;
    }


    public String getWorkShop() {
        return workShop;
    }

    public void setWorkShop(String workShop) {
        this.workShop = workShop;
    }

    public String getWorkShopCode() {
        return workShopCode;
    }

    public void setWorkShopCode(String workShopCode) {
        this.workShopCode = workShopCode;
    }


}

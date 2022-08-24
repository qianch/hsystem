package com.bluebirdme.mes.store.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("条码作废记录")
@Entity
@Table(name = "hs_abandon_barcode")
@DynamicInsert
public class AbandonBarCode extends BaseEntity {
    @Desc("条码号")
    @Column
    private String barCode;

    @Desc("用户名")
    @Column
    private String userId;

    @Desc("创建时间")
    @Column
    private Date createDate;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

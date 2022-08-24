package com.bluebirdme.mes.store.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 托条码
 *
 * @author 宋黎明
 * @Date 2016年11月08日 上午10:02:34
 */
@Desc("拆包记录")
@Entity
@Table(name = "hs_openpack_barcode")
@DynamicInsert
public class OpenPackBarCode extends BaseEntity {
    @Desc("条码号")
    @Column
    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Desc("拆包日期")
    @Column
    private Date openPackDate;

    public Date getOpenPackDate() {
        return openPackDate;
    }

    public void setOpenPackDate(Date openPackDate) {
        this.openPackDate = openPackDate;
    }

    @Desc("拆包人ID")
    @Column
    private Long operateUserId;

    public Long getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(Long operateUserId) {
        this.operateUserId = operateUserId;
    }

    @Desc("拆包内容")
    @Column
    private String openPackContent;

    public String getOpenPackContent() {
        return openPackContent;
    }

    public void setOpenPackContent(String openPackContent) {
        this.openPackContent = openPackContent;
    }
}

package com.bluebirdme.mes.cut.baocai.entity;


import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("裁剪套材BOM主表")
@Entity
@Table(name = "hs_cut_baocaiku")
@DynamicInsert
public class BaoCaiKu extends BaseEntity {
    public String getBCKID() {
        return BCKID;
    }

    public void setBCKID(String BCKID) {
        this.BCKID = BCKID;
    }

    @Desc("BCKID")
    @Column(nullable = false)
    private String BCKID;

    @Desc("类型")
    @Column(nullable = false)
    private String type;

    @Desc("规格")
    @Column(nullable = false)
    private String specs;

    @Desc("尺寸")
    @Column(nullable = false)
    private String size;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    @Desc("库存")
    @Column(nullable = false)
    @Index(name = "stock")
    private String stock;
    @Desc("备注")
    @Column(nullable = false)
    @Index(name = "remarks")
    private String remarks;
    @Desc("预警")
    @Column(nullable = false)
    @Index(name = "warning")
    private String warning;
}

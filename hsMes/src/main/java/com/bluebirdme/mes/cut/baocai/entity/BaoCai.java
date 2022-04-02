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
@Table(name = "hs_cut_baocai")
@DynamicInsert
public class BaoCai extends BaseEntity {
    @Desc("BCID")
    @Column(nullable = false)
    private String BCID;

    @Desc("叶型")
    @Column(nullable = false)
    private String leaftype;

    @Desc("部件")
    @Column(nullable = false)
    private String specs;

    @Desc("种类")
    @Column(nullable = false)
    private String type;

    @Desc("尺寸")
    @Column(nullable = false)
    private String stock;

    @Desc("需求数量")
    @Column(nullable = false)
    private String ncounts;

    @Desc("数量")
    @Column(nullable = false)
    private String counts;

    public String getLeaftype() {
        return leaftype;
    }

    public void setLeaftype(String leaftype) {
        this.leaftype = leaftype;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getNcounts() {
        return ncounts;
    }

    public void setNcounts(String ncounts) {
        this.ncounts = ncounts;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getBCID() {
        return BCID;
    }

    public void setBCID(String BCID) {
        this.BCID = BCID;
    }
}

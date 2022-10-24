package com.bluebirdme.mes.core.base.entity;

import com.bluebirdme.mes.core.annotation.Desc;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@MappedSuperclass
public abstract class BaseEntityExt extends BaseEntity {
    @Desc("卷号")
    @Column
    private String rollNo;

    @Desc("图号")
    @Column
    private String drawNo;

    @Desc("层号")
    @Column
    private String levelNo;

    @Desc("排序号")
    @Column
    private Integer sorting;

    @Desc("部件ID")
    @Column
    private Long partId;

    @Desc("部件名称")
    @Column
    private String partName;

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getDrawNo() {
        return drawNo;
    }

    public void setDrawNo(String drawNo) {
        this.drawNo = drawNo;
    }

    public String getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(String levelNo) {
        this.levelNo = levelNo;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Integer getSorting() {
        return sorting;
    }

    public void setSorting(Integer sorting) {
        this.sorting = sorting;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

}
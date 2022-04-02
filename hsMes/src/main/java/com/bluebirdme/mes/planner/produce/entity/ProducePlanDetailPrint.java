package com.bluebirdme.mes.planner.produce.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("打印模版信息")
@Entity
@Table(name="HS_Produce_Plan_Detail_Print")
public class ProducePlanDetailPrint extends BaseEntity {

    @Desc("生产计划明细Id")
    @Column
    private Long producePlanDetailId;

    @Desc("标签模板ID")
    @Column
    private Long btwFileId;

    @Desc("打印属性")
    @Column(nullable=false)
    private String printAttribute;

    @Desc("打印属性名称")
    @Column(nullable=false)
    private String printAttributeName;

    @Desc("打印内容")
    @Column
    private String printAttributeContent;

    public long getProducePlanDetailId() {
        return producePlanDetailId;
    }
    public void setProducePlanDetailId(Long producePlanDetailId) {
        this.producePlanDetailId = producePlanDetailId;
    }

    public Long getBtwFileId() {return btwFileId;}
    public void setBtwFileId(Long btwFileId) {this.btwFileId = btwFileId;}

    public String getPrintAttribute() {
        return printAttribute;
    }
    public void setPrintAttribute(String printAttribute) {
        this.printAttribute = printAttribute;
    }

    public String getPrintAttributeName() {
        return printAttributeName;
    }
    public void setPrintAttributeName(String printAttributeName) {
        this.printAttributeName = printAttributeName;
    }

    public String getPrintAttributeContent() {
        return printAttributeContent;
    }
    public void setPrintAttributeContent(String printAttributeContent) {this.printAttributeContent = printAttributeContent;}
}

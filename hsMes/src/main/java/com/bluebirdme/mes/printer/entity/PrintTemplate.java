package com.bluebirdme.mes.printer.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("打印模版信息")
@Entity
@Table(name = "HS_PrintTemplate")
public class PrintTemplate extends BaseEntity {
    @Desc("打印属性")
    @Column(nullable = false)
    private String printAttribute;

    @Desc("打印属性名称")
    @Column(nullable = false)
    private String printAttributeName;

    @Desc("打印属性类型")
    @Column
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.bluebirdme.mes.btwManager.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("打印模版属性")
@Entity
@Table(name="hs_btwfile_print")
public class BtwFilePrint extends BaseEntity {

    @Desc("标签模板ID")
    private Long btwFileId;

    @Desc("打印属性")
    private String printAttribute;

    @Desc("打印属性名称")
    private String printAttributeName;

    @Desc("创建人")
    @Column
    private String creater;

    @Desc("创建时间")
    @Column
    private Date createTime;

    @Desc("修改人")
    @Column
    private String  modifyUser;

    @Desc("修改时间")
    @Column
    private Date modifyTime;

    public Long getBtwFileId() {
        return btwFileId;
    }

    public void setBtwFileId(Long btwFileId) {
        this.btwFileId = btwFileId;
    }

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

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }


}

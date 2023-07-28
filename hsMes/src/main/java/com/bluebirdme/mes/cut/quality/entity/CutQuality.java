package com.bluebirdme.mes.cut.quality.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("裁剪质检尺寸表")
@Entity
@Table(name = "hs_cut_quality")
public class CutQuality extends BaseEntity {
    @Desc("Bom代码+版本")
    @Column
    private String tcProcBomCodeVersion;

    @Desc("叶型名称")
    @Column
    private String bladeTypeName;

    @Desc("基布规格")
    @Column
    private String productModel;

    @Desc("产品编号")
    @Column
    private String productSerial;

    @Desc("图纸长度")
    @Column
    private String drawingLength;

    @Desc("图纸宽度")
    @Column
    private String drawingWidth;

    @Desc("所属部件名称")
    @Column
    private String partName;

    @Desc("基布类型")
    @Column
    private String baseClothType;

    @Desc("导入时间")
    @Column
    private Date exoportDate;

    @Desc("导入人ID")
    @Column
    private Long exoportUserId;

    @Desc("修改时间")
    @Column
    private Date modifyDate;

    @Desc("修改人ID")
    @Column
    private Long modifyUserId;

    public String getTcProcBomCodeVersion() {
        return tcProcBomCodeVersion;
    }

    public void setTcProcBomCodeVersion(String tcProcBomCodeVersion) {
        this.tcProcBomCodeVersion = tcProcBomCodeVersion;
    }

    public String getBladeTypeName() {
        return bladeTypeName;
    }

    public void setBladeTypeName(String bladeTypeName) {
        this.bladeTypeName = bladeTypeName;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductSerial() {
        return productSerial;
    }

    public void setProductSerial(String productSerial) {
        this.productSerial = productSerial;
    }

    public String getDrawingLength() {
        return drawingLength;
    }

    public void setDrawingLength(String drawingLength) {
        this.drawingLength = drawingLength;
    }

    public String getDrawingWidth() {
        return drawingWidth;
    }

    public void setDrawingWidth(String drawingWidth) {
        this.drawingWidth = drawingWidth;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getBaseClothType() {
        return baseClothType;
    }

    public void setBaseClothType(String baseClothType) {
        this.baseClothType = baseClothType;
    }

    public Date getExoportDate() {
        return exoportDate;
    }

    public void setExoportDate(Date exoportDate) {
        this.exoportDate = exoportDate;
    }

    public Long getExoportUserId() {
        return exoportUserId;
    }

    public void setExoportUserId(Long exoportUserId) {
        this.exoportUserId = exoportUserId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Long modifyUserId) {
        this.modifyUserId = modifyUserId;
    }
}

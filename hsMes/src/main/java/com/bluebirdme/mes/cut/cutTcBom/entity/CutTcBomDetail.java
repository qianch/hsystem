package com.bluebirdme.mes.cut.cutTcBom.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("裁剪套材BOM从表")
@Entity
@Table(name = "hs_cut_tc_proc_bom_detail")
@DynamicInsert
public class CutTcBomDetail extends BaseEntity {
    @Desc("派工套材主键")
    @Index(name = "mainId")
    private Long mainId;

    @Desc("部件名称")
    private String partName;

    @Desc("图纸名称")
    private String drawName;

    @Desc("朝向")
    private String orientation;

    @Desc("规格")
    private String productModel;

    @Desc("米长")
    private Double length;

    @Desc("门幅")
    private Double productWidth;

    @Desc("克重")
    private Double gramWeight;

    @Desc("制成率")
    private Double productionRate;

    @Desc("单价")
    private Double unitPrice;

    @Desc("尺寸上限")
    private Double upperSizeLimit;

    @Desc("尺寸下限")
    private Double lowerSizeLimit;

    @Desc("尺寸百分比")
    private Double sizePercentage;

    @Desc("尺寸绝对值")
    private Double sizeAbsoluteValue;

    @Desc("创建人")
    @Column(nullable = true)
    private String creater;

    @Desc("创建时间")
    @Column
    private Date createTime;

    @Desc("修改人")
    @Column
    private String modifyUser;

    @Desc("修改时间")
    @Column
    private Date modifyTime;


    /**
     * 派工套材主键
     *
     * @return
     */
    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    /**
     * 部件名称
     *
     * @return
     */
    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }


    /**
     * 图纸名称
     *
     * @return
     */
    public String getDrawName() {
        return drawName;
    }

    public void setDrawName(String drawName) {
        this.drawName = drawName;
    }


    /**
     * 朝向
     *
     * @return
     */
    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     * 规格
     *
     * @return
     */
    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }


    /**
     * 米长
     *
     * @return
     */
    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    /**
     * 门幅
     *
     * @return
     */
    public Double getProductWidth() {
        return productWidth;
    }

    public void setProductWidth(Double productWidth) {
        this.productWidth = productWidth;
    }

    /**
     * 克重
     *
     * @return
     */
    public Double getGramWeight() {
        return gramWeight;
    }

    public void setGramWeight(Double gramWeight) {
        this.gramWeight = gramWeight;
    }

    /**
     * 制成率
     *
     * @return
     */
    public Double getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(Double productionRate) {
        this.productionRate = productionRate;
    }


    /**
     * 单价
     *
     * @return
     */
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * 尺寸上限
     *
     * @return
     */
    public Double getUpperSizeLimit() {
        return upperSizeLimit;
    }

    public void setUpperSizeLimit(Double upperSizeLimit) {
        this.upperSizeLimit = upperSizeLimit;
    }

    /**
     * 尺寸下限
     *
     * @return
     */
    public Double getLowerSizeLimit() {
        return lowerSizeLimit;
    }

    public void setLowerSizeLimit(Double lowerSizeLimit) {
        this.lowerSizeLimit = lowerSizeLimit;
    }


    /**
     * 尺寸百分比
     *
     * @return
     */
    public Double getSizePercentage() {
        return sizePercentage;
    }

    public void setSizePercentage(Double sizePercentage) {
        this.sizePercentage = sizePercentage;
    }

    /**
     * 尺寸绝对值
     *
     * @return
     */
    public Double getSizeAbsoluteValue() {
        return sizeAbsoluteValue;
    }

    public void setSizeAbsoluteValue(Double sizeAbsoluteValue) {
        this.sizeAbsoluteValue = sizeAbsoluteValue;
    }


    /**
     * 创建人
     *
     * @return
     */
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    /**
     * 创建时间
     *
     * @return
     */
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 修改人
     *
     * @return
     */
    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    /**
     * 修改时间
     *
     * @return
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

}

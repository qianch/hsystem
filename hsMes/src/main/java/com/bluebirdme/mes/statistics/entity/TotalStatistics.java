package com.bluebirdme.mes.statistics.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("综合统计")
@Entity
@Table(name = "hs_TotalStatistics")
public class TotalStatistics extends BaseEntity {
    @Desc("条码号")
    @Column
    @Index(name = "rollBarcode")
    private String rollBarcode;

    @Desc("条码类型")
    @Column
    private String barcodeType;

    @Desc("客户名称")
    @Column
    @Index(name = "CONSUMERNAME")
    private String CONSUMERNAME;

    @Desc("产品规格")
    @Column
    @Index(name = "productModel")
    private String productModel;

    @Desc("产品质量等级")
    @Column
    private String rollQualityGradeCode;

    @Desc("批次号")
    @Column
    @Index(name = "batchCode")
    private String batchCode;

    @Desc("卷重")
    @Column
    private Double rollWeight;

    @Desc("计划单号")
    @Column
    @Index(name = "producePlanCode")
    private String producePlanCode;

    @Desc("订单号")
    @Column
    @Index(name = "salesOrderCode")
    private String salesOrderCode;

    @Desc("出产时间")
    @Column
    @Index(name = "rollOutputTime")
    private Date rollOutputTime;

    @Desc("入库时间")
    @Column
    @Index(name = "inTime")
    private Date inTime;

    @Desc("出库时间")
    @Column
    @Index(name = "outTime")
    private Date outTime;

    @Desc("出库时间")
    @Column
    @Index(name = "isTurnBag")
    private Integer isTurnBag;

    @Desc("机台号")
    @Column
    @Index(name = "deviceCode")
    private String deviceCode;

    @Desc("车间")
    @Column
    @Index(name = "name")
    private String name;

    @Desc("操作人")
    @Column
    @Index(name = "loginName")
    private String loginName;

    /**
     * -1出库，0未入库，1在库
     */
    @Desc("在库状态")
    @Column
    @Index(name = "state")
    private Integer state;

    /**
     * -1正常，1冻结
     */
    @Desc("是否冻结")
    @Column
    @Index(name = "isLocked")
    private Integer isLocked;

    @Desc("产品名称")
    @Column
    @Index(name = "productName")
    private String productName;

    @Desc("卷长")
    @Column
    private Double productLength;

    @Desc("产品重量")
    @Column
    private Double productWeight;

    @Desc("门幅")
    @Column
    private Double productWidth;

    @Desc("谁冻结")
    @Column
    private String isNameLock;

    /**
     * 1已打包，0未打包
     */
    @Desc("是否打包")
    @Column
    @Index(name = "isPacked")
    private Integer isPacked;

    @Desc("车间代码")
    @Column
    @Index(name = "workShopCode")
    private String workShopCode;

    @Desc("叶型")
    @Column
    @Index(name = "bladeProfile")
    private String bladeProfile;

    public String getWorkShopCode() {
        return workShopCode;
    }

    public void setWorkShopCode(String workShopCode) {
        this.workShopCode = workShopCode;
    }

    public Integer getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(Integer isPacked) {
        this.isPacked = isPacked;
    }

    public String getIsNameLock() {
        return isNameLock;
    }

    public void setIsNameLock(String isNameLock) {
        this.isNameLock = isNameLock;
    }

    public Integer getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public String getRollBarcode() {
        return rollBarcode;
    }

    public void setRollBarcode(String rollBarcode) {
        this.rollBarcode = rollBarcode;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public String getCONSUMERNAME() {
        return CONSUMERNAME;
    }

    public void setCONSUMERNAME(String cONSUMERNAME) {
        CONSUMERNAME = cONSUMERNAME;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Double getRollWeight() {
        return rollWeight;
    }

    public void setRollWeight(Double rollWeight) {
        this.rollWeight = rollWeight;
    }

    public String getProducePlanCode() {
        return producePlanCode;
    }

    public void setProducePlanCode(String producePlanCode) {
        this.producePlanCode = producePlanCode;
    }

    public String getSalesOrderCode() {
        return salesOrderCode;
    }

    public void setSalesOrderCode(String salesOrderCode) {
        this.salesOrderCode = salesOrderCode;
    }

    public Date getRollOutputTime() {
        return rollOutputTime;
    }

    public void setRollOutputTime(Date rollOutputTime) {
        this.rollOutputTime = rollOutputTime;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * **get**
     */
    public String getProductName() {
        return productName;
    }

    /**
     * **set**
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * **get**
     */
    public Double getProductLength() {
        return productLength;
    }

    /**
     * **set**
     */
    public void setProductLength(Double productLength) {
        this.productLength = productLength;
    }

    /**
     * **get**
     */
    public Double getProductWeight() {
        return productWeight;
    }

    /**
     * **set**
     */
    public void setProductWeight(Double productWeight) {
        this.productWeight = productWeight;
    }

    /**
     * **get**
     */
    public Double getProductWidth() {
        return productWidth;
    }

    /**
     * **set**
     */
    public void setProductWidth(Double productWidth) {
        this.productWidth = productWidth;
    }

    public String getRollQualityGradeCode() {
        return rollQualityGradeCode;
    }

    public void setRollQualityGradeCode(String rollQualityGradeCode) {
        this.rollQualityGradeCode = rollQualityGradeCode;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public Integer getIsTurnBag() {
        return isTurnBag;
    }

    public void setIsTurnBag(Integer isTurnBag) {
        this.isTurnBag = isTurnBag;
    }

    public String getBladeProfile() {
        return bladeProfile;
    }

    public void setBladeProfile(String bladeProfile) {
        this.bladeProfile = bladeProfile;
    }
}

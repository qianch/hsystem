package com.bluebirdme.mes.tracings.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("生产追溯日志")
@Entity
@Table(name = "hs_TracingLog")
public class TracingLog extends BaseEntity {
    /**
     * 通用字段 1：销售 2：计调 3：排产 4：产出 5：打包 6：入库 7：出库 8：翻包 9：冻结10：解冻11投料
     */
    @Desc("产品ID")
    @Index(name = "productId")
    private Long productId;
    @Desc("日志类型")
    @Index(name = "logType")
    private Integer logType;
    @Desc("触发设备")
    private String deviceCode;
    @Desc("触发人员")
    private String userName;
    @Desc("触发时间")
    private Date triggerDate;
    @Desc("触发详情")
    private String triggerInfo;
    @Desc("订单号")
    @Index(name = "salesCode")
    private String salesCode;
    // 计调字段
    @Desc("生产计划单号")
    private String planCode;
    // 排产字段
    @Desc("日生产计划单号")
    private String dailyPlanCode;
    // 产出字段
    @Desc("批次号")
    private String batchCode;
    @Desc("卷条码")
    @Index(name = "rollBarcode")
    private String rollBarcode;
    @Desc("部件条码")
    @Index(name = "partBarcode")
    private String partBarcode;
    @Desc("产品规格")
    @Index(name = "productModel")
    private String productModel;
    // 打包字段
    @Desc("箱条码")
    @Index(name = "boxBarcode")
    private String boxBarcode;
    @Desc("托条码")
    @Index(name = "trayBarcode")
    private String trayBarcode;
    // 库房字段
    @Desc("库房")
    private String stockCode;
    @Desc("库位")
    private String stockPosition;
    // 翻包字段
    @Desc("来自订单")
    private String reSaleCode;

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * **get**
     */
    public Date getTriggerDate() {
        return triggerDate;
    }

    /**
     * **set**
     */
    public void setTriggerDate(Date triggerDate) {
        this.triggerDate = triggerDate;
    }

    public String getTriggerInfo() {
        return triggerInfo;
    }

    public void setTriggerInfo(String triggerInfo) {
        this.triggerInfo = triggerInfo;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getDailyPlanCode() {
        return dailyPlanCode;
    }

    public void setDailyPlanCode(String dailyPlanCode) {
        this.dailyPlanCode = dailyPlanCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getRollBarcode() {
        return rollBarcode;
    }

    public void setRollBarcode(String rollBarcode) {
        this.rollBarcode = rollBarcode;
    }

    public String getPartBarcode() {
        return partBarcode;
    }

    public void setPartBarcode(String partBarcode) {
        this.partBarcode = partBarcode;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getBoxBarcode() {
        return boxBarcode;
    }

    public void setBoxBarcode(String boxBarcode) {
        this.boxBarcode = boxBarcode;
    }

    public String getTrayBarcode() {
        return trayBarcode;
    }

    public void setTrayBarcode(String trayBarcode) {
        this.trayBarcode = trayBarcode;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockPosition() {
        return stockPosition;
    }

    public void setStockPosition(String stockPosition) {
        this.stockPosition = stockPosition;
    }

    public String getReSaleCode() {
        return reSaleCode;
    }

    public void setReSaleCode(String reSaleCode) {
        this.reSaleCode = reSaleCode;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}

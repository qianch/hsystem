package com.bluebirdme.mes.store.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bluebirdme.mes.printer.entity.BarCodePrintRecord;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 托条码
 *
 * @author 宋黎明
 * @Date 2016年11月08日 上午10:02:34
 */
@Desc("托条码")
@Entity
@Table(name = "hs_Tray_BarCode")
@DynamicInsert
public class TrayBarCode extends BaseEntity implements IBarcode {
    @Desc("托条码号")
    @Column(unique = true)
    @Index(name = "barcode")
    private String barcode;

    @Desc("计划发货日期")
    @Column
    private Date planDeliveryDate;

    @Index(name = "salesOrderDetailId")
    @Desc("订单明细ID")
    @Column
    private Long salesOrderDetailId;

    @Desc("订单号")
    @Column
    private String salesOrderCode;

    @Desc("批次号")
    @Column
    private String batchCode;
    @Desc("生产计划ID")
    // 可能是编织或者裁剪计划
    private Long planId;

    @Desc("生产计划明细ID")
    @Column
    private Long producePlanDetailId;

    @Desc("打印时间")
    @Column
    private Date printDate;

    @Desc("输出字符串")
    @Column
    private String outputString;

    @Desc("产品部件名称")
    @Column
    private String partName;

    @Desc("产品ID")
    @Column
    private Long salesProductId;

    @Desc("部件ID")
    private Long partId;

    /**
     * 1:拆包，0：正常
     */
    @Desc("被拆包")
    @Column(columnDefinition = "int(2) default 0")
    private Integer isOpened;

    @Desc("属于哪个订单")
    @Column(nullable = true)
    private Long belongToSalesOrderId;

    @Desc("翻包计划ID")
    private Long turnBagPlanId;

    @Desc("每托卷数")
    @Column
    private Integer rollCountPerTray;

    @Desc("旧订单号")
    @Column
    private String oldSalesOrder;

    @Desc("旧批次号")
    @Column
    private String oldBatchCode;

    @Desc("翻包日期")
    @Column
    private String turnBagDate;

    @Desc("翻包操作人")
    @Column
    private String turnBagUser;

    @Desc("标签模版ID")
    private Long btwfileId;

    @Desc("成品类型 0：成品  2：胚布")
    private Integer stockType;

    @Desc("镜像版本id")
    @Column
    private Long mirrorProcBomId;

    @Desc("输出个性化标签字符串")
    @Column
    private String individualOutputString;

    @Override
    public Long getMirrorProcBomId() {
        return mirrorProcBomId;
    }

    @Override
    public void setIndividualOutPutString(String individualOutputString) {
        this.individualOutputString = individualOutputString;
    }

    @Override
    public void setMirrorProcBomId(Long mirrorProcBomId) {
        this.mirrorProcBomId = mirrorProcBomId;
    }

    public Integer getStockType() {
        return stockType;
    }

    public void setStockType(Integer stockType) {
        this.stockType = stockType;
    }

    public Integer getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(Integer isOpened) {
        this.isOpened = isOpened;
    }

    public Long getBelongToSalesOrderId() {
        return belongToSalesOrderId;
    }

    public void setBelongToSalesOrderId(Long belongToSalesOrderId) {
        this.belongToSalesOrderId = belongToSalesOrderId;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    /**
     * **get**
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * **set**
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * **get**
     */
    public String getSalesOrderCode() {
        return salesOrderCode;
    }

    /**
     * **set**
     */
    public void setSalesOrderCode(String salesOrderCode) {
        this.salesOrderCode = salesOrderCode;
    }

    @Override
    public Long getSalesProductId() {
        return salesProductId;
    }

    public String getOutputString() {
        return outputString;
    }

    public void setOutputString(String outputString) {
        this.outputString = outputString;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    @Override
    public void setOutPutString(String outputString) {
        this.outputString = outputString;
    }

    @Override
    public String getIndividualOutPutString() {
        return individualOutputString;
    }

    @Override
    public String getOutPutString() {
        return outputString;
    }

    @Override
    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    @Override
    public String getBatchCode() {
        return this.batchCode;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void setSalesProductId(Long salesProductId) {
        this.salesProductId = salesProductId;
    }

    public Long getSalesOrderDetailId() {
        return salesOrderDetailId;
    }

    public void setSalesOrderDetailId(Long salesOrderDetailId) {
        this.salesOrderDetailId = salesOrderDetailId;
    }

    public Date getPlanDeliveryDate() {
        return planDeliveryDate;
    }

    public void setPlanDeliveryDate(Date planDeliveryDate) {
        this.planDeliveryDate = planDeliveryDate;
    }

    public Long getTurnBagPlanId() {
        return turnBagPlanId;
    }

    public void setTurnBagPlanId(Long turnBagPlanId) {
        this.turnBagPlanId = turnBagPlanId;
    }

    public Long getProducePlanDetailId() {
        return producePlanDetailId;
    }

    public void setProducePlanDetailId(Long producePlanDetailId) {
        this.producePlanDetailId = producePlanDetailId;
    }

    public Integer getRollCountPerTray() {
        return rollCountPerTray;
    }

    public void setRollCountPerTray(Integer rollCountPerTray) {
        this.rollCountPerTray = rollCountPerTray;
    }

    public String getOldSalesOrder() {
        return oldSalesOrder;
    }

    public void setOldSalesOrder(String oldSalesOrder) {
        this.oldSalesOrder = oldSalesOrder;
    }

    public String getOldBatchCode() {
        return oldBatchCode;
    }

    public void setOldBatchCode(String oldBatchCode) {
        this.oldBatchCode = oldBatchCode;
    }

    public String getTurnBagDate() {
        return turnBagDate;
    }

    public void setTurnBagDate(String turnBagDate) {
        this.turnBagDate = turnBagDate;
    }

    public String getTurnBagUser() {
        return turnBagUser;
    }

    public void setTurnBagUser(String turnBagUser) {
        this.turnBagUser = turnBagUser;
    }

    public Long getBtwfileId() {
        return btwfileId;
    }

    public void setBtwfileId(Long btwfileId) {
        this.btwfileId = btwfileId;
    }

    @Desc("客户条码")
    @Column
    private String customerBarCode;

    @Override
    public String getCustomerBarCode() {
        return customerBarCode;
    }

    @Override
    public void setCustomerBarCode(String customerBarCode) {
        this.customerBarCode = customerBarCode;
    }

    @Desc("经销商条码")
    @Column
    private String agentBarCode;

    @Override
    public String getAgentBarCode() {
        return agentBarCode;
    }

    @Override
    public void setAgentBarCode(String agentBarCode) {
        this.agentBarCode = agentBarCode;
    }
}

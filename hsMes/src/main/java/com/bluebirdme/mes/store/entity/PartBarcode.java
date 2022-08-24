package com.bluebirdme.mes.store.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 部件条码
 *
 * @author 宋黎明
 * @Date 2016年11月08日 上午10:02:34
 */
@Desc("部件条码")
@Entity
@Table(name = "hs_Part_Barcode")
@DynamicInsert
public class PartBarcode extends BaseEntity implements IBarcode {
    @Desc("订单号")
    @Column
    @Index(name = "salesOrderCode")
    private String salesOrderCode;

    @Desc("子订单号")
    private Long salesProductId;

    @Desc("订单明细ID")
    @Column
    private Long salesOrderDetailId;

    @Desc("产品部件名称")
    @Column
    private String partName;

    @Desc("条码号")
    @Column
    @Index(name = "barcode")
    private String barcode;

    @Desc("批次号")
    @Column
    private String batchCode;

    @Desc("打印时间")
    @Column
    private Date printDate;

    @Desc("输出字符串")
    private String outputString;
    @Desc("旧订单号")
    @Column
    private String oldSalesOrder;


    @Desc("被拆包")
    @Column(columnDefinition = "int(2) default 0")
    private Integer isOpened;

    @Desc("旧批次号")
    @Column
    private String oldBatchCode;

    @Desc("旧条码号")
    @Column
    private String oldbarcode;

    @Desc("翻包日期")
    @Column
    private String turnBagDate;

    @Desc("翻包操作人")
    @Column
    private String turnBagUser;
    /**
     * 裁剪计划ID
     */
    @Desc("生产计划ID")
    @Index(name = "planId")
    private Long planId;

    @Desc("部件ID")
    private Long partId;

    @Desc("生产计划明细ID")
    @Column
    private Long producePlanDetailId;

    /**
     * 0:正常，1:作废
     */
    @Desc("作废")
    @Column(columnDefinition = "int(2) default 0")
    private Integer isAbandon;

    @Desc("标签模版ID")
    private Long btwfileId;

    @Desc("成品类型 0：成品  2：胚布")
    private Integer stockType;

    public String getOldbarcode() {
        return oldbarcode;
    }

    public void setOldbarcode(String oldbarcode) {
        this.oldbarcode = oldbarcode;
    }

    @Desc("镜像版本id")
    @Column
    private Long mirrorProcBomId;

    @Desc("输出个性化标签字符串")
    private String individualOutputString;

    @Override
    public Long getMirrorProcBomId() {
        return mirrorProcBomId;
    }

    @Override
    @Column
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

    public Integer getIsAbandon() {
        return isAbandon;
    }

    public void setIsAbandon(Integer isAbandon) {
        this.isAbandon = isAbandon;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getOutputString() {
        return outputString;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Long getSalesProductId() {
        return salesProductId;
    }

    public void setSalesProductId(Long salesProductId) {
        this.salesProductId = salesProductId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long plannerDetailId) {
        this.planId = plannerDetailId;
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

    /**
     * **get**
     */
    public String getBarcode() {
        return barcode;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    /**
     * **set**
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setOutputString(String outputString) {
        this.outputString = outputString;
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

    public void setProductId(Long id) {
        this.salesProductId = id;
    }

    public Long getProductId() {
        return this.salesProductId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Long getSalesOrderDetailId() {
        return salesOrderDetailId;
    }

    public void setSalesOrderDetailId(Long salesOrderDetailId) {
        this.salesOrderDetailId = salesOrderDetailId;
    }

    public Long getProducePlanDetailId() {
        return producePlanDetailId;
    }

    public void setProducePlanDetailId(Long producePlanDetailId) {
        this.producePlanDetailId = producePlanDetailId;
    }

    public Long getBtwfileId() {
        return btwfileId;
    }

    public void setBtwfileId(Long btwfileId) {
        this.btwfileId = btwfileId;
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

    public Integer getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(Integer isOpened) {
        this.isOpened = isOpened;
    }

    public void setTurnBagUser(String turnBagUser) {
        this.turnBagUser = turnBagUser;
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

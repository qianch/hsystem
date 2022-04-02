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
 * 箱条码
 *
 * @author 宋黎明
 * @Date 2016年11月08日 上午10:02:34
 */
@Desc("箱条码")
@Entity
@Table(name = "hs_Box_Barcode")
@DynamicInsert
public class BoxBarcode extends BaseEntity implements IBarcode {
    @Desc("条码号")
    @Column
    @Index(name = "barcode")
    private String barcode;

    @Desc("输出字符串")
    private String outputString;

    @Desc("订单号")
    @Column
    private String salesOrderCode;

    @Desc("产品ID")
    @Column
    private Long salesProductId;

    @Desc("订单明细ID")
    @Column
    private Long salesOrderDetailId;

    @Desc("计划ID")
    private Long planId;

    @Desc("生产计划明细ID")
    @Column
    private Long producePlanDetailId;


    @Desc("批次号")
    @Column
    private String batchCode;

    @Desc("打印时间")
    @Column
    private Date printDate;

    @Desc("部件名称")
    @Column
    private String partName;

    @Desc("部件ID")
    private Long partId;

    @Desc("被拆包")
    @Column(columnDefinition = "int(2) default 0")
    private Integer isOpened;

    @Desc("标签模版ID")
    private Long btwfileId;

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

    public Integer getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(Integer isOpened) {
        this.isOpened = isOpened;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
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

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public String getOutputString() {
        return outputString;
    }

    public void setOutputString(String outputString) {
        this.outputString = outputString;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
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

    public Long getSalesProductId() {
        return salesProductId;
    }

    public void setSalesProductId(Long salesProductId) {
        this.salesProductId = salesProductId;
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

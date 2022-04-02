package com.bluebirdme.mes.planner.weave.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntityExt;
import com.bluebirdme.mes.planner.cut.entity.Iplan;

@Desc("编织计划")
@Entity
@Table(name = "HS_Weave_Plan")
@DynamicInsert
public class WeavePlan extends BaseEntityExt implements Iplan {
	@Column
	@Desc("产品id")
	private Long productId;
	@Column
	@Desc("任务单号")
	@Index(name="planCode")
	private String planCode;
	@Column
	@Desc("客户id")
	private Long consumerId;
	@Column
	@Desc("客户名称")
	@Index(name="consumerName")
	private String consumerName;
	@Column
	@Desc("订单号")
	@Index(name="salesOrderCode")
	private String salesOrderCode;
	@Column
	@Desc("批次号")
	@Index(name="batchCode")
	private String batchCode;
	@Column
	@Desc("产品规格")
	@Index(name="productModel")
	private String productModel;
	@Column
	@Desc("产品名称")
	private String productName;
	@Column
	@Desc("门幅")
	@Index(name="productWidth")
	private Double productWidth;
	@Column
	@Desc("卷重")
	private Double productWeight;
	@Column
	@Desc("卷长")
	private Double productLength;
	@Column
	@Desc("订单数量(kg/套)")
	// 套材为套，非套材为kg
	private Double orderCount;
	@Column
	@Desc("工艺版本")
	private String processBomVersion;
	@Column
	@Desc("工艺代码")
	private String processBomCode;
	@Column
	@Desc("包装版本")
	private String bcBomVersion;
	@Column
	@Desc("包装代码")
	private String bcBomCode;
	@Column
	@Desc("总卷数")
	private Integer totalRollCount=0;
	@Column
	@Desc("总托数")
	private Integer totalTrayCount=0;
	@Column
	@Desc("需要生产数量(kg/套)")
	private Double requirementCount;

	@Desc("已生产卷数")
	@Column
	private Double produceRollCount;

	@Desc("已打包托数")
	private Double produceTrayCount;

	@Desc("已生产重量")
	private Double producedTotalWeight;

	@Column
	@Desc("出货日期")
	private Date deleveryDate;
	@Column
	@Desc("机台号")
	private String deviceCode;
	@Column
	@Desc("备注")
	private String comment;
	@Column
	@Desc("是否已完成")
	@Index(name="isFinished")
	private Integer isFinished;
	/*
	 * 1:大卷
	 * 2:中卷
	 * 3:小卷
	 * 4:其他
	 */
	@Column
	@Desc("产品属性")
	private Integer productType=-1;
	@Column
	@Desc("生产计划明细Id")
	private Long producePlanDetailId;
	@Column
	@Desc("打包数量")
	private Integer packagedCount;
	@Column
	@Desc("来自套材")
	private Long fromTcId;
	@Column
	@Desc("来自套材名称")
	private String fromTcName;
	@Column
	@Desc("来自的订单明细ID")
	private Long fromSalesOrderDetailId;
	@Desc("包装需求")
	@Column
	private String packReq;
	@Desc("工艺需求")
	@Column
	private String procReq;


	@Desc("是否已排产")
	@Column(columnDefinition="int(2) default 0")
	@Index(name="isSettled")
	private Integer isSettled=0;

	@Desc("工艺版本ID")
	private Long procBomId;
	@Desc("包装版本ID")
	private Long packBomId;
	/**
	 * 0，null:未关闭
	 * 1：已关闭
	 */
	@Desc("是否已关闭")
	@Column
	private Integer closed=0;


	@Desc("是否打印")
	@Column(columnDefinition="int(2) default 0")
	private Integer isStamp=0;

	@Desc("总重量")
	@Column(columnDefinition="double default 0")
	private Double sumWeight;

	@Desc("总个数")
	@Column(columnDefinition="int default 0")
	private Integer sumCount;

	@Desc("作废数")
	@Column(columnDefinition="int default 0")
	private Integer toVoid;

	@Desc("作废重量")
	@Column(columnDefinition="double default 0")
	private Double toVoidWeight;



	@Desc("镜像版本id")
	@Column
	private Long mirrorProcBomId;

	@Override
	public Long getMirrorProcBomId() {
		return mirrorProcBomId;
	}

	public void setMirrorProcBomId(Long mirrorProcBomId) {
		this.mirrorProcBomId = mirrorProcBomId;
	}

	public Double getToVoidWeight() {
		return toVoidWeight;
	}

	public void setToVoidWeight(Double toVoidWeight) {
		this.toVoidWeight = toVoidWeight;
	}

	public Integer getToVoid() {
		return toVoid;
	}

	public void setToVoid(Integer toVoid) {
		this.toVoid = toVoid;
	}



	public Double getSumWeight() {
		return sumWeight;
	}

	public void setSumWeight(Double sumWeight) {
		this.sumWeight = sumWeight;
	}

	public Integer getSumCount() {
		return sumCount;
	}

	public void setSumCount(Integer sumCount) {
		this.sumCount = sumCount;
	}

	public Integer getClosed() {
		return closed;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public Long getProcBomId() {
		return procBomId;
	}

	public void setProcBomId(Long procBomId) {
		this.procBomId = procBomId;
	}

	public Long getPackBomId() {
		return packBomId;
	}

	public void setPackBomId(Long packBomId) {
		this.packBomId = packBomId;
	}

	public Integer getIsSettled() {
		return isSettled;
	}

	public void setIsSettled(Integer isSettled) {
		this.isSettled = isSettled;
	}


	public String getPackReq() {
		return packReq;
	}

	public void setPackReq(String packReq) {
		this.packReq = packReq;
	}

	public String getProcReq() {
		return procReq;
	}

	public void setProcReq(String procReq) {
		this.procReq = procReq;
	}

	public Double getProducedTotalWeight() {
		return producedTotalWeight;
	}

	public void setProducedTotalWeight(Double producedTotalWeight) {
		this.producedTotalWeight = producedTotalWeight;
	}

	public Double getProduceRollCount() {
		return produceRollCount;
	}

	public void setProduceRollCount(Double produceRollCount) {
		this.produceRollCount = produceRollCount;
	}

	public Double getProduceTrayCount() {
		return produceTrayCount;
	}

	public void setProduceTrayCount(Double produceTrayCount) {
		this.produceTrayCount = produceTrayCount;
	}

	public Long getFromSalesOrderDetailId() {
		return fromSalesOrderDetailId;
	}

	public void setFromSalesOrderDetailId(Long fromSalesOrderDetailId) {
		this.fromSalesOrderDetailId = fromSalesOrderDetailId;
	}


	public String getProcessBomVersion() {
		return processBomVersion;
	}

	public void setProcessBomVersion(String processBomVersion) {
		this.processBomVersion = processBomVersion;
	}

	public String getProcessBomCode() {
		return processBomCode;
	}

	public void setProcessBomCode(String processBomCode) {
		this.processBomCode = processBomCode;
	}

	public Integer getTotalRollCount() {
		return totalRollCount;
	}

	public void setTotalRollCount(Integer totalRollCount) {
		this.totalRollCount = totalRollCount;
	}

	public Integer getTotalTrayCount() {
		return totalTrayCount;
	}

	public void setTotalTrayCount(Integer totalTrayCount) {
		this.totalTrayCount = totalTrayCount;
	}

	public Long getFromTcId() {
		return fromTcId;
	}

	public void setFromTcId(Long fromTcId) {
		this.fromTcId = fromTcId;
	}

	public String getFromTcName() {
		return fromTcName;
	}

	public void setFromTcName(String fromTcName) {
		this.fromTcName = fromTcName;
	}

	public Integer getPackagedCount() {
		return packagedCount;
	}

	public void setPackagedCount(Integer packagedCount) {
		this.packagedCount = packagedCount;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Integer getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Integer isFinished) {
		this.isFinished = isFinished;
	}


	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public Long getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Long consumerId) {
		this.consumerId = consumerId;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getSalesOrderCode() {
		return salesOrderCode;
	}

	public void setSalesOrderCode(String salesOrderCode) {
		this.salesOrderCode = salesOrderCode;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getProductWidth() {
		return productWidth;
	}

	public void setProductWidth(Double productWidth) {
		this.productWidth = productWidth;
	}

	public Double getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(Double productWeight) {
		this.productWeight = productWeight;
	}

	public Double getProductLength() {
		return productLength;
	}

	public void setProductLength(Double productLength) {
		this.productLength = productLength;
	}

	public Double getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Double orderCount) {
		this.orderCount = orderCount;
	}

	public String getBcBomVersion() {
		return bcBomVersion;
	}

	public void setBcBomVersion(String bcBomVersion) {
		this.bcBomVersion = bcBomVersion;
	}

	public String getBcBomCode() {
		return bcBomCode;
	}

	public void setBcBomCode(String bcBomCode) {
		this.bcBomCode = bcBomCode;
	}

	public Double getRequirementCount() {
		return requirementCount;
	}

	public void setRequirementCount(Double requirementCount) {
		this.requirementCount = requirementCount;
	}


	public Date getDeleveryDate() {
		return deleveryDate;
	}

	public void setDeleveryDate(Date deleveryDate) {
		this.deleveryDate = deleveryDate;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Long getProducePlanDetailId() {
		return producePlanDetailId;
	}

	public void setProducePlanDetailId(Long producePlanDetailId) {
		this.producePlanDetailId = producePlanDetailId;
	}

	@Override
	public Double getProducedCount() {
		return null;
	}

	public Integer getIsStamp() {
		return isStamp;
	}

	public void setIsStamp(Integer isStamp) {
		this.isStamp = isStamp;
	}

	@Override
	public Long getSort() {
		return null;
	}


    @Desc("打印订单信息内容")
    @Column
    private String printOrderContent;
    public String getPrintOrderContent() {
        return printOrderContent;
    }

    public void setPrintOrderContent(String printOrderContent) {
        this.printOrderContent = printOrderContent;
    }

}

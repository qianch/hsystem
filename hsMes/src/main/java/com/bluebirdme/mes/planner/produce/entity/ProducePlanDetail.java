package com.bluebirdme.mes.planner.produce.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntityExt;

@Desc("生产计划明细")
@Entity
@Table(name = "HS_Produce_Plan_Detail")
public class ProducePlanDetail extends BaseEntityExt {

	@Desc("生产计划ID")
	@Column
	@Index(name="producePlanId")
	private Long producePlanId;

	@Desc("产品id")
	@Column
	private Long productId;

	@Desc("任务单号")
	@Column
	@Index(name="planCode")
	private String planCode;
	@Desc("客户id")
	@Column
	private Long consumerId;
	@Desc("客户名称")
	@Index(name="consumerName")
	private String consumerName;
	@Column
	@Desc("订单号")
	@Index(name="salesOrderCode")
	private String salesOrderCode;
	@Column
	@Desc("客户订单号")
	@Index(name="salesOrderSubcodePrint")
	private String salesOrderSubcodePrint;
	

	@Column
	@Desc("批次号")
	private String batchCode;
	@Column
	@Desc("厂内名称")
	private String factoryProductName;
	@Column
	@Desc("产品名称")
	private String consumerProductName;
	@Column
	@Desc("产品规格")
	@Index(name="productModel")
	private String productModel;
	@Column
	@Desc("门幅")
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
	
	@Desc("订单总重")//米（非套材）、卷(非套材)、重量KG（非套材） 、套（套材）
	@Column
	private String orderTotalWeight;
	
	@Desc("订单总米长")
	private Double orderTotalMetres;
	
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
	@Desc("计划数量")
	private Double requirementCount;
	
	@Column
	@Desc("计划总重量")
	private Double planTotalWeight;
	
	@Column
	@Desc("计划数量单位")//套数、卷数
	private String planMainUnit;
	
	@Column
	@Desc("辅助数量")
	private Double planAssistCount;
	
	@Column
	@Desc("辅助数量")
	private String planAssistUnit;
	
	@Deprecated
	@Column
	@Desc("总卷数")
	private Integer totalRollCount;
	
	@Deprecated
	@Column
	@Desc("总托数")
	private Integer totalTrayCount;
	
	
	@Column
	@Desc("已生产数量(kg/套)")
	private Double producedCount;
	
	@Desc("已生产的卷数")
	@Column
	private Integer producedRolls;
	
	@Column
	@Desc("打包数量")
	private Integer packagedCount;
	
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
	@Column
	@Desc("排序")
	private Long sort;
	
	@Column
	@Desc("产品属性")
	@Index(name="productType")
	private Integer productType;
	
	@Column
	@Desc("来自套材ID")
	private Long fromTcId;
	@Column
	@Desc("来自套材名称")
	private String fromTcName;
	
	@Column
	@Desc("来自的订单明细ID")
	@Index(name="fromSalesOrderDetailId")
	private Long fromSalesOrderDetailId;
	
	//1:套材，其他：非套材
	@Desc("是否套材")
	@Column
	private Integer productIsTc;
	
	@Desc("包装需求")
	@Column(length = 16777215)
	private String packReq;
	
	
	@Desc("工艺需求")
	@Column(length = 16777215)
	private String procReq;
	
	@Desc("包装版本ID")
	private Long packBomId;
	@Desc("工艺版本ID")
	private Long procBomId;
	/**
	 * 0，null:未关闭
	 * 1：已关闭
	 */
	@Desc("是否已关闭")
	@Column
	private Integer closed;
	
	/**
	 * 1:翻包计划
	 * 0：非翻包计划
	 */
	@Desc("是否翻包计划")
	@Column(columnDefinition="varchar(12) default '生产'")
	private String isTurnBagPlan;
	
	@Desc("翻包单号")
	@Column
	private String turnBagCode;


	@Desc("镜像bom版本id")
	@Column
	private Long mirrorProcBomId;

	@Desc("镜像来自套材ID")
	@Column
	private Long mirrorFromTcId;

	@Desc("检测状态")
	@Column
	private Integer productStatus;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column
	@Desc("产品名称2")
	private String productName;



	public Integer getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}

	public Long getMirrorFromTcId() {
		return mirrorFromTcId;
	}

	public void setMirrorFromTcId(Long mirrorFromTcId) {
		this.mirrorFromTcId = mirrorFromTcId;
	}


	public Long getMirrorProcBomId() {
		return mirrorProcBomId;
	}

	public void setMirrorProcBomId(Long mirrorProcBomId) {
		this.mirrorProcBomId = mirrorProcBomId;
	}

	@Transient
	private List<ProducePlanDetailPartCount> list;
	
	public String getSalesOrderSubcodePrint() {
		return salesOrderSubcodePrint;
	}

	public void setSalesOrderSubcodePrint(String salesOrderSubcodePrint) {
		this.salesOrderSubcodePrint = salesOrderSubcodePrint;
	}
	
	public Integer getClosed() {
		return closed;
	}
	
	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public Long getPackBomId() {
		return packBomId;
	}

	public void setPackBomId(Long packBomId) {
		this.packBomId = packBomId;
	}

	public Long getProcBomId() {
		return procBomId;
	}

	public void setProcBomId(Long procBomId) {
		this.procBomId = procBomId;
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
	public Long getProducePlanId() {
		return producePlanId;
	}

	public void setProducePlanId(Long producePlanId) {
		this.producePlanId = producePlanId;
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

	public String getFactoryProductName() {
		return factoryProductName;
	}

	public void setFactoryProductName(String factoryProductName) {
		this.factoryProductName = factoryProductName;
	}

	public String getConsumerProductName() {
		return consumerProductName;
	}

	public void setConsumerProductName(String consumerProductName) {
		this.consumerProductName = consumerProductName;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
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

	public Double getRequirementCount() {
		return requirementCount;
	}

	public void setRequirementCount(Double requirementCount) {
		this.requirementCount = requirementCount;
	}

	public Double getProducedCount() {
		return producedCount;
	}

	public void setProducedCount(Double producedCount) {
		this.producedCount = producedCount;
	}

	public Integer getProducedRolls() {
		return producedRolls;
	}

	public void setProducedRolls(Integer producedRolls) {
		this.producedRolls = producedRolls;
	}

	public Integer getPackagedCount() {
		return packagedCount;
	}

	public void setPackagedCount(Integer packagedCount) {
		this.packagedCount = packagedCount;
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

	public Integer getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Integer isFinished) {
		this.isFinished = isFinished;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
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

	public Long getFromSalesOrderDetailId() {
		return fromSalesOrderDetailId;
	}

	public void setFromSalesOrderDetailId(Long fromSalesOrderDetailId) {
		this.fromSalesOrderDetailId = fromSalesOrderDetailId;
	}

	public Integer getProductIsTc() {
		return productIsTc;
	}

	public void setProductIsTc(Integer productIsTc) {
		this.productIsTc = productIsTc;
	}

	public List<ProducePlanDetailPartCount> getList() {
		return list;
	}

	public void setList(List<ProducePlanDetailPartCount> list) {
		this.list = list;
	}


	public String getPlanMainUnit() {
		return planMainUnit;
	}

	public void setPlanMainUnit(String planMainUnit) {
		this.planMainUnit = planMainUnit;
	}

	public Double getPlanAssistCount() {
		return planAssistCount;
	}

	public void setPlanAssistCount(Double planAssistCount) {
		this.planAssistCount = planAssistCount;
	}

	public String getPlanAssistUnit() {
		return planAssistUnit;
	}

	public void setPlanAssistUnit(String planAssistUnit) {
		this.planAssistUnit = planAssistUnit;
	}

	public String getOrderTotalWeight() {
		return orderTotalWeight;
	}

	public void setOrderTotalWeight(String orderTotalWeight) {
		this.orderTotalWeight = orderTotalWeight;
	}

	public Double getOrderTotalMetres() {
		return orderTotalMetres;
	}

	public void setOrderTotalMetres(Double orderTotalMetres) {
		this.orderTotalMetres = orderTotalMetres;
	}

	public Double getPlanTotalWeight() {
		return planTotalWeight;
	}

	public void setPlanTotalWeight(Double planTotalWeight) {
		this.planTotalWeight = planTotalWeight;
	}

	public String getIsTurnBagPlan() {
		return isTurnBagPlan==null?"生产":isTurnBagPlan;
	}

	public void setIsTurnBagPlan(String isTurnBagPlan) {
		this.isTurnBagPlan = isTurnBagPlan;
	}

	public String getTurnBagCode() {
		return turnBagCode;
	}

	public void setTurnBagCode(String turnBagCode) {
		this.turnBagCode = turnBagCode;
	}

}

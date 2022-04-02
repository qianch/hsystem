package com.bluebirdme.mes.complaint.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.core.dev.DevHelper;

/**
 * @author Goofy
 * @Date 2016年11月25日 下午3:37:04
 */
@Desc("投诉")
@Entity
@Table(name = "HS_COMPLAINT")
public class Complaint extends BaseEntity {
	
	@Desc("投诉代码")
	@Column
	@Index(name="complaintCode")
	private String complaintCode;
	
	@Desc("8D报告")
	@Column
	private String d8Report;
	
	@Desc("客户来源")
	@Column
	private String consumerFrom;
	@Desc("客户ID")
	private Long complaintConsumerId;
	@Desc("客户名称")
	@Index(name="consumerName")
	@Column
	private String consumerName;
	
	@Desc("基地")
	@Column
	private String basePlace;
	
	@Desc("投诉日期")
	@Column
	private Date complaintDate;
	
	@Desc("信息来源")
	@Column
	@Index(name="infoFrom")
	private String infoFrom;
	
	@Desc("供货规格")
	@Column
	@Index(name="productModel")
	private String productModel;
	
	@Desc("供货状态")
	@Column
	private String supplyState;
	
	@Desc("出货数量")
	@Column(length=2000)
	private String supplyCount;
	
	@Desc("发货日期")
	@Column
	@Index(name="deliveryDate")
	private String deliveryDate;
	
	@Desc("生成日期")
	@Column
	private String produceDate;
	
	@Desc("问题描述")
	@Column(length=3000)
	private String questionDesc;
	
	@Desc("生产单位")
	@Column
	@Index(name="businessUnit")
	private String businessUnit;
	
	@Desc("投诉类型")
	@Column
	@Index(name="complaintType")
	private String complaintType;
	
	@Desc("措施验收")
	@Column(length=3000)
	private String checkMesures;
	
	@Desc("赔偿金额")
	@Column
	private String compensation;
	
	@Desc("处理进度")
	@Column
	@Index(name="handlingProgress")
	private String handlingProgress;
	
	@Desc("原因分析")
	@Column(length=3000)
	private String analysisOfCauses;
	
	@Desc("整改措施")
	@Column(length=3000)
	private String correctiveMeasures;
	
	@Desc("文件路径")
	@Column
	private String filePath;
	
	@Desc("最后操作时间")
	@Column
	private Date lastUpdateDate;
	
	@Desc("最后操作人")
	@Column
	private String lastUpdateUser;
	
	
	@Desc("计划完成时间")
	@Column
	private Date planFinishDate;
	
	@Desc("实际完成时间")
	@Column
	private Date realFinishDate;
	
	@Desc("叶型")
	@Column
	private String model;
	
	@Desc("涉及产品/服务")
	@Column
	private String product;
	
	@Desc("批号")
	@Column
	private String batchCode;
	
	@Desc("卷编号")
	@Column
	private String rollBarcode;
	
	@Desc("机台")
	@Column
	private String device;
	
	@Desc("班别")
	@Column
	private String shift;
	
	@Desc("操作工")
	@Column
	private String operator;
	
	@Desc("产品结构分类")
	@Column
	private String productType;
	
	@Desc("整改单位")
	@Column
	private String rectificationUnit;
	
	@Desc("原因分类")
	@Column
	private String reason;
	
	@Desc("原因细分")
	@Column
	private String specificReasons;
	
	public String getSpecificReasons() {
		return specificReasons;
	}

	public void setSpecificReasons(String specificReasons) {
		this.specificReasons = specificReasons;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
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

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getRectificationUnit() {
		return rectificationUnit;
	}

	public void setRectificationUnit(String rectificationUnit) {
		this.rectificationUnit = rectificationUnit;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReasonSegmentation() {
		return ReasonSegmentation;
	}

	public void setReasonSegmentation(String reasonSegmentation) {
		ReasonSegmentation = reasonSegmentation;
	}

	@Desc("原因细分")
	@Column
	private String ReasonSegmentation;

	public Date getPlanFinishDate() {
		return planFinishDate;
	}

	public void setPlanFinishDate(Date planFinishDate) {
		this.planFinishDate = planFinishDate;
	}

	public Date getRealFinishDate() {
		return realFinishDate;
	}

	public void setRealFinishDate(Date realFinishDate) {
		this.realFinishDate = realFinishDate;
	}

	public Long getComplaintConsumerId() {
		return complaintConsumerId;
	}

	public void setComplaintConsumerId(Long complaintConsumerId) {
		this.complaintConsumerId = complaintConsumerId;
	}

	public String getComplaintCode() {
		return complaintCode;
	}

	public void setComplaintCode(String complaintCode) {
		this.complaintCode = complaintCode;
	}

	public String getD8Report() {
		return d8Report;
	}

	public void setD8Report(String d8Report) {
		this.d8Report = d8Report;
	}

	public String getConsumerFrom() {
		return consumerFrom;
	}

	public void setConsumerFrom(String consumerFrom) {
		this.consumerFrom = consumerFrom;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getBasePlace() {
		return basePlace;
	}

	public void setBasePlace(String basePlace) {
		this.basePlace = basePlace;
	}

	public Date getComplaintDate() {
		return complaintDate;
	}

	public void setComplaintDate(Date complaintDate) {
		this.complaintDate = complaintDate;
	}

	public String getInfoFrom() {
		return infoFrom;
	}

	public void setInfoFrom(String infoFrom) {
		this.infoFrom = infoFrom;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getSupplyState() {
		return supplyState;
	}

	public void setSupplyState(String supplyState) {
		this.supplyState = supplyState;
	}

	public String getSupplyCount() {
		return supplyCount;
	}

	public void setSupplyCount(String supplyCount) {
		this.supplyCount = supplyCount;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
	}

	public String getQuestionDesc() {
		return questionDesc;
	}

	public void setQuestionDesc(String questionDesc) {
		this.questionDesc = questionDesc;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getComplaintType() {
		return complaintType;
	}

	public void setComplaintType(String complaintType) {
		this.complaintType = complaintType;
	}

	public String getCheckMesures() {
		return checkMesures;
	}

	public void setCheckMesures(String checkMesures) {
		this.checkMesures = checkMesures;
	}

	public String getCompensation() {
		return compensation;
	}

	public void setCompensation(String compensation) {
		this.compensation = compensation;
	}

	public String getHandlingProgress() {
		return handlingProgress;
	}

	public void setHandlingProgress(String handlingProgress) {
		this.handlingProgress = handlingProgress;
	}

	public String getAnalysisOfCauses() {
		return analysisOfCauses;
	}

	public void setAnalysisOfCauses(String analysisOfCauses) {
		this.analysisOfCauses = analysisOfCauses;
	}

	public String getCorrectiveMeasures() {
		return correctiveMeasures;
	}

	public void setCorrectiveMeasures(String correctiveMeasures) {
		this.correctiveMeasures = correctiveMeasures;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	
}

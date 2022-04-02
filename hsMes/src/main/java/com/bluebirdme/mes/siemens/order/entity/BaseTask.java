package com.bluebirdme.mes.siemens.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 任务单和派工单基类
 * @author Goofy
 * @Date 2017年7月31日 下午6:25:10
 */
@MappedSuperclass
public class BaseTask extends BaseEntity {
	
	@Desc("任务单编号")
	@Column(nullable = false)
	private String taskCode;

	@Desc("订单号")
	@Column(nullable = false)
	private String orderCode;

	@Desc("部件名称")
	@Column(nullable = false)
	private String partName;

	@Desc("部件ID")
	@Column(nullable = false)
	private Long partId;

	@Desc("批次号")
	@Column(nullable = false)
	private String batchCode;

	@Desc("客户简称")
	@Column(nullable = false)
	private String consumerSimpleName;

	@Desc("客户ID")
	@Column(nullable = false)
	private Long consumerId;

	@Desc("客户大类")
	@Column(nullable = false)
	private Integer consumerCategory;

	@Desc("任务单套数")
	@Column(nullable = false)
	private Integer suitCount;

	@Desc("派工套数")
	@Column(nullable = false)
	private Integer assignSuitCount;

	@Desc("发货日期")
	@Column(nullable = false)
	private Date deliveryDate;

	@Desc("任务已打包套数")
	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer packedSuitCount;

	@Desc("任务单创建时间")
	@Column(nullable = false)
	private Date createTime;

	@Desc("创建人")
	@Column(nullable = false)
	private String createUserName;

	@Desc("裁剪计划ID")
	@Column
	private Long cutPlanId;

	@Desc("完成情况")
	// 0:未完成，1：已完成
	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer isComplete;

	@Desc("状态")
	// 0:正常，1：关闭
	@Column(nullable = false, columnDefinition = "int default 0")
	private Integer isClosed;

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public Long getPartId() {
		return partId;
	}

	public void setPartId(Long partId) {
		this.partId = partId;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getConsumerSimpleName() {
		return consumerSimpleName;
	}

	public void setConsumerSimpleName(String consumerSimpleName) {
		this.consumerSimpleName = consumerSimpleName;
	}

	public Long getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Long consumerId) {
		this.consumerId = consumerId;
	}

	public Integer getConsumerCategory() {
		return consumerCategory;
	}

	public void setConsumerCategory(Integer consumerCategory) {
		this.consumerCategory = consumerCategory;
	}

	public Integer getSuitCount() {
		return suitCount;
	}

	public void setSuitCount(Integer suitCount) {
		this.suitCount = suitCount;
	}

	public Integer getAssignSuitCount() {
		return assignSuitCount;
	}

	public void setAssignSuitCount(Integer assignSuitCount) {
		this.assignSuitCount = assignSuitCount;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Integer getPackedSuitCount() {
		return packedSuitCount;
	}

	public void setPackedSuitCount(Integer packedSuitCount) {
		this.packedSuitCount = packedSuitCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Long getCutPlanId() {
		return cutPlanId;
	}

	public void setCutPlanId(Long cutPlanId) {
		this.cutPlanId = cutPlanId;
	}

	public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}

	public Integer getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(Integer isClosed) {
		this.isClosed = isClosed;
	}
}

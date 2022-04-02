package com.bluebirdme.mes.sales.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 销售订单
 * 
 * @authorGoofy
 * @Date2016年10月13日上午10:27:28
 */
@Desc("销售订单")
@Entity
@Table(name = "hs_sales_order")
public class SalesOrder extends BaseEntity {

	@Desc("订单号")
	@Column(nullable = false)
	@Index(name="salesOrderCode")
	private String salesOrderCode;

	@Desc("下单日期")
	@Column(nullable = false)
	private Date salesOrderDate;

	@Desc("客户名称")
	@Column(nullable = false)
	@Index(name="salesOrderConsumerId")
	private Long salesOrderConsumerId;

	@Desc("业务员")
	@Column(nullable = false)
	private Long salesOrderBizUserId;

	@Desc("内销/外销/胚布")
	// 0:外销，1：内销，-1，胚布
	@Column(nullable = false)
	private Integer salesOrderIsExport;

	@Desc("订单审核状态")
	@Column
	private String salesOrderReviewState;

	@Desc("订单类型")
	// （3新品，2试样，1常规产品，-1未知）
	@Column
	private Integer salesOrderType;

	@Desc("订单总金额")
	@Column
	private Double salesOrderTotalMoney;

	@Desc("发货时间")
	@Column
	private Date salesOrderDeliveryTime;

	@Desc("生产进度")
	@Column
	private Double salesOrderProduceProgress;

	@Desc("打包进度")
	@Column
	private Double salesOrderPackagingProgress;

	@Desc("订单备注")
	@Column(length = 16777215)
	private String salesOrderMemo;

	@Desc("来自哪个生产计划")
	@Column
	private String fromProducePlancode;
	
	@Desc("审核状态")
	@Column
	@Index(name="auditState")
	private Integer auditState;
	
	/**
	 * 1:已创建，0：未创建
	 */
	@Desc("是否已经创建计划")
	@Column
	private Integer hasCreatedPlans;
	
	/**
	 * 1：关闭，0：未关闭
	 */
	@Desc("订单是否已关闭")
	@Column
	@Index(name="isClosed")
	private Integer isClosed;
	
	/**
	 * 0，null:未完成，1：完成
	 */
	@Desc("订单已完成")
	@Column
	@Index(name="isComplete")
	private Integer isComplete;
	@Transient
	private Long cutPlanId;

	@Transient
	private List<SalesOrderDetail> details;
	@Transient
	private Long finalConsumerId;
	

	public Long getFinalConsumerId() {
		return finalConsumerId;
	}

	public void setFinalConsumerId(Long finalConsumerId) {
		this.finalConsumerId = finalConsumerId;
	}

	public Long getCutPlanId() {
		return cutPlanId;
	}

	public void setCutPlanId(Long cutPlanId) {
		this.cutPlanId = cutPlanId;
	}

	public String getSalesOrderCode() {
		return salesOrderCode;
	}

	public void setSalesOrderCode(String salesOrderCode) {
		this.salesOrderCode = salesOrderCode;
	}

	public Date getSalesOrderDate() {
		return salesOrderDate;
	}

	public void setSalesOrderDate(Date salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
	}

	public Long getSalesOrderConsumerId() {
		return salesOrderConsumerId;
	}

	public void setSalesOrderConsumerId(Long salesOrderConsumerId) {
		this.salesOrderConsumerId = salesOrderConsumerId;
	}

	public Long getSalesOrderBizUserId() {
		return salesOrderBizUserId;
	}

	public void setSalesOrderBizUserId(Long salesOrderBizUserId) {
		this.salesOrderBizUserId = salesOrderBizUserId;
	}

	public Integer getSalesOrderIsExport() {
		return salesOrderIsExport;
	}

	public void setSalesOrderIsExport(Integer salesOrderIsExport) {
		this.salesOrderIsExport = salesOrderIsExport;
	}

	public String getSalesOrderReviewState() {
		return salesOrderReviewState;
	}

	public void setSalesOrderReviewState(String salesOrderReviewState) {
		this.salesOrderReviewState = salesOrderReviewState;
	}

	public Integer getSalesOrderType() {
		return salesOrderType;
	}

	public void setSalesOrderType(Integer salesOrderType) {
		this.salesOrderType = salesOrderType;
	}

	public Double getSalesOrderTotalMoney() {
		return salesOrderTotalMoney;
	}

	public void setSalesOrderTotalMoney(Double salesOrderTotalMoney) {
		this.salesOrderTotalMoney = salesOrderTotalMoney;
	}

	public Date getSalesOrderDeliveryTime() {
		return salesOrderDeliveryTime;
	}

	public void setSalesOrderDeliveryTime(Date salesOrderDeliveryTime) {
		this.salesOrderDeliveryTime = salesOrderDeliveryTime;
	}

	public Double getSalesOrderProduceProgress() {
		return salesOrderProduceProgress;
	}

	public void setSalesOrderProduceProgress(Double salesOrderProduceProgress) {
		this.salesOrderProduceProgress = salesOrderProduceProgress;
	}

	public Double getSalesOrderPackagingProgress() {
		return salesOrderPackagingProgress;
	}

	public void setSalesOrderPackagingProgress(Double salesOrderPackagingProgress) {
		this.salesOrderPackagingProgress = salesOrderPackagingProgress;
	}

	public String getSalesOrderMemo() {
		return salesOrderMemo;
	}

	public void setSalesOrderMemo(String salesOrderMemo) {
		this.salesOrderMemo = salesOrderMemo;
	}

	public String getFromProducePlancode() {
		return fromProducePlancode;
	}

	public void setFromProducePlancode(String fromProducePlancode) {
		this.fromProducePlancode = fromProducePlancode;
	}
	
	public List<SalesOrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<SalesOrderDetail> details) {
		this.details = details;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public Integer getHasCreatedPlans() {
		return hasCreatedPlans;
	}

	public void setHasCreatedPlans(Integer hasCreatedPlans) {
		this.hasCreatedPlans = hasCreatedPlans;
	}

	public Integer getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(Integer isClosed) {
		this.isClosed = isClosed;
	}

	public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}

}

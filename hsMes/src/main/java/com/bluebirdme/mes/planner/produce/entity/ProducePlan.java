package com.bluebirdme.mes.planner.produce.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("生产任务单")
@Entity
@Table(name="HS_Produce_Plan")
@DynamicInsert
public class ProducePlan extends BaseEntity {
	
	@Desc("生产任务单号")
	@Column(nullable=false)
	@Index(name="producePlanCode")
	private String producePlanCode;
	
	@Desc("创建时间")
	@Column(nullable=false)
	@Index(name="createTime")
	private Date createTime;
	
	@Desc("创建人")
	@Column(nullable=false)
	private String createUserName;
	
	@Desc("创建人ID")
	@Column(nullable=false)
	private Long createUserId;
	
	@Desc("车间")
	@Column(nullable=false)
	@Index(name="workshop")
	private String workshop;
	
	@Desc("审核状态")
	@Column(nullable=false)
	@Index(name="auditState")
	private Integer auditState;
	
	/**
	 * 1:已创建，0:未创建,-1:创建失败
	 */
	@Desc("已创建裁剪和编织计划")
	@Column
	private Integer hasCreatedCutAndWeavePlan;
	
	@Desc("自动生成计划反馈")
	@Index(name="createFeedback")
	private String createFeedback;

	@Desc("车间代码")
	@Index(name="workShopCode")
	private String workShopCode;


	@Transient
	private List<ProducePlanDetail> list;

	public String getProducePlanCode() {
		return producePlanCode;
	}

	public void setProducePlanCode(String producePlanCode) {
		this.producePlanCode = producePlanCode;
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

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getWorkshop() {
		return workshop;
	}

	public void setWorkshop(String workshop) {
		this.workshop = workshop;
	}

	public List<ProducePlanDetail> getList() {
		return list;
	}

	public void setList(List<ProducePlanDetail> list) {
		this.list = list;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public Integer getHasCreatedCutAndWeavePlan() {
		return hasCreatedCutAndWeavePlan;
	}

	public void setHasCreatedCutAndWeavePlan(Integer hasCreatedCutAndWeavePlan) {
		this.hasCreatedCutAndWeavePlan = hasCreatedCutAndWeavePlan;
	}

	public String getCreateFeedback() {
		return createFeedback;
	}

	public void setCreateFeedback(String createFeedback) {
		this.createFeedback = createFeedback;
	}

	public String getWorkShopCode() {
		return workShopCode;
	}

	public void setWorkShopCode(String workShopCode) {
		this.workShopCode = workShopCode;
	}

	
}

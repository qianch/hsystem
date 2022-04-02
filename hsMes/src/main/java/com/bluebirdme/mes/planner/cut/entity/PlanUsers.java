package com.bluebirdme.mes.planner.cut.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 计划和人员信息
 * @author 宋黎明
 * @Date 2016年10月18日 上午10:02:34
 */

@Desc("计划和人员信息")
@Entity
@Table(name="HS_Plan_Users")
@DynamicInsert
public class PlanUsers extends BaseEntity {
	
	@Desc("人员ID")
	@Column(nullable=false)
	@Index(name="userId")
	private Long userId;
	
	@Desc("生产计划明细ID")
	@Column(nullable=false)
	@Index(name="producePlanDetailId")
	private Long producePlanDetailId;
	
	@Desc("部件ID")
	private Long tcBomPartId;
	
	@Desc("指定人员日期")
	private String planUserDate;
	

	/**
	 * **get**
	 */
	public String getPlanUserDate() {
		return planUserDate;
	}

	/**
	 * **set**
	 */
	public void setPlanUserDate(String planUserDate) {
		this.planUserDate = planUserDate;
	}

	/**
	 * **get**
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * **set**
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * **get**
	 */
	public Long getProducePlanDetailId() {
		return producePlanDetailId;
	}

	/**
	 * **set**
	 */
	public void setProducePlanDetailId(Long producePlanDetailId) {
		this.producePlanDetailId = producePlanDetailId;
	}

	/**
	 * **get**
	 */
	public Long getTcBomPartId() {
		return tcBomPartId;
	}

	/**
	 * **set**
	 */
	public void setTcBomPartId(Long tcBomPartId) {
		this.tcBomPartId = tcBomPartId;
	}
	
	
	
	
}

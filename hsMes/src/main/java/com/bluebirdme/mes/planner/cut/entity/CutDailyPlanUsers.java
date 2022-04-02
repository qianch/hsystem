package com.bluebirdme.mes.planner.cut.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 裁剪日计划，部件任务人员列表
 * @author Goofy
 * @Date 2016年12月13日 下午9:30:51
 */
@Entity
@Table(name="hs_Cut_Daily_Plan_users")
public class CutDailyPlanUsers extends BaseEntity {
	
	@Desc("裁剪日计划ID")
	@Column(nullable=false)
	@Index(name="cutPlanDailyPlanId")
	private Long cutPlanDailyPlanId;
	
	@Desc("裁剪计划ID")
	@Column(nullable=false)
	@Index(name="cutPlanId")
	private Long cutPlanId;
	
	@Desc("用户ID")
	@Column(nullable=false)
	@Index(name="userId")
	private Long userId;
	
	@Desc("数量")
	@Column(nullable=false)
	private Integer count;
	
	@Desc("部件名称")
	private String partName;
	@Desc("部件ID")
	private Long partId;

	public Long getPartId() {
		return partId;
	}

	public void setPartId(Long partId) {
		this.partId = partId;
	}

	public Long getCutPlanDailyPlanId() {
		return cutPlanDailyPlanId;
	}

	public void setCutPlanDailyPlanId(Long cutPlanDailyPlanId) {
		this.cutPlanDailyPlanId = cutPlanDailyPlanId;
	}

	public Long getCutPlanId() {
		return cutPlanId;
	}

	public void setCutPlanId(Long cutPlanId) {
		this.cutPlanId = cutPlanId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}
	
}

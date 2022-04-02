package com.bluebirdme.mes.planner.material.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 套材物料需求计划
 * @author Goofy
 * @Date 2016年10月19日 下午1:27:23
 */
@Deprecated
@Desc("物料需求计划")
@Entity
@Table(name="HS_TC_MATERIAL_REQUIREMENT_PLAN")
public class TcMaterialRequirementPlan extends BaseEntity {
	
	@Desc("生产计划ID")
	@Column(nullable=false)
	@Index(name="producePlanId")
	private Long producePlanId;
	
	@Desc("胚布规格")
	@Column(nullable=false)
	private String fabricModel;
	
	@Desc("胚布代码")
	@Column(nullable=false)
	private String fabricCode;
	
	@Desc("门幅")
	@Column(nullable=false)
	private Double fabricWidth;
	
	@Desc("米长")
	@Column(nullable=false)
	private Double fabricLength;
	
	@Desc("数量(卷)")//多少卷
	@Column(nullable=false)
	private Double fabricCount;

	public Long getProducePlanId() {
		return producePlanId;
	}

	public void setProducePlanId(Long producePlanId) {
		this.producePlanId = producePlanId;
	}

	public String getFabricModel() {
		return fabricModel;
	}

	public void setFabricModel(String fabricModel) {
		this.fabricModel = fabricModel;
	}

	public String getFabricCode() {
		return fabricCode;
	}

	public void setFabricCode(String fabricCode) {
		this.fabricCode = fabricCode;
	}

	public Double getFabricWidth() {
		return fabricWidth;
	}

	public void setFabricWidth(Double fabricWidth) {
		this.fabricWidth = fabricWidth;
	}

	public Double getFabricLength() {
		return fabricLength;
	}

	public void setFabricLength(Double fabricLength) {
		this.fabricLength = fabricLength;
	}

	public Double getFabricCount() {
		return fabricCount;
	}

	public void setFabricCount(Double fabricCount) {
		this.fabricCount = fabricCount;
	}
}

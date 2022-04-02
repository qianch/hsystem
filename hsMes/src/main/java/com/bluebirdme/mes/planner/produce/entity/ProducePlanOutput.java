package com.bluebirdme.mes.planner.produce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 生产计划明细产出记录
 * @author Goofy
 * @Date 2016年12月3日 上午9:45:12
 */
@Desc(" 套材生产计划明细产出记录")
@Entity
@Table(name="hs_produce_plan_output")
public class ProducePlanOutput extends BaseEntity {
	
	@Column
	private Long salesOrderDetailId;
	
	/**
	 * 计划明细ID
	 */
	@Desc("计划明细ID")
	@Column
	@Index(name="producePlanDetailId")
	private Long producePlanDetailId;
	
	
	/**
	 * 部件名称
	 */
	@Desc("计划名称")
	@Column
	private String partName;
	
	/**
	 * 已打包数量(托)
	 */
	@Desc("数量")
	@Column
	private Integer count;

	public Long getProducePlanDetailId() {
		return producePlanDetailId;
	}

	public void setProducePlanDetailId(Long producePlanDetailId) {
		this.producePlanDetailId = producePlanDetailId;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	

}

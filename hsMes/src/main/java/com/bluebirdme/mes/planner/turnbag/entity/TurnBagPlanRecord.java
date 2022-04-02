package com.bluebirdme.mes.planner.turnbag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 翻包明细
 * 
 * @author Goofy
 * @Date 2017年3月22日 下午3:24:19
 */
@Desc("翻包记录")
@Entity
@Table(name = "hs_turnbag_plan_record")
public class TurnBagPlanRecord extends BaseEntity {
	
	@Desc("翻包计划ID")
	@Column(nullable=false)
	private Long turnBagPlanId;
	
	@Desc("原订单明细ID")
	@Column(nullable=false)
	private Long salesOrderDetailId;
	
	@Desc("原订单批次号")
	@Column(nullable=false)
	private String oldBatchCode;
	
	@Desc("新订单ID")
	@Column(nullable=false)
	private Long newSalesOrderDetailId;
	
	@Desc("新订单批次号")
	@Column(nullable=false)
	private String newBatchCode;
	
	@Desc("托条码号")
	@Column(nullable=false)
	private String trayCode;

	public Long getTurnBagPlanId() {
		return turnBagPlanId;
	}

	public void setTurnBagPlanId(Long turnBagPlanId) {
		this.turnBagPlanId = turnBagPlanId;
	}

	public Long getSalesOrderDetailId() {
		return salesOrderDetailId;
	}

	public void setSalesOrderDetailId(Long salesOrderDetailId) {
		this.salesOrderDetailId = salesOrderDetailId;
	}

	public Long getNewSalesOrderDetailId() {
		return newSalesOrderDetailId;
	}

	public void setNewSalesOrderDetailId(Long newSalesOrderDetailId) {
		this.newSalesOrderDetailId = newSalesOrderDetailId;
	}

	public String getTrayCode() {
		return trayCode;
	}

	public void setTrayCode(String trayCode) {
		this.trayCode = trayCode;
	}

	public String getOldBatchCode() {
		return oldBatchCode;
	}

	public void setOldBatchCode(String oldBatchCode) {
		this.oldBatchCode = oldBatchCode;
	}

	public String getNewBatchCode() {
		return newBatchCode;
	}

	public void setNewBatchCode(String newBatchCode) {
		this.newBatchCode = newBatchCode;
	}

}

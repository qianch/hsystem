package com.bluebirdme.mes.stock.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;


/**
 * 盘库表
 * @author Goofy
 * @Date 2018年3月6日 下午3:11:48
 */
@Desc("盘库记录表")
@Entity
@Table(name="hs_stock_check",indexes={
		@Index(columnList="checkWarehousePosCode",name="idx_checkWarehousePosCode"),
		@Index(columnList="checkWarehouseCode",name="idx_checkWarehouseCode"),
		@Index(columnList="optUser",name="idx_optUser"),
		@Index(columnList="checkTime",name="idx_checkTime"),
})
public class StockCheck extends BaseEntity {

	@Desc("库位代码")
	@Column(nullable=false)
	private String checkWarehousePosCode;
	
	@Desc("库房代码")
	@Column(nullable=false)
	private String checkWarehouseCode;
	
	@Desc("盘点人")
	@Column(nullable=false)
	private String optUser;
	
	@Desc("盘点时间")
	@Column(nullable=false)
	private String checkTime;
	
	/**
	 * 0:原料，1：成品
	 */
	@Desc("盘点类型")
	@Column(nullable=false)
	private String checkType;
	
	@Transient
	public List<StockCheckResult> list;

	public String getCheckWarehousePosCode() {
		return checkWarehousePosCode;
	}

	public void setCheckWarehousePosCode(String checkWarehousePosCode) {
		this.checkWarehousePosCode = checkWarehousePosCode;
	}

	public String getCheckWarehouseCode() {
		return checkWarehouseCode;
	}

	public void setCheckWarehouseCode(String checkWarehouseCode) {
		this.checkWarehouseCode = checkWarehouseCode;
	}

	public String getOptUser() {
		return optUser;
	}

	public void setOptUser(String optUser) {
		this.optUser = optUser;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public List<StockCheckResult> getList() {
		return list;
	}

	public void setList(List<StockCheckResult> list) {
		this.list = list;
	}
	
}

package com.bluebirdme.mes.stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
@Desc("成品库存状态表")
@Entity
@Table(name="HS_Product_Stock_State")
public class ProductStockState extends BaseEntity {
	
	@Desc("条码号")
	@Column
	@Index(name="barCode")
	private String barCode;
	
	@Desc("库位编码")
	@Column(nullable=false)
	@Index(name="warehousePosCode")
	private String warehousePosCode;
	
	@Desc("仓库编码")
	@Column(nullable=false)
	@Index(name="warehouseCode")
	private String warehouseCode;
	
	/**
	 * 超期的天数
	 */
	@Desc("超期时间")
	@Column(nullable=false)
	private Long overTime;
	   
	/**
	 * 1合格、2不合格、3冻结、5退货、6超产
	 *//*
	@Desc("状态")
	private Integer state;*/
	
	/**
	 * 1在库，-1不在库
	 */
	@Desc("库存状态")
	@Column(nullable=false)
	@Index(name="stockState")
	private Integer stockState;

	
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	/**
	 * **get**
	 */
	public String getWarehousePosCode() {
		return warehousePosCode;
	}

	/**
	 * **set**
	 */
	public void setWarehousePosCode(String warehousePosCode) {
		this.warehousePosCode = warehousePosCode;
	}

	/**
	 * **get**
	 */
	public String getWarehouseCode() {
		return warehouseCode;
	}

	/**
	 * **set**
	 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/**
	 * **get**
	 *//*
	public Integer getState() {
		return state;
	}

	*//**
	 * **set**
	 *//*
	public void setState(Integer state) {
		this.state = state;
	}*/

	/**
	 * **get**
	 */
	public Integer getStockState() {
		return stockState;
	}

	/**
	 * **set**
	 */
	public void setStockState(Integer stockState) {
		this.stockState = stockState;
	}

	/**
	 * **get**
	 */
	public Long getOverTime() {
		return overTime;
	}

	/**
	 * **set**
	 */
	public void setOverTime(Long overTime) {
		this.overTime = overTime;
	}
	
	
}

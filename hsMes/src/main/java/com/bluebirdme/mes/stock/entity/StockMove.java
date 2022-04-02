package com.bluebirdme.mes.stock.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 库存移库
 * @author 宋黎明
 * @Date 2016年11月01日 上午13:02:34
 */

@Desc("库存移库表")
@Entity
@Table(name="hs_stock_move")
public class StockMove extends BaseEntity {
	@Desc("条码号")
	@Column(nullable=false)
	@Index(name="barcode")
	private String barcode;

	@Desc("原库位")
	@Column(nullable=false)
	private String originWarehousePosCode;

	@Desc("原库房")
	@Column(nullable=false)
	private String originWarehouseCode;

	@Desc("新库位")
	@Column(nullable=false)
	private String newWarehousePosCode;

	@Desc("新库房")
	@Column(nullable=false)
	private String newWarehouseCode;

	@Desc("移库时间")
	@Column(nullable=false)
	private Date moveTime;

	@Desc("操作人")
	@Column(nullable=false)
	private Long moveUserId;

	@Desc("计划明细ID")
	private Long producePlanDetailId;

	/**
	 * **get**
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * **set**
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}


	/**
	 * **get**
	 */
	public String getOriginWarehousePosCode() {
		return originWarehousePosCode;
	}

	/**
	 * **set**
	 */
	public void setOriginWarehousePosCode(String originWarehousePosCode) {
		this.originWarehousePosCode = originWarehousePosCode;
	}

	/**
	 * **get**
	 */
	public String getOriginWarehouseCode() {
		return originWarehouseCode;
	}

	/**
	 * **set**
	 */
	public void setOriginWarehouseCode(String originWarehouseCode) {
		this.originWarehouseCode = originWarehouseCode;
	}

	/**
	 * **get**
	 */
	public String getNewWarehousePosCode() {
		return newWarehousePosCode;
	}

	/**
	 * **set**
	 */
	public void setNewWarehousePosCode(String newWarehousePosCode) {
		this.newWarehousePosCode = newWarehousePosCode;
	}

	/**
	 * **get**
	 */
	public String getNewWarehouseCode() {
		return newWarehouseCode;
	}

	/**
	 * **set**
	 */
	public void setNewWarehouseCode(String newWarehouseCode) {
		this.newWarehouseCode = newWarehouseCode;
	}

	/**
	 * **get**
	 */
	public Date getMoveTime() {
		return moveTime;
	}

	/**
	 * **set**
	 */
	public void setMoveTime(Date moveTime) {
		this.moveTime = moveTime;
	}

	/**
	 * **get**
	 */
	public Long getMoveUserId() {
		return moveUserId;
	}

	/**
	 * **set**
	 */
	public void setMoveUserId(Long moveUserId) {
		this.moveUserId = moveUserId;
	}


	public Long getProducePlanDetailId() {
		return producePlanDetailId;
	}

	public void setProducePlanDetailId(Long producePlanDetailId) {
		this.producePlanDetailId = producePlanDetailId;
	}

}

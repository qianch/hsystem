package com.bluebirdme.mes.stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("原料强制出库")
@Entity
@Table(name = "HS_Material_ForceOut_Record")
public class MaterialForceOutRecord extends BaseEntity {
	@Desc("托盘ID")
	@Column(nullable = false,unique=true)
	private Long mssId;
	@Desc("出库时间")
	@Column(nullable = false)
	private Long outTime;
	//以下需要从PDA带出
	@Desc("仓库编码")
	@Index(name = "warehouseCode")
	private String warehouseCode;
	@Desc("库位编码")
	@Index(name = "warehousePosCode")
	private String warehousePosCode;

	@Desc("出库地址")
	@Index(name = "outAddress")
	private String outAddress;
	@Desc("操作人")
	@Index(name = "outUser")
	private String outUser;
	
	@Desc("K3同步状态")
	@Column(nullable=false)
	@Index(name="syncState")
	private Integer syncState=0;

	public Long getMssId() {
		return mssId;
	}

	public void setMssId(Long mssId) {
		this.mssId = mssId;
	}

	public Long getOutTime() {
		return outTime;
	}

	public void setOutTime(Long outTime) {
		this.outTime = outTime;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehousePosCode() {
		return warehousePosCode;
	}

	public void setWarehousePosCode(String warehousePosCode) {
		this.warehousePosCode = warehousePosCode;
	}

	public String getOutAddress() {
		return outAddress;
	}

	public void setOutAddress(String outAddress) {
		this.outAddress = outAddress;
	}

	public String getOutUser() {
		return outUser;
	}

	public void setOutUser(String outUser) {
		this.outUser = outUser;
	}

	public Integer getSyncState() {
		return syncState;
	}

	public void setSyncState(Integer syncState) {
		this.syncState = syncState;
	}

}

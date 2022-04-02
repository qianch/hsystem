package com.bluebirdme.mes.stock.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("原料强制出库")
@Entity
@Table(name = "HS_Product_ForceOut_Record")
public class ProductForceOutRecord extends BaseEntity {
	@Desc("产品条码")
	@Index(name = "barcode")
	private String barcode;
	@Desc("出库时间")
	@Column(nullable = false)
	private Date outTime;
	@Desc("出库地址")
	@Index(name = "outAddress")
	private String outAddress;
	@Desc("操作人")
	@Index(name = "outUser")
	private String outUser;

	@Desc("K3同步状态")
	@Column(nullable = false)
	@Index(name = "syncState")
	private Integer syncState = 0;

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
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

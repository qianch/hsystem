package com.bluebirdme.mes.stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("出库单明细")
@Entity
@Table(name="HS_Material_Out_Order_Detail",indexes={
		@Index(columnList="outOrderId",name="idx_outOrderId"),
		@Index(columnList="mssId",name="idx_mssId"),
		@Index(columnList="syncState",name="idx_syncState"),
		@Index(columnList="outTime",name="idx_outTime"),
})
public class MaterialOutOrderDetail extends BaseEntity{
	@Desc("出库单ID")
	@Column(nullable=false)
	private Long outOrderId;
	
	@Desc("托盘ID")
	@Column(nullable = false)
	private Long mssId;
	
	@Desc("同步状态")
	@Column(nullable=false,columnDefinition="int default 0")
	private Integer syncState;
	
	@Desc("出库时间")
	@Column(nullable=false)
	private Long outTime;
	
	@Desc("操作人")
	@Column(nullable=false)
	private String optUser;
	

	/**
	 * @param outOrderId
	 * @param mssId
	 */
	public MaterialOutOrderDetail(Long outOrderId, Long mssId,String optUser) {
		super();
		this.outOrderId = outOrderId;
		this.mssId = mssId;
		this.syncState=0;
		this.outTime=System.currentTimeMillis();
		this.optUser=optUser;
	}

	public MaterialOutOrderDetail() {
	}

	public Long getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(Long outOrderId) {
		this.outOrderId = outOrderId;
	}

	public Long getMssId() {
		return mssId;
	}

	public void setMssId(Long mssId) {
		this.mssId = mssId;
	}

	public Integer getSyncState() {
		return syncState;
	}

	public void setSyncState(Integer syncState) {
		this.syncState = syncState;
	}

	public Long getOutTime() {
		return outTime;
	}

	public void setOutTime(Long outTime) {
		this.outTime = outTime;
	}

	public String getOptUser() {
		return optUser;
	}

	public void setOptUser(String optUser) {
		this.optUser = optUser;
	}
	
}

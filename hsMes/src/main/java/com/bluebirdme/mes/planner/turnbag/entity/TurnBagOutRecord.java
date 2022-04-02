package com.bluebirdme.mes.planner.turnbag.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 翻包领出记录
 * @author Goofy
 * @Date 2017年7月3日 下午12:39:14
 */
@Entity
@Table(name="hs_turnbag_out_record")
public class TurnBagOutRecord extends BaseEntity {
	
	@Desc("翻包领出ID")
	@Column
	private Long turnBagPlanDetailId;
	
	@Desc("托条码号")
	@Column
	private String trayCode;
	
	@Desc("领出时间")
	@Column
	private Date outDate;
	
	@Desc("操作人")
	@Column
	private String optUserName;
	
	@Desc("同步状态")
	@Column(nullable=false)
	private Integer syncState=0;
	
	@Desc("领出车间")
	@Column(nullable=false)
	private String outAddress;

	public Long getTurnBagPlanDetailId() {
		return turnBagPlanDetailId;
	}

	public void setTurnBagPlanDetailId(Long turnBagPlanDetailId) {
		this.turnBagPlanDetailId = turnBagPlanDetailId;
	}

	public String getTrayCode() {
		return trayCode;
	}

	public void setTrayCode(String trayCode) {
		this.trayCode = trayCode;
	}

	public Date getOutDate() {
		return outDate==null?(new Date()):outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getOptUserName() {
		return optUserName;
	}

	public void setOptUserName(String optUserName) {
		this.optUserName = optUserName;
	}

	public Integer getSyncState() {
		return syncState;
	}

	public void setSyncState(Integer syncState) {
		this.syncState = syncState;
	}

	public String getOutAddress() {
		return outAddress;
	}

	public void setOutAddress(String outAddress) {
		this.outAddress = outAddress;
	}
	
}

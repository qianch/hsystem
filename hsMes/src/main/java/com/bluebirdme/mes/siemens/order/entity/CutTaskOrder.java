package com.bluebirdme.mes.siemens.order.entity;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;
import org.xdemo.superutil.j2se.ReflectUtils;

import com.bluebirdme.mes.core.annotation.Desc;

/**
 * 裁剪派工单
 * 
 * @author Goofy
 * @Date 2017年7月26日 上午10:37:32
 */
@Desc("裁剪派工单")
@Entity
@Table(name = "hs_siemens_cut_task_order")
public class CutTaskOrder extends BaseTask {

	@Desc("派工单编号")
	@Column
	@Index(name="ctoCode")
	private String ctoCode;
	@Desc("裁剪任务单ID")
	@Column
	private Long ctId;
	@Desc("组别")
	@Column
	private String ctoGroupName;
	@Desc("机长")
	@Column
	private String ctoGroupLeader;
	
	@Desc("派工单图纸信息")
	@Transient
	private List<CutTaskOrderDrawings> list;

	public String getCtoCode() {
		return ctoCode;
	}
	public void setCtoCode(String ctoCode) {
		this.ctoCode = ctoCode;
	}
	public Long getCtId() {
		return ctId;
	}
	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}
	public String getCtoGroupName() {
		return ctoGroupName;
	}
	public void setCtoGroupName(String ctoGroupName) {
		this.ctoGroupName = ctoGroupName;
	}
	public String getCtoGroupLeader() {
		return ctoGroupLeader;
	}
	public void setCtoGroupLeader(String ctoGroupLeader) {
		this.ctoGroupLeader = ctoGroupLeader;
	}
	public List<CutTaskOrderDrawings> getList() {
		return list;
	}
	public void setList(List<CutTaskOrderDrawings> list) {
		this.list = list;
	}

}

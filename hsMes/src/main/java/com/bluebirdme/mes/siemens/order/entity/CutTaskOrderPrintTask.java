package com.bluebirdme.mes.siemens.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Comment;
import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.core.dev.DevHelper;

/**
 * 
 * @author Goofy
 * @Date 2019年4月15日 下午4:21:59
 */
@Desc("西门子裁剪车间机台打印任务")
@Entity
@Table(name = "hs_siemens_cut_task_order_print_task",indexes={@Index(columnList="ip,ctoCode,drawingNo",unique=true,name = "ip")})
public class CutTaskOrderPrintTask extends BaseEntity {
	
	@Desc("计算机IP")
	@Column(name = "ip")
	private String ip;
	@Desc("派工单号")
	@Column(name = "ctoCode")
	private String ctoCode;
	@Desc("图号")
	@Column(name = "drawingNo")
	private String drawingNo;
	@Desc("当前打印序号")
	@Column(name = "currentPrintOrder")
	private String currentPrintOrder;
	@Desc("层数")
	@Column(name = "leveCount")
	private String leveCount;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCtoCode() {
		return ctoCode;
	}
	public void setCtoCode(String ctoCode) {
		this.ctoCode = ctoCode;
	}
	public String getDrawingNo() {
		return drawingNo;
	}
	public void setDrawingNo(String drawingNo) {
		this.drawingNo = drawingNo;
	}
	public String getCurrentPrintOrder() {
		return currentPrintOrder;
	}
	public void setCurrentPrintOrder(String currentPrintOrder) {
		this.currentPrintOrder = currentPrintOrder;
	}
	public String getLeveCount() {
		return leveCount;
	}
	public void setLeveCount(String leveCount) {
		this.leveCount = leveCount;
	}
}

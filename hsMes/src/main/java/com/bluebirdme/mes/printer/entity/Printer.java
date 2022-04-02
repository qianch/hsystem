package com.bluebirdme.mes.printer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
@Desc("打印机信息")
@Entity
@Table(name="HS_Printer")
public class Printer extends BaseEntity {
	@Desc("打印机名称")
	@Column(nullable=false)
	private String printerName;
	@Desc("打印机显示名称")
	@Column(nullable=false)
	private String printerTxtName;
	@Desc("打印机状态")
	@Column
	private Integer printerState=0;
	@Desc("打印机ip")
	@Column(nullable=false)
	private String printerIp;

	@Desc("部门")
	@Column(nullable=false)
	private Long  departmentId;

	@Desc("打印端口号")
	@Column
	private Integer  port;


	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public String getPrinterTxtName() {
		return printerTxtName;
	}
	public void setPrinterTxtName(String printerTxtName) {
		this.printerTxtName = printerTxtName;
	}
	public String getPrinterName() {
		return printerName;
	}
	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public Integer getPrinterState() {
		return printerState;
	}
	public void setPrinterState(Integer printerState) {
		this.printerState = printerState;
	}
	public String getPrinterIp() {
		return printerIp;
	}
	public void setPrinterIp(String printerIp) {
		this.printerIp = printerIp;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}

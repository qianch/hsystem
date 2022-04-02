package com.bluebirdme.mes.baseInfo.entityMirror;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("套材BOM")
@Entity
@Table(name="hs_tc_proc_bom_mirror")
@DynamicInsert
public class TcBomMirror extends BaseEntity {

	@Desc("套材BOMID")
	@Column
	private Long tcProcBomId;

	@Desc("BOM名称")
	@Column(nullable=false)
	private String tcProcBomName;
	
	@Desc("BOM代码")
	@Column(nullable=false)
	@Index(name="tcProcBomCode")
	private String tcProcBomCode;
	
	@Desc("客户信息")
	@Column(nullable=false)
	private Integer tcProcBomConsumerId;
	//0常规 1变更试样2新品试样
	@Desc("试样工艺")
	@Column(nullable=false)
	@Index(name="isTestPro")
	private Integer isTestPro;

	@Desc("镜像时间")
	@Column
	private Date gmtCreate;

	@Desc("订单Id")
	@Column
	private Long salesOrderId;

	public Long getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(Long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public Long getTcProcBomId() {
		return tcProcBomId;
	}

	public void setTcProcBomId(Long tcProcBomId) {
		this.tcProcBomId = tcProcBomId;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Integer getIsTestPro() {
		return isTestPro;
	}

	public void setIsTestPro(Integer isTestPro) {
		this.isTestPro = isTestPro;
	}

	public String getTcProcBomName() {
		return tcProcBomName;
	}

	public void setTcProcBomName(String tcProcBomName) {
		this.tcProcBomName = tcProcBomName;
	}

	public String getTcProcBomCode() {
		return tcProcBomCode;
	}

	public void setTcProcBomCode(String tcProcBomCode) {
		this.tcProcBomCode = tcProcBomCode;
	}

	public Integer getTcProcBomConsumerId() {
		return tcProcBomConsumerId;
	}

	public void setTcProcBomConsumerId(Integer tcProcBomConsumerId) {
		this.tcProcBomConsumerId = tcProcBomConsumerId;
	}
	
	
	  
}

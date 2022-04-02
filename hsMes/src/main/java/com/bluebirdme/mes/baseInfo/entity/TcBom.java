package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
@Desc("套材BOM")
@Entity
@Table(name="HS_TC_PROC_BOM")
@DynamicInsert
public class TcBom extends BaseEntity {
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

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 非套材工艺BOM
 * @author 宋黎明
 * @Date 2016年10月08日 上午10:02:34
 */
@Desc("非套材工艺BOM")
@Entity
@Table(name="HS_FTC_PROC_BOM")
public class FtcBom extends BaseEntity {
	
	@Desc("BOM名称")
	@Column(nullable=false)
	private String ftcProcBomName;
	
	@Desc("BOM代码")
	@Column
	@Index(name="ftcProcBomCode")
	private String ftcProcBomCode;
	
	@Desc("客户信息")
	@Column(nullable=false)
	@Index(name="ftcProcBomConsumerId")
	private Long ftcProcBomConsumerId;
	
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

	/**
	 * **get**
	 */
	public String getFtcProcBomName() {
		return ftcProcBomName;
	}

	/**
	 * **set**
	 */
	public void setFtcProcBomName(String ftcProcBomName) {
		this.ftcProcBomName = ftcProcBomName;
	}

	/**
	 * **get**
	 */
	public String getFtcProcBomCode() {
		return ftcProcBomCode;
	}

	/**
	 * **set**
	 */
	public void setFtcProcBomCode(String ftcProcBomCode) {
		this.ftcProcBomCode = ftcProcBomCode;
	}

	/**
	 * **get**
	 */
	public Long getFtcProcBomConsumerId() {
		return ftcProcBomConsumerId;
	}

	/**
	 * **set**
	 */
	public void setFtcProcBomConsumerId(Long ftcProcBomConsumerId) {
		this.ftcProcBomConsumerId = ftcProcBomConsumerId;
	}
	
	

}

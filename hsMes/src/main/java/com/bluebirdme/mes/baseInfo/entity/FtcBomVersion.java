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
 * 非套材工艺BOM版本
 * @author 宋黎明
 * @Date 2016年10月09日 上午10:02:34
 */
@Desc("非套材工艺BOM版本")
@Entity
@Table(name="HS_FTC_PROC_BOM_VERSION")
@DynamicInsert
public class FtcBomVersion extends BaseEntity {
	
	@Desc("工艺信息")
	@Column(nullable=false)
	@Index(name="ftcProcBomId")
	private Long ftcProcBomId;
	
	@Desc("版本号")
	@Column(nullable=false)
	@Index(name="ftcProcBomVersionCode")
	private String ftcProcBomVersionCode;

	@Desc("客户版本号")
	@Column(nullable=true)
	@Index(name="ftcConsumerVersionCode")
	private String ftcConsumerVersionCode;
	
	//1:启用  -1:未启用
	@Desc("是否启用")
	@Column
	private Integer ftcProcBomVersionEnabled;

	//1:是默认  -1:不是默认
	@Desc("是否默认")
	@Column
	private Integer ftcProcBomVersionDefault;
	
	@Desc("审批状态")
	@Column
	private Integer auditState;

	@Desc("Attachment的主键Id")
	private Integer attachmentId;
	/**
	 * **get**
	 */
	public Long getFtcProcBomId() {
		return ftcProcBomId;
	}

	/**
	 * **set**
	 */
	public void setFtcProcBomId(Long ftcProcBomId) {
		this.ftcProcBomId = ftcProcBomId;
	}

	/**
	 * **get**
	 */
	public String getFtcProcBomVersionCode() {
		return ftcProcBomVersionCode;
	}

	/**
	 * **set**
	 */
	public void setFtcProcBomVersionCode(String ftcProcBomVersionCode) {
		this.ftcProcBomVersionCode = ftcProcBomVersionCode;
	}

	public String getFtcConsumerVersionCode() {
		return ftcConsumerVersionCode;
	}

	public void setFtcConsumerVersionCode(String ftcConsumerVersionCode) {
		this.ftcConsumerVersionCode = ftcConsumerVersionCode;
	}

	/**
	 * **get**
	 */
	public Integer getFtcProcBomVersionEnabled() {
		return ftcProcBomVersionEnabled;
	}

	/**
	 * **set**
	 */
	public void setFtcProcBomVersionEnabled(Integer ftcProcBomVersionEnabled) {
		this.ftcProcBomVersionEnabled = ftcProcBomVersionEnabled;
	}

	/**
	 * **get**
	 */
	public Integer getFtcProcBomVersionDefault() {
		return ftcProcBomVersionDefault;
	}

	/**
	 * **set**
	 */
	public void setFtcProcBomVersionDefault(Integer ftcProcBomVersionDefault) {
		this.ftcProcBomVersionDefault = ftcProcBomVersionDefault;
	}

	/**
	 * **get**
	 */
	public Integer getAuditState() {
		return auditState;
	}

	/**
	 * **set**
	 */
	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}
}

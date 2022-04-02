package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 包材bom版本信息
 * @author 徐波
 * @Date 2016年10月08日 13:19
 */
@Desc("包材bom版本信息")
@Entity
@Table(name="HS_Bc_Bom_Version")
@DynamicInsert
public class BCBomVersion extends BaseEntity{
	@Transient
	private String packBomCode;
	@Desc("版本号")
	@Column(name="packVersion",nullable=false)
	private String packVersion;
	public String getPackBomCode() {
		return packBomCode;
	}
	public void setPackBomCode(String packBomCode) {
		this.packBomCode = packBomCode;
	}
	@Desc("是否启用，-1不启用，1启用")
	@Column(name="packEnabled",nullable=false,columnDefinition = "int default -1")
	private Integer packEnabled;
	@Desc("是否默认，-1不是默认，1默认")
	@Column(name="packIsDefault",nullable=false,columnDefinition = "int default -1")
	private Integer packIsDefault;
	@Desc("包材BOM ID")
	@Index(name="packBomId")
	@Column(name="packBomId",nullable=false)
	private Long packBomId;
	@Desc("审核状态")
	@Column(nullable=false,columnDefinition = "int default 0")
	private Integer auditState=0;
	@Desc("Attachment的id")
	private Integer attachmentId;


public Integer getAuditState() {
		return auditState;
	}
	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}
	//	@Desc("标准代码")
//	@Column(name="packCode",nullable=false)
//	private String packCode;
	public String getPackVersion() {
		return packVersion;
	}
	public void setPackVersion(String packVersion) {
		this.packVersion = packVersion;
	}
	public Integer getPackEnabled() {
		return packEnabled;
	}
	public void setPackEnabled(Integer packEnabled) {
		this.packEnabled = packEnabled;
	}
	public Integer getPackIsDefault() {
		return packIsDefault;
	}
	public void setPackIsDefault(Integer packIsDefault) {
		this.packIsDefault = packIsDefault;
	}
	public Long getPackBomId() {
		return packBomId;
	}
	public void setPackBomId(Long packBomId) {
		this.packBomId = packBomId;
	}

//	public String getPackCode() {
//		return packCode;
//	}
//	public void setPackCode(String packCode) {
//		this.packCode = packCode;
//	}


	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}
}

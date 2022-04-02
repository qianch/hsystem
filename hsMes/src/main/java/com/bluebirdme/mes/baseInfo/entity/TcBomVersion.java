package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
@Desc("套材Bom版本")
@Entity
@Table(name="HS_TC_PROC_BOM_VERSION")
@DynamicInsert
public class TcBomVersion extends BaseEntity {

    @Desc("工艺信息")
	@Column(nullable=false)
	@Index(name="tcProcBomId")
	private Long tcProcBomId;
	
	@Desc("版本号")
	@Column(nullable=true)
	@Index(name="tcProcBomVersionCode")
	private String tcProcBomVersionCode;

	@Desc("客户版本号")
	@Column(nullable=true)
	@Index(name="tcConsumerVersionCode")
	private String tcConsumerVersionCode;
/*
 * 启用：1
 * 未启用：-1
 * 0:改版
 */
	@Desc("是否启用")
	@Column(nullable=true)
	private Integer tcProcBomVersionEnabled;
	
/*
 * 默认：1
 * 非默认：-1或null
*/
    @Desc("是否默认")
	@Column(nullable=true)
	private Integer tcProcBomVersionDefault;
    
    @Desc("审批状态")
	@Index(name="auditState")
    @Column(nullable=false,columnDefinition = "int default 0")
	private Integer auditState;

	@Desc("Attachment的主键Id")
	private Integer attachmentId;

	public TcBomVersion() {
	}


	public Long getTcProcBomId() {
		return tcProcBomId;
	}

	public void setTcProcBomId(Long tcProcBomId) {
		this.tcProcBomId = tcProcBomId;
	}

	public String getTcProcBomVersionCode() {
		return tcProcBomVersionCode;
	}

	public void setTcProcBomVersionCode(String tcProcBomVersionCode) {
		this.tcProcBomVersionCode = tcProcBomVersionCode;
	}

	public String getTcConsumerVersionCode() {
		return tcConsumerVersionCode;
	}

	public void setTcConsumerVersionCode(String tcConsumerVersionCode) {
		this.tcConsumerVersionCode = tcConsumerVersionCode;
	}

	public Integer getTcProcBomVersionEnabled() {
		return tcProcBomVersionEnabled;
	}

	public void setTcProcBomVersionEnabled(Integer tcProcBomVersionEnabled) {
		this.tcProcBomVersionEnabled = tcProcBomVersionEnabled;
	}

	public Integer getTcProcBomVersionDefault() {
		return tcProcBomVersionDefault;
	}

	public void setTcProcBomVersionDefault(Integer tcProcBomVersionDefault) {
		this.tcProcBomVersionDefault = tcProcBomVersionDefault;
	}

	public Integer getAuditState() {
		return auditState;
	}

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

package com.bluebirdme.mes.baseInfo.entityMirror;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("套材Bom版本")
@Entity
@Table(name="hs_tc_proc_bom_version_mirror")
@DynamicInsert
public class TcBomVersionMirror extends BaseEntity {


	@Desc("套材Bom版本镜像Id")
	@Column
	private Long tcProcBomVersionId;

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

	@Desc("镜像时间")
	@Column
	private Date gmtCreate;

	@Desc("订单id")
	@Column
	private Long salesOrderId;

	public Long getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(Long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public Long getTcProcBomVersionId() {
		return tcProcBomVersionId;
	}

	public void setTcProcBomVersionId(Long tcProcBomVersionId) {
		this.tcProcBomVersionId = tcProcBomVersionId;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public TcBomVersionMirror() {
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

    
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.core.dev.DevHelper;

/**
 * 非套材包材BOM
 * 
 * @author Goofy
 * @Date 2017年11月17日 下午2:51:14
 */
@Desc("包装版本信息")
@Entity
@Table(name = "hs_ftc_bc_bom_version")
public class FtcBcBomVersion extends BaseEntity {

	@Desc("包材BOM ID")
	@Column(nullable = false)
	public Long bid;
	@Desc("版本")
	@Column(nullable = false)
	public String version;
	@Desc("包材总重")
	@Column
	public String bcTotalWeight;
	@Desc("产品类别")
	@Column(nullable = false)
	public String productType;
	@Desc("客户名称")
	@Column(nullable = false)
	public Long consumerId;
	@Desc("卷径")
	@Column
	public String rollDiameter;
	@Desc("托长")
	@Column
	public String palletLength;
	@Desc("托宽")
	@Column
	public String palletWidth;
	@Desc("每托卷数")
	@Column(nullable = false)
	public String rollsPerPallet;
	@Desc("托高")
	@Column
	public String palletHeight;
	@Desc("塑料膜要求")
	@Column
	public String requirement_suliaomo;
	@Desc("摆放要求")
	@Column
	public String requirement_baifang;
	@Desc("打包带要求")
	@Column
	public String requirement_dabaodai;
	@Desc("标签要求")
	@Column
	public String requirement_biaoqian;
	@Desc("小标签要求")
	@Column
	public String requirement_xiaobiaoqian;
	@Desc("卷标签要求")
	@Column
	public String requirement_juanbiaoqian;
	@Desc("托标签要求")
	@Column
	public String requirement_tuobiaoqian;
	@Desc("缠绕要求")
	@Column
	public String requirement_chanrao;
	@Desc("其他")
	@Column
	public String requirement_other;
	
	/**
	 * 0:可用,1:不可用
	 */
	@Desc("是否可用")
	@Column(nullable=false)
	public Integer enabled=0;
	
	@Desc("审核状态")
	@Column(nullable=false,columnDefinition = "int default 0")
	private Integer auditState=0;

	@Desc("Attachment的id")
	private Integer attachmentId;

	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBcTotalWeight() {
		return bcTotalWeight;
	}

	public void setBcTotalWeight(String bcTotalWeight) {
		this.bcTotalWeight = bcTotalWeight;
	}

	/**
	 * 0:常规产品		1:变更试样		2:新品试样
	 */
	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Long getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Long consumerId) {
		this.consumerId = consumerId;
	}

	public String getRollDiameter() {
		return rollDiameter;
	}

	public void setRollDiameter(String rollDiameter) {
		this.rollDiameter = rollDiameter;
	}

	public String getPalletLength() {
		return palletLength;
	}

	public void setPalletLength(String palletLength) {
		this.palletLength = palletLength;
	}

	public String getPalletWidth() {
		return palletWidth;
	}

	public void setPalletWidth(String palletWidth) {
		this.palletWidth = palletWidth;
	}

	public String getRollsPerPallet() {
		return rollsPerPallet;
	}

	public void setRollsPerPallet(String rollsPerPallet) {
		this.rollsPerPallet = rollsPerPallet;
	}

	public String getPalletHeight() {
		return palletHeight;
	}

	public void setPalletHeight(String palletHeight) {
		this.palletHeight = palletHeight;
	}

	public String getRequirement_suliaomo() {
		return requirement_suliaomo;
	}

	public void setRequirement_suliaomo(String requirement_suliaomo) {
		this.requirement_suliaomo = requirement_suliaomo;
	}

	public String getRequirement_baifang() {
		return requirement_baifang;
	}

	public void setRequirement_baifang(String requirement_baifang) {
		this.requirement_baifang = requirement_baifang;
	}

	public String getRequirement_dabaodai() {
		return requirement_dabaodai;
	}

	public void setRequirement_dabaodai(String requirement_dabaodai) {
		this.requirement_dabaodai = requirement_dabaodai;
	}

	public String getRequirement_biaoqian() {
		return requirement_biaoqian;
	}

	public void setRequirement_biaoqian(String requirement_biaoqian) {
		this.requirement_biaoqian = requirement_biaoqian;
	}

	public String getRequirement_xiaobiaoqian() {
		return requirement_xiaobiaoqian;
	}

	public void setRequirement_xiaobiaoqian(String requirement_xiaobiaoqian) {
		this.requirement_xiaobiaoqian = requirement_xiaobiaoqian;
	}

	public String getRequirement_juanbiaoqian() {
		return requirement_juanbiaoqian;
	}

	public void setRequirement_juanbiaoqian(String requirement_juanbiaoqian) {
		this.requirement_juanbiaoqian = requirement_juanbiaoqian;
	}

	public String getRequirement_tuobiaoqian() {
		return requirement_tuobiaoqian;
	}

	public void setRequirement_tuobiaoqian(String requirement_tuobiaoqian) {
		this.requirement_tuobiaoqian = requirement_tuobiaoqian;
	}

	public String getRequirement_chanrao() {
		return requirement_chanrao;
	}

	public void setRequirement_chanrao(String requirement_chanrao) {
		this.requirement_chanrao = requirement_chanrao;
	}

	public String getRequirement_other() {
		return requirement_other;
	}

	public void setRequirement_other(String requirement_other) {
		this.requirement_other = requirement_other;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
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

	public static void main(String[] args) throws Exception {
		DevHelper.genAll(FtcBcBomVersion.class, "高飞");
	}

}

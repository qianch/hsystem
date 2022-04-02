package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 包材bom版本信息
 * @author 徐波
 * @Date 2016年10月08日 13:30
 */
@Desc("包材bom明细")
@Entity
@Table(name="HS_Bc_Bom_Version_Detail")
public class BcBomVersionDetail extends BaseEntity{
	@Transient
	private String packBomCode;
	@Transient
	private String packVersion;
	public String getPackBomCode() {
		return packBomCode;
	}
	public void setPackBomCode(String packBomCode) {
		this.packBomCode = packBomCode;
	}
	public String getPackVersion() {
		return packVersion;
	}
	public void setPackVersion(String packVersion) {
		this.packVersion = packVersion;
	}
	@Desc("包材名称")
	@Column(name="packMaterialName",nullable=false)
	private String packMaterialName;
	@Desc("规格")
	@Column(name="packMaterialModel")
	private String packMaterialModel;
	@Desc("材质")
	@Column(name="packMaterialAttr")
	private String packMaterialAttr;
	@Desc("数量")
	@Column(name="packMaterialCount")
	private Double packMaterialCount;
	@Desc("物料单位")
	@Column(name="packMaterialUnit")
	private String packMaterialUnit;
	@Desc("包装单位")
	@Column(name="packUnit")
	private String packUnit;
	@Desc("包装要求")
	@Column(name="packRequire")
	private String packRequire;
	@Desc("备注")
	@Column(name="packMemo")
	private String packMemo;
	@Desc("包装版本信息")
	@Index(name="packVersionId")
	@Column(name="packVersionId",nullable=false)
	private Long packVersionId;
	public String getPackMaterialName() {
		return packMaterialName;
	}
	public void setPackMaterialName(String packMaterialName) {
		this.packMaterialName = packMaterialName;
	}
	public String getPackMaterialModel() {
		return packMaterialModel;
	}
	public void setPackMaterialModel(String packMaterialModel) {
		this.packMaterialModel = packMaterialModel;
	}
	public String getPackMaterialAttr() {
		return packMaterialAttr;
	}
	public void setPackMaterialAttr(String packMaterialAttr) {
		this.packMaterialAttr = packMaterialAttr;
	}
	public Double getPackMaterialCount() {
		return packMaterialCount;
	}
	public void setPackMaterialCount(Double packMaterialCount) {
		this.packMaterialCount = packMaterialCount;
	}
	public String getPackMaterialUnit() {
		return packMaterialUnit;
	}
	public void setPackMaterialUnit(String packMaterialUnit) {
		this.packMaterialUnit = packMaterialUnit;
	}
	public String getPackUnit() {
		return packUnit;
	}
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}
	public String getPackRequire() {
		return packRequire;
	}
	public void setPackRequire(String packRequire) {
		this.packRequire = packRequire;
	}
	public String getPackMemo() {
		return packMemo;
	}
	public void setPackMemo(String packMemo) {
		this.packMemo = packMemo;
	}
	public Long getPackVersionId() {
		return packVersionId;
	}
	public void setPackVersionId(Long packVersionId) {
		this.packVersionId = packVersionId;
	}
	
	
	
}

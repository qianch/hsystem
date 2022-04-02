package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 非套材包材bom版本信息明细
 * @author 徐秦冬
 * @Date 2017年12月07日 14:03
 */
@Desc("非套材包材bom明细")
@Entity
@Table(name="hs_ftc_bc_bom_version_detail")
public class FtcBcBomVersionDetail extends BaseEntity{
	
	@Desc("物料代码")
	@Column(name="packMaterialCode",nullable=false)
	private String packMaterialCode;
	
	@Desc("标准码")
	@Column(name="packStandardCode")
	private String packStandardCode;
	
	@Desc("材料名称")
	@Column(name="packMaterialName",nullable=false)
	private String packMaterialName;
	
	@Desc("规格")
	@Column(name="packMaterialModel")
	private String packMaterialModel;
	
	@Desc("单位")
	@Column(name="packUnit",nullable=false)
	private String packUnit;
	
	@Desc("数量")
	@Column(name="packMaterialCount",nullable=false)
	private Double packMaterialCount;

	@Desc("备注")
	@Column(name="packMemo")
	private String packMemo;
	
	@Desc("包装版本信息")
	@Index(name="packVersionId")
	@Column(name="packVersionId",nullable=false)
	private Long packVersionId;
	public String getPackMaterialCode() {
		return packMaterialCode;
	}
	public void setPackMaterialCode(String packMaterialCode) {
		this.packMaterialCode = packMaterialCode;
	}
	public String getPackStandardCode() {
		return packStandardCode;
	}
	public void setPackStandardCode(String packStandardCode) {
		this.packStandardCode = packStandardCode;
	}
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
	public String getPackUnit() {
		return packUnit;
	}
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}
	public Double getPackMaterialCount() {
		return packMaterialCount;
	}
	public void setPackMaterialCount(Double packMaterialCount) {
		this.packMaterialCount = packMaterialCount;
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

package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
@Desc("版本部件")
@Entity
@Table(name="HS_TC_PROC_BOM_VERSION_PARTS")
@DynamicInsert
public class TcBomVersionParts extends BaseEntity {
	@Desc("BOM版本ID")
	@Column(nullable=false)
	@Index(name="tcProcBomVersoinId")
	private Long tcProcBomVersoinId;
	
	@Desc("部件名称")
	@Column(nullable=false)
	private String tcProcBomVersionPartsName;
	
	@Desc("部件物料号")
	@Column(nullable=false)
	private String tcProcBomVersionMaterialNumber;
	
	@Desc("部件类型")
	@Column(nullable=false,columnDefinition="varchar(100) default '常规部件'")
	private String tcProcBomVersionPartsType;
	
	
	@Desc("上级部件")
	@Column(nullable=true)
	private Long tcProcBomVersionParentParts;
	
	@Desc("部件裁剪包装工艺代码")
	@Column(nullable=false)
	private String tcProcBomVersionPartsCutCode;
	
	@Desc("部件数量")
	@Column(nullable=false)
	private Double tcProcBomVersionPartsCount;
	
	@Desc("小部件个数")
	@Column(nullable=false)
	private Double tcProcBomVersionPartsSubsCount;
	
	@Desc("部件重量")
	@Column(nullable=false)
	private Double tcProcBomVersionPartsWeight;

	@Desc("是否删除")//0,正常，1删除
	@Column(nullable=false,columnDefinition="int default 0")
	private Integer isDeleted;
	
	@Desc("小部件是否需要排序")//0：需要，1：不需要
	@Column(nullable=false,columnDefinition="int default 1")
	private Integer needSort;
	
	/**
	 * 0，null:不可用，1：可用
	 */
	@Desc("图纸BOM状态")
	@Column(nullable=true)
	private Integer drawingsComplete;
	
	@Desc("组套状态")
	@Column(nullable=true)
	private Integer suitComplete;

	@Desc("客户物料号")
	@Column(nullable=true)
	private String customerMaterialCode;

	public String getCustomerMaterialCode() {
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode) {
		this.customerMaterialCode = customerMaterialCode;
	}

	public Long getTcProcBomVersoinId() {
		return tcProcBomVersoinId;
	}

	public void setTcProcBomVersoinId(Long tcProcBomVersoinId) {
		this.tcProcBomVersoinId = tcProcBomVersoinId;
	}

	public void setTcProcBomVersionParentParts(Long tcProcBomVersionParentParts) {
		this.tcProcBomVersionParentParts = tcProcBomVersionParentParts;
	}

	public String getTcProcBomVersionPartsName() {
		return tcProcBomVersionPartsName;
	}

	public void setTcProcBomVersionPartsName(String tcProcBomVersionPartsName) {
		this.tcProcBomVersionPartsName = tcProcBomVersionPartsName;
	}

	

	public Long getTcProcBomVersionParentParts() {
		return tcProcBomVersionParentParts;
	}

	public String getTcProcBomVersionPartsCutCode() {
		return tcProcBomVersionPartsCutCode;
	}

	public void setTcProcBomVersionPartsCutCode(String tcProcBomVersionPartsCutCode) {
		this.tcProcBomVersionPartsCutCode = tcProcBomVersionPartsCutCode;
	}

	public Double getTcProcBomVersionPartsCount() {
		return tcProcBomVersionPartsCount;
	}

	public void setTcProcBomVersionPartsCount(Double tcProcBomVersionPartsCount) {
		this.tcProcBomVersionPartsCount = tcProcBomVersionPartsCount;
	}

	public Double getTcProcBomVersionPartsSubsCount() {
		return tcProcBomVersionPartsSubsCount;
	}

	public void setTcProcBomVersionPartsSubsCount(
			Double tcProcBomVersionPartsSubsCount) {
		this.tcProcBomVersionPartsSubsCount = tcProcBomVersionPartsSubsCount;
	}

	public Double getTcProcBomVersionPartsWeight() {
		return tcProcBomVersionPartsWeight;
	}

	public void setTcProcBomVersionPartsWeight(Double tcProcBomVersionPartsWeight) {
		this.tcProcBomVersionPartsWeight = tcProcBomVersionPartsWeight;
	}

	public String getTcProcBomVersionPartsType() {
		return tcProcBomVersionPartsType;
	}

	public void setTcProcBomVersionPartsType(String tcProcBomVersionPartsType) {
		this.tcProcBomVersionPartsType = tcProcBomVersionPartsType;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getNeedSort() {
		return needSort;
	}

	public void setNeedSort(Integer needSort) {
		this.needSort = needSort;
	}

	public Integer getDrawingsComplete() {
		return drawingsComplete;
	}

	public void setDrawingsComplete(Integer drawingsComplete) {
		this.drawingsComplete = drawingsComplete;
	}

	public Integer getSuitComplete() {
		return suitComplete;
	}

	public void setSuitComplete(Integer suitComplete) {
		this.suitComplete = suitComplete;
	}

	public String getTcProcBomVersionMaterialNumber() {
		return tcProcBomVersionMaterialNumber;
	}

	public void setTcProcBomVersionMaterialNumber(
			String tcProcBomVersionMaterialNumber) {
		this.tcProcBomVersionMaterialNumber = tcProcBomVersionMaterialNumber;
	}
	
	
	
}

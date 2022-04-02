package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
@Desc("部件成品重量胚布信息")
@Entity
@Table(name="HS_TC_PROC_BOM_VERSION_PARTS_FINISHED_WEIGHT_EMBRYO_CLOTH")
@DynamicInsert
public class TcBomVersionPartsFinishedWeightEmbryoCloth extends BaseEntity {
	@Desc("部件信息")
	@Column(nullable=false)
	@Index(name="tcProcBomPartsId")
	private Long tcProcBomPartsId;
	
	@Desc("物料代码")
	@Column(nullable=false,unique=true)
	private String materialNumber;
	
	@Desc("胚布名称")
	@Column(nullable=false)
	private String embryoClothName;

	@Desc("重量")
	@Column(nullable=false)
	private Double weight;

	public Long getTcProcBomPartsId() {
		return tcProcBomPartsId;
	}

	public void setTcProcBomPartsId(Long tcProcBomPartsId) {
		this.tcProcBomPartsId = tcProcBomPartsId;
	}

	public String getMaterialNumber() {
		return materialNumber;
	}

	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}

	public String getEmbryoClothName() {
		return embryoClothName;
	}

	public void setEmbryoClothName(String embryoClothName) {
		this.embryoClothName = embryoClothName;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	
}

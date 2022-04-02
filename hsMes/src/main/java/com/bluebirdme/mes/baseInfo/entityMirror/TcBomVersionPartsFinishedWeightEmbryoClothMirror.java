package com.bluebirdme.mes.baseInfo.entityMirror;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Desc("部件成品重量胚布信息")
@Entity
@Table(name="HS_TC_PROC_BOM_VERSION_PARTS_FINISHED_WEIGHT_EMBRYO_CLOTH_MIRROR")
@DynamicInsert
public class TcBomVersionPartsFinishedWeightEmbryoClothMirror extends BaseEntity {

	@Desc("部件成品重量胚布信息ID")
	@Column
	private Long embryoClothMirrorId;

	@Desc("部件信息")
	@Column(nullable=false)
	@Index(name="tcProcBomPartsId")
	private Long tcProcBomPartsId;
	
	@Desc("物料代码")
	@Column(nullable=false)
	private String materialNumber;
	
	@Desc("胚布名称")
	@Column(nullable=false)
	private String embryoClothName;

	@Desc("重量")
	@Column(nullable=false)
	private Double weight;

	@Desc("镜像时间")
	@Column
	private Date gmtCreate;

	@Desc("订单ID")
	@Column
	private Long salesOrderId;


	public Long getEmbryoClothMirrorId() {
		return embryoClothMirrorId;
	}

	public void setEmbryoClothMirrorId(Long embryoClothMirrorId) {
		this.embryoClothMirrorId = embryoClothMirrorId;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}


	public Long getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(Long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

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

package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
@Desc("部件明细")
@Entity
@Table(name="HS_Tc_Proc_Bom_Version_Parts_Detail")
@DynamicInsert
public class TcBomVersionPartsDetail extends BaseEntity {
	@Desc("部件信息")
	@Column(nullable=false)
	@Index(name="tcProcBomPartsId")
	private Long tcProcBomPartsId;
	
	/**
	 * 关联的是成品ID
	 */
	@Desc("坯布规格")
	@Column(nullable=false)
	private Long tcFinishedProductId;
	
	@Desc("米长")
	@Column(nullable=true)
	private Double length;
	
	@Desc("图号")
	@Column(nullable=false)
	private String drawingNo;
	
	@Desc("层数")
	@Column
	private String levelNo;
	
	@Desc("卷号")
	@Column
	private String rollNo;
	
	@Desc("每托卷数")
	@Column(nullable=false)
	private Double tcProcBomFabricCount;
	
	@Desc("排序")
	@Column(nullable=true)
	private Integer sorting;

	@Desc("理论耗用重量")
	@Column
	private Double tcTheoreticalWeigh;
	
	
	
	public Double getTcTheoreticalWeigh() {
		return tcTheoreticalWeigh;
	}


	public void setTcTheoreticalWeigh(Double tcTheoreticalWeigh) {
		this.tcTheoreticalWeigh = tcTheoreticalWeigh;
	}


	public Double getLength() {
		return length;
	}


	public void setLength(Double length) {
		this.length = length;
	}


	public String getDrawingNo() {
		return drawingNo;
	}


	public void setDrawingNo(String drawingNo) {
		this.drawingNo = drawingNo;
	}


	public Long getTcProcBomPartsId() {
		return tcProcBomPartsId;
	}


	public void setTcProcBomPartsId(Long tcProcBomPartsId) {
		this.tcProcBomPartsId = tcProcBomPartsId;
	}


	public Long getTcFinishedProductId() {
		return tcFinishedProductId;
	}


	public void setTcFinishedProductId(Long tcFinishedProductId) {
		this.tcFinishedProductId = tcFinishedProductId;
	}


	public Double getTcProcBomFabricCount() {
		return tcProcBomFabricCount;
	}


	public void setTcProcBomFabricCount(Double tcProcBomFabricCount) {
		this.tcProcBomFabricCount = tcProcBomFabricCount;
	}


	public String getLevelNo() {
		return levelNo;
	}


	public void setLevelNo(String levelNo) {
		this.levelNo = levelNo;
	}


	public String getRollNo() {
		return rollNo;
	}


	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}


	public Integer getSorting() {
		return sorting;
	}


	public void setSorting(Integer sorting) {
		this.sorting = sorting;
	}
	
}

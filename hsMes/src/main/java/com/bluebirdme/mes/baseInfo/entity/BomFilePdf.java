package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("Bom转PDF文件信息")
@Entity
@Table(name = "HS_BOM_FILE_PDF")
public class BomFilePdf extends BaseEntity {
	@Column
	private Long bomFileId;
	
	@Column
	private String PDFfilePath;

	public Long getBomFileId() {
		return bomFileId;
	}

	public void setBomFileId(Long bomFileId) {
		this.bomFileId = bomFileId;
	}

	public String getPDFfilePath() {
		return PDFfilePath;
	}

	public void setPDFfilePath(String pDFfilePath) {
		PDFfilePath = pDFfilePath;
	}
	
	
}

package com.bluebirdme.mes.baseInfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import java.util.Date;

@Desc("Bom文件信息")
@Entity
@Table(name = "HS_BOM_FILE")
public class BomFile extends BaseEntity {
	@Column
	private String bomName;
	
	@Column
	private String processBomCode;
	
	@Column
	private String processBomVersion;
	
	@Column
	private String bcBomCode;
	
	@Column
	private String bcbomVersion;
	
	@Column
	private String fileName;
	
	@Column
	private String filePath;

	@Column
	private Date uploadTime;

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getProcessBomCode() {
		return processBomCode;
	}

	public void setProcessBomCode(String processBomCode) {
		this.processBomCode = processBomCode;
	}

	public String getProcessBomVersion() {
		return processBomVersion;
	}

	public void setProcessBomVersion(String processBomVersion) {
		this.processBomVersion = processBomVersion;
	}

	public String getBcBomCode() {
		return bcBomCode;
	}

	public void setBcBomCode(String bcBomCode) {
		this.bcBomCode = bcBomCode;
	}

	public String getBcbomVersion() {
		return bcbomVersion;
	}

	public void setBcbomVersion(String bcbomVersion) {
		this.bcbomVersion = bcbomVersion;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
}

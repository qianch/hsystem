package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Entity
@Table(name = "platform_attachment")
public class Attachment extends BaseEntity {
    @Column(nullable = false, name = "fileName")
    private String fileName;

    @Column(nullable = false, name = "fileUUIDName")
    private String fileUUIDName;

    @Column(name = "suffix")
    private String suffix;

    @Column(nullable = false, name = "fileSize")
    private Long fileSize;

    @Column(nullable = false, name = "md5")
    private String md5;

    @Column(name = "shortFilePath")
    private String shortFilePath;

    @Column(nullable = false, name = "filePath")
    private String filePath;

    @Column(nullable = false, name = "uploadTime")
    private Date uploadTime;

    public Attachment() {
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUUIDName() {
        return this.fileUUIDName;
    }

    public void setFileUUIDName(String fileUUIDName) {
        this.fileUUIDName = fileUUIDName;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getShortFilePath() {
        return this.shortFilePath;
    }

    public void setShortFilePath(String shortFilePath) {
        this.shortFilePath = shortFilePath;
    }

    public Date getUploadTime() {
        return this.uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}

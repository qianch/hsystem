package com.bluebirdme.mes.mobile.common.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * App版本管理
 *
 * @author Goofy
 * @Date 2016年11月6日 上午10:17:47
 */
@Desc("PDA终端版本")
@Entity
@Table(name = "hs_app_version")
public class AppVersion extends BaseEntity {

    @Desc("版本号")
    @Column(nullable = false)
    private String version;

    @Desc("版本说明")
    @Column(nullable = true)
    private String versionMemo;

    @Desc("上传时间")
    @Column(nullable = false)
    private Date uploadTime;

    @Desc("路径")
    @Column(nullable = false)
    private String path;

    /**
     * 0:旧版本，1：新版本
     */
    @Desc("是否最新")
    @Column(nullable = false)
    private Integer isLatest;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getIsLatest() {
        return isLatest;
    }

    public void setIsLatest(Integer isLatest) {
        this.isLatest = isLatest;
    }

    public String getVersionMemo() {
        return versionMemo;
    }

    public void setVersionMemo(String versionMemo) {
        this.versionMemo = versionMemo;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

}

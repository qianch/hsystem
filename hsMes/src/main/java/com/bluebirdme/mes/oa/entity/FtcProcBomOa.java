package com.bluebirdme.mes.oa.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("非套材bom")
@Entity
@Table(name = "hs_ftc_proc_bom_oa")
public class FtcProcBomOa extends BaseEntity {
    @Desc("BOM名称")
    @Column(name = "ftcProcBomName")
    private String ftcProcBomName;

    @Desc("BOM代码")
    @Column(name = "ftcProcBomCode")
    private String ftcProcBomCode;

    @Desc("客户信息")
    @Column(name = "ftcProcBomConsumerId")
    private Long ftcProcBomConsumerId;

    @Desc("试样工艺")
    @Column(name = "isTestPro")
    private Integer isTestPro;

    @Desc("版本号")
    @Column(name = "ftcProcBomVersionCode")
    private Integer ftcProcBomVersionCode;

    @Desc("客户版本号")
    @Column(name = "ftcConsumerVersionCode")
    private Integer ftcConsumerVersionCode;

    @Desc("添加状态")
    @Column(name = "status")
    private String status;

    @Desc("工艺文件Excel访问路径")
    @Column(name = "tcProcBomPath")
    private String tcProcBomPath;

    @Desc("报错原因")
    @Column(name = "reason")
    private String reason;

    public String getTcProcBomPath() {
        return tcProcBomPath;
    }

    public void setTcProcBomPath(String tcProcBomPath) {
        this.tcProcBomPath = tcProcBomPath;
    }

    public String getFtcProcBomName() {
        return ftcProcBomName;
    }

    public void setFtcProcBomName(String ftcProcBomName) {
        this.ftcProcBomName = ftcProcBomName;
    }

    public String getFtcProcBomCode() {
        return ftcProcBomCode;
    }

    public void setFtcProcBomCode(String ftcProcBomCode) {
        this.ftcProcBomCode = ftcProcBomCode;
    }

    public Long getFtcProcBomConsumerId() {
        return ftcProcBomConsumerId;
    }

    public void setFtcProcBomConsumerId(Long ftcProcBomConsumerId) {
        this.ftcProcBomConsumerId = ftcProcBomConsumerId;
    }

    public Integer getIsTestPro() {
        return isTestPro;
    }

    public void setIsTestPro(Integer isTestPro) {
        this.isTestPro = isTestPro;
    }

    public Integer getFtcProcBomVersionCode() {
        return ftcProcBomVersionCode;
    }

    public void setFtcProcBomVersionCode(Integer ftcProcBomVersionCode) {
        this.ftcProcBomVersionCode = ftcProcBomVersionCode;
    }

    public Integer getFtcConsumerVersionCode() {
        return ftcConsumerVersionCode;
    }

    public void setFtcConsumerVersionCode(Integer ftcConsumerVersionCode) {
        this.ftcConsumerVersionCode = ftcConsumerVersionCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

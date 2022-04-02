package com.bluebirdme.mes.oa.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("套材BOM")
@Entity
@Table(name = "hs_tc_proc_bom_oa")
public class TcBomOa extends BaseEntity {
    @Desc("BOM名称")
    @Column(nullable = false)
    private String tcProcBomName;

    @Desc("BOM代码")
    @Column(nullable = false)
    @Index(name = "tcProcBomCode")
    private String tcProcBomCode;

    @Desc("客户信息")
    @Column(nullable = false)
    private Integer tcProcBomConsumerId;
    //0常规 1变更试样2新品试样
    @Desc("试样工艺")
    @Column(nullable = false)
    @Index(name = "isTestPro")
    private Integer isTestPro;
    @Desc("版本号")
    @Column(name = "tcProcBomVersionCode")
    private String tcProcBomVersionCode;
    @Desc("客户版本号")
    @Column(name = "tcConsumerVersionCode")
    private String tcConsumerVersionCode;
    @Desc("工艺文件Excel访问路径")
    @Column(name = "tcProcBomPath")
    private String tcProcBomPath;
    @Desc("节点状态 0未添加  1已添加")
    @Column(nullable = false)
    private Integer status;
    @Desc("错误原因")
    @Column(name = "reason")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTcProcBomName() {
        return tcProcBomName;
    }

    public void setTcProcBomName(String tcProcBomName) {
        this.tcProcBomName = tcProcBomName;
    }

    public String getTcProcBomCode() {
        return tcProcBomCode;
    }

    public void setTcProcBomCode(String tcProcBomCode) {
        this.tcProcBomCode = tcProcBomCode;
    }

    public Integer getTcProcBomConsumerId() {
        return tcProcBomConsumerId;
    }

    public void setTcProcBomConsumerId(Integer tcProcBomConsumerId) {
        this.tcProcBomConsumerId = tcProcBomConsumerId;
    }

    public Integer getIsTestPro() {
        return isTestPro;
    }

    public void setIsTestPro(Integer isTestPro) {
        this.isTestPro = isTestPro;
    }

    public String getTcProcBomVersionCode() {
        return tcProcBomVersionCode;
    }

    public void setTcProcBomVersionCode(String tcProcBomVersionCode) {
        this.tcProcBomVersionCode = tcProcBomVersionCode;
    }

    public String getTcConsumerVersionCode() {
        return tcConsumerVersionCode;
    }

    public void setTcConsumerVersionCode(String tcConsumerVersionCode) {
        this.tcConsumerVersionCode = tcConsumerVersionCode;
    }

    public String getTcProcBomPath() {
        return tcProcBomPath;
    }

    public void setTcProcBomPath(String tcProcBomPath) {
        this.tcProcBomPath = tcProcBomPath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

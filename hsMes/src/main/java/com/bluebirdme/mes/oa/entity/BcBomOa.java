package com.bluebirdme.mes.oa.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("包材bom")
@Entity
@Table(name = "hs_bc_bom_oa")
public class BcBomOa extends BaseEntity {
    @Desc("总称")
    @Column(name = "packBomGenericName")
    private String packBomGenericName;
    @Desc("包装标准代码")
    @Column(name = "packBomCode")
    private String packBomCode;
    @Desc("包装大类")
    @Column(name = "packBomType")
    private String packBomType;
    @Desc("适用客户")
    @Column(name = "packBomConsumerId")
    private Long packBomConsumerId;
    @Desc("产品规格")
    @Column(name = "packBomModel")
    private String packBomModel;
    @Desc("门幅")
    @Column(name = "packBomWidth")
    private String packBomWidth;
    @Desc("卷长")
    @Column(name = "packBomLength")
    private String packBomLength;
    @Desc("卷重")
    @Column(name = "packBomWeight")
    private String packBomWeight;
    @Desc("卷径")
    @Column(name = "packBomRadius")
    private String packBomRadius;
    @Desc("每箱卷数")
    @Column(name = "packBomRollsPerBox")
    private Double packBomRollsPerBox;
    @Desc("每托箱数")
    @Column(name = "packBomBoxesPerTray")
    private Integer packBomBoxesPerTray;
    @Desc("每托卷数")
    @Column(name = "packBomRollsPerTray")
    private Double packBomRollsPerTray;
    @Desc("试样工艺 0:常规产品 1: 变更试用 2: 新品试样")
    @Column(name = "isTestPro")
    private Integer isTestPro;
    @Desc("版本号")
    @Column(name = "packVersion")
    private String packVersion;
    @Desc("工艺文件Excel访问路径")
    @Column(name = "packBomPath")
    private String packBomPath;
    @Desc("标示状态，0：未添加 1.已添加")
    @Column(name = "status")
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

    public String getPackBomGenericName() {
        return packBomGenericName;
    }

    public void setPackBomGenericName(String packBomGenericName) {
        this.packBomGenericName = packBomGenericName;
    }

    public String getPackBomCode() {
        return packBomCode;
    }

    public void setPackBomCode(String packBomCode) {
        this.packBomCode = packBomCode;
    }

    public String getPackBomType() {
        return packBomType;
    }

    public void setPackBomType(String packBomType) {
        this.packBomType = packBomType;
    }

    public Long getPackBomConsumerId() {
        return packBomConsumerId;
    }

    public void setPackBomConsumerId(Long packBomConsumerId) {
        this.packBomConsumerId = packBomConsumerId;
    }

    public String getPackBomModel() {
        return packBomModel;
    }

    public void setPackBomModel(String packBomModel) {
        this.packBomModel = packBomModel;
    }

    public String getPackBomWidth() {
        return packBomWidth;
    }

    public void setPackBomWidth(String packBomWidth) {
        this.packBomWidth = packBomWidth;
    }

    public String getPackBomLength() {
        return packBomLength;
    }

    public void setPackBomLength(String packBomLength) {
        this.packBomLength = packBomLength;
    }

    public String getPackBomWeight() {
        return packBomWeight;
    }

    public void setPackBomWeight(String packBomWeight) {
        this.packBomWeight = packBomWeight;
    }

    public String getPackBomRadius() {
        return packBomRadius;
    }

    public void setPackBomRadius(String packBomRadius) {
        this.packBomRadius = packBomRadius;
    }

    public Double getPackBomRollsPerBox() {
        return packBomRollsPerBox;
    }

    public void setPackBomRollsPerBox(Double packBomRollsPerBox) {
        this.packBomRollsPerBox = packBomRollsPerBox;
    }

    public Integer getPackBomBoxesPerTray() {
        return packBomBoxesPerTray;
    }

    public void setPackBomBoxesPerTray(Integer packBomBoxesPerTray) {
        this.packBomBoxesPerTray = packBomBoxesPerTray;
    }

    public Double getPackBomRollsPerTray() {
        return packBomRollsPerTray;
    }

    public void setPackBomRollsPerTray(Double packBomRollsPerTray) {
        this.packBomRollsPerTray = packBomRollsPerTray;
    }

    public Integer getIsTestPro() {
        return isTestPro;
    }

    public void setIsTestPro(Integer isTestPro) {
        this.isTestPro = isTestPro;
    }

    public String getPackVersion() {
        return packVersion;
    }

    public void setPackVersion(String packVersion) {
        this.packVersion = packVersion;
    }

    public String getPackBomPath() {
        return packBomPath;
    }

    public void setPackBomPath(String packBomPath) {
        this.packBomPath = packBomPath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

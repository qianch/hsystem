package com.bluebirdme.mes.oa.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("非套材包材bom")
@Entity
@Table(name = "hs_bc_ftcbom_oa")
public class FtcBomOa extends BaseEntity {
    @Desc("包装标准代码（1）")
    @Column(name = "level1_code")
    private String level1Code;
    @Desc("产品规格（1）")
    @Column(name = "level1_name")
    private String level1Name;
    @Desc("二级包装工艺代码（2）")
    @Column(name = "level2_code")
    private String level2Code;
    @Desc("厂内名称（2）")
    @Column(name = "level2_name")
    private String level2Name;
    @Desc("三级包装工艺代码（3）")
    @Column(name = "level3_code")
    private String level3Code;
    @Desc("产品包装名称（3）")
    @Column(name = "level3_name")
    private String level3Name;
    @Desc("版本号")
    @Column(name = "version")
    private String version;
    @Desc("包材总重")
    @Column(name = "bcTotalWeight")
    private String bcTotalWeight;
    @Desc("产品类别")
    @Column(name = "productType")
    private String productType;
    @Desc("客户名称")
    @Column(name = "consumerId")
    private Long consumerId;
    @Desc("卷长")
    @Column(name = "rollDiameter")
    private String rollDiameter;
    @Desc("托长")
    @Column(name = "palletLength")
    private String palletLength;
    @Desc("托宽")
    @Column(name = "palletWidth")
    private String palletWidth;
    @Desc("每托卷数")
    @Column(name = "rollsPerPallet")
    private String rollsPerPallet;
    @Desc("托高")
    @Column(name = "palletHeight")
    private String palletHeight;
    @Desc("塑料膜要求")
    @Column(name = "requirement_suliaomo")
    private String requirementSuLiaomMo;
    @Desc("摆放要求")
    @Column(name = "requirement_baifang")
    private String requirementBaiFang;
    @Desc("打包带要求")
    @Column(name = "requirement_dabaodai")
    private String requirementDaBaoDai;
    @Desc("标签要求")
    @Column(name = "requirement_biaoqian")
    private String requirementBiaoQian;
    @Desc("小标签要求")
    @Column(name = "requirement_xiaobiaoqian")
    private String requirementXiaoBiaoQian;
    @Desc("卷标签要求")
    @Column(name = "requirement_juanbiaoqian")
    private String requirementJuanBiaoQian;
    @Desc("托标签要求")
    @Column(name = "requirement_tuobiaoqian")
    private String requirementTuoBiaoQian;
    @Desc("缠绕要求")
    @Column(name = "requirement_chanrao")
    private String requirement_chanrao;
    @Desc("其他")
    @Column(name = "requirement_other")
    private String requirementOther;
    @Desc("工艺文件Excel访问路径")
    @Column(name = "ftcPackBomPath")
    private String ftcPackBomPath;
    @Desc("添加状态")
    @Column(name = "status")
    private String status;
    @Desc("报错原因")
    @Column(name = "reason")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel1Code() {
        return level1Code;
    }

    public void setLevel1Code(String level1Code) {
        this.level1Code = level1Code;
    }

    public String getLevel1Name() {
        return level1Name;
    }

    public void setLevel1Name(String level1Name) {
        this.level1Name = level1Name;
    }

    public String getLevel2Code() {
        return level2Code;
    }

    public void setLevel2Code(String level2Code) {
        this.level2Code = level2Code;
    }

    public String getLevel2Name() {
        return level2Name;
    }

    public void setLevel2Name(String level2Name) {
        this.level2Name = level2Name;
    }

    public String getLevel3Code() {
        return level3Code;
    }

    public void setLevel3Code(String level3Code) {
        this.level3Code = level3Code;
    }

    public String getLevel3Name() {
        return level3Name;
    }

    public void setLevel3Name(String level3Name) {
        this.level3Name = level3Name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBcTotalWeight() {
        return bcTotalWeight;
    }

    public void setBcTotalWeight(String bcTotalWeight) {
        this.bcTotalWeight = bcTotalWeight;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public String getRollDiameter() {
        return rollDiameter;
    }

    public void setRollDiameter(String rollDiameter) {
        this.rollDiameter = rollDiameter;
    }

    public String getPalletLength() {
        return palletLength;
    }

    public void setPalletLength(String palletLength) {
        this.palletLength = palletLength;
    }

    public String getPalletWidth() {
        return palletWidth;
    }

    public void setPalletWidth(String palletWidth) {
        this.palletWidth = palletWidth;
    }

    public String getRollsPerPallet() {
        return rollsPerPallet;
    }

    public void setRollsPerPallet(String rollsPerPallet) {
        this.rollsPerPallet = rollsPerPallet;
    }

    public String getPalletHeight() {
        return palletHeight;
    }

    public void setPalletHeight(String palletHeight) {
        this.palletHeight = palletHeight;
    }

    public String getRequirementSuLiaomMo() {
        return requirementSuLiaomMo;
    }

    public void setRequirementSuLiaomMo(String requirementSuLiaomMo) {
        this.requirementSuLiaomMo = requirementSuLiaomMo;
    }

    public String getRequirementBaiFang() {
        return requirementBaiFang;
    }

    public void setRequirementBaiFang(String requirementBaiFang) {
        this.requirementBaiFang = requirementBaiFang;
    }

    public String getRequirementDaBaoDai() {
        return requirementDaBaoDai;
    }

    public void setRequirementDaBaoDai(String requirementDaBaoDai) {
        this.requirementDaBaoDai = requirementDaBaoDai;
    }

    public String getRequirementBiaoQian() {
        return requirementBiaoQian;
    }

    public void setRequirementBiaoQian(String requirementBiaoQian) {
        this.requirementBiaoQian = requirementBiaoQian;
    }

    public String getRequirementXiaoBiaoQian() {
        return requirementXiaoBiaoQian;
    }

    public void setRequirementXiaoBiaoQian(String requirementXiaoBiaoQian) {
        this.requirementXiaoBiaoQian = requirementXiaoBiaoQian;
    }

    public String getRequirementJuanBiaoQian() {
        return requirementJuanBiaoQian;
    }

    public void setRequirementJuanBiaoQian(String requirementJuanBiaoQian) {
        this.requirementJuanBiaoQian = requirementJuanBiaoQian;
    }

    public String getRequirementTuoBiaoQian() {
        return requirementTuoBiaoQian;
    }

    public void setRequirementTuoBiaoQian(String requirementTuoBiaoQian) {
        this.requirementTuoBiaoQian = requirementTuoBiaoQian;
    }

    public String getRequirement_chanrao() {
        return requirement_chanrao;
    }

    public void setRequirement_chanrao(String requirement_chanrao) {
        this.requirement_chanrao = requirement_chanrao;
    }

    public String getRequirementOther() {
        return requirementOther;
    }

    public void setRequirementOther(String requirementOther) {
        this.requirementOther = requirementOther;
    }

    public String getFtcPackBomPath() {
        return ftcPackBomPath;
    }

    public void setFtcPackBomPath(String ftcPackBomPath) {
        this.ftcPackBomPath = ftcPackBomPath;
    }
}

package com.bluebirdme.mes.baseInfo.entity;


import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 包材bom
 *
 * @author 徐波
 * @Date 2016年10月08日 13:02
 */
@Desc("包材bom")
@Entity
@Table(name = "HS_BC_BOM")
public class BcBom extends BaseEntity {
    @Desc("总称")
    @Column(name = "packBomGenericName", nullable = false)
    private String packBomGenericName;

    @Desc("包装名称")
    @Column(name = "packBomName")
    private String packBomName;

    @Index(name = "packBomCode")
    @Desc("包装标准代码")
    @Column(name = "packBomCode", nullable = false)
    private String packBomCode;

    @Desc("包装大类")
    @Column(name = "packBomType", nullable = false)
    private String packBomType;

    @Desc("适用客户")
    @Column(name = "packBomConsumerId", nullable = false)
    private Long packBomConsumerId;

    @Desc("产品规格")
    @Column(name = "packBomModel", nullable = false)
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

    @Desc("试样工艺")
    @Column(nullable = false)
    @Index(name = "isTestPro")
    private Integer isTestPro;

    public Integer getIsTestPro() {
        return isTestPro;
    }

    public void setIsTestPro(Integer isTestPro) {
        this.isTestPro = isTestPro;
    }

    public String getPackBomGenericName() {
        return packBomGenericName;
    }

    public void setPackBomGenericName(String packBomGenericName) {
        this.packBomGenericName = packBomGenericName;
    }

    public String getPackBomName() {
        return packBomName;
    }

    public void setPackBomName(String packBomName) {
        this.packBomName = packBomName;
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

    public Integer getPackBomBoxesPerTray() {
        return packBomBoxesPerTray;
    }

    public void setPackBomBoxesPerTray(Integer packBomBoxesPerTray) {
        this.packBomBoxesPerTray = packBomBoxesPerTray;
    }

    public Double getPackBomRollsPerBox() {
        return packBomRollsPerBox;
    }

    public void setPackBomRollsPerBox(Double packBomRollsPerBox) {
        this.packBomRollsPerBox = packBomRollsPerBox;
    }

    public Double getPackBomRollsPerTray() {
        return packBomRollsPerTray;
    }

    public void setPackBomRollsPerTray(Double packBomRollsPerTray) {
        this.packBomRollsPerTray = packBomRollsPerTray;
    }

    @Desc("是否作废，-1作废")
    @Column(name = "packCanceled")
    private Integer packCanceled;

    public Integer getPackCanceled() {
        return packCanceled;
    }

    public void setPackCanceled(Integer packCanceled) {
        this.packCanceled = packCanceled;
    }
}

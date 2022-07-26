package com.bluebirdme.mes.planner.material.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 套材物料需求计划
 *
 * @author Goofy
 * @Date 2016年10月19日 下午1:27:23
 */
@Desc("非套材物料需求计划")
@Entity
@Table(name = "HS_FTC_MATERIAL_REQUIREMENT_PLAN")
public class FtcMaterialRequirementPlan extends BaseEntity {
    @Desc("生产计划ID")
    @Column(nullable = false)
    @Index(name = "producePlanId")
    private Long producePlanId;

    @Desc("物料名称")
    @Column
    private String materialName;

    @Desc("规格型号")
    @Column
    private String materialModel;

    @Desc("总重")
    @Column(nullable = false)
    private Double materialTotalWeight;

    public Long getProducePlanId() {
        return producePlanId;
    }

    public void setProducePlanId(Long producePlanId) {
        this.producePlanId = producePlanId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public Double getMaterialTotalWeight() {
        return materialTotalWeight;
    }

    public void setMaterialTotalWeight(Double materialTotalWeight) {
        this.materialTotalWeight = materialTotalWeight;
    }
}

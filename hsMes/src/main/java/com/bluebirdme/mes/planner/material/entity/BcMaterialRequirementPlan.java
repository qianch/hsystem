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
@Desc("包材物料需求计划")
@Entity
@Table(name = "HS_BC_MATERIAL_REQUIREMENT_PLAN")
public class BcMaterialRequirementPlan extends BaseEntity {
    @Desc("生产计划ID")
    @Index(name = "producePlanId")
    @Column(nullable = false)
    private Long producePlanId;

    @Desc("包材名称")
    @Column
    private String packMaterialName;

    @Desc("规格型号")
    @Column
    private String packMaterialModel;

    @Desc("材质")
    @Column
    private String packMaterialAttr;

    @Desc("数量")
    @Column(nullable = false)
    private Double packMateriaTotalCount;

    @Desc("单位")
    @Column
    private String packMaterialUnit;

    @Desc("包装单位")
    @Column
    private String packUnit;
    @Desc("物料代码")
    @Column
    private String mtCode;
    @Desc("标准码")
    @Column
    private String stCode;

    public Long getProducePlanId() {
        return producePlanId;
    }

    public void setProducePlanId(Long producePlanId) {
        this.producePlanId = producePlanId;
    }

    public String getPackMaterialName() {
        return packMaterialName;
    }

    public void setPackMaterialName(String packMaterialName) {
        this.packMaterialName = packMaterialName;
    }

    public String getPackMaterialModel() {
        return packMaterialModel;
    }

    public void setPackMaterialModel(String packMaterialModel) {
        this.packMaterialModel = packMaterialModel;
    }

    public String getPackMaterialAttr() {
        return packMaterialAttr;
    }

    public void setPackMaterialAttr(String packMaterialAttr) {
        this.packMaterialAttr = packMaterialAttr;
    }

    public Double getPackMateriaTotalCount() {
        return packMateriaTotalCount;
    }

    public void setPackMateriaTotalCount(Double packMateriaTotalCount) {
        this.packMateriaTotalCount = packMateriaTotalCount;
    }

    public String getPackMaterialUnit() {
        return packMaterialUnit;
    }

    public void setPackMaterialUnit(String packMaterialUnit) {
        this.packMaterialUnit = packMaterialUnit;
    }

    public String getPackUnit() {
        return packUnit;
    }

    public void setPackUnit(String packUnit) {
        this.packUnit = packUnit;
    }

    public String getMtCode() {
        return mtCode;
    }

    public void setMtCode(String mtCode) {
        this.mtCode = mtCode;
    }

    public String getStCode() {
        return stCode;
    }

    public void setStCode(String stCode) {
        this.stCode = stCode;
    }
}

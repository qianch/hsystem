package com.bluebirdme.mes.planner.material.entity;

public class FtcMaterialInfo {
    //"物料名称"
    private String materialName;

    //"规格型号"
    private String materialModel;

    //"总重"
    private Double materialTotalWeight;

    //"总重"
    private Double materialStockWeight;

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

    public Double getMaterialStockWeight() {
        return materialStockWeight;
    }

    public void setMaterialStockWeight(Double materialStockWeight) {
        this.materialStockWeight = materialStockWeight;
    }
}

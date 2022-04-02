package com.bluebirdme.mes.baseInfo.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.core.dev.DevHelper;

/**
 * 原料表
 * @author Goofy
 * @Date 2016年10月12日 上午11:01:35
 */
@Desc("原料")
@Entity
@Table(name="hs_material",
	uniqueConstraints={
		@UniqueConstraint(columnNames={"produceCategory","materialModel"},name="uk_material_type"),
		@UniqueConstraint(columnNames="materialCode",name="uk_materialCode")
	}
)
public class Material extends BaseEntity {
	
	@Desc("编码")
	@Column(nullable=false)
	private String materialCode;
	
	@Desc("产品大类")
	@Column(nullable=false)
	private String produceCategory;
	
	@Desc("规格型号")
	@Column(nullable=false)
	private String materialModel;
	
	@Desc("计量单位")
	@Column(nullable=true)
	private String materialMeasure;
	
	@Desc("最低库存")
	@Column(nullable=false)
	private BigDecimal materialMinStock;
	
	@Desc("最大库存")
	@Column(nullable=false)
	private BigDecimal materialMaxStock;
	
	@Desc("备注")
	@Column(nullable=true)
	private String materialMemo;
	
	@Desc("上偏差")
	@Column(nullable=true)
	private Integer upperDeviation;
	
	@Desc("下偏差")
	@Column(nullable=true)
	private Integer lowerDeviation;
	
	@Desc("接头方式")
	@Column(nullable=true)
	private String subWay;
	
	@Desc("保质期")
	@Column(nullable=true)
	private Integer materialShelfLife;
	
	@Desc("制成率")
	@Column(nullable=true)
	private Double madeRate;
	
	@Desc("总重量")
	@Column(nullable = false,columnDefinition="decimal(30,2) default 0 not null")
	private BigDecimal weight;
	
	public Double getMadeRate() {
		return madeRate;
	}

	public void setMadeRate(Double madeRate) {
		this.madeRate = madeRate;
	}

	
	public Integer getMaterialShelfLife() {
		return materialShelfLife;
	}

	public void setMaterialShelfLife(Integer materialShelfLife) {
		this.materialShelfLife = materialShelfLife;
	}

	public Integer getUpperDeviation() {
		return upperDeviation;
	}

	public void setUpperDeviation(Integer upperDeviation) {
		this.upperDeviation = upperDeviation;
	}

	public Integer getLowerDeviation() {
		return lowerDeviation;
	}

	public void setLowerDeviation(Integer lowerDeviation) {
		this.lowerDeviation = lowerDeviation;
	}

	public String getSubWay() {
		return subWay;
	}

	public void setSubWay(String subWay) {
		this.subWay = subWay;
	}

	public String getProduceCategory() {
		return produceCategory;
	}

	public void setProduceCategory(String produceCategory) {
		this.produceCategory = produceCategory;
	}

	/*public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getMaterialFullName() {
		return materialFullName;
	}

	public void setMaterialFullName(String materialFullName) {
		this.materialFullName = materialFullName;
	}*/

	public String getMaterialModel() {
		return materialModel;
	}

	public void setMaterialModel(String materialModel) {
		this.materialModel = materialModel;
	}

	public String getMaterialMeasure() {
		return materialMeasure;
	}

	public void setMaterialMeasure(String materialMeasure) {
		this.materialMeasure = materialMeasure;
	}


	public String getMaterialMemo() {
		return materialMemo;
	}

	public void setMaterialMemo(String materialMemo) {
		this.materialMemo = materialMemo;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	
	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getMaterialMinStock() {
		return materialMinStock;
	}

	public void setMaterialMinStock(BigDecimal materialMinStock) {
		this.materialMinStock = materialMinStock;
	}

	public BigDecimal getMaterialMaxStock() {
		return materialMaxStock;
	}

	public void setMaterialMaxStock(BigDecimal materialMaxStock) {
		this.materialMaxStock = materialMaxStock;
	}

	public static void main(String[] args) throws Exception {
		DevHelper.genJsJsp(Material.class, "高飞");
	}
}

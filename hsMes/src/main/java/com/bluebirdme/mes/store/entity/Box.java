package com.bluebirdme.mes.store.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;


/**
 * 箱
 * @author 宋黎明
 * @Date 2016年11月08日 上午10:02:34
 */
@Desc("箱")
@Entity
@Table(name="hs_Box")
@DynamicInsert
public class Box extends BaseEntity {
	@Desc("打包时间")
	private Date packagingTime;
	
	@Desc("打包人")
	private Long packagingStaff;
	
	@Column(unique=true)
	@Desc("箱条码号")
	@Index(name="boxBarcode")
	private String boxBarcode;
	
	@Desc("重量")
	private Double weight;
	
	@Desc("批次号")
	private String batchCode;

	@Desc("产品规格")
	private String productModel;

	@Desc("产出机台")
	private String deviceCode;

	@Desc("计划单号")
	private String producePlanCode;

	@Desc("产出车间")
	private String name;

	@Desc("状态")
	@Column(nullable=false)
	private Integer state;

	@Desc("产品质量等级")
	private String rollQualityGradeCode;
	
	
	@Desc("备注")
	@Column(nullable=true,length=16777215)
	private String memo;
	
	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	/**
	 * 1:打包结束
	 * 0：打包未结束
	 */
	@Desc("状态")
	@Column(nullable=false,columnDefinition="int(2) default 1")
	private Integer endPack;
	
	public String getRollQualityGradeCode() {
		return rollQualityGradeCode;
	}

	public void setRollQualityGradeCode(String rollQualityGradeCode) {
		this.rollQualityGradeCode = rollQualityGradeCode;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getProducePlanCode() {
		return producePlanCode;
	}

	public void setProducePlanCode(String producePlanCode) {
		this.producePlanCode = producePlanCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * **get**
	 */
	public Date getPackagingTime() {
		return packagingTime;
	}

	/**
	 * **set**
	 */
	public void setPackagingTime(Date packagingTime) {
		this.packagingTime = packagingTime;
	}

	/**
	 * **get**
	 */
	public Long getPackagingStaff() {
		return packagingStaff;
	}

	/**
	 * **set**
	 */
	public void setPackagingStaff(Long packagingStaff) {
		this.packagingStaff = packagingStaff;
	}

	/**
	 * **get**
	 */
	public String getBoxBarcode() {
		return boxBarcode;
	}

	/**
	 * **set**
	 */
	public void setBoxBarcode(String boxBarcode) {
		this.boxBarcode = boxBarcode;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getEndPack() {
		return endPack;
	}

	public void setEndPack(Integer endPack) {
		this.endPack = endPack;
	}
	
}

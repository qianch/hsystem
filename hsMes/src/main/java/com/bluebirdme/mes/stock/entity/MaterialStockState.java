package com.bluebirdme.mes.stock.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.bluebirdme.mes.baseInfo.entity.Material;
import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("原料库存状态表")
@Entity
@Table(name = "Hs_Material_Stock_State", 
	indexes = { 
			@Index(columnList = "warehouseCode",name="idx_warehouseCode"),
			@Index(columnList = "warehousePosCode",name="idx_warehousePosCode"),
			@Index(columnList = "mid",name="idx_mid"),
			@Index(columnList = "state",name="idx_state"),
			@Index(columnList = "productionDate",name="idx_productionDate"),
			@Index(columnList = "expireDate",name="idx_expireDate"),
			@Index(columnList = "isLocked",name="idx_isLocked"),
			@Index(columnList = "isPass",name="idx_isPass"),
			@Index(columnList = "stockState",name="idx_stockState")
	},
	uniqueConstraints={
			@UniqueConstraint(columnNames="palletCode",name="uk_palletCode")
	}
)
public class MaterialStockState extends BaseEntity {
	/**
	 * 原料信息ID
	 */
	@Desc("状态")
	@Column(nullable = false)
	private Long mid;

	@Desc("托盘编码")
	@Column(nullable = false)
	private String palletCode;

	@Desc("仓库编码")
	@Column(nullable = false)
	private String warehouseCode;

	@Desc("库位编码")
	@Column(nullable = false)
	private String warehousePosCode;

	/**
	 * 0:待检 1:合格 2:不合格
	 */
	@Desc("状态")
	@Column(nullable = false)
	private Integer state;

	/**
	 * 0:正常，1：放行
	 */
	@Desc("是否放行")
	@Column(nullable=false)
	private Integer isPass;

	/**
	 * 0:正常，1：冻结
	 */
	@Desc("是否冻结")
	@Column(nullable=false)
	private Integer isLocked;

	/**
	 * 0:在库 1:不再库
	 */
	@Desc("库存状态")
	@Column(nullable = false)
	private Integer stockState;

	/**
	 * 0:在库 1:不再库
	 */
	@Desc("是否投料")
	private Integer isFeed;

	@Desc("库存状态")
	@Column(nullable = false)
	private Long productionDate;

	@Desc("有效期")
	@Column(nullable = false)
	private Long expireDate;

	@Desc("重量")
	@Column(nullable = false, columnDefinition = "decimal(30,2) not null")
	private BigDecimal weight;
	
	@Desc("上偏差")
	@Column(nullable=true)
	private Integer realUpperDeviation;
	
	@Desc("下偏差")
	@Column(nullable=true)
	private Integer realLowerDeviation;
	
	@Desc("接头方式")
	@Column(nullable=true)
	private String realSubWay;

	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public String getPalletCode() {
		return palletCode;
	}

	public void setPalletCode(String palletCode) {
		this.palletCode = palletCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehousePosCode() {
		return warehousePosCode;
	}

	public void setWarehousePosCode(String warehousePosCode) {
		this.warehousePosCode = warehousePosCode;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIsPass() {
		return isPass;
	}

	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}

	public Integer getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Integer isLocked) {
		this.isLocked = isLocked;
	}

	public Integer getStockState() {
		return stockState;
	}

	public void setStockState(Integer stockState) {
		this.stockState = stockState;
	}

	public Long getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Long productionDate) {
		this.productionDate = productionDate;
	}

	public Long getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Long expireDate) {
		this.expireDate = expireDate;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public Integer getRealUpperDeviation() {
		return realUpperDeviation;
	}

	public void setRealUpperDeviation(Integer realUpperDeviation) {
		this.realUpperDeviation = realUpperDeviation;
	}

	public Integer getRealLowerDeviation() {
		return realLowerDeviation;
	}

	public void setRealLowerDeviation(Integer realLowerDeviation) {
		this.realLowerDeviation = realLowerDeviation;
	}

	public String getRealSubWay() {
		return realSubWay;
	}

	public void setRealSubWay(String realSubWay) {
		this.realSubWay = realSubWay;
	}

	public Integer getIsFeed() {
		return isFeed;
	}

	public void setIsFeed(Integer isFeed) {
		this.isFeed = isFeed;
	}
}

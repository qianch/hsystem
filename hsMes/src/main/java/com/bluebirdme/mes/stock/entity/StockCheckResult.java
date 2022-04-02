package com.bluebirdme.mes.stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 盘点结果
 * @author Goofy
 * @Date 2018年3月6日 下午3:56:10
 */
@Desc("盘库结果表")
@Entity
@Table(name="hs_Stock_Check_Result",indexes={
		
		@Index(columnList="materialPalletCode",name="idx_materialPalletCode"),
		@Index(columnList="productPalletCode",name="idx_productPalletCode"),
		@Index(columnList="checkResult",name="idx_checkResult"),
})
public class StockCheckResult extends BaseEntity {
	
	@Desc("原料条码")
	@Column(nullable=true)
	private String materialPalletCode;
	
	@Desc("托条码")
	@Column(nullable=true)
	private String productPalletCode;
	
	/**
	 * 0.正常
	 * 1.错库
	 * 2.非法入库
	 * 3.非法出库
	 * 4.错库或非法出库
	 */
	@Desc("盘点结果")
	@Column(nullable=false)
	private Integer checkResult;

	@Desc("盘点记录ID")
	@Column(nullable=false)
	private Long cid;

	public String getMaterialPalletCode() {
		return materialPalletCode;
	}

	public void setMaterialPalletCode(String materialPalletCode) {
		this.materialPalletCode = materialPalletCode;
	}

	public String getProductPalletCode() {
		return productPalletCode;
	}

	public void setProductPalletCode(String productPalletCode) {
		this.productPalletCode = productPalletCode;
	}

	public Integer getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(Integer checkResult) {
		this.checkResult = checkResult;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}
	
	
	
	

}

package com.bluebirdme.mes.siemens.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 部件组套
 * @author Goofy
 * @Date 2017年8月8日 下午6:15:28
 */
@Entity
@Table(name="hs_siemens_part_suit")
public class PartSuit extends BaseEntity {
	
	@Desc("部件条码")
	@Column(nullable=false)
	private String partBarcode;
	
	@Desc("小部件条码")
	@Column(nullable=false)
	private String fragmentBarcode;

	public String getPartBarcode() {
		return partBarcode;
	}

	public void setPartBarcode(String partBarcode) {
		this.partBarcode = partBarcode;
	}

	public String getFragmentBarcode() {
		return fragmentBarcode;
	}

	public void setFragmentBarcode(String fragmentBarcode) {
		this.fragmentBarcode = fragmentBarcode;
	}
	
}

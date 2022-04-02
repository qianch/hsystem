package com.bluebirdme.mes.cut.cutTcBom.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanDetails;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Desc("裁剪套材BOM主表")
@Entity
@Table(name="hs_cut_tc_proc_bom_main")
@DynamicInsert
public class CutTcBomMain extends BaseEntity {
	@Desc("BOM代码版本")
	@Column(nullable=false)
	private String tcProcBomCodeVersion;

	@Desc("客户编码")
	private String customerCode;

	@Desc("客户名称")
	private String customerName;

	@Desc("叶型名称")
	private String bladeTypeName;

	@Desc("是否生效")
	@Column
	private Integer state;

	@Desc("创建人")
	@Column(nullable=true)
	private String creater;

	@Desc("创建时间")
	@Column
	private Date createTime;

	@Desc("修改人")
	@Column
	private String  modifyUser;

	@Desc("修改时间")
	@Column
	private Date modifyTime;

	@Transient
	private List<CutTcBomDetail> listCutTcBomDetail;

	/**
	 * BOM代码版本
	 * @return
	 */
	public String getTcProcBomCodeVersion() {
		return tcProcBomCodeVersion;
	}

	public void setTcProcBomCodeVersion(String tcProcBomCodeVersion) {
		this.tcProcBomCodeVersion = tcProcBomCodeVersion;
	}


	/**
	 * 客户编码
	 * @return
	 */
	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}


	/**
	 * 客户名称
	 * @return
	 */
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


	/**
	 * 叶型名称
	 * @return
	 */
	public String getBladeTypeName() {
		return bladeTypeName;
	}

	public void setBladeTypeName(String bladeTypeName) {
		this.bladeTypeName = bladeTypeName;
	}


	/**
	 * 是否生效
	 * @return
	 */
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * 创建人
	 * @return
	 */
	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	/**
	 * 创建时间
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 修改人
	 * @return
	 */
	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	/**
	 * 修改时间
	 * @return
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * 套材列表信息
	 * @return
	 */
	public List<CutTcBomDetail> getListCutTcBomDetail() {
		return listCutTcBomDetail;
	}
	public void setListCutTcBomDetail(List<CutTcBomDetail> listCutTcBomDetail) {
		this.listCutTcBomDetail = listCutTcBomDetail;
	}
}

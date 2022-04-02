/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.entityMirror;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 非套材BOM明细
 * @author 宋黎明
 * @Date 2016年10月09日 上午10:02:34
 */
@Desc("非套材BOM明细")
@Entity
@Table(name="HS_FTC_PROC_BOM_DETAIL_MIRROR")
public class FtcBomDetailMirror extends BaseEntity {

	@Desc("明细ID")
	@Column
	private Long ftcBomVersionDetailId;


	@Desc("原料名称")
	@Column(nullable=false)
	private String ftcBomDetailName;
	
	@Desc("原料规格")
	@Column(nullable=false)
	@Index(name="ftcBomDetailModel")
	private String ftcBomDetailModel;
	
	@Desc("单位面积质量")
	@Column
	private Double ftcBomDetailWeightPerSquareMetre;

	@Desc("总单位面积质量")
	@Column
	private Double ftcBomDetailTotalWeight;
	
	@Desc("物料代码")
	@Column
	private String ftcBomDetailItemNumber;
	
	@Desc("钢筘规格")
	@Column
	private String ftcBomDetailReed;
	
	@Desc("导纱针规格")
	@Column
	private String ftcBomDetailGuideNeedle;
	
	@Desc("备注")
	@Column
	private String ftcBomDetailRemark;
	
	@Desc("非套材BOM版本信息")
	@Column
	private Long ftcBomVersionId;


	@Desc("镜像时间")
	@Column
	private Date gmtCreate;

	@Desc("订单Id")
	private Long salesOrderId;

	public Long getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(Long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public Long getFtcBomVersionDetailId() {
		return ftcBomVersionDetailId;
	}

	public void setFtcBomVersionDetailId(Long ftcBomVersionDetailId) {
		this.ftcBomVersionDetailId = ftcBomVersionDetailId;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	/**
	 * **get**
	 */
	public String getFtcBomDetailName() {
		return ftcBomDetailName;
	}

	/**
	 * **set**
	 */
	public void setFtcBomDetailName(String ftcBomDetailName) {
		this.ftcBomDetailName = ftcBomDetailName;
	}

	/**
	 * **get**
	 */
	public String getFtcBomDetailModel() {
		return ftcBomDetailModel;
	}

	/**
	 * **set**
	 */
	public void setFtcBomDetailModel(String ftcBomDetailModel) {
		this.ftcBomDetailModel = ftcBomDetailModel;
	}

	/**
	 * **get**
	 */
	public Double getFtcBomDetailWeightPerSquareMetre() {
		return ftcBomDetailWeightPerSquareMetre;
	}

	/**
	 * **set**
	 */
	public void setFtcBomDetailWeightPerSquareMetre(Double ftcBomDetailWeightPerSquareMetre) {
		this.ftcBomDetailWeightPerSquareMetre = ftcBomDetailWeightPerSquareMetre;
	}

	/**
	 * **get**
	 */
	public Double getFtcBomDetailTotalWeight() {
		return ftcBomDetailTotalWeight;
	}

	/**
	 * **set**
	 */
	public void setFtcBomDetailTotalWeight(Double ftcBomDetailTotalWeight) {
		this.ftcBomDetailTotalWeight = ftcBomDetailTotalWeight;
	}

	/**
	 * **get**
	 */
	public Long getFtcBomVersionId() {
		return ftcBomVersionId;
	}

	/**
	 * **set**
	 */
	public void setFtcBomVersionId(Long ftcBomVersionId) {
		this.ftcBomVersionId = ftcBomVersionId;
	}

	/**
	 * **get**
	 */
	public String getFtcBomDetailItemNumber() {
		return ftcBomDetailItemNumber;
	}

	/**
	 * **set**
	 */
	public void setFtcBomDetailItemNumber(String ftcBomDetailItemNumber) {
		this.ftcBomDetailItemNumber = ftcBomDetailItemNumber;
	}

	/**
	 * **get**
	 */
	public String getFtcBomDetailReed() {
		return ftcBomDetailReed;
	}

	/**
	 * **set**
	 */
	public void setFtcBomDetailReed(String ftcBomDetailReed) {
		this.ftcBomDetailReed = ftcBomDetailReed;
	}

	/**
	 * **get**
	 */
	public String getFtcBomDetailGuideNeedle() {
		return ftcBomDetailGuideNeedle;
	}

	/**
	 * **set**
	 */
	public void setFtcBomDetailGuideNeedle(String ftcBomDetailGuideNeedle) {
		this.ftcBomDetailGuideNeedle = ftcBomDetailGuideNeedle;
	}

	/**
	 * **get**
	 */
	public String getFtcBomDetailRemark() {
		return ftcBomDetailRemark;
	}

	/**
	 * **set**
	 */
	public void setFtcBomDetailRemark(String ftcBomDetailRemark) {
		this.ftcBomDetailRemark = ftcBomDetailRemark;
	}


}

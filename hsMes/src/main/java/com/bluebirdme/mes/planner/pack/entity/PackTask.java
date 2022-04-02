/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.planner.pack.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 包装任务（订单、计划公用）
 * @author Goofy
 * @Date 2017年12月1日 上午10:41:43
 */
@Entity
@Table(name="hs_pack_task")
public class PackTask extends BaseEntity {
	
	/**
	 * 包装任务ID
	 */
	@Column
	private Long ptId;
	
	/**
	 * 订单明细ID
	 */
	@Column
	private Long sodId;
	
	/**
	 * 计划明细ID
	 */
	@Column
	private Long ppdId;
	
	/**
	 * 非套材包材BOM版本ID
	 */
	@Column
	private Long vid;
	
	/**
	 * 版本号
	 */
	@Column
	private String version;
	
	/**
	 * 包装代码
	 */
	@Column
	private String code;
	
	/**
	 * 总托数
	 */
	@Column
	private Integer totalCount;
	
	/**
	 * 剩余数量
	 */
	@Column
	private Integer leftCount;
	
	/**
	 * 每托卷数
	 */
	@Column
	private Integer rollsPerTray;
	
	/**
	 * 生产总托数
	 */
	private Integer produceTotalCount;
	
	/**
	 * 已打印托数
	 */
	@Column
	private Integer printedTrayBarcodeCount=0;
	
	@Column
	private String memo;

	public Long getVid() {
		return vid;
	}

	public void setVid(Long vid) {
		this.vid = vid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getLeftCount() {
		return leftCount;
	}

	public void setLeftCount(Integer leftCount) {
		this.leftCount = leftCount;
	}

	public Integer getRollsPerTray() {
		return rollsPerTray;
	}

	public void setRollsPerTray(Integer rollsPerTray) {
		this.rollsPerTray = rollsPerTray;
	}

	public Long getSodId() {
		return sodId;
	}

	public void setSodId(Long sodId) {
		this.sodId = sodId;
	}

	public Long getPpdId() {
		return ppdId;
	}

	public void setPpdId(Long ppdId) {
		this.ppdId = ppdId;
	}

	public Integer getPrintedTrayBarcodeCount() {
		return printedTrayBarcodeCount;
	}

	public void setPrintedTrayBarcodeCount(Integer printedTrayBarcodeCount) {
		this.printedTrayBarcodeCount = printedTrayBarcodeCount;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getPtId() {
		return ptId;
	}

	public void setPtId(Long ptId) {
		this.ptId = ptId;
	}

	public Integer getProduceTotalCount() {
		return produceTotalCount;
	}

	public void setProduceTotalCount(Integer produceTotalCount) {
		this.produceTotalCount = produceTotalCount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}

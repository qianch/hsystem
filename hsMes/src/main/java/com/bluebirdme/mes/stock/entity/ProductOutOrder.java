package com.bluebirdme.mes.stock.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
@Desc("成品出库装厢表")
@Entity
@Table(name="HS_Product_Out_Order")
public class ProductOutOrder extends BaseEntity {
	
	@Desc("客户名称")
	@Column(nullable=false)
	@Index(name="consumerName")
	private String consumerName;
	@Desc("出货单uuid")
	@Column(nullable=false)
	@Index(name="packingNumber")
	private String packingNumber;
	@Desc("出货单编号")
	@Column(nullable=false)
	private String deliveryCode;
	@Desc("装车序号")
	@Column(nullable=false)
	private String pn;
	@Desc("物流公司")
	private String logisticsCompany;
	@Desc("提单号")
	@Column
	private String ladingCode;
	@Desc("车牌号")
	@Column
	private String plate;
	@Desc("箱号")
	@Column
	private String boxNumber;
	@Desc("实际箱号")
	@Column
	private String realBoxNumber;
	@Desc("封号")
	@Column
	private String serialNumber;
	@Desc("件数")
	@Column
	private Double count;
	@Desc("出库时间")
	@Column(nullable=false)
	private Date outTime;
	
	@Desc("操作人")
	private Long operateUserId;
	
	@Desc("出库单ID")
	@Column(nullable=false)
	private Long deliveryId;

	@Desc("样布信息")
	@Column(length=2500)
	private String sampleInformation;

	public String getConsumerName() {
		return consumerName;
	}



	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}



	public String getPackingNumber() {
		return packingNumber;
	}



	public void setPackingNumber(String packingNumber) {
		this.packingNumber = packingNumber;
	}



	public String getDeliveryCode() {
		return deliveryCode;
	}



	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}



	public String getPn() {
		return pn;
	}



	public void setPn(String pn) {
		this.pn = pn;
	}






	public String getLogisticsCompany() {
		return logisticsCompany;
	}



	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}



	public Date getOutTime() {
		return outTime;
	}



	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}



	public Long getOperateUserId() {
		return operateUserId;
	}



	public void setOperateUserId(Long operateUserId) {
		this.operateUserId = operateUserId;
	}



	public String getLadingCode() {
		return ladingCode;
	}



	public void setLadingCode(String ladingCode) {
		this.ladingCode = ladingCode;
	}



	public String getPlate() {
		return plate;
	}



	public void setPlate(String plate) {
		this.plate = plate;
	}



	public String getBoxNumber() {
		return boxNumber;
	}



	public void setBoxNumber(String boxNumber) {
		this.boxNumber = boxNumber;
	}



	public String getRealBoxNumber() {
		return realBoxNumber;
	}



	public void setRealBoxNumber(String realBoxNumber) {
		this.realBoxNumber = realBoxNumber;
	}



	public String getSerialNumber() {
		return serialNumber;
	}



	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}



	public Double getCount() {
		return count;
	}



	public void setCount(Double count) {
		this.count = count;
	}



	public Long getDeliveryId() {
		return deliveryId;
	}



	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getSampleInformation() {
		return sampleInformation;
	}



	public void setSampleInformation(String sampleInformation) {
		this.sampleInformation = sampleInformation;
	}

}

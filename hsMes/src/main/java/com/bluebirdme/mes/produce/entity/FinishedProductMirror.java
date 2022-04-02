/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 成品信息
 * @author 宋黎明
 * @Date 2016年09月30日 上午10:02:34
 */
@Desc("成品信息")
@Entity
@Table(name="hs_finishproduct_mirror")
@DynamicInsert
public class FinishedProductMirror extends BaseEntity {

	@Desc("成品信息ID")
	@Column(nullable=false)
	private Long productId;

	@Desc("成品客户信息ID")
	@Column(nullable=false)
	private Long productConsumerId;

	@Desc("物料编码")
	@Column(nullable=false)
	private String materielCode;

	@Desc("客户产品名称")
	@Column(nullable=false)
	@Index(name="consumerProductName")
	private String consumerProductName;
	@Desc("成品载具编码")
	@Column
	private String  carrierCode;
	@Desc("厂内名称")
	@Index(name="factoryProductName")
	private String factoryProductName;

	@Desc("门幅")
	@Index(name="productWidth")
	private Double productWidth;

	@Desc("卷长")
	@Column
	private Double productRollLength;

	@Desc("卷重")
	@Column
	private Double productRollWeight;

	@Desc("工艺标准名称")
	@Column
	private String productProcessName;

	@Desc("工艺标准代码")
	@Column
	private String productProcessCode;

	@Desc("工艺标准版本")
	@Column
	private String productProcessBomVersion;

	@Desc("工艺标准版本")
	@Column
	private String customerMaterialCodeOfFP;

	@Desc("客户版本号")
	@Column
	private String productConsumerBomVersion;

	@Desc("包装标准代码")
	@Column
	private String productPackagingCode;

	@Desc("包装标准版本")
	@Column
	private String productPackageVersion;

	@Desc("卷标签代码")
	@Column
	private String productRollCode;

	@Desc("箱唛头代码")
	@Column
	private String productBoxCode;

	@Desc("托唛头代码")
	@Column
	private String productTrayCode;

	@Desc("备注")
	@Column(length=16777215)
	private String productMemo;

	@Desc("状态 1:正式 0:审核中 null:审核中")
	@Column
	private Integer productState;

	@Desc("称重规则 0:全称 1:抽称 2:不称")
	@Column
	private Integer productWeigh;

	@Desc("产品型号")
	@Column(nullable=false)
	@Index(name="productModel")
	private String productModel;

	//1:是套材  2:非套材
	@Desc("是否套材")
	@Column(nullable=false)
	private Integer productIsTc;

	@Desc("保质期")
	@Column(nullable=true)
	private Integer productShelfLife;
	@Desc("包装需求")
	@Column(length = 16777215)
	private String packReq;
	@Desc("工艺需求")
	@Column(length = 16777215)
	private String procReq;
	@Desc("包装版本ID")
	private Long packBomId;
	@Desc("工艺版本ID")
	private Long procBomId;
	@Desc("是否由工艺bom添加")
	private Integer isShow;
	@Desc("废弃：1,正常：null")
	@Column
	private Integer obsolete;

	@Desc("常规或试样")
	@Column
	private Integer isCommon;
	@Desc("审核状态")
	@Column
	private Integer auditState;
	@Desc("是否显示变更")
	@Column
	private Integer auditChange=1;

	@Desc("成品类别")
	@Column(nullable=false)
	private Long fpcid;

	@Desc("最大重量")
	@Column
	private Double maxWeight;

	@Desc("最小重量")
	@Column
	private Double minWeight;



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

	@Desc("镜像时间")
	@Column
	private Date gmtCreate;

	@Desc("订单Id")
	@Column
	private Long salesOrderId;

	@Desc("订单明细Id")
	@Column
	private Long salesOrderDetailId;

	@Desc("部件镜像Id")
	@Column
	private Long mirrorPartId;

	public Long getMirrorPartId() {
		return mirrorPartId;
	}

	public void setMirrorPartId(Long mirrorPartId) {
		this.mirrorPartId = mirrorPartId;
	}

	public String getCustomerMaterialCodeOfFP() {
		return customerMaterialCodeOfFP;
	}

	public void setCustomerMaterialCodeOfFP(String customerMaterialCodeOfFP) {
		this.customerMaterialCodeOfFP = customerMaterialCodeOfFP;
	}

	public Long getSalesOrderDetailId() {
		return salesOrderDetailId;
	}

	public void setSalesOrderDetailId(Long salesOrderDetailId) {
		this.salesOrderDetailId = salesOrderDetailId;
	}

	public Long getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(Long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	/**
	 * 成品类别ID
	 * @return
	 */
	public Long getFpcid() {
		return fpcid;
	}

	/**
	 * 成品类别ID
	 * @param fpcid
	 */
	public void setFpcid(Long fpcid) {
		this.fpcid = fpcid;
	}

	public Integer getProductWeigh() {
		return productWeigh;
	}

	public void setProductWeigh(Integer productWeigh) {
		this.productWeigh = productWeigh;
	}

	public Integer getAuditChange() {
		return auditChange;
	}

	public void setAuditChange(Integer auditChange) {
		this.auditChange = auditChange;
	}

	public String getProductProcessName() {
		return productProcessName;
	}

	public void setProductProcessName(String productProcessName) {
		this.productProcessName = productProcessName;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public Integer getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(Integer isCommon) {
		this.isCommon = isCommon;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Integer getObsolete() {
		return obsolete;
	}

	public void setObsolete(Integer obsolete) {
		this.obsolete = obsolete;
	}

	public Long getPackBomId() {
		return packBomId;
	}

	public void setPackBomId(Long packBomId) {
		this.packBomId = packBomId;
	}

	public Long getProcBomId() {
		return procBomId;
	}

	public void setProcBomId(Long procBomId) {
		this.procBomId = procBomId;
	}

	public String getMaterielCode() {
		return materielCode;
	}

	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}

	public String getPackReq() {
		return packReq;
	}

	public void setPackReq(String packReq) {
		this.packReq = packReq;
	}

	public String getProcReq() {
		return procReq;
	}

	public void setProcReq(String procReq) {
		this.procReq = procReq;
	}

	/**
	 * **get**
	 */
	public Integer getProductIsTc() {
		return productIsTc;
	}

	/**
	 * **set**
	 */
	public void setProductIsTc(Integer productIsTc) {
		this.productIsTc = productIsTc;
	}

	public Long getProductConsumerId() {
		return productConsumerId;
	}

	public void setProductConsumerId(Long productConsumerId) {
		this.productConsumerId = productConsumerId;
	}

	public String getConsumerProductName() {
		return consumerProductName;
	}

	public void setConsumerProductName(String consumerProductName) {
		this.consumerProductName = consumerProductName;
	}

	public String getFactoryProductName() {
		return factoryProductName;
	}

	public void setFactoryProductName(String factoryProductName) {
		this.factoryProductName = factoryProductName;
	}

	public Double getProductWidth() {
		return productWidth;
	}

	public void setProductWidth(Double productWidth) {
		this.productWidth = productWidth;
	}

	public Double getProductRollLength() {
		return productRollLength;
	}

	public void setProductRollLength(Double productRollLength) {
		this.productRollLength = productRollLength;
	}

	public Double getProductRollWeight() {
		return productRollWeight;
	}

	public void setProductRollWeight(Double productRollWeight) {
		this.productRollWeight = productRollWeight;
	}

	public String getProductProcessCode() {
		return productProcessCode;
	}

	public void setProductProcessCode(String productProcessCode) {
		this.productProcessCode = productProcessCode;
	}

	public String getProductProcessBomVersion() {
		return productProcessBomVersion;
	}

	public void setProductProcessBomVersion(String productProcessBomVersion) {
		this.productProcessBomVersion = productProcessBomVersion;
	}

	public String getProductConsumerBomVersion() {
		return productConsumerBomVersion;
	}

	public void setProductConsumerBomVersion(String productConsumerBomVersion) {
		this.productConsumerBomVersion = productConsumerBomVersion;
	}


	public String getProductPackagingCode() {
		return productPackagingCode;
	}

	public void setProductPackagingCode(String productPackagingCode) {
		this.productPackagingCode = productPackagingCode;
	}

	public String getProductPackageVersion() {
		return productPackageVersion;
	}

	public void setProductPackageVersion(String productPackageVersion) {
		this.productPackageVersion = productPackageVersion;
	}

	public String getProductRollCode() {
		return productRollCode;
	}

	public void setProductRollCode(String productRollCode) {
		this.productRollCode = productRollCode;
	}

	public String getProductBoxCode() {
		return productBoxCode;
	}

	public void setProductBoxCode(String productBoxCode) {
		this.productBoxCode = productBoxCode;
	}

	public String getProductTrayCode() {
		return productTrayCode;
	}

	public void setProductTrayCode(String productTrayCode) {
		this.productTrayCode = productTrayCode;
	}

	public String getProductMemo() {
		return productMemo;
	}

	public void setProductMemo(String productMemo) {
		this.productMemo = productMemo;
	}

	public Integer getProductState() {
		return productState;
	}

	public void setProductState(Integer productState) {
		this.productState = productState;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	/**
	 * **get**
	 */
	public Integer getProductShelfLife() {
		return productShelfLife;
	}

	/**
	 * **set**
	 */
	public void setProductShelfLife(Integer productShelfLife) {
		this.productShelfLife = productShelfLife;
	}

	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Double getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(Double minWeight) {
		this.minWeight = minWeight;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}


	@Desc("卷毛重")
	@Column
	private Double rollGrossWeight;

	public void setRollGrossWeight(Double rollGrossWeight)
	{
		this.rollGrossWeight=rollGrossWeight;
	}

	public Double getRollGrossWeight()
	{
		return  rollGrossWeight;
	}

	@Desc("预留长度")
	@Column
	private Double reserveLength;
	public Double getReserveLength() {
		return reserveLength;
	}

	public void setReserveLength(Double reserveLength) {
		this.reserveLength = reserveLength;
	}

}

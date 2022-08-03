package com.bluebirdme.mes.sales.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntityExt;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * 销售明细
 *
 * @author Goofy
 * @Date 2016年10月13日 下午2:54:49
 */
@Desc("销售明细")
@Entity
@Table(name = "HS_Sales_Order_Detail")
public class SalesOrderDetail extends BaseEntityExt {
    @Desc("产品ID")
    @Column(nullable = false)
    @Index(name = "productId")
    private Long productId;

    @Desc("销售订单ID")
    @Column(nullable = true)
    @Index(name = "salesOrderId")
    private Long salesOrderId;

    @Desc("产品批次号")
    @Column(nullable = true)
    private String productBatchCode;

    @Desc("订单号")
    @Column(nullable = true)
    @Index(name = "salesOrderSubCode")
    private String salesOrderSubCode;

    @Desc("订单号打印")
    @Column(nullable = true)
    private String salesOrderSubCodePrint;

    @Desc("客户产品名称")
    @Column(name = "consumerProductName", nullable = false)
    @Index(name = "consumerProductName")
    private String consumerProductName;

    @Desc("厂内名称")
    @Column(name = "factoryProductName", nullable = true)
    @Index(name = "factoryProductName")
    private String factoryProductName;

    @Desc("门幅")
    @Column(name = "productWidth", nullable = true)
    private Double productWidth;

    @Desc("卷长")
    @Column(name = "productRollLength")
    private Double productRollLength;

    @Desc("卷重")
    @Column(name = "productRollWeight")
    private Double productRollWeight;

    @Desc("工艺标准代码")
    @Column(name = "productProcessCode")
    private String productProcessCode;

    @Desc("工艺标准版本")
    @Column(name = "productProcessBomVersion")
    private String productProcessBomVersion;

    @Desc("客户版本号")
    @Column(name = "productConsumerBomVersion")
    private String productConsumerBomVersion;

    @Desc("包装标准代码")
    @Column(name = "productPackagingCode")
    private String productPackagingCode;

    @Desc("包装标准版本")
    @Column(name = "productPackageVersion")
    private String productPackageVersion;

    @Desc("卷标签代码")
    @Column(name = "productRollCode")
    private String productRollCode;

    @Desc("箱唛头代码")
    @Column(name = "productBoxCode")
    private String productBoxCode;

    @Desc("托唛头代码")
    @Column(name = "productTrayCode")
    private String productTrayCode;

    @Desc("产品型号")
    @Column(name = "productModel", nullable = false)
    @Index(name = "productModel")
    private String productModel;

    @Desc("叶型")
    @Column(name = "bladeProfile")
    private String bladeProfile;

    @Desc("备注")
    @Column(nullable = true, length = 16777215)
    private String productMemo;

    @Desc("产品数量")
    @Column(nullable = false)
    private Double productCount;

    @Desc("总米长")
    @Column
    private Double totalMetres;

    @Desc("总重量")
    @Column
    private Double totalWeight;

    @Desc("已分配数量")
    @Column(nullable = true)
    private Double assignedCount;

    @Desc("发货时间")
    @Column(nullable = true)
    private Date deliveryTime;

    @Desc("是否套材")
    // 1:是套材 2:非套材
    @Column(nullable = false)
    private Integer productIsTc;

    @Desc("生产数量（卷/套）")
    @Column(nullable = true)
    private Double produceCount;

    @Desc("已生产卷数")
    @Column
    private Double producedRolls;

    @Desc("已生产托数")
    @Column
    private Double producedTrays;

    @Desc("打包数量")
    @Column(nullable = true)
    private Double packagingCount;

    @Desc("分配数量")
    @Column(nullable = true)
    private Double allocateCount;
    @Desc("工艺版本ID")
    @Column
    private Long procBomId;
    @Desc("包装版本ID")
    @Column
    private Long packBomId;

    /**
     * 0，null:未关闭 1：关闭
     */
    @Desc("是否已关闭")
    @Column
    private Integer closed;

    @Desc("编辑次数")
    @Column
    private Integer editTimes;

    /**
     * 0，null:未完成，1：完成
     */
    @Desc("订单已完成")
    @Column
    @Index(name = "isComplete")
    private Integer isComplete;
    /**
     * 0，null:未分配完成，1：分配完成
     */
    @Desc("分配订单已完成")
    @Column
    @Index(name = "isPlaned")
    private Integer isPlaned = 0;

    public Integer getIsPlaned() {
        return isPlaned;
    }

    public void setIsPlaned(Integer isPlaned) {
        this.isPlaned = isPlaned;
    }

    @Desc("订单部件明细")
    @Transient
    private List<SalesOrderDetailPartsCount> partsCountList;

    @Desc("镜像工艺版本ID")
    @Column
    private Long mirrorProcBomVersionId;

    @Desc("镜像成品信息ID")
    @Column
    private Long mirrorProductId;

    public Long getMirrorPartId() {
        return mirrorPartId;
    }

    public void setMirrorPartId(Long mirrorPartId) {
        this.mirrorPartId = mirrorPartId;
    }

    @Desc("镜像部件ID")
    @Column
    private Long mirrorPartId;


    public Long getMirrorProductId() {
        return mirrorProductId;
    }

    public void setMirrorProductId(Long mirrorProductId) {
        this.mirrorProductId = mirrorProductId;
    }

    public Long getMirrorProcBomVersionId() {
        return mirrorProcBomVersionId;
    }

    public void setMirrorProcBomVersionId(Long mirrorProcBomVersionId) {
        this.mirrorProcBomVersionId = mirrorProcBomVersionId;
    }

    public String getSalesOrderSubCodePrint() {
        return salesOrderSubCodePrint;
    }

    public void setSalesOrderSubCodePrint(String salesOrderSubCodePrint) {
        this.salesOrderSubCodePrint = salesOrderSubCodePrint;
    }

    public String getBladeProfile() {
        return bladeProfile;
    }

    public void setBladeProfile(String bladeProfile) {
        this.bladeProfile = bladeProfile;
    }

    public Integer getEditTimes() {
        return editTimes;
    }

    public void setEditTimes(Integer editTimes) {
        this.editTimes = editTimes;
    }

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
    }

    public Long getProcBomId() {
        return procBomId;
    }

    public void setProcBomId(Long procBomId) {
        this.procBomId = procBomId;
    }

    public Long getPackBomId() {
        return packBomId;
    }

    public void setPackBomId(Long packBomId) {
        this.packBomId = packBomId;
    }

    public Double getAssignedCount() {
        return assignedCount;
    }

    public void setAssignedCount(Double assignedCount) {
        this.assignedCount = assignedCount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(Long salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public String getProductBatchCode() {
        return productBatchCode;
    }

    public void setProductBatchCode(String productBatchCode) {
        this.productBatchCode = productBatchCode;
    }

    public String getSalesOrderSubCode() {
        return salesOrderSubCode;
    }

    public void setSalesOrderSubCode(String salesOrderSubCode) {
        this.salesOrderSubCode = salesOrderSubCode;
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

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductMemo() {
        return productMemo;
    }

    public void setProductMemo(String productMemo) {
        this.productMemo = productMemo;
    }

    public Double getProductCount() {
        return productCount;
    }

    public void setProductCount(Double productCount) {
        this.productCount = productCount;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getProductIsTc() {
        return productIsTc;
    }

    public void setProductIsTc(Integer productIsTc) {
        this.productIsTc = productIsTc;
    }

    public Double getProduceCount() {
        return produceCount;
    }

    public void setProduceCount(Double produceCount) {
        this.produceCount = produceCount;
    }

    public Double getPackagingCount() {
        return packagingCount;
    }

    public void setPackagingCount(Double packagingCount) {
        this.packagingCount = packagingCount;
    }

    public Double getProducedRolls() {
        return producedRolls;
    }

    public void setProducedRolls(Double producedRolls) {
        this.producedRolls = producedRolls;
    }

    public Double getProducedTrays() {
        return producedTrays;
    }

    public void setProducedTrays(Double producedTrays) {
        this.producedTrays = producedTrays;
    }

    public Double getAllocateCount() {
        return allocateCount;
    }

    public void setAllocateCount(Double allocateCount) {
        this.allocateCount = allocateCount;
    }

    public Integer getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Integer isComplete) {
        this.isComplete = isComplete;
    }

    public List<SalesOrderDetailPartsCount> getPartsCountList() {
        return partsCountList;
    }

    public void setPartsCountList(List<SalesOrderDetailPartsCount> partsCountList) {
        this.partsCountList = partsCountList;
    }

    public Double getTotalMetres() {
        return totalMetres;
    }

    public void setTotalMetres(Double totalMetres) {
        this.totalMetres = totalMetres;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public static void main(String[] args) {
        Field fs[] = SalesOrderDetail.class.getDeclaredFields();
        for (Field f : fs) {
            System.out.println("sod." + f.getName() + ",");
        }
    }

}

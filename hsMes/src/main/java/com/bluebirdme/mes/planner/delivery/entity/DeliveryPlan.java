package com.bluebirdme.mes.planner.delivery.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Desc("出货计划")
@Entity
@Table(name = "HS_Delivery_Plan")
@DynamicInsert
public class DeliveryPlan extends BaseEntity {
    @Desc("出货时间")
    @Column
    private Date deliveryDate;
    @Desc("出货人")
    @Column
    private Long deliveryBizUserId;
    @Desc("包装方式")
    @Column
    private String packagingType;
    @Desc("条码")
    @Column
    private String barcode;
    @Desc("物流公司")
    @Column
    private String logisticsCompany;
    @Desc("装箱图")
    @Column
    private String boxPic;
    @Desc("注意事项")
    @Column(length = 2500)
    private String attention;
    @Desc("样布信息")
    @Column(length = 2500)
    private String sampleInformation;

    @Desc("审核状态")
    @Column(nullable = false, columnDefinition = "int default 0")
    @Index(name = "auditState")
    private Integer auditState = 0;
    @Desc("出货单编号")
    @Column(nullable = false)
    @Index(name = "deliveryCode")
    private String deliveryCode;
    @Desc("要货公司")
    @Column
    @Index(name = "deliveryTargetCompany")
    private String deliveryTargetCompany;
    @Desc("要货公司id")
    @Column
    @Index(name = "consumerId")
    private Long consumerId;
    /**
     * -1未完成   1已完成
     */
    @Desc("是否已完成")
    @Column
    private Integer isComplete = -1;
    @Desc("是否已关闭")
    @Column
    private Integer isClosed;

    @Desc("客户基地")
    @Column
    private String basePlace;

    @Desc("客户备注信息")
    @Column(length = 2500)
    private String customerNotes;

    @Desc("码单订单号")
    @Column
    private String codeOrderNo;

    @Desc("联系人及电话")
    @Column
    private String linkmanAndPhone;

    @Desc("收货地址")
    @Column
    private String shippingAddress;

    @Desc("客户批号")
    @Column
    private String customerBatchCode;

    @Desc("Po No.")
    @Column
    private String poNo;

    @Desc("关税编号")
    @Column
    private String tariffCode;

    @Desc("海关代码")
    @Column
    private String customsCode;

    @Desc("装运唛头")
    @Column
    private String shippingMark;

    @Transient
    private List<DeliveryPlanDetails> productDatas;
    @Transient
    private List<DeliveryPlanSalesOrders> orderDatas;

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public Integer getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Integer isClosed) {
        this.isClosed = isClosed;
    }

    /**
     * **get**
     */
    public Integer getIsComplete() {
        return isComplete;
    }

    /**
     * **set**
     */
    public void setIsComplete(Integer isComplete) {
        this.isComplete = isComplete;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getDeliveryTargetCompany() {
        return deliveryTargetCompany;
    }

    public void setDeliveryTargetCompany(String deliveryTargetCompany) {
        this.deliveryTargetCompany = deliveryTargetCompany;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Long getDeliveryBizUserId() {
        return deliveryBizUserId;
    }

    public void setDeliveryBizUserId(Long deliveryBizUserId) {
        this.deliveryBizUserId = deliveryBizUserId;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public void setPackagingType(String packagingType) {
        this.packagingType = packagingType;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getBoxPic() {
        return boxPic;
    }

    public void setBoxPic(String boxPic) {
        this.boxPic = boxPic;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getSampleInformation() {
        return sampleInformation;
    }

    public void setSampleInformation(String sampleInformation) {
        this.sampleInformation = sampleInformation;
    }

    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }

    public String getBasePlace() {
        return basePlace;
    }

    public void setBasePlace(String basePlace) {
        this.basePlace = basePlace;
    }

    public String getCustomerNotes() {
        return customerNotes;
    }

    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }

    public List<DeliveryPlanDetails> getProductDatas() {
        return productDatas;
    }

    public void setProductDatas(List<DeliveryPlanDetails> productDatas) {
        this.productDatas = productDatas;
    }

    public List<DeliveryPlanSalesOrders> getOrderDatas() {
        return orderDatas;
    }

    public void setOrderDatas(List<DeliveryPlanSalesOrders> orderDatas) {
        this.orderDatas = orderDatas;
    }

    public String getCodeOrderNo() {
        return codeOrderNo;
    }

    public void setCodeOrderNo(String codeOrderNo) {
        this.codeOrderNo = codeOrderNo;
    }

    public String getLinkmanAndPhone() {
        return linkmanAndPhone;
    }

    public void setLinkmanAndPhone(String linkmanAndPhone) {
        this.linkmanAndPhone = linkmanAndPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCustomerBatchCode() {
        return customerBatchCode;
    }

    public void setCustomerBatchCode(String customerBatchCode) {
        this.customerBatchCode = customerBatchCode;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getTariffCode() {
        return tariffCode;
    }

    public void setTariffCode(String tariffCode) {
        this.tariffCode = tariffCode;
    }

    public String getCustomsCode() {
        return customsCode;
    }

    public void setCustomsCode(String customsCode) {
        this.customsCode = customsCode;
    }

    public String getShippingMark() {
        return shippingMark;
    }

    public void setShippingMark(String shippingMark) {
        this.shippingMark = shippingMark;
    }
}

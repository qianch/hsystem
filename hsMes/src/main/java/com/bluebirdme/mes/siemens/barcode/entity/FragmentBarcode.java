package com.bluebirdme.mes.siemens.barcode.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.siemens.order.entity.BaseBarcode;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 类注释
 *
 * @author Goofy
 * @Date 2017年8月2日 下午4:08:31
 */
@Desc("裁片条码")
@Entity
@Table(name = "hs_siemens_fragment_barcode")
public class FragmentBarcode extends BaseBarcode {
    @Desc("裁剪派工单ID")
    @Column(nullable = false)
    private Long ctoId;

    @Desc("裁剪任务单ID")
    @Column(nullable = false)
    private Long ctId;

    @Desc("图纸ID")
    @Column(nullable = false)
    private Long dwId;

    @Desc("任务单号")
    @Column(nullable = false)
    @Index(name = "ctCode")
    private String ctCode;
    @Desc("派工单号")
    @Column(nullable = false)
    @Index(name = "ctCode")
    private String ctoCode;
    @Desc("订单号")
    @Column(nullable = false)
    @Index(name = "orderCode")
    private String orderCode;
    @Desc("批次号")
    @Column(nullable = false)
    @Index(name = "batchCode")
    private String batchCode;
    @Desc("客户名称")
    @Column(nullable = false)
    @Index(name = "consumerName")
    private String consumerName;
    @Desc("客户大类")
    @Column(nullable = false)
    private String consumerCategory;
    @Desc("部件名称")
    @Column(nullable = false)
    private String partName;
    @Desc("裁片名称")
    @Column(nullable = false)
    private String fragmentName;
    @Desc("组别")
    @Column(nullable = false)
    private String groupName;
    @Desc("机长")
    @Column(nullable = false)
    private String groupLeader;
    @Desc("机台")
    @Column(nullable = true)
    private String deviceCode;
    @Desc("车间")
    @Column(nullable = true)
    private String workshop;
    @Desc("长度（M）")
    @Column(nullable = false)
    private String fragmentLength;
    @Desc("宽度（M）")
    @Column(nullable = false)
    private String fragmentWidth;
    @Desc("重量")
    @Column(nullable = false)
    private String fragmentWeight;

    @Desc("小部件编码")
    @Column(nullable = false)
    private String fragmentCode;
    /**
     * 0：为组套，1：已组套
     */
    @Desc("组套状态")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer isPacked;
    @Desc("组套时间")
    @Column(nullable = true)
    private String packedTime;
    @Desc("组套操作人")
    @Column(nullable = true)
    private String packUserName;
    @Desc("胚布规格")
    @Column(nullable = true)
    private String farbicModel;

    @Desc("图号")
    @Column(nullable = false)
    private String fragmentDrawingNo;

    @Desc("图纸版本")
    @Column(nullable = false)
    private String fragmentDrawingVer;

    @Desc("备注")
    @Column(nullable = true, columnDefinition = "text")
    private String fragmentMemo;

    @Desc("出图顺序")
    @Column(nullable = false)
    private Integer printSort;

    @Desc("补打时间")
    @Column(nullable = true)
    private String extraPrintTime;
    @Desc("补打人")
    @Column(nullable = true)
    private String extraPrintUser;

    @Desc("补打原因")
    @Column(nullable = true)
    private String extraPrintReason;

    @Desc("重打原因")
    @Column(nullable = true)
    private String rePrintReason;

    @Desc("车间代码")
    @Column(nullable = true)
    private String workShopCode;

    public String getWorkShopCode() {
        return workShopCode;
    }

    public void setWorkShopCode(String workShopCode) {
        this.workShopCode = workShopCode;
    }

    public String getCtCode() {
        return ctCode;
    }

    public void setCtCode(String ctCode) {
        this.ctCode = ctCode;
    }

    public String getCtoCode() {
        return ctoCode;
    }

    public void setCtoCode(String ctoCode) {
        this.ctoCode = ctoCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerCategory() {
        return consumerCategory;
    }

    public void setConsumerCategory(String consumerCategory) {
        this.consumerCategory = consumerCategory;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(String groupLeader) {
        this.groupLeader = groupLeader;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public String getFragmentLength() {
        return fragmentLength;
    }

    public void setFragmentLength(String fragmentLength) {
        this.fragmentLength = fragmentLength;
    }

    public String getFragmentWidth() {
        return fragmentWidth;
    }

    public void setFragmentWidth(String fragmentWidth) {
        this.fragmentWidth = fragmentWidth;
    }

    public String getFragmentWeight() {
        return fragmentWeight;
    }

    public void setFragmentWeight(String fragmentWeight) {
        this.fragmentWeight = fragmentWeight;
    }

    public Integer getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(Integer isPacked) {
        this.isPacked = isPacked;
    }

    public String getPackedTime() {
        return packedTime;
    }

    public void setPackedTime(String packedTime) {
        this.packedTime = packedTime;
    }

    public String getPackUserName() {
        return packUserName;
    }

    public void setPackUserName(String packUserName) {
        this.packUserName = packUserName;
    }

    public String getFarbicModel() {
        return farbicModel;
    }

    public void setFarbicModel(String farbicModel) {
        this.farbicModel = farbicModel;
    }

    public String getFragmentDrawingNo() {
        return fragmentDrawingNo;
    }

    public void setFragmentDrawingNo(String fragmentDrawingNo) {
        this.fragmentDrawingNo = fragmentDrawingNo;
    }

    public String getFragmentMemo() {
        return fragmentMemo;
    }

    public void setFragmentMemo(String fragmentMemo) {
        this.fragmentMemo = fragmentMemo;
    }

    public Integer getPrintSort() {
        return printSort;
    }

    public void setPrintSort(Integer printSort) {
        this.printSort = printSort;
    }

    public String getFragmentDrawingVer() {
        return fragmentDrawingVer;
    }

    public void setFragmentDrawingVer(String fragmentDrawingVer) {
        this.fragmentDrawingVer = fragmentDrawingVer;
    }

    public String getExtraPrintReason() {
        return extraPrintReason;
    }

    public void setExtraPrintReason(String extraPrintReason) {
        this.extraPrintReason = extraPrintReason;
    }

    public Long getCtoId() {
        return ctoId;
    }

    public void setCtoId(Long ctoId) {
        this.ctoId = ctoId;
    }

    public Long getCtId() {
        return ctId;
    }

    public void setCtId(Long ctId) {
        this.ctId = ctId;
    }

    public Long getDwId() {
        return dwId;
    }

    public void setDwId(Long dwId) {
        this.dwId = dwId;
    }

    public String getExtraPrintTime() {
        return extraPrintTime;
    }

    public void setExtraPrintTime(String extraPrintTime) {
        this.extraPrintTime = extraPrintTime;
    }

    public String getExtraPrintUser() {
        return extraPrintUser;
    }

    public void setExtraPrintUser(String extraPrintUser) {
        this.extraPrintUser = extraPrintUser;
    }

    public String getRePrintReason() {
        return rePrintReason;
    }

    public void setRePrintReason(String rePrintReason) {
        this.rePrintReason = rePrintReason;
    }

    public String getFragmentCode() {
        return fragmentCode;
    }

    public void setFragmentCode(String fragmentCode) {
        this.fragmentCode = fragmentCode;
    }
}

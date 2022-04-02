package com.bluebirdme.mes.store.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;


/**
 * 卷
 *
 * @author 宋黎明
 * @Date 2016年11月08日 上午10:02:34
 */
@Desc("卷")
@Entity
@Table(name = "hs_roll")
@DynamicInsert
public class Roll extends BaseEntity {
    @Desc("重量")
    private Double rollWeight;

    @Desc("自动称重")
    private Double rollAutoWeight;

    /**
     * 1:应该称重
     * 0:无需称重
     */
    @Desc("是否应该称重")
    @Column(columnDefinition = "int default 0")
    private Integer rollWeighState = 0;

    /**
     * 1:异常
     * 0:正常
     */
    @Desc("是异常品")
    @Column(columnDefinition = "int default 0")
    private Integer isAbnormalRoll = 0;

    @Desc("实际米长")
    @Column(columnDefinition = "double default 0")
    private Double rollRealLength;

    @Desc("机台号")
    private String rollDeviceCode;

    @Desc("时间")
    private Date rollOutputTime;

    @Desc("质量等级编码")
    private String rollQualityGradeCode;

    @Desc("人员ID")
    private Long rollUserId;

    @Desc("卷条码号")
    @Index(name = "rollBarcode")
    private String rollBarcode;

    @Desc("部件条码")
    @Index(name = "partBarcode")
    private String partBarcode;

    @Desc("状态")
    @Column(nullable = false)
    private Integer state;

    @Desc("备注")
    @Column(nullable = true, length = 16777215)
    private String memo;

    @Desc("入库时间")
    @Column
    private Date inTime;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Double getRollRealLength() {
        return rollRealLength;
    }

    public void setRollRealLength(Double rollRealLength) {
        this.rollRealLength = rollRealLength;
    }

    /**
     * **get**
     */
    public Double getRollWeight() {
        return rollWeight;
    }

    /**
     * **set**
     */
    public void setRollWeight(Double rollWeight) {
        this.rollWeight = rollWeight;
    }

    /**
     * **get**
     */
    public String getRollDeviceCode() {
        return rollDeviceCode;
    }

    /**
     * **set**
     */
    public void setRollDeviceCode(String rollDeviceCode) {
        this.rollDeviceCode = rollDeviceCode;
    }

    /**
     * **get**
     */
    public Date getRollOutputTime() {
        return rollOutputTime;
    }

    /**
     * **set**
     */
    public void setRollOutputTime(Date rollOutputTime) {
        this.rollOutputTime = rollOutputTime;
    }

    /**
     * **get**
     */
    public String getRollQualityGradeCode() {
        return rollQualityGradeCode;
    }

    /**
     * **set**
     */
    public void setRollQualityGradeCode(String rollQualityGradeCode) {
        this.rollQualityGradeCode = rollQualityGradeCode;
    }

    public Long getRollUserId() {
        return rollUserId;
    }

    public void setRollUserId(Long rollUserId) {
        this.rollUserId = rollUserId;
    }

    /**
     * **get**
     */
    public String getRollBarcode() {
        return rollBarcode;
    }

    /**
     * **set**
     */
    public void setRollBarcode(String rollBarcode) {
        this.rollBarcode = rollBarcode;
    }

    /**
     * **get**
     */
    public String getPartBarcode() {
        return partBarcode;
    }

    /**
     * **set**
     */
    public void setPartBarcode(String partBarcode) {
        this.partBarcode = partBarcode;
    }

    public Double getRollAutoWeight() {
        return rollAutoWeight;
    }

    public void setRollAutoWeight(Double rollAutoWeight) {
        this.rollAutoWeight = rollAutoWeight;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRollWeighState() {
        return rollWeighState == null ? 0 : rollWeighState;
    }

    public void setRollWeighState(Integer rollWeighState) {
        if (rollWeighState == null)
            isAbnormalRoll = 0;
        this.rollWeighState = rollWeighState;
    }

    /**
     * **get**
     */
    public Integer getIsAbnormalRoll() {
        return isAbnormalRoll == null ? 0 : isAbnormalRoll;
    }

    /**
     * **set**
     */
    public void setIsAbnormalRoll(Integer isAbnormalRoll) {
        if (isAbnormalRoll == null)
            isAbnormalRoll = 0;
        this.isAbnormalRoll = isAbnormalRoll;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }
}

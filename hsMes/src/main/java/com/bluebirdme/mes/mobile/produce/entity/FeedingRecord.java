package com.bluebirdme.mes.mobile.produce.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

@Desc("投料记录")
@Entity
@Table(name = "HS_Feeding_Record")
public class FeedingRecord extends BaseEntity {
    @Desc("机台ID")
    @Column
    private String deviceCode;
    @Desc("操作人ID")
    @Column(nullable = false)
    private String operateUserId;
    @Desc("原料条码")
    @Column
    private String materialCode;
    @Desc("卷条码")
    @Column
    @Index(name = "rollCode")
    private String rollCode;
    @Desc("编织计划ID")
    @Column
    @Index(name = "weaveId")
    private Long weaveId;
    @Desc("裁减计划ID")
    @Column
    @Index(name = "cutId")
    private Long cutId;
    @Desc("投料日期")
    @Column(nullable = false)
    private Date feedingDate;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(String operateUserId) {
        this.operateUserId = operateUserId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getRollCode() {
        return rollCode;
    }

    public void setRollCode(String rollCode) {
        this.rollCode = rollCode;
    }

    public Long getWeaveId() {
        return weaveId;
    }

    public void setWeaveId(Long weaveId) {
        this.weaveId = weaveId;
    }

    public Long getCutId() {
        return cutId;
    }

    public void setCutId(Long cutId) {
        this.cutId = cutId;
    }

    public Date getFeedingDate() {
        return feedingDate;
    }

    public void setFeedingDate(Date feedingDate) {
        this.feedingDate = feedingDate;
    }
}

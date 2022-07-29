package com.bluebirdme.mes.planner.weave.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Desc("计划和机台信息")
@Entity
@Table(name = "hs_Weave_Plan_Devices")
@DynamicInsert
public class WeavePlanDevices extends BaseEntity {
    @Desc("机台ID")
    @Column(nullable = false)
    @Index(name = "deviceId")
    private Long deviceId;

    @Desc("编织计划ID")
    @Column(nullable = false)
    @Index(name = "weavePlanId")
    private Long weavePlanId;

    @Desc("正在生产")
    @Column(columnDefinition = "int(2) default 0")
    private Integer isProducing;
    
    @Desc("优先")
    @Column
    private Long prior;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getWeavePlanId() {
        return weavePlanId;
    }

    public void setWeavePlanId(Long weavePlanId) {
        this.weavePlanId = weavePlanId;
    }

    public Integer getIsProducing() {
        return isProducing;
    }

    public void setIsProducing(Integer isProducing) {
        this.isProducing = isProducing;
    }

    public Long getPrior() {
        return prior;
    }

    public void setPrior(Long prior) {
        this.prior = prior;
    }


}

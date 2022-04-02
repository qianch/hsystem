package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Desc("调度实例")
@Entity
@Table(name = "platform_schedule_instance")
public class ScheduleInstance extends BaseEntity {
    public static final String STATUS_RUN = "RUN";
    public static final String STATUS_STOP = "STOP";

    @Desc("调度任务模板ID")
    @Column(nullable = false)
    private Long templateId;

    @Desc("CRON表达式")
    @Column(nullable = false)
    private String cron;

    @Desc("状态")
    @Column(nullable = false)
    private String status;

    @Desc("描述")
    private String instanceDesc;

    @Desc("是否可编辑")
    private Integer editable;

    public ScheduleInstance() {
    }

    public Long getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getCron() {
        return this.cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstanceDesc() {
        return this.instanceDesc;
    }

    public void setInstanceDesc(String instanceDesc) {
        this.instanceDesc = instanceDesc;
    }

    public Integer getEditable() {
        return this.editable;
    }

    public void setEditable(Integer editable) {
        this.editable = editable;
    }
}

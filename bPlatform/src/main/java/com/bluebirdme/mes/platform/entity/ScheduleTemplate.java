package com.bluebirdme.mes.platform.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Desc("任务调度模板")
@Entity
@Table(name = "platform_schedule_template")
public class ScheduleTemplate extends BaseEntity {
    @Desc("模板名称")
    @Column(nullable = false)
    private String templateName;

    @Desc("对应的执行的类名")
    @Column(nullable = false)
    private String clazz;

    @Desc("创建时间")
    @Column(nullable = false)
    private Date createTime;

    @Desc("描述")
    private String templateDesc;

    public ScheduleTemplate() {
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTemplateDesc() {
        return this.templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }
}

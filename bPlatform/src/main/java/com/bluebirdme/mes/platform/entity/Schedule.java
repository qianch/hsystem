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

/**
 * @deprecated
 */
@Deprecated
@Entity
@Table(name = "platform_schedule")
@Desc("任务调度")
public class Schedule extends BaseEntity {
    @Desc("任务类名")
    @Column(name = "clazz", unique = true)
    private String clazz;

    @Desc("任务名称")
    @Column(name = "name", nullable = false)
    private String name;

    @Desc("任务类型")
    @Column(name = "type", nullable = false)
    private Integer type;

    @Desc("cron表达式")
    @Column(name = "cron")
    private String cron;

    @Desc("创建时间")
    @Column(name = "createTime", nullable = false)
    private Date createTime;

    @Desc("启动时间")
    @Column(name = "startTime", nullable = false)
    private Date startTime;

    @Desc("执行间隔")
    @Column(name = "intervals")
    private Integer intervals;

    @Desc("执行次数")
    @Column(name = "times")
    private Integer times;

    @Desc("已执行次数")
    @Column(name = "executeTimes")
    private Integer executeTimes;

    @Desc("状态")
    @Column(name = "status", nullable = false)
    private Integer status;

    @Desc("运行描述")
    @Column(name = "runDesc")
    private String runDesc;

    public Schedule() {
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCron() {
        return this.cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getIntervals() {
        return this.intervals;
    }

    public void setIntervals(Integer intervals) {
        this.intervals = intervals;
    }

    public Integer getTimes() {
        return this.times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getExecuteTimes() {
        return this.executeTimes;
    }

    public void setExecuteTimes(Integer executeTimes) {
        this.executeTimes = executeTimes;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRunDesc() {
        return this.runDesc;
    }

    public void setRunDesc(String runDesc) {
        this.runDesc = runDesc;
    }
}

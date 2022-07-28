package com.bluebirdme.mes.planner.produce.entity;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 包装任务
 *
 * @author Goofy
 * @Date 2017年5月26日 上午9:56:23
 */
@Desc("包装任务列表")
@Entity
@Table(name = "hs_produce_package_task")
public class PackageTask extends BaseEntity {
    @Desc("包装BOM ID")
    @Column
    private Long packageBomId;

    @Desc("包装代码")
    @Column
    private String packageCode;

    @Desc("每托卷数")
    @Column
    private Integer rollCountPerTray;

    @Desc("托数")
    @Column
    private Integer trayCount;

    @Desc("生产明细")
    @Column
    private Long producePlanDetailId;

    @Desc("打印张数")
    @Column
    private Integer printTrayCount;

    public Long getPackageBomId() {
        return packageBomId;
    }

    public void setPackageBomId(Long packageBomId) {
        this.packageBomId = packageBomId;
    }

    public Integer getTrayCount() {
        return trayCount;
    }

    public void setTrayCount(Integer trayCount) {
        this.trayCount = trayCount;
    }

    public Long getProducePlanDetailId() {
        return producePlanDetailId;
    }

    public void setProducePlanDetailId(Long producePlanDetailId) {
        this.producePlanDetailId = producePlanDetailId;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public Integer getRollCountPerTray() {
        return rollCountPerTray;
    }

    public void setRollCountPerTray(Integer rollCountPerTray) {
        this.rollCountPerTray = rollCountPerTray;
    }

    public Integer getPrintTrayCount() {
        return printTrayCount;
    }

    public void setPrintTrayCount(Integer printTrayCount) {
        this.printTrayCount = printTrayCount;
    }

}

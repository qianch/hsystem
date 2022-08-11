package com.bluebirdme.mes.siemens.order.entity;

import com.bluebirdme.mes.core.annotation.Desc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 裁剪任务单图纸BOM
 *
 * @author Goofy
 * @Date 2017年7月31日 下午12:52:03
 */
@Desc("裁剪任务单图纸BOM")
@Entity
@Table(name = "hs_siemens_cut_task_drawings")
public class CutTaskDrawings extends FragmentSummary {

    @Desc("裁剪任务单ID")
    @Column
    private Long ctId;

    public Long getCtId() {
        return ctId;
    }

    public void setCtId(Long ctId) {
        this.ctId = ctId;
    }
}

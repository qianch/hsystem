package com.bluebirdme.mes.siemens.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.siemens.bom.entity.BaseSuit;

/**
 * 任务单组套BOM
 * @author Goofy
 * @Date 2017年7月31日 下午4:09:52
 */
@Entity
@Table(name="hs_siemens_cut_task_suit")
public class CutTaskSuit extends BaseSuit implements Comparable<CutTaskSuit> {
	
	@Desc("裁剪任务单ID")
	@Column(nullable=false)
	public Long ctId;

	public Long getCtId() {
		return ctId;
	}

	public void setCtId(Long ctId) {
		this.ctId = ctId;
	}

	@Override
	public int compareTo(CutTaskSuit cts) {
		return this.getSuitSort()-cts.getSuitSort();
	}

}

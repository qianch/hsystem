package com.bluebirdme.mes.siemens.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.entity.BaseEntity;

/**
 * 裁剪任务单
 * 
 * @author Goofy
 * @Date 2017年7月26日 上午10:37:32
 */
@Desc("西门子裁剪任务单")
@Entity
@Table(name = "hs_siemens_cut_task")
public class CutTask extends BaseTask {

}

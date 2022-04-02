/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.scheduleLog.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import org.springframework.stereotype.Repository;
/**
 * 
 * @author 徐秦冬
 * @Date 2018-2-8 10:50:23
 */

public interface IScheduleLogDao extends IBaseDao {
	public void clearAll();
}

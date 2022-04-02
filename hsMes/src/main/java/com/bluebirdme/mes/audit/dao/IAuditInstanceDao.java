/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.audit.dao;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
/**
 * 
 * @author 高飞
 * @Date 2016-10-25 13:52:50
 */

public interface IAuditInstanceDao extends IBaseDao {
	/**
	 * 我的待审核任务
	 * @param filter
	 * @param page
	 * @return
	 */
	public Map<String, Object> auditTask(Filter filter, Page page);
	
	/**
	 * 已审核任务
	 * @param filter
	 * @param page
	 * @return
	 */
	public Map<String, Object> finishedAuditTask(Filter filter, Page page);
	
	/**
	 * 我提交的审核任务
	 * @param filter
	 * @param page
	 * @return
	 */
	public Map<String, Object> myAuditTask(Filter filter, Page page);
	

	public <T> void updateByCondition(Class<T> clazz,Map<String,Object> condition,Map<String,Object> values);
}

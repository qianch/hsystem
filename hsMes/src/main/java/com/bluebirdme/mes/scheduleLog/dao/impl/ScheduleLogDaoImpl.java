/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.scheduleLog.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.springframework.stereotype.Repository;
import com.bluebirdme.mes.scheduleLog.dao.IScheduleLogDao;

/**
 * 
 * @author 徐秦冬
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class ScheduleLogDaoImpl extends BaseDaoImpl implements IScheduleLogDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"scheduleLog-list");
	}
	
	public void clearAll(){
		String sql="delete from hs_schedule_log";
		SQLQuery query=getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

}

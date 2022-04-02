/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.audit.dao.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.audit.dao.IAuditInstanceDao;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

/**
 * 
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class AuditInstanceDaoImpl extends BaseDaoImpl implements IAuditInstanceDao {
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"auditInstance-list");
	}

	public Map<String, Object> auditTask(Filter filter, Page page){
		return this.findPageInfo(filter,page,"auditTask-unfinished");
	}

	@Override
	public Map<String, Object> finishedAuditTask(Filter filter, Page page) {
		return this.findPageInfo(filter,page,"auditTask-finished");
	}
	
	@Override
	public Map<String, Object> myAuditTask(Filter filter, Page page) {
		Map<String, Object> findPageInfo = this.findPageInfo(filter,page,"auditTask-myAudit");
		return findPageInfo;
	}

	public <T> void updateByCondition(Class<T> clazz,Map<String,Object> condition,Map<String,Object> values){
		StringBuffer sb=new StringBuffer("update "+clazz.getSimpleName()+" set ");
		
		Iterator<Entry<String, Object>> it=values.entrySet().iterator();
		Entry<String,Object> entry;
		int count=0;
		while(it.hasNext()){
			entry=it.next();
			sb.append((count==0?"":",")+entry.getKey()+"=:v"+entry.getKey());
			count++;
		}
		it=condition.entrySet().iterator();
		sb.append(" where 1=1 ");
		while(it.hasNext()){
			entry=it.next();
			sb.append(" and "+entry.getKey()+"=:w"+entry.getKey());
		}
		Query query=getSession().createQuery(sb.toString());
		
		it=values.entrySet().iterator();
		while(it.hasNext()){
			entry=it.next();
			query.setParameter("v"+entry.getKey(), entry.getValue());
		}
		
		it=condition.entrySet().iterator();
		while(it.hasNext()){
			entry=it.next();
			query.setParameter("w"+entry.getKey(), entry.getValue());
		}
		query.executeUpdate();
	}
}

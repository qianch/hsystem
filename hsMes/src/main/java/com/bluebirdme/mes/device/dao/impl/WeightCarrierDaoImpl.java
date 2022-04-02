/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;

import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.device.dao.IWeightCarrierDao;
import com.bluebirdme.mes.device.entity.WeightCarrier;

/**
 * 
 * @author 孙利
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class WeightCarrierDaoImpl extends BaseDaoImpl implements IWeightCarrierDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"weightCarrier-list");
	}
	@Override
	public List<String> getWeightCodes() {
		String sql=SQL.get("carrierCodes");
		SQLQuery query=getSession().createSQLQuery(sql);
		return query.list();
	}

	@Override
	public WeightCarrier getCarrierById(int id) {
		String sql=SQL.get("weightCarrier");
		SQLQuery query=getSession().createSQLQuery(sql);
		return  (WeightCarrier) query.list();
	}

	@Override
	public WeightCarrier findByCode(String carrierCode) {
		String hql="From WeightCarrier where carrierCode=:carrierCode";
		Query query=getSession().createQuery(hql);
		return (WeightCarrier) query.setParameter("carrierCode", carrierCode).uniqueResult();
	}

}

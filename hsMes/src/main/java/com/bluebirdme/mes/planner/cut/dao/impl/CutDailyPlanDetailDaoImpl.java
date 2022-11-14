/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;

import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.planner.cut.dao.ICutDailyPlanDetailDao;

/**
 * 
 * @author 宋黎明
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class CutDailyPlanDetailDaoImpl extends BaseDaoImpl implements ICutDailyPlanDetailDao {
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"cutDailyPlan-list");
	}

	@Override
	public Map<String, Object> findNofinish(Filter filter, Page page) {
		return this.findPageInfo(filter,page,"findNoFinishCutPlan");
	}

	@Override
	public List<Map<String, Object>> findC(List<Long> ids) {
		String sql = SQL.get("findCutPlan-list");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameterList("id", ids).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findPlanUsers(Long cutPlanDetailId) {
		String sql = SQL.get("findCutPlanUserByDetailId");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("cutPlanDetailId", cutPlanDetailId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findWorkShop(String cutPlanId) {
		String sql = SQL.get("cutDailyPlanDetail-list");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("cutPlanId",cutPlanId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
}

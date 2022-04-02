/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.planner.cut.dao.ICutPlanDao;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;

/**
 * 
 * @author 宋黎明
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class CutPlanDaoImpl extends BaseDaoImpl implements ICutPlanDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"cutPlan-list");
	}
	
	@Override
	public List<Map<String, Object>> findCutPlan(String planCode) throws Exception {
		String sql=SQL.get("cutPlan-list");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("planCode", planCode);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	@Override
	public List<Map<String, Object>> userListById(String id) throws Exception {
		String sql=SQL.get("userListBydetailId");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	@Override
	public void deleteCutPlans(String ids) {
		getSession().createSQLQuery(SQL.get("deleteCutPlans-list")).setParameterList("id", ids.split(",")).executeUpdate();
	}
	
	@Override
	public void deleteCutPlansUsers(String ids) {
		getSession().createSQLQuery(SQL.get("deleteUsers-list")).setParameterList("id", ids.split(",")).executeUpdate();
	}
	
	//根据生产明细ID找人员信息
	@Override
	public List<Map<String, Object>> planCodeCombobox() throws Exception {
		String sql=SQL.get("planCode-combobox");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	@Override
	public Map<String, Object> dataList(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"cutPlan-datalist");
	}

	@Override
	public void deleteUserByPartId(Long tcBomPartId) {
		getSession().createSQLQuery(SQL.get("deleteUsers-list")).setParameter("id", tcBomPartId).executeUpdate();
	}
	
	@Override
	public List<HashMap<String,Object>> findCutPlanUserByUserID(Long userId) throws Exception {
		String sql=SQL.get("findCutPlanUserByUserID");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("userId", userId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public Map<String, Object> findCutPageInfo(Filter filter, Page page) {
		return findPageInfo(filter,page,"cp-list");
	}

	@Override
	public Map<String, Object> countRollsAndTrays(Long wid) {
		return (Map<String, Object>) getSession().createSQLQuery(SQL.get("count-weave-rolls-trays"))
				.setParameter("id", wid).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
	}

}

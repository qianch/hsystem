/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.core.sql.SQL;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import com.bluebirdme.mes.planner.produce.dao.IProducePlanDetailDao;

/**
 *
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class ProducePlanDetailDaoImpl extends BaseDaoImpl implements IProducePlanDetailDao {

	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"producePlanDetail-list");
	}

	@Override
	public <T> Map<String, Object> findProducePlanDetail(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"producePlanDetail-list2");
	}

	@Override
	public List<Map<String,Object>> findProducePlanDetailPrints(Long producePlanDetailId) throws Exception {
		String sql= SQL.get("findProducePlanDetailPrints");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("producePlanDetailId", producePlanDetailId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String,Object>> findPlanDetailPrintsBybtwFileId(Long producePlanDetailId,long btwFileId) throws Exception {
		String sql= SQL.get("findPlanDetailPrintsBybtwFileId");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("producePlanDetailId", producePlanDetailId);
		query.setParameter("btwFileId", btwFileId);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.baseInfo.dao.IBcBomDao;
import com.bluebirdme.mes.baseInfo.entity.BcBom;

/**
 * 
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class BcBomDaoImpl extends BaseDaoImpl implements IBcBomDao {

	@Resource
	SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page)
			throws Exception {
		return findPageInfo(filter, page, "bcBom-list");
	}

	@Override
	public void delete(String ids) throws Exception {
		String sql = "delete from HS_Bc_Bom_Version  where id in :id";
		SQLQuery query = getSession().createSQLQuery(sql);
		ArrayList list1 = new ArrayList();
		String _ids[] = ids.split(",");
		for (int a = 0; a < _ids.length; a++) {
			list1.add(_ids[a]);

		}
		query.setParameterList("id", list1);
		query.executeUpdate();
	}

	public List<Map<String, Object>> getBcBomJson(String data) throws SQLTemplateException {

		
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("data", data);
		String sql=SQL.get(map, "bcBom-list");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		return query.list();
	}

	@Override
	public List<Map<String, Object>> getBcBomJsonTest(String data)
			throws SQLTemplateException {
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("data", data);
		String sql=SQL.get(map, "bcBom-list1");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		return query.list();
	}
	
	@Override
	public List<Map<String, Object>> getBcBomJsonTest1(String data)
			throws SQLTemplateException {
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("data", data);
		String sql=SQL.get(map, "bcBom-list2");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		return query.list();
	}
}

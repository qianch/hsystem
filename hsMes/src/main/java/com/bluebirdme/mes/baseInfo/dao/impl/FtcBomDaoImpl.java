/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.dao.impl;

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

import com.bluebirdme.mes.baseInfo.dao.IFtcBomDao;

/**
 * 
 * @author 宋黎明
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class FtcBomDaoImpl extends BaseDaoImpl implements IFtcBomDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	//非套材明细列表
	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return findPageInfo(filter, page,"ftc-bom-detailList");
	}
	
	public void delete(String ids) {
		getSession().createSQLQuery(SQL.get("ftc-bom-Delete")).setParameterList("id", ids.split(",")).executeUpdate();
		getSession().createSQLQuery(SQL.get("ftc-bom-DeleteAndVersion")).setParameterList("id", ids.split(",")).executeUpdate();
		getSession().createSQLQuery(SQL.get("ftc-bom-versionDeleteAndDetail")).executeUpdate();
	}
	
	public void deleteDetail(String ids) {
		getSession().createSQLQuery(SQL.get("ftc-bom-detailDelete")).setParameterList("id", ids.split(",")).executeUpdate();
	}
	
	@Override
	public void deleteBomVersion(String ids) {
		getSession().createSQLQuery(SQL.get("ftc-bom-versionDelete")).setParameterList("id", ids.split(",")).executeUpdate();
		getSession().createSQLQuery(SQL.get("ftc-bom-versionDeleteAndDetail")).executeUpdate();
	}
	
	public List<Map<String, Object>> getFtcBomJson(String data) throws SQLTemplateException {
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("data", data);
		String sql=SQL.get(map, "ftc-Bom-list");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	public List<Map<String, Object>> getFtcBomJsonTest(String data) throws SQLTemplateException {
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("data", data);
		String sql=SQL.get(map, "ftc-Bom-list1");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	public List<Map<String, Object>> getFtcBomJsonTest1(String data) throws SQLTemplateException {
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("data", data);
		String sql=SQL.get(map, "ftc-Bom-list2");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	public List<Map<String, Object>> getFtcBomByVersionJson(String id) throws SQLTemplateException {
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		String sql=SQL.get(map, "ftc-Bom-VersionList");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public Map<String, Object> findPageInfo1(Filter filter, Page page) {
		return this.findPageInfo(filter, page,"ftc-bom-mirrorDetailList");
	}


}

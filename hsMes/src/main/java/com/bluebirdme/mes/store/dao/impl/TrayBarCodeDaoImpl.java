/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.dao.impl;

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

import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.store.dao.ITrayBarCodeDao;

/**
 *
 * @author 宋黎明
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class TrayBarCodeDaoImpl extends BaseDaoImpl implements ITrayBarCodeDao {

	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return findPageInfo(filter,page,"trayBarcode-list");
	}

	@Override
	public List<Map<String, Object>> findSalesOrderByRollcode(String code) {
		String sql=SQL.get("salesOrderbyRollBarcode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("code", code);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findSalesOrderByBoxcode(String code) {
		String sql=SQL.get("salesOrderbyRollBarcode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("code", code);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findSalesOrderByTraycode(String code) {
		String sql=SQL.get("salesOrderbyRollBarcode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("code", code);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findProductByRollcode(String code) {
		String sql=SQL.get("salesOrderbyRollBarcode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("code", code);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findProductByBoxcode(String code) {
		String sql=SQL.get("salesOrderbyRollBarcode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("code", code);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}


	@Override
	public List<Map<String, Object>> findSalesOrderByBarcode(String code) {
		String sql=SQL.get("product-byBarcode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("code", code);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findTrayBoxRollByBarcode(String code) {
		String sql=SQL.get("iTraybarcode-byBarcode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameterList("code", code.split(","));
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findIbarcodeByBarcode(String code) {
		String sql=SQL.get("ibarcode-byBarcode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameterList("code", code.split(","));
		List<Map<String, Object>>list=  query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return  list;
	}

	@Override
	public List<Map<String, Object>> findMaxTrayBarCode(long btwfileId) {
		String sql = SQL.get("findMaxTrayBarCode");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("btwfileId", btwfileId);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@Override
	public List<Map<String, Object>> findMaxTrayBarCodeCount()
	{
		String sql = SQL.get("findMaxTrayBarCodeCount");
		SQLQuery query = getSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@Override
	public List<Map<String, Object>> findMaxTrayPartBarCodeCount()
	{
		String sql = SQL.get("findMaxTrayPartBarCodeCount");
		SQLQuery query = getSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
}

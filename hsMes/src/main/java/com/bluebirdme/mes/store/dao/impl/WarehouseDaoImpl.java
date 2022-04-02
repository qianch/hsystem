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

import com.bluebirdme.mes.core.sql.SQLTemplateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.store.dao.IWarehouseDao;

/**
 * 
 * @author 肖文彬
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class WarehouseDaoImpl extends BaseDaoImpl implements IWarehouseDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return findPageInfo(filter, page,"warehouse-list");
	}
	
	public void delete(String ids) {
		getSession().createSQLQuery(SQL.get("warehouse-delete")).setParameterList("id", ids.split(",")).executeUpdate();
	}

	@Override
	public void updateS(String ids) {
		getSession().createSQLQuery(SQL.get("warehouse-updateS")).setParameterList("id", ids.split(",")).executeUpdate();
	}

	@Override
	public List<Map<String, Object>> combobox() {
		String sql=SQL.get("warehouse-warehouse");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> queryWarehousebyType(String waretype)  throws SQLTemplateException
	{
		String[] waretypes = waretype.split(",");
		String strType="";
		for(int i=0;i<waretypes.length;i++)
		{
			strType+="'"+waretypes[i]+"',";
		}
		if(strType.length()>0)
		{
			strType=strType.substring(0,strType.length() - 1);
		}
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("waretype", strType);
		String sql=SQL.get(map, "warehouse-combobox");
		return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
    
}

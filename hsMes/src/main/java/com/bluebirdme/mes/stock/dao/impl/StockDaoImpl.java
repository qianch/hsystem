package com.bluebirdme.mes.stock.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.Array;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.stock.dao.IStockDao;

/**
 * 库存查询
 * @author Goofy
 * @Date 2016年11月24日 下午3:05:02
 */
@Repository
public class StockDaoImpl extends BaseDaoImpl implements IStockDao{
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}
	
	public Map<String, Object> list(String type,String[] kuweis) throws SQLTemplateException{
		
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("wtype", type);
		String sql;
		Map<String, Object> ret=new HashMap<String,Object>();
		
		List<Map<String,Object>> trays=null;
		List<Map<String,Object>> rolls=null;
		List<Map<String,Object>> materials=null;

		if(type.equals("cp")){
			
			param.put("ptype", "tray");
			sql=SQL.get(param,"stock-sum");
			trays=getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("pos", kuweis).list();
			param.put("ptype", "roll");
			sql=SQL.get(param,"stock-sum");
			rolls=getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("pos", kuweis).list();
			
			if(trays!=null){
				for(Map<String,Object> map:trays){
					ret.put(map.get("WAREHOUSEPOSCODE").toString(), map.get("TRAYCOUNT"));
				}
			}
			
			if(rolls!=null){
				for(Map<String,Object> map:rolls){
					ret.put(map.get("WAREHOUSEPOSCODE").toString(), ret.get(map.get("WAREHOUSEPOSCODE").toString())==null?(";"+map.get("ROLLCOUNT")):(ret.get(map.get("WAREHOUSEPOSCODE").toString())+";"+map.get("ROLLCOUNT")));
				}
			}
			
		}else{
			sql=SQL.get(param,"stock-sum");
			materials=getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("pos", kuweis).list();
			
			if(materials!=null){
				for(Map<String,Object> map:materials){
					ret.put(map.get("WAREHOUSEPOSCODE").toString(), map.get("WEIGTH"));
				}
			}
		}
		
		System.out.println(GsonTools.toJson(ret));
		return ret;
		
	}

}

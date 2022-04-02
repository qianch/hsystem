package com.bluebirdme.mes.mobile.stock.dao.impl;

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
import com.bluebirdme.mes.mobile.stock.dao.IMobilePackageDao;
import com.bluebirdme.mes.store.entity.Box;
import com.bluebirdme.mes.store.entity.BoxRoll;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBoxRoll;

/**
 * 打包Dao
 * @author Goofy
 * @Date 2016年11月18日 上午9:35:32
 */
@Repository
public class MobilePackageDaoImpl extends BaseDaoImpl implements IMobilePackageDao {

	@Resource SessionFactory factory;
	
	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}
	
	public void saveBoxRoll(Box box,BoxRoll[] boxRolls,Long planId,String partName){
		super.save(box);
		super.save(boxRolls);
	}
	
	public void saveTrayBoxRoll(Tray tray,TrayBoxRoll[] trayBoxRolls,Long planId,String partName){
		super.save(tray);
		super.save(trayBoxRolls);
	}
	
	public List<Map<String,Object>> isPackedRoll(String[] rolls){
		String sql = SQL.get("isPacked-roll");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("rolls", rolls).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
		
	}

	@Override
	public List<Map<String, Object>> isPackedBoxRoll(String[] rolls, String[] boxs) {
		String sql = SQL.get("isPacked-BoxRoll");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("rolls", rolls)
			 .setParameter("boxs", boxs)
			 .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

}

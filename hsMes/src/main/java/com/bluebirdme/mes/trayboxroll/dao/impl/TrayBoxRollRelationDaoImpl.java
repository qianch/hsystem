package com.bluebirdme.mes.trayboxroll.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.trayboxroll.dao.TrayBoxRollRelationDao;

@Repository
public class TrayBoxRollRelationDaoImpl extends BaseDaoImpl implements TrayBoxRollRelationDao{

	@Resource SessionFactory factory;
	
	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page)
			throws Exception {
		return this.findPageInfo(filter,page,"trayboxrollrelation-list");
	}

	public <T> Map<String,Object> findPageInfoRollWeight(Filter filter,Page page)
	        throws Exception{
		return  this.findPageInfo(filter,page,"trayboxrollrelation-totalrollweight");
	}
}

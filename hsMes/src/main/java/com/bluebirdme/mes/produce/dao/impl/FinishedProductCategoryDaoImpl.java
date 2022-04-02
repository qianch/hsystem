package com.bluebirdme.mes.produce.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.produce.dao.IFinishedProductCategoryDao;


/**
 * 
 * @author king
 *
 */
@Repository
public class FinishedProductCategoryDaoImpl extends BaseDaoImpl implements IFinishedProductCategoryDao{

	@Resource SessionFactory factory;
	
	@Override
	public Session getSession() {
		// TODO Auto-generated method stub
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page)throws Exception {
		// TODO Auto-generated method stub
		return this.findPageInfo(filter,page,"productCategory-list");
	}

}

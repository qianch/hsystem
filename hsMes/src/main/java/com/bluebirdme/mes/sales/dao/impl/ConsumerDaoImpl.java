package com.bluebirdme.mes.sales.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.sales.dao.IConsumerDao;
@Repository
public class ConsumerDaoImpl extends BaseDaoImpl implements IConsumerDao {
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return findPageInfo(filter, page,"consumer-list");
	}

	public void delete(String ids) {
		getSession().createSQLQuery(SQL.get("consumer-delete")).setParameterList("id", ids.split(",")).executeUpdate();
	}

	@Override
	public void old(String ids) {
		getSession().createSQLQuery(SQL.get("consumer-old")).setParameterList("id", ids.split(",")).executeUpdate();
	}
}

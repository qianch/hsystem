/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.barcode.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.springframework.stereotype.Repository;
import com.bluebirdme.mes.siemens.barcode.dao.ISuitErrorLogDao;

/**
 * 
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class SuitErrorLogDaoImpl extends BaseDaoImpl implements ISuitErrorLogDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"suitErrorLog-list");
	}

}

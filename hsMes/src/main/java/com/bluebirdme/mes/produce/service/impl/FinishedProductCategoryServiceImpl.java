package com.bluebirdme.mes.produce.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.produce.dao.IFinishedProductCategoryDao;
import com.bluebirdme.mes.produce.service.IFinishedProductCategoryService;

/**
 * 
 * @author king
 *
 */
@Service
@AnyExceptionRollback
public class FinishedProductCategoryServiceImpl extends BaseServiceImpl implements IFinishedProductCategoryService{
	
	@Resource
	IFinishedProductCategoryDao productCategoryDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		// TODO Auto-generated method stub
		return productCategoryDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		// TODO Auto-generated method stub
		return productCategoryDao.findPageInfo(filter, page);
	}

}

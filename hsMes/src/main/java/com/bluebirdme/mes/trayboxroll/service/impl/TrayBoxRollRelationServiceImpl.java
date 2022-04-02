package com.bluebirdme.mes.trayboxroll.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.trayboxroll.dao.TrayBoxRollRelationDao;
import com.bluebirdme.mes.trayboxroll.service.TrayBoxRollRelationService;

@Service
@AnyExceptionRollback
public class TrayBoxRollRelationServiceImpl extends BaseServiceImpl implements TrayBoxRollRelationService{

	@Resource TrayBoxRollRelationDao trayBoxRollRelationDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return trayBoxRollRelationDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page)
			throws Exception {
		return trayBoxRollRelationDao.findPageInfo(filter, page);
	}

	public <T> Map<String,Object> findPageInfoRollWeight(Filter filter,Page page)
	        throws Exception{
		return  trayBoxRollRelationDao.findPageInfoRollWeight(filter,page);
	}
}

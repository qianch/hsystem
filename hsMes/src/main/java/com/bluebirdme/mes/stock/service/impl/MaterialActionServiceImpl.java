package com.bluebirdme.mes.stock.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bluebirdme.mes.baseInfo.dao.IMaterialDao;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.stock.dao.IMaterialActionDao;
import com.bluebirdme.mes.stock.service.IMaterialActionService;
import com.bluebirdme.mes.stock.service.IMaterialStockService;
@Service
@AnyExceptionRollback
public class MaterialActionServiceImpl extends BaseServiceImpl implements IMaterialActionService {
	@Resource IMaterialActionDao materialActionDao  ;
	@Resource IMaterialStockService mssService;
	
	@Override
	protected IBaseDao getBaseDao() {
		return materialActionDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return materialActionDao.findPageInfo(filter,page);
	}
	
	public <T> Map<String, Object> findPageOutInfo(Filter filter, Page page) throws Exception {
		return materialActionDao.findPageOutInfo(filter,page);
	}

	@Override
	public <T> Map<String, Object> findPageDetailInfo(Filter filter, Page page)
			throws Exception {
		return materialActionDao.findPageDetailInfo(filter,page);
	}
	
	/**
	 * 根据ID 冻结\解冻|放行\取消放行
	 * @param mssId
	 * @param action
	 * @param actionValue
	 * @throws Exception 
	 */
	public void action(Long mssId[],String action,String actionValue) throws Exception{
		String codes=mssService.validMaterialStockState(mssId);
		if(!StringUtils.isEmpty(codes)){
			throw new Exception(codes+"不在库,无法操作");
		}
		materialActionDao.action(mssId, action, actionValue);
	}

	@Override
	public <T> Map<String, Object> findPageForceOutInfo(Filter filter, Page page)
			throws Exception {
		return  materialActionDao.findPageForceOutInfo(filter,page);
	}
	
}

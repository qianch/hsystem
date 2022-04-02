/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.baseInfo.service.ITcBomVersionPartsDetailService;
import com.bluebirdme.mes.baseInfo.dao.ITcBomVersionPartsDetailDao;

/**
 * 
 * @author 肖文彬
 * @Date 2016-10-9 13:49:37
 */
@Service
@AnyExceptionRollback
public class TcBomVersionPartsDetailServiceImpl extends BaseServiceImpl implements ITcBomVersionPartsDetailService {
	
	@Resource ITcBomVersionPartsDetailDao tcBomVersionPartsDetailDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return tcBomVersionPartsDetailDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return tcBomVersionPartsDetailDao.findPageInfo(filter, page);
	}
    
	public void delete(String ids) {
		tcBomVersionPartsDetailDao.delete(ids);
	}

	@Override
	public Map<String, Object> findPageInfo1(Filter filter, Page page) {
		return tcBomVersionPartsDetailDao.findPageInfo1(filter, page);
	}
}

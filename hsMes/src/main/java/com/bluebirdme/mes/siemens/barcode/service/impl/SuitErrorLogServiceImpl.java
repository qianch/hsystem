/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.barcode.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.siemens.barcode.dao.ISuitErrorLogDao;
import com.bluebirdme.mes.siemens.barcode.service.ISuitErrorLogService;

/**
 * 
 * @author 高飞
 * @Date 2017-8-9 16:30:35
 */
@Service
@AnyExceptionRollback
public class SuitErrorLogServiceImpl extends BaseServiceImpl implements ISuitErrorLogService {
	
	@Resource ISuitErrorLogDao suitErrorLogDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return suitErrorLogDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return suitErrorLogDao.findPageInfo(filter,page);
	}

}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.common.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.mobile.common.service.IAppVersionService;
import com.bluebirdme.mes.mobile.common.dao.IAppVersionDao;
import com.bluebirdme.mes.mobile.common.entity.AppVersion;

/**
 * 
 * @author 高飞
 * @Date 2016-11-6 10:22:52
 */
@Service
@AnyExceptionRollback
public class AppVersionServiceImpl extends BaseServiceImpl implements IAppVersionService {
	
	@Resource IAppVersionDao appVersionDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return appVersionDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return appVersionDao.findPageInfo(filter,page);
	}
	
	public void save(AppVersion app){
		appVersionDao.save(app);
	}

}

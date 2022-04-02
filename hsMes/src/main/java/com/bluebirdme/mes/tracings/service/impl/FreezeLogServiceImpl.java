/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.tracings.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.tracings.dao.IFreezeLogDao;
import com.bluebirdme.mes.tracings.service.IFreezeLogService;

/**
 * 冻结解冻记录
 * @author 宋黎明
 * @Date 2016年12月6日 下午1:35:34
 */
@Service
@AnyExceptionRollback
public class FreezeLogServiceImpl extends BaseServiceImpl implements IFreezeLogService {
	
	@Resource IFreezeLogDao freezeLogDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return freezeLogDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return freezeLogDao.findPageInfo(filter,page);
	}

}

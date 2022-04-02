package com.bluebirdme.mes.sales.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.sales.dao.IConsumerDao;
import com.bluebirdme.mes.sales.service.IConsumerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
@Service
@AnyExceptionRollback
public class ConsumerServiceImpl extends BaseServiceImpl implements
		IConsumerService {
	@Resource IConsumerDao consumerDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return consumerDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return consumerDao.findPageInfo(filter, page);
	}

	public void delete(String ids) {
		consumerDao.delete(ids);
	}

	@Override
	public void old(String ids) {
		consumerDao.old(ids);
	}
}

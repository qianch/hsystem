/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.produce.service.IProduceCalendarService;
import com.bluebirdme.mes.produce.dao.IProduceCalendarDao;

/**
 * 
 * @author 高飞
 * @Date 2016-11-1 13:05:53
 */
@Service
@AnyExceptionRollback
public class ProduceCalendarServiceImpl extends BaseServiceImpl implements IProduceCalendarService {
	
	@Resource IProduceCalendarDao produceCalendarDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return produceCalendarDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return produceCalendarDao.findPageInfo(filter,page);
	}
	
	public List<Map<String, Object>> findList(String start,String end){
		return produceCalendarDao.findList(start, end);
	}
}

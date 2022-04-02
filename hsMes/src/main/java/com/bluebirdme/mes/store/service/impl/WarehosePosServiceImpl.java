/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.store.dao.IWarehosePosDao;
import com.bluebirdme.mes.store.service.IWarehosePosService;

/**
 * 
 * @author 肖文彬
 * @Date 2016-9-29 16:26:04
 */
@Service
@AnyExceptionRollback
public class WarehosePosServiceImpl extends BaseServiceImpl implements IWarehosePosService {
	
	@Resource IWarehosePosDao warehosePosDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return warehosePosDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return warehosePosDao.findPageInfo(filter, page);
	}

	@Override
	public void delete(String ids) {
		warehosePosDao.delete(ids);
		
	}

	@Override
	public void updateS(String ids) {
		warehosePosDao.updateS(ids);
	}
}

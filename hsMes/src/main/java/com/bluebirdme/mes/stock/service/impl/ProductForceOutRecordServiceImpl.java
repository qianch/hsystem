/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.stock.service.IProductForceOutRecordService;
import com.bluebirdme.mes.stock.dao.IProductForceOutRecordDao;

/**
 * 
 * @author 徐波
 * @Date 2017-2-13 14:10:25
 */
@Service
@AnyExceptionRollback
public class ProductForceOutRecordServiceImpl extends BaseServiceImpl implements IProductForceOutRecordService {
	
	@Resource IProductForceOutRecordDao productForceOutRecordDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return productForceOutRecordDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return productForceOutRecordDao.findPageInfo(filter,page);
	}

}

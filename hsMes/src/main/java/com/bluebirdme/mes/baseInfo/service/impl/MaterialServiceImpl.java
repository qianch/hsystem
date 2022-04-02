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
import com.bluebirdme.mes.baseInfo.service.IMaterialService;
import com.bluebirdme.mes.baseInfo.dao.IMaterialDao;

/**
 * 
 * @author 高飞
 * @Date 2016-10-12 11:06:09
 */
@Service
@AnyExceptionRollback
public class MaterialServiceImpl extends BaseServiceImpl implements IMaterialService {
	
	@Resource IMaterialDao materialDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return materialDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return materialDao.findPageInfo(filter,page);
	}
	
	public Map<String,Object> materialInfo(String code){
		return materialDao.materialInfo(code);
	}

}

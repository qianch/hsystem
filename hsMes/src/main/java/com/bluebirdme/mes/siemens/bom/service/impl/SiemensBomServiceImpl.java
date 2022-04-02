/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.bom.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.siemens.bom.dao.ISiemensBomDao;
import com.bluebirdme.mes.siemens.bom.service.ISiemensBomService;

/**
 * 
 * @author 高飞
 * @Date 2017-7-18 14:54:41
 */
@Service
@AnyExceptionRollback
public class SiemensBomServiceImpl extends BaseServiceImpl implements ISiemensBomService {
	
	@Resource ISiemensBomDao siemensBomDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return siemensBomDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return siemensBomDao.findPageInfo(filter,page);
	}
	
	public List<Map<String, Object>> findPageInfo(Boolean siemens,String code) throws Exception{
		return siemensBomDao.findPageInfo(siemens,code);
	}

	public List<Map<String,Object>> listAllParts(Long tcBomId){
		return siemensBomDao.listAllParts(tcBomId);
	}
	public int enableOrDisable(Long partId,Integer enable,Boolean isDrawingsBom){
		return siemensBomDao.enableOrDisable(partId, enable, isDrawingsBom);
	}
}

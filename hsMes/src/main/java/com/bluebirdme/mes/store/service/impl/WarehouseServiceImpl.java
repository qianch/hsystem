/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.store.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import com.bluebirdme.mes.core.sql.SQLTemplateException;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.store.dao.IWarehouseDao;
import com.bluebirdme.mes.store.service.IWarehouseService;

/**
 * 
 * @author 肖文彬
 * @Date 2016-9-29 15:45:32
 */
@Service
@AnyExceptionRollback
public class WarehouseServiceImpl extends BaseServiceImpl implements IWarehouseService {
	
	@Resource IWarehouseDao warehouseDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return warehouseDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return warehouseDao.findPageInfo(filter, page);
	}
    
	public void delete(String ids) {
		warehouseDao.delete(ids);
	}

	@Override
	public void updateS(String ids) {
		warehouseDao.updateS(ids);
	}

	@Override
	public List<Map<String, Object>> warehouse() {
		List<Map<String, Object>> listMap = warehouseDao.combobox();
		Map<String, Object> ret =null;
		List<Map<String, Object>> _ret = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : listMap) {
			ret= new HashMap<String, Object>();
			ret.put("value", MapUtils.getAsLong(map, "ID"));
			ret.put("text", MapUtils.getAsString(map, "WAREHOUSENAME"));
			_ret.add(ret);
		}
		return _ret;
	}

	@Override
	public List<Map<String, Object>> queryWarehousebyType(String waretype) throws SQLTemplateException
	{
		return warehouseDao.queryWarehousebyType(waretype);
	}
}

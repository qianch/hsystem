package com.bluebirdme.mes.device.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.device.dao.IDeviceTypeDao;
import com.bluebirdme.mes.device.entity.DeviceType;
import com.bluebirdme.mes.device.service.IDeviceTypeService;
@Service
@AnyExceptionRollback
public class DeviceTypeServiceImpl extends BaseServiceImpl implements
		IDeviceTypeService {

    @Resource IDeviceTypeDao deviceTypeDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return deviceTypeDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return deviceTypeDao.findPageInfo(filter, page);
	}

	public void delete(String ids) {
		deviceTypeDao.delete(ids);
	}

	

	public void batchUpdateDeviceTypeLevel(List<DeviceType> list){
		deviceTypeDao.batchUpdateDeviceTypeLevel(list);
	}


}

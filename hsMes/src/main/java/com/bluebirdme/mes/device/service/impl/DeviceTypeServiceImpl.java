package com.bluebirdme.mes.device.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.device.dao.IDeviceTypeDao;
import com.bluebirdme.mes.device.entity.DeviceType;
import com.bluebirdme.mes.device.service.IDeviceTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@AnyExceptionRollback
public class DeviceTypeServiceImpl extends BaseServiceImpl implements IDeviceTypeService {
    @Resource
    IDeviceTypeDao deviceTypeDao;

    @Override
    protected IBaseDao getBaseDao() {
        return deviceTypeDao;
    }


    public void delete(String ids) {
        deviceTypeDao.delete(ids);
    }

    public void batchUpdateDeviceTypeLevel(List<DeviceType> list) {
        deviceTypeDao.batchUpdateDeviceTypeLevel(list);
    }
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.device.dao.IDeviceDao;
import com.bluebirdme.mes.device.service.IDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-9-29 11:46:46
 */
@Service
@AnyExceptionRollback
public class DeviceServiceImpl extends BaseServiceImpl implements IDeviceService {
    @Resource
    IDeviceDao deviceDao;

    @Override
    protected IBaseDao getBaseDao() {
        return deviceDao;
    }

    public void delete(String ids) {
        deviceDao.delete(ids);
    }

    @Override
    public List<Map<String, Object>> find(String id) {
        return deviceDao.find(id);
    }

    @Override
    public Map<String, Object> getDeliveryDate(Filter filter, Page page) {
        return deviceDao.getDeliveryDate(filter, page);
    }

    public List<Map<String, Object>> getDeviceDepartment() throws SQLTemplateException {
        return deviceDao.getDeviceDepartment();
    }

    public void saveDeviceAndOrder(String dids, String wids) {
        deviceDao.saveDeviceAndOrder(dids, wids);
    }

    public void saveDeviceAndOrder2(String dids, String wids) {
        deviceDao.saveDeviceAndOrder2(dids, wids);
    }


    public void deleteDeviceAndOrder(String dids, String wids) {
        deviceDao.deleteDeviceAndOrder(dids, wids);
    }

    /**
     * 获取机台的计划
     *
     * @param dids
     * @return
     */
    public Map<String, Object> findDevicePlans(String dids) {
        return deviceDao.findDevicePlans(dids);
    }

    /**
     * 删除设备上的计划
     */
    public void deleteDevicePlans(String dids, String wids) {
        deviceDao.deleteDevicePlans(dids, wids);
    }

    public Map<String, Object> findAllDevicePlans(Filter filter, Page page) throws Exception {
        return deviceDao.findAllDevicePlans(filter, page);
    }

    public List<Map<String, Object>> findDevicePlans(Long did) {
        return deviceDao.findDevicePlans(did);
    }

    @Override
    public void setProducing(Long deviceId, Long weavePlanId) {
        deviceDao.setProducing(deviceId, weavePlanId);
    }

    @Override
    public List<Map<String, Object>> getFtcBcBomVersionDetail(Integer packVersionId) {
        return deviceDao.getFtcBcBomVersionDetail(packVersionId);
    }

    @Override
    public List<Map<String, Object>> getCutWorkShop() throws SQLTemplateException {
        return deviceDao.getCutWorkShop();
    }

    @Override
    public Map<String, Object> getBjDetails(String devcode, String yx, String partname) {
        return deviceDao.getBjDetails(devcode, yx, partname);
    }
}

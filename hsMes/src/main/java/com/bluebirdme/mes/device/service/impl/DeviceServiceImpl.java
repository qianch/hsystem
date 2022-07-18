/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.device.dao.IDeviceDao;
import com.bluebirdme.mes.device.service.IDeviceService;
import com.bluebirdme.mes.planner.weave.dao.IWeavePlanDao;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlanDevices;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;

/**
 * 
 * @author 宋黎明
 * @Date 2016-9-29 11:46:46
 */
@Service
@AnyExceptionRollback
public class DeviceServiceImpl extends BaseServiceImpl implements IDeviceService {
	
	@Resource IDeviceDao deviceDao;
	@Resource IWeavePlanService wpService;
	
	@Override
	protected IBaseDao getBaseDao() {
		return deviceDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return deviceDao.findPageInfo(filter, page);
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
	
	public List<Map<String,Object>> getDeviceDepartment() throws SQLTemplateException{
		return deviceDao.getDeviceDepartment();
	}
	
	public void saveDeviceAndOrder(String dids,String wids){
		deviceDao.saveDeviceAndOrder(dids, wids);
	}
	
	public void saveDeviceAndOrder2(String dids,String wids){
		deviceDao.saveDeviceAndOrder2(dids, wids);
	}
	
	
	public void deleteDeviceAndOrder(String dids,String wids){
		deviceDao.deleteDeviceAndOrder(dids, wids);
	}

	/**
	 * 获取机台的计划
	 * @param dids
	 * @return
	 */
	public Map<String,Object> findDevicePlans(String dids){
		return deviceDao.findDevicePlans(dids);
	}
	
	/**
	 * 删除设备上的计划
	 */
	public void deleteDevicePlans(String dids,String wids){
		deviceDao.deleteDevicePlans(dids, wids);
	}

	public Map<String, Object> findAllDevicePlans(Filter filter, Page page) throws Exception{
		return deviceDao.findAllDevicePlans(filter, page);
	}
	
	public List<Map<String,Object>> findDevicePlans(Long did){
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
	public Map<String, Object> getBjDetails(String devcode, String yx,
			String partname) {
		// TODO Auto-generated method stub
		return deviceDao.getBjDetails(devcode, yx, partname);
	}

	
}

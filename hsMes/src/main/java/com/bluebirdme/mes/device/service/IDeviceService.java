/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.device.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

/**
 * 
 * @author 宋黎明
 * @Date 2016-9-29 11:46:46
 */
public interface IDeviceService extends IBaseService {
	public void delete(String ids);
	public List<Map<String, Object>> find(String id);
	public Map<String, Object> getDeliveryDate(Filter filter, Page page) ;
	public List<Map<String,Object>> getDeviceDepartment() throws SQLTemplateException;
	public void saveDeviceAndOrder(String dids,String wids);
	public void saveDeviceAndOrder2(String dids,String wids);
	public void deleteDeviceAndOrder(String dids,String wids);
	public Map<String,Object> findDevicePlans(String dids);
	
	public Map<String,Object> getBjDetails(String devcode, String yx, String partname);
	
	
	public void deleteDevicePlans(String dids,String wids);
	public <T> Map<String, Object> findAllDevicePlans(Filter filter, Page page) throws Exception;
	public List<Map<String,Object>> findDevicePlans(Long did);
	public void setProducing(Long deviceId,Long weavePlanId);
	public List<Map<String,Object>> getFtcBcBomVersionDetail(Integer packVersionId);

	List<Map<String,Object>> getCutWorkShop() throws SQLTemplateException;
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.weave.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlanDevices;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;

/**
 * 
 * @author 肖文彬
 * @Date 2016-10-18 13:37:59
 */
public interface IWeavePlanService extends IBaseService {
	//更新编织计划
	public void updateWeave(Long wid,String date,String workshop,Long[] did,Integer[] dCount)throws Exception ;
	//保存编织计划
	public void save(WeavePlan weavePlan,Long[] did,Integer[] dCount,String time);
	//更新index
	public void updateIndex(Long id,Long index);
	//查询编织计划下的机台
	public List<Map<String, Object>> findDevice(Long wid,String date,String workshop,Long id);
	/**
	 * 自动保存编织计划
	 * @param oldList 
	 * @param newList 
	 */
	public void saveWeavePlan(ProducePlan producePaln);
	public List<Map<String, Object>> PlanCodeCombobox() throws Exception;
	/**
	 * 根据生产计划id查询编织计划
	 */
	public List<Map<String,Object>> findWeavePlan(String planCode)throws SQLTemplateException;
	
	public Map<String,Object> getBjDetails(String planCode,String yx, String partname)throws SQLTemplateException;
	
	
	public Map<String, Object> findWeavePageInfo(Filter filter,Page page);
	public Map<String, Object> findWeavePageInfo1(Filter filter,Page page);
	public Map<String, Object> findWeavePageInfo2(Filter filter,Page page);
	/**
	 * 根据明细id删除编织计划
	 * @param id
	 */
	public void deleteWeavePlan(String id);
	public List<HashMap<String,Object>> findWeavePlanByDeviceId(Long deviceId);
	//跟新编织计划完成状态
	public void updateState(String ids)throws Exception;
	//更新优先排序字段
	public void updateSort(String id,Long time);
	//加载计划单号
	public Map<String, Object> dataList(Filter filter, Page page) throws Exception;
	
	public List<Map<String,Object>> ledWeavePlan(Integer workshopNo);
	
	public void saveDevices(List<WeavePlanDevices> devices);
	
	public Map<String, Object> findNofinish(Filter filter,Page page);
	public Map<String, Object> findfinished(Filter filter,Page page);
	//修改总重量和总个数
	public void updateSumWeight(Long id,Double weight);
	//查询上一卷是否产出
	public List<Map<String, Object>> findRollIsPro(String batchcode,String factoryProductName) throws SQLTemplateException;
	public List<Map<String, Object>> findRollisNoA(String batchcode);
	public Map<String,Object> countRollsAndTrays(Long wid);
	public String getWeavePlanDevices(Long wid);
	public List<Map<String,Object>> getWeavePlanPackTask(Long wid);
	
	public Map<String,Object> findDevicePlans(String dids);
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlan;

/**
 * 
 * @author 宋黎明
 * @Date 2016-11-28 10:32:39
 */
public interface ICutDailyPlanDetailService extends IBaseService {

	/**
	 * 查询未完成的裁剪计划
	 * @param filter
	 * @param page
	 * @return
	 */
	public Map<String, Object> findNofinish(Filter filter,Page page);
	/**
	 * 保存裁剪日计划明细
	 * @param cutDailyPlan
	 * @param ids
	 */
	public void saveD(CutDailyPlan cutDailyPlan,Long cids[],Integer counts[],String partsNames[],String pCounts[],String partNames[],String comments[],String uids[],String partids[])  throws Exception;
	
	/**
	 * 查询日计划下的裁剪计划
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> findC(Long id);

	/**
	 * @param 保存编辑日计划明细
	 * @param ids
	 * @throws Exception
	 */
	public void deletePlans(String ids) throws Exception;
	
	/**
	 * 根据裁剪日计划明细id查询裁剪人员表 
	 * @param cutPlanDetailId
	 * @return
	 */
	public List<Map<String,Object>> findPlanUsers(Long cutPlanDetailId);
	/**
	 *  查询车间信息
	 * */
	public List<Map<String,Object>> findWorkShop(String cutPlanId);
}

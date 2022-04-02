/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;

/**
 * 
 * @author 宋黎明
 * @Date 2016-10-18 13:35:17
 */
public interface ICutPlanService extends IBaseService {
	/**
	 * 根据裁剪计划添加人员
	 * @param cutPlan
	 * @param userIds
	 */
	public void save(CutPlan cutPlan,Long[] userIds,String tcBomPartId);
	/**
	 * 更新裁剪计划
	 * @param cutPlan
	 * @param userIds
	 * @throws Exception
	 */
	public void updateUser(CutPlan cutPlan,Long[] userIds,String tcBomPartId) throws Exception;
	/**
	 * 根据id查找人员列表
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> userListById(String id) throws Exception;
	/**
	 * 添加裁剪计划
	 * @param producePlan
	 * @throws Exception
	 */
	public void add(ProducePlan producePlan) throws Exception;
	/**
	 * 加载生产计划单号combobox
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> planCodeCombobox() throws Exception;
	/**
	 * 加载计划单号列表
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> dataList(Filter filter, Page page) throws Exception;
	/**
	 * 查找裁剪计划
	 * @param producePlanId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findCutPlan(String planCode) throws Exception;
	/**
	 * 删除裁剪计划
	 */
	public void deleteCutPlans(String ids);
	
	/**
	 * 根据userId查询裁剪计划人员
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> findCutPlanUserByUserID(Long userId) throws Exception;
	
	/**
	 * 更新完成状态
	 * @param ids
	 * @throws Exception
	 */
	public void updateState(String ids) throws Exception;

    Map<String, Object> findCutPageInfo(Filter filter, Page page);
}

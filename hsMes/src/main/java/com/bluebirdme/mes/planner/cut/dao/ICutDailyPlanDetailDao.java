/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-11-28 10:32:39
 */

public interface ICutDailyPlanDetailDao extends IBaseDao {
    /**
     * 查询未完成的裁剪计划
     *
     * @param filter
     * @param page
     * @return
     */
	Map<String, Object> findNofinish(Filter filter, Page page);

    /**
     * 查询日计划下的裁剪计划
     *
     * @param ids
     * @return
     */
	List<Map<String, Object>> findC(List<Long> ids);

    /**
     * 查询裁剪计划人员关联表数据
     *
     * @param cutPlanDetailId
     * @return
     */
	List<Map<String, Object>> findPlanUsers(Long cutPlanDetailId);

    /**
     * 查询车间信息
     */
	List<Map<String, Object>> findWorkShop(String cutPlanId);
}

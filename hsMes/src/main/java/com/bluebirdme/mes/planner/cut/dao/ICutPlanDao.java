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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-10-18 13:35:17
 */

public interface ICutPlanDao extends IBaseDao {
    /**
     * 查找人员列表
     *
     * @param id
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> userListById(String id) throws Exception;

    /**
     * 删除裁剪计划
     */
    void deleteCutPlans(String ids);

    /**
     * 批量删除人员表信息
     *
     * @param ids
     */
    void deleteCutPlansUsers(String ids);

    /**
     * 加载生产订单号combobox
     *
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> planCodeCombobox() throws Exception;

    /**
     * 加载dataList 计划单号列表
     *
     * @param filter
     * @param page
     * @return
     * @throws Exception
     */
    Map<String, Object> dataList(Filter filter, Page page) throws Exception;

    /**
     * 查找裁剪计划
     *
     * @param planCode
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> findCutPlan(String planCode) throws Exception;

    /**
     * 编辑时删除人员表信息
     *
     * @param tcBomPartId
     */
    void deleteUserByPartId(Long tcBomPartId);

    /**
     * 根据userId查询裁剪计划人员
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<HashMap<String, Object>> findCutPlanUserByUserID(Long userId) throws Exception;

    Map<String, Object> findCutPageInfo(Filter filter, Page page);

    Map<String, Object> countRollsAndTrays(Long id);
}

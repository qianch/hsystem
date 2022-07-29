/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.weave.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 肖文彬
 * @Date 2016-10-18 13:37:59
 */

public interface IWeavePlanDao extends IBaseDao {
    /**
     * 删除编制计划和机台表的机台
     */
    void deleteDevice(Long wid, String date, String workshop);

    void updateIndex(Long id, Long index);

    /**
     * 查询编制计划下的机台信息
     */
    List<Map<String, Object>> findDevice(Long wid, String date, String workshop, Long id);

    /**
     * 删除编织计划
     */
    void deleteWeavePlan(String id);

    List<Map<String, Object>> PlanCodeCombobox() throws Exception;

    /**
     * 根据生产计划id查询编织计划
     */
    List<Map<String, Object>> findWeavePlan(String planCode) throws SQLTemplateException;

    /**
     * 根据明细id删除机台
     */
    void deleteDevices(String id);

    List<HashMap<String, Object>> findWeavePlanByDeviceId(Long deviceId);

    //更新优先排序字段
    void updateSort(String id, Long time);

    //加载计划单号
    Map<String, Object> dataList(Filter filter, Page page) throws Exception;

    /**
     * LED编制任务
     *
     * @return
     */
    List<Map<String, Object>> ledWeavePlan(Integer workshopNo);

    Map<String, Object> findWeavePageInfo(Filter filter, Page page);

    Map<String, Object> findWeavePageInfo1(Filter filter, Page page);

    Map<String, Object> findWeavePageInfo2(Filter filter, Page page);

    Map<String, Object> findNofinish(Filter filter, Page page);

    Map<String, Object> findfinished(Filter filter, Page page);

    //修改总重量和总已经称重的个数
    void updateSumWeight(Long id, Double weight);

    //查询上一卷是否称重
    List<Map<String, Object>> findRollIsPro(String batchcode, String factoryProductName) throws SQLTemplateException;

    //查询作废和非A的个数
    List<Map<String, Object>> findRollisNoA(String batchcode);

    Map<String, Object> countRollsAndTrays(Long wid);

    String getWeavePlanDevices(Long wid);

    List<Map<String, Object>> getWeavePlanPackTask(Long wid);


    Map<String, Object> getBjDetails(String planCode, String yx, String partname) throws SQLTemplateException;

    Map<String, Object> findDevicePlans(String dids);
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.produce.entity.FinishedProduct;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016-11-28 21:25:48
 */
public interface IProducePlanService extends IBaseService {
    Map<String, Object> findOrderPageInfo(Filter filter, Page page) throws Exception;

    Map<String, Object> listOrders(Filter filter, Page page) throws Exception;

    void savePlan(ProducePlan plan);

    void updatePlan(ProducePlan plan) throws Exception;

    void createCutAndWeavePlans(ProducePlan plan) throws Exception;

    String getSerial(String workShopCode) throws Exception;

    Map<String, Object> dataList(Filter filter, Page page) throws Exception;

    void deletePlan(String ids);

    void setIsSettled(Long weaveDailyId, Long cutDailyId);

    void updateProductInfo(Long producePlanId);

    void forceEdit(ProducePlan plan, Long userId) throws Exception;

    String getSdeviceCode(Long id);//得到机台实际机台编码

    /**
     * 检查批次号 是否存在
     */
    List<Map<String, Object>> checkBatchCode(String batchCode, Long producePlanId);

    Double getFbslByDdh(String ddh) throws Exception;

    Double getFbslByDdhs(Map<String, Object> planId) throws Exception;

    List<Map<String, Object>> details(Long planId);

    List<Map<String, Object>> detailsMirror(Long planId);

    Map<String, Object> findOrderPageInfo2(Filter filter, Page page) throws Exception;

    //获取未分配的订单
    List<Map<String, Object>> searchbox(String searchbox);

    Map<String, Object> findfinished(Filter filter, Page page);

    void noClose(String ids);

    Map<String, Object> findSchedule(Filter filter, Page page) throws Exception;

    Map<String, Object> findScheduleWeight(Filter filter, Page page) throws Exception;

    String hasPackTask(Long id);

    Map<String, Object> findTBPageInfo(Filter filter, Page page) throws Exception;

    Map<String, Object> findTBPageInfo2(Filter filter, Page page) throws Exception;

    List<Map<String, Object>> findProductListByMap(FinishedProduct finishProduct);
}

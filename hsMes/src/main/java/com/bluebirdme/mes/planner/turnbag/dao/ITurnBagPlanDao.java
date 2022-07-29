/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.turnbag.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2017-2-9 11:28:32
 */

public interface ITurnBagPlanDao extends IBaseDao {
    Map<String, Object> findOrderPageInfo(Filter filter, Page page) throws Exception;

    List<Map<String, Object>> getBatchCodeCountBySalesOrderCode(Long orderId, Long partId);

    String getSerial();

    List<Map<String, Object>> getDetails(Long turnbagPlanId);

    List<Map<String, Object>> getGoodsPosition(List<Long> ids, List<String> batchCodes);

    List<Map<String, Object>> getPackChildren(String code);

    String trayDeviceCode(String trayCode);

    Map<String, Object> getBatchInfo(Long targetProducePlanDetailId, Long fromProducePlanDetailId);

    /**
     * 查询翻包领出记录
     */
    Map<String, Object> findTurnBagOutPageInfo(Filter filter, Page page) throws Exception;
}

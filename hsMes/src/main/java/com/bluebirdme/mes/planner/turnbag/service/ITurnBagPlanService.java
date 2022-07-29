/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.turnbag.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagOutRecord;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlan;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlanDetails;

import java.util.List;
import java.util.Map;

/**
 * 翻包Service
 *
 * @author 高飞
 * @Date 2017-2-9 11:28:32
 */
public interface ITurnBagPlanService extends IBaseService {
    <T> Map<String, Object> findOrderPageInfo(Filter filter, Page page) throws Exception;

    List<Map<String, Object>> getBatchCodeCountBySalesOrderCode(Long orderId, Long partId);

    String getSerial();

    void addOrEdit(List<TurnBagPlanDetails> list);

    void delete(List<TurnBagPlan> list);

    List<Map<String, Object>> getDetails(Long turnbagPlanId);

    void saveOutRecord(List<TurnBagOutRecord> list) throws Exception;

    void doTurnBag(String newTrayCode, String oldTrayCode, String optUser);

    List<Map<String, Object>> getGoodsPosition(List<Long> ids, List<String> batchCodes);

    List<Map<String, Object>> getPackChildren(String code);

    String trayDeviceCode(String trayCode);

    Map<String, Object> getBatchInfo(Long targetProducePlanDetailId, Long fromProducePlanDetailId);

    /**
     * 查询翻包领出记录
     *
     * @param filter
     * @param page
     * @return
     * @throws Exception
     */
    Map<String, Object> findTurnBagOutPageInfo(Filter filter, Page page) throws Exception;

    void doCutTurnBag(String newTrayCode, String oldTrayCode, String optUser);
}

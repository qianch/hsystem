/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.deliveryontheway.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */

public interface IDeliveryOnTheWayPlanDao extends IBaseDao {

    /**
     * 根据计划id查询计划明细
     *
     * @param deliveryId
     * @return
     */
    List<Map<String, Object>> findDeliveryOnTheWayPlanDetails(Long deliveryId);

    List<Map<String, Object>> findProductDeliveryOnTheWayPlanDetailsByDeliveryId(Long deliveryId);

    String getSerial(String type);

    Map<String, Object> findPageInfoTotalWeight(Filter filter, Page page) throws Exception;

    Map<String, Object> findDeliveryOnTheWayPlanDetails(Filter filter, Page page) throws Exception;
}

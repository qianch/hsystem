/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016-11-28 21:52:43
 */

public interface IProducePlanDetailDao extends IBaseDao {

    Map<String, Object> findProducePlanDetail(Filter filter, Page page) throws Exception;

    List<Map<String, Object>> findProducePlanDetailPrints(Long ProducePlanDetailId) throws Exception;


    List<Map<String, Object>> findPlanDetailPrintsBybtwFileId(Long ProducePlanDetailId, long btwFileId) throws Exception;
}

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

import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */
public interface IProducePlanDetailService extends IBaseService {
    /**
     * 根据条件查询计划明细
     *
     * @param filter
     * @param page
     * @return
     */

    public <T> Map<String, Object> findProducePlanDetail(Filter filter, Page page) throws Exception;

    /**
     * 根据计划明细Id和标签模板Id查询对应的打印内容
     *
     * @param ProducePlanDetailId
     * @return
     */
    public List<Map<String, Object>> findProducePlanDetailPrints(Long ProducePlanDetailId) throws Exception;

    public List<Map<String, Object>> findPlanDetailPrintsBybtwFileId(Long ProducePlanDetailId, long btwFileId) throws Exception;


    public String saveProducePlanDetailPrints(long producePlanDetailId, long btwFileId, String planDetailPrintsData) throws Exception;

    public String createProducePlanDetailPrints(Long producePlanDetailId, Long btwFileId) throws Exception;
}

package com.bluebirdme.mes.planner.material.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;

import java.util.Map;

/**
 * 物料需求计划
 *
 * @author Goofy
 * @Date 2016年10月20日 上午10:19:44
 */
public interface IMrpService extends IBaseService {
    Map<String, Object> findRequirementPlans(Long[] ids);

    /**
     * 创建物料需求计划，包括TC，FTC，BC
     *
     * @param pp 生产计划ID
     * @throws Exception
     */
    void createRequirementPlans(ProducePlan pp) throws Exception;

    /**
     * 根据生产计划，删除对应的物料需求计划
     */
    void deleteMrpByProduce(ProducePlan producePlan) throws SQLTemplateException;

    Map<String, Object> findFarbic(Long producePlanId);
}

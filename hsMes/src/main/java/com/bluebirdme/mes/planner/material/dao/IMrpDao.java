package com.bluebirdme.mes.planner.material.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;

import java.util.Map;

/**
 * @author Goofy
 * @Date 2016年10月20日 下午4:53:35
 */
public interface IMrpDao extends IBaseDao {
    Map<String, Object> findRequirementPlans(Long[] ids);

    void deleteMrpByProduce(ProducePlan producePlan) throws SQLTemplateException;

    Map<String, Object> findFarbic(Long producePlanId);

    Double sumMaterialTotalWeightPerSquar(Long id);

    Double sumMaterialTotalWeightPerSquarMirror(Long id);
}

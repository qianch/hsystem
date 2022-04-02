package com.bluebirdme.mes.planner.material.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;

/**
 * 
 * @author Goofy
 * @Date 2016年10月20日 下午4:53:35
 */
public interface IMrpDao extends IBaseDao {
	public Map<String, Object> findRequirementPlans(Long ids[]);
	public void deleteMrpByProduce(ProducePlan producePlan) throws SQLTemplateException;
	public Map<String,Object> findFarbic(Long producePlanId);
	public Double sumMaterialTotalWeightPerSquar(Long id);
	public Double sumMaterialTotalWeightPerSquarMirror(Long id);
}

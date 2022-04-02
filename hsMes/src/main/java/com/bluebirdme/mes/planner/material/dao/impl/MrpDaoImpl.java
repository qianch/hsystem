package com.bluebirdme.mes.planner.material.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.material.dao.IMrpDao;
import com.bluebirdme.mes.planner.material.entity.BcMaterialRequirementPlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;

/**
 * 物料需求计划
 * 
 * @author Goofy
 * @Date 2016年10月20日 下午4:54:41
 */
@Repository
public class MrpDaoImpl extends BaseDaoImpl implements IMrpDao {

	@Resource
	SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}

	@Override
	public Map<String, Object> findRequirementPlans(Long ids[]) {
		
		String bcSql= "	SELECT" +
					"		*" +
					"	FROM" +
					"		(" +
					"			(" +
					"				SELECT" +
					"					mtCode,stCode," +
					"					sum(packMateriaTotalCount) packMateriaTotalCount," +
					"					packMaterialAttr," +
					"					packMaterialModel," +
					"					packMaterialName," +
					"					packMaterialUnit," +
					"					packUnit" +
					"				FROM" +
					"					hs_bc_material_requirement_plan " +
					"				WHERE" +
					"					producePlanId in (:ids) " +
					"				AND mtCode IS NOT NULL" +
					"				GROUP BY" +
					"					mtCode" +
					"			)" +
					"			UNION ALL" +
					"				(" +
					"					SELECT" +
					"						mtCode,stCode," +
					"						packMateriaTotalCount," +
					"						packMaterialAttr," +
					"						packMaterialModel," +
					"						packMaterialName," +
					"						packMaterialUnit," +
					"						packUnit" +
					"					FROM" +
					"						hs_bc_material_requirement_plan" +
					"					WHERE" +
					"						producePlanId in (:ids) " +
					"					AND mtCode IS NULL" +
					"				)" +
					"		) x" +
					" 	where packMaterialName is not null" +
					"	ORDER BY" +
					"		packMaterialName DESC";
		
		String ylSql=	"	SELECT" +
						"		materialModel," +
						"		materialName," +
						"		ROUND(sum(materialTotalWeight), 2) materialTotalWeight," +
						"		(SELECT 'kg') materialUnit" +
						"	FROM" +
						"		hs_ftc_material_requirement_plan" +
						"	WHERE" +
						"		producePlanId in (:ids)" +
						"	GROUP BY" +
						"		materialModel ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("baocai", getSession().createSQLQuery(bcSql).setParameterList("ids", ids).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		map.put("yuanliao", getSession().createSQLQuery(ylSql).setParameterList("ids", ids).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list());
		return map;
	}

	public void deleteMrpByProduce(ProducePlan producePlan) throws SQLTemplateException{
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("pid", producePlan.getId());
		getSession().createSQLQuery(SQL.get(map, "mrp-delete-tc")).executeUpdate();
		getSession().createSQLQuery(SQL.get(map, "mrp-delete-ftc")).executeUpdate();
		getSession().createSQLQuery(SQL.get(map, "mrp-delete-bc")).executeUpdate();
	}
	
	public Map<String,Object> findFarbic(Long producePlanId){
		
		String sql="SELECT " +
				"	sum(requirementCount) total, productId " +
				"FROM " +
				"	hs_produce_plan_detail " +
				"WHERE " +
				"	producePlanId =  " +producePlanId+
				"GROUP BY " +
				"	productId " +
				"UNION ALL " +
				"	SELECT " +
				"		sum(c.partCount) total, " +
				"		d.tcFinishedProductId " +
				"	FROM " +
				"		hs_produce_plan_detail_part_count c " +
				"	LEFT JOIN hs_tc_proc_bom_version_parts_detail d ON d.tcProcBomPartsId = c.partId " +
				"	WHERE " +
				"		c.planDetailId IN ( " +
				"			SELECT " +
				"				id " +
				"			FROM " +
				"				hs_produce_plan_detail " +
				"			WHERE " +
				"				producePlanId =  " +producePlanId+
				" ) " +
				"	GROUP BY " +
				"		d.tcFinishedProductId";
		
		return (Map<String, Object>) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	public Double sumMaterialTotalWeightPerSquar(Long id){
		String sql="SELECT sum(d.ftcBomDetailWeightPerSquareMetre) from hs_ftc_proc_bom_detail d where d.ftcBomVersionId="+id;
		return (Double) getSession().createSQLQuery(sql).uniqueResult();
	}
@Override
	public Double sumMaterialTotalWeightPerSquarMirror(Long id){
		String sql="SELECT sum(d.ftcBomDetailWeightPerSquareMetre) from hs_ftc_proc_bom_detail_mirror d where d.ftcBomVersionId="+id;
		return (Double) getSession().createSQLQuery(sql).uniqueResult();
	}
	
}

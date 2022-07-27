/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.planner.produce.dao.IProducePlanDao;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class ProducePlanDaoImpl extends BaseDaoImpl implements IProducePlanDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "producePlan-list");
    }

    public Map<String, Object> findOrderPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "produce-order");
    }

    public Map<String, Object> findOrderPageInfo2(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "produce-order2");
    }

    public String getSerial(String workShopCode) throws Exception {
        String sql = SQL.get("producePlan-serial");
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String code = "JD-" + sdf.format(now).substring(0, 6);
        ProducePlan pp = (ProducePlan) getSession().createQuery(sql).setParameter("workShopCode", workShopCode).setParameter("code", "%" + code + "%").setMaxResults(1).uniqueResult();
        if (pp == null) {
            code = "JD-" + sdf.format(now) + "-" + getWorkshopCode(workShopCode) + "-001";
        } else {
            int s = Integer.parseInt(pp.getProducePlanCode().substring(pp.getProducePlanCode().lastIndexOf("-") + 1)) + 1;
            if (Integer.toString(s).length() == 1) {
                code = "JD-" + sdf.format(now) + "-" + getWorkshopCode(workShopCode) + "-00" + s;
            }
            if (Integer.toString(s).length() == 2) {
                code = "JD-" + sdf.format(now) + "-" + getWorkshopCode(workShopCode) + "-0" + s;
            }
            if (Integer.toString(s).length() == 3) {
                code = "JD-" + sdf.format(now) + "-" + getWorkshopCode(workShopCode) + "-" + s;
            }
        }
        return code;
    }

    public String getWorkshopCode(String workShopCode) throws Exception {
        Filter filter = new Filter();
        filter.set("code", workShopCode);
        Page page = new Page();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) this.findPageInfo(filter, page, "dept-list").get("rows");
        return (String) rows.get(0).get("PREFIX");
    }

    @Override
    public Map<String, Object> dataList(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "producePlan-datalist");
    }

    public void setIsSettled(Long weaveDailyId, Long cutDailyId) {
        if (weaveDailyId != null) {
            getSession().createSQLQuery(SQL.get("update-settled-weave")).setParameter("id", weaveDailyId).executeUpdate();
        }
        if (cutDailyId != null) {
            getSession().createSQLQuery(SQL.get("update-settled-cut")).setParameter("id", cutDailyId).executeUpdate();
        }
    }

    public List<Map<String, Object>> checkBatchCode(String batchCode, Long producePlanId) {
        SQLQuery query = getSession().createSQLQuery(SQL.get("producePlan-checkBatchCode"));
        query.setParameter("batchCode", batchCode);
        query.setParameter("producePlanId", producePlanId);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<Map<String, Object>> details(Long planId) {
        String sql = SQL.get("producePlan-details");
        return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("id", planId).list();
    }

    public List<Map<String, Object>> detailsMirror(Long planId) {
        String sql = SQL.get("producePlan-details-mirror");
        return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("id", planId).list();
    }

    @Override
    public List<Map<String, Object>> searchbox(String searchbox) {
        if (searchbox == "") {
            SQLQuery query = getSession().createSQLQuery(SQL.get("produce-productOrder1"));
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        } else {
            SQLQuery query = getSession().createSQLQuery(SQL.get("produce-productOrder"));
            query.setParameter("searchbox", searchbox);
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.list();
        }
    }

    @Override
    public Map<String, Object> findfinished(Filter filter, Page page) {
        Map<String, Object> findPageInfo = this.findPageInfo(filter, page, "produce-order2");
        return findPageInfo;
    }

    @Override
    public Map<String, Object> listOrders(Filter filter, Page page) {
        return findPageInfo(filter, page, "delivery-order");
    }

    @Override
    public <T> Map<String, Object> findSchedule(Filter filter, Page page) {
        return findPageInfo(filter, page, "producePlan-schedule");
    }

    @Override
    public <T> Map<String, Object> findScheduleWeight(Filter filter, Page page) {
        return findPageInfo(filter, page, "producePlan-schedule-weight");
    }

    @Override
    public String getSdeviceCode(Long id) {
        SQLQuery query = getSession().createSQLQuery(SQL.get("sdevicecode"));
        query.setParameter("id", id);
        return query.list().toString();
    }

    public String hasPackTask(Long id) {

        String sql = " SELECT " +
                " GROUP_CONCAT(factoryProductName) " +
                "FROM" +
                "	hs_produce_plan_detail " +
                "WHERE" +
                "	id IN (" +
                "		SELECT" +
                "			x.id" +
                "		FROM" +
                "			(" +
                "				SELECT" +
                "					id" +
                "				FROM" +
                "					hs_produce_plan_detail _ppd" +
                "				WHERE" +
                "					_ppd.producePlanId = " + id +
                "				and _ppd.productIsTc=2" +
                "			) x" +
                "		LEFT JOIN (" +
                "			SELECT" +
                "				id," +
                "				ppdId" +
                "			FROM" +
                "				hs_pack_task pt" +
                "			WHERE" +
                "				pt.ppdId IN (" +
                "					SELECT" +
                "					id" +
                "				FROM" +
                "					hs_produce_plan_detail _ppd" +
                "				WHERE" +
                "					_ppd.producePlanId = " + id +
                "	and _ppd.productIsTc=2" +
                "				)" +
                "		) y ON x.id = y.ppdId" +
                "		WHERE" +
                "			y.ppdId IS NULL" +
                "		GROUP BY" +
                "			x.id" +
                "	)";
        Object v = getSession().createSQLQuery(sql).uniqueResult();
        return v == null ? "" : v.toString();
    }

    public Map<String, Object> findTBPageInfo(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "turnBagPlanDetail-list");
    }

    public Map<String, Object> findTBPageInfo2(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "turnBagPlanDetail-list2");
    }

    @Override
    public Double getFbslByDdh(String ddh) {
        // TODO Auto-generated method stub
        String sql = "select requirementCount from HS_Produce_Plan_Detail where isTurnBagPlan = '翻包' and salesOrderCode='" + ddh + "'";
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() == 0) {
            return 0.00;
        } else {
            double sl = 0;
            for (int i = 0; i < list.size(); i++) {
                sl = sl + Double.parseDouble((String) list.get(0).get("REQUIREMENTCOUNT").toString());
            }
            return sl;
        }
    }

    @Override
    public Double getFbslByDdhs(Map<String, Object> ddhs) {
        // TODO Auto-generated method stub
        Object SALESORDERSUBCODEPRINT = null == ddhs.get("SALESORDERSUBCODEPRINT") ? "" : ddhs.get("SALESORDERSUBCODEPRINT");
        Object SALESORDERCODE = null == ddhs.get("SALESORDERCODE") ? "" : ddhs.get("SALESORDERCODE");
        Object ORDERTOTALMETRES = null == ddhs.get("ORDERTOTALMETRES") ? "" : ddhs.get("ORDERTOTALMETRES");
        Object DRAWNO = null == ddhs.get("DRAWNO") ? "" : ddhs.get("DRAWNO");
        Object PRODUCTLENGTH = null == ddhs.get("PRODUCTLENGTH") ? "" : ddhs.get("PRODUCTLENGTH");
        String LEVELNO = null == ddhs.get("LEVELNO") ? "" : (String) ddhs.get("LEVELNO");
        Object CONSUMERPRODUCTNAME = null == ddhs.get("CONSUMERPRODUCTNAME") ? "" : ddhs.get("CONSUMERPRODUCTNAME");
        Object FACTORYPRODUCTNAME = null == ddhs.get("FACTORYPRODUCTNAME") ? "" : ddhs.get("FACTORYPRODUCTNAME");
        Object PRODUCTMODEL = null == ddhs.get("PRODUCTMODEL") ? "" : ddhs.get("PRODUCTMODEL");
        Object PRODUCTWIDTH = null == ddhs.get("PRODUCTWIDTH") ? "" : ddhs.get("PRODUCTWIDTH");
        Object PRODUCTWEIGHT = null == ddhs.get("PRODUCTWEIGHT") ? "" : ddhs.get("PRODUCTWEIGHT");
        Object PROCESSBOMCODE = null == ddhs.get("PROCESSBOMCODE") ? "" : ddhs.get("PROCESSBOMCODE");
        Object PROCESSBOMVERSION = null == ddhs.get("PROCESSBOMVERSION") ? "" : ddhs.get("PROCESSBOMVERSION");
        Object BCBOMCODE = null == ddhs.get("BCBOMCODE") ? "" : ddhs.get("BCBOMCODE");
        Object BCBOMVERSION = null == ddhs.get("BCBOMVERSION") ? "" : ddhs.get("BCBOMVERSION");
        StringBuilder sql = new StringBuilder("select requirementCount from HS_Produce_Plan_Detail where isTurnBagPlan = '翻包' ");
        if (SALESORDERSUBCODEPRINT != "") {
            sql.append("and salesOrderCode='" + SALESORDERSUBCODEPRINT + "'");
        }
        if (SALESORDERCODE != "") {
            sql.append("and SALESORDERCODE='" + SALESORDERCODE + "'");
        }
        if (ORDERTOTALMETRES != "") {
            sql.append("and ORDERTOTALMETRES='" + ORDERTOTALMETRES + "'");
        }
        if (DRAWNO != "") {
            sql.append("and DRAWNO='" + DRAWNO + "'");
        }
        if (PRODUCTLENGTH != "") {
            sql.append("and PRODUCTLENGTH='" + PRODUCTLENGTH + "'");
        }
        if (LEVELNO != "") {
            sql.append("and LEVELNO='" + LEVELNO + "'");
        }
        if (CONSUMERPRODUCTNAME != "") {
            sql.append("and CONSUMERPRODUCTNAME='" + CONSUMERPRODUCTNAME + "'");
        }
        if (FACTORYPRODUCTNAME != "") {
            sql.append("and FACTORYPRODUCTNAME='" + FACTORYPRODUCTNAME + "'");
        }
        if (PRODUCTMODEL != "") {
            sql.append("and PRODUCTMODEL='" + PRODUCTMODEL + "'");
        }
        if (PRODUCTWIDTH != "") {
            sql.append("and PRODUCTWIDTH='" + PRODUCTWIDTH + "'");
        }
        if (PRODUCTWEIGHT != "") {
            sql.append("and PRODUCTWEIGHT='" + PRODUCTWEIGHT + "'");
        }
        if (PROCESSBOMCODE != "") {
            sql.append("and PROCESSBOMCODE='" + PROCESSBOMCODE + "'");
        }
        if (PROCESSBOMVERSION != "") {
            sql.append("and PROCESSBOMVERSION='" + PROCESSBOMVERSION + "'");
        }
        if (BCBOMCODE != "") {
            sql.append("and BCBOMCODE='" + BCBOMCODE + "'");
        }
        if (BCBOMVERSION != "") {
            sql.append("and BCBOMVERSION='" + BCBOMVERSION + "'");
        }
        String sqlString = sql + "";
        List<Map<String, Object>> list = getSession().createSQLQuery(sqlString).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list.size() == 0) {
            return 0.00;
        } else {
            double sl = 0;
            for (int i = 0; i < list.size(); i++) {
                sl = sl + Double.parseDouble((String) list.get(i).get("REQUIREMENTCOUNT").toString());
            }
            return sl;
        }
    }


    @Override
    public List<Map<String, Object>> findProductListByMap(Long id) {
        String sql = SQL.get("produce-order3");
        return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("PRODUCTID", id).list();
    }

    @Override
    public List<Map<String, Object>> selectByFormId(Long formId) {
        String sql = SQL.get("selectPlanDetailByPlanId");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("producePlanId", formId);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<ProducePlanDetail> findListGroupByMap(String batchcode, String productmodel) {
        String sql = SQL.get("findListGroupBy-map");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("batchCode", batchcode);
        query.setParameter("productModel", productmodel);
        return query.addEntity(ProducePlanDetail.class).list();
    }
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.turnbag.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.turnbag.dao.ITurnBagPlanDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class TurnBagPlanDaoImpl extends BaseDaoImpl implements ITurnBagPlanDao {
    private static final Logger logger = LoggerFactory.getLogger(TurnBagPlanDaoImpl.class);
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "turnBagPlan-list");
    }

    public Map<String, Object> findOrderPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "turnbag-order");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBatchCodeCountBySalesOrderCode(Long orderId, Long partId) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (partId != null)
            map.put("partId", partId);
        String sql = null;
        try {
            sql = SQL.get(map, "turnbag-batchcode-count");
        } catch (SQLTemplateException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (partId != null)
            return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("orderId", orderId).setParameter("partId", partId).list();
        else
            return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("orderId", orderId).list();
    }

    public String getSerial() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String preffix = "FB" + sdf.format(now);

        ProducePlanDetail ppd = (ProducePlanDetail) getSession().createQuery(SQL.get("turnBagPlan-serial")).setParameter("code", "%" + preffix + "%").setMaxResults(1).uniqueResult();
        String suffix = "0000";
        if (ppd == null) {
            suffix += 1;
        } else {
            String s = ppd.getTurnBagCode().substring(10);
            //s=s.split("-")[0];
            suffix += (Integer.parseInt(s) + 1);
        }
        return preffix + suffix.substring(suffix.length() - 2);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDetails(Long producePlanDetailId) {
        String sql = SQL.get("turnbag-details");
        return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("id", producePlanDetailId).list();
    }

    @Override
    public List<Map<String, Object>> getGoodsPosition(List<Long> ids, List<String> batchCodes) {
        String sql;
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < ids.size(); i++) {
            sql = SQL.get("turnbag-position");
            ret.addAll(getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("orderId", ids.get(i)).setParameter("batchCode", batchCodes.get(i)).list());
        }
        return ret;
    }

    @Override
    public List<Map<String, Object>> getPackChildren(String code) {
        String sql = SQL.get("children-plan");
        return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("code", code).list();
    }

    public String trayDeviceCode(String trayCode) {
        String sql = SQL.get("tray-device");
        Map<String, Object> map = (Map<String, Object>) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("code", trayCode).uniqueResult();
        return map.get("codes") == null ? "" : map.get("codes").toString();
    }

    public Map<String, Object> getBatchInfo(Long targetProducePlanDetailId, Long fromProducePlanDetailId) {
        String sql = SQL.get("turnbag-batch-info");
        return (Map<String, Object>) getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("fromProducePlanDetailId", fromProducePlanDetailId).setParameter("targetProducePlanDetailId", targetProducePlanDetailId).uniqueResult();
    }

    /**
     * 翻包领出查询
     */
    @Override
    public Map<String, Object> findTurnBagOutPageInfo(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "turnBagOut-list");
    }
}

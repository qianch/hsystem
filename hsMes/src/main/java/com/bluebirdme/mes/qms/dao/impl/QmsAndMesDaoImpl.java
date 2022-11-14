package com.bluebirdme.mes.qms.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.qms.dao.QmsAndMesDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QmsAndMesDaoImpl extends BaseDaoImpl implements QmsAndMesDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    @Override
    public List<Map<String, Object>> findRollandPartByBatchCode(String batchCode, String deliveryCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("batchCode", batchCode);
        map.put("deliveryCode", deliveryCode);
        String sql = SQL.get(map, "find-RollandPart");
        SQLQuery query = getSession().createSQLQuery(sql);
        if (batchCode != null) {
            query.setParameter("batchCode", batchCode);
        }
        if (deliveryCode != null) {
            query.setParameter("deliveryCode", deliveryCode);
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> selectBomByCode(String salesOrderId, String procBomCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("salesOrderId", salesOrderId);
        map.put("procBomCode", procBomCode);
        String sql = SQL.get(map, "select-procBomInfo");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("salesOrderId", salesOrderId);
        query.setParameter("procBomCode", procBomCode);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (query.list().size() == 0) {
            map.clear();
            map.put("procBomCode", procBomCode);
            sql = SQL.get(map, "select-procBomInfo1");
            query = getSession().createSQLQuery(sql);
            query.setParameter("procBomCode", procBomCode);
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }
        return query.list();
    }

    @Override
    public List<Map<String, Object>> selectOUTOrder(String deliveryCode, String batchCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("batchCode", batchCode);
        map.put("deliveryCode", deliveryCode);
        String sql = SQL.get(map, "select-outOrder");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("batchCode", batchCode);
        query.setParameter("deliveryCode", deliveryCode);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> selectOUTOrderByDeliveryPlan(String deliveryCode, String batchCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("batchCode", batchCode);
        map.put("deliveryCode", deliveryCode);
        String sql = SQL.get(map, "select-outOrder-ByDeliveryPlan");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("batchCode", batchCode);
        query.setParameter("deliveryCode", deliveryCode);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> selectOUTOrderOfMirror(String deliveryCode, String batchCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("batchCode", batchCode);
        map.put("deliveryCode", deliveryCode);
        String sql = SQL.get(map, "select-outOrderMirror");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("batchCode", batchCode);
        query.setParameter("deliveryCode", deliveryCode);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> selectOUTOrderByDeliveryPlanOfMirror(String deliveryCode, String batchCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("batchCode", batchCode);
        map.put("deliveryCode", deliveryCode);
        String sql = SQL.get(map, "select-outOrder-ByDeliveryPlanMirror");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("batchCode", batchCode);
        query.setParameter("deliveryCode", deliveryCode);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }
}

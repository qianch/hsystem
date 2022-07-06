package com.bluebirdme.mes.baseInfo.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.baseInfo.dao.IBomDao;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;

/**
 * BOM统一的管理DAO
 *
 * @author Goofy
 * @Date 2016年10月19日 下午2:38:21
 */
@Repository
public class BomDaoImpl extends BaseDaoImpl implements IBomDao {
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

    public <T> List<T> getBomDetails(Class<T> clazz, String bomCode, String bomVersionCode) {
        String sql;
        SQLQuery query;
        switch (clazz.getSimpleName()) {
            case "BcBomVersionDetail":
                sql = SQL.get("bc-bom-details");
                query = getSession().createSQLQuery(sql);
                query.setParameter("version", bomVersionCode);
                query.setParameter("code", bomCode);
                query.addEntity(clazz);
                return query.list();
            case "TcBomVersionPartsDetail":
                sql = SQL.get("tc-bom-details");
                query = getSession().createSQLQuery(sql);
                query.setParameter("version", bomVersionCode);
                query.setParameter("code", bomCode);
                query.addEntity(clazz);
                return query.list();
            case "FtcBomDetail":
                sql = SQL.get("ftc-bom-details");
                query = getSession().createSQLQuery(sql);
                query.setParameter("version", bomVersionCode);
                query.setParameter("code", bomCode);
                query.addEntity(clazz);
                return query.list();
            default:
                break;
        }
        return new ArrayList<T>();
    }

    @Override
    public List<Map<String, Object>> findSalesOrderDetail(Long id, String c) throws Exception {
        String sql = SQL.get("salesOrderD-list2");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("procBomId", id).setParameterList("productIsTc", c.split(",")).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findSalesOrderDetail1(Long id) throws Exception {
        String sql = SQL.get("salesOrderD-list3");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("packBomId", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }
}

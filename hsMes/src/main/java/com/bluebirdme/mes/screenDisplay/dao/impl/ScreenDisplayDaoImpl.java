/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.screenDisplay.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.screenDisplay.dao.IScreenDisplayDao;
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
 * @author 宋黎明
 * @Date 2016年11月25日 下午4:35:34
 */
@Repository
public class ScreenDisplayDaoImpl extends BaseDaoImpl implements IScreenDisplayDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "");
    }

    @Override
    public List<Map<String, Object>> findProductInfo(String ip) throws Exception {
        String sql = SQL.get("findProductInfo-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("ip", ip);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findFirstProductInfo(String ip) {
        String sql = SQL.get("findFirstProductInfo-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("ip", ip);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        query.setParameter("today", sdf.format(date));

        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findProduceNum(Long productId, Long deviceId) {
        String sql = SQL.get("findProduceNum");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("productId", productId);
        query.setParameter("deviceId", deviceId);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> initCombox() {
        String sql = SQL.get("initCombox-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }
}

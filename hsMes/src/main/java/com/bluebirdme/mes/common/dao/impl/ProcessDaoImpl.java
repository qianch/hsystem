package com.bluebirdme.mes.common.dao.impl;

import com.bluebirdme.mes.common.dao.IProcessDao;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 进度更新
 *
 * @author Goofy
 * @Date 2016年12月2日 下午9:11:11
 */
@Repository
public class ProcessDaoImpl extends BaseDaoImpl implements IProcessDao {
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
    public List<Map<String, Object>> getLeafPart(String code, String ver) {
        String sql = SQL.get("bom-leaf-node");
        return getSession().createSQLQuery(sql).setParameter("code", code).setParameter("ver", ver).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    public List<Map<String, Object>> getProducedPartCount(Long producePlanId) {
        String sql = SQL.get("produced-part-count");
        return getSession().createSQLQuery(sql).setParameter("id", producePlanId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}

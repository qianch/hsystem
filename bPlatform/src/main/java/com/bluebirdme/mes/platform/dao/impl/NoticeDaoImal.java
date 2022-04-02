package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.platform.dao.INoticeDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Repository
public class NoticeDaoImal extends BaseDaoImpl implements INoticeDao {
    @Resource
    SessionFactory sessionFactory;

    @Override
    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.findPageInfo(filter, page, "notice-list");
    }

    @Override
    public void delete(final String ids) {
        this.getSession().createSQLQuery(SQL.get("notice-delete")).setParameterList("id", ids.split(",")).executeUpdate();
    }

    @Override
    public List<Map<String, Object>> findNotice() {
        final String sqlForNotice = SQL.get("notice-findnotice");
        final SQLQuery query = this.getSession().createSQLQuery(sqlForNotice);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (List<Map<String, Object>>) query.list();
    }
}

package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.platform.dao.ISystemInfoDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Repository
public class SystemInfoDaoImal extends BaseDaoImpl implements ISystemInfoDao {
    @Resource
    SessionFactory sessionFactory;

    @Override
    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public Object findUserCount() {
        final String sqlForNotice = SQL.get("userCount");
        final SQLQuery userCount = this.getSession().createSQLQuery(sqlForNotice);
        return userCount.list().get(0);
    }

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return null;
    }
}

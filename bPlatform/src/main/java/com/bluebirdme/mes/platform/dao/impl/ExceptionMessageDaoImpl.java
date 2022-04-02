package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.platform.dao.IExceptionMessageDao;
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
public class ExceptionMessageDaoImpl extends BaseDaoImpl implements IExceptionMessageDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.findPageInfo(filter, page, "exception-list");
    }

    @Override
    public void clearAll() {
        final String sql = "delete from platform_exception_message ";
        final SQLQuery query = this.getSession().createSQLQuery(sql);
        query.executeUpdate();
    }
}

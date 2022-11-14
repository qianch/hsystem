package com.bluebirdme.mes.common.dao.impl;

import com.bluebirdme.mes.common.dao.IFixDao;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 类注释
 *
 * @author Goofy
 * @Date 2017年7月13日 上午9:31:41
 */
@Repository
public class FixDaoImpl extends BaseDaoImpl implements IFixDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter var1, Page var2) throws Exception {
        return null;
    }
}

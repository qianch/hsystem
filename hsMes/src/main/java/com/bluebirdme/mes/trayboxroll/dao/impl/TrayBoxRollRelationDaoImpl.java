package com.bluebirdme.mes.trayboxroll.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.trayboxroll.dao.TrayBoxRollRelationDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class TrayBoxRollRelationDaoImpl extends BaseDaoImpl implements TrayBoxRollRelationDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "trayboxrollrelation-list");
    }

    public Map<String, Object> findPageInfoRollWeight(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "trayboxrollrelation-totalrollweight");
    }
}

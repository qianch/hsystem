package com.bluebirdme.mes.mobile.stock.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.mobile.stock.dao.IMobileTurnBagDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * PDA翻包操作
 *
 * @author Goofy
 * @Date 2017年2月11日 上午11:03:13
 */
@Repository
public class MobileTurnBagDaoImpl extends BaseDaoImpl implements IMobileTurnBagDao {

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
}

package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.platform.dao.IScheduleTemplateDao;
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
public class ScheduleTemplateDaoImpl extends BaseDaoImpl implements IScheduleTemplateDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return this.factory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return super.findPageInfo(filter, page, "scheduleTmpelate-list");
    }
}

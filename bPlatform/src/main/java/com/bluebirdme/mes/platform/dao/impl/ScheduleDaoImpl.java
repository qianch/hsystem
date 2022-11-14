package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.platform.dao.IScheduleDao;
import com.bluebirdme.mes.platform.entity.Schedule;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Repository
public class ScheduleDaoImpl extends BaseDaoImpl implements IScheduleDao {
    @Resource
    SessionFactory factory;

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return super.findPageInfo(filter, page, "schedule-list");
    }

    @Override
    public Session getSession() {
        return this.factory.getCurrentSession();
    }

    @Override
    public Schedule getScheduleByClazz(final String clazz) {
        final Query query = this.getSession().createQuery(SQL.get("schedule-getScheduleByClazz"));
        query.setParameter("clazz", clazz);
        return (Schedule) query.uniqueResult();
    }

    @Override
    public List<Schedule> findUncompleteSchedule() {
        final Query query = this.getSession().createQuery(SQL.get("schedule-findUncompleteSchedule"));
        return (List<Schedule>) query.list();
    }
}

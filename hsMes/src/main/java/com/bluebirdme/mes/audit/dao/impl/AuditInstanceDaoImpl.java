/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.audit.dao.impl;

import com.bluebirdme.mes.audit.dao.IAuditInstanceDao;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class AuditInstanceDaoImpl extends BaseDaoImpl implements IAuditInstanceDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "auditInstance-list");
    }

    public Map<String, Object> auditTask(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "auditTask-unfinished");
    }

    @Override
    public Map<String, Object> finishedAuditTask(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "auditTask-finished");
    }

    @Override
    public Map<String, Object> myAuditTask(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "auditTask-myAudit");
    }

    public <T> void updateByCondition(Class<T> clazz, Map<String, Object> condition, Map<String, Object> values) {
        StringBuilder builder = new StringBuilder("update " + clazz.getSimpleName() + " set ");
        Iterator<Entry<String, Object>> it = values.entrySet().iterator();
        Entry<String, Object> entry;
        int count = 0;
        while (it.hasNext()) {
            entry = it.next();
            builder.append(count == 0 ? "" : ",").append(entry.getKey()).append("=:v").append(entry.getKey());
            count++;
        }
        it = condition.entrySet().iterator();
        builder.append(" where 1=1 ");
        while (it.hasNext()) {
            entry = it.next();
            builder.append(" and ").append(entry.getKey()).append("=:w").append(entry.getKey());
        }
        Query query = getSession().createQuery(builder.toString());

        it = values.entrySet().iterator();
        while (it.hasNext()) {
            entry = it.next();
            query.setParameter("v" + entry.getKey(), entry.getValue());
        }

        it = condition.entrySet().iterator();
        while (it.hasNext()) {
            entry = it.next();
            query.setParameter("w" + entry.getKey(), entry.getValue());
        }
        query.executeUpdate();
    }
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.tracings.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.tracings.dao.ITracingLogDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class TracingLogDaoImpl extends BaseDaoImpl implements ITracingLogDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "tracingLog-list");
    }

    @Override
    public List<Map<String, Object>> findByTraceBackTo(String rollbarcode) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) getSession().createSQLQuery(SQL.get("tracingLog-lists")).setParameter("rollbarcode", rollbarcode).list();
        return list;
    }
}

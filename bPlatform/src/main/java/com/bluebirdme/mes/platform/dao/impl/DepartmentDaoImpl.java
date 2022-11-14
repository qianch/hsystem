package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.dao.IDepartmentDao;
import com.bluebirdme.mes.platform.entity.Department;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Repository
public class DepartmentDaoImpl extends BaseDaoImpl implements IDepartmentDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.findPageInfo(filter, page, "dept-list");
    }

    @Override
    public void batchUpdateDepartmentLevel(final List<Department> list) {
        final String sql = "update platform_department set pid=:pid where id=:id";
        final SQLQuery query = getSession().createSQLQuery(sql);
        for (final Department dept : list) {
            query.setParameter("pid", dept.getPid());
            query.setParameter("id", dept.getId());
            query.executeUpdate();
        }
    }

    @Override
    public List<Map<String, Object>> queryDepartment(String type) throws SQLTemplateException {
        String[] types = type.split(",");
        String strType = "";
        for (int i = 0; i < types.length; i++) {
            strType += "'" + types[i] + "',";
        }

        if (strType.length() > 0) {
            strType = strType.substring(0, strType.length() - 1);
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", strType);
        String sql = SQL.get(map, "dept-combobox");
        return getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    @Override
    public List<Map<String, Object>> queryAllDepartment() throws SQLTemplateException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String sql = SQL.get(map, "allworkshop-combobox");
        return getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    @Override
    public List<Map<String, Object>> queryAllDepartmentByType(String type) throws SQLTemplateException {
        String[] types = type.split(",");
        String strType = "";
        for (int i = 0; i < types.length; i++) {
            strType += "'" + types[i] + "',";
        }

        if (strType.length() > 0) {
            strType = strType.substring(0, strType.length() - 1);
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", strType);
        String sql = SQL.get(map, "dept-combobox");
        return getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }
}

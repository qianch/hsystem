package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.platform.dao.IUserDao;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.entity.UserRole;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl implements IUserDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return this.factory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.findPageInfo(filter, page, "user-list");
    }

    @Override
    public List<Map<String, Object>> getMenuPermissions(final Serializable userId) {
        final String sql = SQL.get("user-getMenuPermissions");
        final Map<String, Object> map = new HashMap();
        map.put("u.id", userId);
        return super.findListMapByMap(sql, map);
    }

    @Override
    public List<Map<String, Object>> getButtonPermissions(final Serializable userId) {
        final String sql = SQL.get("user-getButtonPermissions");
        final Map<String, Object> map = new HashMap();
        map.put("u.id", userId);
        return super.findListMapByMap(sql, map);
    }

    @Override
    public List<String> getUrlPermissions(final Serializable userId) {
        final String sql = SQL.get("user-getUrlPermissions");
        final SQLQuery query = this.getSession().createSQLQuery(sql);
        query.setParameter("u.id", userId);
        return (List<String>) query.list();
    }

    @Override
    public List<String> getIdPermissions(final Serializable userId) {
        final String sql = "select m.id from platform_permission p left join platform_menu m on p.mid=m.id where p.rid in (select ur.rid from platform_user_role ur left join platform_user u on u.id=ur.uid where u.id=:u.id) ";
        final SQLQuery query = this.getSession().createSQLQuery(sql);
        query.setParameter("u.id", userId);
        return (List<String>) query.list();
    }

    @Override
    public void saveRole(final Long[] uids, final Long[] rids) {
        final String hql = SQL.get("user-deleteRoles");
        this.getSession().createQuery(hql).setParameterList("uid", uids).executeUpdate();
        final List<UserRole> list = new ArrayList();
        for (final Long uid : uids) {
            for (final Long rid : rids) {
                UserRole ur = new UserRole();
                ur.setRoleId(rid);
                ur.setUserId(uid);
                list.add(ur);
            }
        }
        super.save(list.toArray(new UserRole[0]));
    }

    @Override
    public List<Map<String, Object>> getUsersByDepartments(final List<Department> list) throws Exception {
        if (list == null || list.size() == 0) {
            return null;
        }
        final StringBuilder builder = new StringBuilder();
        int i = 0;
        for (final Department department : list) {
            builder.append(((i == 0) ? "" : ",") + department.getId());
            ++i;
        }
        final Filter filter = new Filter();
        filter.set("did", "in:" + builder.toString());
        final Page page = new Page();
        page.setAll(1);
        return this.findPageData(filter, page, "user-getUsersByDepartments");
    }

    @Override
    public void deleteUserRole(final List<User> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        final StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            buffer.append(((i == 0) ? "" : ",") + list.get(i).getId());
        }
        final String sql = "delete from platform_user_role where id in (" + buffer.toString() + ")";
        this.getSession().createSQLQuery(sql).executeUpdate();
    }
}

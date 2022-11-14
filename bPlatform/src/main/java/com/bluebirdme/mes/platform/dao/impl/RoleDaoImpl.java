package com.bluebirdme.mes.platform.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.platform.dao.IRoleDao;
import com.bluebirdme.mes.platform.entity.Permission;
import com.bluebirdme.mes.platform.entity.Role;
import com.bluebirdme.mes.platform.entity.UserRole;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Repository
public class RoleDaoImpl extends BaseDaoImpl implements IRoleDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return this.factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.findPageInfo(filter, page, "role-list");
    }

    @Override
    public void delete(final List<Role> roles) {
        final SQLQuery query = this.getSession().createSQLQuery(SQL.get("role-delete"));
        for (final Role role : roles) {
            query.setParameter("id", role.getId());
            query.executeUpdate();
        }
    }

    public void deletePermissionByRole(final Serializable roleId) {
        final SQLQuery query = this.getSession().createSQLQuery(SQL.get("role-deletePermission"));
        query.setParameter("rid", roleId);
        query.executeUpdate();
    }

    @Override
    public void deletePermissionByRole(final List<Role> list) {
        final SQLQuery query = this.getSession().createSQLQuery(SQL.get("role-deletePermissionByRole"));
        final Object[] o = new Object[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            o[i] = list.get(i).getId();
        }
        query.setParameterList("rid", o);
        query.executeUpdate();
    }

    @Override
    public void savePermission(final Role role, final String ids) {
        final String[] idArray = ids.split(",");
        this.deletePermissionByRole(role.getId());
        Permission permission;
        for (final String id : idArray) {
            permission = new Permission();
            permission.setMid(Long.parseLong(id));
            permission.setRid(role.getId());
            super.save(permission);
        }
    }

    @Override
    public List<UserRole> getRolesByUserId(final String userId) {
        return (List<UserRole>) this.getSession().createSQLQuery(SQL.get("user-getRolesByUser")).setParameter("uid", Long.parseLong(userId)).setResultTransformer(Transformers.aliasToBean(UserRole.class)).list();
    }

    @Override
    public List<String> getPermissionByRole(final Role role) throws Exception {
        final String sql = "select mid from platform_permission where rid=:rid ";
        final SQLQuery query = this.getSession().createSQLQuery(sql);
        query.setParameter("rid", role.getId());
        return (List<String>) query.list();
    }
}

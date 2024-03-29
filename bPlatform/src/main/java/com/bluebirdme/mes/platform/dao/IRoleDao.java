package com.bluebirdme.mes.platform.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.platform.entity.Role;
import com.bluebirdme.mes.platform.entity.UserRole;

import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public interface IRoleDao extends IBaseDao {
    @Override
    <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception;

    void delete(final List<Role> p0);

    void savePermission(final Role p0, final String p1);

    List<UserRole> getRolesByUserId(final String p0);

    List<String> getPermissionByRole(final Role p0) throws Exception;

    void deletePermissionByRole(final List<Role> p0);
}

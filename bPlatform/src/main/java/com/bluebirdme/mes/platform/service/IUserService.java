package com.bluebirdme.mes.platform.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public interface IUserService extends IBaseService {
    @Override
    Map<String, Object> findPageInfo(final Filter p0, final Page p1) throws Exception;

    List<Map<String, Object>> getMenuPermissions(final Serializable p0);

    List<Map<String, Object>> getButtonPermissions(final Serializable p0);

    List<String> getUrlPermissions(final Serializable p0);

    List<String> getIdPermissions(final Serializable p0);

    void saveRole(final Long[] p0, final Long[] p1);

    List<Map<String, Object>> getUsersByDepartments(final List<Department> p0) throws Exception;

    void deleteUserRole(final List<User> p0);

    void delete(final List<User> p0) throws Exception;
}

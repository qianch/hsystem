package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.annotation.Desc;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.IRoleDao;
import com.bluebirdme.mes.platform.entity.Role;
import com.bluebirdme.mes.platform.entity.UserRole;
import com.bluebirdme.mes.platform.service.IRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Desc("角色Service")
@Service
@Transactional
public class RoleServiceImpl extends BaseServiceImpl implements IRoleService {
    @Resource
    IRoleDao roleDao;

    @Override
    protected IBaseDao getBaseDao() {
        return this.roleDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.roleDao.findPageInfo(filter, page);
    }

    @Override
    public void delete(final List<Role> roles) {
        this.roleDao.deletePermissionByRole(roles);
        this.roleDao.delete(roles);
    }

    @Override
    public void savePermission(final Role role, final String ids) {
        this.roleDao.savePermission(role, ids);
    }

    @Override
    public List<UserRole> getRolesByUserId(final String userId) {
        return this.roleDao.getRolesByUserId(userId);
    }

    @Override
    public List<String> getPermissionByRole(final Role role) throws Exception {
        return this.roleDao.getPermissionByRole(role);
    }
}

package com.bluebirdme.mes.platform.service.impl;


import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.platform.dao.IUserDao;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl implements IUserService {
    @Resource
    IUserDao userDao;

    @Override
    protected IBaseDao getBaseDao() {
        return this.userDao;
    }

    @Override
    public List<Map<String, Object>> getMenuPermissions(final Serializable userId) {
        return this.userDao.getMenuPermissions(userId);
    }

    @Override
    public List<Map<String, Object>> getButtonPermissions(final Serializable userId) {
        return this.userDao.getButtonPermissions(userId);
    }

    @Override
    public List<String> getUrlPermissions(final Serializable userId) {
        return this.userDao.getUrlPermissions(userId);
    }

    @Override
    public <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return this.userDao.findPageInfo(filter, page);
    }

    @Override
    public List<String> getIdPermissions(final Serializable userId) {
        return this.userDao.getIdPermissions(userId);
    }

    @Override
    public void saveRole(final Long[] uids, final Long[] rids) {
        this.userDao.saveRole(uids, rids);
    }

    @Override
    public List<Map<String, Object>> getUsersByDepartments(final List<Department> list) throws Exception {
        return this.userDao.getUsersByDepartments(list);
    }

    @Override
    public void deleteUserRole(final List<User> list) {
        this.userDao.deleteUserRole(list);
    }

    @Override
    public void delete(final List<User> list) throws Exception {
        this.userDao.delete(User.class, true, list);
        this.deleteUserRole(list);
    }
}

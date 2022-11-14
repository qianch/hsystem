package com.bluebirdme.mes.platform.service.impl;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.dao.IDepartmentDao;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.service.IDepartmentService;
import com.bluebirdme.mes.platform.service.IUserService;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
@Service
@Transactional
public class DepartmentServiceImpl extends BaseServiceImpl implements IDepartmentService {
    @Resource
    IDepartmentDao departmentDao;
    @Resource
    IUserService userService;

    @Override
    protected IBaseDao getBaseDao() {
        return departmentDao;
    }

    @Override
    public Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception {
        return departmentDao.findPageInfo(filter, page);
    }

    @Override
    public void batchUpdateDepartmentLevel(final List<Department> list) {
        departmentDao.batchUpdateDepartmentLevel(list);
    }

    @Override
    public void delete(final List<Department> list) throws Exception {
        final List<Map<String, Object>> ulist = userService.getUsersByDepartments(list);
        if (ulist == null || ulist.size() == 0) {
            delete(Department.class, true, list);
            return;
        }
        final String[] names = new String[ulist.size()];
        for (int i = 0; i < ulist.size(); ++i) {
            names[i] = MapUtils.getAsStringIgnoreCase(ulist.get(i), "USERNAME");
        }
        throw new Exception("无法删除所选部门,请先到人员管理删除如下人员信息：" + new GsonBuilder().create().toJson(names));
    }

    @Override
    public List<Map<String, Object>> queryDepartment(String type) throws SQLTemplateException {
        return departmentDao.queryDepartment(type);
    }

    @Override
    public List<Map<String, Object>> queryAllDepartment() throws SQLTemplateException {
        return departmentDao.queryAllDepartment();
    }

    @Override
    public List<Map<String, Object>> queryAllDepartmentByType(String type) throws SQLTemplateException {
        return departmentDao.queryAllDepartmentByType(type);
    }
}

package com.bluebirdme.mes.platform.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.entity.Department;

import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface IDepartmentService extends IBaseService {
    @Override
    Map<String, Object> findPageInfo(final Filter p0, final Page p1) throws Exception;

    void batchUpdateDepartmentLevel(final List<Department> p0);

    void delete(final List<Department> p0) throws Exception;

    public List<Map<String, Object>> queryDepartment(String type) throws SQLTemplateException;

    public List<Map<String, Object>> queryAllDepartment() throws SQLTemplateException;

    List<Map<String, Object>> queryAllDepartmentByType(String type) throws SQLTemplateException;
}

package com.bluebirdme.mes.platform.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.platform.entity.Department;

import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface IDepartmentDao extends IBaseDao {
    @Override
    <T> Map<String, Object> findPageInfo(final Filter filter, final Page page) throws Exception;

    void batchUpdateDepartmentLevel(final List<Department> p0);

    /**
     * 根据车间类型获取车间
     * @param type
     * @return
     * @throws SQLTemplateException
     */
    List<Map<String, Object>> queryDepartment(String type) throws SQLTemplateException;

    /**
     * 获取车间
     * @return
     * @throws SQLTemplateException
     */
    List<Map<String, Object>> queryAllDepartment() throws SQLTemplateException;

    List<Map<String, Object>> queryAllDepartmentByType(String type) throws SQLTemplateException;
}

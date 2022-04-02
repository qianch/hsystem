package com.bluebirdme.mes.cache;

import com.bluebirdme.mes.core.spring.SpringContextHolder;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.service.IDepartmentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentCache {
    private static DepartmentCache instance = new DepartmentCache();

    public static DepartmentCache getInstance() {
        return instance;
    }

    private static final Map<String, Department> departmentMap = new HashMap();

    public static Map<String, Department> getDepartmentList() {
        if (departmentMap.size() > 0) {
            return departmentMap;
        }
        //清空缓存数据
        departmentMap.clear();
        IDepartmentService departmentService = SpringContextHolder.getBean(IDepartmentService.class);
        List<Department> listDepartment = departmentService.findAll(Department.class);
        for (Department department : listDepartment) {

            departmentMap.put(department.getCode(), department);
        }

        return departmentMap;
    }
}

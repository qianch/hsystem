package com.bluebirdme.mes.printer.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

public interface IPrinterManageDao extends IBaseDao {
    //查询所有部门
    List<Map<String, Object>> findALLDepartment();
}

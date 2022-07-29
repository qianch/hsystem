package com.bluebirdme.mes.printer.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IPrinterManageSerivice extends IBaseService {
    /**
     * 查询所有部门
     *
     */
    List<Map<String, Object>> findALLDepartment();
}

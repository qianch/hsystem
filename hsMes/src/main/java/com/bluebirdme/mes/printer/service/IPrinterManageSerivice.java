package com.bluebirdme.mes.printer.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.service.IBaseService;

public interface IPrinterManageSerivice extends IBaseService {
	//查询所有部门
	public List<Map<String,Object>> findALLDepartment();
}

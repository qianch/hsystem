package com.bluebirdme.mes.printer.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

public interface IPrinterManageDao extends IBaseDao {
	//查询所有部门
	public List<Map<String,Object>> findALLDepartment();
}

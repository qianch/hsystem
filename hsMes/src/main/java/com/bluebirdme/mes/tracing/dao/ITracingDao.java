package com.bluebirdme.mes.tracing.dao;

import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

public interface ITracingDao extends IBaseDao {
	public Map<String, Object> tracing(String code);
}

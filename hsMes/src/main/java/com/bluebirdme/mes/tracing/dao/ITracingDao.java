package com.bluebirdme.mes.tracing.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.Map;

public interface ITracingDao extends IBaseDao {
    Map<String, Object> tracing(String code);
}

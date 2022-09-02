package com.bluebirdme.mes.trayboxroll.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import java.util.Map;

public interface TrayBoxRollRelationDao extends IBaseDao {
    Map<String, Object> findPageInfoRollWeight(Filter filter, Page page) throws Exception;
}

package com.bluebirdme.mes.trayboxroll.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.Map;

public interface TrayBoxRollRelationService extends IBaseService {
    Map<String, Object> findPageInfoRollWeight(Filter filter, Page page) throws Exception;
}

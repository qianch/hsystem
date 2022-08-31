package com.bluebirdme.mes.store.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface ITrayService extends IBaseService {
    List<Map<String, Object>> findRollBox(String code);
}

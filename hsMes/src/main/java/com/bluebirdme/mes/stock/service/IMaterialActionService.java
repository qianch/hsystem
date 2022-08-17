package com.bluebirdme.mes.stock.service;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.Map;

public interface IMaterialActionService extends IBaseService {
    /**
     * 原料出库(明细)
     */
    Map<String, Object> findPageOutInfo(Filter filter, Page page) throws Exception;

    /**
     * 原料库存
     */
    Map<String, Object> findPageDetailInfo(Filter filter, Page page) throws Exception;

    /**
     * 异常退回巨石
     */
    Map<String, Object> findPageForceOutInfo(Filter filter, Page page) throws Exception;

    /**
     * 根据ID 冻结\解冻|放行\取消放行
     */
    void action(Long[] mssId, String action, String actionValue) throws Exception;
}

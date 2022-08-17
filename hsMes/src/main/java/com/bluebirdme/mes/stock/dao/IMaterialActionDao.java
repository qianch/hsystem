package com.bluebirdme.mes.stock.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import java.util.Map;

public interface IMaterialActionDao extends IBaseDao {
    Map<String, Object> findPageOutInfo(Filter filter, Page page) throws Exception;

    Map<String, Object> findPageDetailInfo(Filter filter, Page page) throws Exception;

    Map<String, Object> findPageForceOutInfo(Filter filter, Page page) throws Exception;

    /**
     * 根据ID 冻结\解冻|放行\取消放行
     *
     * @param mssId
     * @param action
     * @param actionValue
     */
    void action(Long mssId[], String action, String actionValue);
}

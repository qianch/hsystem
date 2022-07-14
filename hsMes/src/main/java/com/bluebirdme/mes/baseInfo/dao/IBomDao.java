package com.bluebirdme.mes.baseInfo.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * @author Goofy
 * @Date 2016年10月19日 下午2:38:04
 */
public interface IBomDao extends IBaseDao {
    <T> List<T> getBomDetails(Class<T> clazz, String bomCode, String bomVersionCode);

    List<Map<String, Object>> findSalesOrderDetail(Long id, String c) throws Exception;

    List<Map<String, Object>> findSalesOrderDetail1(Long id) throws Exception;
}

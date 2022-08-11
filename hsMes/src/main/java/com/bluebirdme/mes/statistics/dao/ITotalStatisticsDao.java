/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.statistics.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;

import java.util.Map;

/**
 * @author 徐波
 * @Date 2016-11-26 14:44:04
 */

public interface ITotalStatisticsDao extends IBaseDao {
    Map<String, Object> findPageInfoByBox(Filter filter, Page page) throws Exception;

    Map<String, Object> findPageInfoByTray(Filter filter, Page page) throws Exception;

    Map<String, Object> findPageInfoByPart(Filter filter, Page page) throws Exception;

    Map<String, Object> findPageInfoByRoll(Filter filter, Page page) throws Exception;

    boolean isPackaged(String barcode);

    /**
     * 车间生产汇总(车间生产统计(产品大类、订单号、次号、车间))
     */
    Map<String, Object> productsShopSummary(Filter filter, Page page) throws Exception;

    /**
     * 车间生产统计（产品大类、厂内名称）
     */
    Map<String, Object> genericFactorySummary(Filter filter, Page page) throws Exception;

    /**
     * 生产领料汇总
     */
    Map<String, Object> getPickingStatistics(Filter filter, Page page) throws Exception;
}

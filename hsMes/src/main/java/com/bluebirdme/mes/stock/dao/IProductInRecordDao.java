/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.stock.entity.ProductInRecord;

import java.util.Map;

/**
 * @author 宋黎明
 * @Date 2016-11-14 11:42:22
 */

public interface IProductInRecordDao extends IBaseDao {
    /**
     * 车间入库汇总报表（产品大类、订单号、批次号、厂内名称)
     */
    Map<String, Object> productsShopStatistics(Filter filter, Page page) throws Exception;

    /**
     * 车间入库汇总报表（产品大类、厂内名称汇总重量)
     */
    Map<String, Object> shopStorageCategoryStatistics(Filter filter, Page page) throws Exception;

    Map<String, Object> findPageInfoDRK(Filter filter, Page page);

    ProductInRecord findfirstProductInRecord(String column, Object value);
}

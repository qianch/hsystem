/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.service;

import java.util.Map;

import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.stock.entity.ProductInRecord;

/**
 *
 * @author 宋黎明
 * @Date 2016-11-14 11:42:22
 */
public interface IProductInRecordService extends IBaseService {
	/**
	 * 车间入库汇总报表（产品大类、订单号、批次号、厂内名称)
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> productsShopStatistics(Filter filter, Page page)throws Exception;

	/**
	 * 车间入库汇总报表（产品大类、厂内名称汇总重量)
	 * @param filter
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> shopStorageCategoryStatistics(Filter filter, Page page)throws Exception;

	Map<String, Object> findPageInfoDRK(Filter filter, Page page);

	 /**
	 * 根据列查询第一个内容
	 * @param column 列名
	 * @param value 列值
	 * @return 返回实体对象
	 * @throws Exception
	 */
	public ProductInRecord findfirstProductInRecord(String column, Object value);
}

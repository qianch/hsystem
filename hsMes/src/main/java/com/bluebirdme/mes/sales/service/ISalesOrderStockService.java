/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * 
 * @author 高飞
 * @Date 2016-12-15 18:28:22
 */
public interface ISalesOrderStockService extends IBaseService {
	/**
	 * 套材添加库存
	 * @param salesOrderSubCode
	 * @param product
	 * @param partName
	 *//*
	public void addStock(String salesOrderSubCode,FinishedProduct product,String partName);
	
	*//**
	 * 添加非套材库存
	 * @param salesOrderSubCode
	 * @param product
	 *//*
	public void addStock(String salesOrderSubCode,FinishedProduct product);
	
	*//**
	 * 根据订单号，产品ID，部件名称，查询订单库存信息
	 * @param salesOrderSubCode
	 * @param productId
	 * @param partName
	 * @return
	 *//*
	public SalesOrderStock  getSalesOrderStock(String salesOrderSubCode,Long productId,String partName);
	
	*//**
	 * 真正出库时候调用
	 * @param sos
	 * @throws Exception 
	 *//*
	public void outStock(List<SalesOrderStock> sosList) throws Exception;
	public void addStock(String salesOrderSubCode,FinishedProduct product,int count);*/
}

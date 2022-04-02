package com.bluebirdme.mes.mobile.stock.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.service.IBaseService;

public interface IMobileProductStockService extends IBaseService {
	public String findDeliveryPlanDetail(String project,String content) throws Exception;
	public String findDeliveryPlanById(String id,String pn) throws Exception;
	public String findDeliveryPlanProductById(String id,String pn) throws Exception;
	public List<Map<String,Object>> searchProduct(String salesOrderSubCode,String batchCode,Long productId,Long partId) throws Exception;
	public String findTrayByBarCode(String barcode ) throws Exception;
}

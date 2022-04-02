package com.bluebirdme.mes.mobile.stock.dao;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

public interface IMobileProductStockDao extends IBaseDao {
	public String findDeliveryPlanDetail(String project,String content) throws SQLTemplateException;
	public String findDeliveryPlanById(String id,String pn) throws SQLTemplateException;
	public String findDeliveryPlanProductById(String id,String pn) throws SQLTemplateException;
	public List<Map<String, Object>> searchProduct(String salesOrderSubCode, String batchCode, Long productId, Long partId) throws SQLTemplateException;
	public List<Map<String, Object>> findTrayByBarCode(String barcode ) throws Exception;
	public List<Map<String, Object>> findPartByBarCode(String barcode ) throws Exception;
}

package com.bluebirdme.mes.mobile.stock.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

import java.util.List;
import java.util.Map;

public interface IMobileProductStockDao extends IBaseDao {
    String findDeliveryPlanDetail(String project, String content) throws SQLTemplateException;

    String findDeliveryPlanById(String id, String pn) throws SQLTemplateException;

    String findDeliveryPlanProductById(String id, String pn) throws SQLTemplateException;

    List<Map<String, Object>> searchProduct(String salesOrderSubCode, String batchCode, Long productId, Long partId) throws SQLTemplateException;

    List<Map<String, Object>> findTrayByBarCode(String barcode) throws Exception;

    List<Map<String, Object>> findPartByBarCode(String barcode) throws Exception;
}

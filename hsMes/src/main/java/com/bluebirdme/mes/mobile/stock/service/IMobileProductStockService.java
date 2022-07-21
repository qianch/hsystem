package com.bluebirdme.mes.mobile.stock.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IMobileProductStockService extends IBaseService {
    String findDeliveryPlanDetail(String project, String content) throws Exception;

    String findDeliveryPlanById(String id, String pn) throws Exception;

    String findDeliveryPlanProductById(String id, String pn) throws Exception;

    List<Map<String, Object>> searchProduct(String salesOrderSubCode, String batchCode, Long productId, Long partId) throws Exception;

    String findTrayByBarCode(String barcode) throws Exception;
}

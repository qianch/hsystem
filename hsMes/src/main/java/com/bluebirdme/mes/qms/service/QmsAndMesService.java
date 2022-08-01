package com.bluebirdme.mes.qms.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface QmsAndMesService extends IBaseService {
    List<Map<String, Object>> findRollandPartByBatchCode(String batchCode, String deliveryCode) throws Exception;

    List<Map<String, Object>> selectBomByCode(String salesOrderId, String procBomCode) throws Exception;

    List<Map<String, Object>> selectOUTOrder(String deliveryCode, String batchCode) throws Exception;

    List<Map<String, Object>> selectOUTOrderByByDeliveryPlan(String deliveryCode, String batchCode) throws Exception;
}

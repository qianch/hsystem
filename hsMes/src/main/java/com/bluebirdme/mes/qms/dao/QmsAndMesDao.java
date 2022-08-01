package com.bluebirdme.mes.qms.dao;

import com.bluebirdme.mes.core.base.dao.IBaseDao;

import java.util.List;
import java.util.Map;

public interface QmsAndMesDao extends IBaseDao {
    List<Map<String, Object>> findRollandPartByBatchCode(String batchCode, String deliveryCode) throws Exception;

    List<Map<String, Object>> selectBomByCode(String salesOrderId, String procBomCode) throws Exception;

    List<Map<String, Object>> selectOUTOrder(String deliveryCode, String batchCode) throws Exception;

    List<Map<String, Object>> selectOUTOrderByDeliveryPlan(String deliveryCode, String batchCode) throws Exception;

    List<Map<String, Object>> selectOUTOrderOfMirror(String deliveryCode, String batchCode) throws Exception;

    List<Map<String, Object>> selectOUTOrderByDeliveryPlanOfMirror(String deliveryCode, String batchCode) throws Exception;
}

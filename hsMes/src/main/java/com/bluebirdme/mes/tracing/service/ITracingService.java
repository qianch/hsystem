package com.bluebirdme.mes.tracing.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import com.bluebirdme.mes.stock.entity.ProductOutRecord;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.entity.TrayBarCode;
import com.bluebirdme.mes.store.entity.TrayBoxRoll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ITracingService extends IBaseService {
    HashMap<String, List<TrayBarCode>> findTrayBarCode(List<String> trayCodeList);

    HashMap<String, List<ProductOutRecord>> findOutStockRecordByCondition(List<String> barcode, int type);

    HashMap<String, List<ProductInRecord>> findProductInRecord(List<String> barcode, int type);

    HashMap<String, List<TrayBoxRoll>> findTrayBoxRollByCondition(List<String> barcode, int type);

    HashMap<String, List<SalesOrder>> findSalesOrder(List<String> salesOrderCode);

    HashMap<Long, List<SalesOrderDetail>> findSalesOrderDetail(List<Long> ids, int type);

    HashMap<String, List<ProducePlan>> findProducePlan(List<String> salesOrderCodeList);

    HashMap<Long, List<ProducePlanDetail>> findProducePlanDetail(List<Long> producePlanId);

    HashMap<String, List<RollBarcode>> findRollBarcodes(List<String> batchCodes);

    HashMap<String, List<ProducePlanDetail>> findProducePlanDetailByBatchCode(List<String> batchCodes);

    HashMap<String, List<WeavePlan>> findWeavePlanByBatchCode(List<String> batchCodes);

    HashMap<String, List<CutPlan>> findCutPlanByBatchCode(List<String> batchCodes);

    Map<String, Object> tracing(String code);
}

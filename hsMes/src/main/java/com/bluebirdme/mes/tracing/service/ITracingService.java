package com.bluebirdme.mes.tracing.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public interface ITracingService extends IBaseService {
	public HashMap<String, List<TrayBarCode>> findTrayBarCode(
			List<String> trayCodeList);
	public HashMap<String, List<ProductOutRecord>> findOutStockRecordByCondition(
			List<String> barcode,int type);
	public HashMap<String,List<ProductInRecord>> findProductInRecord(List<String> barcode,int type);
	public HashMap<String, List<TrayBoxRoll>> findTrayBoxRollByCondition(
			List<String> barcode, int type);
	public HashMap<String, List<SalesOrder>> findSalesOrder(
			List<String> salesOrderCode);
	public HashMap<Long, List<SalesOrderDetail>> findSalesOrderDetail(
			List<Long> ids, int type) ;
	public HashMap<String,List<ProducePlan>> findProducePlan(List<String> salesOrderCodeList);
	public HashMap<Long,List<ProducePlanDetail>> findProducePlanDetail(List<Long> producePlanId);
	public HashMap<String,List<RollBarcode>> findRollBarcodes(List<String> batchCodes);
	public HashMap<String,List<ProducePlanDetail>> findProducePlanDetailByBatchCode(List<String> batchCodes);
	public HashMap<String,List<WeavePlan>> findWeavePlanByBatchCode(List<String> batchCodes);
	public HashMap<String,List<CutPlan>> findCutPlanByBatchCode(List<String> batchCodes);
	public Map<String, Object> tracing(String code);
}

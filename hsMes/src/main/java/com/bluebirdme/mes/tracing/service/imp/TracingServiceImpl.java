package com.bluebirdme.mes.tracing.service.imp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
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
import com.bluebirdme.mes.tracing.dao.ITracingDao;
import com.bluebirdme.mes.tracing.service.ITracingService;
import com.bluebirdme.mes.tracing.service.TracingType;
@Service
@AnyExceptionRollback
public class TracingServiceImpl extends BaseServiceImpl implements
		ITracingService {
	@Resource
	ITracingDao tracingDao;

	@Override
	protected IBaseDao getBaseDao() {
		return tracingDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page)
			throws Exception {
		return tracingDao.findPageInfo(filter, page);
	}

	// 通过托条码查询TrayBarCode
	public HashMap<String, List<TrayBarCode>> findTrayBarCode(
			List<String> trayCodeList) {
		HashMap<String, List<TrayBarCode>> resultList = new HashMap<String, List<TrayBarCode>>();
		for (String trayCode : trayCodeList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("barcode", trayCode);
			resultList.put(trayCode, findListByMap(TrayBarCode.class, map));
		}
		return resultList;
	}

	// 通过托条码查询产品出库记录
	public HashMap<String, List<ProductOutRecord>> findOutStockRecordByCondition(
			List<String> barcode, int type) {
		HashMap<String, List<ProductOutRecord>> resultList = new HashMap<String, List<ProductOutRecord>>();
		if (type == TracingType.TRAYBARCODE) {
			for (String trayCode : barcode) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("trayCode", trayCode);
				resultList.put(trayCode,
						findListByMap(ProductOutRecord.class, map));
			}
		} else if (type == TracingType.ROLLBARCODE) {
			for (String trayCode : barcode) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("rollCode", trayCode);
				resultList.put(trayCode,
						findListByMap(ProductOutRecord.class, map));
			}
		}
		return resultList;
	}

	// 通过条码查询入库记录
	public HashMap<String, List<ProductInRecord>> findProductInRecord(
			List<String> barcode, int type) {
		HashMap<String, List<ProductInRecord>> resultList = new HashMap<String, List<ProductInRecord>>();
		if (type == TracingType.TRAYBARCODE) {
			for (String trayCode : barcode) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("trayCode", trayCode);
				resultList.put(trayCode,
						findListByMap(ProductInRecord.class, map));
			}
		} else if (type == TracingType.ROLLBARCODE) {
			for (String trayCode : barcode) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("rollCode", trayCode);
				resultList.put(trayCode,
						findListByMap(ProductInRecord.class, map));
			}
		}
		return resultList;
	}

	// 通过托条码查询箱托卷关系
	public HashMap<String, List<TrayBoxRoll>> findTrayBoxRollByCondition(
			List<String> barcode, int type) {

		HashMap<String, List<TrayBoxRoll>> resultList = new HashMap<String, List<TrayBoxRoll>>();
		if (type == TracingType.TRAYBARCODE) {
			for (String trayCode : barcode) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("trayBarcode", trayCode);
				resultList.put(trayCode, findListByMap(TrayBoxRoll.class, map));
			}
		}
		if (type == TracingType.ROLLBARCODE) {
			for (String trayCode : barcode) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("rollBarcode", trayCode);
				resultList.put(trayCode, findListByMap(TrayBoxRoll.class, map));
			}
		}
		if (type == TracingType.BOXBARCODE) {
			for (String trayCode : barcode) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("boxBarcode", trayCode);
				resultList.put(trayCode, findListByMap(TrayBoxRoll.class, map));
			}
		}
		if (type == TracingType.PARTBARCODE) {
			for (String trayCode : barcode) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("partBarcode", trayCode);
				resultList.put(trayCode, findListByMap(TrayBoxRoll.class, map));
			}
		}
		return resultList;
	}

	// 通过订单号查询订单
	public HashMap<String, List<SalesOrder>> findSalesOrder(
			List<String> salesOrderCode) {
		HashMap<String, List<SalesOrder>> resultList = new HashMap<String, List<SalesOrder>>();
		for (String orderCode : salesOrderCode) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("salesOrderCode", orderCode);
			resultList.put(orderCode, findListByMap(SalesOrder.class, map));
		}
		return resultList;
	}

	// 通过订单id/产品id，类型查找订单详情
	public HashMap<Long, List<SalesOrderDetail>> findSalesOrderDetail(
			List<Long> ids, int type) {
		HashMap<Long, List<SalesOrderDetail>> resultList = new HashMap<Long, List<SalesOrderDetail>>();
		if (type == TracingType.ORDERID) {
			for (Long id : ids) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("salesOrderId", id);
				resultList.put(id, findListByMap(SalesOrderDetail.class, map));
			}
		} else if (type == TracingType.PRODUCTID) {
			for (Long id : ids) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("productId", id);
				resultList.put(id, findListByMap(SalesOrderDetail.class, map));
			}
		}
		return resultList;
	}

	// 通过销售订单号查看生产计划
	public HashMap<String, List<ProducePlan>> findProducePlan(
			List<String> salesOrderCodeList) {
		HashMap<String, List<ProducePlan>> resultList = new HashMap<String, List<ProducePlan>>();
		for (String salesOrder : salesOrderCodeList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("salesOrderCode", salesOrder);
			resultList.put(salesOrder, findListByMap(ProducePlan.class, map));
		}
		return resultList;
	}

	// 通过生产计划id查看生产计划明细
	public HashMap<Long, List<ProducePlanDetail>> findProducePlanDetail(
			List<Long> producePlanId) {
		HashMap<Long, List<ProducePlanDetail>> resultList = new HashMap<Long, List<ProducePlanDetail>>();
		for (Long id : producePlanId) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("producePlanId", id);
			resultList.put(id, findListByMap(ProducePlanDetail.class, map));
		}
		return resultList;
	}

	// 通过批次号查询卷条码
	public HashMap<String, List<RollBarcode>> findRollBarcodes(
			List<String> batchCodes) {
		HashMap<String, List<RollBarcode>> resultList = new HashMap<String, List<RollBarcode>>();
		for (String batchCode : batchCodes) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("batchCode", batchCode);
			resultList.put(batchCode, findListByMap(RollBarcode.class, map));
		}
		return resultList;
	}

	// 通过批次号查询生产计划详情
	public HashMap<String, List<ProducePlanDetail>> findProducePlanDetailByBatchCode(
			List<String> batchCodes) {
		HashMap<String, List<ProducePlanDetail>> resultList = new HashMap<String, List<ProducePlanDetail>>();
		for (String batchCode : batchCodes) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("batchCode", batchCode);
			resultList.put(batchCode,
					findListByMap(ProducePlanDetail.class, map));
		}
		return resultList;
	}

	// 通过批次号查询编制计划
	public HashMap<String, List<WeavePlan>> findWeavePlanByBatchCode(
			List<String> batchCodes) {
		HashMap<String, List<WeavePlan>> resultList = new HashMap<String, List<WeavePlan>>();
		for (String batchCode : batchCodes) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("batchCode", batchCode);
			resultList.put(batchCode, findListByMap(WeavePlan.class, map));
		}
		return resultList;
	}

	// 通过批次号查询裁剪计划
	public HashMap<String, List<CutPlan>> findCutPlanByBatchCode(
			List<String> batchCodes) {
		HashMap<String, List<CutPlan>> resultList = new HashMap<String, List<CutPlan>>();
		for (String batchCode : batchCodes) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("batchCode", batchCode);
			resultList.put(batchCode, findListByMap(CutPlan.class, map));
		}
		return resultList;
	}
	
	public Map<String, Object> tracing(String code){
		return tracingDao.tracing(code);
	}

}

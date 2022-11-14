/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.deliveryontheway.service.impl;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.common.service.IMessageCreateService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.deliveryontheway.dao.IDeliveryOnTheWayPlanDao;
import com.bluebirdme.mes.planner.deliveryontheway.entity.DeliveryOnTheWayPlan;
import com.bluebirdme.mes.planner.deliveryontheway.entity.DeliveryOnTheWayPlanDetails;
import com.bluebirdme.mes.planner.deliveryontheway.service.IDeliveryOnTheWayPlanService;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import com.bluebirdme.mes.stock.service.IProductStockService;
import com.bluebirdme.mes.store.dao.ITrayBarCodeDao;
import com.bluebirdme.mes.store.entity.Tray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */
@Service
@AnyExceptionRollback
public class DeliveryOnTheWayPlanServiceImpl extends BaseServiceImpl implements IDeliveryOnTheWayPlanService {

    @Resource
    IDeliveryOnTheWayPlanDao deliveryPlanDao;
    @Resource
    IMessageCreateService msgCreateService;

    @Resource
    ITrayBarCodeDao trayBarCodeDao;


    @Override
    protected IBaseDao getBaseDao() {
        return deliveryPlanDao;
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageInfo = deliveryPlanDao.findPageInfo(filter, page);
        for (int i = 0; i < ((List<Map<String, Object>>) findPageInfo.get("rows")).size(); i++) {
            long deliveryId = Long.parseLong(((List<Map<String, Object>>) findPageInfo.get("rows")).get(i).get("ID".toUpperCase()).toString());
            List<Map<String, Object>> listDeliveryOnTheWayPlanDetails = deliveryPlanDao.findDeliveryOnTheWayPlanDetails(deliveryId);
            int onTheWayCount = 0;
            for (Map<String, Object> listDeliveryOnTheWayPlanDetail : listDeliveryOnTheWayPlanDetails) {
                String barcode = listDeliveryOnTheWayPlanDetail.get("BarCode".toUpperCase()).toString();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("barCode", barcode);
                ProductStockState pss = findUniqueByMap(ProductStockState.class, map);
                if (pss != null && pss.getStockState() == 3) {
                    onTheWayCount++;
                }
            }
            ((List<Map<String, Object>>) findPageInfo.get("rows")).get(i).put("onTheWayCount".toUpperCase(), onTheWayCount);
        }
        return findPageInfo;
    }

    @Override
    public void saveDeliveryPlan(String code, String warehousecode, String logisticscompany, String plate, long loginid) throws Exception {
        String[] codes = code.split(",");
        String deliveryCode = deliveryPlanDao.getSerial("YK");
        DeliveryOnTheWayPlan deliveryPlan = new DeliveryOnTheWayPlan();
        deliveryPlan.setDeliveryDate(new Date());
        deliveryPlan.setDeliveryBizUserId(loginid);
        deliveryPlan.setDeliveryCode(deliveryCode);
        deliveryPlan.setWareHouseCode(warehousecode);
        deliveryPlan.setPlate(plate);
        deliveryPlan.setAuditState(AuditConstant.RS.SUBMIT);//审核状态
        List<DeliveryOnTheWayPlanDetails> listDeliveryPlanDetails = new ArrayList<DeliveryOnTheWayPlanDetails>();
        for (String barcode : codes) {
            double weight = 0;
            Tray tray = findOne(Tray.class, "trayBarcode", barcode);
            if (tray != null && null != tray.getWeight()) {
                weight = tray.getWeight();
            }

            ProductInRecord productInRecord = findOne(ProductInRecord.class, "barCode", barcode);
            if (weight == 0 && productInRecord != null && null != productInRecord.getWeight() && null != productInRecord.getInTime()) {
                weight = productInRecord.getWeight();
            }

            List<Map<String, Object>> list = trayBarCodeDao.findSalesOrderByBarcode(barcode);
            for (Map<String, Object> m : list) {

                DeliveryOnTheWayPlanDetails deliveryPlanDetails = new DeliveryOnTheWayPlanDetails();
                deliveryPlanDetails.setBarcode(barcode);
                deliveryPlanDetails.setBatchCode(m.get("BATCHCODE") == null ? "" : m.get("BATCHCODE").toString());
                deliveryPlanDetails.setSalesOrderSubCode(m.get("SALESORDERCODE") == null ? "" : m.get("SALESORDERCODE").toString());
                deliveryPlanDetails.setProductId(Long.parseLong(m.get("SALESPRODUCTID").toString()));
                deliveryPlanDetails.setSalesOrderDetailId(Long.parseLong(m.get("SALESORDERDETAILID").toString()));
                deliveryPlanDetails.setPartName(m.get("PARTNAME") == null ? "" : m.get("PARTNAME").toString());
                deliveryPlanDetails.setWeight(weight);
                deliveryPlanDetails.setConsumerProductName("");
                deliveryPlanDetails.setFactoryProductName("");
                SalesOrderDetail salesOrderDetail = findOne(SalesOrderDetail.class, "id", Long.parseLong(m.get("SALESORDERDETAILID").toString()));
                if (salesOrderDetail != null) {
                    deliveryPlanDetails.setConsumerProductName(salesOrderDetail.getConsumerProductName());
                    deliveryPlanDetails.setFactoryProductName(salesOrderDetail.getFactoryProductName());
                }
                listDeliveryPlanDetails.add(deliveryPlanDetails);
            }
        }

        deliveryPlan.setProductDatas(listDeliveryPlanDetails);

        if (deliveryPlan.getId() == null) {
            deliveryPlanDao.save(deliveryPlan);
        } else {
            deliveryPlanDao.update(deliveryPlan);

            Map<String, Object> map = new HashMap<String, Object>();

            map.put("deliveryId", deliveryPlan.getId());

            delete(DeliveryOnTheWayPlanDetails.class, map);
        }

        // 根据出货计划订单的序号绑定产品所属的id
        for (DeliveryOnTheWayPlanDetails dpd : deliveryPlan.getProductDatas()) {
            dpd.setDeliveryId(deliveryPlan.getId());
        }
        save(deliveryPlan.getProductDatas().toArray(new DeliveryOnTheWayPlanDetails[]{}));
    }


    @Resource
    IProductStockService productStockService;

    @Override
    public List<Map<String, Object>> findDeliveryOnTheWayPlanDetails(Long deliveryId) {
        List<Map<String, Object>> listDeliveryOnTheWayPlanDetails = deliveryPlanDao.findDeliveryOnTheWayPlanDetails(deliveryId);
        for (Map<String, Object> listDeliveryOnTheWayPlanDetail : listDeliveryOnTheWayPlanDetails) {
            String barcode = listDeliveryOnTheWayPlanDetail.get("BarCode".toUpperCase()).toString();
            int stockState = -1;
            String stockStateText = "";
            Map<String, Object> map = new HashMap<>();
            map.put("barCode", barcode);
            ProductStockState pss = findUniqueByMap(ProductStockState.class, map);
            if (pss != null) {
                stockState = pss.getStockState();
                stockStateText = productStockService.GetStockState(stockState);
            }
            // 条码状态。
            listDeliveryOnTheWayPlanDetail.put("StockState".toUpperCase(), "" + stockState);
            // 条码状态。
            listDeliveryOnTheWayPlanDetail.put("StockStateText".toUpperCase(), stockStateText);
        }

        return listDeliveryOnTheWayPlanDetails;
    }

    @Override
    public List<Map<String, Object>> findProductDeliveryOnTheWayPlanDetailsByDeliveryId(Long deliveryId) {
        return deliveryPlanDao.findProductDeliveryOnTheWayPlanDetailsByDeliveryId(deliveryId);
    }

    @Override
    public Map<String, Object> findPageInfoTotalWeight(Filter filter, Page page)
            throws Exception {
        return deliveryPlanDao.findPageInfoTotalWeight(filter, page);
    }

    @Override
    public Map<String, Object> findDeliveryOnTheWayPlanDetails(Filter filter, Page page) throws Exception {
        return deliveryPlanDao.findDeliveryOnTheWayPlanDetails(filter, page);
    }
}

package com.bluebirdme.mes.mobile.stock.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.mobile.stock.dao.IMobileProductStockDao;

@Repository
public class MobileProductStockDaoImpl extends BaseDaoImpl implements IMobileProductStockDao {
    @Resource
    SessionFactory factory;

    @Override
    public String findDeliveryPlanDetail(String project, String content) throws SQLTemplateException {
        Map<String, Object> map = new HashMap();
        map.put("project", project);
        String sql = SQL.get(map, "findDeliveryPlanDetails");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("content", "%" + content + "%").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return GsonTools.toJson(query.list());
    }

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return null;
    }

    @Override
    public String findDeliveryPlanById(String id, String pn) {
        String sql = SQL.get("findDeliveryPlanById");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setParameter("pn", pn).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return GsonTools.toJson(query.list());
    }

    @Override
    public String findDeliveryPlanProductById(String id, String pn) {
        String sql = SQL.get("findDeliveryPlanProductById");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setParameter("pn", pn).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.list();
        for (Map map : list) {
//		if("0".equals(map.get("DELIVERYCOUNT").toString())){
//			map.put("DELIVERYCOUNT",map.get("DELIVERYSUITCOUNT"));
//		}
            if (!"0".equals(map.get("DELIVERYSUITCOUNT").toString())) {
                map.put("DELIVERYCOUNT", map.get("DELIVERYSUITCOUNT"));
            }
        }
        return GsonTools.toJson(list);
    }

    @Override
    public List<Map<String, Object>> searchProduct(String salesOrderSubCode, String batchCode, Long productId, Long partId) {
        String sql = "SELECT  tb.id,  tb.salesOrderCode,  tb.barcode,  tb.batchCode,  fp.consumerProductName,  fp.factoryProductName,  tb.planDeliveryDate,  ir.inTime,  ss.warehouseCode,  ss.warehousePosCode,  t.weight,  t1.consumername,  fp.productModel,  tbp.deliveryDate,  tbp.newSalesOrderCode,  tbp.newBatchCode,  tbp.newProductModel,  tbp.newConsumer,  fp.productShelfLife FROM  hs_tray t LEFT JOIN hs_tray_barcode tb ON tb.barcode = t.trayBarcode LEFT JOIN hs_finishproduct fp ON fp.id = tb.salesProductId LEFT JOIN hs_product_stock_state ss ON ss.barCode = t.trayBarcode LEFT JOIN hs_turnbag_plan tbp ON tbp.id = tb.turnBagPlanId LEFT JOIN hs_product_in_record ir ON ir.barCode = t.trayBarcode LEFT JOIN hs_TotalStatistics t1 ON t1.rollBarcode = t.trayBarcode WHERE  (   ss.stockState = 1   OR ss.stockState IS NULL  ) AND (  tb.isOpened = 0  OR tb.isOpened = NULL ) AND ss.warehouseCode IS NOT NULL AND tb.salesProductId =:salesProductId AND (  (   tb.salesOrderCode LIKE :salesCode   AND tb.belongToSalesOrderId IS NULL  )  OR tbp.newSalesOrderCode LIKE :salesCode ) AND (  (   tb.batchCode LIKE :batchCode   AND tb.belongToSalesOrderId IS NULL  )  OR tbp.newBatchCode LIKE :batchCode )";
        if (partId != null) {
            sql += " AND tb.partId =:partId";
        }
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("salesCode", salesOrderSubCode);
        query.setParameter("batchCode", batchCode);
        query.setParameter("salesProductId", productId);
        if (partId != null) {
            query.setParameter("partId", partId);
        }
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findTrayByBarCode(String barcode) throws Exception {
        String sql = SQL.get("findTrayByBarCode");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("barcode", barcode);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findPartByBarCode(String barcode) throws Exception {
        String sql = SQL.get("findPartByBarCode");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("barcode", barcode);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }
}

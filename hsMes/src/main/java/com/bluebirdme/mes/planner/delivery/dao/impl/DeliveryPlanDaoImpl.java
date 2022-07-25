/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.delivery.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.planner.delivery.dao.IDeliveryPlanDao;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class DeliveryPlanDaoImpl extends BaseDaoImpl implements IDeliveryPlanDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "deliveryPlan-list");
    }

    @Override
    public <T> Map<String, Object> findTcPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "tcDeliveryPlan-list");
    }

    @Override
    public List<Map<String, Object>> findDeliveryCode() {
        SQLQuery query = getSession().createSQLQuery(SQL.get("findDeliveryCode-list"));
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> getDeliveryProducts(Long deliveryId) {
        SQLQuery query = getSession().createSQLQuery(SQL.get("findDeliveryPlanDetailsById"));
        query.setParameter("id", deliveryId);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public String getSerial(String type) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String preffix = type + sdf.format(now);
        DeliveryPlan dp = (DeliveryPlan) getSession().createQuery(SQL.get("delivery-code")).setParameter("code", preffix + "%").setMaxResults(1).uniqueResult();
        System.out.println(type);
        if (dp == null) {
            System.out.println("NULL");
            return preffix + "0001";
        } else {
            String suffix = "0000" + (Integer.parseInt(dp.getDeliveryCode().replace(preffix, "")) + 1);
            return preffix + suffix.substring(suffix.length() - 4);
        }
    }

    @Override
    public <T> Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception {
        return findPageInfo(filter, page, "finishProduct-list-delivery-tc");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBatchCodeCountBySalesOrderCode(String salesOrderCode, Long productId, Long partId) {
        String sql = null;
//		if(isNew){
//			sql=SQL.get("turnbag-batchcode-count-new");
//			return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("code", salesOrderCode).setParameter("productId", productId).list();
//		}
        List<Map<String, Object>> list_all = null;
        if (partId == null) {
            sql = "SELECT t.batchCode,count(1) as count FROM hs_tray_barcode t WHERE t.salesProductId=:productId and t.belongToSalesOrderId is null AND t.salesOrderCode =:code and t.partId is null";
            list_all = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("code", salesOrderCode).setParameter("productId", productId).list();

        } else {
            sql = SQL.get("delivery-batchcode-count-all");
            list_all = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("code", salesOrderCode).setParameter("productId", productId).setParameter("partId", partId).list();

        }

        List<Map<String, Object>> list_out = null;
        if (partId == null) {
            sql = "SELECT t.batchCode,count(1) as count FROM hs_tray_barcode t LEFT JOIN hs_product_stock_state s on s.barcode=t.barcode WHERE t.salesProductId=:productId and t.belongToSalesOrderId is null AND t.salesOrderCode =:code and s.stockState=-1 and (t.partId is null ) group by t.batchCode";
            list_out = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("code", salesOrderCode).setParameter("productId", productId).list();

        } else {
            sql = SQL.get("delivery-batchcode-count-out");
            list_out = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("code", salesOrderCode).setParameter("productId", productId).setParameter("partId", partId).list();

        }

        for (Map<String, Object> m1 : list_all) {
            for (Map<String, Object> m2 : list_out) {
                if (m2.get("BATCHCODE").toString().equals(m1.get("BATCHCODE").toString())) {
                    m1.put("COUNT", ((BigInteger) m1.get("COUNT")).intValue() - ((BigInteger) m2.get("COUNT")).intValue());
                    break;
                }
            }
        }

        return list_all;
    }

    @Override
    public List<Map<String, Object>> searchProduct(String salesOrderSubCode, String batchCode, Long productId, Long partId) {
        String sql = "SELECT  tb.id,  tb.salesOrderCode,  tb.barcode,tp.tcProcBomVersionPartsName, tb.batchCode, fp.consumerProductName" +
                ",  fp.factoryProductName,  tb.planDeliveryDate,  ir.inTime,  ss.warehouseCode,  ss.warehousePosCode,hw.warehouseName,  t.weight,  t1.consumername," +
                "  fp.productModel,  tbp.deliveryDate,  tbp.newSalesOrderCode,  tbp.newBatchCode,  tbp.newProductModel,  tbp.newConsumer," +
                "  fp.productShelfLife FROM  hs_tray t LEFT JOIN hs_tray_barcode tb ON tb.barcode = t.trayBarcode " +
                "LEFT JOIN HS_TC_PROC_BOM_VERSION_PARTS tp ON tb.partId = tp.id LEFT JOIN hs_finishproduct fp ON fp.id " +
                "= tb.salesProductId LEFT JOIN hs_product_stock_state ss ON ss.barCode = t.trayBarcode " +
                "LEFT JOIN hs_warehouse hw on ss.warehouseCode = hw.warehouseCode" +
                " LEFT JOIN hs_turnbag_plan tbp" +
                " ON tbp.id = tb.turnBagPlanId LEFT JOIN hs_product_in_record ir ON ir.barCode = t.trayBarcode LEFT JOIN hs_TotalStatistics" +
                " t1 ON t1.rollBarcode = t.trayBarcode WHERE  (   ss.stockState = 1   OR ss.stockState IS NULL  ) AND (  tb.isOpened = 0  OR tb.isOpened = NULL )" +
                " AND ss.warehouseCode IS NOT NULL AND tb.salesProductId =:salesProductId AND (  (   tb.salesOrderCode LIKE :salesCode   AND tb.belongToSalesOrderId IS NULL  )" +
                "  OR tbp.newSalesOrderCode LIKE :salesCode ) AND (  (   tb.batchCode LIKE :batchCode   AND tb.belongToSalesOrderId IS NULL  )  OR tbp.newBatchCode LIKE :batchCode )";
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
        List<Map<String, Object>> trayList = query.list();

        String partSql = "SELECT tb.id, tb.salesOrderCode,tb.barcode,tb.partName" +
                ",tb.batchCode,fp.consumerProductName,fp.factoryProductName" +
                ",'' AS planDeliveryDate,ir.inTime,ss.warehouseCode,ss.warehousePosCode,hw.warehouseName" +
                ",t.rollWeight AS weight,t1.consumername,fp.productModel,'' AS deliveryDate" +
                ",tb.salesOrderCode AS newSalesOrderCode,tb.barcode AS newBatchCode,'' AS newProductModel" +
                ",'' AS newConsumer,fp.productShelfLife FROM hs_roll t LEFT JOIN hs_part_barcode tb ON tb.barcode " +
                "= t.partBarcode LEFT JOIN hs_finishproduct fp ON fp.id = tb.salesProductId LEFT JOIN hs_product_stock_state " +
                "ss ON ss.barCode = t.partBarcode LEFT JOIN hs_warehouse hw ON ss.warehouseCode = hw.warehouseCode LEFT JOIN hs_product_in_record ir ON ir.barCode = t.partBarcode LEFT JOIN hs_TotalStatistics" +
                " t1 ON t1.rollBarcode = t.partBarcode WHERE(ss.stockState = 1 OR ss.stockState IS NULL) AND ss.warehouseCode IS NOT NULL AND" +
                " tb.salesProductId =:salesProductId AND tb.salesOrderCode LIKE :salesCode AND tb.batchCode LIKE :batchCode";
        if (partId != null) {
            partSql += " AND tb.partId =:partId";
        }
        SQLQuery partQuery = getSession().createSQLQuery(partSql);
        partQuery.setParameter("salesCode", salesOrderSubCode);
        partQuery.setParameter("batchCode", batchCode);
        partQuery.setParameter("salesProductId", productId);
        if (partId != null) {
            partQuery.setParameter("partId", partId);
        }
        partQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> partList = partQuery.list();
        trayList.addAll(partList);

        return trayList;
    }

    @Override
    public List<Map<String, Object>> searchAuditer(String entityJavaClass, Long formId) {
        String sql = "SELECT pu1.userName firstRealAuditUserName,pu2.userName secondRealAuditUserName from hs_audit_instance hai LEFT JOIN platform_user pu1 on pu1.id=hai.firstRealAuditUserId LEFT JOIN platform_user pu2 on pu2.id=hai.secondRealAuditUserId where hai.entityJavaClass=:entityJavaClass and hai.formId=:formId";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("formId", formId);
        query.setParameter("entityJavaClass", entityJavaClass);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findProductOutRecordByPackingNumber(String packingNumber) {
        SQLQuery query = getSession().createSQLQuery(SQL.get("findProductOutRecordByPackingNumber"));
        query.setParameter("packingNumber", packingNumber);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<String> cars() {
        SQLQuery query = getSession().createSQLQuery("SELECT distinct UPPER(REPLACE(TRIM(o.plate),' ','')) carNo from hs_delivery_plan_salesorders o where isBlank(plate)=0");
        return query.list();
    }

    @Override
    public int getxdl(String salesOrderCode, String pch) {
        SQLQuery query = getSession().createSQLQuery(SQL.get("findDeliveryPlanCountByIdPch"));
        query.setParameter("salesOrderCode", salesOrderCode).setParameter("pch", pch);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.list();
        int xdl = 0;
        if (list.size() > 0) {
            if (list.get(0).get("SLP") != null) {
                xdl = Integer.parseInt(list.get(0).get("SLP").toString());
            } else if (list.get(0).get("SL") == null && list.get(0).get("SLP") == null) {
                xdl = 0;
            } else if (list.get(0).get("SL") != null && list.get(0).get("SLP") == null) {
                xdl = Integer.parseInt(list.get(0).get("SL").toString());
            }
        }
        return xdl;
    }

    @Override
    public int getkcl(String id, String pch, String cnmc, String bjmc) {
        // TODO Auto-generated method stub
        String sql = "select t.* from (SELECT ppd.totalTrayCount,ROUND(t.weight, 2) AS weight,tb.salesOrderCode,tb.barcode,tb.batchCode,tb.planDeliveryDate,ss.warehouseCode,ss.warehouseCode AS type,ss.warehousePosCode,ss.stockState,t.inTime,t.rollQualityGradeCode,ppd.consumerName,ts.isLocked, ppd.productModel,"
                + "  ppd.consumerProductName,ppd.factoryProductName,ppd.processBomCode PRODUCTPROCESSCODE,ppd.processBomVersion PRODUCTPROCESSBOMVERSION,ppd.bcBomCode PRODUCTPACKAGINGCODE, ppd.bcBomVersion PRODUCTPACKAGEVERSION, tp.tcProcBomVersionPartsName,  fpc.categoryCode,fpc.categoryName,t.rollCountInTray,ppd.salesOrderSubcodePrint FROM"
                + " hs_product_stock_state ss LEFT JOIN hs_tray t ON t.trayBarcode = ss.barCode LEFT JOIN hs_tray_barcode tb ON tb.barcode = ss.barCode LEFT JOIN hs_produce_plan_detail ppd on tb.producePlanDetailId=ppd.id LEFT JOIN hs_tc_proc_bom_version_parts tp on tb.partId=tp.id"
                + " LEFT JOIN hs_finishproduct fp on fp.id=tb.salesProductId LEFT JOIN hs_finished_product_category fpc on fpc.id=fp.fpcid LEFT JOIN hs_totalstatistics ts on ts.rollBarcode=ss.barCode where ss.stockState=1 and tb.isOpened=0 and ss.warehouseCode like 'cp%') t"
                + " where t.SALESORDERCODE='" + id + "' and t.BATCHCODE='" + pch + "' and t.FACTORYPRODUCTNAME='" + cnmc + "' and ifnull(t.TCPROCBOMVERSIONPARTSNAME,'')='" + bjmc + "'";

        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list.size();
    }

    @Override
    public int getfhl(String id, String pch, String cnmc, String bjmc) {
        // TODO Auto-generated method stub
        String sql = "select t.*,t2.WAREHOUSENAME,u.userName as OPERATEUSERNAME,t1.CONSUMERNAME,t1.batchCode, 	t1.productWeight  as rollWeight,t1.salesOrderCode,t1.rollQualityGradeCode from HS_Product_Out_Record t "
                + " left join platform_user u on u.id=t.operateUserId left join hs_TotalStatistics t1 on t1.rollBarcode = t.barCode  left join hs_warehouse t2 on t2.WAREHOUSECODE=t.warehouseCode "
                + " LEFT JOIN HS_Delivery_Plan t3 on t.deliveryCode=t3.deliveryCode and IFNULL(t3.isClosed,0) <> 1"
                + " where t1.salesOrderCode='" + id + "' and t1.batchCode = '" + pch + "' and t.productFactoryName='" + cnmc + "'";

        //String sql = "select sum(deliveryCount) as fhl from HS_Delivery_Plan_Details where salesOrderSubCode='" + id + "' and batchCode = '" + pch + "' and factoryProductName='" + cnmc + "' and ifnull(partName,'')='" + bjmc + "'";
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list.size();
    }

    @Override
    public int getOrderFhl(String salesOrderCode) {
        // TODO Auto-generated method stub
        String sql = "select * from  hs_TotalStatistics  where salesOrderCode='" + salesOrderCode + "' and  barcodeType in ('part','tray') and state=-1  ";
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list.size();
    }

    @Override
    public int getDetailPlanOrderFhl(String salesOrderCode, String batchCode) {
        // TODO Auto-generated method stub
        String sql = "select * from  hs_TotalStatistics  where salesOrderCode='" + salesOrderCode + "' and  batchCode='" + batchCode + "' and  barcodeType in ('part','tray') and state=-1 ";
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list.size();
    }


    @Override
    public int getdjs(String id, String pch) {
        // TODO Auto-generated method stub
        String sql = "select * from hs_TotalStatistics where salesOrderCode='" + id + "' and batchCode = '" + pch + "' and isLocked=1";
        List<Map<String, Object>> list = getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list.size();
    }

    @Override
    public List<Map<String, Object>> QueryProductWareHouse(long deliveryId) {
        String sql = SQL.get("QueryProductWareHouse");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("deliveryId", deliveryId);
        List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    @Override
    public List<Map<String, Object>> findDeliverySlipMirror(Long id) {
        String sql = SQL.get("QueryDeliverySlipMirror");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("productOutOrderId", id);
        List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    @Override
    public List<Map<String, Object>> findDeliverySlip(long id) {
        String sql = SQL.get("QueryDeliverySlip");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("productOutOrderId", id);
        List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }
}

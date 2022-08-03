/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.dao.impl;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.sales.dao.ISalesOrderDao;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class SalesOrderDaoImpl extends BaseDaoImpl implements ISalesOrderDao {
    @Resource
    SessionFactory factory;

    @Override
    public Session getSession() {
        return factory.getCurrentSession();
    }

    @Override
    public Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "salesOrder-list");
    }

    @Override
    public Map<String, Object> findPageInfo1(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "salesOrder-list-peibu");
    }

    public Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception {
        return this.findPageInfo(filter, page, "salesOrder-list2");
    }

    public void deleteDetails(Long salesId) {
        getSession().createQuery("delete from SalesOrderDetail where salesOrderId=:salesId").setParameter("salesId", salesId).executeUpdate();
    }

    public List<SalesOrderDetail> getDetails(Long salesOrderId) {
        return getSession().createQuery("from SalesOrderDetail where salesOrderId=:salesId").setParameter("salesId", salesOrderId).list();
    }

    @Override
    public List<SalesOrderDetail> findDetailByCode(String orderCode) throws SQLTemplateException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("orderCode", orderCode);
        String sql = SQL.get(map, "salesOrder-list-code");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.aliasToBean(SalesOrderDetail.class));
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("productId", StandardBasicTypes.LONG);
        query.addScalar("salesOrderId", StandardBasicTypes.LONG);
        query.addScalar("productWidth", StandardBasicTypes.DOUBLE);
        query.addScalar("productRollLength", StandardBasicTypes.DOUBLE);
        query.addScalar("productRollWeight", StandardBasicTypes.DOUBLE);
        query.addScalar("productCount", StandardBasicTypes.DOUBLE);
        query.addScalar("produceCount", StandardBasicTypes.DOUBLE);
        query.addScalar("packagingCount", StandardBasicTypes.DOUBLE);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findDetailByCode2(String orderCode) throws SQLTemplateException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("orderCode", orderCode);
        String sql = SQL.get(map, "salesOrder-list-code");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    public List<Map<String, Object>> getOrderPartCount(Long id) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return findListMapByMap(SQL.get("order-part-count"), map);
    }

    public List<SalesOrder> findUncreatePlans() {
        return getSession().createQuery(SQL.get("salesOrder-unCreatePlans")).list();
    }

    public String getSerial(Integer export) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String preffix;
        if (export == 1) {
            preffix = "HSNXD" + sdf.format(now);
        } else if (export == 0) {
            preffix = "HSWXD" + sdf.format(now);
        } else {
            preffix = "HSCJD" + sdf.format(now);
        }

        SalesOrder order = (SalesOrder) getSession().createQuery(SQL.get("salesOrder-serial")).setParameter("code", "%" + preffix + "%").setMaxResults(1).uniqueResult();
        String suffix = "0000";
        if (order == null) {
            suffix += 1;
        } else {
            String s = order.getSalesOrderCode().substring(11);
            s = s.split("-")[0];
            suffix += (Integer.parseInt(s) + 1);
        }
        System.out.println(preffix + suffix);
        return preffix + suffix.substring(3);
    }


    @Override
    public void update(String id) {
        getSession().createSQLQuery(SQL.get("updateFinish-list")).setParameter("id", id).executeUpdate();
    }


    @Override
    public String getCode(Integer salesOrderIsExport, Date salesOrderDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String preffix = "";
        if (salesOrderIsExport.intValue() == 1) {
            preffix = "HSNXD" + sdf.format(salesOrderDate);
        } else if (salesOrderIsExport.intValue() == 0) {
            preffix = "HSWXD" + sdf.format(salesOrderDate);
        } else {
            preffix = "HSCJD" + sdf.format(salesOrderDate);
        }

        SalesOrder order = (SalesOrder) getSession().createQuery(SQL.get("salesOrder-serial")).setParameter("code", "%" + preffix + "%").setMaxResults(1).uniqueResult();
        String suffix = "0000";
        if (order == null) {
            suffix += 1;
        } else {
            String s = order.getSalesOrderCode().substring(11);
            s = s.split("-")[0];
            suffix += (Integer.parseInt(s) + 1);
        }
        System.out.println(preffix + suffix);
        return preffix + suffix.substring(3);
    }

    @Override
    public Map<String, Object> countRollsAndTrays(Long salesOrderDetailId) {
        return (Map<String, Object>) getSession().createSQLQuery(SQL.get("count-order-rolls-trays")).setParameter("id", salesOrderDetailId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
    }

    @Override
    public Map<String, Object> findPageOut(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "salesOrderOut");
    }

    @Override
    public Map<String, Object> findPageQuantity(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "salesQuantity");
    }

    @Override
    public Map<String, Object> findPageSummaryMonthly(Filter filter, Page page) {
        return this.findPageInfo(filter, page, "summaryMonthly");
    }

    public String hasPackTask(Long salesOrderId) {
        String sql = "SELECT " +
                "	GROUP_CONCAT(salesOrderSubCode) " +
                "FROM " +
                "	hs_sales_order_detail " +
                "WHERE " +
                "	id IN ( " +
                "		SELECT " +
                "			x.id " +
                "		FROM " +
                "			( " +
                "				SELECT " +
                "					id " +
                "				FROM " +
                "					hs_sales_order_detail _sod " +
                "				WHERE _sod.productIsTc=2 and " +
                "					_sod.salesOrderId =  " + salesOrderId +
                "			) x " +
                "		LEFT JOIN ( " +
                "			SELECT " +
                "				id, " +
                "				sodId " +
                "			FROM " +
                "				hs_pack_task pt " +
                "			WHERE " +
                "				pt.sodId IN ( " +
                "					SELECT " +
                "						id " +
                "					FROM " +
                "						hs_sales_order_detail sod " +
                "					WHERE sod.productIsTc=2 and " +
                "						sod.salesOrderId = " + salesOrderId +
                "				) " +
                "		) y ON x.id = y.sodId " +
                "		WHERE " +
                "			y.sodId IS NULL " +
                "		GROUP BY " +
                "			x.id " +
                "	)";

        Object v = getSession().createSQLQuery(sql).uniqueResult();
        return v == null ? "" : v.toString();
    }

    public void setAllocated(String ids) {
        String sql = "UPDATE hs_sales_order_detail SET ALLOCATECOUNT = CASE WHEN totalWeight IS NULL THEN 0.1 WHEN totalWeight = 0 THEN 0.1 ELSE totalWeight END, totalWeight = CASE WHEN totalWeight IS NULL THEN 0 ELSE totalWeight END where id in (" + ids + ") ";
        getSession().createSQLQuery(sql).executeUpdate();
    }

    @Override
    public List<Map<String, Object>> findOrder() {
        String sql = SQL.get("salesOrder-list-mirror");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findDetailByCodes(String orderCode) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderCode", orderCode);
        String sql = SQL.get(map, "salesOrder-list-code");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findSalesOrder(String data) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", data);
        String sql = SQL.get(map, "salesOrderMirror-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findD(String id) {
        String sql = SQL.get("findD-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findFV(String id) {
        String sql = SQL.get("findFV-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findTV(String id) {
        String sql = SQL.get("findTV-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findFBom(String id) {
        String sql = SQL.get("findFBom-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findTBom(String id) {
        String sql = SQL.get("findTBom-list");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findP(String id) {
        String sql = SQL.get("findP-mirrorList");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> findListByMap1(Long id) {
        String sql = SQL.get("find-salesOrderListById");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List<Map<String, Object>> selectSalesOrder(String salesOrderSubCode, String factoryProductName) {
        String sql = SQL.get("find-selectSalesOrder");
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setParameter("factoryProductName", factoryProductName).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("salesOrderSubCode", salesOrderSubCode).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }
}

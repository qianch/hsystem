/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.mobile.common.dao.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomMirror;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.mobile.common.dao.IMobileDao;

/**
 * 
 * @author 高飞
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class MobileDaoImpl extends BaseDaoImpl implements IMobileDao {

	@Resource
	SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}

	public BigInteger isCount(String barCode, Integer productWeigh) {
		return (BigInteger) getSession().createSQLQuery(SQL.get("mobile-count")).setParameter("barCode", barCode)
				.setParameter("productWeigh", productWeigh).uniqueResult();
	}

	public Double isAvg(String barCode, Integer productWeigh) {
		return (Double) getSession().createSQLQuery(SQL.get("mobile-avg")).setParameter("barCode", barCode)
				.setParameter("productWeigh", productWeigh).uniqueResult();
	}

	public BigInteger isCount1(String batchcode, String producePlanCode, String factoryproductname) {
		return (BigInteger) getSession().createSQLQuery(SQL.get("mobile-count1")).setParameter("batchcode", batchcode)
				.setParameter("producePlanCode", producePlanCode).setParameter("factoryproductname", factoryproductname)
				.uniqueResult();
	}

	public String getOrderCnt(String salesOrderCode, String batchCode) {
		String sql = "SELECT sum(totalTrayCount) as sl from HS_Produce_Plan_Detail where salesOrderCode='"
				+ salesOrderCode + "' and batchCode = '" + batchCode
				+ "' and ifnull(closed,0) = 0 and isTurnBagPlan='生产'";
		Object obj = getSession().createSQLQuery(sql).uniqueResult();
		if (obj != null)
			return obj.toString();
		return "0";
	}

	public String getStrockCnt(String salesOrderCode, String batchCode, String factoryProductName, String bjmc) {
		String sql = "select count(*) from (SELECT ppd.totalTrayCount,ROUND(t.weight, 2) AS weight,tb.salesOrderCode,tb.barcode,tb.batchCode,tb.planDeliveryDate,ss.warehouseCode,ss.warehouseCode AS type,ss.warehousePosCode,ss.stockState,t.inTime,t.rollQualityGradeCode,ppd.consumerName,ts.isLocked, ppd.productModel,"
				+ "  ppd.consumerProductName,ppd.factoryProductName,ppd.processBomCode PRODUCTPROCESSCODE,ppd.processBomVersion PRODUCTPROCESSBOMVERSION,ppd.bcBomCode PRODUCTPACKAGINGCODE, ppd.bcBomVersion PRODUCTPACKAGEVERSION, tp.tcProcBomVersionPartsName,  fpc.categoryCode,fpc.categoryName,t.rollCountInTray,ppd.salesOrderSubcodePrint FROM"
				+ " hs_product_stock_state ss LEFT JOIN hs_tray t ON t.trayBarcode = ss.barCode LEFT JOIN hs_tray_barcode tb ON tb.barcode = ss.barCode LEFT JOIN hs_produce_plan_detail ppd on tb.producePlanDetailId=ppd.id LEFT JOIN hs_tc_proc_bom_version_parts tp on tb.partId=tp.id"
				+ " LEFT JOIN hs_finishproduct fp on fp.id=tb.salesProductId LEFT JOIN hs_finished_product_category fpc on fpc.id=fp.fpcid LEFT JOIN hs_totalstatistics ts on ts.rollBarcode=ss.barCode where ss.stockState=1 and tb.isOpened=0 and ss.warehouseCode like 'cp%') t"
				+ " where t.SALESORDERCODE='" + salesOrderCode + "' and t.BATCHCODE='" + batchCode
				+ "' and t.FACTORYPRODUCTNAME='" + factoryProductName + "' and ifnull(t.TCPROCBOMVERSIONPARTSNAME,'')='"
				+ bjmc + "'";		
		Object obj = getSession().createSQLQuery(sql).uniqueResult();
		if (obj != null)
			return obj.toString();
		return "0";
	}

	@Override
	public String getPackageCnt(String salesOrderCode, String batchCode) {
		String sql = "select sum(a.totalTrayCount) as kcl from hs_produce_plan_detail a "
				+ "LEFT JOIN hs_tray_barcode b on a.id=b.producePlanDetailId "
				+ "LEFT JOIN HS_Product_In_Record c on b.barcode=c.barcode  "
				+ "LEFT JOIN hs_TotalStatistics d on b.barcode=d.rollBarcode where a.salesOrderCode='" + salesOrderCode
				+ "' and a.batchCode = '" + batchCode + "' and d.ISPACKED=1";
		Object obj = getSession().createSQLQuery(sql).uniqueResult();
		if (obj != null)
			return obj.toString();
		return "0";
	}
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.bluebirdme.mes.stock.entity.ProductInRecord;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.ArrayUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.stock.dao.IProductStockDao;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import com.bluebirdme.mes.stock.entity.StockMove;

/**
 *
 * @author 徐波
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class ProductStockDaoImpl extends BaseDaoImpl implements IProductStockDao {

	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();

		String sql=SQL.get(filter.getFilter(), "productStock-list");
		if (page.getAll() == 0) {
			sql+=" limit "+(page.getPage() - 1) * page.getRows()+","+page.getRows();
		}
		SQLQuery dataQuery = getSession().createSQLQuery(sql);
		Iterator<Entry<String, String>> it=filter.getFilter().entrySet().iterator();
		Entry<String, String> entry=null;
		while(it.hasNext()){
			entry=it.next();
			if(ArrayUtils.contains(SQL.IGNORE_KEYS, entry.getKey()))continue;
			if(entry.getValue().startsWith("in:")){
				dataQuery.setParameterList(entry.getKey(), entry.getValue().substring(3).split(","));
			}else if(entry.getValue().startsWith("like:")){
				dataQuery.setParameter(entry.getKey(), "%"+entry.getValue().substring(5)+"%");
			}else{
				dataQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}

		dataQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		map.put("rows", dataQuery.list());

		if(page.getTotalRows()==0){
			SQLQuery totalQuery=getSession().createSQLQuery(SQL.get(filter.getFilter(),"productStock-list-count"));
			Iterator<Entry<String, String>> totalIt=filter.getFilter().entrySet().iterator();
			Entry<String, String> totalEntry=null;
			while(totalIt.hasNext()){
				totalEntry=totalIt.next();
				if(ArrayUtils.contains(SQL.IGNORE_KEYS, totalEntry.getKey()))continue;
				if(totalEntry.getValue().startsWith("in:")){
					totalQuery.setParameterList(totalEntry.getKey(), totalEntry.getValue().substring(3).split(","));
				}else if(totalEntry.getValue().startsWith("like:")){
					totalQuery.setParameter(totalEntry.getKey(), "%"+totalEntry.getValue().substring(5)+"%");
				}else{
					totalQuery.setParameter(totalEntry.getKey(), totalEntry.getValue());
				}
			}
			map.put("total", totalQuery.uniqueResult());
		}else{
			map.put("total", page.getTotalRows());
		}

		return map;
	}


	public <T> Map<String, Object> stockView(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"productStock-view");
	}

	@Override
	public <T> Map<String, Object> stockViewNew(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"productStock-view-new");
	}

	@Override
	public <T> Map<String, Object> stockViewNewPcj(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"productStock-view-new-PCJ");
	}


	public List<Map<String, Object>> findProductStockInfo(String warehouseCode,String warehousePosCode) throws SQLTemplateException{
		String sql=SQL.get("findProductStrockByCode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("warehouseCode", warehouseCode);
		query.setParameter("warehousePosCode", warehousePosCode);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	public List<Map<String, Object>> findWarhourse(String salesOrderCode, String batchCode, String productModel) throws Exception{
		String sql=SQL.get("findPendingWarhourseByCode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("salesOrderCode", salesOrderCode);
		query.setParameter("batchCode", batchCode);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setMaxResults(1);
		return query.list();
	}


	@Override
	public List<Map<String, Object>> findRoll(String code) {
		String sql = SQL.get("findrRoll-list");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("code",code)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findTray(String code) {
		String sql = SQL.get("findtTray-list");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("code",code)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findMaterial(String code) {
		String sql = SQL.get("findMaterialS-list");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("code",code)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findProductInfo(String trayCode, String boxCode, String rollCode) {
		String sql = SQL.get("findProductListByBarcode");
		SQLQuery query = getSession().createSQLQuery(sql);
		if(!StringUtils.isBlank(trayCode)){
			query.setParameter("barcode",trayCode).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		}else if(!StringUtils.isBlank(boxCode)){
			query.setParameter("barcode",boxCode).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		}else{
			query.setParameter("barcode",rollCode).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		}
		return query.list();
	}

	@Override
	public <T> Map<String, Object> warehouseDetail(Filter filter, Page page) {
		return this.findPageInfo(filter, page, "warehouseDetail-list");
	}

	@Override
	public <T> Map<String, Object> summaryDetail(Filter filter, Page page) {
		return this.findPageInfo(filter, page, "summaryDetail-list");
	}

	@Override
	public <T> Map<String, Object> comparisonDetail(Filter filter, Page page) {
		return this.findPageInfo(filter, page, "comparisonDetail-list");
	}

	@Override
	public <T> Map<String, Object> getGreigeStockInfo(Filter filter, Page page) {
		return this.findPageInfo(filter, page, "greigeStock-list");
	}

	@Override
	public List<Map<String, Object>> getMoveInfoBybarcode(String barcode)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql = "SELECT t.*,a.userName,wh.WAREHOUSENAME from  hs_stock_move t  left join platform_user a on t.moveUserId=a.id LEFT JOIN hs_warehouse wh ON wh.WAREHOUSECODE = t.originWarehouseCode where t.barcode = '" + barcode + "' ORDER BY moveTime desc LIMIT 1";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	/**
		* 移库记录
		* @param filter
		* @param page
		* @param <T>
		* @return
		*/
	@Override
	public <T> Map<String, Object> findPageInfoMoveList(Filter filter, Page page) {
		return this.findPageInfo(filter,page,"moveInfo-list");
	}

	@Override
	public List<Map<String, Object>> findPendingWarhourse(String salesOrderCode, String batchCode, String productModel) {
		String sql=SQL.get("findWarhourseByCode");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("salesOrderCode", salesOrderCode);
		query.setParameter("batchCode", batchCode);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setMaxResults(1);
		return query.list();
	}

	/**
	 * 移库记录
	 * @param filter
	 * @param page
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> Map<String, Object> moveInfolist(Filter filter, Page page) {
		return this.findPageInfo(filter,page,"moveInfolist");
	}
}

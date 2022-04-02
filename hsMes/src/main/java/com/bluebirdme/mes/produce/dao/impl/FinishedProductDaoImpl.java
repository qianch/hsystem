/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.produce.dao.IFinishedProductDao;

/**
 *
 * @author 宋黎明
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class FinishedProductDaoImpl extends BaseDaoImpl implements IFinishedProductDao {

	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return findPageInfo(filter, page,"finishProduct-list");
	}

	public <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception {
		return findPageInfo(filter, page,"finishProduct-list-all");
	}

	@Override
	public <T> Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception {
		return findPageInfo(filter, page,"finishProduct-list-delivery");
	}

	public void delete(String ids) {
		getSession().createSQLQuery(SQL.get("finishProduct-delete")).setParameterList("id", ids.split(",")).executeUpdate();
	}

	@Override
	public List<Map<String, Object>> findTcBom(String data) throws SQLTemplateException{
		if(StringUtils.isBlank(data)){
			data="";
		}
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("data", data);
		String sql=SQL.get(map, "bomTc-list");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findFtcBom(String data) throws SQLTemplateException {
		if(StringUtils.isBlank(data)){
			data="";
		}
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("data", data);
		String sql=SQL.get(map, "bomFtc-list");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<Map<String, Object>> findBcBom(String data) throws SQLTemplateException {
		if(StringUtils.isBlank(data)){
			data="";
		}
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("data", data);
		String sql=SQL.get(map, "bomBc-list");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public List<FtcBomVersion> findFtcV(Long id) {
		String sql=SQL.get("bomV-list");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(FtcBomVersion.class));
		query.addScalar("id", StandardBasicTypes.LONG);
		query.addScalar("ftcProcBomId", StandardBasicTypes.LONG);
		query.addScalar("ftcProcBomVersionCode", StandardBasicTypes.STRING);
		query.addScalar("ftcProcBomVersionEnabled", StandardBasicTypes.INTEGER);
		query.addScalar("ftcProcBomVersionDefault", StandardBasicTypes.INTEGER);
		query.addScalar("auditState", StandardBasicTypes.INTEGER);
		return query.list();
	}

	public List<TcBomVersion> findTcV(Long id) {
		String sql=SQL.get("bomTV-list");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(TcBomVersion.class));
		query.addScalar("id", StandardBasicTypes.LONG);
		query.addScalar("tcProcBomId", StandardBasicTypes.LONG);
		query.addScalar("tcProcBomVersionCode", StandardBasicTypes.STRING);
		query.addScalar("tcProcBomVersionEnabled", StandardBasicTypes.INTEGER);
		query.addScalar("tcProcBomVersionDefault", StandardBasicTypes.INTEGER);
		query.addScalar("auditState", StandardBasicTypes.INTEGER);
		return query.list();
	}

	public List<BCBomVersion> findBcV(Long id) {
		String sql=SQL.get("bomBV-list");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(BCBomVersion.class));
		query.addScalar("id", StandardBasicTypes.LONG);
		query.addScalar("packBomId", StandardBasicTypes.LONG);
		query.addScalar("packVersion", StandardBasicTypes.STRING);
		query.addScalar("packEnabled", StandardBasicTypes.INTEGER);
		query.addScalar("packIsDefault", StandardBasicTypes.INTEGER);
		return query.list();
	}

	@Override
	public void updates(Long id) {
		getSession().createSQLQuery(SQL.get("update-list")).setParameter("id", id).executeUpdate();

	}

	/**
	 * 产成品汇总（按成品类别）
	 */
	@Override
	public Map<String, Object> productsSummary(Filter filter, Page page) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		String sql=SQL.get(filter.getFilter(), "products-category-list");
		if (page.getAll() == 0) {
			sql+=" limit "+(page.getPage() - 1) * page.getRows()+","+page.getRows();
		}
		SQLQuery dataQuery = getSession().createSQLQuery(sql);
		Iterator<Entry<String, String>> it=filter.getFilter().entrySet().iterator();
		Entry<String, String> entry=null;
		while(it.hasNext()){
			entry=it.next();
			if(!entry.getKey().startsWith("_language")){
				if(entry.getValue().startsWith("in:")){
					dataQuery.setParameterList(entry.getKey(), entry.getValue().substring(3).split(","));
				}else if(entry.getValue().startsWith("like:")){
					dataQuery.setParameter(entry.getKey(), "%"+entry.getValue().substring(5)+"%");
				}else{
					dataQuery.setParameter(entry.getKey(), entry.getValue());

				}
			}
		}

		dataQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		map.put("rows", dataQuery.list());

		if(page.getTotalRows()==0){
			SQLQuery totalQuery=getSession().createSQLQuery(SQL.get(filter.getFilter(),"products-category-list-count"));
			Iterator<Entry<String, String>> totalIt=filter.getFilter().entrySet().iterator();
			Entry<String, String> totalEntry=null;
			while(totalIt.hasNext()){
				totalEntry=totalIt.next();
				if(!totalEntry.getKey().startsWith("_language")){
					if(totalEntry.getValue().startsWith("in:")){
						totalQuery.setParameterList(totalEntry.getKey(), totalEntry.getValue().substring(3).split(","));
					}else if(totalEntry.getValue().startsWith("like:")){
						totalQuery.setParameter(totalEntry.getKey(), "%"+totalEntry.getValue().substring(5)+"%");
					}else{
						totalQuery.setParameter(totalEntry.getKey(), totalEntry.getValue());
					}
				}
			}
			map.put("total", totalQuery.uniqueResult());
		}else{
			map.put("total", page.getTotalRows());
		}

		return map;

	}

	/**
	 * 产成品汇总（按厂内名称）
	 */
	@Override
	public Map<String, Object> productsFactorySummary(Filter filter, Page page)throws Exception {
		// TODO Auto-generated method stub
		return findPageInfo(filter, page,"productsSummary-factory-list");
	}

	/**
	 * 产成品汇总（订单号、批次号、厂内名称）
	 */
	@Override
	public Map<String, Object> productsSundrySummary(Filter filter, Page page)throws Exception {

		Map<String, Object> map=new HashMap<String, Object>();
		String sql=SQL.get(filter.getFilter(), "products-sundr-list");
		if (page.getAll() == 0) {
			sql+=" limit "+(page.getPage() - 1) * page.getRows()+","+page.getRows();
		}
		SQLQuery dataQuery = getSession().createSQLQuery(sql);
		Iterator<Entry<String, String>> it=filter.getFilter().entrySet().iterator();
		Entry<String, String> entry=null;
		while(it.hasNext()){
			entry=it.next();
			if(!entry.getKey().startsWith("_language")){
				if(entry.getValue().startsWith("in:")){
					dataQuery.setParameterList(entry.getKey(), entry.getValue().substring(3).split(","));
				}else if(entry.getValue().startsWith("like:")){
					dataQuery.setParameter(entry.getKey(), "%"+entry.getValue().substring(5)+"%");
				}else{
					dataQuery.setParameter(entry.getKey(), entry.getValue());

				}
			}
		}

		dataQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		map.put("rows", dataQuery.list());

		if(page.getTotalRows()==0){
			SQLQuery totalQuery=getSession().createSQLQuery(SQL.get(filter.getFilter(),"products-sundr-list-count"));
			Iterator<Entry<String, String>> totalIt=filter.getFilter().entrySet().iterator();
			Entry<String, String> totalEntry=null;
			while(totalIt.hasNext()){
				totalEntry=totalIt.next();
				if(!totalEntry.getKey().startsWith("_language")){
					if(totalEntry.getValue().startsWith("in:")){
						totalQuery.setParameterList(totalEntry.getKey(), totalEntry.getValue().substring(3).split(","));
					}else if(totalEntry.getValue().startsWith("like:")){
						totalQuery.setParameter(totalEntry.getKey(), "%"+totalEntry.getValue().substring(5)+"%");
					}else{
						totalQuery.setParameter(totalEntry.getKey(), totalEntry.getValue());
					}
				}
			}
			map.put("total", totalQuery.uniqueResult());
		}else{
			map.put("total", page.getTotalRows());
		}

		return map;


	}

	@Override
	public Map<String, Object> productsCustomerStockSummary(Filter filter,Page page) throws Exception {
		// TODO Auto-generated method stub
		return super.findPageInfo(filter, page, "products-customer-stock-summary");
	}

	@Override
	public List<Map<String, Object>> findAllFinishProduct() throws Exception {
		String sql=SQL.get("finishProductAll-list");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public int querySlBycode(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql="SELECT materielCode from HS_FINISHPRODUCT WHERE materielCode like '%" + wlbh + "%'";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list.size();
	}

	@Override
	public int querySlinfo(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql="SELECT materielCode from HS_FINISHPRODUCT WHERE materielCode ='" + wlbh + "'";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list.size();
	}

	@Override
	public List<Map<String, Object>> checkYxInfo(String planCode)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql = "select t.* from  HS_TC_PROC_BOM t left join hs_consumer c on c.id=t.tcProcBomConsumerId where t.tcProcBomConsumerId =" + planCode;
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@Override
	public List<Map<String, Object>> queryYxInfo(long finishedProductId)throws SQLTemplateException {
		String sql=SQL.get("queryYxInfo");
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameter("finishedProductId", finishedProductId).list();
		return list;
	}

	@Override
	public int queryCpggInfo(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql="select   DISTINCT productModel from HS_FINISHPRODUCT where materielCode like '%" + wlbh + "%'";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list.size();
	}

	@Override
	public List<Map<String, Object>> queryMfinfo(String wlbh, String mf)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql = "select  materielCode, productWidth from HS_FINISHPRODUCT  where  materielCode like '%" + wlbh + "%' and productWidth = " + mf;
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@Override
	public int queryMfnewcode(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql="select  DISTINCT productWidth from HS_FINISHPRODUCT where  materielCode like '%" + wlbh + "%'";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list.size();
	}

	@Override
	public List<Map<String, Object>> queryJZinfo(String wlbh, String jz)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql = "select  materielCode, productRollWeight from HS_FINISHPRODUCT  where  materielCode like '%" + wlbh + "%' and productRollWeight = " + jz;
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@Override
	public int queryJznewcode(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql="select  DISTINCT productRollWeight from HS_FINISHPRODUCT where  materielCode like '%" + wlbh + "%'";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list.size();
	}

	@Override
	public List<Map<String, Object>> queryJCinfo(String wlbh, String jc)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql = "select  materielCode, productRollLength from HS_FINISHPRODUCT  where materielCode like '%" + wlbh + "%' and productRollLength = " + jc;
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@Override
	public int queryJcnewcode(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql="select  DISTINCT productRollLength from HS_FINISHPRODUCT where  materielCode like '%" + wlbh + "%'";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list.size();
	}

	@Override
	public List<Map<String, Object>> queryJZCinfo(String wlbh, String jz,
			String jc) throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql = "select  materielCode, productRollLength,productRollWeight from HS_FINISHPRODUCT  where  materielCode like '%" + wlbh + "%' and productRollLength = " + jc + " and productRollWeight = " + jz;
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@Override
	public int queryJzcnewcode(String wlbh) throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql="select  DISTINCT productRollWeight,productRollLength from HS_FINISHPRODUCT where materielCode like '%" + wlbh + "%'";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list.size();
	}

	@Override
	public List<Map<String, Object>> queryGGinfo(String wlbh, String gg)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql = "select  materielCode, productModel from HS_FINISHPRODUCT where materielCode like '%" + wlbh + "%' and productModel = '" + gg + "'";
		System.out.println("查下规格的sql:" + sql);
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		System.out.println("查下的结果集:" + list.size());
		return list;
	}

	@Override
	public String getzgmcbycode(String code) throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql = "select carrierName from HS_WEIGHTCARRIER where carrierCode='" + code + "'";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		if(list.size() > 0){
			return list.get(0).get("CARRIERNAME").toString();
		}else{
			return "";
		}
	}

	@Override
	public List<Map<String, Object>> queryBcinfoByBcbm(String bcmb)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql = "select ftcbcbom.code from hs_ftc_bc_bom ftcbcbom left join hs_ftc_bc_bom_version gybb on ftcbcbom.id=gybb.bid and gybb.auditState=2 and gybb.enabled=0 left join hs_ftc_bc_bom_version_detail gybbmx on gybb.bid=gybbmx.packVersionId where gybbmx.packMaterialCode='" + bcmb + "'";
		List<Map<String, Object>> list= getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	@Override
	public Map<String, Object> findPageInfo1(Filter filter, Page page) {
		return findPageInfo(filter, page,"product-mirror-list");
	}

	//产品的总克重
	@Override
	public int queryProcBomDetail(String ftcBomVersionId) throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql="SELECT sum(t.ftcBomDetailWeightPerSquareMetre) FROM hsmes.hs_ftc_proc_bom_detail t where t.ftcBomVersionId='"+ftcBomVersionId+"'";
		java.lang.Double bd=(java.lang.Double) getSession().createSQLQuery(sql).uniqueResult();
		if(bd != null) {
			return bd.intValue();
		} else {
			return 0;
		}
	}
	
	

}

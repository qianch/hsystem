/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.weave.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.planner.weave.dao.IWeavePlanDao;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.store.entity.Roll;

/**
 * 
 * @author 肖文彬
 * @Date 2016年4月5日 下午4:35:34
 */
@Repository
public class WeavePlanDaoImpl extends BaseDaoImpl implements IWeavePlanDao {
	
	@Resource SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"weavePlan-list");
	}

	@Override
	public void deleteDevice(Long wid,String date,String workshop) {
		getSession().createSQLQuery(SQL.get("deviceDelete-list")).setParameter("wid", wid).setParameter("date", date).setParameter("workshop", workshop).executeUpdate();
		
	}

	@Override
	public void updateIndex(Long id, Long index) {
		getSession().createSQLQuery(SQL.get("updateIndex-list")).setParameter("id", id).executeUpdate();
		
	}

	@Override
	public List<Map<String, Object>> findDevice(Long wid,String date,String workshop,Long id) {
		String sql = SQL.get("weave-findDevice");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("wid", wid).setParameter("workshop", workshop).setParameter("date", date).setParameter("id", id).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public void deleteWeavePlan(String id) {
		getSession().createSQLQuery(SQL.get("weaveDelete-list")).setParameterList("id", id.split(",")).executeUpdate();
	}

	public List<Map<String, Object>> PlanCodeCombobox() throws Exception {
		String sql=SQL.get("plan-combobox");
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	public String getWeavePlanDevices(Long wid){
		String sql="SELECT GROUP_CONCAT(d.deviceCode) from  hs_weave_plan_devices wpd LEFT JOIN hs_device d on d.id=wpd.deviceId where wpd.weavePlanId=:wid";
		String deviceCodes=(String) getSession().createSQLQuery(sql).setParameter("wid", wid).uniqueResult();
		return deviceCodes;
	}

	@Override
	public List<Map<String, Object>> findWeavePlan(String planCode) throws SQLTemplateException {
		String sql=SQL.get("weavePlan-list");
		System.out.println(sql);
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("planCode", planCode).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public void deleteDevices(String id) {
		getSession().createSQLQuery(SQL.get("deviceDelete-list")).setParameterList("id", id.split(",")).executeUpdate();
	}

	@Override
	public List<HashMap<String,Object>> findWeavePlanByDeviceId(Long deviceId) {
		String sql = SQL.get("plan-device-weave");
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("deviceId", deviceId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return  query.list();
	}

	@Override
	public void updateSort(String id,Long time) {
		getSession().createSQLQuery(SQL.get("updateSort-list")).setParameter("time", time).setParameter("id", id).executeUpdate();
	}
	
	public Map<String, Object> dataList(Filter filter, Page page) throws Exception {
		return this.findPageInfo(filter,page,"weavePlan-datalist");
	}
	
	public List<Map<String,Object>> ledWeavePlan(Integer workshopNo) {
		String sql = SQL.get("weave-led");
		SQLQuery query = getSession().createSQLQuery(sql);
		String[] wss={"编织一车间","编织二车间","编织三车间","编织四车间","编织五车间"};
		String ws=wss[workshopNo-1];
		String wp_ids_sql="SELECT GROUP_CONCAT(wp.id) FROM hs_weave_plan wp LEFT JOIN hs_produce_plan_detail ppd on ppd.id=wp.producePlanDetailId LEFT JOIN hs_produce_plan pp on pp.id=ppd.producePlanId WHERE wp.isFinished =- 1 AND wp.deleveryDate >= NOW() and pp.workshop='"+ws+"' ORDER BY wp.deviceCode ASC";
		String wp_ids=(String) getSession().createSQLQuery(wp_ids_sql).uniqueResult();
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setParameterList("wids", wp_ids.split(","));
		return  query.list();
	}

	@Override
	public Map<String, Object> findWeavePageInfo(Filter filter, Page page) {
		return findPageInfo(filter,page,"wp-list");
	}
	@Override
	public Map<String, Object> findWeavePageInfo2(Filter filter, Page page) {
		return findPageInfo(filter,page,"wp-list2");
	}
	@Override
	public Map<String, Object> findWeavePageInfo1(Filter filter, Page page) {
		return findPageInfo(filter,new Page(),"wp-list1");
	}
	
	@Override
	public Map<String, Object> findNofinish(Filter filter,Page page) {
		return this.findPageInfo(filter,page,"findW-list");
	}
	@Override
	public Map<String, Object> findfinished(Filter filter, Page page) {
		Map<String, Object> findPageInfo = this.findPageInfo(filter,page,"wp-list");
		return findPageInfo;
	}
	
	@Override
	public void updateSumWeight(Long id,Double weight){
		getSession().createSQLQuery(SQL.get("updateSum-count")).setParameter("weight", weight).setParameter("id", id).executeUpdate();
	}
	@Override
	public List<Map<String, Object>> findRollIsPro(String batchcode,String factoryProductName) throws SQLTemplateException{
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("batchcode", batchcode);
		String sql=SQL.get("selectRoll-ispro");
		
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("batchcode", batchcode);
		query.setParameter("factoryProductName", factoryProductName);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
		//return getSession().createSQLQuery(SQL.get("selectRoll-ispro")).setParameter("batchcode", batchcode)
	}
	public Map<String,Object> countRollsAndTrays(Long wid){
		return (Map<String, Object>) getSession().createSQLQuery(SQL.get("count-weave-rolls-trays")).setParameter("id", wid).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
	}
	@Override
	public List<Map<String, Object>> findRollisNoA(String batchcode){
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("batchcode", batchcode);
		String sql=SQL.get("selectRoll-isNoA");
		
		SQLQuery query=getSession().createSQLQuery(sql);
		query.setParameter("batchcode", batchcode);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getWeavePlanPackTask(Long wid){
		String sql=SQL.get("weave-pack-task");
		return getSession().createSQLQuery(sql).setParameter("id", wid).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@Override
	public Map<String,Object> getBjDetails(String planCode, String yx, String partname)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		String sql=SQL.get("device-plans_bjinfo");
		
		Map<String, Object> ret=new HashMap<String,Object>();
		
		String wids=(String) getSession().createSQLQuery("SELECT GROUP_CONCAT(wpd.weavePlanId) from hs_weave_plan_devices wpd where wpd.deviceId in ("+planCode+")").uniqueResult();
		if(StringUtils.isBlank(wids)){
			ret.put("total", 0);
			ret.put("rows", null);
			return ret;
		}
		System.out.println(wids);
		System.out.println(planCode);
		if("null".equals(partname)){
			partname = "";
		}
		List<Map<String, Object>> rows = getSession().createSQLQuery(sql)
				.setParameterList("dids", planCode.split(","))
				.setParameterList("ids", wids.split(","))
				.setParameter("dvid", planCode)
				.setParameter("yx", yx)
				.setParameter("partname", partname)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		
		Double productWeight=null;
		Double productLength=null;
		Map<String, Object> map=new HashMap<String,Object>();
		FinishedProduct product=null;
		TcBomVersionParts tvp=null;
		for (Map<String, Object> items : rows) {
			if(items.get("PRODUCTWEIGHT")==null){
				//套材订单
				if("1".equals(items.get("PRODUCTISTC")+"")){
					productWeight=new Double(0);
					map.clear();
					map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID")+""));
					ProducePlanDetail ppd = findById(ProducePlanDetail.class, Long.parseLong(items.get("producePlanDetailId")+""));
					for (ProducePlanDetailPartCount c : ppd.getList()) {
						tvp = findById(TcBomVersionParts.class, c.getPartId());
						productWeight += tvp.getTcProcBomVersionPartsWeight() * c.getPlanPartCount();
					}
				}else if("2".equals(items.get("PRODUCTISTC")+"")){//非套材订单					
					productWeight=new Double(0);					
					product=findById(FinishedProduct.class,Long.parseLong(items.get("PRODUCTID")+""));
					if(product.getProductRollWeight()==null){
						List<FtcBomDetail> bomDetails=null;
						map.clear();
						map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID")+""));
						
						bomDetails=findListByMap(FtcBomDetail.class, map);
						Double bomWeight=0D;
						for(FtcBomDetail d:bomDetails){
							bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
						}
						//米长*门幅*单位面积克重*卷数
						productWeight=MathUtils.add(bomWeight*Double.parseDouble(items.get("PRODUCTWIDTH")+"") * Double.parseDouble(items.get("PRODUCTLENGTH")+""), 0D, 2);
						
						productWeight = MathUtils.div(productWeight, 1000000, 2);
					}else{
						productWeight = product.getProductRollWeight();
					}
					
				}else{//胚布订单
					productWeight=new Double(0);
					
					List<FtcBomDetail> bomDetails=null;
					map.clear();
					map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID")+""));
					
					bomDetails=findListByMap(FtcBomDetail.class, map);
					Double bomWeight=0D;
					for(FtcBomDetail d:bomDetails){
						bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
					}
					//米长*门幅*单位面积克重*卷数
					productWeight=MathUtils.add(bomWeight * Double.parseDouble(items.get("PRODUCTWIDTH")+"") * Double.parseDouble(items.get("PRODUCTLENGTH")+""), 0D, 2);
					productWeight = MathUtils.div(productWeight, 1000000, 2);
				}
				
				items.put("PRODUCTWEIGHT", productWeight);
			}
			if(items.get("PRODUCTLENGTH")==null){
				if("1".equals(items.get("PRODUCTISTC")+"")){
					productLength=new Double(0);
				}
				else if("2".equals(items.get("PRODUCTISTC")+"")){
					productLength=new Double(0);					
					product=findById(FinishedProduct.class,Long.parseLong(items.get("PRODUCTID")+""));
					if(product.getProductRollWeight()!=null){
						List<FtcBomDetail> bomDetails=null;
						map.clear();
						map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID")+""));
						
						bomDetails=findListByMap(FtcBomDetail.class, map);
						Double bomWeight=0D;
						for(FtcBomDetail d:bomDetails){
							bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
						}
						productLength =MathUtils.div(Double.parseDouble(items.get("PRODUCTWEIGHT")+"")*1000000, bomWeight*Double.parseDouble(items.get("PRODUCTWIDTH")+"") , 2);
					}
				}
				else{
					productLength=new Double(0);					
					List<FtcBomDetail> bomDetails=null;
					map.clear();
					map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID")+""));
					
					bomDetails=findListByMap(FtcBomDetail.class, map);
					Double bomWeight=0D;
					for(FtcBomDetail d:bomDetails){
						bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
					}
					productLength =MathUtils.div(Double.parseDouble(items.get("PRODUCTWEIGHT")+"")*1000000, bomWeight*Double.parseDouble(items.get("PRODUCTWIDTH")+"") , 2);
				}
				items.put("PRODUCTLENGTH", productLength);
			}

		}
		
		ret.put("total", rows.size());
		ret.put("rows", rows);
		
		return ret;
	}

	@Override
	public Map<String, Object> findDevicePlans(String dids) {
		// TODO Auto-generated method stub

		
		String sql=SQL.get("device-plans_bjhzinfo");
		
		Map<String, Object> ret=new HashMap<String,Object>();
		
		String wids=(String) getSession().createSQLQuery("SELECT GROUP_CONCAT(wpd.weavePlanId) from hs_weave_plan_devices wpd where wpd.deviceId in ("+dids+")").uniqueResult();
		if(StringUtils.isBlank(wids)){
			ret.put("total", 0);
			ret.put("rows", null);
			return ret;
		}
		System.out.println(wids);
		System.out.println(dids);
		List<Map<String, Object>> rows=getSession().createSQLQuery(sql).setParameterList("dids", dids.split(",")).setParameterList("ids", wids.split(",")).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
//		Double productWeight=null;
//		Double productLength=null;
//		Map<String, Object> map=new HashMap<String,Object>();
//		FinishedProduct product=null;
//		TcBomVersionParts tvp=null;
//		for (Map<String, Object> items : rows) {
//			if(items.get("PRODUCTWEIGHT")==null){
//				//套材订单
//				if("1".equals(items.get("PRODUCTISTC")+"")){
//					productWeight=new Double(0);
//					map.clear();
//					map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID")+""));
//					ProducePlanDetail ppd = findById(ProducePlanDetail.class, Long.parseLong(items.get("producePlanDetailId")+""));
//					for (ProducePlanDetailPartCount c : ppd.getList()) {
//						tvp = findById(TcBomVersionParts.class, c.getPartId());
//						productWeight += tvp.getTcProcBomVersionPartsWeight() * c.getPlanPartCount();
//					}
//				}else if("2".equals(items.get("PRODUCTISTC")+"")){//非套材订单					
//					productWeight=new Double(0);					
//					product=findById(FinishedProduct.class,Long.parseLong(items.get("PRODUCTID")+""));
//					if(product.getProductRollWeight()==null){
//						List<FtcBomDetail> bomDetails=null;
//						map.clear();
//						map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID")+""));
//						
//						bomDetails=findListByMap(FtcBomDetail.class, map);
//						Double bomWeight=0D;
//						for(FtcBomDetail d:bomDetails){
//							bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
//						}
//						//米长*门幅*单位面积克重*卷数
//						productWeight=MathUtils.add(bomWeight*Double.parseDouble(items.get("PRODUCTWIDTH")+"") * Double.parseDouble(items.get("PRODUCTLENGTH")+""), 0D, 2);
//						
//						productWeight = MathUtils.div(productWeight, 1000000, 2);
//					}else{
//						productWeight = product.getProductRollWeight();
//					}
//					
//				}else{//胚布订单
//					productWeight=new Double(0);
//					
//					List<FtcBomDetail> bomDetails=null;
//					map.clear();
//					map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID")+""));
//					
//					bomDetails=findListByMap(FtcBomDetail.class, map);
//					Double bomWeight=0D;
//					for(FtcBomDetail d:bomDetails){
//						bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
//					}
//					//米长*门幅*单位面积克重*卷数
//					productWeight=MathUtils.add(bomWeight * Double.parseDouble(items.get("PRODUCTWIDTH")+"") * Double.parseDouble(items.get("PRODUCTLENGTH")+""), 0D, 2);
//					productWeight = MathUtils.div(productWeight, 1000000, 2);
//				}
//				
//				items.put("PRODUCTWEIGHT", productWeight);
//			}
//			if(items.get("PRODUCTLENGTH")==null){
//				if("1".equals(items.get("PRODUCTISTC")+"")){
//					productLength=new Double(0);
//				}
//				else if("2".equals(items.get("PRODUCTISTC")+"")){
//					productLength=new Double(0);					
//					product=findById(FinishedProduct.class,Long.parseLong(items.get("PRODUCTID")+""));
//					if(product.getProductRollWeight()!=null){
//						List<FtcBomDetail> bomDetails=null;
//						map.clear();
//						map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID")+""));
//						
//						bomDetails=findListByMap(FtcBomDetail.class, map);
//						Double bomWeight=0D;
//						for(FtcBomDetail d:bomDetails){
//							bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
//						}
//						productLength =MathUtils.div(Double.parseDouble(items.get("PRODUCTWEIGHT")+"")*1000000, bomWeight*Double.parseDouble(items.get("PRODUCTWIDTH")+"") , 2);
//					}
//				}
//				else{
//					productLength=new Double(0);					
//					List<FtcBomDetail> bomDetails=null;
//					map.clear();
//					map.put("ftcBomVersionId", Long.parseLong(items.get("PROCBOMID")+""));
//					
//					bomDetails=findListByMap(FtcBomDetail.class, map);
//					Double bomWeight=0D;
//					for(FtcBomDetail d:bomDetails){
//						bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
//					}
//					productLength =MathUtils.div(Double.parseDouble(items.get("PRODUCTWEIGHT")+"")*1000000, bomWeight*Double.parseDouble(items.get("PRODUCTWIDTH")+"") , 2);
//				}
//				items.put("PRODUCTLENGTH", productLength);
//			}
//
//		}
		
		ret.put("total", rows.size());
		ret.put("rows", rows);
		
		return ret;
	
	}
}

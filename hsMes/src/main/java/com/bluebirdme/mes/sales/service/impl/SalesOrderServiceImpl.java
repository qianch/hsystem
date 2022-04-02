/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomDetailMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.FtcBomVersionMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionMirror;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.store.entity.TrayBarCode;
import com.bluebirdme.mes.store.service.ITrayBarCodeService;
import com.bluebirdme.mes.utils.ProductIsTc;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;
import org.xdemo.superutil.j2se.MathUtils;
import org.xdemo.superutil.j2se.ObjectUtils;

import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.common.service.IProcessService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.pack.entity.PackTask;
import com.bluebirdme.mes.planner.pack.service.IPackTaskService;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlanDetails;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.dao.ISalesOrderDao;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.sales.service.ISalesOrderStockService;

/**
 * 
 * @author 高飞
 * @Date 2016-10-13 11:06:42
 */
@Service
@AnyExceptionRollback
public class SalesOrderServiceImpl extends BaseServiceImpl implements ISalesOrderService {

	@Resource
	ISalesOrderDao salesOrderDao;
	@Resource
	ISalesOrderStockService salesOrderStockService;
	
	@Resource IProcessService processService;
	
	@Resource IAuditInstanceService auditService;
	
	@Resource
	IPackTaskService ptService;

	@Resource
	ITrayBarCodeService trayBarCodeService;

	@Override
	protected IBaseDao getBaseDao() {
		return salesOrderDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return salesOrderDao.findPageInfo(filter, page);
	}
	@Override
	public <T> Map<String, Object> findPageInfo1(Filter filter, Page page) throws Exception {
		return salesOrderDao.findPageInfo1(filter, page);
	}

	public <T> Map<String, Object> findPageInfo2(Filter filter, Page page) throws Exception {
		return salesOrderDao.findPageInfo2(filter, page);
	}

	@Override
	public void save(SalesOrder order) throws Exception {
		
		//保存订单
		super.save(order);

		List<SalesOrderDetail> list = order.getDetails();
		
		Map<String, Object> map=new HashMap<String,Object>();
		FinishedProduct product=null;
		Double orderWeight=null;
		TcBomVersionParts tvp=null;
		
		for (SalesOrderDetail detail : list) {
			detail.setSalesOrderId(order.getId());
			detail.setIsPlaned(0);
			detail.setAllocateCount(0D);

			//套材订单
			if(detail.getProductIsTc()==1){
				orderWeight=new Double(0);
				//保存订单明细
				save(detail);
				for(SalesOrderDetailPartsCount c:detail.getPartsCountList()){
					tvp=findById(TcBomVersionParts.class, c.getPartId());
					orderWeight+=tvp.getTcProcBomVersionPartsWeight()*c.getPartCount();
					c.setSalesOrderDetailId(detail.getId());
					c.setSalesOrderId(order.getId());
					if(tvp.getTcProcBomVersionPartsWeight()!=null) {
						c.setPartWeight(tvp.getTcProcBomVersionPartsWeight());
					}
					//保存订单明细的部件个数
					save(c);
				}

				detail.setTotalWeight(MathUtils.add(orderWeight, 0D, 2));
				update(detail);
			}else if(detail.getProductIsTc()==2){//非套材订单

				orderWeight=new Double(0);

				product=findById(FinishedProduct.class, detail.getProductId());
				if(product.getProductRollWeight()!=null){
					orderWeight=MathUtils.add(detail.getProductCount()*product.getProductRollWeight(), 0D, 2);
					detail.setTotalWeight(orderWeight);
				}else{
					List<FtcBomDetail> bomDetails=null;
					map.clear();
					map.put("ftcBomVersionId", detail.getProcBomId());

					bomDetails=findListByMap(FtcBomDetail.class, map);
					Double bomWeight=0D;
					for(FtcBomDetail d:bomDetails){
						bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
					}
					//米长*门幅*单位面积克重*卷数
					orderWeight=MathUtils.add(bomWeight*detail.getProductCount()*detail.getProductWidth()*detail.getProductRollLength(), 0D, 2);

					detail.setTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
				}

			}else{//胚布订单
				if(null != detail.getMirrorProcBomVersionId()){
					orderWeight=new Double(0);

					List<FtcBomDetailMirror> bomDetails=null;
					map.clear();
					map.put("ftcBomVersionId", detail.getMirrorProcBomVersionId());

					bomDetails=findListByMap(FtcBomDetailMirror.class, map);
					Double bomWeight=0D;
					for(FtcBomDetailMirror d:bomDetails){
						bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
					}
					//米长*门幅*单位面积克重*卷数
					orderWeight=MathUtils.add(bomWeight*detail.getProductCount()*detail.getProductWidth()*detail.getProductRollLength(), 0D, 2);
					detail.setTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
				}else {
					orderWeight=new Double(0);

					List<FtcBomDetail> bomDetails=null;
					map.clear();
					map.put("ftcBomVersionId", detail.getProcBomId());

					bomDetails=findListByMap(FtcBomDetail.class, map);
					Double bomWeight=0D;
					for(FtcBomDetail d:bomDetails){
						bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
					}
					//米长*门幅*单位面积克重*卷数
					orderWeight=MathUtils.add(bomWeight*detail.getProductCount()*detail.getProductWidth()*detail.getProductRollLength(), 0D, 2);
					detail.setTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
				}

			}

			
			//保存订单明细
			save(detail);
			
		}
	}

	@Override
	public void edit(SalesOrder order) throws Exception {
		//销售订单不为空时走更新update2，为空走update
		if(order.getSalesOrderMemo()!=null){
			super.update2(order);
		}else{
			super.update(order);
		}

		List<SalesOrderDetail> list = order.getDetails();
		
		//删除订单明细
		salesOrderDao.deleteDetails(order.getId());

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("salesOrderId", order.getId());
		//删除订单的部件明细
		delete(SalesOrderDetailPartsCount.class, param);
		
		List<SalesOrderDetail> old = findListByMap(SalesOrderDetail.class, param);
		
		List<Long> oldIds = new ArrayList<Long>();
		List<Long> newIds = new ArrayList<Long>();
		
		Map<String, Object> map=new HashMap<String,Object>();
		
		FinishedProduct product=null;
		Double orderWeight=null;
		TcBomVersionParts tvp=null;
		
		for (SalesOrderDetail detail : list) {
			
			if (detail.getId() != null) {
				newIds.add(detail.getId());
			}
			
			detail.setSalesOrderId(order.getId());
			
			detail.setIsPlaned(0);
			detail.setAllocateCount(0D);
			
			//套材订单
			if(detail.getProductIsTc()==1){
				orderWeight=new Double(0);
				//保存订单明细
				save(detail);
				for(SalesOrderDetailPartsCount c:detail.getPartsCountList()){
					tvp=findById(TcBomVersionParts.class, c.getPartId());
					orderWeight+=tvp.getTcProcBomVersionPartsWeight()*c.getPartCount();
					c.setSalesOrderDetailId(detail.getId());
					c.setSalesOrderId(order.getId());
					//保存订单明细的部件个数
					save(c);
				}
				detail.setTotalWeight(MathUtils.add(orderWeight, 0D, 2));
				update(detail);
			}else if(detail.getProductIsTc()==2){//非套材订单，需重新关联包装任务
				
				orderWeight=new Double(0);
				
				product=findById(FinishedProduct.class, detail.getProductId());
				if(product.getProductRollWeight()!=null){
					orderWeight=MathUtils.add(detail.getProductCount()*product.getProductRollWeight(), 0D, 2);
					detail.setTotalWeight(orderWeight);
				}else{
					List<FtcBomDetail> bomDetails=null;
					map.clear();
					map.put("ftcBomVersionId", detail.getProcBomId());
					
					bomDetails=findListByMap(FtcBomDetail.class, map);
					Double bomWeight=0D;
					for(FtcBomDetail d:bomDetails){
						bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
					}
					//米长*门幅*单位面积克重*卷数
					orderWeight=MathUtils.add(bomWeight*detail.getProductCount()*detail.getProductWidth()*detail.getProductRollLength(), 0D, 2);
					detail.setTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
				}
				
				map.clear();
				map.put("sodId",detail.getId());
				List<PackTask> tasks = new ArrayList<PackTask>();
				
				if(detail.getId()!=null){
					tasks=ptService.findListByMap(PackTask.class, map);
				}
				
				//保存订单明细
				save(detail);
				
				//重新关联新的计划ID
				for(PackTask pt:tasks){
					pt.setSodId(detail.getId());
					update(pt);
				}
				
			}else{//胚布订单
				orderWeight=new Double(0);
				
				List<FtcBomDetail> bomDetails=null;
				map.clear();
				map.put("ftcBomVersionId", detail.getProcBomId());
				
				bomDetails=findListByMap(FtcBomDetail.class, map);
				Double bomWeight=0D;
				for(FtcBomDetail d:bomDetails){
					bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
				}
				//米长*门幅*单位面积克重*卷数
				orderWeight=MathUtils.add(bomWeight*detail.getProductCount()*detail.getProductWidth()*detail.getProductRollLength(), 0D, 2);
				detail.setTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
				save(detail);
			}
			
			for(SalesOrderDetail sod:old){
				oldIds.add(sod.getId());
			}
			
			
			oldIds.removeAll(newIds);

			for (Long id : oldIds) {
				
				if(id==null)throw new Exception("计划ID不能为空");
				
				param.clear();
				param.put("sodId", id);
				
				// 删除包装任务
				param.put("sodId", id);
				this.delete(PackTask.class, param);
				
			}
			
			/*if(detail.getProductIsTc()==1){
				for(SalesOrderDetailPartsCount c:detail.getPartsCountList()){
					c.setSalesOrderDetailId(detail.getId());
					c.setSalesOrderId(order.getId());
					//保存订单明细的部件个数
					save(c);
				}
			}*/
		}
	}

	/**
	 * 强制变更
	 * @param details
	 * @throws Exception 
	 */
	public void forceEdit(List<SalesOrderDetail> details,Long userId) throws Exception {
		SalesOrder order=null;
		SalesOrderDetail d = this.findById(SalesOrderDetail.class, details.get(0).getId());;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("salesOrderId",d.getSalesOrderId());
		//删除订单的部件明细
		delete(SalesOrderDetailPartsCount.class, param);
		
		Map<String, Object> map=new HashMap<String,Object>();
		
		FinishedProduct product=null;
		Double orderWeight=null;
		TcBomVersionParts tvp=null;
		for (SalesOrderDetail sod : details) {
			d = this.findById(SalesOrderDetail.class, sod.getId());
			order=this.findById(SalesOrder.class, d.getSalesOrderId());
			
			if (!d.getProductMemo().equals(sod.getProductMemo())||!sdf.format(d.getDeliveryTime()).equals(sdf.format(sod.getDeliveryTime()))||d.getProductCount().doubleValue()!=sod.getProductCount().doubleValue()) {
				d.setEditTimes((d.getEditTimes()==null?0:d.getEditTimes())+1);
				d.setDeliveryTime(sod.getDeliveryTime());
				d.setProductCount(sod.getProductCount());
				d.setProductMemo(sod.getProductMemo());
				this.update(d);
			}
			
			//套材订单
			if(d.getProductIsTc()==1){
				orderWeight=new Double(0);
				//保存订单明细
				for(SalesOrderDetailPartsCount c:sod.getPartsCountList()){
					tvp=findById(TcBomVersionParts.class, c.getPartId());
					orderWeight+=tvp.getTcProcBomVersionPartsWeight()*c.getPartCount();
					c.setSalesOrderDetailId(d.getId());
					c.setSalesOrderId(order.getId());
					//保存订单明细的部件个数
					save(c);
				}
				d.setTotalWeight(orderWeight);
			}else if(d.getProductIsTc()==2){//非套材订单
				
				orderWeight=new Double(0);
				
				product=findById(FinishedProduct.class, d.getProductId());
				if(product.getProductRollWeight()!=null){
					orderWeight=MathUtils.add(d.getProductCount()*product.getProductRollWeight(), 0D, 2);
					d.setTotalWeight(orderWeight);
				}else{
					List<FtcBomDetail> bomDetails=null;
					map.clear();
					map.put("ftcBomVersionId", d.getProcBomId());
					
					bomDetails=findListByMap(FtcBomDetail.class, map);
					Double bomWeight=0D;
					for(FtcBomDetail bd:bomDetails){
						bomWeight+=bd.getFtcBomDetailWeightPerSquareMetre()==null?0D:bd.getFtcBomDetailWeightPerSquareMetre();
					}
					//米长*门幅*单位面积克重*卷数
					orderWeight=MathUtils.add(bomWeight*d.getProductCount()*d.getProductWidth()*d.getProductRollLength(), 0D, 2);
					d.setTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
				}
			}else{//胚布订单
				orderWeight=new Double(0);
				
				List<FtcBomDetail> bomDetails=null;
				map.clear();
				map.put("ftcBomVersionId", d.getProcBomId());
				
				bomDetails=findListByMap(FtcBomDetail.class, map);
				Double bomWeight=0D;
				for(FtcBomDetail bd:bomDetails){
					bomWeight+=bd.getFtcBomDetailWeightPerSquareMetre()==null?0D:bd.getFtcBomDetailWeightPerSquareMetre();
				}
				//米长*门幅*单位面积克重*卷数
				orderWeight=MathUtils.add(bomWeight*d.getProductCount()*d.getProductWidth()*d.getProductRollLength(), 0D, 2);
				d.setTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
			}
			
			update(d);
			
		}
		
		String auditCode="";
		if(order.getSalesOrderIsExport()==0)auditCode="XS2BG";
		if(order.getSalesOrderIsExport()==1)auditCode="XS1BG";
		
		auditService.submitAudit("订单变更，订单编号:"+order.getSalesOrderCode(), auditCode, userId, "salesOrder/audit?id=" + order.getId(), order.getId(), SalesOrder.class);
	}

	@Override
	public List<SalesOrderDetail> getDetails(Long salesOrderId) {
		return salesOrderDao.getDetails(salesOrderId);
	}
	
	@Override
	public List<Map<String,Object>> getDetails2(String salesOrderCode) throws SQLTemplateException {
		
		List<Map<String,Object>> list=salesOrderDao.findDetailByCode2(salesOrderCode);

		Map<String,Long> bomPartCount=null;

		List<Map<String,Object>> orderPartCounts=null;
		Map<String,Long> orderPartCount=null;


		int suitCount=Integer.MAX_VALUE;

		BigDecimal b1;
		BigDecimal b2;
		Long bomCount;
		Long producedCount;

		Iterator<Entry<String, Long>> it;
		Entry<String,Long> ent;

		for(Map<String,Object> sod:list){
			if(MapUtils.getAsInt(sod, "PRODUCTISTC")!=1)continue;

			//BOM部件和数量
			bomPartCount=getPartCount(MapUtils.getAsLong(sod, "ID"));
			//订单部件数量
			orderPartCounts=salesOrderDao.getOrderPartCount(MapUtils.getAsLong(sod, "ID"));
			//订单部件数量MAP
			orderPartCount=new HashMap<String, Long>();
			for(Map<String,Object> m:orderPartCounts){
				orderPartCount.put(MapUtils.getAsString(m, "PARTNAME"), MapUtils.getAsLong(m, "COUNT"));
			}
			
			it= bomPartCount.entrySet().iterator();
			
			
			while (it.hasNext()) {
				ent=it.next();
				bomCount=(Long) ent.getValue();
				producedCount=orderPartCount.get(ent.getKey());
				if(producedCount==null||producedCount==0L){
					suitCount=0;
					break;
				}else{
					b1=new BigDecimal(bomCount);
					b2=new BigDecimal(producedCount);
					if(b2.divide(b1,0,BigDecimal.ROUND_DOWN).intValue()<suitCount){
						suitCount=b2.divide(b1,0,BigDecimal.ROUND_DOWN).intValue();
					}
				}
			}
			
			sod.put("TS", suitCount);
		}
		
		return list;
	}
	
	public Map<String,Long> getPartCount(Long salesOrderDetailId){
		
		Map<String,Object> param=new HashMap<String,Object>();
		List<SalesOrderDetailPartsCount> parts=null;
		param.clear();
		param.put("salesOrderDetailId", salesOrderDetailId);
		
		parts=findListByMap(SalesOrderDetailPartsCount.class, param);
		
		Map<String,Long> ret=new HashMap<String, Long>();
		
		for(SalesOrderDetailPartsCount part:parts){
			ret.put(part.getPartName(),part.getPartBomCount().longValue());
		}
		
		return ret;
	}

	@Override
	public List<SalesOrderDetail> getDetails(String salesOrderCode) throws SQLTemplateException {
		return salesOrderDao.findDetailByCode(salesOrderCode);
	}

	public List<SalesOrder> findUncreatePlans() {
		return salesOrderDao.findUncreatePlans();
	}

	public String getSerial(Integer export) {
		return salesOrderDao.getSerial(export);
	}

	@Override
	public void deleteSalesOrder(String ids) {
		salesOrderDao.delete(SalesOrder.class, ids);
		String[] idarray = ids.split(",");
		for (String id : idarray) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("salesOrderId", Long.parseLong(id));
			List<SalesOrderDetail> list = findListByMap(SalesOrderDetail.class, map);
			 HashMap<String , Object> map1 =new HashMap<String, Object>();
             map1.put("weaveSalesOrderId", Long.parseLong(id));
             CutPlan cutPlan = findUniqueByMap(CutPlan.class, map1);
             if (cutPlan!=null) {
            	 cutPlan.setIsCreatWeave(0);
            	 cutPlan.setWeaveSalesOrderId(null);
			}

			salesOrderDao.delete(list.toArray(new SalesOrderDetail[] {}));
		}
	}

	@Override
	public void update(String id) {
		salesOrderDao.update(id);
	}
	//复制订单，订单明细以及套件的部件数量
	@Override
	public void copy(String id) throws Exception {
		Date date=new Date();
		SalesOrder oldSalesOrder= salesOrderDao.findById(SalesOrder.class, Long.parseLong(id));
		SalesOrder salesOrder = new SalesOrder();
		ObjectUtils.clone(oldSalesOrder, salesOrder);
		salesOrder.setSalesOrderCode(salesOrderDao.getCode(salesOrder.getSalesOrderIsExport(),salesOrder.getSalesOrderDate())); 
		salesOrder.setAuditState(0);
		salesOrder.setSalesOrderDate(date);
		salesOrderDao.save(salesOrder);
		
		 HashMap<String , Object> map =new HashMap<String, Object>();
		 map.put("salesOrderId", oldSalesOrder.getId());
		 map.put("closed", 1);
		 List<SalesOrderDetail> li = salesOrderDao.findListByMap(SalesOrderDetail.class, map);
		 for(SalesOrderDetail oldSales : li){
			 SalesOrderDetail sales= new SalesOrderDetail();
			 ObjectUtils.clone(oldSales, sales);
			 sales.setSalesOrderId(salesOrder.getId());
			 sales.setClosed(null);
			 sales.setEditTimes(0);
			 sales.setAllocateCount(Double.valueOf(0));
			 sales.setProducedRolls(Double.valueOf(0));
			 sales.setProduceCount(Double.valueOf(0));
			 sales.setProducedTrays(Double.valueOf(0));
			 salesOrderDao.save(sales);
			 
			 HashMap<String , Object> map1 =new HashMap<String, Object>();
			 map1.put("salesOrderDetailId", oldSales.getId());
			 List<SalesOrderDetailPartsCount> list = salesOrderDao.findListByMap(SalesOrderDetailPartsCount.class, map1);
			 if(list.size()!=0){
				 for(SalesOrderDetailPartsCount sodc:list){
					 SalesOrderDetailPartsCount newSodc=new SalesOrderDetailPartsCount();
					 ObjectUtils.clone(sodc, newSodc);
					 newSodc.setSalesOrderId(salesOrder.getId());
					 newSodc.setSalesOrderDetailId(sales.getId());
					 salesOrderDao.save(newSodc);
					 
				 }
			 }
		 }
	}

	@Override
	public List<Map<String, Object>> getOrderPartCount(Long id) {
		return salesOrderDao.getOrderPartCount(id);
	}

	@Override
	public Map<String, Object> countRollsAndTrays(Long salesOrderDetailId) {
		return salesOrderDao.countRollsAndTrays(salesOrderDetailId);
	}

	@Override
	public <T> Map<String, Object> findPageOut(Filter filter, Page page)throws Exception {
		return salesOrderDao.findPageOut(filter, page);
	}

	@Override
	public <T> Map<String, Object> findPageQuantity(Filter filter, Page page)throws Exception {
		return salesOrderDao.findPageQuantity(filter, page);
	}
	//月度订单产品汇总
	@Override
	public <T> Map<String, Object> findPageSummaryMonthly(Filter filter,
			Page page) throws Exception {
		return salesOrderDao.findPageSummaryMonthly(filter, page);
	}


	/*@Override
	public void fix(int type) {
		if(type==2){
			fixPlan();
			return;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		List<SalesOrderDetail> list=findAll(SalesOrderDetail.class);
		Double oldCount=null;
		Double rollWeight=null;
		Double rollCount=0D;
		List<FtcBomDetail> bomDetails=null;
		Double orderWeight=0D;
		TcBomVersionParts tvp=null;
		FinishedProduct fp=null;
		List<SalesOrderDetailPartsCount> partList;
		for(SalesOrderDetail sod:list){
			fp=findById(FinishedProduct.class, sod.getProductId());
			if(sod.getProductIsTc()!=1){
				
				rollWeight=0D;
				
				oldCount=sod.getProductCount();
				
				if(fp.getProductRollWeight()!=null){
					rollCount=oldCount/fp.getProductRollWeight();
					sod.setProductCount(MathUtils.add(rollCount, 0D, 0));
				}else{
					if(fp.getProductRollLength()!=null){
						map.clear();
						map.put("ftcBomVersionId", sod.getProcBomId());
						
						bomDetails=findListByMap(FtcBomDetail.class, map);
						Double bomWeight=0D;
						for(FtcBomDetail bd:bomDetails){
							bomWeight+=bd.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
						}
						//卷重，BOM单位面积克重*门幅*卷长
						try {
							rollWeight=MathUtils.add(bomWeight*fp.getProductWidth()*fp.getProductRollLength(), 0D, 2);
						} catch (Exception e) {
							System.out.println(sod.getId()+"\t"+sod.getSalesOrderSubCode()+" 不定长也不定重");
							wirteToFile(sod.getId()+"\t"+sod.getSalesOrderSubCode()+" 不定长也不定重");
							rollWeight=0D;
						}
						
						rollWeight=MathUtils.div(rollWeight,1000000D,2);
						sod.setProductCount(MathUtils.add(rollCount, rollWeight, 0));
					}
				}
			}
			
			
			计算卷数结束
			
			计算订单重量开始
			if(sod.getProductIsTc()==1){
				orderWeight=new Double(0);
				try {
					
					map.clear();
					map.put("salesOrderDetailId", sod.getId());
					
					partList=findListByMap(SalesOrderDetailPartsCount.class, map);
					for(SalesOrderDetailPartsCount c:partList){
						tvp=findById(TcBomVersionParts.class, c.getPartId());
						orderWeight+=tvp.getTcProcBomVersionPartsWeight()*c.getPartCount();
					}
				} catch (Exception e) {
					orderWeight=0D;
					System.out.println(sod.getId()+"\t"+sod.getSalesOrderSubCode()+" 无部件");
					wirteToFile(sod.getId()+"\t"+sod.getSalesOrderSubCode()+" 无部件");
				}
				
				sod.setTotalWeight(MathUtils.add(orderWeight, 0D, 2));
				update(sod);
			}else if(sod.getProductIsTc()==2){//非套材订单
				
				orderWeight=new Double(0);
				
				if(fp.getProductRollWeight()!=null){
					orderWeight=MathUtils.add(sod.getProductCount()*fp.getProductRollWeight(), 0D, 2);
					sod.setTotalWeight(orderWeight);
				}else{
					map.clear();
					map.put("ftcBomVersionId", sod.getProcBomId());
					
					bomDetails=findListByMap(FtcBomDetail.class, map);
					Double bomWeight=0D;
					for(FtcBomDetail d:bomDetails){
						bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
					}
					
					//米长*门幅*单位面积克重*卷数
					try {
						orderWeight=MathUtils.add(bomWeight*sod.getProductCount()*fp.getProductWidth()*fp.getProductRollLength(), 0D, 2);
					} catch (Exception e) {
						orderWeight=0D;
						System.out.println(sod.getId()+"\t"+sod.getSalesOrderSubCode()+" 不定长也不定重");
						wirteToFile(sod.getId()+"\t"+sod.getSalesOrderSubCode()+" 不定长也不定重");
					}
					
					sod.setTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
				}
				
			}else{//胚布订单
				orderWeight=new Double(0);
				
				map.clear();
				map.put("ftcBomVersionId", sod.getProcBomId());
				
				bomDetails=findListByMap(FtcBomDetail.class, map);
				Double bomWeight=0D;
				for(FtcBomDetail d:bomDetails){
					bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
				}
				
				try {
					//米长*门幅*单位面积克重*卷数
					orderWeight=MathUtils.add(bomWeight*sod.getProductCount()*fp.getProductWidth()*fp.getProductRollLength(), 0D, 2);
				} catch (Exception e) {
					orderWeight=0D;
					System.out.println(sod.getId()+"\t"+sod.getSalesOrderSubCode()+" 不定长也不定重");
					wirteToFile(sod.getId()+"\t"+sod.getSalesOrderSubCode()+" 不定长也不定重");
				}
				sod.setTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
			}
		}
	}
	
	public void wirteToFile(String content){
		try {
			FileUtils.writeToFile(content, "C:\\fix.order.txt", true, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fixPlan(){
		Map<String,Object> map=new HashMap<String,Object>();
		List<ProducePlanDetail> list=findAll(ProducePlanDetail.class);
		List<FtcBomDetail> bomDetails=null;
		Double orderWeight=0D;
		TcBomVersionParts tvp=null;
		FinishedProduct fp=null;
		List<ProducePlanDetailPartCount> partList;
		for(ProducePlanDetail ppd:list){
			
			fp=findById(FinishedProduct.class, ppd.getProductId());
			if(ppd.getProductIsTc()==1){
				orderWeight=new Double(0);
				try {
					map.clear();
					map.put("planDetailId", ppd.getId());
					partList=findListByMap(ProducePlanDetailPartCount.class, map);
					if(ListUtils.isEmpty(partList)){
						System.out.println(ppd.getProducePlanId()+" 空的");
					}
					for(ProducePlanDetailPartCount c:partList){
						tvp=findById(TcBomVersionParts.class, c.getPartId());
						if(tvp==null){
							continue;
						}
						if(tvp.getTcProcBomVersionPartsWeight()==null){
							continue;
						}
						orderWeight+=tvp.getTcProcBomVersionPartsWeight()*c.getPlanPartCount();
					}
				} catch (Exception e) {
					e.printStackTrace();
					orderWeight=0D;
					System.out.println(ppd.getId()+"\t"+ppd.getSalesOrderCode()+" 无部件");
					wirteToFile(ppd.getId()+"\t"+ppd.getSalesOrderCode()+" 无部件");
				}
				
				ppd.setPlanTotalWeight(MathUtils.add(orderWeight, 0D, 2));
				update(ppd);
			}else if(ppd.getProductIsTc()==2){//非套材订单
				
				orderWeight=new Double(0);
				
				if(fp.getProductRollWeight()!=null){
					orderWeight=MathUtils.add(ppd.getRequirementCount()*fp.getProductRollWeight(), 0D, 2);
					ppd.setPlanTotalWeight(orderWeight);
				}else{
					map.clear();
					map.put("ftcBomVersionId", ppd.getProcBomId());
					
					bomDetails=findListByMap(FtcBomDetail.class, map);
					Double bomWeight=0D;
					for(FtcBomDetail d:bomDetails){
						bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
					}
					
					//米长*门幅*单位面积克重*卷数
					try {
						orderWeight=MathUtils.add(bomWeight*ppd.getRequirementCount()*fp.getProductWidth()*fp.getProductRollLength(), 0D, 2);
					} catch (Exception e) {
						orderWeight=0D;
						System.out.println(ppd.getId()+"\t"+ppd.getSalesOrderCode()+" 不定长也不定重");
						wirteToFile(ppd.getId()+"\t"+ppd.getSalesOrderCode()+" 不定长也不定重");
					}
					
					ppd.setPlanTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
				}
				
			}else{//胚布订单
				orderWeight=new Double(0);
				
				map.clear();
				map.put("ftcBomVersionId", ppd.getProcBomId());
				
				bomDetails=findListByMap(FtcBomDetail.class, map);
				Double bomWeight=0D;
				for(FtcBomDetail d:bomDetails){
					bomWeight+=d.getFtcBomDetailWeightPerSquareMetre()==null?0D:d.getFtcBomDetailWeightPerSquareMetre();
				}
				
				try {
					//米长*门幅*单位面积克重*卷数
					orderWeight=MathUtils.add(bomWeight*ppd.getRequirementCount()*fp.getProductWidth()*fp.getProductRollLength(), 0D, 2);
				} catch (Exception e) {
					orderWeight=0D;
					System.out.println(ppd.getId()+"\t"+ppd.getSalesOrderCode()+" 不定长也不定重");
					wirteToFile(ppd.getId()+"\t"+ppd.getSalesOrderCode()+" 不定长也不定重");
				}
				ppd.setPlanTotalWeight(MathUtils.div(orderWeight, 1000000, 2));
			}
			
		}
		
	}*/
	
	public String hasPackTask(Long salesOrderId){
		return salesOrderDao.hasPackTask(salesOrderId);
	}
	
	public void setAllocated(String ids){
		salesOrderDao.setAllocated(ids);
	}

	@Override
	public List<TrayBarCode> findTrayBySalesOrdercode(String salesOrderCode) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("salesordercode",salesOrderCode);
		List<TrayBarCode> list = trayBarCodeService.findListByMap(TrayBarCode.class,map);
		return  list;
	}

	@Override
	public List<Map<String, Object>> findOrder() {
		return salesOrderDao.findOrder();
	}

	@Override
	public List<Map<String, Object>> findDetailByCodes(String salesOrderCode) throws Exception{
		return salesOrderDao.findDetailByCodes(salesOrderCode);
	}

	@Override
	public List<Map<String, Object>> findSalesOrder(String data) throws Exception {
		List<Map<String, Object>> listMap = salesOrderDao.findSalesOrder(data);
		Map<String, Object> ret = null;
		List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : listMap) {
			map.put("status", "1");
			ret = new HashMap<String, Object>();
			ret.put("id", MapUtils.getAsLong(map, "ID"));
			ret.put("text", "订单编号:" + MapUtils.getAsString(map, "SALESORDERCODE"));
			ret.put("state", "closed");
			ret.put("attributes", map);
			_ret.add(ret);
		}
		return _ret;
	}

	@Override
	public List<Map<String, Object>> findD(String id) {
		List<Map<String, Object>> listMap = salesOrderDao.findD(id);
		Map<String, Object> ret = null;
		List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : listMap) {
			ret = new HashMap<String, Object>();
			ret.put("id", MapUtils.getAsLong(map, "ID"));
			ret.put("text", "详情订单号/产品型号："+MapUtils.getAsString(map, "salesOrderSubCode".toUpperCase()) + "/" + MapUtils.getAsString(map, "productModel".toUpperCase()));
			map.put("status", "2");
			ret.put("state", "closed");
			ret.put("attributes", map);
			_ret.add(ret);
		}
		return _ret;
	}

	@Override
	public List<Map<String, Object>> findBom(String id) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		Map<String, Object> ret = null;
		List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
		SalesOrderDetail sod = salesOrderDao.findById(SalesOrderDetail.class,Long.parseLong(id));
		if(null != sod.getMirrorProcBomVersionId()){
			if(sod.getProductIsTc() != ProductIsTc.TC){
				FtcBomVersionMirror ftcBomVersionMirror = salesOrderDao.findById(FtcBomVersionMirror.class,sod.getMirrorProcBomVersionId());
				listMap = salesOrderDao.findFBom(ftcBomVersionMirror.getFtcProcBomId().toString());
				for (Map<String, Object> map : listMap) {
					map.put("status", "3");
					map.put("vId", id);
					ret = new HashMap<String, Object>();
					ret.put("id", MapUtils.getAsLong(map, "ID"));
					ret.put("text", "非套材:" + MapUtils.getAsString(map, "FTCPROCBOMNAME") + "/" + MapUtils.getAsString(map, "FTCPROCBOMCODE"));
					ret.put("state", "closed");
					ret.put("attributes", map);
					_ret.add(ret);
				}
			}else {
				TcBomVersionMirror tcBomVersionMirror = salesOrderDao.findById(TcBomVersionMirror.class,sod.getMirrorProcBomVersionId());
				listMap = salesOrderDao.findTBom(tcBomVersionMirror.getTcProcBomId().toString());
				for (Map<String, Object> map : listMap) {
					map.put("status", "3");
					map.put("vId", id);
					ret = new HashMap<String, Object>();
					ret.put("id", MapUtils.getAsLong(map, "ID"));
					ret.put("text", "套材:" + MapUtils.getAsString(map, "TCPROCBOMNAME") + "/" + MapUtils.getAsString(map, "TCPROCBOMCODE"));
					ret.put("state", "closed");
					ret.put("attributes", map);
					_ret.add(ret);
				}
			}
		}

		return _ret;
	}

	@Override
	public List<Map<String, Object>> findV(String id, String vId) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		Map<String, Object> ret = null;
		List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
		SalesOrderDetail sod = salesOrderDao.findById(SalesOrderDetail.class,Long.parseLong(vId));
		if(sod.getProductIsTc() != ProductIsTc.TC){
			FtcBomVersionMirror ftcBomVersionMirror = salesOrderDao.findById(FtcBomVersionMirror.class,sod.getMirrorProcBomVersionId());
			listMap = salesOrderDao.findFV(ftcBomVersionMirror.getFtcProcBomId().toString());
			for (Map<String, Object> map : listMap) {
				map.put("status", "4");
				map.put("vId", id);
				map.put("nodeType", "version");
				map.put("productIsTc","2");
				ret = new HashMap<String, Object>();
				ret.put("id", MapUtils.getAsLong(map, "ID"));
				ret.put("text", MapUtils.getAsString(map, "ftcProcBomVersionCode".toUpperCase()));
				ret.put("state", "closed");
				ret.put("attributes", map);
				_ret.add(ret);
			}
		}else {
			TcBomVersionMirror tcBomVersionMirror = salesOrderDao.findById(TcBomVersionMirror.class,sod.getMirrorProcBomVersionId());
			listMap = salesOrderDao.findTV(tcBomVersionMirror.getTcProcBomId().toString());
			for (Map<String, Object> map : listMap) {
				map.put("status", "4");
				map.put("vId", id);
				map.put("productIsTc","1");
				ret = new HashMap<String, Object>();
				ret.put("id", MapUtils.getAsLong(map, "ID"));
				ret.put("text", MapUtils.getAsString(map, "tcProcBomVersionCode".toUpperCase()));
				ret.put("state", "closed");
				ret.put("attributes", map);
				_ret.add(ret);
			}
		}
		return _ret;
	}

	@Override
	public List<Map<String, Object>> findP(String id, String vId) {
		List<Map<String, Object>> listMap = salesOrderDao.findP(id);
		Map<String, Object> ret = null;
		List<Map<String, Object>> _ret = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : listMap) {
			map.put("status", "5");
			map.put("vId", id);
			ret = new HashMap<String, Object>();
			ret.put("id", MapUtils.getAsLong(map, "ID"));
			ret.put("text", MapUtils.getAsString(map, "TCPROCBOMVERSIONPARTSNAME"));
			ret.put("state", "closed");
			ret.put("attributes", map);
			_ret.add(ret);
		}
		return _ret;
	}

    @Override
    public List<Map<String,Object>>  findListByMap1(Long id) {
        return salesOrderDao.findListByMap1(id);
    }

	@Override
	public List<Map<String, Object>> selectSalesOrder(String salesOrderSubCode,String factoryProductName) {
		return salesOrderDao.selectSalesOrder(salesOrderSubCode,factoryProductName);
	}
}


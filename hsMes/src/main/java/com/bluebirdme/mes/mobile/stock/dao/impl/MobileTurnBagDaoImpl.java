package com.bluebirdme.mes.mobile.stock.dao.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.xdemo.superutil.j2se.FileUtils;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.j2se.ObjectUtils;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.core.base.dao.BaseDaoImpl;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.sql.SQL;
import com.bluebirdme.mes.mobile.stock.dao.IMobileTurnBagDao;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.stock.entity.ProductForceOutRecord;
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import com.bluebirdme.mes.stock.entity.ProductOutRecord;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import com.bluebirdme.mes.store.entity.Box;
import com.bluebirdme.mes.store.entity.BoxBarcode;
import com.bluebirdme.mes.store.entity.BoxRoll;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBarCode;
import com.bluebirdme.mes.store.entity.TrayBoxRoll;

/**
 * PDA翻包操作
 * 
 * @author Goofy
 * @Date 2017年2月11日 上午11:03:13
 */
@Repository
public class MobileTurnBagDaoImpl extends BaseDaoImpl implements IMobileTurnBagDao {

	@Resource
	SessionFactory factory;

	@Override
	public Session getSession() {
		return factory.getCurrentSession();
	}
	
	Map<String, Object> param=new HashMap<String, Object>();

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}

	/**
	 * 执行翻包
	 * @param turnbag
	 * @param oldCode
	 * @param newCode
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	/*public void turnbag(TurnBagPlan turnbag, String oldCode, String newCode,Long uid) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		turnbag=findById(TurnBagPlan.class, turnbag.getId());
		
		param.clear();
		param.put("rollBarcode", oldCode);
		TotalStatistics _ts=findUniqueByMap(TotalStatistics.class, param);
		//删除旧的条码的生产统计
		delete(_ts);
		TotalStatistics ts=new TotalStatistics();
		ObjectUtils.clone(_ts, ts);
		ts.setId(null);
		ts.setBatchCode(turnbag.getNewBatchCode());
		Consumer c=findById(Consumer.class, turnbag.getNewConsumerId());
		ts.setCONSUMERNAME(c.getConsumerName());
		ts.setDeviceCode("FB");
		User u=findById(User.class, uid);
		ts.setLoginName(u.getUserName());
		Department dept=findById(Department.class, u.getDid());
		ts.setName(dept.getName());
		ts.setProducePlanCode("来自翻包");
		ts.setRollBarcode(newCode);
		ts.setRollQualityGradeCode("A");
		ts.setSalesOrderCode(turnbag.getNewSalesOrderCode());
		ts.setProductModel(turnbag.getNewProductModel());
		ts.setState(0);
		
		

		// 1 托条码
			// 旧的托条码 tray_barcode,tray,tray_box_roll，替换为新条码
			// 旧订单产出托数-1
			// 新订单产出托数+1
			// 新订单产出总卷数+旧的托的数量，重量+旧的托重量
			// 旧的订单产出总卷数减去该托的总卷数，还有该托重量
			// 之前该托所有卷，盒子，订单和产品信息替换为新的订单和产品信息
			// 旧条码编织计划，生产计划都要减产出托数和重量，和订单更新行为一致，除了没有新的计划更新
			//替换托条码为新条码

		// 2 盒条码
			// 如果上面还能找到托条码,说明直接翻包盒条码了
				// 删除旧的托条码 tray_barcode,tray
				// 旧订单产出托数-1
				// 新订单产出总卷数+旧的盒的卷数量
				// 旧的订单产出总卷数减去该盒的总卷数，减去该盒重量
				// 之前该盒所有卷，订单和产品信息替换为新的订单和产品信息
				// 旧条码编织计划，生产计划都要减产出托数和重量，和订单更新行为一致，除了没有新的计划更新
			// 替换盒条码box_barcode,box为新条码

		// 3 卷条码
			// 如果上面还能找到盒条码或者托条码,说明直接翻包卷条码了
				// 删除旧的托条码，tray_barcode,tray，tray_box_roll，盒条码 ,box_barcode,box，删除
				// 旧订单产出托数-1
				// 新订单产出总卷数+1，重量+该卷重量
				// 旧的订单产出总卷数-1，还有重量减去改卷重量
				// 旧条码编织计划，生产计划都要减产出托数和重量，和订单更新行为一致，除了没有新的计划更新
			// 改卷，订单和产品信息替换为新的订单和产品信息
			//替换卷条码为新条码
		
		
		//整托翻包
		if(newCode.startsWith("T")){
			ts.setBarcodeType("tray");
			
			deleteProductStockInfo(oldCode);
			
			//处理订单和计划信息,整托翻包，
			dealTrayOrderAndPlans(turnbag, oldCode,oldCode);
			
			//更换盒信息，条码信息
			updateInfoByTrayCode(turnbag, oldCode, newCode);
			
			saveTrayInfo(oldCode, newCode,uid);
			
			param.clear();
			param.put("trayBarcode", oldCode);
			
			delete(Tray.class,param);
			
			param.clear();
			param.put("barcode", oldCode);
			delete(TrayBarCode.class,param);
			
			param.clear();
			param.put("rollbarcode", oldCode);
			delete(TotalStatistics.class,param);
			
			
			//更换条码
			changeTrayCode(oldCode, newCode);
			
		}
		
		if(newCode.startsWith("B")){
			ts.setBarcodeType("box");


			//如果托条码存在，删除之，并解除关系
			Tray tray=getTray(oldCode);
			if(tray!=null){
				deleteProductStockInfo(tray.getTrayBarcode());
				//处理订单和计划信息
				dealTrayOrderAndPlans(turnbag, tray.getTrayBarcode(), oldCode);
				//删除上级的托关系
				deleteTrayRelation(tray);
				
				//删除生产统计中的托
				param.clear();
				param.put("rollbarcode", tray.getTrayBarcode());
				delete(TotalStatistics.class,param);
			}
			
			//更换盒信息，条码信息
			updateBoxInfoByBoxCode(turnbag, oldCode, newCode);
			
			//保存新的和信息
			saveBoxInfo(oldCode, newCode,uid);
			
			param.clear();
			param.put("boxBarcode", oldCode);
			delete(Box.class,param);
			
			param.clear();
			param.put("barcode", oldCode);
			delete(BoxBarcode.class,param);
			
			//更换条码
			changeBoxCode(oldCode, newCode);
			
		}
		
		if(newCode.startsWith("R")){
			ts.setBarcodeType("roll");
			Tray tray=getTray(oldCode);
			if(tray!=null){
				
				deleteProductStockInfo(tray.getTrayBarcode());
				
				//处理订单和计划信息
				dealTrayOrderAndPlans(turnbag, tray.getTrayBarcode(), oldCode);
				//删除上级的托关系
				deleteTrayRelation(tray);
				
				param.clear();
				param.put("rollbarcode", tray.getTrayBarcode());
				delete(TotalStatistics.class,param);
			}
			
			Box box=getBox(oldCode);
			if(box!=null){
				//删除盒的关系
				deleteBoxRelation(box);
				
				param.clear();
				param.put("rollbarcode", box.getBoxBarcode());
				delete(TotalStatistics.class,param);
			}
			
			saveRollInfo(oldCode, newCode,uid);
			
			deleteRollRelation(oldCode);
			
			
			//更换盒信息，条码信息//卷条码直接删除就好了
//			updateRollInfoRollCode(turnbag, oldCode, newCode);
			
			//更换条码，同理，卷条码直接删除，不需要换条码
//			changeRollCode(oldCode, newCode);
		}
		
		save(ts);
	}
	
	*//**
	 * 删除托的其他 业务信息
	 * @param oldTrayCode
	 *//*
	public void deleteProductStockInfo(String oldTrayCode){
		param.clear();
		param.put("barcode",oldTrayCode);
		delete(ProductForceOutRecord.class,param);
		delete(ProductInRecord.class,param);
		delete(ProductOutRecord.class,param);
		delete(ProductStockState.class,param);
	}
	
	public void saveRollInfo(String oldCode,String newCode,Long uid) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		param.clear();
		param.put("rollBarcode", oldCode);
		Roll oldRoll=findUniqueByMap(Roll.class, param);
		Roll newRoll=new Roll();
		ObjectUtils.clone(oldRoll, newRoll);
		newRoll.setRollBarcode(newCode);
		newRoll.setRollUserId(uid);
		save(newRoll);
		
	}
	
	public void saveBoxInfo(String oldCode,String newCode,Long uid) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		param.clear();
		param.put("boxBarcode", oldCode);
		Box oldObj=findUniqueByMap(Box.class, param);
		Box newObj=new Box();
		ObjectUtils.clone(oldObj, newObj);
		newObj.setBoxBarcode(newCode);
		newObj.setPackagingStaff(uid);
		save(newObj);
	}
	
	public void saveTrayInfo(String oldCode,String newCode,Long uid) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		param.clear();
		param.put("trayBarcode", oldCode);
		Tray oldObj=findUniqueByMap(Tray.class, param);
		Tray newObj=new Tray();
		ObjectUtils.clone(oldObj, newObj);
		newObj.setTrayBarcode(newCode);
		newObj.setPackagingStaff(uid);
		save(newObj);
		
	}
	
	*//**
	 * 删除托条码，托信息，托箱卷关系
	 * @param tray
	 *//*
	public void deleteTrayRelation(Tray tray){
		if(tray==null)return;
		param.clear();
		param.put("trayBarcode", tray.getTrayBarcode());
		
		delete(Tray.class, param);
		
		param.clear();
		param.put("barcode", tray.getTrayBarcode());
		delete(TrayBarCode.class, param);
		
		param.clear();
		param.put("trayBarcode",  tray.getTrayBarcode());
		delete(TrayBoxRoll.class, param);
	}
	
	*//**
	 * 删除盒条码，盒信息，盒卷关系
	 * @param tray
	 *//*
	public void deleteBoxRelation(Box box){
		if(box==null)return;
		param.clear();
		param.put("boxBarcode", box.getBoxBarcode());
		
		delete(Box.class, param);
		
		param.clear();
		param.put("barcode", box.getBoxBarcode());
		delete(BoxBarcode.class, param);
		
		param.clear();
		param.put("boxBarcode",  box.getBoxBarcode());
		delete(BoxRoll.class, param);
	}
	
	*//**
	 * 删除卷条码
	 * @param rollCode
	 *//*
	public void deleteRollRelation(String rollCode){
		param.clear();
		param.put("rollBarcode", rollCode);

		Roll roll=findUniqueByMap(Roll.class, param);
		delete(roll);
		
		param.clear();
		param.put("barcode", rollCode);
		
		RollBarcode rb=findUniqueByMap(RollBarcode.class, param);
		delete(rb);
	}
	
	
	public void changeTrayCode(String oldCode,String newCode){
		getSession().createSQLQuery(SQL.get("turnbag-change_tray-bytraycode")).setParameter("oldCode", oldCode).setParameter("newCode", newCode).executeUpdate();
		getSession().createSQLQuery(SQL.get("turnbag-change_tray_barcode-bytraycode")).setParameter("oldCode", oldCode).setParameter("newCode", newCode).executeUpdate();
		getSession().createSQLQuery(SQL.get("turnbag-change_tray_box_roll-bytraycode")).setParameter("oldCode", oldCode).setParameter("newCode", newCode).executeUpdate();
	}
	
	public void changeBoxCode(String oldCode,String newCode){
		getSession().createSQLQuery(SQL.get("turnbag-change_box-bytraycode")).setParameter("oldCode", oldCode).setParameter("newCode", newCode).executeUpdate();
		getSession().createSQLQuery(SQL.get("turnbag-change_box_barcode-bytraycode")).setParameter("oldCode", oldCode).setParameter("newCode", newCode).executeUpdate();
		getSession().createSQLQuery(SQL.get("turnbag-change_box_roll-bytraycode")).setParameter("oldCode", oldCode).setParameter("newCode", newCode).executeUpdate();
	}
	
	public void changeRollCode(String oldCode,String newCode){
		getSession().createSQLQuery(SQL.get("turnbag-change_roll-bytraycode")).setParameter("oldCode", oldCode).setParameter("newCode", newCode).executeUpdate();
		getSession().createSQLQuery(SQL.get("turnbag-change_roll_barcode-bytraycode")).setParameter("oldCode", oldCode).setParameter("newCode", newCode).executeUpdate();
	}
	
	
	public void updateInfoByTrayCode(TurnBagPlan turnbag, String oldCode, String newCode){
		
		//批量替换卷信息
		getSession().createSQLQuery(SQL.get("turnbag-updateRollInfoByTrayCode"))
					.setParameter("trayCode", oldCode)
					.setParameter("orderCode",turnbag.getNewSalesOrderCode())
					.setParameter("productId", turnbag.getNewProductId())
					.setParameter("batchCode", turnbag.getBatchCode())
					.setParameter("planId",turnbag.getId()<0?turnbag.getId():(turnbag.getId()*-1))
					.executeUpdate();
		
		//批量替换盒信息
		getSession().createSQLQuery(SQL.get("turnbag-updateBoxInfoByTrayCode"))
							.setParameter("trayCode", oldCode)
							.setParameter("orderCode",turnbag.getNewSalesOrderCode())
							.setParameter("productId", turnbag.getNewProductId())
							.setParameter("batchCode", turnbag.getBatchCode())
							.setParameter("planId",turnbag.getId()<0?turnbag.getId():(turnbag.getId()*-1))
							.executeUpdate();
		//批量替换托信息
		getSession().createSQLQuery(SQL.get("turnbag-updateTrayInfoByTrayCode"))
							.setParameter("trayCode", oldCode)
							.setParameter("orderCode",turnbag.getNewSalesOrderCode())
							.setParameter("productId", turnbag.getNewProductId())
							.setParameter("batchCode", turnbag.getBatchCode())
							.setParameter("planId",turnbag.getId()<0?turnbag.getId():(turnbag.getId()*-1))
							.executeUpdate();
		
		
	}
	
	private void updateBoxInfoByBoxCode(TurnBagPlan turnbag, String oldCode, String newCode){
		
		//批量替换卷信息
		getSession().createSQLQuery(SQL.get("turnbag-updateRollInfoByBoxCode"))
							.setParameter("boxCode", oldCode)
							.setParameter("orderCode",turnbag.getNewSalesOrderCode())
							.setParameter("productId", turnbag.getNewProductId())
							.setParameter("batchCode", turnbag.getBatchCode())
							.setParameter("planId",turnbag.getId()<0?turnbag.getId():(turnbag.getId()*-1))
							.executeUpdate();
				
		//批量替换盒信息
		getSession().createSQLQuery(SQL.get("turnbag-updateRollInfoByBoxCode"))
									.setParameter("boxCode", oldCode)
									.setParameter("orderCode",turnbag.getNewSalesOrderCode())
									.setParameter("productId", turnbag.getNewProductId())
									.setParameter("batchCode", turnbag.getBatchCode())
									.setParameter("planId",turnbag.getId()<0?turnbag.getId():(turnbag.getId()*-1))
									.executeUpdate();
		
		
	}
	
	private void updateRollInfoRollCode(TurnBagPlan turnbag, String oldCode, String newCode){
			
			//批量替换卷信息
			getSession().createSQLQuery(SQL.get("turnbag-updateRollInfoByRollCode"))
								.setParameter("rollCode", oldCode)
								.setParameter("orderCode",turnbag.getNewSalesOrderCode())
								.setParameter("productId", turnbag.getNewProductId())
								.setParameter("batchCode", turnbag.getBatchCode())
								.setParameter("planId",turnbag.getId()<0?turnbag.getId():(turnbag.getId()*-1))
								.executeUpdate();
	}
	
	*//**
	 * 托翻包的场景
	 * @param turnbag
	 * @param oldTrayCode 旧的托条码
	 * @param newCode 新的托条码
	 *//*
	public void dealTrayOrderAndPlans(TurnBagPlan turnbag, String oldTrayCode,String turnbagCode){
		
		SalesOrderDetail oldOrder=findById(SalesOrderDetail.class, turnbag.getOldSalesOrderDetailsId());
		SalesOrderDetail newOrder=findById(SalesOrderDetail.class, turnbag.getNewSalesOrderDetailsId());
		
		//旧的订单托数-1
		oldOrder.setProducedTrays((oldOrder.getProducedTrays()==null?0:oldOrder.getProducedTrays())-1);
		//原条码整托翻包
		if(turnbagCode.startsWith("T")){
			//整托翻包，新订单托数+1
			newOrder.setProducedTrays((newOrder.getProducedTrays()==null?0:newOrder.getProducedTrays())+1);
		}
				
		List<Roll> rolls=getRollsByCode(turnbagCode);
		Double rollsWeight=0D;
		
		for(Roll roll:rolls){
			rollsWeight+=roll.getRollWeight();
		}
		//更新产出重量
		oldOrder.setProduceCount((oldOrder.getProduceCount()==null?0:oldOrder.getProduceCount())-rollsWeight);
		newOrder.setProduceCount((newOrder.getProduceCount()==null?0:newOrder.getProduceCount())+rollsWeight);
		
		//更新卷数
		oldOrder.setProducedRolls((oldOrder.getProducedRolls()==null?0:oldOrder.getProducedRolls())-rolls.size());
		
		update(oldOrder);
		update(newOrder);
		
		param.clear();
		param.put("barcode", oldTrayCode);
		TrayBarCode tbc=findUniqueByMap(TrayBarCode.class, param);
		
		//负数表示翻包计划,不要做其他操作了
		if(tbc.getPlanId()<0)return;
		
		WeavePlan wp=findById(WeavePlan.class, tbc.getPlanId());
		
		if(wp==null)return;
		
		
		ProducePlanDetail ppd=findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
		ppd.setProducedRolls((ppd.getProducedRolls()==null?0:ppd.getProducedRolls())-rolls.size());
		ppd.setProducedCount((ppd.getProducedCount()==null?0:ppd.getProducedCount())+rollsWeight);
		//生产计划打包托数-1
		ppd.setPackagedCount((ppd.getPackagedCount()==null?0:ppd.getPackagedCount())-1);
		
		update(ppd);
		
		wp.setProduceRollCount((wp.getProduceRollCount()==null?0:wp.getProduceRollCount())-rolls.size());
		wp.setProducedTotalWeight((wp.getProducedTotalWeight()==null?0:wp.getProducedTotalWeight())+rollsWeight);
		wp.setPackagedCount((wp.getPackagedCount()==null?0:wp.getPackagedCount())-1);
		
		update(wp);
		
	}
	
	*//**
	 * 整盒翻包
	 * @param turnbag
	 * @param oldTrayCode 旧的托条码
	 * @param newCode 新的托条码
	 *//*
	public void dealBoxOrderAndPlans(TurnBagPlan turnbag, String oldBoxCode,String turnbagCode){
		
		SalesOrderDetail oldOrder=findById(SalesOrderDetail.class, turnbag.getOldSalesOrderDetailsId());
		SalesOrderDetail newOrder=findById(SalesOrderDetail.class, turnbag.getNewSalesOrderDetailsId());
		
		List<Roll> rolls=getRollsByCode(turnbagCode);
		Double rollsWeight=0D;
		
		for(Roll roll:rolls){
			rollsWeight+=roll.getRollWeight();
		}
		
		//更新产出重量
		oldOrder.setProduceCount((oldOrder.getProduceCount()==null?0:oldOrder.getProduceCount())-rollsWeight);
		newOrder.setProduceCount((newOrder.getProduceCount()==null?0:newOrder.getProduceCount())+rollsWeight);
		
		//更新卷数
		oldOrder.setProducedRolls((oldOrder.getProducedRolls()==null?0:oldOrder.getProducedRolls())-rolls.size());
		newOrder.setProducedRolls((newOrder.getProducedRolls()==null?0:newOrder.getProducedRolls())+rolls.size());
		
		update(oldOrder);
		update(newOrder);
		
		param.clear();
		param.put("barcode", oldBoxCode);
		BoxBarcode tbc=findUniqueByMap(BoxBarcode.class, param);
		
		//负数表示翻包计划,不要做其他操作了
		if(tbc.getPlanId()<0)return;
		
		WeavePlan wp=findById(WeavePlan.class, tbc.getPlanId());
		
		if(wp==null)return;
		
		
		ProducePlanDetail ppd=findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
		ppd.setProducedRolls((ppd.getProducedRolls()==null?0:ppd.getProducedRolls())-rolls.size());
		ppd.setProducedCount((ppd.getProducedCount()==null?0:ppd.getProducedCount())+rollsWeight);
		
		update(ppd);
		
		wp.setProduceRollCount((wp.getProduceRollCount()==null?0:wp.getProduceRollCount())-rolls.size());
		wp.setProducedTotalWeight((wp.getProducedTotalWeight()==null?0:wp.getProducedTotalWeight())+rollsWeight);
		update(wp);
		
	}
	
	*//**
	 * 更加条码获取下面所有的卷
	 * @param oldBarcode
	 * @return
	 *//*
	public List<Roll> getRollsByCode(String oldBarcode){
		String sql=SQL.get("turnbag-rolls");
		return getSession().createSQLQuery(sql).addEntity(Roll.class).setParameter("code", oldBarcode).list();
	}
	
	
	*//**
	 * 更新卷标签信息
	 * @param orderCode 订单号
	 * @param productId 产品ID
	 * @param batchCode 批次号
	 * @param planId 计划ID
	 *//*
	public void updateRollInfo(String orderCode,Long productId,String batchCode,Long planId,String oldBarcode){
		getSession().createSQLQuery(SQL.get("turnbag-updateRollInfo")).setParameter("orderCode", orderCode).setParameter("productId", productId).setParameter("batchCode", batchCode).setParameter("planId", planId).setParameter("code", oldBarcode).executeUpdate();
	}
	
	*//**
	 * 更新盒标签信息
	 * @param orderCode 订单号
	 * @param productId 产品ID
	 * @param batchCode 批次号
	 * @param planId 计划ID
	 *//*
	public void updateBoxInfo(String orderCode,Long productId,String batchCode,Long planId){
		getSession().createSQLQuery(SQL.get("turnbag-updateBoxInfo")).setParameter("orderCode", orderCode).setParameter("productId", productId).setParameter("batchCode", batchCode).setParameter("planId", planId).executeUpdate();
	}
	
	
	public Box getBox(String rollBarcode){
		param.clear();
		
		param.put("rollBarcode", rollBarcode);
		
		List<BoxRoll> list=findListByMap(BoxRoll.class, param);
		if(ListUtils.isEmpty(list)){
			return null;
		}
		
		param.clear();
		param.put("boxBarcode", list.get(0).getBoxBarcode());
		
		
		return findUniqueByMap(Box.class, param);
	}
	
	public Tray getTray(String rollOrBoxBarcode){
		
		String sql=null;
		
		if(rollOrBoxBarcode.startsWith("R")){
			sql=SQL.get("burnbag-getTrayByRoll");
		}else{
			sql=SQL.get("burnbag-getTrayByBox");
		}
		
		return (Tray) getSession().createSQLQuery(sql).addEntity(Tray.class).setParameter("barcode", rollOrBoxBarcode).uniqueResult();
	}
	
	public static void main(String[] args) throws IOException {
		File files[]=new File("C:\\x\\").listFiles();
		String content[]={};
		for (int i = 0; i < files.length; i++) {
			content=FileUtils.readContent(files[i].getAbsolutePath(), Charset.forName("UTF-8"));
			for(String s:content){
				if(!StringUtils.isBlank(s))
					FileUtils.writeToFile(s, "C:\\all.js", true, true, Charset.forName("UTF-8"));
				FileUtils.writeToFile(";", "C:\\all.js", true, true, Charset.forName("UTF-8"));
			}
		}
		
	}*/

}

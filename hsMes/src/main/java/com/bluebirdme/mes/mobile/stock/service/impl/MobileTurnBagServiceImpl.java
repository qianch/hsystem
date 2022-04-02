package com.bluebirdme.mes.mobile.stock.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.mobile.stock.dao.IMobileTurnBagDao;
import com.bluebirdme.mes.mobile.stock.service.IMobileTurnBagService;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlan;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlanDetails;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlanRecord;
import com.bluebirdme.mes.store.entity.BarCodeType;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBarCode;
import com.bluebirdme.mes.store.service.IBarCodeService;

/**
 * 类注释
 * @author Goofy
 * @Date 2017年2月11日 下午3:50:16
 */
@Service
@AnyExceptionRollback
public class MobileTurnBagServiceImpl extends BaseServiceImpl implements IMobileTurnBagService {
	
	@Resource IMobileTurnBagDao turnbagDao;
	
	@Resource IBarCodeService barcodeService;

	@Override
	protected IBaseDao getBaseDao() {
		return turnbagDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}
	
	/**
	 * 翻包
	 */
	public void turnbag(Long tid,Long[] dids,String[] codes) throws Exception{
		
		TurnBagPlan tbp=findById(TurnBagPlan.class, tid);
		
		// 翻包明细的订单 {翻包明细ID:[条码号]}
		Map<Long, List<String>> map=new HashMap<Long, List<String>>();
		
		int i=0;
		
		for(Long did:dids){
			if(map.get(did)==null){
				map.put(did, new ArrayList<String>());
			}
			map.get(did).add(codes[i++]);
		}
		
		Iterator<Entry<Long, List<String>>> it=map.entrySet().iterator();
		
		Map<String, Object> param=new HashMap<String,Object>();
		
		Entry<Long,List<String>> entry;
		
		TurnBagPlanDetails detail;
		
		TrayBarCode tbc=null;
		
		List<TurnBagPlanRecord> records=new ArrayList<TurnBagPlanRecord>();
		TurnBagPlanRecord record=null;
		
		int left;
		
		while (it.hasNext()) {
			entry=it.next();
			
			param.clear();
			param.put("turnBagPlanId", tid);
			param.put("salesOrderDetailId",entry.getKey());
			
			detail=findUniqueByMap(TurnBagPlanDetails.class, param);
			
			left=detail.getTurnBagCount();
			
			if(left<entry.getValue().size()){
				throw new Exception("翻包数量超出");
			}
			
//			detail.setTurnBagCountProcess(entry.getValue().size());
			
			for(String trayCode:entry.getValue()){
				tbc=barcodeService.findBarcodeInfo(BarCodeType.TRAY, trayCode);
				//将该条码标记为新订单的ID
//				tbc.setBelongToSalesOrderId(entry.getKey());
				tbc.setBelongToSalesOrderId(tbp.getNewSalesOrderDetailsId());
				tbc.setTurnBagPlanId(tbp.getId());
				tbc.setPlanDeliveryDate(tbp.getDeliveryDate());
				
				//翻包回去了
				if(tbc.getSalesOrderDetailId().longValue()==tbc.getBelongToSalesOrderId().longValue()){
					tbc.setBelongToSalesOrderId(null);
					tbc.setTurnBagPlanId(null);
					tbc.setPlanDeliveryDate(null);
				}
				update(tbc);
				//System.out.println(1/0);
				//翻包记录
				record=new TurnBagPlanRecord();
				record.setNewSalesOrderDetailId(tbp.getNewSalesOrderDetailsId());
				record.setOldBatchCode(tbp.getNewBatchCode());
				record.setSalesOrderDetailId(detail.getSalesOrderDetailId());
				record.setNewBatchCode(detail.getBatchCode());
				record.setTurnBagPlanId(tbp.getId());
				record.setTrayCode(trayCode);
				
				records.add(record);
				
			}
		}
		
		//保存翻包记录
		save(records.toArray(new TurnBagPlanRecord[records.size()]));
		
		//检查是否全部翻包结束
		param.clear();
		param.put("turnBagPlanId", tid);
		
		List<TurnBagPlanDetails> list=findListByMap(TurnBagPlanDetails.class, param);
		
		boolean complete=true; 
		for(TurnBagPlanDetails d:list){
			//如果翻包数量等于计划数量，那么表示该明细已完成
			/*if(d.getTurnBagCount().intValue()!=d.getTurnBagCountProcess().intValue()){
				complete=false;
				break;
			}*/
		}
		if(complete){
			//明细全部完成，那么自动完成
			tbp.setIsCompleted(1);
		}
		
		
	}
	

}

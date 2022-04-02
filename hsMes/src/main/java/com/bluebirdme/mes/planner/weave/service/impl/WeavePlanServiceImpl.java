/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.weave.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;

import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.weave.dao.IWeavePlanDao;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlanDevices;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;

/**
 * 
 * @author 肖文彬
 * @Date 2016-10-18 13:37:59
 */
@Service
@AnyExceptionRollback
public class WeavePlanServiceImpl extends BaseServiceImpl implements IWeavePlanService {
	
	@Resource IWeavePlanDao weavePlanDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return weavePlanDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return weavePlanDao.findPageInfo(filter,page);
	}	
	
	@Override
	public void save(WeavePlan weavePlan,Long[] did,Integer[] dCount,String time) {
		weavePlanDao.update(weavePlan);
		/*WeavePlanDevices[] devices = new WeavePlanDevices[did.length];
		WeavePlanDevices device = null;
		for(int i=0;i<did.length;i++){
			device=new WeavePlanDevices();
			device.setDeviceId(did[i]);
			device.setWeavePlanId(weavePlan.getId());
			devices[i]=device;
		}
		weavePlanDao.save(devices);*/
	}
	
	@Override
	public void updateWeave(Long wid,String date,String workshop, Long[] did,Integer[] dCount) throws Exception {
		/*weavePlanDao.deleteDevice(wid,date,workshop);
		WeavePlanDevices[] devices = new WeavePlanDevices[did.length];
		WeavePlanDevices device = null;
		for(int i=0;i<did.length;i++){
			device=new WeavePlanDevices();
			device.setDeviceId(did[i]);
			device.setWeavePlanId(wid);
			device.setWorkshop(workshop);
			device.setProduceDate(date);
			device.setProduceCount(dCount[i]);
			devices[i]=device;
		}
		weavePlanDao.save(devices);*/
	}

	@Override
	public void updateIndex(Long id, Long index) {
		weavePlanDao.updateIndex(id,index);
		
	}

	@Override
	public List<Map<String, Object>> findDevice(Long wid,String date,String workshop,Long id) {
		return weavePlanDao.findDevice(wid,date,workshop,id);
	}

	@Override
	public void saveWeavePlan(ProducePlan producePaln) {
		List<ProducePlanDetail> list=producePaln.getList();
		if(list.size()!=0){
			ArrayList<WeavePlan> planList=new ArrayList<WeavePlan>();
			for(ProducePlanDetail producePlanDetail:list){
				WeavePlan weavePlan = new WeavePlan();
				
				weavePlan.setRollNo(producePlanDetail.getRollNo());
				weavePlan.setDrawNo(producePlanDetail.getDrawNo());
				weavePlan.setLevelNo(producePlanDetail.getLevelNo());
				weavePlan.setSorting(producePlanDetail.getSorting());
				weavePlan.setPartId(producePlanDetail.getPartId());
//				weavePlan.setPartId(producePlanDetail.getMirrorPartId());
				weavePlan.setPlanCode(producePaln.getProducePlanCode());
				weavePlan.setProducePlanDetailId(producePlanDetail.getId());
				weavePlan.setBatchCode(producePlanDetail.getBatchCode());
				weavePlan.setBcBomCode(producePlanDetail.getBcBomCode());
				weavePlan.setBcBomVersion(producePlanDetail.getBcBomVersion());
				weavePlan.setConsumerId(producePlanDetail.getConsumerId());
				weavePlan.setComment(producePlanDetail.getComment());
				weavePlan.setConsumerName(producePlanDetail.getConsumerName());
				weavePlan.setDeleveryDate(producePlanDetail.getDeleveryDate());
				weavePlan.setDeviceCode(producePlanDetail.getDeviceCode());
				weavePlan.setFromSalesOrderDetailId(producePlanDetail.getFromSalesOrderDetailId());
				weavePlan.setFromTcId(producePlanDetail.getFromTcId());
//				weavePlan.setFromTcId(producePlanDetail.getMirrorFromTcId());
				weavePlan.setFromTcName(producePlanDetail.getFromTcName());
				weavePlan.setPartName(producePlanDetail.getPartName());
//				weavePlan.setPartId(producePlanDetail.getPartId());
				weavePlan.setIsFinished(-1);
				weavePlan.setOrderCount(producePlanDetail.getOrderCount());
				weavePlan.setTotalTrayCount(producePlanDetail.getTotalTrayCount());
				//FIXME 
				weavePlan.setTotalRollCount(producePlanDetail.getRequirementCount()==null?0:producePlanDetail.getRequirementCount().intValue());
				weavePlan.setSalesOrderCode(producePlanDetail.getSalesOrderCode());
				weavePlan.setProductWidth(producePlanDetail.getProductWidth());
				weavePlan.setProductLength(producePlanDetail.getProductLength());
				weavePlan.setProductWeight(producePlanDetail.getProductWeight());
				weavePlan.setProducedTotalWeight(Double.valueOf(0));
				weavePlan.setProduceRollCount(Double.valueOf(0));
				weavePlan.setProduceTrayCount(Double.valueOf(0));
				weavePlan.setPackagedCount(producePlanDetail.getPackagedCount());
				weavePlan.setProductModel(producePlanDetail.getProductModel());
				weavePlan.setProductName(producePlanDetail.getFactoryProductName());
				weavePlan.setProductType(producePlanDetail.getProductType());
				weavePlan.setRequirementCount(producePlanDetail.getRequirementCount());
				weavePlan.setProcessBomCode(producePlanDetail.getProcessBomCode());
				weavePlan.setProcessBomVersion(producePlanDetail.getProcessBomVersion());
				weavePlan.setProductId(producePlanDetail.getProductId());
				weavePlan.setPackReq(producePlanDetail.getPackReq());
				weavePlan.setProcReq(producePlanDetail.getProcReq());
				weavePlan.setPackBomId(producePlanDetail.getPackBomId());
				weavePlan.setProcBomId(producePlanDetail.getProcBomId());
				if(null != producePlanDetail.getMirrorProcBomId()) {
					weavePlan.setMirrorProcBomId(producePlanDetail.getMirrorProcBomId());
				}
//				weavePlan.setProcBomId(producePlanDetail.getMirrorProcBomId());
				planList.add(weavePlan);
			}
			weavePlanDao.save(planList.toArray(new WeavePlan[]{}));
		}
		
		
	}
		
	public List<Map<String, Object>> PlanCodeCombobox() throws Exception {
		List<Map<String, Object>> planCodeList=weavePlanDao.PlanCodeCombobox();
		Map<String, Object> ret =null;
		List<Map<String, Object>> _ret = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : planCodeList) {
			ret= new HashMap<String, Object>();
			ret.put("value", MapUtils.getAsString(map, "PRODUCEPLANCODE"));
			ret.put("text", MapUtils.getAsString(map, "PRODUCEPLANCODE"));
			_ret.add(ret);
		}
		return _ret;
	}

	@Override
	public List<Map<String, Object>> findWeavePlan(String planCode) throws SQLTemplateException {
		return weavePlanDao.findWeavePlan(planCode);
	}

	@Override
	public void deleteWeavePlan(String id) {
		weavePlanDao.deleteWeavePlan(id);
		weavePlanDao.deleteDevices(id);
	}

	@Override
	public List<HashMap<String,Object>> findWeavePlanByDeviceId(Long deviceId) {
		return weavePlanDao.findWeavePlanByDeviceId(deviceId);
	}

	@Override
	public void updateState(String ids) throws Exception {
		String[] li=ids.split(",");
		List<WeavePlan> list=new ArrayList<WeavePlan>();
		Map<String, Object> param=new HashMap<String, Object>();
		for(int i=0;i<li.length;i++){
			WeavePlan weavePlan=weavePlanDao.findById(WeavePlan.class, Long.valueOf(li[i]));
			weavePlan.setIsFinished(1);
			list.add(weavePlan);
			//完成了，从设备的计划中移除
			
			param.clear();
			param.put("weavePlanId", weavePlan.getId());
			
			delete(WeavePlanDevices.class, param);
		}
		weavePlanDao.update2(list.toArray(new WeavePlan[]{}));
	}

	@Override
	public void updateSort(String id,Long time) {
		weavePlanDao.updateSort(id,time);
		
	}

	public Map<String, Object> dataList(Filter filter, Page page) throws Exception {
		return weavePlanDao.dataList(filter, page);
	}
	
	public List<Map<String,Object>> ledWeavePlan(Integer workshopNo){
		List<Map<String,Object>> list=weavePlanDao.ledWeavePlan(workshopNo);
		/*Map<String, Object> count=null;
		for(Map<String,Object> map:list){
			count=weavePlanDao.countRollsAndTrays(MapUtils.getAsLong(map, "ID"));
			map.put("RC", MapUtils.getAsLong(count, "RC"));
			map.put("TC", MapUtils.getAsLong(count, "TC"));
		}*/
		return list;
	}

	@Override
	public Map<String, Object> findWeavePageInfo(Filter filter, Page page) {
		Map<String,Object> ret=weavePlanDao.findWeavePageInfo(filter, page);
		List<Map<String,Object>> list=(List<Map<String, Object>>) ret.get("rows");
		Map<String, Object> count=null;
		for(Map<String,Object> map:list){
			count=weavePlanDao.countRollsAndTrays(MapUtils.getAsLong(map, "ID"));
			map.put("RC", MapUtils.getAsLong(count, "RC"));
			map.put("TC", MapUtils.getAsLong(count, "TC"));
		}
		return ret;
	}

	@Override
	public Map<String, Object> findWeavePageInfo2(Filter filter, Page page) {
		Map<String,Object> ret=weavePlanDao.findWeavePageInfo2(filter, page);

		return ret;
	}

	@Override
	public Map<String, Object> findWeavePageInfo1(Filter filter, Page page) {
		return weavePlanDao.findWeavePageInfo1(filter,page);
	}

	@Override
	public void saveDevices(List<WeavePlanDevices> devices) {
		/*Map<String, Object> param=new HashMap<String,Object>();
		param.put("weavePlanId", devices.get(0).getWeavePlanId());
		delete(WeavePlanDevices.class, param);
		save(devices.toArray(new WeavePlanDevices[devices.size()]));*/
	}
	
	public Map<String, Object> findNofinish(Filter filter,Page page){
		Map<String,Object> ret=weavePlanDao.findNofinish(filter, page);
		List<Map<String,Object>> list=(List<Map<String, Object>>) ret.get("rows");
		Map<String, Object> count=null;
		for(Map<String,Object> map:list){
			count=countRollsAndTrays(MapUtils.getAsLong(map, "ID"));
			map.put("RC", MapUtils.getAsLong(count, "RC"));
			map.put("TC", MapUtils.getAsLong(count, "TC"));
		}
		return ret;
	}
	@Override
	public Map<String, Object> findfinished(Filter filter,Page page) {
		return weavePlanDao.findfinished(filter,page);
	}
	@Override
	public void updateSumWeight(Long id,Double weight){
		weavePlanDao.updateSumWeight(id, weight);
	}
	@Override
	public List<Map<String, Object>> findRollIsPro(String batchcode,String factoryProductName) throws SQLTemplateException{
		return weavePlanDao.findRollIsPro(batchcode,factoryProductName);
	}

	@Override
	public Map<String, Object> countRollsAndTrays(Long wid) {
		return weavePlanDao.countRollsAndTrays(wid);
	}
	
	@Override
	public List<Map<String, Object>> findRollisNoA(String batchcode){
		return weavePlanDao.findRollisNoA(batchcode);
	}

	@Override
	public String getWeavePlanDevices(Long wid) {
		return weavePlanDao.getWeavePlanDevices(wid);
	}
	
	public List<Map<String,Object>> getWeavePlanPackTask(Long wid){
		return weavePlanDao.getWeavePlanPackTask(wid);
	}

	@Override
	public Map<String,Object> getBjDetails(String planCode, String yx, String partname)
			throws SQLTemplateException {
		// TODO Auto-generated method stub
		return weavePlanDao.getBjDetails(planCode, yx, partname);
	}

	@Override
	public Map<String, Object> findDevicePlans(String dids) {
		// TODO Auto-generated method stub
		return weavePlanDao.findDevicePlans(dids);
	}
}

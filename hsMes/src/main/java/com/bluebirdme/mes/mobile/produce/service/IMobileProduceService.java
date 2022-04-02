package com.bluebirdme.mes.mobile.produce.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.planner.cut.entity.Iplan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.store.entity.Roll;

public interface IMobileProduceService extends IBaseService {
	//保存卷的产出进度
	public void updateRoll(Roll roll,Iplan w,ProducePlanDetail producePlanDetail)  throws Exception;
	//保存部件的产出
	@Deprecated
	public void updatePart(Roll roll);
	
	public List<Map<String,Object>> querylist(String deviceCode);
	public List<Map<String,Object>> querylist2(String deviceCode);
	public List<Map<String,Object>> queryYlbh(String ylbh, String oldweaveId, String deviceCode);
	
	public void deleteYlbh(String ylbh, String oldweaveId, String deviceCode);
	
	
	
	public void editInfo(String weaveid, String userid, String tlids);
}

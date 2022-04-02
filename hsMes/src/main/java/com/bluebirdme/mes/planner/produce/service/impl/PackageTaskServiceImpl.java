/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;


import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.produce.service.IPackageTaskService;
import com.bluebirdme.mes.planner.produce.dao.IPackageTaskDao;
import com.bluebirdme.mes.planner.produce.entity.PackageTask;

/**
 * 
 * @author 高飞
 * @Date 2017-5-26 10:04:05
 */
@Service
@AnyExceptionRollback
public class PackageTaskServiceImpl extends BaseServiceImpl implements IPackageTaskService {
	
	@Resource IPackageTaskDao packageTaskDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return packageTaskDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return packageTaskDao.findPageInfo(filter,page);
	}

	@Override
	public List<Map<String, Object>> findTasks(Long planDetailId) {
		return packageTaskDao.findTasks(planDetailId);
	}
	
	public List<Map<String, Object>> findPakcageInfo(Long bcBomId){
		return packageTaskDao.findPakcageInfo(bcBomId);
	}
	

	@Override
	public void saveTask(List<PackageTask> list) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("producePlanDetailId", list.get(0).getProducePlanDetailId());
		delete(PackageTask.class, map);
		BcBom bc=null;
		for(PackageTask task:list){
			bc=findById(BcBom.class, task.getPackageBomId());
			task.setPackageCode(bc.getPackBomCode()+"/"+bc.getPackBomGenericName()+"/每托"+bc.getPackBomRollsPerTray()+"卷");
			task.setRollCountPerTray(bc.getPackBomRollsPerTray()==null?0:bc.getPackBomRollsPerTray().intValue());
		}
		saveList(list);
	}

	@Override
	public void deleteTask(PackageTask task) {
		
	}

}

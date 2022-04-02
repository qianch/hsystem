/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.screenDisplay.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;

import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.MapUtils;
import org.xdemo.superutil.j2se.StringUtils;

import com.bluebirdme.mes.core.base.LanguageProvider;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.screenDisplay.dao.IScreenDisplayDao;
import com.bluebirdme.mes.screenDisplay.service.IScreenDisplayService;

/**
 * 
 * @author 宋黎明
 * @Date 2016-11-25 11:25:41
 */
@Service
@AnyExceptionRollback
public class ScreenDisplayServiceImpl extends BaseServiceImpl implements IScreenDisplayService {
	
	@Resource IScreenDisplayDao screenDisplayDao;
	
	@Override
	protected IBaseDao getBaseDao() {
		return screenDisplayDao;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return screenDisplayDao.findPageInfo(filter,page);
	}
	

	@Override
	public List<Map<String, Object>> findProductInfo(String ip) throws Exception {
		return screenDisplayDao.findProductInfo(ip);
	}
	
	@Override
	public List<Map<String, Object>> findFirstProductInfo(String ip) throws Exception {
		return screenDisplayDao.findFirstProductInfo(ip);
	}

	@Override
	public List<Map<String, Object>> findProduceNum(Long productId,Long deviceId) throws Exception {
		return screenDisplayDao.findProduceNum(productId,deviceId);
	}
	
	public List<Map<String,Object>> initCombox() throws Exception{
		List<Map<String,Object>> listMap=screenDisplayDao.initCombox();
		List<Map<String,Object>>_ret=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:listMap){
			Map<String,Object> ret=new HashMap<String, Object>();
			if(StringUtils.isBlank( MapUtils.getAsStringIgnoreCase(map,"MACHINESCREENNAME")))continue;
//			ret.put("id", MapUtils.getAsStringIgnoreCase(map,"MACHINESCREENIP"));
			ret.put("id", MapUtils.getAsStringIgnoreCase(map,"ID"));
			ret.put("text", MapUtils.getAsStringIgnoreCase(map,"MACHINESCREENNAME"));
			_ret.add(ret);
		}
		return _ret;
	}

}

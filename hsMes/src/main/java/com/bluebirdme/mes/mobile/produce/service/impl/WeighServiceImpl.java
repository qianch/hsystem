/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.mobile.produce.service.impl;

import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.mobile.produce.dao.IWeighDao;
import com.bluebirdme.mes.mobile.produce.service.IWeighService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Map;

/**
 * 称重业务
 * @author Goofy
 * @Date 2017年11月22日 下午1:38:13
 */
@Service
@AnyExceptionRollback
public class WeighServiceImpl extends BaseServiceImpl implements IWeighService {
	
	@Resource
	IWeighDao wd;

	@Override
	protected IBaseDao getBaseDao() {
		return wd;
	}

	@Override
	public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
		return null;
	}

	@Override
	public boolean shouldWeigh(String barcode, String deviceCode) throws ParseException{
		return wd.shouldWeigh(barcode, deviceCode);
	}

	@Override
	public double getAvg(Long planId, String deviceCode) {
		return wd.getAvg(planId, deviceCode);
	}

	@Override
	public boolean isGroupFirstRoll(Long planId, String deviceCode) throws ParseException{
		return wd.isGroupFirstRoll(planId, deviceCode);
	}

	@Override
	public boolean isNormalWeight(String barcode) {
		return wd.isNormalWeight(barcode);
	}

	@Override
	public int getDeviceRollIndex(Long planId, String deviceCode) {
		return wd.getDeviceRollIndex(planId, deviceCode);
	}
	
	/**
	 * 获取编织任务的卷序号
	 * @param planId
	 * @return
	 */
	public int getWeaveRollIndex(Long planId){
		return wd.getWeaveRollIndex(planId);
	}

	@Override
	public Double getRollTheoryWeight(String barcode) {
		return wd.getRollTheoryWeight(barcode);
	}

	@Override
	public int getRollWeighRule(String barcode) {
		return wd.getRollWeighRule(barcode);
	}
	
	/**
	 * 检查是否有未称重的卷
	 * @param planId
	 * @param deviceCode
	 * @return
	 */
	public String hasWaitForWeighRoll(Long planId,String deviceCode){
		return wd.hasWaitForWeighRoll(planId, deviceCode);
	}
	
	/**
	 * 保存称重的重量
	 * @param barcode
	 * @param weight
	 * @param qualityGrade
	 * @throws Exception
	 */
	public void saveWeight(String barcode, Double weight,String qualityGrade) throws Exception{
		wd.saveWeight(barcode, weight, qualityGrade);
	}
	
	public boolean validDevice(String barcode,String deviceCode){
		return wd.validDevice(barcode, deviceCode);
	}
}

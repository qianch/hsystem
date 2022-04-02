/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2017版权所有
 */
package com.bluebirdme.mes.mobile.produce.service;

import java.text.ParseException;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * 
 * @author Goofy
 * @Date 2017年11月22日 下午1:38:00
 */
public interface IWeighService extends IBaseService {
	/**
	 * 是否要称重
	 * 1：首卷必称
	 * 2：每卷必称
	 * 3：白班，晚班首卷必称
	 * @return
	 */
	public boolean shouldWeigh(String barcode,String deviceCode) throws ParseException;
	
	/**
	 * 获取平均值
	 * @return
	 */
	public double getAvg(Long planId,String deviceCode);
	
	
	/**
	 * 班组首卷
	 * @return
	 */
	public boolean isGroupFirstRoll(Long planId,String deviceCode) throws ParseException;
	
	/**
	 * 判断重量是否正常
	 * @return
	 */
	public boolean isNormalWeight(String barcode);
	
	/**
	 * 获取该机台卷序号
	 * @return
	 */
	public int getDeviceRollIndex(Long planId,String deviceCode);
	
	/**
	 * 获取编织任务的卷序号
	 * @param planId
	 * @return
	 */
	public int getWeaveRollIndex(Long planId);
	
	/**
	 * 根据条码获取理论重量
	 * @param barcode
	 * @return
	 */
	public Double getRollTheoryWeight(String barcode);
	
	/**
	 * 称重规则
	 * @param barcode
	 * @return
	 */
	public int getRollWeighRule(String barcode);
	
	/**
	 * 检查是否有未称重的卷
	 * @param planId
	 * @param deviceCode
	 * @return
	 */
	public String hasWaitForWeighRoll(Long planId,String deviceCode);
	
	/**
	 * 保存称重的重量
	 * @param barcode
	 * @param weight
	 * @param qualityGrade
	 * @throws Exception
	 */
	public void saveWeight(String barcode, Double weight,String qualityGrade) throws Exception;
	
	/**
	 * 校验是否在机台排产
	 * @param barcode
	 * @param deviceCode
	 * @return
	 */
	public boolean validDevice(String barcode,String deviceCode);
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * 
 * @author 徐秦冬
 * @Date 2017-12-6 16:30:30
 */
public interface IFtcBcBomVersionDetailService extends IBaseService {
	/**
	* delete 方法的简述.
	* 根据传入的非套材包材bom明细的id删除对应的非套材包材bom明细<br>
	* @param ids 类型:String，多个id用‘,’号分割
	* @return 无
	*/
	public void deleteAll(String ids) throws Exception;
}

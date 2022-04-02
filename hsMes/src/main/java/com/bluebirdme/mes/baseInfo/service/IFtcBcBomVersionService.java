/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * 
 * @author 徐秦冬
 * @Date 2017-12-6 16:26:52
 */
public interface IFtcBcBomVersionService extends IBaseService {
	/**
	* delete 方法的简述.
	* 根据传入的包材bom版本的id删除对应的包材bom版本和明细<br>
	* @param ids 类型:String，多个id用‘,’号分割
	* @return 无
	*/
	public void deleteAll(String ids) throws Exception;
	/**
	* getBcBomJson 方法的简述.
	* 根据传入的包材bom版本的id获得对应的json数据用于创建treeview节点<br>
	* @param ids 类型:String，单个id
	* @return List<Map<String, Object>> 
	*/
	public List<Map<String, Object>> getFtcBcBomJson(String id,String productType);
}

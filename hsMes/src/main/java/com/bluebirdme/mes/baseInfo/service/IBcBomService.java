/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

/**
 * 
 * @author 徐波
 * @Date 2016-10-8 16:53:24
 */
public interface IBcBomService extends IBaseService {
	/**
	* delete 方法的简述.
	* 根据传入的包材bom的id删除对应的包材bom和明细<br>
	* @param ids 类型:String，多个id用‘,’号分割
	* @return 无
	*/
	public void deleteAll(String ids) throws Exception;
	/**
	* getBcBomJson 方法的简述.
	* 获取包材bom的json格式数据，用于组装treeview<br>
	* @param 无
	* @return 类型:List<Map<String, Object>>，返回组装完成的包材bom的json数据
	*/
	public List<Map<String, Object>> getBcBomJson(String data);
	public List<Map<String, Object>> getBcBomJsonTest(String data)throws SQLTemplateException;
	public List<Map<String, Object>> getBcBomJsonTest1(String data)throws SQLTemplateException;
}

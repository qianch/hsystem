/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.baseInfo.entity.FtcBcBom;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

/**
 * 
 * @author 高飞
 * @Date 2017-11-28 11:10:48
 */
public interface IFtcBcBomService extends IBaseService {
	/**
	* delete 方法的简述.
	* 根据传入的非套材包材bom的id删除对应的非套材包材bom和明细<br>
	* @param ids 类型:String，多个id用‘,’号分割
	* @return 无
	*/
	public void deleteAll(String ids) throws Exception;
	/**
	* getFtcBcBomJson 方法的简述.
	* 获取非套材包材bom的json格式数据，用于组装treeview<br>
	* @param 无
	* @return 类型:List<Map<String, Object>>，返回组装完成的非套材包材bom的json数据
	*/
	public List<Map<String, Object>> getFtcBcBomJson(String pid,String queryData,Integer level);
	
	/**
	 * 添加保存非套材包材(二级)并导入Excel文件中的数据(三级包装、版本，版本明细)
	 * @param ftcBcBom		非套材包材BOM(二级)
	 * @param fileId		Excel文件id
	 * @return				返回Excel导入不成功时的错误信息
	 * @throws Exception
	 */
	public ExcelImportMessage doAddFtcBcBom(FtcBcBom ftcBcBom,Long fileId) throws Exception;
	
	/**
	 * 修改非套材包材BOM(二级)和增补导入Excel文件中的数据(三级包装、版本，版本明细)
	 * @param ftcBcBom		非套材包材BOM(二级)
	 * @param fileId		Excel文件id
	 * @param info			存放Excel导入时忽略导入的信息
	 * @return				返回Excel导入不成功时的错误信息
	 * @throws Exception
	 */
	public ExcelImportMessage doUpdateFtcBcBom(FtcBcBom ftcBcBom,Long fileId,StringBuffer info) throws Exception;
	
	public void savePdfFile(FtcBcBom ftcBcBom, Long fileId, ExcelImportMessage eim) throws Exception;
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;

/**
 * 
 * @author 宋黎明
 * @Date 2016-10-8 13:36:52
 */
public interface IFtcBomService extends IBaseService {
	
	//删除工艺BOM及它的所有节点
	public void delete(String ids);
	//删除BOM明细
	public void deleteDetail(String ids);
	//删除BOM版本及它的明细
	public void deleteBomVersion(String ids);
	//获取工艺BOM数据
	public List<Map<String, Object>> getFtcBomJson(String data);
	public List<Map<String, Object>> getFtcBomJsonTest(String data);
	public List<Map<String, Object>> getFtcBomJsonTest1(String data);
	//获取BOM版本数据
	public List<Map<String, Object>> getFtcBomByVersionJson(String id);
	//拷贝BOM版本
	public void toCompliteCopy(String id)throws Exception;
	public void updateFtcInfos(int value);
	
	/**
	 * 添加保存非套材BOM数据、BOM版本、BOM明细
	 * @param ftcBom			非套材BOM数据
	 * @param ftcBomVersion		非套材BOM版本
	 * @param fileId			Excel文件id
	 * @return					返回Excel导入不成功时的错误信息
	 * @throws Exception
	 */
	public ExcelImportMessage doAddFtcBom(FtcBom ftcBom,FtcBomVersion ftcBomVersion,Long fileId) throws Exception;
	
	/**
	 * 更新保存非套材BOM版本、BOM明细
	 * @param ftcBomVersion		非套材BOM版本
	 * @param fileId			Excel文件id
	 * @return					返回Excel导入不成功时的错误信息
	 * @throws Exception
	 */
	public ExcelImportMessage doUpdateFtcBomVersion(FtcBomVersion ftcBomVersion,Long fileId) throws Exception;

	Map<String, Object> findPageInfo1(Filter filter, Page page);
}

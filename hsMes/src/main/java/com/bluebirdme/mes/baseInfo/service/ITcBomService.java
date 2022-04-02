/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.baseInfo.entity.TcBom;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.sql.SQLTemplateException;

/**
 * 
 * @author 肖文彬
 * @Date 2016-10-9 9:19:51
 */
public interface ITcBomService extends IBaseService {
	//查询Bom
	public List<Map<String, Object>> findBom(String data,String state)throws SQLTemplateException;
	public List<Map<String, Object>> getTcBomJsonTest(String data,String state)throws SQLTemplateException;
	//获取工艺Bom下的版本
	public List<Map<String, Object>> findV(String id);
	//查询一级部件
	public List<Map<String, Object>> findP(String id);
	//删除部件
	public void deleteP(String ids);
	//查出一级部件的子部件
	public List<Map<String, Object>> findPC(String id,String vId);
	//复制版本树
	public void toCopy(String id)throws Exception;
	public void toCopyBom(String id)throws Exception;
	//删除工艺Bom以及下面的所有
	public void deleteB(String id);
	//删除版本以及下面的所有
	public void deleteV(String id);
	//查询版本下的部件
	public List<Map<String,Object>> find1(String id);
	//查询成品ftc信息
//	public List<Map<String,Object>> findFtc(String id);
	public Map<String,Object> findFtc(Filter filter, Page page);
	//查询bom客户信息
	public List<Map<String,Object>> findConsumer(String id);
	public List<Map<String, Object>> getTcBomJsonTest1(String data,String state) throws SQLTemplateException;
	
	/**
	 * 添加保存套材BOM数据、BOM版本、BOM版本部件、版本部件明细、部件成品重量胚布信息
	 * @param tcBom				套材BOM
	 * @param tcBomVersion		套材BOM版本
	 * @param fileId			Excel文件id
	 * @return					返回Excel导入不成功时的错误信息
	 * @throws Exception
	 */
	public ExcelImportMessage doAddTcBom(TcBom tcBom,TcBomVersion tcBomVersion,Long fileId)throws Exception;
	
	/**
	 * 更新保存非套材BOM版本、BOM版本部件、版本部件明细、部件成品重量胚布信息
	 * @param tcBomVersion		套材BOM版本
	 * @param fileId			Excel文件id
	 * @return					返回Excel导入不成功时的错误信息
	 * @throws Exception
	 */
	public ExcelImportMessage doUpdateTcBomVersion(TcBomVersion tcBomVersion,Long fileId)throws Exception;
	
	public void savePdfFile(TcBom tcBom, TcBomVersion tcBomVersion, Long fileId, ExcelImportMessage eim) throws Exception ;
}

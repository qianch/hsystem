/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author 徐波
 * @Date 2016-10-8 16:53:24
 */
public interface IBCBomVersionService extends IBaseService {
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
	public List<Map<String, Object>> getBcBomJson(String id );
	/**
	* toCompliteCopy 方法的简述.
	* 根据传入的包材bom版本的id复制包材bom版本<br>
	* @param ids 类型:String，单个id
	* @return 无
	*/
	public void toCompliteCopy(String id)throws Exception;
	public void toCompliteCopyBom(String id) throws Exception;
	public void toCompliteCopy(String id,String versionName) throws Exception;
	
	public void savePdfFile(BCBomVersion bcBomVersion, Long fileId, ExcelImportMessage eim) throws Exception;

    String importBcBomMainUploadFile(MultipartFile file, String userId,String packVersionId) throws IOException;
}

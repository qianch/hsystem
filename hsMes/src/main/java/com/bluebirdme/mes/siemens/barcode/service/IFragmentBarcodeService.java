/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.siemens.barcode.service;

import java.util.List;
import java.util.Map;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * 
 * @author 高飞
 * @Date 2017-8-3 20:38:40
 */
public interface IFragmentBarcodeService extends IBaseService {
	public void extraPrint(String barcodes,String printer,String user,String reason) throws Exception;
	public void suit(String ctoCode,String part,String fragments,String user,String device) throws Exception;
	public List<String> getFeedingFarbic(Long cutId);
	public List<Map<String,Object>> getSuitInfo(String code);
}

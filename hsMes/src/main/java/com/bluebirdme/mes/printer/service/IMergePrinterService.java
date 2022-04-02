/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.printer.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.printer.entity.PrinterOut;
import com.bluebirdme.mes.store.entity.IBarcode;

import java.util.List;

/**
 * 
 * @author 徐波
 * @Date 2016-11-14 15:40:51
 */
public interface IMergePrinterService extends IBaseService {

	public PrinterOut getQRBarCode(List<IBarcode> barcode, String printerName, String btwFileUrl, String txtFileUrl);

	public String doIndividualPrintBarcode(String weavePlanId,
                                           String cutPlanId, String count, String pName, String type,
                                           String partName, String departmentCode, String trugPlanId, Long partId, String btwfileId, String devCode,String copies) throws Exception;


	/**
	 * 打印空的条码新
	 * @param departmentCode 0:裁剪车间，1：编织1车间，2：编织2车间，3：编织3车间
	 * @param pName 打印机名称
	 * @param type 条码类型：roll：卷条码，box：盒条码，tray：托条码，part：部件条码
	 * @param count 打印数量
	 * @return
	 */
	public String doPrintBarcode(String weavePlanId, String departmentCode, String pName, String type, int count, long partId, String btwfileId) throws Exception ;

	/**
	 * 打印个性化条码标签
	 * @param id  条码id
     * @param type 条码类型：roll：卷条码，box：盒条码，tray：托条码，part：部件条码
	 * @param pName 打印机名称
	 * @param btwfileId 个性化标签模版
	 * @return
	 */
	public  String doReplayPrintBarcode(String id, String type, String departmentCode, String pName, String btwfileId)throws Exception ;

	public String rePrint(String ids, String pName, String type);

	public String reIndividualPrint(String id, String pName, String type, long btwfileId, int printCount);

}

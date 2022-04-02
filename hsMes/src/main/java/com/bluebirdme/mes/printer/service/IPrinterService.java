/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.printer.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.print.PrintException;

import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.cut.entity.Iplan;
import com.bluebirdme.mes.printer.entity.MyException;
import com.bluebirdme.mes.printer.entity.Printer;
import com.bluebirdme.mes.printer.entity.PrinterOut;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.store.entity.IBarcode;

/**
 *
 * @author 徐波
 * @Date 2016-11-14 15:40:51
 */
public interface IPrinterService extends IBaseService {
	public List<IBarcode> getOutputString(Iplan plan, String type, int count, String printName, String departmentName, Long partId, String trugPlanId) throws SQLTemplateException, MyException;
	public PrinterOut getQRBarCode(List<IBarcode> barcode,String printerName,String btwFileUrl,String txtFileUrl);
	public String doPrintBarcodeByPage(String weavePlanId,
			String cutPlanId, String count, String pName, String type,
			String partName,String departmentName,String trugPlanId,Long partId,String copies) throws Exception;

	public String rePrint(String ids,String pName, String type);
	/**
	 * 补打质量等级不为A或者判为废品的产品的新条码，用于处理订单和任务已经完成后的追加生产
	 * @param ids TotalStatistics的id集合
	 * @param pName 打印机名称
	 * @return
	 */
	public String buda(String ids, String pName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;
	/**
	 * 打印空的条码
	 * @param departmentType 0:裁剪车间，1：编织1车间，2：编织2车间，3：编织3车间
	 * @param pName 打印机名称
	 * @param type 条码类型：roll：卷条码，box：盒条码，tray：托条码，part：部件条码
	 * @param count 打印数量
	 * @return
	 */
	public String printBarcodeFirst(String departmentType,String pName, String type,int count,int copies)throws Exception;

	public void printOrderInfo(String weavePlanId, String cutPlanId, String printer) throws Exception;

	public String doPrintBarcodeByPageList(String ids,
									   String cutPlanId, String count, String pName, String type,
									   String partName,String departmentName,String trugPlanId,Long partId) throws Exception;

	public void insert(Object... object) throws Exception;
}

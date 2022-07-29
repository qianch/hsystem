/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.printer.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.cut.entity.Iplan;
import com.bluebirdme.mes.printer.entity.MyException;
import com.bluebirdme.mes.printer.entity.PrinterOut;
import com.bluebirdme.mes.store.entity.IBarcode;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author 徐波
 * @Date 2016-11-14 15:40:51
 */
public interface IPrinterService extends IBaseService {
    List<IBarcode> getOutputString(Iplan plan, String type, int count, String printName, String departmentName, Long partId, String trugPlanId) throws SQLTemplateException, MyException;

    PrinterOut getQRBarCode(List<IBarcode> barcode, String printerName, String btwFileUrl, String txtFileUrl);

    String doPrintBarcodeByPage(String weavePlanId,
                                String cutPlanId, String count, String pName, String type,
                                String partName, String departmentName, String trugPlanId, Long partId, String copies) throws Exception;

    String rePrint(String ids, String pName, String type);

    /**
     * 补打质量等级不为A或者判为废品的产品的新条码，用于处理订单和任务已经完成后的追加生产
     *
     * @param ids   TotalStatistics的id集合
     * @param pName 打印机名称
     * @return
     */
    String buda(String ids, String pName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;

    /**
     * 打印空的条码
     *
     * @param departmentType 0:裁剪车间，1：编织1车间，2：编织2车间，3：编织3车间
     * @param pName          打印机名称
     * @param type           条码类型：roll：卷条码，box：盒条码，tray：托条码，part：部件条码
     * @param count          打印数量
     * @return
     */
    String printBarcodeFirst(String departmentType, String pName, String type, int count, int copies) throws Exception;

    void printOrderInfo(String weavePlanId, String cutPlanId, String printer) throws Exception;

    String doPrintBarcodeByPageList(String ids,
                                    String cutPlanId, String count, String pName, String type,
                                    String partName, String departmentName, String trugPlanId, Long partId) throws Exception;

    void insert(Object... object) throws Exception;
}

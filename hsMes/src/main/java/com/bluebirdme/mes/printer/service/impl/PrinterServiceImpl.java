/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.printer.service.impl;

import com.bluebirdme.mes.baseInfo.entity.TcBom;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsMirror;
import com.bluebirdme.mes.baseInfo.service.IBomService;
import com.bluebirdme.mes.cache.DepartmentCache;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlanDetail;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.cut.entity.Iplan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.printer.PrintBarCode;
import com.bluebirdme.mes.printer.dao.IPrinterDao;
import com.bluebirdme.mes.printer.entity.DailyPrintCount;
import com.bluebirdme.mes.printer.entity.Printer;
import com.bluebirdme.mes.printer.entity.PrinterOut;
import com.bluebirdme.mes.printer.service.IPrinterService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.utils.PrintUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.*;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-11-14 15:40:51
 */
@Service
@AnyExceptionRollback
public class PrinterServiceImpl extends BaseServiceImpl implements IPrinterService {
    private static Logger logger = LoggerFactory.getLogger(PrinterServiceImpl.class);
    private String dataUrl = PathUtils.getDrive() + "BtwFiles" + File.separator + "Data" + File.separator;
    private String fileUrl = new File(PathUtils.getClassPath()) + File.separator + "BtwFiles" + File.separator;
    private static String btwName = null;
    byte[] dotfont;

    @Resource
    IPrinterDao printerDao;

    @Resource
    ISalesOrderService salesOrderService;

    @Resource
    IBomService bomService;

    @Resource
    IWeavePlanService weavePlanService;

    @Override
    protected IBaseDao getBaseDao() {
        return printerDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return printerDao.findPageInfo(filter, page);
    }

    /**
     * @param barcode 根据传入的barcode列表中的元素的类型判断应该打印的二维码的字符串
     * @return 要打印的二维码的字符串列表
     */
    public PrinterOut getQRBarCode(List<IBarcode> barcode, String printerName, String btwFileUrl, String txtFileUrl) {
        PrinterOut out = new PrinterOut();
        out.setBtwFileUrl(btwFileUrl);
        out.setPrinterName(printerName);
        out.setTxtFileUrl(txtFileUrl);
        out.setLi(barcode);

        return out;
    }

    public PrinterOut getQRBarCodeByList(List<IBarcode> barcode, String printerName, String btwFileUrl, String txtFileUrl) {
        PrinterOut out = new PrinterOut();
        out.setBtwFileUrl(btwFileUrl);
        out.setPrinterName(printerName);
        out.setTxtFileUrl(txtFileUrl);
        out.setLi(barcode);
        return out;
    }

    public PrinterOut getContentByList(List<String> contents, String printerName, String btwFileUrl, String txtFileUrl) {
        PrinterOut out = new PrinterOut();
        out.setBtwFileUrl(btwFileUrl);
        out.setPrinterName(printerName);
        out.setTxtFileUrl(txtFileUrl);
        out.setContents(contents);
        return out;
    }

    public IBarcode getBarcode(String type, String dcString) {
        IBarcode barcode;
        Calendar c = Calendar.getInstance();

        String year = "000" + (c.get(Calendar.YEAR) - 1999);
        String day = "00" + c.get(Calendar.DAY_OF_YEAR);

        year = year.substring(year.length() - 3);
        day = day.substring(day.length() - 3);

        String dateString = year + day;

        if (type.equals("roll")) {
            barcode = new RollBarcode();
        } else if (type.equals("tray")) {
            barcode = new TrayBarCode();
        } else if (type.equals("trayPart")) {
            barcode = new TrayBarCode();
        } else if (type.equals("box")) {
            barcode = new BoxBarcode();
        } else {
            barcode = new PartBarcode();
        }
        barcode.setBarcode(dcString);
        barcode.setSalesOrderCode("");
        barcode.setSalesProductId(null);
        barcode.setBatchCode("");
        barcode.setPartName("");
        barcode.setPlanId(null);
        barcode.setPrintDate(new Date());
        // 拼装要输出的信息内容 outputString
        String outputString = "";
        outputString += " ";
        outputString += "\t";
        // System.out.println("厂内名称"+ fdp.getConsumerProductName());
        outputString += " ";
        outputString += "\t";
        outputString += " ";
        outputString += "\t";
        outputString += " ";
        outputString += "\t";
        outputString += barcode.getBarcode();
        outputString += "\t";

        outputString += " ";
        outputString += "\t";
        outputString += " ";
        outputString += "\t";
        outputString += " ";
        outputString += "\t";
        outputString += dateString;
        outputString += "\t";
        outputString += " ";
        outputString += "\t";
        outputString += barcode.getBarcode();
        outputString += ";";
        // outputString += plan.getProductModel();
        outputString += " ";
        outputString += ";";
        // outputString += plan.getBatchCode();
        outputString += " ";
        outputString += ";";
        // outputString += plan.getSalesOrderCode();
        outputString += " ";
        outputString += ";";
        outputString += " ";
        outputString += "\n";
        barcode.setOutPutString(outputString);
        return barcode;
    }

    public IBarcode getBarcode(Iplan plan, String type, String code, Long partId) {
        IBarcode barcode;
        Calendar c = Calendar.getInstance();

        String year = "000" + (c.get(Calendar.YEAR) - 1999);
        String day = "00" + c.get(Calendar.DAY_OF_YEAR);

        year = year.substring(year.length() - 3);
        day = day.substring(day.length() - 3);

        String dateString = year + day;
        HashMap<String, Object> map = new HashMap();

        SalesOrderDetail salesOrderDetail = findById(SalesOrderDetail.class, plan.getFromSalesOrderDetailId());
        List<FinishedProductMirror> finishedProductMirrorList;
        FinishedProductMirror fdm = null;
        map.clear();
        map.put("productId", plan.getProductId());
        map.put("salesOrderDetailId", salesOrderDetail.getId());
        finishedProductMirrorList = findListByMap(FinishedProductMirror.class, map);
        if (finishedProductMirrorList.size() > 0) {
            fdm = finishedProductMirrorList.get(0);
        } else {
            map.clear();
            map.put("productId", plan.getProductId());
            map.put("salesOrderId", salesOrderDetail.getSalesOrderId());
            finishedProductMirrorList = findListByMap(FinishedProductMirror.class, map);
            if (finishedProductMirrorList.size() > 0) {
                fdm = finishedProductMirrorList.get(0);
            }
        }

        if (null != fdm) {
            if (type.equals("roll") || type.equals("roll_peibu")) {
                barcode = new RollBarcode();
                barcode.setMirrorProcBomId(finishedProductMirrorList.get(0).getProcBomId());
            } else if (type.equals("tray") || type.equals("traypart")) {
                barcode = new TrayBarCode();
            } else if (type.equals("box")) {
                barcode = new BoxBarcode();
            } else {
                barcode = new PartBarcode();
                barcode.setMirrorProcBomId(finishedProductMirrorList.get(0).getProcBomId());
            }
            barcode.setProducePlanDetailId(plan.getProducePlanDetailId());
            barcode.setBarcode(code);
            barcode.setSalesOrderCode(plan.getSalesOrderCode());
            barcode.setSalesProductId(plan.getProductId());
            barcode.setBatchCode(plan.getBatchCode());
            barcode.setPartName(plan.getPartName());
            barcode.setSalesOrderDetailId(plan.getFromSalesOrderDetailId());
            if (partId == null || partId < 0) {
                barcode.setPartId(null);
            } else {

                barcode.setPartId(partId);
            }
            barcode.setPlanId(plan.getId());
            barcode.setPrintDate(new Date());
            barcode.setSalesProductId(plan.getProductId());
            String danXiangBuCode = "";// 层数，卷号
            String partName = "";
            if (plan instanceof WeavePlan && plan.getPartId() != null) {
                WeavePlan wp = (WeavePlan) plan;
                try {
                    danXiangBuCode += StringUtils.isBlank(wp.getRollNo()) ? "" : ("卷号" + wp.getRollNo() + " ");
                    danXiangBuCode += StringUtils.isBlank(wp.getLevelNo()) ? "" : ("层号" + wp.getLevelNo() + " ");
                    danXiangBuCode += StringUtils.isBlank(wp.getDrawNo()) ? "" : ("图号" + wp.getDrawNo());
                    partName = wp.getPartName();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            } else {
                if (partId != null && partId > 0) {
                    map.clear();
                    map.put("salesOrderDetailId", salesOrderDetail.getId());
                    map.put("versionPartsId", partId);
                    List<TcBomVersionPartsMirror> partsMirrorList = findListByMap(TcBomVersionPartsMirror.class, map);
                    if (partsMirrorList.size() > 0) {
                        partName = partsMirrorList.get(0).getTcProcBomVersionPartsName();
                    }
                }
            }
            // 拼装要输出的信息内容 outputString
            String outputString = "";
            if (type.equals("roll_peibu")) {
                WeavePlan wp = findById(WeavePlan.class, plan.getId());
                TcBomVersionParts tbp = null;
                TcBomVersion tbv = null;
                TcBom tb = null;

                try {
                    tbp = findById(TcBomVersionParts.class, partId);
                    tbv = findById(TcBomVersion.class, tbp.getTcProcBomVersoinId());
                    tb = findById(TcBom.class, tbv.getTcProcBomId());
                    outputString += tb.getTcProcBomName().trim() + "  " + tbp.getTcProcBomVersionPartsName().trim();
                } catch (Exception e) {
                    outputString += "** " + wp.getPartName();
                }

                String rollNo = "";
                if (wp.getRollNo() != null) {
                    rollNo = wp.getRollNo().trim();
                }

                String levelNo = "";
                if (wp.getLevelNo() != null) {
                    levelNo = wp.getLevelNo().trim();
                }

                String drawNo = "";
                if (wp.getDrawNo() != null) {
                    drawNo = wp.getDrawNo().trim();
                }
                FinishedProductMirror fp = null;
                map.clear();
                map.put("productId", plan.getProductId());
                map.put("salesOrderDetailId", salesOrderDetail.getId());
                finishedProductMirrorList = findListByMap(FinishedProductMirror.class, map);
                if (finishedProductMirrorList.size() > 0) {
                    fp = finishedProductMirrorList.get(0);
                }
                if (finishedProductMirrorList.size() == 0) {
                    map.clear();
                    map.put("productId", plan.getProductId());
                    map.put("salesOrderId", salesOrderDetail.getSalesOrderId());
                    finishedProductMirrorList = findListByMap(FinishedProductMirror.class, map);
                    if (finishedProductMirrorList.size() > 0) {
                        fp = finishedProductMirrorList.get(0);
                    }
                }

                outputString += "\t" + fp.getFactoryProductName().trim() + "\t"
                        + barcode.getBarcode() + "  "
                        + plan.getProductLength() + "M\t"
                        + rollNo + "\t"
                        + levelNo + "\t"
                        + drawNo + "\t"
                        + barcode.getBarcode() + ";;;;";
            } else {
                outputString += fdm.getFactoryProductName().trim() + (StringUtils.isEmpty(partName) ? "" : ("(" + partName.trim() + ")"));
                outputString += "\t";
                // System.out.println("厂内名称"+ fdp.getConsumerProductName());
                outputString += fdm.getConsumerProductName().trim() + " " + (barcode.getPartName() == null ? "" : barcode.getPartName().trim());
                outputString += "\t";
                outputString += plan.getSalesOrderCode().trim();
                outputString += "\t";

                outputString += danXiangBuCode.trim();
                outputString += "\t";
                outputString += barcode.getBarcode();
                outputString += "\t";
                if (fdm.getProductRollLength() != null) {
                    outputString += fdm.getProductRollLength();
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                if (fdm.getProductRollWeight() != null) {
                    outputString += fdm.getProductRollWeight();
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                if (plan.getPartId() != null && plan.getPartId() > 0) {
                    map.clear();
                    map.put("versionPartsId", plan.getPartId());
                    map.put("tcProcBomVersoinId", plan.getMirrorProcBomId());
                    map.put("salesOrderDetailId", salesOrderDetail.getId());
                    TcBomVersionPartsMirror tParts = findUniqueByMap(TcBomVersionPartsMirror.class, map);
                    if (tParts != null) {
                        outputString += tParts.getTcProcBomVersionPartsName().trim();
                    }
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                outputString += dateString;
                outputString += "\t";
                outputString += plan.getProductModel().trim();
                outputString += "\t";
                outputString += barcode.getBarcode();
                outputString += ";";
                // outputString += plan.getProductModel();
                outputString += " ";
                outputString += ";";
                // outputString += plan.getBatchCode();
                outputString += " ";
                outputString += ";";
                // outputString += plan.getSalesOrderCode();
                outputString += " ";
                outputString += ";";
                // Base64 b = new Base64();
                // String s =
                // b.byteArrayToBase64(plan.getConsumerName().getBytes());
                outputString += " ";
                outputString += "\n";
            }
            barcode.setOutPutString(outputString);
            return barcode;
        } else {
            FinishedProduct fdp = findById(FinishedProduct.class, plan.getProductId());

            if (type.equals("roll") || type.equals("roll_peibu")) {
                barcode = new RollBarcode();
            } else if (type.equals("tray")) {
                barcode = new TrayBarCode();
            } else if (type.equals("box")) {
                barcode = new BoxBarcode();
            } else {
                barcode = new PartBarcode();
            }
            barcode.setProducePlanDetailId(plan.getProducePlanDetailId());
            barcode.setBarcode(code);
            barcode.setSalesOrderCode(plan.getSalesOrderCode());
            barcode.setSalesProductId(plan.getProductId());
            barcode.setBatchCode(plan.getBatchCode());
            barcode.setPartName(plan.getPartName());
            barcode.setSalesOrderDetailId(plan.getFromSalesOrderDetailId());
            if (partId == null || partId < 0) {
                barcode.setPartId(null);
            } else {
                barcode.setPartId(partId);
            }
            barcode.setPlanId(plan.getId());
            barcode.setPrintDate(new Date());
            barcode.setSalesProductId(plan.getProductId());
            String danXiangBuCode = "";// 层数，卷号
            String partName = "";
            if (plan instanceof WeavePlan && plan.getPartId() != null) {
                WeavePlan wp = (WeavePlan) plan;
                try {
                    danXiangBuCode += StringUtils.isBlank(wp.getRollNo()) ? "" : ("卷号" + wp.getRollNo() + " ");
                    danXiangBuCode += StringUtils.isBlank(wp.getLevelNo()) ? "" : ("层号" + wp.getLevelNo() + " ");
                    danXiangBuCode += StringUtils.isBlank(wp.getDrawNo()) ? "" : ("图号" + wp.getDrawNo());
                    partName = wp.getPartName();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            } else {
                if (partId != null && partId > 0) {
                    TcBomVersionParts part = findById(TcBomVersionParts.class, partId);
                    if (part != null)
                        partName = part.getTcProcBomVersionPartsName();
                }
            }
            // 拼装要输出的信息内容 outputString
            String outputString = "";
            if (type.equals("roll_peibu")) {
                WeavePlan wp = findById(WeavePlan.class, plan.getId());
                TcBomVersionParts tbp = null;
                TcBomVersion tbv = null;
                TcBom tb = null;

                try {
                    tbp = findById(TcBomVersionParts.class, partId);
                    tbv = findById(TcBomVersion.class, tbp.getTcProcBomVersoinId());
                    tb = findById(TcBom.class, tbv.getTcProcBomId());
                    outputString += tb.getTcProcBomName().trim() + "  " + tbp.getTcProcBomVersionPartsName().trim();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                    outputString += "** " + wp.getPartName();
                }

                String rollNo = "";
                if (wp.getRollNo() != null) {
                    rollNo = wp.getRollNo().trim();
                }

                String levelNo = "";
                if (wp.getLevelNo() != null) {
                    levelNo = wp.getLevelNo().trim();
                }

                String drawNo = "";
                if (wp.getDrawNo() != null) {
                    drawNo = wp.getDrawNo().trim();
                }

                FinishedProduct fp = findById(FinishedProduct.class, wp.getProductId());
                outputString += "\t" + fp.getFactoryProductName().trim() + "\t"
                        + barcode.getBarcode() + "  "
                        + plan.getProductLength() + "M\t"
                        + rollNo + "\t"
                        + levelNo + "\t"
                        + drawNo + "\t"
                        + barcode.getBarcode() + ";;;;";
            } else {
                outputString += fdp.getFactoryProductName().trim() + (StringUtils.isEmpty(partName) ? "" : ("(" + partName.trim() + ")"));
                outputString += "\t";
                outputString += fdp.getConsumerProductName().trim() + " " + (barcode.getPartName() == null ? "" : barcode.getPartName().trim());
                outputString += "\t";
                outputString += plan.getSalesOrderCode().trim();
                outputString += "\t";
                outputString += danXiangBuCode.trim();
                outputString += "\t";
                outputString += barcode.getBarcode();
                outputString += "\t";
                if (fdp.getProductRollLength() != null) {
                    outputString += fdp.getProductRollLength();
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                if (fdp.getProductRollWeight() != null) {
                    outputString += fdp.getProductRollWeight();
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                if (plan.getPartId() != null && plan.getPartId() > 0) {
                    TcBomVersionParts tParts = bomService.findById(TcBomVersionParts.class, plan.getPartId());
                    if (tParts != null) {
                        outputString += tParts.getTcProcBomVersionPartsName().trim();
                    }
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                outputString += dateString;
                outputString += "\t";
                outputString += plan.getProductModel().trim();
                outputString += "\t";
                outputString += barcode.getBarcode();
                outputString += ";";
                // outputString += plan.getProductModel();
                outputString += " ";
                outputString += ";";
                // outputString += plan.getBatchCode();
                outputString += " ";
                outputString += ";";
                // outputString += plan.getSalesOrderCode();
                outputString += " ";
                outputString += ";";
                // Base64 b = new Base64();
                // String s =
                // b.byteArrayToBase64(plan.getConsumerName().getBytes());
                outputString += " ";
                outputString += "\n";
            }
            barcode.setOutPutString(outputString);
            return barcode;
        }
    }

    public List<IBarcode> getOutputString(String departmentType, String pName, String type, int count) throws SQLTemplateException {
        List<IBarcode> li = new ArrayList();
        String prefixPrint = "HS";
        Department department = DepartmentCache.getInstance().getDepartmentList().get(departmentType);
        prefixPrint = department == null ? "" : department.getPrefix();

        if (prefixPrint.startsWith("BZ") && type == "trayPart") {
            prefixPrint = "HS" + prefixPrint.substring(2);
        }

        List<String> barcodelist = PrintBarCode.getInstance().getBarCodeList(type, prefixPrint, count);
        for (String barcode : barcodelist) {
            li.add(getBarcode(type, barcode));
        }

        for (IBarcode i : li) {
            if (i instanceof RollBarcode) {
                save((RollBarcode) i);
            } else if (i instanceof BoxBarcode) {
                save((BoxBarcode) i);
            } else if (i instanceof TrayBarCode) {
                save((TrayBarCode) i);
            } else if (i instanceof PartBarcode) {
                save((PartBarcode) i);
            }
        }
        return li;
    }

    public IBarcode getBarcodeList(Iplan plan, String type, String dcString, Long partId) {
        IBarcode barcode;
        Calendar c = Calendar.getInstance();
        String year = "000" + (c.get(Calendar.YEAR) - 1999);
        String day = "00" + c.get(Calendar.DAY_OF_YEAR);
        year = year.substring(year.length() - 3);
        day = day.substring(day.length() - 3);
        String dateString = year + day;
        HashMap<String, Object> map = new HashMap<>();
        SalesOrderDetail salesOrderDetail = findById(SalesOrderDetail.class, plan.getFromSalesOrderDetailId());
        List<FinishedProductMirror> finishedProductMirrorList;
        FinishedProductMirror fdm = null;
        map.clear();
        map.put("productId", plan.getProductId());
        map.put("salesOrderDetailId", salesOrderDetail.getId());
        finishedProductMirrorList = findListByMap(FinishedProductMirror.class, map);
        if (finishedProductMirrorList.size() > 0) {
            fdm = finishedProductMirrorList.get(0);
        } else {
            map.clear();
            map.put("productId", plan.getProductId());
            map.put("salesOrderId", salesOrderDetail.getSalesOrderId());
            finishedProductMirrorList = findListByMap(FinishedProductMirror.class, map);
            if (finishedProductMirrorList.size() > 0) {
                fdm = finishedProductMirrorList.get(0);
            }
        }

        if (null != fdm) {
            if (type.equals("roll") || type.equals("roll_peibu")) {
                barcode = new RollBarcode();
                barcode.setMirrorProcBomId(finishedProductMirrorList.get(0).getProcBomId());
            } else if (type.equals("tray")) {
                barcode = new TrayBarCode();
            } else if (type.equals("box")) {
                barcode = new BoxBarcode();
            } else {
                barcode = new PartBarcode();
                barcode.setMirrorProcBomId(finishedProductMirrorList.get(0).getProcBomId());
            }
            barcode.setProducePlanDetailId(plan.getProducePlanDetailId());
            barcode.setBarcode(dcString);
            barcode.setSalesOrderCode(plan.getSalesOrderCode());
            barcode.setSalesProductId(plan.getProductId());
            barcode.setBatchCode(plan.getBatchCode());
            barcode.setPartName(plan.getPartName());
            barcode.setSalesOrderDetailId(plan.getFromSalesOrderDetailId());
            if (partId == null || partId < 0) {
                barcode.setPartId(null);
            } else {
                barcode.setPartId(partId);
            }
            barcode.setPlanId(plan.getId());
            barcode.setPrintDate(new Date());
            barcode.setSalesProductId(plan.getProductId());
            String danXiangBuCode = "";// 层数，卷号
            String partName = "";
            if (plan instanceof WeavePlan && plan.getPartId() != null) {
                WeavePlan wp = (WeavePlan) plan;
                try {
                    danXiangBuCode += StringUtils.isBlank(wp.getRollNo()) ? "" : ("卷号" + wp.getRollNo() + " ");
                    danXiangBuCode += StringUtils.isBlank(wp.getLevelNo()) ? "" : ("层号" + wp.getLevelNo() + " ");
                    danXiangBuCode += StringUtils.isBlank(wp.getDrawNo()) ? "" : ("图号" + wp.getDrawNo());
                    partName = wp.getPartName();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            } else {
                if (partId != null && partId > 0) {
                    map.clear();
                    map.put("salesOrderDetailId", salesOrderDetail.getId());
                    map.put("versionPartsId", partId);
                    List<TcBomVersionPartsMirror> partsMirrorList = findListByMap(TcBomVersionPartsMirror.class, map);
                    if (partsMirrorList.size() > 0) {
                        partName = partsMirrorList.get(0).getTcProcBomVersionPartsName();
                    }
                }
            }
            // 拼装要输出的信息内容 outputString
            String outputString = "";
            if (type.equals("roll_peibu")) {
                WeavePlan wp = findById(WeavePlan.class, plan.getId());
                TcBomVersionParts tbp = null;
                TcBomVersion tbv = null;
                TcBom tb = null;
                try {
                    tbp = findById(TcBomVersionParts.class, partId);
                    tbv = findById(TcBomVersion.class, tbp.getTcProcBomVersoinId());
                    tb = findById(TcBom.class, tbv.getTcProcBomId());
                    outputString += tb.getTcProcBomName().trim() + "  " + tbp.getTcProcBomVersionPartsName().trim();
                } catch (Exception e) {
                    outputString += "** " + wp.getPartName();
                }

                String rollNo = "";
                if (wp.getRollNo() != null) {
                    rollNo = wp.getRollNo().trim();
                }

                String levelNo = "";
                if (wp.getLevelNo() != null) {
                    levelNo = wp.getLevelNo().trim();
                }

                String drawNo = "";
                if (wp.getDrawNo() != null) {
                    drawNo = wp.getDrawNo().trim();
                }
                FinishedProductMirror fp = null;
                map.clear();
                map.put("productId", plan.getProductId());
                map.put("salesOrderDetailId", salesOrderDetail.getId());
                finishedProductMirrorList = findListByMap(FinishedProductMirror.class, map);
                if (finishedProductMirrorList.size() > 0) {
                    fp = finishedProductMirrorList.get(0);
                }
                if (finishedProductMirrorList.size() == 0) {
                    map.clear();
                    map.put("productId", plan.getProductId());
                    map.put("salesOrderId", salesOrderDetail.getSalesOrderId());
                    finishedProductMirrorList = findListByMap(FinishedProductMirror.class, map);
                    if (finishedProductMirrorList.size() > 0) {
                        fp = finishedProductMirrorList.get(0);
                    }
                }

                outputString += "\t" + fp.getFactoryProductName().trim() + "\t"
                        + barcode.getBarcode() + "  "
                        + plan.getProductLength() + "M\t"
                        + rollNo + "\t"
                        + levelNo + "\t"
                        + drawNo + "\t"
                        + barcode.getBarcode() + ";;;;";
            } else {
                outputString += fdm.getFactoryProductName().trim() + (StringUtils.isEmpty(partName) ? "" : ("(" + partName.trim() + ")"));
                outputString += "\t";
                outputString += fdm.getConsumerProductName().trim() + " " + (barcode.getPartName() == null ? "" : barcode.getPartName().trim());
                outputString += "\t";
                outputString += plan.getSalesOrderCode().trim();
                outputString += "\t";

                outputString += danXiangBuCode.trim();
                outputString += "\t";
                outputString += barcode.getBarcode();
                outputString += "\t";
                if (fdm.getProductRollLength() != null) {
                    outputString += fdm.getProductRollLength();
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                if (fdm.getProductRollWeight() != null) {
                    outputString += fdm.getProductRollWeight();
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                if (plan.getPartId() != null && plan.getPartId() > 0) {
                    map.clear();
                    map.put("versionPartsId", plan.getPartId());
                    map.put("salesOrderDetailId", salesOrderDetail.getId());
                    TcBomVersionPartsMirror tParts = findUniqueByMap(TcBomVersionPartsMirror.class, map);
                    if (tParts != null) {
                        outputString += tParts.getTcProcBomVersionPartsName().trim();
                    }
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                outputString += dateString;
                outputString += "\t";
                outputString += plan.getProductModel().trim();
                outputString += "\t";
                outputString += barcode.getBarcode();
                outputString += ";";
                outputString += " ";
                outputString += ";";
                outputString += " ";
                outputString += ";";
                outputString += " ";
                outputString += ";";
                outputString += " ";
                outputString += "\n";
            }
            barcode.setOutPutString(outputString);
            return barcode;
        } else {
            FinishedProduct fdp = findById(FinishedProduct.class, plan.getProductId());
            if (type.equals("roll") || type.equals("roll_peibu")) {
                barcode = new RollBarcode();
            } else if (type.equals("tray")) {
                barcode = new TrayBarCode();
            } else if (type.equals("box")) {
                barcode = new BoxBarcode();
            } else {
                barcode = new PartBarcode();
            }
            barcode.setProducePlanDetailId(plan.getProducePlanDetailId());
            barcode.setBarcode(dcString);
            barcode.setSalesOrderCode(plan.getSalesOrderCode());
            barcode.setSalesProductId(plan.getProductId());
            barcode.setBatchCode(plan.getBatchCode());
            barcode.setPartName(plan.getPartName());
            barcode.setSalesOrderDetailId(plan.getFromSalesOrderDetailId());
            if (partId == null || partId < 0) {
                barcode.setPartId(null);
            } else {
                barcode.setPartId(partId);
            }
            barcode.setPlanId(plan.getId());
            barcode.setPrintDate(new Date());
            barcode.setSalesProductId(plan.getProductId());
            String danXiangBuCode = "";// 层数，卷号
            String partName = "";
            if (plan instanceof WeavePlan && plan.getPartId() != null) {
                WeavePlan wp = (WeavePlan) plan;
                try {
                    danXiangBuCode += StringUtils.isBlank(wp.getRollNo()) ? "" : ("卷号" + wp.getRollNo() + " ");
                    danXiangBuCode += StringUtils.isBlank(wp.getLevelNo()) ? "" : ("层号" + wp.getLevelNo() + " ");
                    danXiangBuCode += StringUtils.isBlank(wp.getDrawNo()) ? "" : ("图号" + wp.getDrawNo());
                    partName = wp.getPartName();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            } else {
                if (partId != null && partId > 0) {
                    TcBomVersionParts part = findById(TcBomVersionParts.class, partId);
                    if (part != null)
                        partName = part.getTcProcBomVersionPartsName();
                }
            }
            // 拼装要输出的信息内容 outputString
            String outputString = "";
            if (type.equals("roll_peibu")) {
                WeavePlan wp = findById(WeavePlan.class, plan.getId());
                TcBomVersionParts tbp = null;
                TcBomVersion tbv = null;
                TcBom tb = null;
                try {
                    tbp = findById(TcBomVersionParts.class, partId);
                    tbv = findById(TcBomVersion.class, tbp.getTcProcBomVersoinId());
                    tb = findById(TcBom.class, tbv.getTcProcBomId());
                    outputString += tb.getTcProcBomName().trim() + "  " + tbp.getTcProcBomVersionPartsName().trim();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                    outputString += "** " + wp.getPartName();
                }

                String rollNo = "";
                if (wp.getRollNo() != null) {
                    rollNo = wp.getRollNo().trim();
                }

                String levelNo = "";
                if (wp.getLevelNo() != null) {
                    levelNo = wp.getLevelNo().trim();
                }

                String drawNo = "";
                if (wp.getDrawNo() != null) {
                    drawNo = wp.getDrawNo().trim();
                }

                FinishedProduct fp = findById(FinishedProduct.class, wp.getProductId());
                outputString += "\t" + fp.getFactoryProductName().trim() + "\t"
                        + barcode.getBarcode() + "  "
                        + plan.getProductLength() + "M\t"
                        + rollNo + "\t"
                        + levelNo + "\t"
                        + drawNo + "\t"
                        + barcode.getBarcode() + ";;;;";
            } else {
                outputString += fdp.getFactoryProductName().trim() + (StringUtils.isEmpty(partName) ? "" : ("(" + partName.trim() + ")"));
                outputString += "\t";
                outputString += fdp.getConsumerProductName().trim() + " " + (barcode.getPartName() == null ? "" : barcode.getPartName().trim());
                outputString += "\t";
                outputString += plan.getSalesOrderCode().trim();
                outputString += "\t";
                outputString += danXiangBuCode.trim();
                outputString += "\t";
                outputString += barcode.getBarcode();
                outputString += "\t";
                if (fdp.getProductRollLength() != null) {
                    outputString += fdp.getProductRollLength();
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                if (fdp.getProductRollWeight() != null) {
                    outputString += fdp.getProductRollWeight();
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                if (plan.getPartId() != null && plan.getPartId() > 0) {
                    TcBomVersionParts tParts = bomService.findById(TcBomVersionParts.class, plan.getPartId());
                    if (tParts != null) {
                        outputString += tParts.getTcProcBomVersionPartsName().trim();
                    }
                } else {
                    outputString += " ";
                }
                outputString += "\t";
                outputString += dateString;
                outputString += "\t";
                outputString += plan.getProductModel().trim();
                outputString += "\t";
                outputString += barcode.getBarcode();
                outputString += ";";
                outputString += " ";
                outputString += ";";
                outputString += " ";
                outputString += ";";
                outputString += " ";
                outputString += ";";
                outputString += " ";
                outputString += "\n";
            }
            barcode.setOutPutString(outputString);
            return barcode;
        }
    }

    /**
     * @param
     * @return
     */
    public List<IBarcode> getOutputStringList(Iplan plan, String type, int count, String printName, String departmentCode, Long partId, String trugPlanId) throws Exception {
        List<IBarcode> li = new ArrayList();

        if (plan instanceof WeavePlan) {
            if (type == null)
                type = "roll";
            if (plan.getPartId() != null) {
                type = "roll_peibu";
                if (trugPlanId != null && trugPlanId.equals("1")) {
                    type = "tray";
                }
            }
        } else if (plan instanceof CutPlan) {
            if (type == null)
                type = "part";
        } else {
            if (type == null)
                type = "roll";
        }

        String prefixPrint = "HS";
        Department department = DepartmentCache.getInstance().getDepartmentList().get(departmentCode);
        prefixPrint = department == null ? "" : department.getPrefix();
        if (trugPlanId != null && trugPlanId.equals("1") && plan.getPartId() != null) {
            prefixPrint = "HS";
        }

        if (trugPlanId != null && trugPlanId.equals("1") && plan.getPartId() != null) {
            type = "traypart";
        }

        IBarcode ibarcode = getBarcodeList(plan, type, "@barcode@", partId);

        List<String> barcodelist = PrintBarCode.getInstance().getBarCodeList(type, prefixPrint, count);
        for (String barcode : barcodelist) {


            IBarcode ibarcodenew;

            if (ibarcode instanceof RollBarcode) {
                ibarcodenew = new RollBarcode();
            } else if (ibarcode instanceof BoxBarcode) {
                ibarcodenew = new BoxBarcode();
            } else if (ibarcode instanceof TrayBarCode) {
                ibarcodenew = new TrayBarCode();
            } else {
                ibarcodenew = new PartBarcode();
            }

            BeanUtils.copyProperties(ibarcode, ibarcodenew);
            ibarcodenew.setBarcode(barcode);
            ibarcodenew.setOutPutString(ibarcode.getOutPutString().replace("@barcode@", barcode));
            li.add(ibarcodenew);


        }

        for (IBarcode i : li) {
            if (i instanceof RollBarcode) {
                insert((RollBarcode) i);
            } else if (i instanceof BoxBarcode) {
                insert((BoxBarcode) i);
            } else if (i instanceof TrayBarCode) {
                insert((TrayBarCode) i);
            } else if (i instanceof PartBarcode) {
                insert((PartBarcode) i);
            }
        }
        return li;
    }

    /**
     * @param
     * @return
     */
    public List<IBarcode> getOutputString(Iplan plan, String type, int count, String printName, String departmentCode, Long partId, String trugPlanId) {
        List<IBarcode> li = new ArrayList();
        if (plan instanceof WeavePlan) {
            if (type == null)
                type = "roll";
            if (plan.getPartId() != null) {
                type = "roll_peibu";
                if (trugPlanId != null && trugPlanId.equals("1")) {
                    type = "tray";
                }
            }
        } else if (plan instanceof CutPlan) {
            if (type == null)
                type = "part";
        } else {
            if (type == null)
                type = "roll";
        }

        String prefixPrint = "HS";
        Department department = DepartmentCache.getInstance().getDepartmentList().get(departmentCode);
        prefixPrint = department == null ? "" : department.getPrefix();
        if (trugPlanId != null && trugPlanId.equals("1") && plan.getPartId() != null) {
            prefixPrint = "HS";
        }

        if (trugPlanId != null && trugPlanId.equals("1") && plan.getPartId() != null) {
            type = "traypart";
        }

        List<String> barcodelist = PrintBarCode.getInstance().getBarCodeList(type, prefixPrint, count);
        for (String barcode : barcodelist) {
            li.add(getBarcode(plan, type, barcode, partId));
        }

        for (IBarcode i : li) {
            if (i instanceof RollBarcode) {
                save((RollBarcode) i);
            } else if (i instanceof BoxBarcode) {
                save((BoxBarcode) i);
            } else if (i instanceof TrayBarCode) {
                save((TrayBarCode) i);
            } else if (i instanceof PartBarcode) {
                save((PartBarcode) i);
            }
        }
        return li;
    }

    public String doPrintBarcodeByPage(String weavePlanId, String cutPlanId, String count, String pName, String type, String partName, String departmentName, String trugPlanId, Long partId,String copies) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startday = new Date();
        System.out.println("doPrintBarcodeByPage" + df.format(startday) + "start");
        Integer counts = Integer.parseInt(count.split("\\.")[0]);
        if (!StringUtils.isBlank(weavePlanId)) {
            // 先根据id查询出信息
            if (type == null)
                type = "roll";
            WeavePlan w = findById(WeavePlan.class, Long.parseLong(weavePlanId));
            // 修改打印的状态
            w.setIsStamp(1);
            update(w);
            if (w.getPartId() != null) {
                type = "roll_peibu";
                if (trugPlanId != null && trugPlanId.equals("1")) {
                    type = "tray";
                }
            }
        } else if (type == null && cutPlanId != null) {
            CutDailyPlanDetail dailyDetail = salesOrderService.findById(CutDailyPlanDetail.class, Long.parseLong(cutPlanId));
            if (dailyDetail != null && trugPlanId == null) {
                cutPlanId = dailyDetail.getCutPlanId() + "";
            }
            if (type == null)
                type = "part";
        }

        if (count == null || counts == 0) {
            return GsonTools.toJson("打印数量不能为空！");
        }
        Iplan plan = null;
        if (weavePlanId != null && !"null".equals(weavePlanId)) {
            plan = salesOrderService.findById(WeavePlan.class, Long.parseLong(weavePlanId));

        } else if (cutPlanId != null && !"null".equals(cutPlanId)) {
            plan = salesOrderService.findById(CutPlan.class, Long.parseLong(cutPlanId));

        }
        plan.setPartName(partName);
        List<IBarcode> li = getOutputString(plan, type, counts, pName, departmentName, partId, trugPlanId);

        if (type.equals("roll")) {
            btwName = "恒石条码(卷)";
        } else if (type.equals("roll_peibu")) {
            btwName = "恒石条码(胚布)";
        } else if (type.equals("part")) {
            btwName = "恒石条码(卷)";
        } else if (type.equals("box")) {
            btwName = "恒石条码(盒)";
        } else {
            btwName = "恒石条码(托)";
        }

        PrinterOut printStrings = getQRBarCode(li, pName, fileUrl + btwName + ".btw", dataUrl + UUID.randomUUID().toString() + ".txt");
        String uname = UUID.randomUUID().toString() + "";

        try {
            File f = new File(printStrings.getTxtFileUrl());

            File parentFile = f.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            List<IBarcode> li1 = printStrings.getLi();
            for (IBarcode i : li1) {
                FileUtils.writeToFile(i.getOutPutString(), printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
                // 托打包时出4张条码
                if (i instanceof TrayBarCode) {
                    for (int a = 0; a < 3; a++) {
                        FileUtils.writeToFile(i.getOutPutString(), printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
                    }
                }
                //部件条码打印4张
                if (i instanceof PartBarcode) {
                    for (int a = 0; a < Integer.parseInt(copies)-1; a++) {
                        FileUtils.writeToFile(i.getOutPutString(), printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
                    }
                }
            }
            int i = PrintUtils.printDb(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), printStrings.getTxtFileUrl(), 1);
            if (i == 20) {
                throw new Exception("找不到打印机");
            }
            if (i == 21) {
                throw new Exception("打印失败");
            }
            if (type.equals("roll") || type.equals("roll_peibu") || type.contentEquals("part")) {
                printOrderInfo(weavePlanId, cutPlanId, pName, type);
            }
        } catch (Exception e) {
            logger.error("打印机：" + printStrings.getPrinterName() + ",\t模板：" + printStrings.getBtwFileUrl() + ";错误：", e);
            return GsonTools.toJson(e.getMessage());
        }
        HashMap<String, String> r = new HashMap<String, String>();
        r.put("url", dataUrl + uname + ".txt");
        r.put("barcode", li.get(0).getBarcode());
        Date endday = new Date();
        System.out.println("doPrintBarcodeByPage:" + df.format(endday) + "end");
        return GsonTools.toJson(r);
    }

    public void printOrderInfo(String weavePlanId, String cutPlanId, String printer, String type) throws Exception {
        if (!StringUtils.isBlank(weavePlanId)) {
            WeavePlan wp = findById(WeavePlan.class, Long.parseLong(weavePlanId));

            ProducePlanDetail producePlanDetail = findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
            FinishedProduct fp1 = findById(FinishedProduct.class, producePlanDetail.getProductId());
            Consumer c = findById(Consumer.class, fp1.getProductConsumerId());

            FinishedProduct fp = findById(FinishedProduct.class, wp.getProductId());
            List<String> content = new ArrayList<String>();
            String factoryName = fp.getFactoryProductName();
            SalesOrderDetail sod = findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
            String order = sod.getSalesOrderSubCodePrint();
            String batch = wp.getBatchCode();
            String procCode = wp.getProcessBomCode() + "(" + wp.getProcessBomVersion() + ")";
            String planCode = wp.getPlanCode();
            String deviceCodes = weavePlanService.getWeavePlanDevices(wp.getId());
            String unit = factoryName.trim() + "\t" + order.trim() + "\t" + batch.trim() + "\t" + procCode.trim() + "\t" + c.getConsumerSimpleName().trim() + "\t" + planCode.trim() + " " + deviceCodes.trim();
            content.add(unit);
            if (type.equals("roll_peibu")) {
                PrintUtils.print(content, fileUrl + "恒石成品胚布条码(订单).btw", printer);
            } else {
                PrintUtils.print(content, fileUrl + "恒石条码(订单).btw", printer);
            }
        } else {
            CutPlan cp = findById(CutPlan.class, Long.parseLong(cutPlanId));
            FinishedProduct fp = findById(FinishedProduct.class, cp.getProductId());
            Consumer c = findById(Consumer.class, cp.getConsumerId());

            List<String> content = new ArrayList<String>();
            SalesOrderDetail sod = findById(SalesOrderDetail.class, cp.getFromSalesOrderDetailId());
            String order = sod.getSalesOrderSubCodePrint();
            String factoryName = fp.getFactoryProductName();
            String batch = cp.getBatchCode();
            String procCode = cp.getProcessBomCode() + "(" + cp.getProcessBomVersion() + ")";
            String planCode = cp.getPlanCode();
            String unit = factoryName.trim() + "\t" + order.trim() + "\t" + batch.trim() + "\t" + procCode.trim() + "\t" + c.getConsumerSimpleName().trim() + "\t" + planCode.trim();
            content.add(unit);
            PrintUtils.print(content, fileUrl + "恒石条码(订单).btw", printer);
        }
    }


    public void printOrderInfoByList(String weavePlanId, String printer, String type) throws Exception {
        WeavePlan wp = findById(WeavePlan.class, Long.parseLong(weavePlanId));
        ProducePlanDetail producePlanDetail = findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
        FinishedProduct fp1 = findById(FinishedProduct.class, producePlanDetail.getProductId());
        Consumer c = findById(Consumer.class, fp1.getProductConsumerId());

        FinishedProduct fp = findById(FinishedProduct.class, wp.getProductId());
        List<String> content = new ArrayList<String>();
        String factoryName = fp.getFactoryProductName();
        SalesOrderDetail sod = findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
        String order = sod.getSalesOrderSubCodePrint();
        String batch = wp.getBatchCode();
        String procCode = wp.getProcessBomCode() + "(" + wp.getProcessBomVersion() + ")";
        String planCode = wp.getPlanCode();
        String deviceCodes = weavePlanService.getWeavePlanDevices(wp.getId());
        String unit = factoryName.trim() + "\t" + order.trim() + "\t" + batch.trim() + "\t" + procCode.trim() + "\t" + c.getConsumerSimpleName().trim() + "\t" + planCode.trim() + " " + deviceCodes.trim();
        content.add(unit);
        if (type.equals("roll_peibu")) {
            PrintUtils.print(content, fileUrl + "恒石成品胚布条码(订单).btw", printer);
        } else {
            PrintUtils.print(content, fileUrl + "恒石条码(订单).btw", printer);
        }
    }

    public void printOrderInfo(String weavePlanId, String cutPlanId, String printer) throws Exception {
        if (!StringUtils.isBlank(weavePlanId)) {
            WeavePlan wp = findById(WeavePlan.class, Long.parseLong(weavePlanId));
            FinishedProduct fp = findById(FinishedProduct.class, wp.getProductId());
            Consumer c = findById(Consumer.class, wp.getConsumerId());
            List<String> content = new ArrayList<String>();
            String factoryName = fp.getFactoryProductName();
            SalesOrderDetail sod = findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
            String order = sod.getSalesOrderSubCodePrint();
            String batch = wp.getBatchCode();
            String procCode = wp.getProcessBomCode() + "(" + wp.getProcessBomVersion() + ")";
            String planCode = wp.getPlanCode();
            String deviceCodes = weavePlanService.getWeavePlanDevices(wp.getId());
            String unit = factoryName.trim() + "\t" + order.trim() + "\t" + batch.trim() + "\t" + procCode.trim() + "\t" + c.getConsumerSimpleName().trim() + "\t" + planCode.trim() + " " + deviceCodes.trim();
            content.add(unit);
            PrintUtils.print(content, fileUrl + "恒石条码(订单).btw", printer);
        } else {
            CutPlan cp = findById(CutPlan.class, Long.parseLong(cutPlanId));
            FinishedProduct fp = findById(FinishedProduct.class, cp.getProductId());
            Consumer c = findById(Consumer.class, cp.getConsumerId());

            List<String> content = new ArrayList<String>();
            SalesOrderDetail sod = findById(SalesOrderDetail.class, cp.getFromSalesOrderDetailId());
            String order = sod.getSalesOrderSubCodePrint();
            String factoryName = fp.getFactoryProductName();
            String batch = cp.getBatchCode();
            String procCode = cp.getProcessBomCode() + "(" + cp.getProcessBomVersion() + ")";
            String planCode = cp.getPlanCode();
            String unit = factoryName.trim() + "\t" + order.trim() + "\t" + batch.trim() + "\t" + procCode.trim() + "\t" + c.getConsumerSimpleName().trim() + "\t" + planCode.trim();
            content.add(unit);
            PrintUtils.print(content, fileUrl + "恒石条码(订单).btw", printer);
        }
    }


    private void initp() throws IOException {
        String path = PathUtils.getClassPath();
        path += File.separator + "ts24.lib";
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        dotfont = new byte[fis.available()];
        fis.read(dotfont);
        fis.close();
    }

    public void print(String context, Printer p) throws IOException {
        initp();
    }

    public String rePrint(String id, String pName, String type) {
        IBarcode ib = null;
        if (type.equals("roll")) {
            RollBarcode r = findById(RollBarcode.class, Long.parseLong(id));
            ib = r;
            WeavePlan wp = findById(WeavePlan.class, r.getPlanId());
            if (wp.getPartId() != null) {
                type = "roll_peibu";
            }
        } else if (type.equals("box")) {
            BoxBarcode r = findById(BoxBarcode.class, Long.parseLong(id));
            ib = r;
        } else if (type.equals("part")) {
            PartBarcode r = findById(PartBarcode.class, Long.parseLong(id));
            ib = r;
        } else {
            TrayBarCode r = findById(TrayBarCode.class, Long.parseLong(id));
            ib = r;
        }

        if (type.equals("roll")) {
            btwName = "恒石条码(卷)";
        } else if (type.equals("roll_peibu")) {
            btwName = "恒石条码(胚布)";
        } else if (type.equals("part")) {
            btwName = "恒石条码(部件)";
        } else if (type.equals("box")) {
            btwName = "恒石条码(盒)_空";
        } else {
            btwName = "恒石条码(托)_空";
        }

        PrinterOut printStrings = new PrinterOut();
        printStrings.setBtwFileUrl(fileUrl + btwName + ".btw");
        printStrings.setTxtFileUrl(dataUrl + RandomUtils.uuid() + ".txt");
        printStrings.setPrinterName(pName);
        String uname = UUID.randomUUID().toString() + "";
        String finalFileName = dataUrl + uname + ".txt";
        String str = "打印失败";
        try {
            File f = new File(printStrings.getTxtFileUrl());

            File parentFile = f.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            File finalFile = new File(finalFileName);

            File parentFinalFile = finalFile.getParentFile();
            if (!parentFile.exists()) {
                parentFinalFile.mkdirs();
            }

            if (finalFile.exists()) {
                finalFile.delete();
            }
            finalFile.createNewFile();

            FileUtils.writeToFile(ib.getOutPutString(), printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
            if (ib instanceof TrayBarCode) {
                for (int a = 0; a < 3; a++) {
                    FileUtils.writeToFile(ib.getOutPutString(), printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
                }
            }
            int i = PrintUtils.printDb(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), printStrings.getTxtFileUrl(), 1);
            if (i == 21) {
                throw new Exception("打印失败");
            }
            if (i == 20) {
                throw new Exception("找不到打印机");
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            HashMap<String, String> r = new HashMap<String, String>();
            r.put("url", dataUrl + uname + ".txt");
            return str;
        }
        return "打印成功";
    }

    public String printBarcodeFirst(String departmentType, String pName, String type, int count,int copies) throws Exception {

        if (count == 0) {

            return "打印数量不能为0";
        }

        List<IBarcode> li = getOutputString(departmentType, pName, type, count);
        if (type.equals("roll")) {
            btwName = "恒石条码(卷)_空";
        } else if (type.equals("part")) {
            btwName = "恒石条码(卷)_空";
        } else if (type.equals("box")) {
            btwName = "恒石条码(盒)_空";
        } else {
            btwName = "恒石条码(托)_空";
        }

        PrinterOut printStrings = getQRBarCode(li, pName, fileUrl + btwName + ".btw", dataUrl + RandomUtils.uuid() + ".txt");
        String name = UUID.randomUUID().toString() + "";
        String finalFileName = dataUrl + name + ".txt";

        File f = new File(printStrings.getTxtFileUrl());

        File parentFile = f.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();

        File finalFile = new File(finalFileName);
        if (!finalFile.exists()) {
            finalFile.getParentFile().mkdirs();
        } else {
            finalFile.delete();
        }
        finalFile.createNewFile();

        List<IBarcode> li1 = printStrings.getLi();
        if (copies == 0){
            copies=3;
        }else{
            copies=copies-1;
        }
        for (IBarcode i : li1) {
            FileUtils.writeToFile(i.getOutPutString(), printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
            if (i instanceof TrayBarCode) {
                for (int a = 0; a < copies; a++) {
                    FileUtils.writeToFile(i.getOutPutString(), printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
                }
            }
            FileUtils.writeToFile(i.getOutPutString(), finalFileName, true, true, Charset.forName("UTF-8"));
        }
        try {
            int i = PrintUtils.printDb(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), printStrings.getTxtFileUrl(), 1);
            if (i == 21) {

                return "打印失败";
            }
            if (i == 20) {
                return "找不到打印机";
            }
        } catch (Exception ex) {
            return "打印失败:" + ex.getMessage();
        }

        return "打印成功";
    }

    @Override
    public String buda(String ids, String pName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        String[] idArrays = ids.split(",");
        String type = null;
        List<IBarcode> li = new ArrayList<IBarcode>();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String barcodeDate = sdf.format(now);

        Calendar c = Calendar.getInstance();
        String year = "000" + (c.get(Calendar.YEAR) - 1999);
        String day = "00" + c.get(Calendar.DAY_OF_YEAR);
        year = year.substring(year.length() - 3);
        day = day.substring(day.length() - 3);
        String dateString = year + day;

        int dc = 0;
        DailyPrintCount dpc = null;
        for (String idString : idArrays) {
            Long id = Long.parseLong(idString);
            TotalStatistics ts = findById(TotalStatistics.class, id);
            String barcode = ts.getRollBarcode();
            IBarcode iBarcode = null;

            HashMap<String, Object> map = new HashMap();
            map.put("barcode", barcode);
            if (type == null) {
                if (barcode.startsWith("R")) {
                    type = "roll";
                    iBarcode = findUniqueByMap(RollBarcode.class, map);
                    if (iBarcode.getPartId() != null) {
                        type = "roll_peibu";
                    }
                } else if (barcode.startsWith("P")) {
                    type = "part";
                    iBarcode = findUniqueByMap(PartBarcode.class, map);
                } else if (barcode.startsWith("T")) {
                    type = "tray";
                    iBarcode = findUniqueByMap(TrayBarCode.class, map);
                } else if (barcode.startsWith("B")) {
                    type = "box";
                    iBarcode = findUniqueByMap(BoxBarcode.class, map);
                }
                HashMap<String, Object> searchCountMap = new HashMap<String, Object>();
                searchCountMap.put("barcodeType", type);
                searchCountMap.put("barcodeDate", barcodeDate);
                dpc = findUniqueByMap(DailyPrintCount.class, searchCountMap);
                if (dpc == null) {
                    dpc = new DailyPrintCount(type, barcodeDate, 0);
                    save(dpc);
                }
                dc = dpc.getBarcodeCount();
            } else {
                if (barcode.startsWith("R") && !type.equals("roll")) {
                    return "请选择使用相同类型模板打印的条码";
                } else if (barcode.startsWith("P") && !type.equals("part")) {
                    return "请选择使用相同类型模板打印的条码";
                } else if (barcode.startsWith("T") && !type.equals("tray")) {
                    return "请选择使用相同类型模板打印的条码";
                } else if (barcode.startsWith("B") && !type.equals("box")) {
                    return "请选择使用相同类型模板打印的条码";
                }

                if (barcode.startsWith("R")) {
                    type = "roll";
                    iBarcode = findUniqueByMap(RollBarcode.class, map);
                    if (iBarcode.getPartId() != null) {
                        type = "roll_peibu";
                    }
                } else if (barcode.startsWith("P")) {
                    type = "part";
                    iBarcode = findUniqueByMap(PartBarcode.class, map);
                } else if (barcode.startsWith("T")) {
                    type = "tray";
                    iBarcode = findUniqueByMap(TrayBarCode.class, map);
                } else if (barcode.startsWith("B")) {
                    type = "box";
                    iBarcode = findUniqueByMap(BoxBarcode.class, map);
                }
            }

            if (iBarcode == null) continue;

            String likeString;
            if (type.equals("roll") || type.equals("roll_peibu")) {
                likeString = iBarcode.getBarcode().substring(0, 4) + dateString + "%";
            } else if (type.equals("tray")) {
                likeString = iBarcode.getBarcode().substring(0, 4) + dateString + "%";
            } else if (type.equals("box")) {
                likeString = iBarcode.getBarcode().substring(0, 4) + dateString + "%";
            } else {
                likeString = iBarcode.getBarcode().substring(0, 4) + dateString + "%";
            }

            String dcString;
            if (dc < 10) {
                dcString = "000" + dc;
            } else if (dc < 100 && dc >= 10) {
                dcString = "00" + dc;
            } else if (dc < 1000 && dc >= 100) {
                dcString = "0" + dc;
            } else {
                dcString = "" + dc;
            }
            IBarcode printBarcode = getBarcode(type, likeString.replace("%", "") + dcString, iBarcode);
            li.add(printBarcode);
            dc++;
        }
        dpc.setBarcodeCount(dc);
        update(dpc);

        if (type.equals("roll")) {
            btwName = "恒石条码(卷)";
        } else if (type.equals("roll_peibu")) {
            btwName = "恒石条码(胚布)";
        } else if (type.equals("part")) {
            btwName = "恒石条码(卷)";
        } else if (type.equals("box")) {
            btwName = "恒石条码(盒)_空";
        } else {
            btwName = "恒石条码(托)_空";
        }

        PrinterOut printStrings = new PrinterOut();
        printStrings.setBtwFileUrl(fileUrl + btwName + ".btw");
        printStrings.setTxtFileUrl(dataUrl + RandomUtils.uuid() + ".txt");
        printStrings.setLi(li);
        printStrings.setPrinterName(pName);
        String uname = UUID.randomUUID().toString() + "";
        String finalFileName = dataUrl + uname + ".txt";
        String str = "打印失败";
        try {
            File f = new File(printStrings.getTxtFileUrl());
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            File finalFile = new File(finalFileName);
            if (finalFile.exists()) {
                finalFile.delete();
            }
            finalFile.createNewFile();

            List<IBarcode> li1 = printStrings.getLi();
            for (IBarcode i : li1) {
                FileUtils.writeToFile(i.getOutPutString(), printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
                if (i instanceof TrayBarCode) {
                    for (int a = 0; a < 3; a++) {
                        FileUtils.writeToFile(i.getOutPutString(), printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
                    }
                }
            }

            int i = PrintUtils.printDb(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), printStrings.getTxtFileUrl(), 1);
            if (i == 21) {
                throw new Exception("打印失败");
            }
            if (i == 20) {
                throw new Exception("找不到打印机");
            }
        } catch (Exception e) {
            btwName = null;
            logger.error(e.getLocalizedMessage(), e);
            HashMap<String, String> r = new HashMap<String, String>();
            r.put("url", dataUrl + uname + ".txt");
            return str;
        }
        HashMap<String, String> r = new HashMap<String, String>();
        r.put("url", dataUrl + uname + ".txt");
        btwName = null;
        return "打印成功";
    }

    public IBarcode getBarcode(String type, String dcString, IBarcode ibarcode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        IBarcode result;
        if (type.equals("roll") || type.equals("roll_peibu")) {
            result = new RollBarcode();
            ObjectUtils.clone((RollBarcode) ibarcode, (RollBarcode) result);
        } else if (type.equals("tray")) {
            result = new TrayBarCode();
            ObjectUtils.clone((TrayBarCode) ibarcode, (TrayBarCode) result);
        } else if (type.equals("box")) {
            result = new BoxBarcode();
            ObjectUtils.clone((BoxBarcode) ibarcode, (BoxBarcode) result);
        } else {
            result = new PartBarcode();
            ObjectUtils.clone((PartBarcode) ibarcode, (PartBarcode) result);
        }
        String outPutString = result.getOutPutString();
        result.setOutPutString(outPutString.replace(result.getBarcode(), dcString));
        result.setBarcode(dcString);
        save(result);
        return result;
    }


    public String doPrintBarcodeByPageList(String ids, String cutPlanId, String count, String pName, String type, String partName, String departmentName, String trugPlanId, Long partId) throws Exception {
        Map<String,Object> pNameMap =new HashMap<>();
        pNameMap.put("printerName",pName);
        List<Printer> printers = printerDao.findListByMap(Printer.class, pNameMap);
        Department department = printerDao.findById(Department.class, printers.get(0).getDepartmentId());
        if(department.getPrefix() !=null){
            departmentName=department.getCode();
        }
        Integer counts = Integer.parseInt(count.split("\\.")[0]);
        if (count == null || counts == 0) {
            return GsonTools.toJson("打印数量不能为空！");
        }

        String str[] = ids.split(",");

        String message = "";
        // List<String> barcodelist = PrintBarCode.getInstance().getBarCodeList(type, prefixPrint, count);

        for (String weavePlanId : str) {

            if (StringUtils.isBlank(weavePlanId)) {
                continue;
            }

            // 先根据id查询出信息
            if (type == null)
                type = "roll";
            WeavePlan w = findById(WeavePlan.class, Long.parseLong(weavePlanId));
            if (null != w.getPartName() && null != w.getPartId()) {
                partId = w.getPartId();
                partName = w.getPartName();
            } else {
                // 如果是编织计划，判断是否是胚布，如果是胚布，判断BOM明细，遍历Bom明细，获得bom部件，判断bom部件是否是成品胚布，如果是成品胚布，取出它的bomID，通过编织计划订单ID，获取裁剪计划，通过裁剪计划获取裁剪计划的产品工艺bom，取出相同的bomID
                FinishedProduct fp = printerDao.findById(FinishedProduct.class, w.getProductId());
                if (fp.getProductIsTc() == -1) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("tcFinishedProductId", fp.getId());
                    List<TcBomVersionPartsDetail> tvpdList = printerDao.findListByMap(TcBomVersionPartsDetail.class, map);
                    HashSet<Long> partIdSet = new HashSet<Long>();
                    for (TcBomVersionPartsDetail tvpd : tvpdList) {
                        partIdSet.add(tvpd.getTcProcBomPartsId());
                    }
                    List<Map<String, Object>> partsMap = new ArrayList<>();
                    Map<String, Object> map1 = new HashMap<>();
                    for (Long partId1 : partIdSet) {
                        TcBomVersionParts tvp = printerDao.findById(TcBomVersionParts.class, partId1);
                        if (tvp.getTcProcBomVersionPartsType().equals("成品胚布")) {
                            map1.put("PARTID", tvp.getId());
                            map1.put("PARTNAME", tvp.getTcProcBomVersionPartsName());
                            partsMap.add(map1);
                        }
                    }
                    if (partsMap.size() > 0) {
                        partId = Long.parseLong(partsMap.get(0).get("PARTID").toString());
                        partName = partsMap.get(0).get("PARTNAME").toString();
                    } else {
                        partId = Long.parseLong("-1");
                        partName = "非成品胚布";
                    }
                }
            }

            // 修改打印的状态
            if (w.getIsStamp() != 1) {
                w.setIsStamp(1);
                update(w);
            }

            if (w.getPartId() != null) {
                type = "roll_peibu";
                if (trugPlanId != null && trugPlanId.equals("1")) {
                    type = "tray";
                }
            }

            PrinterOut printOrders = null;

            List<PrinterOut> listPrinterOut = new ArrayList<>();
            if (type.equals("roll") || type.equals("roll_peibu")) {
                List<String> content = getPrintOrders(w);
                btwName = type.equals("roll_peibu") ? "恒石成品胚布条码(订单).btw" : "恒石条码(订单).btw";
                printOrders = getContentByList(content, pName, fileUrl + btwName, dataUrl + UUID.randomUUID().toString() + ".txt");
            }

            w.setPartName(partName);
            List<IBarcode> li = getOutputStringList(w, type, counts, pName, departmentName, partId, trugPlanId);
            if (type.equals("roll")) {
                btwName = "恒石条码(卷).btw";
            } else if (type.equals("roll_peibu")) {
                btwName = "恒石条码(胚布).btw";
            } else if (type.equals("part")) {
                btwName = "恒石条码(卷).btw";
            } else if (type.equals("box")) {
                btwName = "恒石条码(盒).btw";
            } else {
                btwName = "恒石条码(托).btw";
            }

            PrinterOut printStringsBarcode = getQRBarCodeByList(li, pName, fileUrl + btwName, dataUrl + UUID.randomUUID().toString() + ".txt");
            listPrinterOut.add(printStringsBarcode);
            if (printOrders != null) {
                listPrinterOut.add(printOrders);
            }

            message += doPrinterOut(listPrinterOut);
        }

        return GsonTools.toJson(message);
    }

    public List<String> getPrintOrders(WeavePlan wp) throws Exception {
        String deviceCodes = weavePlanService.getWeavePlanDevices(wp.getId());
        List<String> content = new ArrayList<String>();
        if (wp.getPrintOrderContent() != null) {
            content.add(wp.getPrintOrderContent() + " " + deviceCodes.trim());
            return content;
        }

        ProducePlanDetail producePlanDetail = findById(ProducePlanDetail.class, wp.getProducePlanDetailId());
        FinishedProduct fp1 = findById(FinishedProduct.class, producePlanDetail.getProductId());
        Consumer c = findById(Consumer.class, fp1.getProductConsumerId());

        FinishedProduct fp = findById(FinishedProduct.class, wp.getProductId());

        String factoryName = fp.getFactoryProductName();
        SalesOrderDetail sod = findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
        String order = sod.getSalesOrderSubCodePrint();
        String batch = wp.getBatchCode();
        String procCode = wp.getProcessBomCode() + "(" + wp.getProcessBomVersion() + ")";
        String planCode = wp.getPlanCode();

        String unit = factoryName.trim() + "\t" + order.trim() + "\t" + batch.trim() + "\t" + procCode.trim() + "\t" + c.getConsumerSimpleName().trim() + "\t" + planCode.trim();
        wp.setPrintOrderContent(unit);
        update(wp);
        content.add(unit + " " + deviceCodes.trim());
        return content;
    }


    public String doPrinterOut(List<PrinterOut> listPrinterOut) {

        String message = "";
        String printerName = "";
        String btwFileUrl = "";
        try {
            for (PrinterOut printStrings : listPrinterOut) {
                printerName = printStrings.getPrinterName();
                btwFileUrl = printStrings.getTxtFileUrl();
                File f = new File(printStrings.getTxtFileUrl());
                File parentFile = f.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }

                if (f.exists()) {
                    f.delete();
                }
                f.createNewFile();

                if (printStrings.getLi() != null) {
                    for (IBarcode i : printStrings.getLi()) {
                        FileUtils.writeToFile(i.getOutPutString(), printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
                    }
                }

                if (printStrings.getContents() != null) {
                    for (String content : printStrings.getContents()) {
                        FileUtils.writeToFile(content, printStrings.getTxtFileUrl(), true, true, Charset.forName("UTF-8"));
                    }
                }

                int i = PrintUtils.printDb(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), printStrings.getTxtFileUrl(), 1);
                if (i == 20) {
                    message += "找不到打印机：{打印机：" + printerName + ",\t模板：" + btwFileUrl + ";错误：}";
                    //throw new Exception("找不到打印机");
                }
                if (i == 21) {
                    message += "打印失败:{打印机：" + printerName + ",\t模板：" + btwFileUrl + ";错误：}";
                    //throw new Exception("打印失败");
                }
            }


        } catch (Exception e) {
            logger.error("打印机：" + printerName + ",\t模板：" + btwFileUrl + ";错误：", e);
            message += "打印报错:{打印机：" + printerName + ",\t模板：" + btwFileUrl + ";错误：}" + e.getMessage();
        }

        return message;
    }

    public void insert(Object... object) throws Exception {
        printerDao.insert(object);
    }


}

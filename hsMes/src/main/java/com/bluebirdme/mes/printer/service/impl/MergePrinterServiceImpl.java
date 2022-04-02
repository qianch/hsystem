/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.printer.service.impl;

import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsFinishedWeightEmbryoClothMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsMirror;
import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.cut.entity.Iplan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPrint;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.service.IDepartmentService;
import com.bluebirdme.mes.printer.PrintBarCode;
import com.bluebirdme.mes.printer.dao.IPrinterDao;
import com.bluebirdme.mes.printer.entity.*;
import com.bluebirdme.mes.printer.service.IMergePrinterService;
import com.bluebirdme.mes.printer.service.IPrinterService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.produce.entity.FinishedProductPrintRecord;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.utils.PrintUtils;
import com.bluebirdme.mes.utils.ProductIsTc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.*;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 季晓龙
 * @Date 2020-5-9 15:40:51
 */
@Service
@AnyExceptionRollback
public class MergePrinterServiceImpl extends BaseServiceImpl implements IMergePrinterService {
    private static Logger logger = LoggerFactory.getLogger(MergePrinterServiceImpl.class);
    private String fileUrl = new File(PathUtils.getClassPath()) + File.separator + "BtwFiles" + File.separator;
    private static String btwName = null;

    @Resource
    IPrinterDao printerDao;

    @Resource
    ISalesOrderService salesOrderService;

    @Resource
    IDepartmentService departmentService;

    @Resource
    IPrinterService printerService;

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

    public IBarcode getBarcode(Iplan plan, String type, String barCode, Long partId, long btwfileId, String customerBarCode,int number) {
        ProducePlanDetail producePlanDetail = printerDao.findById(ProducePlanDetail.class, plan.getProducePlanDetailId());
        Map<String, Object> map = new HashMap<String, Object>();
        SalesOrderDetail salesOrderDetail = findById(SalesOrderDetail.class, plan.getFromSalesOrderDetailId());
        map.clear();
        map.put("productId", plan.getProductId());
        map.put("salesOrderDetailId", salesOrderDetail.getId());
        long tcType = ProductIsTc.FTC;
        List<FinishedProductMirror> finishedProductMirrorList = findListByMap(FinishedProductMirror.class, map);
        FinishedProductMirror fdm = null;


        if (finishedProductMirrorList != null && finishedProductMirrorList.size() > 0) {
            Collections.sort(finishedProductMirrorList, (o1, o2) -> o2.getId().compareTo(o1.getId()));
            fdm = finishedProductMirrorList.get(0);
        }

        List<BarCodePrintRecord> barCodePrintRecordDatas = new ArrayList();

        if (producePlanDetail.getProductIsTc() == 1){
            barCodePrintRecordDatas.add(new BarCodePrintRecord("BladeProfile", producePlanDetail.getConsumerProductName() == null ? "" : producePlanDetail.getConsumerProductName().trim()));
        }else if(producePlanDetail.getPartId() != null){
            SalesOrderDetail salesOrderDetail1 = printerDao.findById(SalesOrderDetail.class, producePlanDetail.getFromSalesOrderDetailId());
            if (salesOrderDetail1 != null){
                barCodePrintRecordDatas.add(new BarCodePrintRecord("BladeProfile", salesOrderDetail1.getBladeProfile() == null ? "" : salesOrderDetail1.getBladeProfile().trim()));
            }
        }
        map.clear();
        map.put("productId", salesOrderDetail.getProductId());
        List<FinishedProductPrintRecord> listFinishedProductPrintRecord = findListByMap(FinishedProductPrintRecord.class, map);
        for (FinishedProductPrintRecord entity : listFinishedProductPrintRecord) {
            barCodePrintRecordDatas.add(new BarCodePrintRecord(entity.getPrintAttribute(), entity.getPrintAttributeContent()));
        }

        String partName = plan.getPartName() == null ? "" : plan.getPartName().trim();

        barCodePrintRecordDatas.add(new BarCodePrintRecord("SalesOrderSubCode", plan.getSalesOrderCode() == null ? "" : plan.getSalesOrderCode().trim()));
        barCodePrintRecordDatas.add(new BarCodePrintRecord("BatchCode", plan.getBatchCode() == null ? "" : plan.getBatchCode().trim()));
        barCodePrintRecordDatas.add(new BarCodePrintRecord("ConSumerName", plan.getConsumerName() == null ? "" : plan.getConsumerName().trim()));
        barCodePrintRecordDatas.add(new BarCodePrintRecord("DeviceCode", plan.getDeviceCode() == null ? "" : plan.getDeviceCode().trim()));
        barCodePrintRecordDatas.add(new BarCodePrintRecord("ProductModel", plan.getProductModel() == null ? "" : plan.getProductModel().trim()));

        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        barCodePrintRecordDatas.add(new BarCodePrintRecord("ProduceDate", formatter.format(c.getTime())));
        String year = "000" + (c.get(Calendar.YEAR) - 1999);
        String day = "00" + c.get(Calendar.DAY_OF_YEAR);

        year = year.substring(year.length() - 3);
        day = day.substring(day.length() - 3);
        String dateString = year + day;
        barCodePrintRecordDatas.add(new BarCodePrintRecord("DateString", dateString));
        FinishedProduct fdp = findById(FinishedProduct.class, plan.getProductId());

        if (fdm != null) {
            tcType = fdm.getProductIsTc();
            barCodePrintRecordDatas.add(new BarCodePrintRecord("ConSumerProductName", fdm.getConsumerProductName() == null ? "" : fdm.getConsumerProductName().trim()));
            barCodePrintRecordDatas.add(new BarCodePrintRecord("FactoryProductName", fdm.getFactoryProductName() == null ? "" : fdm.getFactoryProductName()));//厂内名称
            barCodePrintRecordDatas.add(new BarCodePrintRecord("ProductLength", fdm.getProductRollLength() == null ? "" : fdm.getProductRollLength().toString()));//产品长度
            barCodePrintRecordDatas.add(new BarCodePrintRecord("MaterialCode", fdm.getMaterielCode() == null ? "" : fdm.getMaterielCode().toString()));//物料代码
            barCodePrintRecordDatas.add(new BarCodePrintRecord("ProductSheLfLife", fdm.getProductShelfLife() == null ? "" : fdm.getProductShelfLife().toString()));//保质期
            String ConSumerProductPart = (fdm.getConsumerProductName() == null ? "" : fdm.getConsumerProductName().trim()) + " " + (plan.getPartName() == null ? "" : plan.getPartName().trim());
            barCodePrintRecordDatas.add(new BarCodePrintRecord("ConSumerProductPart", ConSumerProductPart));
            barCodePrintRecordDatas.add(new BarCodePrintRecord("RollGrossWeight", fdm.getRollGrossWeight() == null ? "" : fdm.getRollGrossWeight() + ""));
        } else {
            tcType = fdp.getProductIsTc();
            barCodePrintRecordDatas.add(new BarCodePrintRecord("ConSumerProductName", fdp.getConsumerProductName() == null ? "" : fdp.getConsumerProductName().trim()));
            barCodePrintRecordDatas.add(new BarCodePrintRecord("FactoryProductName", fdp.getFactoryProductName() == null ? "" : fdp.getFactoryProductName()));//厂内名称
            barCodePrintRecordDatas.add(new BarCodePrintRecord("ProductLength", fdp.getProductRollLength() == null ? "" : fdp.getProductRollLength().toString()));//产品长度
            barCodePrintRecordDatas.add(new BarCodePrintRecord("MaterialCode", fdp.getMaterielCode() == null ? "" : fdp.getMaterielCode().toString()));//物料代码
            barCodePrintRecordDatas.add(new BarCodePrintRecord("ProductSheLfLife", fdp.getProductShelfLife() == null ? "" : fdp.getProductShelfLife().toString()));//保质期
            String ConSumerProductPart = (fdp.getConsumerProductName() == null ? "" : fdp.getConsumerProductName().trim()) + " " + (plan.getPartName() == null ? "" : plan.getPartName().trim());
            barCodePrintRecordDatas.add(new BarCodePrintRecord("ConSumerProductPart", ConSumerProductPart));
            barCodePrintRecordDatas.add(new BarCodePrintRecord("RollGrossWeight", fdp.getRollGrossWeight() == null ? "" : fdp.getRollGrossWeight() + ""));
        }

        switch (type) {
            case "part":
                barCodePrintRecordDatas.add(new BarCodePrintRecord("PartBarCode", barCode));
                break;
            case "box":
                barCodePrintRecordDatas.add(new BarCodePrintRecord("BoxBarCode", barCode));
                break;
            case "roll_peibu":
            case "roll":
                barCodePrintRecordDatas.add(new BarCodePrintRecord("RollBarCode", barCode));
                if (fdm != null) {
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("RollLength", fdm.getProductRollLength() == null ? "" : fdm.getProductRollLength().toString()));
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("RollNetWeight", fdm.getProductRollWeight() == null ? "" : fdm.getProductRollWeight().toString()));
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("RollWidth", fdm.getProductWidth() == null ? "" : fdm.getProductWidth().toString()));
                    String rollBarCodeLength = barCode + " " + (fdm.getProductRollLength() == null ? "  " : fdm.getProductRollLength().toString()) + "M";
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("RollBarCodeLength", rollBarCodeLength));
                } else {
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("RollLength", fdp.getProductRollLength() == null ? "" : fdp.getProductRollLength().toString()));
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("RollNetWeight", fdp.getProductRollWeight() == null ? "" : fdp.getProductRollWeight().toString()));
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("RollWidth", fdp.getProductWidth() == null ? "" : fdp.getProductWidth().toString()));
                    String rollBarCodeLength = barCode + " " + (fdp.getProductRollLength() == null ? "  " : fdp.getProductRollLength().toString()) + "M";
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("RollBarCodeLength", rollBarCodeLength));
                }

                String danXiangBuCode = "";// 层数，卷号
                try {
                    if (plan instanceof WeavePlan) {
                        WeavePlan wp = (WeavePlan) plan;
                        try {
                            danXiangBuCode += StringUtils.isBlank(wp.getRollNo()) ? "" : ("卷号" + wp.getRollNo() == null ? "" : wp.getRollNo() + " ");
                            danXiangBuCode += StringUtils.isBlank(wp.getLevelNo()) ? "" : ("层号" + wp.getLevelNo() == null ? "" : wp.getLevelNo() + " ");
                            danXiangBuCode += StringUtils.isBlank(wp.getDrawNo()) ? "" : ("图号" + wp.getDrawNo() == null ? "" : wp.getDrawNo());
                            partName = wp.getPartName();
                            barCodePrintRecordDatas.add(new BarCodePrintRecord("RollNo", wp.getRollNo() == null ? "" : wp.getRollNo()));
                            barCodePrintRecordDatas.add(new BarCodePrintRecord("LevelNo", wp.getLevelNo() == null ? "" : wp.getLevelNo()));
                            barCodePrintRecordDatas.add(new BarCodePrintRecord("DrawNo", wp.getDrawNo() == null ? "" : wp.getDrawNo()));

                        } catch (Exception e) {
                            logger.error(e.getLocalizedMessage(), e);
                        }
                    }
                    if (fdm != null) {
                        barCodePrintRecordDatas.add(new BarCodePrintRecord("FactoryProductNamePartName", fdm.getFactoryProductName() == null ? "" : fdm.getFactoryProductName().trim() + partName));
                    } else {
                        barCodePrintRecordDatas.add(new BarCodePrintRecord("FactoryProductNamePartName", fdp.getFactoryProductName() == null ? "" : fdp.getFactoryProductName().trim() + partName));
                    }
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("DanXiangBuCode", danXiangBuCode == null ? "" : danXiangBuCode.trim()));
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }


                break;
            case "tray":
            case "traypart":
                barCodePrintRecordDatas.add(new BarCodePrintRecord("TrayLength", plan.getProductLength() == null ? "" : plan.getProductLength() == null ? "" : plan.getProductLength().toString().trim()));
                barCodePrintRecordDatas.add(new BarCodePrintRecord("TrayBarCode", barCode));
                if (fdm != null) {
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("TrayWidth", fdm.getProductWidth() == null ? "" : fdm.getProductWidth().toString()));
                } else {
                    barCodePrintRecordDatas.add(new BarCodePrintRecord("TrayWidth", fdp.getProductWidth() == null ? "" : fdp.getProductWidth().toString()));
                }

                double weight = 0;
                Tray tray = findOne(Tray.class, "trayBarcode", barCode);
                if (tray != null && null != tray.getWeight()) {
                    weight = tray.getWeight();
                }
                ProductInRecord productInRecord = findOne(ProductInRecord.class, "barCode", barCode);
                if (weight == 0 && productInRecord != null && null != productInRecord.getWeight() && null != productInRecord.getInTime()) {
                    weight = productInRecord.getWeight();
                }
                barCodePrintRecordDatas.add(new BarCodePrintRecord("TrayNetWeight", weight + ""));//托净重

                if (fdp.getProductIsTc() == ProductIsTc.FTC) {
                    try {
                        FtcBcBom ftcBcBom = findOne(FtcBcBom.class, "code", fdp.getProductPackagingCode());
                        if (ftcBcBom != null) {
                            Map<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("bid", ftcBcBom.getId());
                            map2.put("version", fdp.getProductPackageVersion());
                            List<FtcBcBomVersion> ftcBcBomVersionlist = findListByMap(FtcBcBomVersion.class, map2);
                            if (ftcBcBomVersionlist != null && ftcBcBomVersionlist.size() > 0) {
                                weight += Double.parseDouble(ftcBcBomVersionlist.get(0).getBcTotalWeight());
                            }
                        }
                    } catch (Exception ex) {

                    }
                }
                barCodePrintRecordDatas.add(new BarCodePrintRecord("TrayGrossWeight", weight + ""));//托毛重
                break;
        }

        TcBomVersionPartsMirror tcBomVersionPartsMirror = null;
        TcBomVersionMirror tcBomVersionMirror = null;
        TcBomMirror tcBomMiior = null;

        TcBomVersionParts tcBomVersionParts = null;
        TcBomVersion tcBomVersion = null;
        TcBom tcBom = null;
        String yxPartName = "";
        String tcProcBomName = "";
        String tcProcBomNameVersion = "";
        String partsWeight = "";
        String customerMaterialCode = "";
        String embryoClothName = "";//胚布名称
        if (tcType == ProductIsTc.TC && partId != null) {
            try {
                map.clear();
                map.put("salesOrderDetailId", salesOrderDetail.getId());
                map.put("versionPartsId", partId);
                List<TcBomVersionPartsMirror> partsMirrorList = findListByMap(TcBomVersionPartsMirror.class, map);
                if (partsMirrorList.size() > 0) {
                    tcBomVersionPartsMirror = partsMirrorList.get(0);
                    tcBomVersionMirror = findById(TcBomVersionMirror.class, tcBomVersionPartsMirror.getTcProcBomVersoinId());
                    tcBomMiior = findById(TcBomMirror.class, tcBomVersionMirror.getTcProcBomId());
                    partName = yxPartName = tcBomMiior.getTcProcBomName() == null ? "" : tcBomMiior.getTcProcBomName().trim() + " " + tcBomVersionPartsMirror.getTcProcBomVersionPartsName() == null ? "" : tcBomVersionPartsMirror.getTcProcBomVersionPartsName().trim();
                    tcProcBomName = tcBomMiior.getTcProcBomName() == null ? "" : tcBomMiior.getTcProcBomName().trim();
                    tcProcBomNameVersion = tcBomVersionPartsMirror.getTcProcBomVersionPartsName() == null ? "" : tcBomVersionPartsMirror.getTcProcBomVersionPartsName().trim();
                    partsWeight = tcBomVersionPartsMirror == null ? "" : tcBomVersionPartsMirror.getTcProcBomVersionPartsWeight().toString();
                    customerMaterialCode = tcBomVersionPartsMirror == null ? "" : tcBomVersionPartsMirror.getCustomerMaterialCode();
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("tcProcBomPartsId", tcBomVersionPartsMirror.getId());
                    List<TcBomVersionPartsFinishedWeightEmbryoClothMirror> listByMapMirror = findListByMap(TcBomVersionPartsFinishedWeightEmbryoClothMirror.class, map1);
                    for (int i = 0; i < listByMapMirror.size(); i++) {
                        if (i < listByMapMirror.size() - 1) {
                            embryoClothName += listByMapMirror.get(i).getEmbryoClothName() + "，";
                        } else {
                            embryoClothName += listByMapMirror.get(i).getEmbryoClothName();
                        }
                    }
                } else {
                    tcBomVersionParts = findById(TcBomVersionParts.class, partId);
                    tcBomVersion = findById(TcBomVersion.class, tcBomVersionParts.getTcProcBomVersoinId());
                    tcBom = findById(TcBom.class, tcBomVersion.getTcProcBomId());
                    partName = yxPartName = tcBom.getTcProcBomName() == null ? "" : tcBom.getTcProcBomName().trim() + " " + tcBomVersionParts.getTcProcBomVersionPartsName() == null ? "" : tcBomVersionParts.getTcProcBomVersionPartsName().trim();
                    tcProcBomName = tcBom.getTcProcBomName() == null ? "" : tcBom.getTcProcBomName().trim();
                    tcProcBomNameVersion = tcBomVersionParts.getTcProcBomVersionPartsName() == null ? "" : tcBomVersionParts.getTcProcBomVersionPartsName().trim();
                    partsWeight = tcBomVersionParts == null ? "" : tcBomVersionParts.getTcProcBomVersionPartsWeight().toString();
                    customerMaterialCode = tcBomVersionParts == null ? "" : tcBomVersionParts.getCustomerMaterialCode();
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("tcProcBomPartsId", tcBomVersionParts.getId());
                    List<TcBomVersionPartsFinishedWeightEmbryoCloth> listByMap = findListByMap(TcBomVersionPartsFinishedWeightEmbryoCloth.class, map2);
                    for (int i = 0; i < listByMap.size(); i++) {
                        if (i < listByMap.size() - 1) {
                            embryoClothName += listByMap.get(i).getEmbryoClothName() + "，";
                        } else {
                            embryoClothName += listByMap.get(i).getEmbryoClothName();
                        }
                    }
                }


            } catch (Exception e) {
                barCodePrintRecordDatas.add(new BarCodePrintRecord("TcProcBomNameVersion", "** "));
            }
        }

        barCodePrintRecordDatas.add(new BarCodePrintRecord("PartName", partName));
        barCodePrintRecordDatas.add(new BarCodePrintRecord("TcProcBomName", tcProcBomName));
        barCodePrintRecordDatas.add(new BarCodePrintRecord("TcProcBomNameVersion", tcProcBomName));
        barCodePrintRecordDatas.add(new BarCodePrintRecord("PartsWeight", partsWeight));
        barCodePrintRecordDatas.add(new BarCodePrintRecord("YxPartName", yxPartName));
        barCodePrintRecordDatas.add(new BarCodePrintRecord("CustomerMaterialCode", customerMaterialCode));
        barCodePrintRecordDatas.add(new BarCodePrintRecord("EmbryoClothName", embryoClothName));

        barCodePrintRecordDatas.add(new BarCodePrintRecord("BarCode", barCode + ";;;;"));

        map.clear();
        map.put("btwFileId", btwfileId);
        map.put("producePlanDetailId", plan.getProducePlanDetailId());
        List<ProducePlanDetailPrint> ProducePlanDetailPrintlist = findListByMap(ProducePlanDetailPrint.class, map);
        for (ProducePlanDetailPrint producePlanDetailPrint : ProducePlanDetailPrintlist) {
            if (number != -1 && producePlanDetailPrint.getPrintAttribute().equals("xuhao")){
                String[] split = producePlanDetailPrint.getPrintAttributeContent().split("-");
                String before=split[0];
                String order=split[split.length - 1];
                int nowNumber=Integer.parseInt(order)+number;
                String after=""+nowNumber;
                if (after.length() < 4){
                    int a=3;
                    int b=a-after.length();
                    switch (b){
                        case 1:
                            after="0"+after;
                            break;
                        case 2:
                            after="00"+after;
                            break;
                    }
                }
                //producePlanDetailPrint.setPrintAttributeContent(before+"-"+after);
                barCodePrintRecordDatas.add(new BarCodePrintRecord(producePlanDetailPrint.getPrintAttribute(), before+"-"+after));
            }else{
                barCodePrintRecordDatas.add(new BarCodePrintRecord(producePlanDetailPrint.getPrintAttribute(), producePlanDetailPrint.getPrintAttributeContent()));
            }
        }

        IBarcode barcode;
        if (type.equals("roll") || type.equals("roll_peibu")) {
            barcode = new RollBarcode();
        } else if (type.equals("tray") || type.equals("traypart")) {
            barcode = new TrayBarCode();
        } else if (type.equals("box")) {
            barcode = new BoxBarcode();
        } else {
            barcode = new PartBarcode();
        }
        barcode.setProducePlanDetailId(plan.getProducePlanDetailId());
        barcode.setBarcode(barCode);
        barcode.setSalesOrderCode(plan.getSalesOrderCode());
        barcode.setSalesProductId(plan.getProductId());
        barcode.setBatchCode(plan.getBatchCode());
        barcode.setPartName(plan.getPartName());
        barcode.setSalesOrderDetailId(plan.getFromSalesOrderDetailId());
        barcode.setPartId(partId);
        barcode.setPlanId(plan.getId());
        barcode.setPrintDate(new Date());
        barcode.setSalesProductId(plan.getProductId());

        if (customerBarCode != null && customerBarCode.length() > 0) {
            barCodePrintRecordDatas.add(new BarCodePrintRecord("CustomerBarCode", customerBarCode));
        } else {
            BtwFile btwFile = findById(BtwFile.class, btwfileId);
            if (btwFile != null && btwFile.getConsumerBarCodeDigit() != null) {
                btwFile.setConsumerBarCodeRecord(btwFile.getConsumerBarCodeRecord() == null ? 0 : btwFile.getConsumerBarCodeRecord() + 1);
                btwFile.setAgentBarCodeRecord(btwFile.getAgentBarCodeRecord() == null ? 0 : btwFile.getAgentBarCodeRecord() + 1);
                barcode.setCustomerBarCode(GetMaxBarCode(btwFile.getConsumerBarCodePrefix() == null ? "" : btwFile.getConsumerBarCodePrefix(), btwFile.getConsumerBarCodeRecord(), btwFile.getConsumerBarCodeDigit() == null ? 0 : btwFile.getConsumerBarCodeDigit()));
                barcode.setAgentBarCode(GetMaxBarCode(btwFile.getAgentBarCodePrefix() == null ? "" : btwFile.getAgentBarCodePrefix(), btwFile.getAgentBarCodeRecord(), btwFile.getAgentBarCodeDigit() == null ? 0 : btwFile.getAgentBarCodeDigit()));
                barCodePrintRecordDatas.add(new BarCodePrintRecord("CustomerBarCode", barcode.getCustomerBarCode()));
                barCodePrintRecordDatas.add(new BarCodePrintRecord("AgentBarCode", barcode.getAgentBarCode()));
                printerDao.update(btwFile);
            }
        }
        barcode.setIndividualOutPutString(GsonTools.toJson(barCodePrintRecordDatas));
        return barcode;
    }

    public static String GetMaxBarCode(String BarCodePrefix, Integer barCodeRecord, Integer barCodeDigit) {
        String result = "";
        for (int i = 0; i < barCodeDigit; i++) {
            result += "0";
        }
        if (barCodeDigit - barCodeRecord.toString().length() >= 0) {
            result = BarCodePrefix + result.substring(0, barCodeDigit - barCodeRecord.toString().length()) + barCodeRecord.toString();
        }
        return result;
    }

    //jxl-add-2020-04-30 合并标签
    public String doIndividualPrintBarcode(String weavePlanId, String cutPlanId, String count, String pName, String type, String partName, String departmentCode, String trugPlanId, Long partId, String btwfileId, String devCode,String copies) throws Exception {
        Integer counts = Integer.parseInt(count.split("\\.")[0]);
        if (count == null || counts == 0) {
            return GsonTools.toJson("打印数量不能为空！");
        }

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
            }
        }

        Iplan plan = null;
        if (weavePlanId != null && !"null".equals(weavePlanId)) {
            plan = salesOrderService.findById(WeavePlan.class, Long.parseLong(weavePlanId));
        } else if (cutPlanId != null && !"null".equals(cutPlanId)) {
            plan = salesOrderService.findById(CutPlan.class, Long.parseLong(cutPlanId));
        }

        plan.setDeviceCode(devCode == null ? "" : devCode);
        plan.setPartName(partName);

        List<IBarcode> li = getOutputString(plan, type, counts, pName, departmentCode, partId, Long.parseLong(btwfileId));

        BtwFile btwFile = salesOrderService.findById(BtwFile.class, Long.parseLong(btwfileId));
        PrinterOut printStrings = getQRBarCode(li, pName, fileUrl + btwFile.getConsumerCode() + "\\" + btwFile.getTagActName(), "");

        try {
            int result = 99;
            List<IBarcode> li1 = printStrings.getLi();
            for (IBarcode i : li1) {
                if (i instanceof TrayBarCode) {// 托打包时出4张条码
                    result = PrintUtils.printYu(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), i.getIndividualOutPutString(), 4);
                } else if (i instanceof PartBarcode) {    //部件条码打印5张
                    result = PrintUtils.printYu(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), i.getIndividualOutPutString(), Integer.parseInt(copies));
                } else {
                    result = PrintUtils.printYu(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), i.getIndividualOutPutString(), 1);
                }
            }
            switch (result) {
                case 10:
                    return GsonTools.toJson("打印成功");
                case 20:
                    return GsonTools.toJson("找不到打印机");
                default:
                    return GsonTools.toJson("打印失败");
            }
        } catch (Exception e) {
            logger.error("打印机：" + printStrings.getPrinterName() + ",\t模板：" + printStrings.getBtwFileUrl() + ";错误：", e);
            return GsonTools.toJson(e.getMessage());
        }
    }

    /**
     * @param
     * @return
     */
    private List<IBarcode> getOutputString(Iplan plan, String type, int count, String printName, String departmentCode, Long partId, Long btwfileId) {

        List<IBarcode> li = new ArrayList();
        if (plan instanceof WeavePlan) {
            switch (type) {
                case "roll"://如果是卷，并且存在部件则打印胚布卷
                    if (plan.getPartId() != null) {
                        type = "roll_peibu";
                    }
                    break;
            }
        } else if (plan instanceof CutPlan) {
            if (type == null) type = "part";
        } else {
            if (type == null) type = "roll";
        }

        String prefixPrint = "HS";
        List<Department> listDepartment = departmentService.find(Department.class, "code", departmentCode);
        prefixPrint = (listDepartment != null && listDepartment.size() > 0) ? listDepartment.get(0).getPrefix() : "";
        if (prefixPrint.startsWith("BZ") && type == "trayPart") {
            prefixPrint = "HS" + prefixPrint.substring(2);
        }

        List<String> barcodelist = PrintBarCode.getInstance().getBarCodeList(type, prefixPrint, count);
        int number = 0;
        for (String barcode : barcodelist) {
            li.add(getBarcode(plan, type, barcode, partId, btwfileId, "",number));
            number++;
        }

        for (IBarcode i : li) {
            i.setBtwfileId(btwfileId);
            if (i instanceof RollBarcode) {
                save((RollBarcode) i);
            } else if (i instanceof BoxBarcode) {
                save((BoxBarcode) i);
            } else if (i instanceof TrayBarCode) {
                TrayBarCode tbc = (TrayBarCode) i;
                save(tbc);
            } else if (i instanceof PartBarcode) {
                save((PartBarcode) i);
            }
        }
        return li;
    }


    /**
     * 打印空的条码新
     *
     * @param departmentCode 0:裁剪车间，1：编织1车间，2：编织2车间，3：编织3车间
     * @param pName          打印机名称
     * @param type           条码类型：roll：卷条码，box：盒条码，tray：托条码，part：部件条码
     * @param count          打印数量
     * @return
     */
    public String doPrintBarcode(String weavePlanId, String departmentCode, String pName, String type, int count, long partId, String btwfileId) throws Exception {
        if (count == 0) {
            throw new MyException("打印数量不能为0");
        }
        if (StringUtils.isBlank(weavePlanId)) {
            throw new MyException("编织计划id不能为空");
        }

        Iplan plan = salesOrderService.findById(WeavePlan.class, Long.parseLong(weavePlanId));
        List<IBarcode> li = getOutputString(plan, type, count, pName, departmentCode, partId, Long.parseLong(btwfileId));

        BtwFile btwFile = salesOrderService.findById(BtwFile.class, Long.parseLong(btwfileId));
        PrinterOut printStrings = getQRBarCode(li, pName, fileUrl + btwFile.getConsumerCode() + "\\" + btwFile.getTagActName(), "");

        List<IBarcode> li1 = printStrings.getLi();
        int result = 99;
        for (IBarcode i : li1) {
            if (i instanceof TrayBarCode) {
                result = PrintUtils.printYu(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), i.getIndividualOutPutString(), 3);
            } else {
                result = PrintUtils.printYu(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), i.getIndividualOutPutString(), 1);
            }
        }

        switch (result) {
            case 10:
                return "打印成功";
            case 20:
                return "找不到打印机";
            default:
                return "打印失败";
        }
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

    /**
     * 打印个性化条码标签
     *
     * @param id        条码id
     * @param type      条码类型：roll：卷条码，box：盒条码，tray：托条码，part：部件条码
     * @param pName     打印机名称
     * @param btwfileId 个性化标签模版
     * @return
     */
    @Override
    public String doReplayPrintBarcode(String id, String type, String departmentCode, String pName, String btwfileId) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new MyException("条码id不能为空");
        }

        String barCode = "";
        WeavePlan weavePlan;
        switch (type) {
            case "roll":
            case "roll_peibu":
                RollBarcode roll = findById(RollBarcode.class, Long.parseLong(id));
                barCode = roll.getBarcode();
                weavePlan = findById(WeavePlan.class, roll.getPlanId());
                break;
            case "box":
                BoxBarcode box = findById(BoxBarcode.class, Long.parseLong(id));
                barCode = box.getBarcode();
                if (box.getPlanId() == null) {
                    throw new MyException("请先打包盒条码");
                }
                weavePlan = findById(WeavePlan.class, box.getPlanId());
                break;
            case "part":
                PartBarcode part = findById(PartBarcode.class, Long.parseLong(id));
                weavePlan = findById(WeavePlan.class, part.getPlanId());
                break;
            default:
                type = "tray";
                TrayBarCode traybarcode = findById(TrayBarCode.class, Long.parseLong(id));
                barCode = traybarcode.getBarcode();
                if (traybarcode.getPlanId() == null) {
                    throw new MyException("请先打包托条码");
                }
                weavePlan = findById(WeavePlan.class, traybarcode.getPlanId());
                break;
        }

        if (weavePlan == null) {
            throw new MyException("编织为空");
        }

        IBarcode ibarcode = getBarcode(weavePlan, type, barCode, weavePlan.getPartId(), Long.parseLong(btwfileId), "",-1);
        List<IBarcode> li = new ArrayList();
        li.add(ibarcode);

        BtwFile btwFile = salesOrderService.findById(BtwFile.class, Long.parseLong(btwfileId));

        PrinterOut printStrings = getQRBarCode(li, pName, fileUrl + btwFile.getConsumerCode() + "\\" + btwFile.getTagActName(), "");
        List<IBarcode> li1 = printStrings.getLi();
        int result = 99;
        for (IBarcode i : li1) {
            if (i instanceof TrayBarCode) {
                result = PrintUtils.printYu(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), i.getIndividualOutPutString(), 3);
            } else {
                result = PrintUtils.printYu(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), i.getIndividualOutPutString(), 1);
            }
        }

        switch (result) {
            case 10:
                return "打印成功";
            case 20:
                return "找不到打印机";
            default:
                return "打印失败";
        }
    }

    public String rePrint(String id, String pName, String type) {
        IBarcode ib = null;
        String refileUrl = fileUrl;
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

        if (ib.getBtwfileId() == null || ib.getBtwfileId() == 0) {
            if (type.equals("roll")) {
                btwName = "标准版\\恒石条码(卷).btw";
            } else if (type.equals("roll_peibu")) {
                btwName = "标准版\\恒石条码(胚布).btw";
            } else if (type.equals("part")) {
                btwName = "标准版\\恒石条码(部件).btw";
            } else if (type.equals("box")) {
                btwName = "标准版\\恒石条码(盒)_空.btw";
            } else {
                btwName = "标准版\\恒石条码(托)_空.btw";
            }
        } else {
            BtwFile btwFile = salesOrderService.findById(BtwFile.class, ib.getBtwfileId());
            if (btwFile == null) {
                return "找不到打印标签";
            }
            btwName = btwFile.getTagActName();
            refileUrl = fileUrl + btwFile.getConsumerCode() + "\\";
        }

        PrinterOut printStrings = new PrinterOut();
        printStrings.setBtwFileUrl(refileUrl + btwName);
        printStrings.setPrinterName(pName);

        String str = "打印失败";
        try {
            int i;
            int printCount = ib instanceof TrayBarCode ? 3 : 1;
            i = PrintUtils.printYu(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), ib.getIndividualOutPutString(), printCount);
            switch (i) {
                case 10:
                    return "打印成功";
                case 20:
                    return "找不到打印机";
                default:
                    return "打印成功";
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return str;
        }
    }

    public String reIndividualPrint(String id, String pName, String type, long btwfileId, int printCount) {
        IBarcode iBarcode;
        String refileUrl = fileUrl;
        Iplan plan;
        if (type.equals("roll")) {
            RollBarcode r = findById(RollBarcode.class, Long.parseLong(id));
            iBarcode = r;
            plan = findById(WeavePlan.class, r.getPlanId());
            if (plan.getPartId() != null) {
                type = "roll_peibu";
            }
        } else if (type.equals("box")) {
            BoxBarcode box = findById(BoxBarcode.class, Long.parseLong(id));
            if (box.getPlanId() == null) {
                return "请先打包盒条码";
            }
            iBarcode = box;
            plan = findById(WeavePlan.class, box.getPlanId());
        } else if (type.equals("part")) {
            PartBarcode part = findById(PartBarcode.class, Long.parseLong(id));
            iBarcode = part;
            plan = findById(CutPlan.class, part.getPlanId());
        } else {
            TrayBarCode traybarcode = findById(TrayBarCode.class, Long.parseLong(id));
            iBarcode = traybarcode;
            if (traybarcode.getPlanId() == null) {
                return "请先打包托条码";
            }
            plan = findById(WeavePlan.class, traybarcode.getPlanId());
        }

        if (!(iBarcode.getIndividualOutPutString() != null && iBarcode.getIndividualOutPutString().length() > 0)) {
            IBarcode ibarcode = getBarcode(plan, type, iBarcode.getBarcode(), iBarcode.getPartId(), btwfileId, iBarcode.getCustomerBarCode(),-1);
            iBarcode.setIndividualOutPutString(ibarcode.getIndividualOutPutString());
            iBarcode.setCustomerBarCode(ibarcode.getCustomerBarCode());
            iBarcode.setAgentBarCode(ibarcode.getAgentBarCode());
            iBarcode.setBtwfileId(btwfileId);
            if (iBarcode instanceof RollBarcode) {
                update((RollBarcode) iBarcode);
            } else if (iBarcode instanceof BoxBarcode) {
                update((BoxBarcode) iBarcode);
            } else if (iBarcode instanceof TrayBarCode) {
                update((TrayBarCode) iBarcode);
            } else if (iBarcode instanceof PartBarcode) {
                update((PartBarcode) iBarcode);
            }
        }

        if (btwfileId == 0) {
            if (type.equals("roll")) {
                btwName = "标准版\\恒石条码(卷).btw";
            } else if (type.equals("roll_peibu")) {
                btwName = "标准版\\恒石条码(胚布).btw";
            } else if (type.equals("part")) {
                btwName = "标准版\\恒石条码(部件).btw";
            } else if (type.equals("box")) {
                btwName = "标准版\\恒石条码(盒)_空.btw";
            } else {
                btwName = "标准版\\恒石条码(托)_空.btw";
            }
        } else {
            BtwFile btwFile = salesOrderService.findById(BtwFile.class, btwfileId);
            if (btwFile == null) {
                return "找不到打印标签";
            }
            btwName = btwFile.getTagActName();
            refileUrl = fileUrl + btwFile.getConsumerCode() + "\\";
        }

        PrinterOut printStrings = new PrinterOut();
        printStrings.setBtwFileUrl(refileUrl + btwName);
        printStrings.setPrinterName(pName);

        try {
            int i = PrintUtils.printYu(printStrings.getPrinterName(), printStrings.getBtwFileUrl(), iBarcode.getIndividualOutPutString(), printCount);
            switch (i) {
                case 10:
                    return "打印成功";
                case 20:
                    return "找不到打印机";
                default:
                    return "打印成功";
            }
        } catch (Exception e) {
            logger.error("打印机：" + printStrings.getPrinterName() + ",\t模板：" + printStrings.getBtwFileUrl() + ";错误：", e);
            return "打印失败";
        }
    }
}

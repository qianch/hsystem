/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.delivery.service.impl;

import com.bluebirdme.mes.audit.entity.AuditInstance;
import com.bluebirdme.mes.common.service.IMessageCreateService;
import com.bluebirdme.mes.core.annotation.AnyExceptionRollback;
import com.bluebirdme.mes.core.base.dao.IBaseDao;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.base.service.BaseServiceImpl;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.planner.delivery.dao.IDeliveryPlanDao;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanDetails;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanSalesOrders;
import com.bluebirdme.mes.planner.delivery.helper.QRCode;
import com.bluebirdme.mes.planner.delivery.service.IDeliveryPlanService;
import com.bluebirdme.mes.planner.pack.entity.PackTask;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.platform.entity.MessageType;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xdemo.superutil.j2se.ObjectUtils;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.j2se.ZipUtils;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 徐波
 * @Date 2016-11-2 9:30:07
 */
@Service
@AnyExceptionRollback
public class DeliveryPlanServiceImpl extends BaseServiceImpl implements IDeliveryPlanService {
    private static Logger logger = LoggerFactory.getLogger(DeliveryPlanServiceImpl.class);
    @Resource
    IDeliveryPlanDao deliveryPlanDao;
    @Resource
    IMessageCreateService msgCreateService;

    @Override
    protected IBaseDao getBaseDao() {
        return deliveryPlanDao;
    }

    @Override
    public <T> Map<String, Object> findPageInfo(Filter filter, Page page) throws Exception {
        return deliveryPlanDao.findPageInfo(filter, page);
    }

    @Override
    public <T> Map<String, Object> findTcPageInfo(Filter filter, Page page) throws Exception {
        return deliveryPlanDao.findTcPageInfo(filter, page);
    }

    @Override
    public void saveDatas(DeliveryPlan deliveryPlan) throws Exception {
        if (deliveryPlan.getId() == null) {
            deliveryPlanDao.save(deliveryPlan);
        } else {
            deliveryPlanDao.update(deliveryPlan);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("deliveryId", deliveryPlan.getId());
            delete(DeliveryPlanDetails.class, map);
            delete(DeliveryPlanSalesOrders.class, map);
        }
        for (DeliveryPlanSalesOrders dpd : deliveryPlan.getOrderDatas()) {
            String fileName = PathUtils.getDrive() + "barcodePic" + File.separator + dpd.getPackingNumber() + ".PNG";
            File file = new File(fileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            if (!file.exists()) {
                QRCode.encode(dpd.getPackingNumber() + ";" + deliveryPlan.getDeliveryCode() + "-" + dpd.getPn(), fileName, 200, 200);
            }
            dpd.setDeliveryId(deliveryPlan.getId());
        }
        save(deliveryPlan.getOrderDatas().toArray(new DeliveryPlanSalesOrders[]{}));
        // 根据出货计划订单的序号绑定产品所属的id
        for (DeliveryPlanDetails dpd : deliveryPlan.getProductDatas()) {
            dpd.setDeliveryId(deliveryPlan.getId());
        }
        save(deliveryPlan.getProductDatas().toArray(new DeliveryPlanDetails[]{}));
    }

    @Override
    public void copy(String id) throws Exception {
        DeliveryPlan deliveryPlan = findById(DeliveryPlan.class, Long.parseLong(id));
        DeliveryPlan newPlan = new DeliveryPlan();
        ObjectUtils.clone(deliveryPlan, newPlan);
        String type = deliveryPlan.getDeliveryCode().substring(0, 2);
        newPlan.setDeliveryCode(getSerial(type));
        List<DeliveryPlanDetails> productDatas = new ArrayList<DeliveryPlanDetails>();
        newPlan.setProductDatas(productDatas);
        List<DeliveryPlanSalesOrders> salesOrders = new ArrayList<DeliveryPlanSalesOrders>();
        newPlan.setOrderDatas(salesOrders);
        newPlan.setAuditState(0);
        newPlan.setIsClosed(null);
        newPlan.setId(null);
        saveDatas(newPlan);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("deliveryId", deliveryPlan.getId());
        // 出库订单关联信息
        List<DeliveryPlanSalesOrders> dpso = findListByMap(DeliveryPlanSalesOrders.class, map);
        List<DeliveryPlanSalesOrders> newDpso = new ArrayList<>();
        Map<String, String> packingMap = new HashMap<>();
        for (DeliveryPlanSalesOrders orders : dpso) {
            DeliveryPlanSalesOrders newOrders = new DeliveryPlanSalesOrders();
            ObjectUtils.clone(orders, newOrders);
            newOrders.setId(null);
            newOrders.setDeliveryId(newPlan.getId());
            String packingNum = UUID.randomUUID().toString();
            packingMap.put(orders.getPackingNumber(), packingNum);
            newOrders.setPackingNumber(packingNum);
            newOrders.setIsFinished(0);
            newDpso.add(newOrders);
        }
        newPlan.setOrderDatas(newDpso);
        // 出货详情
        List<DeliveryPlanDetails> dpd = findListByMap(DeliveryPlanDetails.class, map);
        List<DeliveryPlanDetails> newDpd = new ArrayList<>();
        for (DeliveryPlanDetails details : dpd) {
            DeliveryPlanDetails newDetails = new DeliveryPlanDetails();
            ObjectUtils.clone(details, newDetails);
            newDetails.setId(null);
            newDetails.setDeliveryId(newPlan.getId());
            newDetails.setPackingNumber(packingMap.get(details.getPackingNumber()));
            newDpd.add(newDetails);
        }
        newPlan.setProductDatas(newDpd);
        saveDatas(newPlan);
    }

    public String copyBarcodeImgs(String ids, String uuid) throws FileNotFoundException {
        // 获取要下载的所有出货计划
        File zipFile = new File(PathUtils.getDrive() + "barcodePic" + File.separator + uuid);
        zipFile.mkdirs();
        String[] idsArray = ids.split(",");
        for (String idstring : idsArray) {
            DeliveryPlan dp = findById(DeliveryPlan.class, Long.parseLong(idstring));
            // 获得出货计划下所有的DeliveryPlanSalesOrders
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("deliveryId", dp.getId());
            List<DeliveryPlanSalesOrders> dpsos = deliveryPlanDao.findListByMap(DeliveryPlanSalesOrders.class, map);
            // 为用户创建私人的打包文件夹，在文件夹中创建发货计划文件夹

            File dpsoFile = new File(PathUtils.getDrive() + "barcodePic" + File.separator + uuid + File.separator + dp.getDeliveryCode());

            dpsoFile.mkdir();
            for (DeliveryPlanSalesOrders dpso : dpsos) {
                // 将每个发货计划下箱的二维码图片复制到打包文件夹
                String oldPath = PathUtils.getDrive() + "barcodePic" + File.separator + dpso.getPackingNumber() + ".PNG";
                String newPath = PathUtils.getDrive() + "barcodePic" + File.separator + uuid + File.separator + dp.getDeliveryCode() + File.separator + dp.getDeliveryCode() + "-" + dpso.getPn() + ".PNG";
                copyFile(oldPath, newPath);
            }
        }
        // 将打包文件夹打入压缩包
        ZipUtils.zipDir(PathUtils.getDrive() + "barcodePic" + File.separator + uuid + ".zip", PathUtils.getDrive() + "barcodePic" + File.separator + uuid);
        zipFile.deleteOnExit();
        return PathUtils.getDrive() + "barcodePic" + File.separator + uuid + ".zip";
    }

    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            logger.info("复制单个文件操作出错");
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void deleteDatas(List<DeliveryPlan> list) {
        List<DeliveryPlanDetails> deleteProducts = new ArrayList<DeliveryPlanDetails>();
        List<DeliveryPlanSalesOrders> deleteOrders = new ArrayList<DeliveryPlanSalesOrders>();
        for (DeliveryPlan dp : list) {
            deleteProducts.addAll(dp.getProductDatas());
            deleteOrders.addAll(dp.getOrderDatas());
        }
        deliveryPlanDao.delete(list.toArray(new DeliveryPlan[]{}));
        delete(deleteProducts.toArray(new DeliveryPlanDetails[]{}));
        delete(deleteOrders.toArray(new DeliveryPlanSalesOrders[]{}));
    }

    @Override
    public List<Map<String, Object>> findDeliveryCode() {
        return deliveryPlanDao.findDeliveryCode();
    }

    public List<Map<String, Object>> getDeliveryProducts(Long deliveryId) {
        return deliveryPlanDao.getDeliveryProducts(deliveryId);
    }

    public synchronized String getSerial(String type) {
        return deliveryPlanDao.getSerial(type);
    }

    @Override
    public void deleteAll(String ids) {
        deliveryPlanDao.delete(DeliveryPlan.class, ids);
        Map<String, Object> map = new HashMap<String, Object>();
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            map.put("deliveryId", Long.valueOf(id[a]));
            deliveryPlanDao.delete(DeliveryPlanDetails.class, map);
            deliveryPlanDao.delete(DeliveryPlanSalesOrders.class, map);
        }
    }

    @Override
    public void cannel(String ids) {
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            DeliveryPlan dp = deliveryPlanDao.findById(DeliveryPlan.class, Long.valueOf(id[a]));
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("formId", dp.getId());
            map.put("entityJavaClass", dp.getClass().getName());
            List<AuditInstance> ai = deliveryPlanDao.findListByMap(AuditInstance.class, map);
            deliveryPlanDao.delete(ai.toArray(new AuditInstance[]{}));
            dp.setAuditState(0);
            update(dp);
        }
    }

    @Override
    public void closeDeliveryPlan(Long did) {
        DeliveryPlan dp = findById(DeliveryPlan.class, did);

        dp.setIsClosed(1);
        update(dp);
        String comment = "发货计划关闭，发货计划号：" + dp.getDeliveryCode();
        msgCreateService.createClose(comment, MessageType.DELIVERY_PLAN_CLOSE);
    }

    @Override
    public String releaseDeliveryPlan(Long did) {
        DeliveryPlan dp = findById(DeliveryPlan.class, did);
        if (dp == null) {
            return "出库计划不存在";
        }
        dp.setIsComplete(-1);
        update(dp);
        List<DeliveryPlanSalesOrders> listDeliveryPlanSalesOrders = find(DeliveryPlanSalesOrders.class, "deliveryId", did);
        List<DeliveryPlanSalesOrders> list = new ArrayList<>();
        for (DeliveryPlanSalesOrders order : listDeliveryPlanSalesOrders) {
            order.setIsFinished(0);
            list.add(order);
        }
        update(list.toArray(new DeliveryPlanSalesOrders[]{}));
        return Constant.AJAX_SUCCESS;
    }

    @Override
    public <T> Map<String, Object> findPageInfoDelivery(Filter filter, Page page) throws Exception {
        return deliveryPlanDao.findPageInfoDelivery(filter, page);
    }

    public List<Map<String, Object>> getBatchCodeCountBySalesOrderCode(String salesOrderCode, Long productId, Long partId) {
        return deliveryPlanDao.getBatchCodeCountBySalesOrderCode(salesOrderCode, productId, partId);
    }

    @Override
    public List<Map<String, Object>> searchProduct(String salesOrderSubCode, String batchCode, Long productId, Long partId) {
        return deliveryPlanDao.searchProduct(salesOrderSubCode, batchCode, productId, partId);
    }

    @Override
    public List<Map<String, Object>> searchAuditer(String entityJavaClass, Long formId) {
        return deliveryPlanDao.searchAuditer(entityJavaClass, formId);
    }

    @Override
    public List<Map<String, Object>> findProductOutRecordByPackingNumber(String packingNumber) {
        return deliveryPlanDao.findProductOutRecordByPackingNumber(packingNumber);
    }

    public List<String> cars() {
        return deliveryPlanDao.cars();
    }

    @Override
    public int getxdl(String id, String pch) {
        return deliveryPlanDao.getxdl(id, pch);
    }


    @Override
    public int getOrderXdl(long salesOrderDetailId, String partName) {
        int intOrderXdl = 0;
        SalesOrderDetail salesOrderDetail = deliveryPlanDao.findOne(SalesOrderDetail.class, "id", salesOrderDetailId);
        FinishedProduct finishedProduct = deliveryPlanDao.findOne(FinishedProduct.class, "id", salesOrderDetail.getProductId());
        //如果是套材
        if (finishedProduct.getProductIsTc() == 1) {
            List<SalesOrderDetailPartsCount> listSalesOrderDetailPartsCount = deliveryPlanDao.find(SalesOrderDetailPartsCount.class, "salesOrderDetailId", salesOrderDetailId);
            for (SalesOrderDetailPartsCount salesOrderDetailPartsCount : listSalesOrderDetailPartsCount) {
                if (partName.equals(salesOrderDetailPartsCount.getPartName())) {
                    intOrderXdl += salesOrderDetailPartsCount.getPartCount();
                }
            }
        } else {//如果是非套材
            List<PackTask> listPackTask = deliveryPlanDao.find(PackTask.class, "sodId", salesOrderDetailId);
            for (PackTask packTask : listPackTask) {
                intOrderXdl += packTask.getTotalCount();
            }
        }
        return intOrderXdl;
    }

    @Override
    public int getPlanXdl(long salesOrderDetailId, String batchCode, String partName) {
        int intPlanXdl = 0;
        Map<String, Object> map = new HashMap();
        map.put("fromSalesOrderDetailId", salesOrderDetailId);
        map.put("batchCode", batchCode);
        List<ProducePlanDetail> listProducePlanDetail = deliveryPlanDao.findListByMap(ProducePlanDetail.class, map);

        if (listProducePlanDetail != null && listProducePlanDetail.size() > 0) {
            Collections.sort(listProducePlanDetail, (o1, o2) -> o2.getId().compareTo(o1.getId()));
        } else {
            return intPlanXdl;
        }
        ProducePlanDetail producePlanDetail = listProducePlanDetail.get(0);
        FinishedProduct finishedProduct = deliveryPlanDao.findOne(FinishedProduct.class, "id", producePlanDetail.getProductId());
        //如果是套材
        if (finishedProduct.getProductIsTc() == 1) {
            List<ProducePlanDetailPartCount> listProducePlanDetailPartCount = deliveryPlanDao.find(ProducePlanDetailPartCount.class, "planDetailId", producePlanDetail.getId());
            for (ProducePlanDetailPartCount producePlanDetailPartCount : listProducePlanDetailPartCount) {
                if (partName.equals(producePlanDetailPartCount.getPartName())) {
                    intPlanXdl += producePlanDetailPartCount.getPlanPartCount();
                }
            }
        } else {//如果是非套材
            List<PackTask> listPackTask = deliveryPlanDao.find(PackTask.class, "ppdId", producePlanDetail.getId());
            for (PackTask packTask : listPackTask) {
                intPlanXdl += packTask.getProduceTotalCount();
            }
        }
        return intPlanXdl;
    }

    @Override
    public int getkcl(String id, String pch, String cnmc, String bjmc) {
        return deliveryPlanDao.getkcl(id, pch, cnmc, bjmc);
    }

    @Override
    public int getfhl(String id, String pch, String cnmc, String bjmc) {
        return deliveryPlanDao.getfhl(id, pch, cnmc, bjmc);
    }

    @Override
    public int getdjs(String id, String pch) {
        return deliveryPlanDao.getdjs(id, pch);
    }

    @Override
    public int getOrderFhl(String salesOrderCode) {
        return deliveryPlanDao.getOrderFhl(salesOrderCode);
    }

    @Override
    public int getDetailPlanOrderFhl(String salesOrderCode, String batchCode) {
        return deliveryPlanDao.getDetailPlanOrderFhl(salesOrderCode, batchCode);
    }


    public SXSSFWorkbook exportDeliveryExcel(String ids) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "成品发货单";
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Font font = wb.createFont();
        // font.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
        font.setFontHeightInPoints((short) 18);
        font.setBold(true); // 字体增粗
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);// 单元格自动换行
        cellStyle.setFont(font);

        CellStyle cellStyle0 = wb.createCellStyle();
        cellStyle0.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle0.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle0.setBorderBottom(BorderStyle.THIN);
        cellStyle0.setBorderTop(BorderStyle.THIN);
        cellStyle0.setBorderRight(BorderStyle.THIN);
        cellStyle0.setBorderLeft(BorderStyle.THIN);
        cellStyle0.setWrapText(true);// 单元格自动换行

        Font font1 = wb.createFont();
        // font.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
        font1.setFontHeightInPoints((short) 10);
        font1.setBold(true); // 字体增粗
        CellStyle cellStyle3 = wb.createCellStyle();
        cellStyle3.setAlignment(HorizontalAlignment.LEFT); // 左对齐
        cellStyle3.setBorderBottom(BorderStyle.THIN);
        cellStyle3.setBorderTop(BorderStyle.THIN);
        cellStyle3.setBorderRight(BorderStyle.THIN);
        cellStyle3.setBorderLeft(BorderStyle.THIN);
        cellStyle3.setFont(font1);
        Sheet sheet = wb.createSheet();
        // sheet.setDisplayGridlines(true);
        // 生成一个字体

        Row row = null;
        Cell cell = null;

        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        sheet.setColumnWidth(0, 13 * 256);// 设置列宽
        cell.setCellStyle(cellStyle);
        cell.setCellValue("浙江恒石纤维基业有限公司发货计划");
        cell = row.createCell(1);
        sheet.setColumnWidth(1, 13 * 256);// 设置列宽
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2);
        sheet.setColumnWidth(2, 13 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        sheet.setColumnWidth(3, 18 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(4);
        sheet.setColumnWidth(4, 22 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5);
        sheet.setColumnWidth(5, 13 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6);
        sheet.setColumnWidth(6, 13 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(7);
        sheet.setColumnWidth(7, 13 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(8);
        sheet.setColumnWidth(8, 13 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(9);
        sheet.setColumnWidth(9, 13 * 256);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(10);
        sheet.setColumnWidth(10, 13 * 256);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 10));
        r++;
        String cellName[] = new String[]{"计划发货号", "要货单位", "发货编号", "车辆装货地点", "装货产品", "批次号", "发货人", "备注", "仓管", "叉车"};
        row = sheet.createRow(r);
        for (int b = 0; b < cellName.length; b++) {
            Cell cell2 = row.createCell(b);
            cell2.setCellValue(cellName[b]);
            cell2.setCellStyle(cellStyle0);
        }

        r++;
        int index1 = 0;
        String idsArray[] = ids.split(",");
        for (String idString : idsArray) {
            DeliveryPlan deliveryPlan = findById(DeliveryPlan.class, Long.parseLong(idString));
            List<Map<String, Object>> listQueryProductWareHouse = deliveryPlanDao.QueryProductWareHouse(Long.parseLong(idString));
            String houseName = "";
            String product = "";
            String batchcode = "";
            String faHuoRen = "";

            for (Map<String, Object> mapobject : listQueryProductWareHouse) {
                houseName += mapobject.get("HOUSENAME") == null ? "" : mapobject.get("HOUSENAME").toString() + "\t";
            }

            Map<String, Object> map = new HashMap();
            map.put("deliveryId", deliveryPlan.getId());
            // 出库订单关联信息
            List<DeliveryPlanDetails> dpso = findListByMap(DeliveryPlanDetails.class, map);
            for (DeliveryPlanDetails detail : dpso) {
                if (!batchcode.contains(detail.getBatchCode() + "\t")) {
                    batchcode += detail.getBatchCode() + "\t";
                }
                if (!product.contains(detail.getConsumerProductName() + "\t")) {
                    product += detail.getConsumerProductName() + "\t";
                }
            }

            List<DeliveryPlanSalesOrders> dpos = findListByMap(DeliveryPlanSalesOrders.class, map);
            for (DeliveryPlanSalesOrders os : dpos) {
                if (!faHuoRen.contains(os.getOptUser() + "\t")) {
                    faHuoRen += os.getOptUser() == null ? "" : os.getOptUser() + "\t";
                }
            }

            row = sheet.createRow(r);
            index1++;

            for (int b = 0; b < cellName.length; b++) {
                Cell cell2 = row.createCell(b);
                cell2.setCellStyle(cellStyle0);
                switch (cellName[b]) {
                    case "计划发货号":
                        cell2.setCellValue(index1);
                        break;
                    case "要货单位":
                        cell2.setCellValue(deliveryPlan.getDeliveryTargetCompany());
                        break;
                    case "发货编号":
                        cell2.setCellValue(deliveryPlan.getDeliveryCode());
                        break;
                    case "车辆装货地点":
                        cell2.setCellValue(houseName);
                        break;
                    case "装货产品":
                        cell2.setCellValue(product);
                        break;
                    case "批次号":
                        cell2.setCellValue(batchcode);
                        break;
                    case "发货人":
                        cell2.setCellValue(faHuoRen);
                        break;
                    case "备注":
                        cell2.setCellValue(deliveryPlan.getAttention());
                        break;
                    case "仓管":
                        cell2.setCellValue("");
                        break;
                    case "叉车":
                        cell2.setCellValue("");
                        break;
                    default:
                        break;
                }
            }
            r++;
        }
        return wb;
    }

    @Override
    public void unbindingPDA(String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("deliveryId", Long.parseLong(id));
        DeliveryPlanSalesOrders dpo = deliveryPlanDao.findUniqueByMap(DeliveryPlanSalesOrders.class,param);
        dpo.setOptUser(null);
        dpo.setPdaID(null);
        deliveryPlanDao.save(dpo);
    }

    @Override
    public List<Map<String, Object>> findDeliverySlipMirror(Long id) {
        List<Map<String, Object>> deliverySlip = deliveryPlanDao.findDeliverySlipMirror(id);
        return  deliverySlip;
    }

    @Override
    public List<Map<String, Object>> findDeliverySlip(long id) {
        List<Map<String, Object>> deliverySlip = deliveryPlanDao.findDeliverySlip(id);
        return deliverySlip;
    }
}

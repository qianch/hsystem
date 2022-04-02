/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.delivery.controller;

import com.aspose.cells.Border;
import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanDetails;
import com.bluebirdme.mes.planner.delivery.entity.DeliveryPlanSalesOrders;
import com.bluebirdme.mes.planner.delivery.helper.QRCode;
import com.bluebirdme.mes.planner.delivery.service.IDeliveryPlanService;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IUserService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.service.IFinishedProductService;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.service.IConsumerService;
import com.bluebirdme.mes.stock.entity.ProductOutOrder;
import com.bluebirdme.mes.stock.entity.ProductOutRecord;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.utils.FilterRules;
import com.bluebirdme.mes.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.bluebirdme.mes.planner.delivery.entity.TcDeliveryPlan;

/**
 * 出货计划Controller
 *
 * @author 徐波
 * @Date 2016-11-2 9:30:06
 */
@Controller
@RequestMapping("/planner/deliveryPlan")
@Journal(name = "出货计划")
public class DeliveryPlanController extends BaseController {
    // 出货计划页面
    final String index = "planner/delivery/deliveryPlan";
    final String addOrEdit = "planner/delivery/deliveryPlanAddOrEdit";
    final String checkDetail = "planner/delivery/deliveryPlanReadOnly";
    final String stockUrl = "planner/delivery/stockProduct";
    final String auditDetail = "planner/delivery/deliveryPlanAudit";
    @Resource
    IAuditInstanceService auditInstanceService;
    @Resource
    IDeliveryPlanService deliveryPlanService;
    @Resource
    IConsumerService consumerService;
    @Resource
    IUserService userService;

    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/product/select")
    public ModelAndView productSelect(Long consumerId) throws Exception {
        return new ModelAndView("planner/delivery/productTc", model.addAttribute("consumerId", consumerId));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取成品信息列表信息")
    @RequestMapping("listDelivery")
    public String listDelivery(Filter filter, Page page, String filterRules) throws Exception {
        if (!StringUtils.isEmpty(filterRules)) {
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(filterRules).getAsJsonArray();
            FilterRules rule;
            Gson gson = new Gson();
            for (JsonElement obj : array) {
                rule = gson.fromJson(obj, FilterRules.class);
                filter.set(rule.getField(), "like:" + rule.getValue());
            }
        }
        return GsonTools.toJson(deliveryPlanService.findPageInfoDelivery(filter, page));
    }

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    public String closeDeliveryPlan(Long did) {
        deliveryPlanService.closeDeliveryPlan(did);
        return ajaxSuccess();
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取出货计划列表信息")
    @RequestMapping("list")
    public String getDeliveryPlan(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageInfo = deliveryPlanService.findPageInfo(filter, page);
        return GsonTools.toJson(findPageInfo);
    }

    @Journal(name = "添加出货计划页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(DeliveryPlan deliveryPlan) {
        return new ModelAndView(addOrEdit, model
                .addAttribute("deliveryPlan", deliveryPlan)
                .addAttribute("orderDatas", GsonTools.toJson(deliveryPlan.getOrderDatas()))
                .addAttribute("deliveryBizUserName", "")
                .addAttribute("productDatas", GsonTools.toJson(deliveryPlan.getProductDatas())));
    }

    @ResponseBody
    @Journal(name = "保存出货计划", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(@RequestBody DeliveryPlan deliveryPlan) throws Exception {
        if (deliveryPlan.getDeliveryDate() == null) {
            return ajaxError("出货时间为必填，请填写出货时间");
        }
        if (deliveryPlan.getDeliveryCode() == null) {
            String type = "NX";
            String loginName = session.getAttribute(Constant.CURRENT_USER_LOGINNAME) == null ? "" : session.getAttribute(Constant.CURRENT_USER_LOGINNAME).toString();
            Filter filter = new Filter();
            filter.getFilter().put("u.loginName", loginName);
            Page page = new Page();
            page.setAll(1);
            Map<String, Object> map = userService.findPageInfo(filter, page);
            List<Map<String, Object>> rows = (List<Map<String, Object>>) map.get("rows");
            if (rows.size() > 0) {
                Map<String, Object> user = rows.get(0);
                String name = user.get("NAME").toString();
                if ("国外销售部".equals(name)) {
                    type = "WX";
                }
            }
            String deliveryCode = deliveryPlanService.getSerial(type);
            deliveryPlan.setDeliveryCode(deliveryCode);
        }
        deliveryPlanService.saveDatas(deliveryPlan);
        return GsonTools.toJson(deliveryPlan);
    }

    @ResponseBody
    @Journal(name = "复制出库计划", logType = LogType.DB)
    @RequestMapping(value = "copy", method = RequestMethod.GET)
    public String copy(String id) throws Exception {
        deliveryPlanService.copy(id);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "编辑出货计划页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(DeliveryPlan deliveryPlan) {
        deliveryPlan = deliveryPlanService.findById(DeliveryPlan.class, deliveryPlan.getId());
        User c = deliveryPlanService.findById(User.class, deliveryPlan.getDeliveryBizUserId());

        Map<String, Object> map = new HashMap();
        map.put("deliveryId", deliveryPlan.getId());
        List<Map<String, Object>> products = deliveryPlanService.getDeliveryProducts(deliveryPlan.getId());

        map.clear();
        map.put("deliveryId", deliveryPlan.getId());
        List<DeliveryPlanSalesOrders> orders = deliveryPlanService.findListByMap(DeliveryPlanSalesOrders.class, map);

        return new ModelAndView(addOrEdit, model
                .addAttribute("deliveryBizUserName", c.userName)
                .addAttribute("deliveryPlan", deliveryPlan)
                .addAttribute("orderDatas", GsonTools.toJson(orders))
                .addAttribute("productDatas", GsonTools.toJson(products)));
    }

    @Journal(name = "编辑出货计划页面")
    @RequestMapping(value = "editOrder", method = RequestMethod.GET)
    public ModelAndView _editOrder(DeliveryPlan deliveryPlan) {
        deliveryPlan = deliveryPlanService.findById(DeliveryPlan.class, deliveryPlan.getId());
        User c = deliveryPlanService.findById(User.class, deliveryPlan.getDeliveryBizUserId());

        Map<String, Object> map = new HashMap();
        map.put("deliveryId", deliveryPlan.getId());
        List<Map<String, Object>> products = deliveryPlanService.getDeliveryProducts(deliveryPlan.getId());

        map.clear();
        map.put("deliveryId", deliveryPlan.getId());
        List<DeliveryPlanSalesOrders> orders = deliveryPlanService.findListByMap(DeliveryPlanSalesOrders.class, map);

        return new ModelAndView(checkDetail, model
                .addAttribute("deliveryBizUserName", c.userName)
                .addAttribute("deliveryPlan", deliveryPlan)
                .addAttribute("orderDatas", GsonTools.toJson(orders))
                .addAttribute("productDatas", GsonTools.toJson(products)));
    }

    @Journal(name = "查看货物仓库位置")
    @RequestMapping(value = "searchProduct", method = RequestMethod.GET)
    public ModelAndView searchProduct(DeliveryPlanSalesOrders dpo) {
        dpo = deliveryPlanService.findById(DeliveryPlanSalesOrders.class, dpo.getId());

        HashMap<String, Object> map = new HashMap();
        map.put("packingNumber", dpo.getPackingNumber());
        List<DeliveryPlanDetails> list = deliveryPlanService.findListByMap(DeliveryPlanDetails.class, map);
        DeliveryPlan dp = deliveryPlanService.findById(DeliveryPlan.class, dpo.getDeliveryId());
        // 获取数据仓库数据结果
        List<Map<String, Object>> stock = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            String salesOrderSubCode = list.get(i).getSalesOrderSubCode();
            String batchCode = list.get(i).getBatchCode();
            Long productId = list.get(i).getProductId();
            Long partId = list.get(i).getPartID();
            stock.addAll(deliveryPlanService.searchProduct(salesOrderSubCode, batchCode, productId, partId));
        }
        return new ModelAndView(stockUrl, model.addAttribute("dpo", dpo)
                .addAttribute("dp", dp)
                .addAttribute("stock", GsonTools.toJson(stock)));
    }

    @Journal(name = "PDA解绑")
    @RequestMapping(value = "unbinding")
    @ResponseBody
    public String unbinding(String id) {
        deliveryPlanService.unbindingPDA(id);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "查看出货计划页面审批")
    @RequestMapping(value = "chekDetailaudit", method = RequestMethod.GET)
    public ModelAndView chekDetailaudit(DeliveryPlan deliveryPlan) {
        deliveryPlan = deliveryPlanService.findById(DeliveryPlan.class, deliveryPlan.getId());
        User c = deliveryPlanService.findById(User.class, deliveryPlan.getDeliveryBizUserId());
        Map<String, Object> map = new HashMap();
        map.put("deliveryId", deliveryPlan.getId());
        List<Map<String, Object>> products = deliveryPlanService.getDeliveryProducts(deliveryPlan.getId());

        map.clear();
        map.put("deliveryId", deliveryPlan.getId());
        List<DeliveryPlanSalesOrders> orders = deliveryPlanService.findListByMap(DeliveryPlanSalesOrders.class, map);

        return new ModelAndView(auditDetail, model
                .addAttribute("deliveryBizUserName", c.userName)
                .addAttribute("deliveryPlan", deliveryPlan)
                .addAttribute("orderDatas", GsonTools.toJson(orders))
                .addAttribute("productDatas", GsonTools.toJson(products)));
    }

    @ResponseBody
    @Journal(name = "提交审核", logType = LogType.DB)
    @RequestMapping(value = "commit", method = RequestMethod.POST)
    public String commit(Long id, String name) throws Exception {
        DeliveryPlan deliveryPlan = deliveryPlanService.findById(DeliveryPlan.class, id);
        if (deliveryPlan.getDeliveryCode().startsWith("NX")) {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.CK, Long.parseLong(String.valueOf(session.getAttribute(Constant.CURRENT_USER_ID))), "planner/deliveryPlan/chekDetailaudit?id=" + id, id, DeliveryPlan.class);
        } else {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.CK1, Long.parseLong(String.valueOf(session.getAttribute(Constant.CURRENT_USER_ID))), "planner/deliveryPlan/chekDetailaudit?id=" + id, id, DeliveryPlan.class);
        }
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "删除发货计划", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        deliveryPlanService.deleteAll(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "取消发货审批", logType = LogType.DB)
    @RequestMapping(value = "cannel", method = RequestMethod.POST)
    public String cannel(String ids) {
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            DeliveryPlan dp = deliveryPlanService.findById(DeliveryPlan.class, Long.valueOf(id[a]));
            if (!dp.getDeliveryBizUserId().equals(session.getAttribute(Constant.CURRENT_USER_ID))) {
                return ajaxError("只能由下单人员撤销审核");
            }

            if (dp.getAuditState() != 1) {
                return ajaxError("只能撤销审核中的计划");
            }
        }
        deliveryPlanService.cannel(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "关闭发货计划", logType = LogType.DB)
    @RequestMapping(value = "doClose", method = RequestMethod.POST)
    public String doClose(String id) {
        deliveryPlanService.closeDeliveryPlan(Long.valueOf(id));
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "更新出库计划为未出库", logType = LogType.DB)
    @RequestMapping(value = "releaseDeliveryPlan", method = RequestMethod.POST)
    public String releaseDeliveryPlan(String id) {
        return deliveryPlanService.releaseDeliveryPlan(Long.valueOf(id));
    }

    @ResponseBody
    @Journal(name = "获取出货单号")
    @RequestMapping(value = "serial/{type}", method = {RequestMethod.GET,
            RequestMethod.POST})
    public String serial(@PathVariable("type") String type) {
        return deliveryPlanService.getSerial(type);
    }

    @Journal(name = "查看出货计划明细")
    @RequestMapping(value = "chekDetail", method = RequestMethod.GET)
    public ModelAndView chekDetail(DeliveryPlan deliveryPlan) {
        deliveryPlan = deliveryPlanService.findById(DeliveryPlan.class, deliveryPlan.getId());
        User c = deliveryPlanService.findById(User.class, deliveryPlan.getDeliveryBizUserId());
        Map<String, Object> map = new HashMap();
        map.put("deliveryId", deliveryPlan.getId());
        List<Map<String, Object>> products = deliveryPlanService.getDeliveryProducts(deliveryPlan.getId());

        //下单量
        int orderxdl;
        int orderfhl;
        int orderkcl;
        int detailplanxdl;
        int detailplanfhl;
        int detailplankcl;

        for (int i = 0; i < products.size(); i++) {
            String salesOrderCode = products.get(i).get("SALESORDERSUBCODE".toUpperCase()).toString();
            String batchCode = products.get(i).get("BATCHCODE").toString();
            long salesOrderDetailId = Long.parseLong(products.get(i).get("salesOrderDetailId".toUpperCase()).toString());
            String partName = products.get(i).get("PARTNAME".toUpperCase()) == null ? "" : products.get(i).get("PARTNAME".toUpperCase()).toString();
            //订单下单量
            orderxdl = deliveryPlanService.getOrderXdl(salesOrderDetailId, partName);
            products.get(i).put("orderxdl", orderxdl);
            //查询订单发货量
            orderfhl = deliveryPlanService.getOrderFhl(salesOrderCode);
            products.get(i).put("orderfhl", orderfhl);
            //查询订单库存量
            orderkcl = orderxdl - orderfhl;
            products.get(i).put("orderkcl", orderkcl);
            //计划下单量
            detailplanxdl = deliveryPlanService.getPlanXdl(salesOrderDetailId, batchCode, partName);
            products.get(i).put("detailplanxdl", detailplanxdl);
            //查询计划发货量
            detailplanfhl = deliveryPlanService.getDetailPlanOrderFhl(salesOrderCode, batchCode);
            products.get(i).put("detailplanfhl", detailplanfhl);
            //查询计划库存量
            detailplankcl = detailplanxdl - detailplanfhl;
            products.get(i).put("detailplankcl", detailplankcl);
        }

        map.clear();
        map.put("deliveryId", deliveryPlan.getId());
        List<DeliveryPlanSalesOrders> orders = deliveryPlanService.findListByMap(DeliveryPlanSalesOrders.class, map);

        return new ModelAndView(checkDetail, model
                .addAttribute("deliveryBizUserName", c.userName)
                .addAttribute("deliveryPlan", deliveryPlan)
                .addAttribute("orderDatas", GsonTools.toJson(orders))
                .addAttribute("productDatas", GsonTools.toJson(products)));
    }

    /**
     * 根据出库计划id导出成品发货通知单
     *
     * @param id
     * @throws Exception
     */
    @NoLogin
    @Journal(name = "根据日计划id导出Excel")
    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(Long id) throws Exception {
        // 出货计划
        DeliveryPlan deliveryPlan = deliveryPlanService.findById(DeliveryPlan.class, id);
        HashMap<String, Object> deliveryCode = new HashMap();// 发货单编号
        deliveryCode.put("deliveryCode", deliveryPlan.getDeliveryCode());
        // 成品出库记录
        List<ProductOutRecord> productOutRecords = deliveryPlanService.findListByMap(ProductOutRecord.class, deliveryCode);
        User operater = null;
        if (productOutRecords.size() > 0) {
            // 出货人员
            operater = deliveryPlanService.findById(User.class, productOutRecords.get(0).getOperateUserId());
        }
        // 业务员
        User user = deliveryPlanService.findById(User.class, deliveryPlan.getDeliveryBizUserId());
        // 操作人所对应的部门
        List<Map<String, Object>> auditInstance = deliveryPlanService.searchAuditer("com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan", id);
        Map<String, Object> map = new HashMap();
        map.put("deliveryId", deliveryPlan.getId());
        // 出库订单关联信息
        List<DeliveryPlanSalesOrders> dpso = deliveryPlanService.findListByMap(DeliveryPlanSalesOrders.class, map);
        // 出货详情
        List<DeliveryPlanDetails> dpd = deliveryPlanService.findListByMap(DeliveryPlanDetails.class, map);
        //客户信息
        Consumer consumer = consumerService.findById(Consumer.class, deliveryPlan.getConsumerId());

        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "成品发货通知单(" + deliveryPlan.getDeliveryCode() + ")";
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
        SXSSFSheet sheet = wb.createSheet();
        // sheet.setDisplayGridlines(true);
        // 生成一个字体
        Row row;
        Cell cell;

        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        row.setHeightInPoints(50);
        cell = row.createCell(0);
        sheet.setColumnWidth(0, 13 * 256);// 设置列宽
        cell.setCellStyle(cellStyle);
        cell.setCellValue("浙江恒石纤维基业有限公司成品发货通知单");
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
        // 第二行
        row = sheet.createRow(r);
        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.RIGHT); // 右对齐
        cellStyle1.setBorderBottom(BorderStyle.THIN);
        cellStyle1.setBorderTop(BorderStyle.NONE);
        cellStyle1.setBorderRight(BorderStyle.THIN);
        cellStyle1.setBorderLeft(BorderStyle.THIN);
        cell = row.createCell(0);
        cell.setCellValue("Q/HS RYX0002-2012");
        cell.setCellStyle(cellStyle1);
        for (int i = 1; i < 11; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle1);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 10));
        r++;
        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.LEFT); // 左对齐
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        // 第三行
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("发货单编号：");
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle2);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
        cell = row.createCell(2);
        cell.setCellValue(deliveryPlan.getDeliveryCode());
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(3);
        cell.setCellStyle(cellStyle2);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 3));
        cell = row.createCell(4);
        cell.setCellValue("发货日期：");
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(5);
        cell.setCellValue(sf.format(deliveryPlan.getDeliveryDate()));
        cell.setCellStyle(cellStyle2);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 5, 6));

        cell = row.createCell(7);
        cell.setCellValue("业务员：");
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(8);
        cell.setCellValue(user.getUserName());
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(9);
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(10);
        cell.setCellStyle(cellStyle2);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 8, 10));
        r++;
        // 第四行
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("要货单位：");
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle2);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
        cell = row.createCell(2);
        cell.setCellValue(deliveryPlan.getDeliveryTargetCompany());
        cell.setCellStyle(cellStyle2);
        for (int i = 3; i < 5; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 4));
        cell = row.createCell(5);
        cell.setCellValue("单位简称：");
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(6);
        cell.setCellValue(consumer.getConsumerSimpleName());
        cell.setCellStyle(cellStyle2);
        for (int i = 7; i < 11; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 6, 10));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("客户基地:" + (deliveryPlan.getBasePlace() == null ? "" : deliveryPlan.getBasePlace()));
        cell.setCellStyle(cellStyle2);
        for (int j = 1; j < 6; j++) {
            cell = row.createCell(j);
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 5));
        cell = row.createCell(6);
        cell.setCellValue("样布信息:" + (deliveryPlan.getSampleInformation() == null ? "" : deliveryPlan.getSampleInformation()));
        cell.setCellStyle(cellStyle2);
        for (int i = 7; i < 11; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 6, 10));
        r++;
        // 第五行 箱单数据
        String columnName[] = new String[]{"箱单数据", "编号", "装箱号", "提单号", "箱号", "封号", "件数", "毛重", "尺码"};
        row = sheet.createRow(r);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle0);
            if (a == 0) {
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$" + (r + 1) + ":$A$" + (r + dpso.size() + 1)));
            }
        }
        cell = row.createCell(10);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle0);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 8, 10));
        r++;
        // 遍历数据的长度，得到递增的行数
        int index = 0;
        for (DeliveryPlanSalesOrders data : dpso) {
            index++;
            row = sheet.createRow(r);
            /* sheet.addMergedRegion(new CellRangeAddress(r,0, dpso.size(), 0)); */
            // 当产品型号和原来的产品型号不同时，记录现在的型号并插入一行型号
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        cell.setCellValue("");
                        break;
                    case 1:
                        cell.setCellValue(index);// 编号
                        break;
                    case 2:
                        cell.setCellValue(data.getPn() == null ? "" : data.getPn());// 装箱号
                        break;
                    case 3:
                        cell.setCellValue(data.getLadingCode() == null ? "" : data.getLadingCode());// 提单号
                        break;
                    case 4:
                        cell.setCellValue(data.getBoxNumber() == null ? "" : data.getBoxNumber());// 箱号
                        break;
                    case 5:
                        cell.setCellValue(data.getSerialNumber() == null ? "" : data.getSerialNumber());// 封号
                        break;
                    case 6:
                        cell.setCellValue(data.getCount() == null ? 0d : data.getCount());// 件数
                        break;
                    case 7:
                        cell.setCellValue(data.getWeight() == null ? 0d : data.getWeight());// 毛重
                        break;
                    case 8:
                        cell.setCellValue(data.getSize() == null ? 0d : data.getSize());// 尺码
                        break;
                }
                cell.setCellStyle(cellStyle0);
            }
            cell = row.createCell(9);
            cell.setCellValue("");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellValue("");
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 8, 10));
            r++;
        }

        row = sheet.createRow(r);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("发货内容:");
        cell1.setCellStyle(cellStyle3);
        for (int j = 1; j < 11; j++) {
            cell1 = row.createCell(j);
            cell1.setCellStyle(cellStyle3);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 10));
        r++;
        String cellName[] = new String[]{"编号", "装箱号", "订单号", "批次号", "客户产品名称", "厂内名称", "部件名称", "门幅", "发货数量(托)", "发货数量(套)", "备注"};
        row = sheet.createRow(r);
        for (int b = 0; b < cellName.length; b++) {
            Cell cell2 = row.createCell(b);
            cell2.setCellValue(cellName[b]);
            cell2.setCellStyle(cellStyle0);
        }
        // 发货内容
        r++;
        int index1 = 0;
        int deliveryCount = 0;
        int deliverySuitCount = 0;

        for (DeliveryPlanDetails dpds : dpd) {
            FinishedProduct procuct = deliveryPlanService.findById(FinishedProduct.class, dpds.getProductId());
            row = sheet.createRow(r);
            index1++;
            for (int b = 0; b < cellName.length; b++) {
                Cell cell2 = row.createCell(b);
                cell2.setCellStyle(cellStyle0);
                if ("装箱号".equals(cellName[b])) {
                    if (dpds.getPn() != null) {
                        cell2.setCellValue(dpds.getPn());
                    } else {
                        cell2.setCellValue("");
                    }
                } else if ("订单号".equals(cellName[b])) {
                    if (dpds.getSalesOrderSubCode() != null) {
                        cell2.setCellValue(dpds.getSalesOrderSubCode());
                    } else {
                        cell2.setCellValue("");
                    }
                } else if ("批次号".equals(cellName[b])) {
                    if (dpds.getBatchCode() != null) {
                        cell2.setCellValue(dpds.getBatchCode());
                    } else {
                        cell2.setCellValue("");
                    }
                } else if ("客户产品名称".equals(cellName[b])) {
                    if (dpds.getConsumerProductName() != null) {
                        cell2.setCellValue(dpds.getConsumerProductName());
                    } else {
                        cell2.setCellValue("");
                    }
                } else if ("厂内名称".equals(cellName[b])) {
                    if (dpds.getFactoryProductName() != null) {
                        cell2.setCellValue(dpds.getFactoryProductName());
                    } else {
                        cell2.setCellValue("");
                    }
                } else if ("部件名称".equals(cellName[b])) {
                    if (dpds.getPartName() != null) {
                        cell2.setCellValue(dpds.getPartName());
                    } else {
                        cell2.setCellValue("");
                    }
                } else if ("门幅".equals(cellName[b])) {
                    if (procuct.getProductWidth() != null) {
                        cell2.setCellValue(procuct.getProductWidth());
                    } else {
                        cell2.setCellValue("");
                    }
                } else if ("发货数量(托)".equals(cellName[b])) {
                    if (dpds.getDeliveryCount() != null) {
                        cell2.setCellValue(dpds.getDeliveryCount());
                        deliveryCount += dpds.getDeliveryCount();
                    } else {
                        cell2.setCellValue("");
                    }
                } else if ("发货数量(套)".equals(cellName[b])) {
                    if (dpds.getDeliverySuitCount() != null) {
                        cell2.setCellValue(dpds.getDeliverySuitCount());
                        deliverySuitCount += dpds.getDeliverySuitCount();
                    } else {
                        cell2.setCellValue("");
                    }
                } else if ("备注".equals(cellName[b])) {
                    if (dpds.getMemo() != null) {
                        cell2.setCellValue(dpds.getMemo());
                    } else {
                        cell2.setCellValue("");
                    }
                } else if ("编号".equals(cellName[b])) {
                    cell2.setCellValue(index1);
                }
            }
            r++;
        }

        // 装箱要求 第一行
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("装箱要求");
        cell.setCellStyle(cellStyle0);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$" + (r + 1) + ":$A$" + (r + 2)));
        cell = row.createCell(1);
        cell.setCellValue("包装方式：");
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(2);
        cell.setCellValue(deliveryPlan.getPackagingType());
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(3);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(4);
        cell.setCellStyle(cellStyle0);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 4));
        cell = row.createCell(5);
        cell.setCellValue("物流公司：");
        cell.setCellStyle(cellStyle0);

        cell = row.createCell(6);
        cell.setCellValue(deliveryPlan.getLogisticsCompany());
        cell.setCellStyle(cellStyle0);

        cell = row.createCell(7);
        cell.setCellStyle(cellStyle0);
        cell.setCellValue("小计:");

        cell = row.createCell(8);
        cell.setCellStyle(cellStyle0);
        cell.setCellValue(deliveryCount);

        cell = row.createCell(9);
        cell.setCellStyle(cellStyle0);
        cell.setCellValue(deliverySuitCount);

        cell = row.createCell(10);
        cell.setCellStyle(cellStyle0);
        r++;
        // 装箱要求 第二行
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("装箱要求");
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(1);
        cell.setCellValue("装箱注意事项: ");
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(2);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 1, 2));
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(3);
        cell.setCellValue(deliveryPlan.getAttention());
        cell.setCellStyle(cellStyle0);
        for (int k = 4; k < 11; k++) {
            cell = row.createCell(k);
            cell.setCellStyle(cellStyle0);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 3, 10));
        r++;
        // 最后一行
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("第一审批人:");
        cell.setCellStyle(cellStyle0);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$" + (r + 1) + ":$A$" + (r + 2)));
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle0);
        if (auditInstance.size() > 0) {
            if (auditInstance.get(0).get("FIRSTREALAUDITUSERNAME") != null) {
                cell.setCellValue(auditInstance.get(0).get("FIRSTREALAUDITUSERNAME").toString());// 第一审核人
            } else {
                cell.setCellValue("");
            }
        } else {
            cell.setCellValue("");
        }
        cell = row.createCell(2);
        cell.setCellStyle(cellStyle0);
        sheet.addMergedRegion(new CellRangeAddress(r, (r + 1), 1, 2));
        cell = row.createCell(3);
        cell.setCellValue("第二审批人:");
        cell.setCellStyle(cellStyle0);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$D$" + (r + 1) + ":$D$" + (r + 2)));
        cell = row.createCell(4);
        cell.setCellStyle(cellStyle0);
        if (auditInstance.size() > 0) {
            if (auditInstance.get(0).get("SECONDREALAUDITUSERNAME") != null) {
                cell.setCellValue(auditInstance.get(0).get("SECONDREALAUDITUSERNAME").toString());// 第二审核人
            } else {
                cell.setCellValue("");
            }
        } else {
            cell.setCellValue("");
        }
        cell = row.createCell(5);
        cell.setCellStyle(cellStyle0);
        sheet.addMergedRegion(new CellRangeAddress(r, (r + 1), 4, 5));
        cell = row.createCell(6);
        cell.setCellStyle(cellStyle0);
        cell.setCellValue("发货人员:");
        sheet.addMergedRegion(new CellRangeAddress(r, (r + 1), 6, 6));
        cell = row.createCell(7);
        cell.setCellStyle(cellStyle0);
        if (operater != null) {
            cell.setCellValue(operater.getUserName());
        } else {
            cell.setCellValue("");
        }
        cell = row.createCell(8);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(9);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(10);
        cell.setCellStyle(cellStyle0);
        sheet.addMergedRegion(new CellRangeAddress(r, (r + 1), 7, 10));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(2);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(3);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(4);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(5);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(6);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(7);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(8);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(9);
        cell.setCellStyle(cellStyle0);
        cell = row.createCell(10);
        cell.setCellStyle(cellStyle0);

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        QRCode.encode(deliveryPlan.getDeliveryCode(), byteArrayOut, 250, 250);
        //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        SXSSFDrawing patriarch = sheet.createDrawingPatriarch();
        //anchor主要用于设置图片的属性
        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 250, 250, (short) 9, 0, (short) 10, 1);
        anchor.setAnchorType(ClientAnchor.AnchorType.byId(3));
        //插入图片
        patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));
        HttpUtils.download (response,wb, templateName);
    }

    /**
     * 根据成品出库装厢表id导出Excel（竖排）
     *
     * @param ids
     * @throws Exception
     */
    @NoLogin
    @Journal(name = "根据成品出库装厢表id导出Excel（竖排）")
    @ResponseBody
    @RequestMapping(value = "exportnew", method = RequestMethod.GET)
    public void exportnew(String ids) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "成品发货通知单(竖版)"+sf.format(new Date());
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
        SXSSFSheet sheet;


        String[] idsArray = ids.split(",");
        for (String idString : idsArray){
            ProductOutOrder productOutOrder = deliveryPlanService.findById(ProductOutOrder.class, Long.parseLong(idString));
            DeliveryPlan deliveryPlan = deliveryPlanService.findById(DeliveryPlan.class, productOutOrder.getDeliveryId());
            //出货员
            User operater =deliveryPlanService.findById(User.class,productOutOrder.getOperateUserId());
            // 业务员
            User user = deliveryPlanService.findById(User.class, deliveryPlan.getDeliveryBizUserId());
            List<Map<String, Object>> auditInstance = deliveryPlanService.searchAuditer("com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan", deliveryPlan.getId());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("deliveryId", deliveryPlan.getId());
            // 出库订单关联信息
            List<DeliveryPlanSalesOrders> dpso = deliveryPlanService.findListByMap(DeliveryPlanSalesOrders.class, map);
            // 出货详情
            List<DeliveryPlanDetails> dpd = deliveryPlanService.findListByMap(DeliveryPlanDetails.class, map);
            //客户信息
            Consumer consumer = consumerService.findById(Consumer.class, deliveryPlan.getConsumerId());
            List<Map<String, Object>> serviceDeliverySlip = deliveryPlanService.findDeliverySlipMirror(Long.parseLong(idString));
            if(serviceDeliverySlip.size() == 0){
                serviceDeliverySlip = deliveryPlanService.findDeliverySlip(Long.parseLong(idString));
            }
            sheet = wb.createSheet(deliveryPlan.getDeliveryCode());
            // 生成一个字体
            Row row = null;
            Cell cell = null;

            int r = 0;// 从第1行开始写数据
            row = sheet.createRow(r);
            row.setHeightInPoints(50);
            cell = row.createCell(0);
            sheet.setColumnWidth(0, 13 * 256);// 设置列宽
            cell.setCellStyle(cellStyle);
            cell.setCellValue("浙江恒石纤维基业有限公司成品发货通知单");
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
            cell = row.createCell(11);
            sheet.setColumnWidth(11, 13 * 256);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(12);
            sheet.setColumnWidth(12, 13 * 256);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(13);
            sheet.setColumnWidth(13, 13 * 256);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(14);
            sheet.setColumnWidth(14, 13 * 256);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(15);
            sheet.setColumnWidth(15, 13 * 256);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(16);
            sheet.setColumnWidth(16, 13 * 256);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(17);
            sheet.setColumnWidth(17, 13 * 256);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(18);
            sheet.setColumnWidth(18, 13 * 256);
            cell.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 18));
            r++;
            // 第二行
            row = sheet.createRow(r);
            CellStyle cellStyle1 = wb.createCellStyle();
            cellStyle1.setAlignment(HorizontalAlignment.RIGHT); // 右对齐
            cellStyle1.setBorderBottom(BorderStyle.THIN);
            cellStyle1.setBorderTop(BorderStyle.NONE);
            cellStyle1.setBorderRight(BorderStyle.THIN);
            cellStyle1.setBorderLeft(BorderStyle.THIN);
            cell = row.createCell(0);
            cell.setCellValue("Q/HS RYX0002-2012");
            cell.setCellStyle(cellStyle1);
            for (int i = 1; i < 18; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(cellStyle1);
            }
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 18));
            r++;
            CellStyle cellStyle2 = wb.createCellStyle();
            cellStyle2.setAlignment(HorizontalAlignment.LEFT); // 左对齐
            cellStyle2.setBorderBottom(BorderStyle.THIN);
            cellStyle2.setBorderTop(BorderStyle.THIN);
            cellStyle2.setBorderRight(BorderStyle.THIN);
            cellStyle2.setBorderLeft(BorderStyle.THIN);
            // 第三行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("发货单编号：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
            cell = row.createCell(2);
            cell.setCellValue(deliveryPlan.getDeliveryCode());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 4));
            cell = row.createCell(5);
            cell.setCellValue("发货日期：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellValue(sf.format(deliveryPlan.getDeliveryDate()));
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 6, 8));
            cell = row.createCell(9);
            cell.setCellValue("业务员：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(10);
            cell.setCellValue(user.getUserName());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(11);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(16);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(17);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(18);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 10, 18));
            r++;
            // 第四行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("要货单位：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
            cell = row.createCell(2);
            cell.setCellValue(deliveryPlan.getDeliveryTargetCompany());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 4));
            cell = row.createCell(5);
            cell.setCellValue("单位简称：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellValue(consumer.getConsumerSimpleName());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 6, 8));
            cell = row.createCell(9);
            cell.setCellValue("收货地址：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(10);
            cell.setCellValue(deliveryPlan.getShippingAddress()==null ? "" : deliveryPlan.getShippingAddress());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(11);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(16);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(17);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(18);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 10, 18));
            r++;
            //第五行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("收货人及联系电话：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
            cell = row.createCell(2);
            cell.setCellValue(deliveryPlan.getLinkmanAndPhone()==null ? "" : deliveryPlan.getLinkmanAndPhone());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 4));
            cell = row.createCell(5);
            cell.setCellValue("样布信息：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellValue(deliveryPlan.getSampleInformation() == null ? "" : deliveryPlan.getSampleInformation());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 6, 10));
            cell = row.createCell(11);
            cell.setCellValue("运输方式");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellValue("□物流    □快递    □送货    □自提    □其他");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(16);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(17);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(18);
            cell.setCellStyle(cellStyle0);
            r++;
            // 第五行 箱单数据
            String columnName[] = new String[]{"箱单数据", "编号", "装箱号", "提单号", "箱号", "封号", "件数", "毛重", "尺码"};
            row = sheet.createRow(r);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellValue(columnName[a]);
                cell.setCellStyle(cellStyle0);
                if (a == 0) {
                    sheet.addMergedRegion(CellRangeAddress.valueOf("$A$" + (r + 1)
                            + ":$A$" + (r + dpso.size() + 1)));
                }
            }
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 8, 10));
            cell = row.createCell(11);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(16);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(17);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(18);
            cell.setCellStyle(cellStyle0);
            r++;
            // 遍历数据的长度，得到递增的行数
            int index = 0;
            for (DeliveryPlanSalesOrders data : dpso) {
                index++;
                row = sheet.createRow(r);
                /* sheet.addMergedRegion(new CellRangeAddress(r,0, dpso.size(), 0)); */
                // 当产品型号和原来的产品型号不同时，记录现在的型号并插入一行型号
                for (int j = 0; j < columnName.length; j++) {
                    cell = row.createCell(j);
                    switch (j) {
                        case 0:
                            cell.setCellValue("");
                            break;
                        case 1:
                            cell.setCellValue(index);// 编号
                            break;
                        case 2:
                            cell.setCellValue(data.getPn() == null ? "" : data.getPn());// 装箱号
                            break;
                        case 3:
                            cell.setCellValue(data.getLadingCode() == null ? "" : data.getLadingCode());// 提单号
                            break;
                        case 4:
                            cell.setCellValue(data.getBoxNumber() == null ? "" : data.getBoxNumber());// 箱号
                            break;
                        case 5:
                            cell.setCellValue(data.getSerialNumber() == null ? "" : data.getSerialNumber());// 封号
                            break;
                        case 6:
                            cell.setCellValue(data.getCount() == null ? 0d : data.getCount());// 件数
                            break;
                        case 7:
                            cell.setCellValue(data.getWeight() == null ? 0d : data.getWeight());// 毛重
                            break;
                        case 8:
                            cell.setCellValue(data.getSize() == null ? 0d : data.getSize());// 尺码
                            break;
                    }
                    cell.setCellStyle(cellStyle0);
                }
                cell = row.createCell(9);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(10);
                cell.setCellStyle(cellStyle0);
                sheet.addMergedRegion(new CellRangeAddress(r, r, 8, 10));
                cell = row.createCell(11);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(12);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(13);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(14);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(15);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(16);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(17);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(18);
                cell.setCellStyle(cellStyle0);
                r++;
            }
            sheet.addMergedRegion(new CellRangeAddress(4, r-1, 11, 11));
            sheet.addMergedRegion(new CellRangeAddress(4, r-1, 12, 18));



            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("发货内容:");
            cell.setCellStyle(cellStyle3);
            for (int j = 1; j < 18; j++) {
                cell = row.createCell(j);
                cell.setCellStyle(cellStyle3);
            }
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 18));
            r++;
            String cellName[] = new String[]{"编号", "装箱号", "订单号", "批次号","Po No", "关税编号",
                    "物料规格型号","客户产品名称", "厂内名称", "部件名称", "门幅", "米长（m）","发货数量(托)", "发货数量(套)", "发货数量（kg）","收货数量(托)",
                    "收货数量(套)","收货数量(kg)","备注"};
            row = sheet.createRow(r);
            for (int b = 0; b < cellName.length; b++) {
                cell = row.createCell(b);
                cell.setCellValue(cellName[b]);
                cell.setCellStyle(cellStyle0);
            }
            // 发货内容
            r++;
            int index1 = 0;
            int tcounts = 0;//总套数
            int traycounts = 0;//总托数
            Double dweights=0.00;

            for (Map<String,Object> mapobject:serviceDeliverySlip) {
                int tcount = 0;
                int traycount=0;
                Double dweight=0.00;
                if(mapobject.get("BARCODES") == null)
                {
                    continue;
                }
                String barCodes[] = mapobject.get("BARCODES").toString().split(",");
                Map<String,Object> map2=new HashMap<String,Object>();

                for (int j = 0; j < barCodes.length; j++) {
                    map2.clear();
                    map2.put("barcode", barCodes[j]);
                    TrayBarCode tb = deliveryPlanService.findUniqueByMap(TrayBarCode.class, map2);
                    if(tb!=null) {
                        ProductOutRecord productOutRecord= deliveryPlanService.findUniqueByMap(ProductOutRecord.class, map2);
                        dweight += productOutRecord.getWeight();
                        dweights += productOutRecord.getWeight();
                        //判断是否是套材
                        if (tb.getPartId() != null && !"".equals(tb.getPartId())) {
                            //判断是成品胚布或常规部件
                            TcBomVersionParts part = deliveryPlanService.findById(TcBomVersionParts.class, tb.getPartId());
                            if ("成品胚布".equals(part.getTcProcBomVersionPartsType())) {//成品胚布
                                tcount += 1;//成品胚布一托为一套
                                tcounts += 1;
                            } else {//常规部件
                                map.clear();
                                map.put("trayBarcode", barCodes[j]);
                                List<TrayBoxRoll> trays = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                for (TrayBoxRoll tray : trays) {
                                    if (tray.getBoxBarcode() != null && !"".equals(tray.getBoxBarcode())) {
                                        map.clear();
                                        map.put("boxBarcode", tray.getBoxBarcode());
                                        List<TrayBoxRoll> boxs = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                        if (boxs.size() > 0) {
                                            tcount += boxs.size();//套数为部件条码数量
                                            tcounts += boxs.size();
                                        }
                                    } else {
                                        if (trays.size() > 0) {
                                            tcount += trays.size();//套数为部件条码数量
                                            tcounts += trays.size();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        else//非套材增加 托数累加
                        {
                            traycount++;
                            traycounts++;
                        }

                    }
                    else
                    {
                        map2.clear();
                        map2.put("barcode", barCodes[j]);
                        PartBarcode partBarcode = deliveryPlanService.findUniqueByMap(PartBarcode.class, map2);
                        if(partBarcode!=null)
                        {
                            tcount += 1;//pcj一个部件为一套
                            tcounts += 1;
                            ProductOutRecord productOutRecord= deliveryPlanService.findUniqueByMap(ProductOutRecord.class, map2);
                            dweight += productOutRecord.getWeight();
                            dweights += productOutRecord.getWeight();
                        }
                    }

                }
                row = sheet.createRow(r);
                index1++;
                for (int b = 0; b < cellName.length; b++) {
                    cell = row.createCell(b);
                    cell.setCellStyle(cellStyle0);
                    switch (b){
                        case 0:
                            cell.setCellValue(index1);//编号
                            break;
                        case 1:
                            cell.setCellValue(mapobject.get("PN")==null ? "" : mapobject.get("PN").toString());//装箱号
                            break;
                        case 2:
                            cell.setCellValue(mapobject.get("SALESORDERSUBCODE")==null ? "" : mapobject.get("SALESORDERSUBCODE").toString());//订单号
                            break;
                        case 3:
                            cell.setCellValue(mapobject.get("BATCHCODE")==null ? "" : mapobject.get("BATCHCODE").toString());//批次号
                            break;
                        case 4:
                            cell.setCellValue("");//Po No
                            break;
                        case 5:
                            cell.setCellValue("");//关税编号
                            break;
                        case 6:
                            cell.setCellValue(mapobject.get("PRODUCTMODEL")==null ? "" :mapobject.get("PRODUCTMODEL").toString());//物料规格型号
                            break;
                        case 7://consumerProductName
                            cell.setCellValue(mapobject.get("CONSUMERPRODUCTNAME")==null ? "" :mapobject.get("CONSUMERPRODUCTNAME").toString());//客户产品名称
                            break;
                        case 8:
                            cell.setCellValue(mapobject.get("FACTORYPRODUCTNAME")==null ? "" :mapobject.get("FACTORYPRODUCTNAME").toString());//厂内名称
                            break;
                        case 9:
                            cell.setCellValue(mapobject.get("PARTNAME") == null ? "" :mapobject.get("PARTNAME").toString());//部件名称
                            break;
                        case 10:
                            cell.setCellValue(mapobject.get("PRODUCTWIDTH") == null ? "" :mapobject.get("PRODUCTWIDTH").toString());//门幅
                            break;
                        case 11:
                            cell.setCellValue(mapobject.get("PRODUCTROLLLENGTH") == null ? "" :mapobject.get("PRODUCTROLLLENGTH").toString());//米长（m）
                            break;
                        case 12:
                            cell.setCellValue(traycount);//发货数量(托)
                            break;
                        case 13:
                            cell.setCellValue(tcount);//发货数量(套)
                            break;
                        case 14:
                            cell.setCellValue(dweight);//发货数量（kg）
                            break;
                        case 15:
                            cell.setCellValue("");//收货数量(托)
                            break;
                        case 16:
                            cell.setCellValue("");//收货数量(套）
                            break;
                        case 17:
                            cell.setCellValue("");//收货数量(kg)
                            break;
                        case 18:
                            cell.setCellValue(mapobject.get("MEMO") == null ? "" :mapobject.get("MEMO").toString());//备注
                            break;
                    }
                }
                r++;
            }

            // 装箱要求 第一行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("装箱要求");
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$" + (r + 1) + ":$A$" + (r + 2)));
            cell = row.createCell(1);
            cell.setCellValue("包装方式：");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(2);
            cell.setCellValue(deliveryPlan.getPackagingType()==null ? "" : deliveryPlan.getPackagingType());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 4));
            cell = row.createCell(5);
            cell.setCellValue("物流公司：");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(6);
            cell.setCellValue(deliveryPlan.getLogisticsCompany()==null ? "" : deliveryPlan.getLogisticsCompany());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 6, 10));

            cell = row.createCell(11);
            cell.setCellStyle(cellStyle0);
            cell.setCellValue("小计:");

            cell = row.createCell(12);
            cell.setCellStyle(cellStyle0);
            cell.setCellValue(traycounts);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle0);
            cell.setCellValue(tcounts);
            cell = row.createCell(14);
            cell.setCellValue(dweights);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(16);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(17);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(18);
            cell.setCellStyle(cellStyle0);
            r++;
            // 装箱要求 第二行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("装箱要求");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(1);
            cell.setCellValue("装箱注意事项: ");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 1, 2));
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellValue(deliveryPlan.getAttention()==null ? "" : deliveryPlan.getAttention());
            cell.setCellStyle(cellStyle0);
            for (int k = 4; k < 11; k++) {
                cell = row.createCell(k);
                cell.setCellStyle(cellStyle0);
            }
            sheet.addMergedRegion(new CellRangeAddress(r, r, 3, 10));
            cell = row.createCell(11);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(16);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(17);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(18);
            cell.setCellStyle(cellStyle0);
            r++;
            // 最后一行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("第一审批人:");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle0);
            if (auditInstance.size() > 0) {
                if (auditInstance.get(0).get("FIRSTREALAUDITUSERNAME") != null) {
                    cell.setCellValue(auditInstance.get(0).get("FIRSTREALAUDITUSERNAME").toString());// 第一审核人
                } else {
                    cell.setCellValue("");
                }
            } else {
                cell.setCellValue("");
            }
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 1, 2));
            cell = row.createCell(3);
            cell.setCellValue("第二审批人:");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle0);
            if (auditInstance.size() > 0) {
                if (auditInstance.get(0).get("SECONDREALAUDITUSERNAME") != null) {
                    cell.setCellValue(auditInstance.get(0).get("SECONDREALAUDITUSERNAME").toString());// 第二审核人
                } else {
                    cell.setCellValue("");
                }
            } else {
                cell.setCellValue("");
            }
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 4, 5));
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle0);
            cell.setCellValue("发货人员:");
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle0);
            if (operater != null) {
                cell.setCellValue(operater.getUserName());
            } else {
                cell.setCellValue("");
            }
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 7, 10));
            cell = row.createCell(11);
            cell.setCellValue("收货人");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellValue("");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(16);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(17);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(18);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 12, 18));
            r++;
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("司机签字：");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 1, 2));
            cell = row.createCell(3);
            cell.setCellValue("客户备注信息：");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellValue(deliveryPlan.getCustomerNotes()==null ? "" : deliveryPlan.getCustomerNotes());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 4, 5));
            cell = row.createCell(6);
            cell.setCellValue("发货时间/发货车牌号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            System.out.println(dpso.get(0).getPlate());
            if(dpso.get(0).getPlate()==null || dpso.get(0).getPlate().equals("")){
                cell.setCellValue("发货时间："+sf.format(deliveryPlan.getDeliveryDate()));
            }else{
                cell.setCellValue("发货时间："+sf.format(deliveryPlan.getDeliveryDate())+"/"+"发货车牌号："+(dpso.get(0).getPlate()==null ? "" : dpso.get(0).getPlate()));
            }
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 7, 10));
            cell = row.createCell(11);
            cell.setCellValue("收货时间");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(16);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(17);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(18);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 12, 18));

            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            QRCode.encode(deliveryPlan.getDeliveryCode(), byteArrayOut, 250, 250);
            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            SXSSFDrawing patriarch = sheet.createDrawingPatriarch();
            //anchor主要用于设置图片的属性
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 250, 250, (short) 16, 0, (short) 17, 1);
            anchor.setAnchorType(ClientAnchor.AnchorType.byId(3));
            //插入图片
            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));

        }
        HttpUtils.download(response,wb,templateName);
}


    /**
     * 根据出库计划id导出成品发货通知单（竖排）
     *
     * @param ids
     * @throws Exception
.     */
    @NoLogin
    @Journal(name = "根据成品出库装厢表id导出Excel（横排1）")
    @ResponseBody
    @RequestMapping(value = "exportnew1", method = RequestMethod.GET)
    public void exportnew1(String ids) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "成品发货通知单(横版)"+sf.format(new Date());
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

        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.LEFT); // 左对齐
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        cellStyle2.setWrapText(true);// 单元格自动换行


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
        SXSSFSheet sheet;

        String[] idsArray = ids.split(",");
        for (String idString : idsArray){
            ProductOutOrder productOutOrder = deliveryPlanService.findById(ProductOutOrder.class, Long.parseLong(idString));
            DeliveryPlan deliveryPlan = deliveryPlanService.findById(DeliveryPlan.class, productOutOrder.getDeliveryId());
            //出货员
            User operater =deliveryPlanService.findById(User.class,productOutOrder.getOperateUserId());
            // 业务员
            User user = deliveryPlanService.findById(User.class, deliveryPlan.getDeliveryBizUserId());
            List<Map<String, Object>> auditInstance = deliveryPlanService.searchAuditer("com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan", deliveryPlan.getId());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("deliveryId", deliveryPlan.getId());
            // 出库订单关联信息
            List<DeliveryPlanSalesOrders> dpso = deliveryPlanService.findListByMap(DeliveryPlanSalesOrders.class, map);
            //客户信息
            Consumer consumer = consumerService.findById(Consumer.class, deliveryPlan.getConsumerId());
            List<Map<String, Object>> serviceDeliverySlip = deliveryPlanService.findDeliverySlipMirror(Long.parseLong(idString));
            if(serviceDeliverySlip.size() == 0){
                serviceDeliverySlip = deliveryPlanService.findDeliverySlip(Long.parseLong(idString));
            }
            sheet = wb.createSheet(deliveryPlan.getDeliveryCode()+idString);
            Row row = null;
            Cell cell = null;

            int outSize=0;
            if(serviceDeliverySlip.size()>18){
                outSize +=serviceDeliverySlip.size()-18;
            }
            //第一行
            int r = 0;// 从第1行开始写数据
            row = sheet.createRow(r);
            row.setHeightInPoints(50);
            for(int i=0;i<20+outSize;i++){
                if (i==0){
                    cell = row.createCell(i);
                    sheet.setColumnWidth(0, 13 * 256);// 设置列宽
                    cell.setCellValue("浙江恒石纤维基业有限公司成品发货通知单");
                    cell.setCellStyle(cellStyle);
                }else{
                    cell = row.createCell(i);
                    sheet.setColumnWidth(0, 13 * 256);// 设置列宽
                    cell.setCellStyle(cellStyle);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(r,r, 0, 19+outSize));
            r++;
            //第二行
            row = sheet.createRow(r);
            for(int i=0;i<20+outSize;i++){
                if (i==0){
                    cell = row.createCell(i);
                    cell.setCellValue("Q/HS RYX0002-2012");
                    cell.setCellStyle(cellStyle2);
                }else{
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle2);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(r,r, 0, 19+outSize));
            r++;
            //第三行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("发货单编号：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 0, 1));
            cell = row.createCell(2);
            cell.setCellValue(deliveryPlan.getDeliveryCode());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 2, 4));
            cell = row.createCell(5);
            cell.setCellValue("发货日期：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellValue(sf.format( deliveryPlan.getDeliveryDate()));
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 6, 8));
            cell = row.createCell(9);
            cell.setCellValue("业务员：");
            cell.setCellStyle(cellStyle2);
            for (int i=10;i<20+outSize;i++){
                if (i==10){
                    cell = row.createCell(i);
                    cell.setCellValue(user.getUserName());
                    cell.setCellStyle(cellStyle2);
                }else{
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle2);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(r,r, 10, 19+outSize));
            r++;
            //第四行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("购货单位：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 0, 1));
            cell = row.createCell(2);
            cell.setCellValue(deliveryPlan.getDeliveryTargetCompany());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 2, 4));
            cell = row.createCell(5);
            cell.setCellValue("单位简称：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellValue(consumer.getConsumerSimpleName());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 6, 8));
            cell = row.createCell(9);
            cell.setCellValue("收货地址:");
            cell.setCellStyle(cellStyle2);
            for (int i=10;i<20+outSize;i++){
                if (i==10){
                    cell = row.createCell(i);
                    cell.setCellValue(deliveryPlan.getShippingAddress()==null ? "" : deliveryPlan.getShippingAddress());
                    cell.setCellStyle(cellStyle2);
                }else{
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle2);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(r,r, 10, 19+outSize));
            r++;
            //第五行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("收货人及联系电话：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            cell.setCellValue(deliveryPlan.getLinkmanAndPhone()==null ? "" : deliveryPlan.getLinkmanAndPhone());
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 0, 1));
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 2, 4));
            cell = row.createCell(5);
            cell.setCellValue("样布信息:");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellValue(deliveryPlan.getSampleInformation()==null ? "" : deliveryPlan.getSampleInformation());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 6, 10));
            cell = row.createCell(11);
            cell.setCellValue("运输方式");
            cell.setCellStyle(cellStyle0);
            for (int i=12;i<20+outSize;i++){
                if (i==12){
                    cell = row.createCell(i);
                    cell.setCellValue("□物流    □快递    □送货    □自提    □其他");
                    cell.setCellStyle(cellStyle0);
                }else{
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle0);
                }
            }
            r++;
            //第六行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("箱单数据");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(1);
            cell.setCellValue("编号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(2);
            cell.setCellValue("装箱号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellValue("提单号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellValue("箱号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(5);
            cell.setCellValue("封号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(6);
            cell.setCellValue("件数");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            cell.setCellValue("毛重");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellValue("尺码");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r,8,10));
            for (int i=11;i<20+outSize;i++){
                cell = row.createCell(i);
                cell.setCellStyle(cellStyle0);
            }
            r++;
            //第七行
            int index = 0;
            for (DeliveryPlanSalesOrders data : dpso) {
                index++;
                row = sheet.createRow(r);
                for (int j = 0; j < 9; j++) {
                    cell = row.createCell(j);
                    switch (j) {
                        case 0:
                            cell.setCellValue("");
                            break;
                        case 1:
                            cell.setCellValue(index);// 编号
                            break;
                        case 2:
                            cell.setCellValue(data.getPn() == null ? "" : data.getPn());// 装箱号
                            break;
                        case 3:
                            cell.setCellValue(data.getLadingCode() == null ? "" : data.getLadingCode());// 提单号
                            break;
                        case 4:
                            cell.setCellValue(data.getBoxNumber() == null ? "" : data.getBoxNumber());// 箱号
                            break;
                        case 5:
                            cell.setCellValue(data.getSerialNumber() == null ? "" : data.getSerialNumber());// 封号
                            break;
                        case 6:
                            cell.setCellValue(data.getCount() == null ? 0d : data.getCount());// 件数
                            break;
                        case 7:
                            cell.setCellValue(data.getWeight() == null ? 0d : data.getWeight());// 毛重
                            break;
                        case 8:
                            cell.setCellValue(data.getSize() == null ? 0d : data.getSize());// 尺码
                            break;
                    }
                    cell.setCellStyle(cellStyle0);
                }
                cell = row.createCell(9);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(10);
                cell.setCellStyle(cellStyle0);
                sheet.addMergedRegion(new CellRangeAddress(r, r, 8, 10));
                for (int i=11;i<20+outSize;i++){
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle0);
                }
                r++;
            }
            sheet.addMergedRegion(new CellRangeAddress(5, r-1, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(4, r-1, 11, 11));
            sheet.addMergedRegion(new CellRangeAddress(4, r-1, 12, 19+outSize));
            //发货内容行
            row = sheet.createRow(r);
            for(int i=0;i<20+outSize;i++){
                if (i==0){
                    cell = row.createCell(i);
                    cell.setCellValue("发货内容:");
                    cell.setCellStyle(cellStyle3);
                }else{
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle3);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(r,r, 0, 19+outSize));
            r++;
            //遍历数据行
            String rowName[] = new String[]{"编号", "装箱号", "订单号", "批次号","Po No", "关税编号",
                    "物料规格型号","客户产品名称", "厂内名称", "部件名称", "门幅", "米长（m）","发货数量(托)", "发货数量(套)", "发货数量（kg）","收货数量(托)",
                    "收货数量(套)","收货数量(kg)","备注"};
            //取值的key
            String rowKey[] = new String[]{"No","PN","SALESORDERSUBCODE","BATCHCODE","4","5",
                    "PRODUCTMODEL","CONSUMERPRODUCTNAME","FACTORYPRODUCTNAME","PARTNAME","PRODUCTWIDTH","PRODUCTROLLLENGTH","12","13","14","15",
                    "16","17","MEMO"
            };
            for (int k=0;k<rowName.length;k++){
                if (k==0){//编号
                    int No=1;
                    row = sheet.createRow(r);
                    for(int i=0;i<20+outSize;i++){
                        if (i==0){
                            cell = row.createCell(i);
                            cell.setCellValue(rowName[k]);
                            cell.setCellStyle(cellStyle0);
                        }else if (i>0&&i<19+outSize){
                            cell = row.createCell(i);
                            cell.setCellValue(No);
                            cell.setCellStyle(cellStyle0);
                            No++;
                        }else{//小计列
                            cell = row.createCell(i);
                            cell.setCellValue("小计");
                            cell.setCellStyle(cellStyle0);
                        }
                    }
                    r++;
                }else if (rowName[k].equals("发货数量(托)")){
                    row = sheet.createRow(r);
                    int b=0;//取dpd集合中的第b位值
                    int tcounts = 0;//总套数
                    int traycounts = 0;//总托数
                    Double dweights=0.00;
                    for(int i=0;i<20+outSize;i++){
                        if (i==0){
                            cell = row.createCell(i);
                            cell.setCellValue(rowName[k]);
                            cell.setCellStyle(cellStyle0);
                        }else if (i>0&&i<19+outSize){
                            if (b>serviceDeliverySlip.size()-1){//超出大dpd集合索引
                                cell = row.createCell(i);
                                cell.setCellValue("");
                                cell.setCellStyle(cellStyle0);
                            }else if(serviceDeliverySlip.get(b).get("BARCODES") != null){
                                String barCodes[] = serviceDeliverySlip.get(b).get("BARCODES").toString().split(",");
                                Map<String,Object> map2=new HashMap<String,Object>();
                                int tcount = 0;
                                int traycount=0;
                                Double dweight=0.00;
                                for (int j = 0; j < barCodes.length; j++) {
                                    map2.clear();
                                    map2.put("barcode", barCodes[j]);
                                    TrayBarCode tb = deliveryPlanService.findUniqueByMap(TrayBarCode.class, map2);
                                    if(tb!=null) {
                                        ProductOutRecord productOutRecord= deliveryPlanService.findUniqueByMap(ProductOutRecord.class, map2);
                                        dweight += productOutRecord.getWeight();
                                        dweights += productOutRecord.getWeight();
                                        //判断是否是套材
                                        if (tb.getPartId() != null && !"".equals(tb.getPartId())) {
                                            //判断是成品胚布或常规部件
                                            TcBomVersionParts part = deliveryPlanService.findById(TcBomVersionParts.class, tb.getPartId());
                                            if ("成品胚布".equals(part.getTcProcBomVersionPartsType())) {//成品胚布
                                                tcount += 1;//成品胚布一托为一套
                                                tcounts += 1;
                                            } else {//常规部件
                                                map.clear();
                                                map.put("trayBarcode", barCodes[j]);
                                                List<TrayBoxRoll> trays = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                                for (TrayBoxRoll tray : trays) {
                                                    if (tray.getBoxBarcode() != null && !"".equals(tray.getBoxBarcode())) {
                                                        map.clear();
                                                        map.put("boxBarcode", tray.getBoxBarcode());
                                                        List<TrayBoxRoll> boxs = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                                        if (boxs.size() > 0) {
                                                            tcount += boxs.size();//套数为部件条码数量
                                                            tcounts += boxs.size();
                                                        }
                                                    } else {
                                                        if (trays.size() > 0) {
                                                            tcount += trays.size();//套数为部件条码数量
                                                            tcounts += trays.size();
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else//非套材增加 托数累加
                                        {
                                            traycount++;
                                            traycounts++;
                                        }

                                    }
                                    else
                                    {
                                        map2.clear();
                                        map2.put("barcode", barCodes[j]);
                                        PartBarcode partBarcode = deliveryPlanService.findUniqueByMap(PartBarcode.class, map2);
                                        if(partBarcode!=null)
                                        {
                                            tcount += 1;//pcj一个部件为一套
                                            tcounts += 1;
                                            ProductOutRecord productOutRecord= deliveryPlanService.findUniqueByMap(ProductOutRecord.class, map2);
                                            dweight += productOutRecord.getWeight();
                                            dweights += productOutRecord.getWeight();
                                        }
                                    }

                                }
                                cell = row.createCell(i);
                                cell.setCellValue(traycount);
                                cell.setCellStyle(cellStyle0);
                                b++;
                            }else{
                                cell = row.createCell(i);
                                cell.setCellValue("");
                                cell.setCellStyle(cellStyle0);
                                b++;
                            }
                        }else {//小计列
                            cell = row.createCell(i);
                            cell.setCellValue(traycounts);
                            cell.setCellStyle(cellStyle0);
                        }
                    }
                    r++;

                }
                else if (rowName[k].equals("发货数量(套)")){
                    row = sheet.createRow(r);
                    int tcounts = 0;//总套数
                    int traycounts = 0;//总托数
                    Double dweights=0.00;
                    int b=0;//取dpd集合中的第b位值
                    for(int i=0;i<20+outSize;i++){
                        if (i==0){
                            cell = row.createCell(i);
                            cell.setCellValue(rowName[k]);
                            cell.setCellStyle(cellStyle0);
                        }else if (i>0&&i<19+outSize){
                            if (b>serviceDeliverySlip.size()-1){//超出大dpd集合索引
                                cell = row.createCell(i);
                                cell.setCellValue("");
                                cell.setCellStyle(cellStyle0);
                            }else if(serviceDeliverySlip.get(b).get("BARCODES") != null){
                                String barCodes[] = serviceDeliverySlip.get(b).get("BARCODES").toString().split(",");
                                Map<String,Object> map2=new HashMap<String,Object>();
                                int tcount = 0;
                                int traycount=0;
                                Double dweight=0.00;
                                for (int j = 0; j < barCodes.length; j++) {
                                    map2.clear();
                                    map2.put("barcode", barCodes[j]);
                                    TrayBarCode tb = deliveryPlanService.findUniqueByMap(TrayBarCode.class, map2);
                                    if(tb!=null) {
                                        ProductOutRecord productOutRecord= deliveryPlanService.findUniqueByMap(ProductOutRecord.class, map2);
                                        dweight += productOutRecord.getWeight();
                                        dweights += productOutRecord.getWeight();
                                        //判断是否是套材
                                        if (tb.getPartId() != null && !"".equals(tb.getPartId())) {
                                            //判断是成品胚布或常规部件
                                            TcBomVersionParts part = deliveryPlanService.findById(TcBomVersionParts.class, tb.getPartId());
                                            if ("成品胚布".equals(part.getTcProcBomVersionPartsType())) {//成品胚布
                                                tcount += 1;//成品胚布一托为一套
                                                tcounts += 1;
                                            } else {//常规部件
                                                map.clear();
                                                map.put("trayBarcode", barCodes[j]);
                                                List<TrayBoxRoll> trays = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                                for (TrayBoxRoll tray : trays) {
                                                    if (tray.getBoxBarcode() != null && !"".equals(tray.getBoxBarcode())) {
                                                        map.clear();
                                                        map.put("boxBarcode", tray.getBoxBarcode());
                                                        List<TrayBoxRoll> boxs = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                                        if (boxs.size() > 0) {
                                                            tcount += boxs.size();//套数为部件条码数量
                                                            tcounts += boxs.size();
                                                        }
                                                    } else {
                                                        if (trays.size() > 0) {
                                                            tcount += trays.size();//套数为部件条码数量
                                                            tcounts += trays.size();
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else//非套材增加 托数累加
                                        {
                                            traycount++;
                                            traycounts++;
                                        }

                                    }
                                    else
                                    {
                                        map2.clear();
                                        map2.put("barcode", barCodes[j]);
                                        PartBarcode partBarcode = deliveryPlanService.findUniqueByMap(PartBarcode.class, map2);
                                        if(partBarcode!=null)
                                        {
                                            tcount += 1;//pcj一个部件为一套
                                            tcounts += 1;
                                            ProductOutRecord productOutRecord= deliveryPlanService.findUniqueByMap(ProductOutRecord.class, map2);
                                            dweight += productOutRecord.getWeight();
                                            dweights += productOutRecord.getWeight();
                                        }
                                    }

                                }
                                cell = row.createCell(i);
                                cell.setCellValue(tcount);
                                cell.setCellStyle(cellStyle0);
                                b++;
                            }else{
                                cell = row.createCell(i);
                                cell.setCellValue("");
                                cell.setCellStyle(cellStyle0);
                                b++;
                            }
                        }else {//小计列
                            cell = row.createCell(i);
                            cell.setCellValue(tcounts);
                            cell.setCellStyle(cellStyle0);
                        }
                    }
                    r++;
                }
                else if (rowName[k].equals("发货数量（kg）")){
                    row = sheet.createRow(r);
                    int tcounts = 0;//总套数
                    int traycounts = 0;//总托数
                    Double dweights=0.00;
                    int b=0;//取dpd集合中的第b位值
                    for(int i=0;i<20+outSize;i++){
                        if (i==0){
                            cell = row.createCell(i);
                            cell.setCellValue(rowName[k]);
                            cell.setCellStyle(cellStyle0);
                        }else if (i>0&&i<19+outSize){
                            if (b>serviceDeliverySlip.size()-1){//超出大dpd集合索引
                                cell = row.createCell(i);
                                cell.setCellValue("");
                                cell.setCellStyle(cellStyle0);
                            }else if(serviceDeliverySlip.get(b).get("BARCODES") != null){
                                String barCodes[] = serviceDeliverySlip.get(b).get("BARCODES").toString().split(",");
                                Map<String,Object> map2=new HashMap<String,Object>();
                                int tcount = 0;
                                int traycount=0;
                                Double dweight=0.00;
                                for (int j = 0; j < barCodes.length; j++) {
                                    map2.clear();
                                    map2.put("barcode", barCodes[j]);
                                    TrayBarCode tb = deliveryPlanService.findUniqueByMap(TrayBarCode.class, map2);
                                    if(tb!=null) {
                                        ProductOutRecord productOutRecord= deliveryPlanService.findUniqueByMap(ProductOutRecord.class, map2);
                                        dweight += productOutRecord.getWeight();
                                        dweights += productOutRecord.getWeight();
                                        //判断是否是套材
                                        if (tb.getPartId() != null && !"".equals(tb.getPartId())) {
                                            //判断是成品胚布或常规部件
                                            TcBomVersionParts part = deliveryPlanService.findById(TcBomVersionParts.class, tb.getPartId());
                                            if ("成品胚布".equals(part.getTcProcBomVersionPartsType())) {//成品胚布
                                                tcount += 1;//成品胚布一托为一套
                                                tcounts += 1;
                                            } else {//常规部件
                                                map.clear();
                                                map.put("trayBarcode", barCodes[j]);
                                                List<TrayBoxRoll> trays = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                                for (TrayBoxRoll tray : trays) {
                                                    if (tray.getBoxBarcode() != null && !"".equals(tray.getBoxBarcode())) {
                                                        map.clear();
                                                        map.put("boxBarcode", tray.getBoxBarcode());
                                                        List<TrayBoxRoll> boxs = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                                        if (boxs.size() > 0) {
                                                            tcount += boxs.size();//套数为部件条码数量
                                                            tcounts += boxs.size();
                                                        }
                                                    } else {
                                                        if (trays.size() > 0) {
                                                            tcount += trays.size();//套数为部件条码数量
                                                            tcounts += trays.size();
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else//非套材增加 托数累加
                                        {
                                            traycount++;
                                            traycounts++;
                                        }

                                    }
                                    else
                                    {
                                        map2.clear();
                                        map2.put("barcode", barCodes[j]);
                                        PartBarcode partBarcode = deliveryPlanService.findUniqueByMap(PartBarcode.class, map2);
                                        if(partBarcode!=null)
                                        {
                                            tcount += 1;//pcj一个部件为一套
                                            tcounts += 1;
                                            ProductOutRecord productOutRecord= deliveryPlanService.findUniqueByMap(ProductOutRecord.class, map2);
                                            dweight += productOutRecord.getWeight();
                                            dweights += productOutRecord.getWeight();
                                        }
                                    }

                                }
                                cell = row.createCell(i);
                                cell.setCellValue(dweight);
                                cell.setCellStyle(cellStyle0);
                                b++;
                            }else{
                                cell = row.createCell(i);
                                cell.setCellValue("");
                                cell.setCellStyle(cellStyle0);
                                b++;
                            }
                        }else {//小计列
                            cell = row.createCell(i);
                            cell.setCellValue(dweights);
                            cell.setCellStyle(cellStyle0);
                        }
                    }
                    r++;
                }
                else{//其它行
                    row = sheet.createRow(r);
                    int b=0;//取dpd集合中的第b位值
                    for(int i=0;i<20+outSize;i++){
                        if (i==0){
                            cell = row.createCell(i);
                            cell.setCellValue(rowName[k]);
                            cell.setCellStyle(cellStyle0);
                        }else if (i>0&&i<19+outSize){
                            if (b>serviceDeliverySlip.size()-1){//超出集合索引
                                cell = row.createCell(i);
                                cell.setCellValue("");
                                cell.setCellStyle(cellStyle0);
                            }else {
                                cell = row.createCell(i);
                                cell.setCellValue(serviceDeliverySlip.get(b).get(rowKey[k])==null ? "" : serviceDeliverySlip.get(b).get(rowKey[k]).toString());
                                cell.setCellStyle(cellStyle0);
                                b++;
                            }
                        }else {//小计列
                            cell = row.createCell(i);
                            cell.setCellValue("");
                            cell.setCellStyle(cellStyle0);
                        }
                    }
                    r++;
                }
            }
            //倒数第三行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("装箱要求");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            cell.setCellValue("包装方式：");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(2);
            cell.setCellValue(deliveryPlan.getPackagingType()==null ? "" : deliveryPlan.getPackagingType());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 2, 4));
            cell = row.createCell(5);
            cell.setCellValue("物流公司：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellValue(deliveryPlan.getLogisticsCompany()==null ? "" : deliveryPlan.getLogisticsCompany());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 6, 10));
            cell = row.createCell(11);
            cell.setCellValue("装箱注意事项: ");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 11, 12));
            for (int i=13;i<20+outSize;i++){
                if (i==13){
                    cell = row.createCell(i);
                    cell.setCellValue(deliveryPlan.getAttention()==null ? "" : deliveryPlan.getAttention());
                    cell.setCellStyle(cellStyle0);
                }else{
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle0);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(r,r, 13, 19+outSize));
            r++;
            //倒数第二行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("第一审批人:");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            if (auditInstance.size() > 0) {
                if (auditInstance.get(0).get("FIRSTREALAUDITUSERNAME") != null) {
                    cell.setCellValue(auditInstance.get(0).get("FIRSTREALAUDITUSERNAME").toString());// 第一审核人
                } else {
                    cell.setCellValue("");
                }
            } else {
                cell.setCellValue("");
            }
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 1, 2));
            cell = row.createCell(3);
            cell.setCellValue("第二审批人:");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            if (auditInstance.size() > 0) {
                if (auditInstance.get(0).get("SECONDREALAUDITUSERNAME") != null) {
                    cell.setCellValue(auditInstance.get(0).get("SECONDREALAUDITUSERNAME").toString());// 第二审核人
                } else {
                    cell.setCellValue("");
                }
            } else {
                cell.setCellValue("");
            }
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 4, 5));
            cell = row.createCell(6);
            cell.setCellValue("发货人员:");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            if (operater != null) {
                cell.setCellValue(operater.getUserName());
            } else {
                cell.setCellValue("");
            }
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 7, 10));
            cell = row.createCell(11);
            cell.setCellValue("收货人");
            cell.setCellStyle(cellStyle0);
            for (int i=12;i<20+outSize;i++){
                if (i==12){
                    cell = row.createCell(i);
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyle0);
                }else{
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle0);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(r,r, 12, 19+outSize));
            r++;
            //最后一行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("司机签字：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            cell.setCellValue("");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 1, 2));
            cell = row.createCell(3);
            cell.setCellValue("客户备注信息：");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellValue(deliveryPlan.getCustomerNotes()==null ? "" : deliveryPlan.getCustomerNotes());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 4, 5));
            cell = row.createCell(6);
            cell.setCellValue("发货时间/发货车牌号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            if(dpso.get(0).getPlate()==null || dpso.get(0).getPlate().equals("")){
                cell.setCellValue("发货时间："+sf.format(deliveryPlan.getDeliveryDate()));
            }else{
                cell.setCellValue("发货时间："+sf.format(deliveryPlan.getDeliveryDate())+"/"+"发货车牌号："+(dpso.get(0).getPlate()==null ? "" : dpso.get(0).getPlate()));
            }
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r,r, 7, 10));
            cell = row.createCell(11);
            cell.setCellValue("收货时间");
            cell.setCellStyle(cellStyle0);
            for (int i=12;i<20+outSize;i++){
                if (i==12){
                    cell = row.createCell(i);
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyle0);
                }else{
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle0);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(r,r, 12, 19+outSize));

            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            QRCode.encode(deliveryPlan.getDeliveryCode(), byteArrayOut, 250, 250);
            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            SXSSFDrawing patriarch = sheet.createDrawingPatriarch();
            //anchor主要用于设置图片的属性
            XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 250, 250, (short) 17+outSize, 0, (short) 18+outSize, 1);
            anchor.setAnchorType(ClientAnchor.AnchorType.byId(3));
            //插入图片
            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));
        }
        HttpUtils.download(response,wb,templateName);
    }

    /**
     * 根据出库计划id导出成品发货通知单（竖排2）
     *
     * @param ids
     * @throws Exception
     */
    @NoLogin
    @Journal(name = "根据成品出库装厢表id导出Excel（横排2）")
    @ResponseBody
    @RequestMapping(value = "exportnew2", method = RequestMethod.GET)
    public void exportnew2(String ids) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "成品发货通知单(横版2)";
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

        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.RIGHT); // 右对齐
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setBorderBottom(BorderStyle.THIN);
        cellStyle1.setBorderTop(BorderStyle.NONE);
        cellStyle1.setBorderRight(BorderStyle.THIN);
        cellStyle1.setBorderLeft(BorderStyle.THIN);

        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.LEFT); // 左对齐
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        cellStyle2.setWrapText(true);// 单元格自动换行

        CellStyle cellStyle4 = wb.createCellStyle();
        cellStyle4.setAlignment(HorizontalAlignment.LEFT); // 左对齐
        cellStyle4.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle4.setBorderBottom(BorderStyle.THIN);
        cellStyle4.setBorderTop(BorderStyle.THIN);
        cellStyle4.setBorderRight(BorderStyle.THIN);
        cellStyle4.setBorderLeft(BorderStyle.THIN);
        cellStyle4.setWrapText(true);// 单元格自动换行

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
        SXSSFSheet sheet;

        String[] idsArray = ids.split(",");
        for (String idString : idsArray){
            ProductOutOrder productOutOrder = deliveryPlanService.findById(ProductOutOrder.class, Long.parseLong(idString));
            DeliveryPlan deliveryPlan = deliveryPlanService.findById(DeliveryPlan.class, productOutOrder.getDeliveryId());
            //出货员
            User operater =deliveryPlanService.findById(User.class,productOutOrder.getOperateUserId());
            // 业务员
            User user = deliveryPlanService.findById(User.class, deliveryPlan.getDeliveryBizUserId());
            List<Map<String, Object>> auditInstance = deliveryPlanService.searchAuditer("com.bluebirdme.mes.planner.delivery.entity.DeliveryPlan", deliveryPlan.getId());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("deliveryId", deliveryPlan.getId());
            // 出库订单关联信息
            List<DeliveryPlanSalesOrders> dpso = deliveryPlanService.findListByMap(DeliveryPlanSalesOrders.class, map);
            //客户信息
            Consumer consumer = consumerService.findById(Consumer.class, deliveryPlan.getConsumerId());
            List<Map<String, Object>> serviceDeliverySlip = deliveryPlanService.findDeliverySlipMirror(Long.parseLong(idString));
            if(serviceDeliverySlip.size() == 0){
                serviceDeliverySlip = deliveryPlanService.findDeliverySlip(Long.parseLong(idString));
            }
            sheet = wb.createSheet(deliveryPlan.getDeliveryCode()+ids);
            Row row = null;
            Cell cell = null;
            int outSize=0;
            if(serviceDeliverySlip.size()>4){
                outSize +=serviceDeliverySlip.size()-4;
            }
            int r=0;
            //第一行
            row = sheet.createRow(r);
            row.setHeightInPoints(50);
            cell=row.createCell(0);
            cell.setCellValue("浙江恒石纤维基业有限公司成品发货通知单");
            sheet.setColumnWidth(0, 8 * 256);// 设置列宽
            cell.setCellStyle(cellStyle);
            for (int i=1;i<7+outSize;i++){
                cell=row.createCell(i);
                sheet.setColumnWidth(i, 13 * 256);// 设置列宽
                cell.setCellStyle(cellStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(r,r, 0, 6+outSize));
            r++;
            //第二行到第十行
            String cellName[] = new String[]{"发货单编号（单据编号）：", "发货日期（单据日期）：","业务员：","购货单位：","单位简称：",
                    "客户基地（收货地址）:","收货人及联系电话：","样布信息:","运输方式："};
            String cellValue[] = new String[]{deliveryPlan.getDeliveryCode(),deliveryPlan.getDeliveryDate().toString(),user.getUserName(),deliveryPlan.getDeliveryTargetCompany(),
                    consumer.getConsumerSimpleName(),deliveryPlan.getShippingAddress(),deliveryPlan.getLinkmanAndPhone(),deliveryPlan.getSampleInformation()
            };
            for (int k=0;k<cellName.length;k++){
                if (k==cellName.length-1){
                    row = sheet.createRow(r);
                    cell=row.createCell(0);
                    cell.setCellValue(r);
                    cell.setCellStyle(cellStyle1);
                    cell=row.createCell(1);
                    cell.setCellValue( cellName[k]);
                    cell.setCellStyle(cellStyle2);
                    cell=row.createCell(2);
                    cell.setCellStyle(cellStyle2);
                    sheet.addMergedRegion(new CellRangeAddress(r,r, 1, 2));
                    for (int i=3;i<7+outSize;i++){
                        if (i==3){
                            cell=row.createCell(i);
                            cell.setCellValue("□物流    □快递    □送货    □自提    □其他");
                            cell.setCellStyle(cellStyle2);
                        }else{
                            cell=row.createCell(i);
                            cell.setCellStyle(cellStyle2);
                        }
                    }
                    sheet.addMergedRegion(new CellRangeAddress(r,r, 3, 6+outSize));
                    r++;
                }else{
                    row = sheet.createRow(r);
                    cell=row.createCell(0);
                    cell.setCellValue(r);
                    cell.setCellStyle(cellStyle1);
                    cell=row.createCell(1);
                    cell.setCellValue( cellName[k]);
                    cell.setCellStyle(cellStyle2);
                    cell=row.createCell(2);
                    cell.setCellStyle(cellStyle2);
                    sheet.addMergedRegion(new CellRangeAddress(r,r, 1, 2));
                    for (int i=3;i<7+outSize;i++){
                        if (i==3){
                            cell=row.createCell(i);
                            cell.setCellValue(cellValue[k]);
                            cell.setCellStyle(cellStyle2);
                        }else{
                            cell=row.createCell(i);
                            cell.setCellStyle(cellStyle2);
                        }
                    }
                    sheet.addMergedRegion(new CellRangeAddress(r,r, 3, 6+outSize));
                    r++;
                }
            }
            //箱单数据
            row = sheet.createRow(r);//编号
            cell=row.createCell(0);
            cell.setCellValue(r);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellValue("箱单数据");
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("编号");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                cell=row.createCell(i);
                cell.setCellValue(i-2);
                cell.setCellStyle(cellStyle2);
            }
            r++;
            row = sheet.createRow(r);//装箱号
            cell=row.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("装箱号");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                if (i-3<dpso.size()){
                    cell=row.createCell(i);
                    cell.setCellValue(dpso.get(i-3).getPn()==null ? "" : dpso.get(i-3).getPn());//从0开始索引
                    cell.setCellStyle(cellStyle2);
                }else{
                    cell=row.createCell(i);
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyle2);
                }
            }
            r++;
            row = sheet.createRow(r);//提单号
            cell=row.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("提单号");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                if (i-3<dpso.size()){
                    cell=row.createCell(i);
                    cell.setCellValue(dpso.get(i-3).getLadingCode()==null ? "" : dpso.get(i-3).getLadingCode());//从0开始索引
                    cell.setCellStyle(cellStyle2);
                }else{
                    cell=row.createCell(i);
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyle2);
                }
            }
            r++;
            row = sheet.createRow(r);//箱号
            cell=row.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("箱号");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                if (i-3<dpso.size()){
                    cell=row.createCell(i);
                    cell.setCellValue(dpso.get(i-3).getBoxNumber()==null ? "" : dpso.get(i-3).getBoxNumber());//从0开始索引
                    cell.setCellStyle(cellStyle2);
                }else{
                    cell=row.createCell(i);
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyle2);
                }
            }
            r++;
            row = sheet.createRow(r);//封号
            cell=row.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("封号");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                if (i-3<dpso.size()){
                    cell=row.createCell(i);
                    cell.setCellValue(dpso.get(i-3).getSerialNumber()==null ? "" : dpso.get(i-3).getSerialNumber());//从0开始索引
                    cell.setCellStyle(cellStyle2);
                }else{
                    cell=row.createCell(i);
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyle2);
                }
            }
            r++;
            row = sheet.createRow(r);//件数
            cell=row.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("件数");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                if (i-3<dpso.size()){
                    cell=row.createCell(i);
                    cell.setCellValue(dpso.get(i-3).getCount()==null ? "" : dpso.get(i-3).getCount().toString());//从0开始索引
                    cell.setCellStyle(cellStyle2);
                }else{
                    cell=row.createCell(i);
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyle2);
                }
            }
            r++;
            row = sheet.createRow(r);//毛重
            cell=row.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("毛重");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                if (i-3<dpso.size()){
                    cell=row.createCell(i);
                    cell.setCellValue(dpso.get(i-3).getWeight()==null ? "" : dpso.get(i-3).getWeight().toString());//从0开始索引
                    cell.setCellStyle(cellStyle2);
                }else{
                    cell=row.createCell(i);
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyle2);
                }
            }
            r++;
            row = sheet.createRow(r);//尺码
            cell=row.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("尺码");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                if (i-3<dpso.size()){
                    cell=row.createCell(i);
                    cell.setCellValue(dpso.get(i-3).getSize()==null ? "" : dpso.get(i-3).getSize().toString());//从0开始索引
                    cell.setCellStyle(cellStyle2);
                }else{
                    cell=row.createCell(i);
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyle2);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(10,r, 1, 1));
            r++;
            //遍历数据行
            String rowName[] = new String[]{"编号（序号）", "装箱号", "订单号", "批次号","Po No", "关税编号",
                    "物料规格型号","客户产品名称", "厂内名称", "部件名称", "门幅", "米长（m）","发货数量(托)", "发货数量(套)", "发货数量（kg）","收货数量(托)",
                    "收货数量(套)","收货数量(kg)","总计（小计）：","备注"};
            //取值的key
            String rowKey[] = new String[]{"No","PN","SALESORDERSUBCODE","BATCHCODE","4","5",
                    "PRODUCTMODEL","CONSUMERPRODUCTNAME","FACTORYPRODUCTNAME","PARTNAME","PRODUCTWIDTH","PRODUCTROLLLENGTH","12","13","14","15",
                    "16","17","18","MEMO"
            };
            for (int k=0;k<rowName.length;k++){
                if (k==0){
                    row = sheet.createRow(r);
                    cell=row.createCell(0);
                    cell.setCellValue(11);
                    cell.setCellStyle(cellStyle1);
                    cell=row.createCell(1);
                    cell.setCellValue("发货内容：");
                    cell.setCellStyle(cellStyle4);
                    cell=row.createCell(2);
                    cell.setCellValue(rowName[k]);
                    cell.setCellStyle(cellStyle2);
                    int No=1;
                    for(int i=3;i<7+outSize;i++){
                        cell=row.createCell(i);
                        cell.setCellValue(No);
                        cell.setCellStyle(cellStyle2);
                        No++;
                    }
                    r++;
                }else{
                    row = sheet.createRow(r);
                    cell=row.createCell(0);
                    cell.setCellStyle(cellStyle1);
                    cell=row.createCell(1);
                    cell.setCellStyle(cellStyle4);
                    cell=row.createCell(2);
                    cell.setCellValue(rowName[k]);
                    cell.setCellStyle(cellStyle2);
                    int b=0;
                    for(int i=3;i<7+outSize;i++){
                        if (b<serviceDeliverySlip.size()){
                            cell=row.createCell(i);
                            cell.setCellValue(serviceDeliverySlip.get(b).get(rowKey[k])==null ? "" : serviceDeliverySlip.get(b).get(rowKey[k]).toString());
                            cell.setCellStyle(cellStyle2);
                            b++;
                        }else{
                            cell=row.createCell(i);
                            cell.setCellValue("");
                            cell.setCellStyle(cellStyle2);
                        }
                    }
                    r++;
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(18,r-1, 1, 1));
            //装箱要求
            row = sheet.createRow(r);//包装方式
            cell=row.createCell(0);
            cell.setCellValue(12);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellValue("装箱要求");
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("包装方式：");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                cell=row.createCell(i);
                cell.setCellValue("");
                cell.setCellStyle(cellStyle2);
            }
            r++;
            row = sheet.createRow(r);//物流公司
            cell=row.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("物流公司");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                cell=row.createCell(i);
                cell.setCellValue("");
                cell.setCellStyle(cellStyle2);
            }
            r++;
            row = sheet.createRow(r);//装箱注意事项
            cell=row.createCell(0);
            cell.setCellStyle(cellStyle1);
            cell=row.createCell(1);
            cell.setCellStyle(cellStyle4);
            cell=row.createCell(2);
            cell.setCellValue("装箱注意事项");
            cell.setCellStyle(cellStyle2);
            for (int i=3;i<7+outSize;i++){
                cell=row.createCell(i);
                cell.setCellValue("");
                cell.setCellStyle(cellStyle2);
            }
            sheet.addMergedRegion(new CellRangeAddress(38,r, 1, 1));
            r++;
            //auditInstance
            String rowName1[] = new String[]{"第一审批人","第二审批人","发货人员","司机签字","收货人",
                    "发货时间","发货车牌号","客户备注信息","收货时间"
            };
            int num=13;
            for (int k=0;k<rowName1.length;k++){
                row = sheet.createRow(r);
                cell=row.createCell(0);
                cell.setCellValue(num);
                cell.setCellStyle(cellStyle1);
                cell=row.createCell(1);
                cell.setCellValue(rowName1[k]);
                cell.setCellStyle(cellStyle2);
                switch (k){
                    case 0:
                        for (int i = 2; i < 7+outSize; i++) {
                            if (i==3){
                                cell=row.createCell(i);
                                //第一审批人
                                if (auditInstance.size() > 0) {
                                    cell.setCellValue(auditInstance.get(0).get("FIRSTREALAUDITUSERNAME")==null ? "" : auditInstance.get(0).get("FIRSTREALAUDITUSERNAME").toString());
                                } else {
                                    cell.setCellValue("");
                                }
                                cell.setCellStyle(cellStyle2);
                            }else{
                                cell=row.createCell(i);
                                cell.setCellValue("");
                                cell.setCellStyle(cellStyle2);
                            }
                        }
                        break;
                    case 1:
                        for (int i = 2; i < 7+outSize; i++) {
                            if (i==3){
                                cell=row.createCell(i);
                                //第二审批人
                                if (auditInstance.size() > 0) {
                                    cell.setCellValue(auditInstance.get(0).get("SECONDREALAUDITUSERNAME")==null ? "" : auditInstance.get(0).get("SECONDREALAUDITUSERNAME").toString());
                                } else {
                                    cell.setCellValue("");
                                }
                                cell.setCellStyle(cellStyle2);
                            }else{
                                cell=row.createCell(i);
                                cell.setCellStyle(cellStyle2);
                            }
                        }
                        break;
                    case 2:
                        for (int i = 2; i < 7+outSize; i++) {
                            if (i==3){
                                cell=row.createCell(i);
                                //发货人员
                                if (operater != null) {
                                    cell.setCellValue(operater.getUserName());
                                } else {
                                    cell.setCellValue("");
                                }
                                cell.setCellStyle(cellStyle2);
                            }else{
                                cell=row.createCell(i);
                                cell.setCellValue("");
                                cell.setCellStyle(cellStyle2);
                            }
                        }
                        break;
                    case 3://司机签字
                        for (int i = 2; i < 7+outSize; i++) {
                            cell=row.createCell(i);
                            cell.setCellStyle(cellStyle2);
                        }
                        break;
                    case 4://收货人
                        for (int i = 2; i < 7+outSize; i++) {
                            cell=row.createCell(i);
                            cell.setCellStyle(cellStyle2);
                        }
                        break;
                    case 5://发货时间
                        for (int i = 2; i < 7+outSize; i++) {
                            if (i==3){
                                cell=row.createCell(i);
                                //发货时间
                                cell.setCellValue(sf.format(deliveryPlan.getDeliveryDate()));
                                cell.setCellStyle(cellStyle2);
                            }else{
                                cell=row.createCell(i);
                                cell.setCellStyle(cellStyle2);
                            }
                        }
                        break;
                    case 6://发货车牌号
                        for (int i = 2; i < 7+outSize; i++) {
                            if (i==3){
                                cell=row.createCell(i);
                                //发货车牌号
                                cell.setCellValue(dpso.get(0).getPlate()==null ? "" : dpso.get(0).getPlate());
                                cell.setCellStyle(cellStyle2);
                            }else{
                                cell=row.createCell(i);
                                cell.setCellStyle(cellStyle2);
                            }
                        }
                        break;
                    case 7://客户备注信息
                        for (int i = 2; i < 7+outSize; i++) {
                            cell=row.createCell(i);
                            cell.setCellStyle(cellStyle2);
                        }
                        break;
                    case 8://收货时间
                        for (int i = 2; i < 7+outSize; i++) {
                            cell=row.createCell(i);
                            cell.setCellStyle(cellStyle2);
                        }
                        break;
                }
                num++;
                r++;
            }
        }
        HttpUtils.download(response,wb,templateName);
    }


    @NoLogin
    @Journal(name = "根据成品出库装箱表id导出明阳风电格式的Excel")
    @ResponseBody
    @RequestMapping(value = "exportMyfd", method = RequestMethod.GET)
    public void exportMyfd(String ids) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "成品出库码单明阳风电版(" + sf.format(new Date()) + ")";
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Font font = wb.createFont();
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
        font1.setFontHeightInPoints((short) 10);
        font1.setBold(true); // 字体增粗
        CellStyle cellStyle3 = wb.createCellStyle();
        cellStyle3.setAlignment(HorizontalAlignment.LEFT); // 左对齐
        cellStyle3.setBorderBottom(BorderStyle.THIN);
        cellStyle3.setBorderTop(BorderStyle.THIN);
        cellStyle3.setBorderRight(BorderStyle.THIN);
        cellStyle3.setBorderLeft(BorderStyle.THIN);
        cellStyle3.setFont(font1);
        SXSSFSheet sheet;
        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.RIGHT); // 右对齐
        cellStyle1.setBorderBottom(BorderStyle.THIN);
        cellStyle1.setBorderTop(BorderStyle.NONE);
        cellStyle1.setBorderRight(BorderStyle.THIN);
        cellStyle1.setBorderLeft(BorderStyle.THIN);

        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.LEFT); // 左对齐
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);

        String[] idsArray = ids.split(",");
        for (String idString : idsArray) {
            List<Map<String, Object>> serviceDeliverySlip = deliveryPlanService.findDeliverySlipMirror(Long.parseLong(idString));
            if (serviceDeliverySlip.size() == 0) {
                serviceDeliverySlip = deliveryPlanService.findDeliverySlip(Long.parseLong(idString));
            }
            ProductOutOrder productOutOrder = deliveryPlanService.findById(ProductOutOrder.class, Long.parseLong(idString));
            Map<String,Object> nameMap=new HashMap<>();
            nameMap.put("deliveryCode",productOutOrder.getDeliveryCode());
            List<ProductOutRecord> productOutRecords = deliveryPlanService.findListByMap(ProductOutRecord.class, nameMap);
            User user = deliveryPlanService.findById(User.class, productOutRecords.get(0).getOperateUserId());
            sheet = wb.createSheet(productOutOrder.getDeliveryCode() + "-" + productOutOrder.getPn());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("deliveryCode", productOutOrder.getDeliveryCode());
            List<DeliveryPlan> plans = deliveryPlanService.findListByMap(DeliveryPlan.class, map);
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("deliveryId", plans.get(0).getId());
            // 生成一个字体
            Row row;
            Cell cell;
            //第一行
            int r = 0;// 从第1行开始写数据
            row = sheet.createRow(r);
            row.setHeightInPoints(50);
            cell = row.createCell(0);
            sheet.setColumnWidth(0, 3 * 256);// 设置列宽
            cell.setCellStyle(cellStyle);
            cell.setCellValue("浙江恒石纤维基业有限公司成品出库码单");
            cell = row.createCell(1);
            sheet.setColumnWidth(1, 13 * 256 + 32);// 设置列宽
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            sheet.setColumnWidth(2, 15 * 256 + 122);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            sheet.setColumnWidth(3, 7 * 256 + 190);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            sheet.setColumnWidth(4, 5 * 256 + 235);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            sheet.setColumnWidth(5, 12 * 256 + 95);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
            sheet.setColumnWidth(6, 10 * 256 + 32);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(7);
            sheet.setColumnWidth(7, 18 * 256 + 32);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(8);
            sheet.setColumnWidth(8, 18 * 256 + 77);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(9);
            sheet.setColumnWidth(9, 5 * 256 + 8);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(10);
            sheet.setColumnWidth(10, 5 * 256 + 8);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(11);
            sheet.setColumnWidth(11, 5 * 256 + 8);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(12);
            sheet.setColumnWidth(12, 5 * 256 + 8);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(13);
            sheet.setColumnWidth(13, 5 * 256 + 8);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(14);
            sheet.setColumnWidth(14, 5 * 256 + 8);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(15);
            sheet.setColumnWidth(15, 5 * 256 + 8);
            cell.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 15));
            r++;
            //第三行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("购货单位：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(2);
            cell.setCellValue(productOutOrder.getConsumerName() == null ? "" : productOutOrder.getConsumerName());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle2);

            cell = row.createCell(9);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 10));
            cell = row.createCell(11);
            cell.setCellValue("单据编号：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 11, 12));
            cell = row.createCell(13);
            //单据编号
            cell.setCellValue(plans.get(0) == null ? "" : plans.get(0).getDeliveryCode());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 13, 15));
            r++;
            //第四行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("收货地址：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
            cell = row.createCell(2);
            //basePlace deliverPlan
            cell.setCellValue(plans.get(0).getShippingAddress() == null ? "" : plans.get(0).getShippingAddress());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 10));
            cell = row.createCell(11);
            cell.setCellValue("单据日期：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 11, 12));
            cell = row.createCell(13);
            cell.setCellValue(sf.format(plans.get(0).getDeliveryDate()));
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 13, 15));
            r++;

            //第五行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("收货人及联系电话：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(1);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(2);
            cell.setCellValue(plans.get(0).getLinkmanAndPhone() == null ? "" : plans.get(0).getLinkmanAndPhone());
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(11);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 15));
            r++;
            //第七行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("运输方式：");
            cell.setCellStyle(cellStyle1);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle1);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle1);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle1);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 3));
            cell = row.createCell(4);
            cell.setCellValue("□物流    □快递    □送货    □自提    □其他");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(11);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 4, 15));
            r++;
            //第八行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("序号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(1);
            cell.setCellValue("订单号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(2);
            cell.setCellValue("批次号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellValue("物料编码");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 3, 4));
            cell = row.createCell(5);
            cell.setCellValue("物料规格型号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 5, 6));
            cell = row.createCell(7);
            cell.setCellValue("客户名称");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellValue("部件名称");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellValue("发货数量(托)");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellValue("发货数量(套)");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(11);
            cell.setCellValue("发货数量(Kg)");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellValue("收货数量(托)");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(13);
            cell.setCellValue("收货数量(套)");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(14);
            cell.setCellValue("收货数量(Kg)");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellValue("备注");
            cell.setCellStyle(cellStyle0);
            r++;

            //遍历数据
            int tcounts = 0;//总套数
            int traycounts = 0;//总托数
            Double dweights = 0.00;
            int index = 1;
            for (Map<String, Object> mapobject : serviceDeliverySlip) {
                int tcount = 0;
                int traycount = 0;
                Double dweight = 0.00;
                if (mapobject.get("BARCODES") == null) {
                    continue;
                }
                String barCodes[] = mapobject.get("BARCODES").toString().split(",");
                Map<String, Object> map2 = new HashMap<String, Object>();
                for (int j = 0; j < barCodes.length; j++) {
                    map2.clear();
                    map2.put("barcode", barCodes[j]);
                    TrayBarCode tb = deliveryPlanService.findUniqueByMap(TrayBarCode.class, map2);
                    if (tb != null) {
                        ProductOutRecord productOutRecord = deliveryPlanService.findUniqueByMap(ProductOutRecord.class, map2);
                        dweight += productOutRecord.getWeight();
                        dweights += productOutRecord.getWeight();
                        //判断是否是套材
                        if (tb.getPartId() != null && !"".equals(tb.getPartId())) {
                            //判断是成品胚布或常规部件
                            TcBomVersionParts part = deliveryPlanService.findById(TcBomVersionParts.class, tb.getPartId());
                            if ("成品胚布".equals(part.getTcProcBomVersionPartsType())) {//成品胚布
                                tcount += 1;//成品胚布一托为一套
                                tcounts += 1;
                            } else {//常规部件
                                map.clear();
                                map.put("trayBarcode", barCodes[j]);
                                List<TrayBoxRoll> trays = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                for (TrayBoxRoll tray : trays) {
                                    if (tray.getBoxBarcode() != null && !"".equals(tray.getBoxBarcode())) {
                                        map.clear();
                                        map.put("boxBarcode", tray.getBoxBarcode());
                                        List<TrayBoxRoll> boxs = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                        if (boxs.size() > 0) {
                                            tcount += boxs.size();//套数为部件条码数量
                                            tcounts += boxs.size();
                                        }
                                    } else {
                                        if (trays.size() > 0) {
                                            tcount += trays.size();//套数为部件条码数量
                                            tcounts += trays.size();
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            //非套材增加 托数累加
                            traycount++;
                            traycounts++;
                        }
                    } else {
                        map2.clear();
                        map2.put("barcode", barCodes[j]);
                        PartBarcode partBarcode = deliveryPlanService.findUniqueByMap(PartBarcode.class, map2);
                        if (partBarcode != null) {
                            tcount += 1;//pcj一个部件为一套
                            tcounts += 1;
                            ProductOutRecord productOutRecord = deliveryPlanService.findUniqueByMap(ProductOutRecord.class, map2);
                            dweight += productOutRecord.getWeight();
                            dweights += productOutRecord.getWeight();
                        }
                    }
                }
                row = sheet.createRow(r);
                cell = row.createCell(0);
                //序号
                cell.setCellValue(index);
                index++;
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(1);
                //订单号
                cell.setCellValue(mapobject.get("CODEORDERNO") == null ? "" : mapobject.get("CODEORDERNO").toString());
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(2);
                //批次号
                cell.setCellValue(mapobject.get("BATCHCODE") == null ? "" : mapobject.get("BATCHCODE").toString());
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(3);
                //物料编码mapobject.get("MATERIELCODE").toString()
                cell.setCellValue(mapobject.get("MATERIELCODE") == null ? "" : mapobject.get("MATERIELCODE").toString());
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(4);
                cell.setCellStyle(cellStyle0);
                sheet.addMergedRegion(new CellRangeAddress(r, r, 3, 4));
                cell = row.createCell(5);
                //物料规格型号
                cell.setCellValue(mapobject.get("FACTORYPRODUCTNAME") == null ? "" : mapobject.get("FACTORYPRODUCTNAME").toString());
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(6);
                cell.setCellStyle(cellStyle0);
                sheet.addMergedRegion(new CellRangeAddress(r, r, 5, 6));
                cell = row.createCell(7);
                //客户名称
                cell.setCellValue(mapobject.get("CONSUMERNAME") == null ? "" : mapobject.get("CONSUMERNAME").toString());
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(8);
                //部件名称mapobject.get("PARTNAME").toString()
                cell.setCellValue(mapobject.get("PARTNAME") == null ? "" : mapobject.get("PARTNAME").toString());
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(9);
                //deliveryCount出库托数,送货数量==null ? 0 : mapobject.get("DELIVERYCOUNT")
                cell.setCellValue(traycount);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(10);
                //发货数量套
                cell.setCellValue(tcount);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(11);
                //发货数量Kg
                cell.setCellValue(dweight);
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(12);
                //收货数量托
                cell.setCellValue("");
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(13);
                //收货数量套
                cell.setCellValue("");
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(14);
                //收货数量Kg
                cell.setCellValue("");
                cell.setCellStyle(cellStyle0);
                cell = row.createCell(15);
                //备注
                cell.setCellValue("");
                cell.setCellStyle(cellStyle0);
                r++;
            }

            //倒数第三行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(7);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 7));
            cell = row.createCell(8);
            cell.setCellValue("总计");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            //合计托
            cell.setCellValue(traycounts);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            //合计套
            cell.setCellValue(tcounts);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(11);
            //合计kg
            cell.setCellValue(dweights);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle0);
            r++;
            //倒数第二行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("发货人");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(1);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(2);
            cell.setCellValue(user.getUserName() == null ? "" : user.getUserName());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 3));
            cell = row.createCell(4);
            cell.setCellValue("司机签字：");
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle2);
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle2);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 4, 6));
            cell = row.createCell(7);
            cell.setCellValue("样布信息");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            //样布信息
            cell.setCellValue(productOutOrder.getSampleInformation() == null ? "" : productOutOrder.getSampleInformation());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellValue("收货人");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 9, 10));
            cell = row.createCell(11);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 11, 15));
            r++;
            //最后一行
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("发货时间/车牌号");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
            cell = row.createCell(2);
            if (productOutOrder.getPlate() != null) {
                cell.setCellValue("发货时间:" + sf.format(plans.get(0).getDeliveryDate()) + "/" + "车牌号:" + productOutOrder.getPlate());
            } else {
                cell.setCellValue("发货时间:" + sf.format(plans.get(0).getDeliveryDate()));
            }
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(5);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(6);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 6));
            cell = row.createCell(7);
            cell.setCellValue("客户备注信息");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(8);
            cell.setCellValue(plans.get(0).getCustomerNotes() == null ? "" : plans.get(0).getCustomerNotes());
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(9);
            cell.setCellValue("收货时间");
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(10);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 9, 10));
            cell = row.createCell(11);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(12);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(13);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(14);
            cell.setCellStyle(cellStyle0);
            cell = row.createCell(15);
            cell.setCellStyle(cellStyle0);
            sheet.addMergedRegion(new CellRangeAddress(r, r, 11, 15));
        }
        HttpUtils.download(response,wb,templateName);
    }

    @NoLogin
    @Journal(name = "根据出货单id导出Excel明细")
    @ResponseBody
    @RequestMapping(value = "export2", method = RequestMethod.GET)
    public void export2(String ids) throws Exception {
        String idsArray[] = ids.split(",");
        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        for (String idString : idsArray) {
            ProductOutOrder poo = deliveryPlanService.findById(ProductOutOrder.class, Long.parseLong(idString));
            int i = 0;
            Sheet sheet = wb.createSheet(poo.getDeliveryCode() + "-" + poo.getPn());
            Row row;
            Cell cell;
            row = sheet.createRow(i);

            String columnName[] = new String[]{"批次号 Batch number",
                    "客户订单号 PO number", "产品名称 Product name", "托编号 Pallet number",
                    "托净重 Pallet Net weight(KGS)", "盒编号 Box number",
                    "盒重 Box Weight(KGS)", "卷编号 Roll number",
                    "卷重 Roll Weight(KGS)", "托毛重 Pallet Gross weight(KGS)",
                    "卷长 Roll Length(M)"};
            HashMap<String, Object> map = new HashMap();
            map.put("packingNumber", poo.getPackingNumber());
            List<ProductOutRecord> porList = deliveryPlanService.findListByMap(ProductOutRecord.class, map);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                if (a == 0) {
                    cell.setCellValue("出货单编号 Bill of sales");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 3));
                }
                if (a == 4) {
                    cell.setCellValue(poo.getDeliveryCode());
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 4, 10));
                }
            }
            i++;
            row = sheet.createRow(i);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                if (a == 0) {
                    cell.setCellValue("发票号 Invoice Number");
                }
                if (a == 1) {
                    cell.setCellValue("");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 3));
                }
                if (a == 4) {
                    cell.setCellValue("日期 DATE");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 4, 5));
                }
                if (a == 6) {
                    cell.setCellValue("");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 6, 10));
                }
            }

            i++;
            row = sheet.createRow(i);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                if (a == 0) {
                    cell.setCellValue("客户名称 Customer name");
                }
                if (a == 1) {
                    cell.setCellValue(poo.getConsumerName());
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 3));
                }
                if (a == 4) {
                    cell.setCellValue("供应商 Supplier");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 4, 5));
                }
                if (a == 6) {
                    cell.setCellValue("");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 6, 10));
                }
            }
            i++;
            row = sheet.createRow(i);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellValue(columnName[a]);
                cell.setCellStyle(cellStyle);
            }
            i++;
            for (ProductOutRecord por : porList) {
                PartBarcode pb = null;
                // 根据托条码查询托箱卷关系获得卷条码列表
                map.clear();
                map.put("barcode", por.getBarCode());
                TrayBarCode tb = deliveryPlanService.findUniqueByMap(TrayBarCode.class, map);
                if (null == tb) {
                    pb = deliveryPlanService.findUniqueByMap(PartBarcode.class, map);
                }
                SalesOrderDetail salesOrderDetail;
                if (tb != null) {
                    salesOrderDetail = deliveryPlanService.findById(SalesOrderDetail.class, Long.valueOf(tb.getSalesOrderDetailId()));
                } else {
                    salesOrderDetail = deliveryPlanService.findById(SalesOrderDetail.class, Long.valueOf(pb.getSalesOrderDetailId()));
                }
                map.clear();
                map.put("trayBarcode", por.getBarCode());
                Tray t = deliveryPlanService.findUniqueByMap(Tray.class, map);
                HashSet<Roll> rolls = getAllInfo(por.getBarCode());
                for (Roll r : rolls) {
                    row = sheet.createRow(i);
                    i++;
                    for (int a = 0; a < columnName.length; a++) {
                        FinishedProduct fp;
                        sheet.setColumnWidth(a, 5120);
                        cell = row.createCell(a);
                        cell.setCellStyle(cellStyle);
                        map.clear();
                        map.put("rollBarcode", r.getRollBarcode());
                        BoxRoll br = deliveryPlanService.findUniqueByMap(BoxRoll.class, map);
                        switch (a) {
                            case 0:
                                if (null != tb) {
                                    cell.setCellValue(tb.getBatchCode());
                                } else {
                                    cell.setCellValue(pb.getBatchCode());
                                }
                                break;
                            case 1:
                                cell.setCellValue(salesOrderDetail.getSalesOrderSubCodePrint() == null ? "" : salesOrderDetail.getSalesOrderSubCodePrint());
                                break;
                            case 2:
                                if (null != tb) {
                                    fp = deliveryPlanService.findById(FinishedProduct.class, tb.getSalesProductId());
                                } else {
                                    fp = deliveryPlanService.findById(FinishedProduct.class, pb.getSalesProductId());
                                }
                                if (null != tb) {
                                    if (tb.getPartName() != null && !tb.getPartName().equals("")) {
                                        cell.setCellValue(fp.getConsumerProductName() + "-" + tb.getPartName());
                                    }
                                } else if (pb.getPartName() != null && !pb.getPartName().equals("")) {
                                    cell.setCellValue(fp.getConsumerProductName() + "-" + pb.getPartName());
                                } else {
                                    cell.setCellValue(fp.getConsumerProductName());
                                }
                                break;
                            case 3:
                                if (null != tb) {
                                    cell.setCellValue(tb.getBarcode());
                                } else {
                                    cell.setCellValue("");
                                }
                                break;
                            case 4:
                                sheet.setColumnWidth(a, 2400);
                                if (null != t) {
                                    cell.setCellValue(t.getWeight());
                                } else {
                                    cell.setCellValue("");
                                }
                                break;
                            case 5:// 盒编号
                                if (br != null) {
                                    cell.setCellValue(br.getBoxBarcode());
                                }
                                break;
                            case 6:// 盒重
                                sheet.setColumnWidth(a, 2400);
                                if (br != null) {
                                    map.clear();
                                    map.put("boxBarcode", br.getBoxBarcode());
                                    Box b = deliveryPlanService.findUniqueByMap(Box.class, map);
                                    if (!"".equals(b.getWeight()) && b.getWeight() != null) {
                                        cell.setCellValue(b.getWeight());
                                    }
                                }
                                break;
                            case 7:
                                cell.setCellValue(r.getPartBarcode() == null ? r.getRollBarcode() : r.getPartBarcode());
                                break;
                            case 8:
                                sheet.setColumnWidth(a, 2400);
                                cell.setCellValue(r.getRollWeight() == null ? r.getRollAutoWeight() : r.getRollWeight());
                                break;
                            case 9:
                                sheet.setColumnWidth(a, 2400);
                                break;
                            case 10:
                                sheet.setColumnWidth(a, 2400);
                                if (r.getRollRealLength() == 0) {
                                    cell.setCellValue("");
                                } else {
                                    cell.setCellValue(r.getRollRealLength());
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            String value = "";
            for (int cl = 0; cl < 3; cl++) {
                for (int a = 0; a < sheet.getLastRowNum(); a++) {
                    int begin = 0;
                    int end = 0;
                    CellAddress address = new CellAddress(a, cl);
                    String beginString = sheet.getCellComment(address) + "";
                    // value一开始为空，beginString为当前单元格的内容，当beginstring和value不等，并且不是第一行的时候，进入条件
                    if (!beginString.equals(value) && begin != end) {
                        // 将value的值改为beginString的值，意味着新的合并条件，将单元格从begin的位置合并到end的位置
                        value = beginString;
                        sheet.addMergedRegion(new CellRangeAddress(begin, end - 1, cl, cl));
                        // 当列为托条码的时候，将后面的产品名称等内容也合并
                        if (cl == 2) {
                            sheet.addMergedRegion(new CellRangeAddress(begin, end - 1, 5, 5));
                            sheet.addMergedRegion(new CellRangeAddress(begin, end - 1, 6, 6));
                            sheet.addMergedRegion(new CellRangeAddress(begin, end - 1, 7, 7));
                        }
                    }
                }
            }
        }
        HttpUtils.download(response,wb,"PackingList");
    }

    @NoLogin
    @Journal(name = "根据出货单id导出Excel明细")
    @ResponseBody
    @RequestMapping(value = "exportDeliveryExcel", method = RequestMethod.GET)
    public void exportDeliveryExcel(String ids) throws Exception {
        SXSSFWorkbook wb = deliveryPlanService.exportDeliveryExcel(ids);
        HttpUtils.download(response,wb,"PackingList");
    }

    // 根据托条码获取托下面的所有卷信息
    public HashSet<Roll> getAllInfo(String trayBarcode) {
        HashSet<Roll> infos = new HashSet();
        HashMap<String, Object> map = new HashMap();
        map.put("trayBarcode", trayBarcode);
        List<TrayBoxRoll> tbrList = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
        if (tbrList.size() > 0) {
            for (TrayBoxRoll tbr : tbrList) {
                if (tbr.getRollBarcode() != null) {
                    map.clear();
                    map.put("rollBarcode", tbr.getRollBarcode());
                    Roll r = deliveryPlanService.findUniqueByMap(Roll.class, map);
                    if (r == null) {
                        map.clear();
                        map.put("partBarcode", tbr.getRollBarcode());
                        r = deliveryPlanService.findUniqueByMap(Roll.class, map);
                    }
                    infos.add(r);
                }

                if (tbr.getPartBarcode() != null) {
                    map.clear();
                    map.put("partBarcode", tbr.getPartBarcode());
                    Roll r = deliveryPlanService.findUniqueByMap(Roll.class, map);
                    if (r == null) {
                        map.clear();
                        map.put("rollBarcode", tbr.getPartBarcode());
                        r = deliveryPlanService.findUniqueByMap(Roll.class, map);
                    }
                    infos.add(r);
                }

                if (tbr.getBoxBarcode() != null) {
                    map.clear();
                    map.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brList = deliveryPlanService.findListByMap(BoxRoll.class, map);
                    for (BoxRoll br : brList) {
                        if (br.getRollBarcode() != null) {
                            map.clear();
                            map.put("rollBarcode", br.getRollBarcode());
                            Roll r = deliveryPlanService.findUniqueByMap(Roll.class, map);
                            if (r == null) {
                                map.clear();
                                map.put("partBarcode", tbr.getRollBarcode());
                                r = deliveryPlanService.findUniqueByMap(Roll.class, map);
                            }
                            infos.add(r);
                        }
                        if (br.getPartBarcode() != null) {
                            map.clear();
                            map.put("partBarcode", br.getPartBarcode());
                            Roll r = deliveryPlanService.findUniqueByMap(Roll.class, map);
                            if (r == null) {
                                map.clear();
                                map.put("rollBarcode", tbr.getPartBarcode());
                                r = deliveryPlanService.findUniqueByMap(Roll.class, map);
                            }
                            infos.add(r);
                        }
                    }
                }
            }
        } else {
            map.clear();
            map.put("partBarcode", trayBarcode);
            Roll r = deliveryPlanService.findUniqueByMap(Roll.class, map);
            infos.add(r);
        }
        return infos;
    }

    @NoLogin
    @Journal(name = "根据出货单id导出Excel")
    @ResponseBody
    @RequestMapping(value = "export1", method = RequestMethod.GET)
    public void export1(String ids) throws Exception {
        String idsArray[] = ids.split(",");
        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
        for (String idString : idsArray) {
            Double totalWeight = 0.0;
            ProductOutOrder poo = deliveryPlanService.findById(ProductOutOrder.class, Long.parseLong(idString));
            Sheet sheet = wb.createSheet(poo.getDeliveryCode() + "-" + poo.getPn());
            Row row = null;
            Cell cell = null;
            int i = 0;
            String columnName[] = new String[]{"行号", "托编号", "客户订单号", "批号",
                    "厂内产品名称", "客户产品名称", "门幅（mm）", "米长（m）", "托数", "套数",
                    "重量（KG）", "备注"};
            row = sheet.createRow(i);
            // 标题
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                if (a == 0) {
                    cell.setCellValue("浙江恒石纤维基业有限公司成品出库单");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 9));
                }
                if (a == columnName.length - 2) {
                    cell.setCellValue("Q/HS RHS0081-2015");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 10, 12));
                }
            }
            i++;
            // 日期和文件编号
            row = sheet.createRow(i);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                if (a == 0) {
                    cell.setCellValue("购货单位:");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
                }
                if (a == 2) {
                    cell.setCellValue(poo.getConsumerName());
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 2, 4));
                }
                if (a == 5) {
                    cell.setCellValue("日期：");
                }
                if (a == 6) {

                    cell.setCellValue(sdf.format(poo.getOutTime()));
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 6, 10));
                }
                if (a == 11) {
                    cell.setCellValue(poo.getDeliveryCode() + "-" + poo.getPn());
                }
            }
            i++;
            // 表头列名
            row = sheet.createRow(i);
            i++;
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName[a]);
            }
            int index = 0;

            HashMap<String, Object> map = new HashMap();
            map.put("packingNumber", poo.getPackingNumber());
            List<ProductOutRecord> porList = deliveryPlanService.findListByMap(ProductOutRecord.class, map);
            //总托数
            for (ProductOutRecord por : porList) {
                TrayBarCode tb;
                PartBarcode pb = null;
                //套数
                int tcount = 0;
                map.clear();
                map.put("barcode", por.getBarCode());
                tb = deliveryPlanService.findUniqueByMap(TrayBarCode.class, map);
                if (null == tb) {
                    pb = deliveryPlanService.findUniqueByMap(PartBarcode.class, map);
                }
                SalesOrderDetail salesOrderDetail;
                if (tb != null) {
                    salesOrderDetail = deliveryPlanService.findById(SalesOrderDetail.class, Long.valueOf(tb.getSalesOrderDetailId()));
                } else {
                    salesOrderDetail = deliveryPlanService.findById(SalesOrderDetail.class, Long.valueOf(pb.getSalesOrderDetailId()));
                }
                //判断是否是套材
                if (tb != null && tb.getPartId() != null && !"".equals(tb.getPartId())) {
                    //判断是成品胚布或常规部件
                    TcBomVersionParts part = deliveryPlanService.findById(TcBomVersionParts.class, tb.getPartId());
                    if ("成品胚布".equals(part.getTcProcBomVersionPartsType())) {//成品胚布
                        tcount += 1;//成品胚布一托为一套
                    } else {//常规部件
                        map.clear();
                        map.put("trayBarcode", por.getBarCode());
                        List<TrayBoxRoll> trays = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                        for (TrayBoxRoll tray : trays) {
                            if (tray.getBoxBarcode() != null && !"".equals(tray.getBoxBarcode())) {
                                map.clear();
                                map.put("boxBarcode", tray.getBoxBarcode());
                                List<TrayBoxRoll> boxs = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                if (boxs.size() > 0) {
                                    tcount += boxs.size();//套数为部件条码数量
                                }
                            } else {
                                if (trays.size() > 0) {
                                    tcount += trays.size();//套数为部件条码数量
                                    break;
                                }
                            }
                        }
                    }
                } else if (pb != null && pb.getPartId() != null && !"".equals(pb.getPartId())) {
                    map.clear();
                    map.put("rollBarcode", por.getBarCode());
                    List<TrayBoxRoll> trays = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                    if (trays.size() > 0) {
                        for (TrayBoxRoll tray : trays) {
                            if (tray.getBoxBarcode() != null && !"".equals(tray.getBoxBarcode())) {
                                map.clear();
                                map.put("boxBarcode", tray.getBoxBarcode());
                                List<TrayBoxRoll> boxs = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                if (boxs.size() > 0) {
                                    tcount += boxs.size();//套数为部件条码数量
                                }
                            } else {
                                if (trays.size() > 0) {
                                    tcount += trays.size();//套数为部件条码数量
                                    break;
                                }
                            }
                        }
                    } else {
                        tcount++;
                    }
                }

                index++;
                row = sheet.createRow(i);
                for (int a = 0; a < columnName.length; a++) {
                    cell = row.createCell(a);
                    cell.setCellStyle(cellStyle);
                    switch (a) {
                        case 0:
                            cell.setCellValue(index);
                            break;
                        case 1:
                            cell.setCellValue(por.getBarCode());
                            break;
                        case 2:
                            cell.setCellValue(salesOrderDetail.getSalesOrderSubCodePrint() == null ? "" : salesOrderDetail.getSalesOrderSubCodePrint());
                            break;
                        case 3:
                            if (null != tb) {
                                cell.setCellValue(tb.getBatchCode());
                            } else {
                                cell.setCellValue(pb.getBatchCode());
                            }
                            break;
                        case 4:
                            cell.setCellValue(por.getProductFactoryName());
                            break;
                        case 5:
                            cell.setCellValue(por.getProductConsumerName());
                            break;
                        case 6:
                            cell.setCellValue(por.getWidth() == null ? "" : por.getWidth() + "");
                            break;
                        case 7:
                            cell.setCellValue(por.getLength() == null ? "" : por.getLength() + "");
                            break;
                        case 8:
                            cell.setCellValue(1);
                            break;
                        case 9:
                            cell.setCellValue(tcount);
                            break;
                        case 10:
                            cell.setCellValue(por.getWeight() == null ? "" : por.getWeight() + "");
                            totalWeight = totalWeight + (por.getWeight() == null ? 0.0 : por.getWeight());
                            break;
                        case 11:
                            cell.setCellValue("");
                            break;
                        default:
                            break;
                    }
                }
                i++;
            }
            row = sheet.createRow(i);
            i++;
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                if (a == 0) {
                    cell.setCellValue("货运签字:");
                }
                if (a == 2) {
                    String plate = poo.getPlate();
                    if (plate != null && !plate.equals("")) {
                        cell.setCellValue("车牌:" + poo.getPlate());
                    } else {
                        map.clear();
                        map.put("packingNumber", poo.getPackingNumber());
                        DeliveryPlanSalesOrders dpso = deliveryPlanService.findUniqueByMap(DeliveryPlanSalesOrders.class, map);
                        plate = dpso.getPlate();
                        if (plate != null && !plate.equals("")) {
                            cell.setCellValue("车牌:" + plate);
                        } else {
                            cell.setCellValue("车牌:");
                        }
                    }
                }
                if (a == 5) {
                    cell.setCellValue("操作人:");
                }
                if (a == 6) {
                    User u = deliveryPlanService.findById(User.class, porList.get(0).getOperateUserId());
                    cell.setCellValue(u.getUserName());
                }
                if (a == 9) {
                    cell.setCellValue("合计:");
                }
                if (a == 10) {
                    cell.setCellValue(totalWeight);
                }
            }
        }
        HttpUtils.download(response,wb, sdf.format(date));
    }

    @NoLogin
    @Journal(name = "根据出货单id导出成品出库单汇总")
    @ResponseBody
    @RequestMapping(value = "export3", method = RequestMethod.GET)
    public void export3(String ids) throws Exception {
        String idsArray[] = ids.split(",");
        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setBorderBottom(BorderStyle.NONE);
        cellStyle1.setBorderTop(BorderStyle.NONE);
        cellStyle1.setBorderRight(BorderStyle.NONE);
        cellStyle1.setBorderLeft(BorderStyle.NONE);
        cellStyle1.setWrapText(true);
        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setBorderBottom(BorderStyle.NONE);
        cellStyle2.setBorderTop(BorderStyle.NONE);
        cellStyle2.setBorderRight(BorderStyle.NONE);
        cellStyle2.setBorderLeft(BorderStyle.NONE);
        cellStyle2.setWrapText(true);
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 14);// 设置字体大小
        cellStyle2.setFont(font);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
        for (String idString : idsArray) {
            Double totalWeight = 0.0;
            ProductOutOrder poo = deliveryPlanService.findById(ProductOutOrder.class, Long.parseLong(idString));
            DeliveryPlan deliveryPlan = deliveryPlanService.findOne(DeliveryPlan.class, "deliveryCode", poo.getDeliveryCode());
            Sheet sheet = wb.createSheet(poo.getDeliveryCode() + "-" + poo.getPn());
            Row row;
            Cell cell;
            int i = 0;
            String columnName[] = new String[]{"序号", "客户订单号", "批号", "厂内名称",
                    "客户名称", "部件名称", "数量(托)", "数量(套)", "数量(kg)", "样布信息", "备注", "出库人"};
            row = sheet.createRow(i);
            // 标题
            for (int a = 0; a < columnName.length; a++) {
                sheet.setColumnWidth(0, 6 * 256);// 设置列宽
                sheet.setColumnWidth(1, 8 * 256);
                sheet.setColumnWidth(2, 10 * 256);
                sheet.setColumnWidth(3, 18 * 256);
                sheet.setColumnWidth(4, 12 * 256);
                sheet.setColumnWidth(5, 6 * 256);
                sheet.setColumnWidth(6, 6 * 256);
                sheet.setColumnWidth(7, 6 * 256);
                sheet.setColumnWidth(8, 8 * 256);
                sheet.setColumnWidth(9, 12 * 256);
                sheet.setColumnWidth(10, 6 * 256);
                sheet.setColumnWidth(11, 6 * 256);
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle2);
                if (a == 0) {
                    cell.setCellValue("浙江恒石纤维基业有限公司成品出库单汇总");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 6));
                }
                if (a == columnName.length - 4) {
                    cell.setCellValue("Q/HS RHS0081-2015");
                    cell.setCellStyle(cellStyle1);
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 7, 10));
                }
            }
            i++;
            // 日期和文件编号
            row = sheet.createRow(i);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                if (a == 0) {
                    cell.setCellValue("购货单位:");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
                }
                if (a == 2) {
                    cell.setCellValue(poo.getConsumerName());
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 2, 3));
                }
                if (a == 4) {
                    cell.setCellValue("日期：");
                }
                if (a == 5) {
                    cell.setCellValue(sdf.format(poo.getOutTime()));
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 5, 6));
                }
                if (a == 7) {
                    cell.setCellValue(poo.getDeliveryCode() + "-" + poo.getPn());
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 7, 10));
                }
            }
            i++;
            // 表头列名
            row = sheet.createRow(i);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName[a]);
            }
            int index = 0;
            HashMap<String, Object> map = new HashMap();
            List<Map<String, Object>> porList = deliveryPlanService.findProductOutRecordByPackingNumber(poo.getPackingNumber());
            int tcounts = 0;//总套数
            int traycounts = 0;//总托数
            for (Map<String, Object> por : porList) {
                //套数
                int tcount = 0;
                int traycount = 0;
                String barCodes[] = por.get("BARCODES").toString().split(",");
                for (int j = 0; j < barCodes.length; j++) {
                    map.clear();
                    map.put("barcode", barCodes[j]);
                    TrayBarCode tb = deliveryPlanService.findUniqueByMap(TrayBarCode.class, map);
                    if (tb != null) {
                        //判断是否是套材
                        if (tb.getPartId() != null && !"".equals(tb.getPartId())) {
                            //判断是成品胚布或常规部件
                            TcBomVersionParts part = deliveryPlanService.findById(TcBomVersionParts.class, tb.getPartId());
                            if ("成品胚布".equals(part.getTcProcBomVersionPartsType())) {//成品胚布
                                tcount += 1;//成品胚布一托为一套
                                tcounts += 1;
                            } else {//常规部件
                                map.clear();
                                map.put("trayBarcode", barCodes[j]);
                                List<TrayBoxRoll> trays = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                for (TrayBoxRoll tray : trays) {
                                    if (tray.getBoxBarcode() != null && !"".equals(tray.getBoxBarcode())) {
                                        map.clear();
                                        map.put("boxBarcode", tray.getBoxBarcode());
                                        List<TrayBoxRoll> boxs = deliveryPlanService.findListByMap(TrayBoxRoll.class, map);
                                        if (boxs.size() > 0) {
                                            tcount += boxs.size();//套数为部件条码数量
                                            tcounts += boxs.size();
                                        }
                                    } else {
                                        if (trays.size() > 0) {
                                            tcount += trays.size();//套数为部件条码数量
                                            tcounts += trays.size();
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            //非套材增加 托数累加
                            traycount++;
                            traycounts++;
                        }
                    } else {
                        map.clear();
                        map.put("barcode", barCodes[j]);
                        PartBarcode partBarcode = deliveryPlanService.findUniqueByMap(PartBarcode.class, map);
                        if (partBarcode != null) {
                            tcount += 1;//pcj一个部件为一套
                            tcounts += 1;
                        }
                    }
                }
                // 总托数
                index++;

                i++;
                row = sheet.createRow(i);
                for (int a = 0; a < columnName.length; a++) {
                    cell = row.createCell(a);
                    cell.setCellStyle(cellStyle);
                    switch (a) {
                        case 0:
                            cell.setCellValue(index);
                            break;
                        case 1:
                            cell.setCellValue(por.get("SALESORDERSUBCODEPRINT") == null ? "" : por.get("SALESORDERSUBCODEPRINT").toString());
                            break;
                        case 2:
                            cell.setCellValue(por.get("BATCHCODE") == null ? "" : por.get("BATCHCODE").toString());
                            break;
                        case 3:
                            cell.setCellValue(por.get("PRODUCTFACTORYNAME") == null ? "" : por.get("PRODUCTFACTORYNAME").toString());
                            break;
                        case 4:
                            cell.setCellValue(por.get("PRODUCTCONSUMERNAME") == null ? "" : por.get("PRODUCTCONSUMERNAME").toString());
                            break;
                        case 5:
                            cell.setCellValue(por.get("PARTNAME") == null ? "" : por.get("PARTNAME").toString());
                            break;
                        case 6:
                            cell.setCellValue(String.valueOf(traycount) == null ? "" : String.valueOf(traycount));
                            break;
                        case 7:
                            cell.setCellValue(String.valueOf(tcount) == null ? "" : String.valueOf(tcount));
                            break;
                        case 8:
                            if (por.get("WEIGHTS") != null) {
                                String weights = String.format("%.1f", por.get("WEIGHTS"));// 保留一位小数
                                cell.setCellValue(weights);
                                totalWeight = totalWeight + (por.get("WEIGHTS") == null ? 0.0 : (Double) por.get("WEIGHTS"));
                                break;
                            } else {
                                cell.setCellValue("");
                            }
                        case 9:
                            cell.setCellValue(poo.getSampleInformation());
                            break;
                        case 10:
                            cell.setCellValue("");
                            break;
                        case 11:
                            cell.setCellValue(por.get("USERNAME") == null ? "" : por.get("USERNAME").toString());
                            break;
                    }
                }
            }

            i++;
            row = sheet.createRow(i);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                if (a == 0) {
                    cell.setCellValue("总计");
                } else if (a == 6) {
                    cell.setCellValue(traycounts);
                } else if (a == 7) {
                    cell.setCellValue(tcounts);
                } else if (a == 8) {
                    cell.setCellValue(totalWeight);
                }
            }

            i++;
            row = sheet.createRow(i);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                cell.setCellStyle(cellStyle);
                if (a == 0) {
                    cell.setCellValue("客户备注信息:");
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
                }
                if (a == 2) {
                    cell.setCellValue(deliveryPlan.getCustomerNotes());
                    sheet.addMergedRegion(new CellRangeAddress(i, i, 2, 3));
                }
            }

            i++;
            row = sheet.createRow(i);
            for (int a = 0; a < columnName.length; a++) {
                cell = row.createCell(a);
                if (a == 0) {
                    cell.setCellValue("货运 :");
                }
                if (a == 3) {
                    String plate = poo.getPlate();
                    if (plate != null && !plate.equals("")) {
                        cell.setCellValue("车牌:" + poo.getPlate());
                    } else {
                        map.clear();
                        map.put("packingNumber", poo.getPackingNumber());
                        DeliveryPlanSalesOrders dpso = deliveryPlanService.findUniqueByMap(DeliveryPlanSalesOrders.class, map);
                        plate = dpso.getPlate();
                        if (plate != null && !plate.equals("")) {
                            cell.setCellValue("车牌:" + plate);
                        } else {
                            cell.setCellValue("车牌:");
                        }
                    }
                }
                if (a == 6) {
                    cell.setCellValue("发货人:");
                }
                if (a == 8) {
                    User u = deliveryPlanService.findById(User.class, Long.parseLong(porList.get(0).get("OPERATEUSERID").toString()));
                    cell.setCellValue(u.getUserName());
                }
            }
        }
        HttpUtils.download(response,wb, sdf.format(date));
    }

    @ResponseBody
    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping(value = "downLoadImage", method = RequestMethod.GET)
    public void downLoadImage(String ids) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String downLoadFileUrl = deliveryPlanService.copyBarcodeImgs(ids, uuid);
        response.reset();
        File file = new File(downLoadFileUrl);
        String filename = file.getName();
        // 以流的形式下载文件。
        InputStream fis = new BufferedInputStream(new FileInputStream(downLoadFileUrl));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
        response.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
        file.deleteOnExit();
    }

    @ResponseBody
    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/batchCode")
    public String getBatchCode(String orderCode, Long productId, Long partId) {
        List<Map<String, Object>> list = deliveryPlanService.getBatchCodeCountBySalesOrderCode(orderCode, productId, partId);
        List<Map<String, Object>> combobox = new ArrayList();
        Map<String, Object> map;
        for (Map<String, Object> m : list) {
            map = new HashMap();
            map.put("t", m.get("BATCHCODE") + "*" + m.get("COUNT") + "托");
            map.put("v", m.get("BATCHCODE"));
            combobox.add(map);
        }
        return GsonTools.toJson(combobox);
    }
}

/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.sales.controller;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.common.service.IMessageCreateService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.planner.pack.entity.PackTask;
import com.bluebirdme.mes.planner.pack.service.IPackTaskService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.service.IProducePlanService;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import com.bluebirdme.mes.sales.service.IConsumerService;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBarCode;
import com.bluebirdme.mes.utils.HttpUtils;
import com.bluebirdme.mes.utils.MapUtils;
import com.google.common.base.CharMatcher;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 销售订单Controller
 *
 * @author 高飞
 * @Date 2016-10-13 11:06:42
 */
@Controller
@RequestMapping("/salesOrder")
@Journal(name = "销售订单")
public class SalesOrderController extends BaseController {
    // 销售订单页面
    final String index = "sales/salesOrder";
    final String calicoOrder = "sales/calicoOrder";
    final String addOrEdit = "sales/salesOrderAddOrEdit";
    final String audit = "sales/salesOrderAudit";
    final String outSalesOrder = "sales/outSalesOrder";
    final String quantity = "sales/salesQuantity";
    final String summaryMonthly = "sales/summaryMonthlyOrder";
    @Resource
    IProducePlanService producePlanService;
    @Resource
    IMessageCreateService msgCreateService;
    @Resource
    ISalesOrderService salesOrderService;
    @Resource
    IAuditInstanceService auditInstanceService;
    @Resource
    IConsumerService consumerService;
    @Resource
    IPackTaskService ptService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @Journal(name = "胚布订单")
    @RequestMapping(value = "calicoOrder", method = RequestMethod.GET)
    public String calicoOrder() {
        return calicoOrder;
    }

    @ResponseBody
    @Journal(name = "获取销售订单列表信息")
    @RequestMapping("list")
    public String getSalesOrder(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(salesOrderService.findPageInfo(filter, page));
    }

    @ResponseBody
    @Journal(name = "获取胚布订单列表信息")
    @RequestMapping("listcalico")
    public String getSalicoOrder(Filter filter, Page page) throws Exception {
        filter.set("export", "-1");
        return GsonTools.toJson(salesOrderService.findPageInfo1(filter, page));
    }

    @ResponseBody
    @Journal(name = "获取销售订单列表信息")
    @RequestMapping("list2")
    public String getSalesOrder2(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(salesOrderService.findPageInfo(filter, page));
    }

    @Journal(name = "添加销售订单页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(SalesOrder salesOrder) {
        Long userId = (Long) session.getAttribute(Constant.CURRENT_USER_ID);

        User u = salesOrderService.findById(User.class, userId);
        Department de = salesOrderService.findById(Department.class, u.getDid());
        if (de.getName().equals("国内销售部")) {
            salesOrder.setSalesOrderIsExport(1);
        } else {
            salesOrder.setSalesOrderIsExport(0);
        }
        return new ModelAndView(addOrEdit, model.addAttribute("salesOrder", salesOrder).addAttribute("details", "[]"));
    }

    @ResponseBody
    @Journal(name = "保存销售订单", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody SalesOrder salesOrder) throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("salesOrderCode", salesOrder.getSalesOrderCode());

        if (salesOrderService.has(SalesOrder.class, param)) {
            return ajaxError("订单编号重复:" + salesOrder.getSalesOrderCode());
        }
        salesOrder.setSalesOrderDate(new Date());
        // 校验产品对应的BOM是否可用
        List<SalesOrderDetail> ps = salesOrder.getDetails();

        TcBomVersion tbv = null;
        FtcBomVersion fbv = null;
        BCBomVersion bv = null;

        Map<String, String> repeat = new HashMap<String, String>();

        for (SalesOrderDetail p : ps) {
            String salesOrderSubCode = CharMatcher.anyOf("\t").trimFrom(p.getSalesOrderSubCode());
            String salesOrderSubCodePrint = CharMatcher.anyOf("\t").trimFrom(p.getSalesOrderSubCodePrint());
            p.setSalesOrderSubCode(salesOrderSubCode);
            p.setSalesOrderSubCodePrint(salesOrderSubCodePrint);
            if (repeat.get(p.getSalesOrderSubCode()) != null && repeat.get(p.getSalesOrderSubCode()).equals(p.getProductId() + "")) {
                return ajaxError("订单号重复:" + p.getSalesOrderSubCode());
            }
            FinishedProduct fp = salesOrderService.findById(FinishedProduct.class, p.getProductId());
            param.clear();
            param.put("productId", p.getProductId());
            param.put("closed", null);
            if (fp.getIsCommon() != null && fp.getIsCommon() == 0) {
                if (salesOrderService.has(SalesOrderDetail.class, param)) {
                    return ajaxError("试样产品下单重复:" + p.getSalesOrderSubCode());
                }
                List<Map<String,Object>> map = salesOrderService.selectSalesOrder(p.getSalesOrderSubCode(), p.getFactoryProductName());
                if (map.size() != 0){
                    return ajaxError("试样产品订单号和厂内名称的组合已存在，不能重复下单，订单号:" + p.getSalesOrderSubCode()+"厂内名称："+p.getFactoryProductName());
                }
            }
            repeat.put(p.getSalesOrderSubCode(), p.getProductId() + "");
            /*
             * if (p.getPackBomId() == null) { return ajaxError("[" +
             * p.getFactoryProductName() + "] 包装工艺为空，不能下单"); }
             */

            if (p.getProcBomId() == null) {
                return ajaxError("[" + p.getFactoryProductName() + "] 产品工艺为空，不能下单");
            }


            tbv = salesOrderService.findById(TcBomVersion.class, p.getProcBomId());
            fbv = salesOrderService.findById(FtcBomVersion.class, p.getProcBomId());
            if (fp.getProductIsTc() == 1) {
                bv = salesOrderService.findById(BCBomVersion.class, p.getPackBomId());
                if (bv != null && bv.getAuditState() != AuditConstant.RS.PASS) {
                    return ajaxError("[" + p.getFactoryProductName() + "] 包材BOM尚未审核通过或变更中，不能下单");
                }
            }


            if (p.getProductIsTc() == 1) {
                if (tbv != null && tbv.getAuditState() != AuditConstant.RS.PASS) {
                    return ajaxError("[" + p.getFactoryProductName() + "] 产品工艺BOM尚未审核通过或变更中，不能下单");
                }
            } else {
                if (fbv != null && fbv.getAuditState() != AuditConstant.RS.PASS) {
                    return ajaxError("[" + p.getFactoryProductName() + "] 产品工艺BOM尚未审核通过或变更中，不能下单");
                }
            }

            param.clear();

            param.put("salesOrderSubCode", p.getSalesOrderSubCode());
            param.put("productId", p.getProductId());
            param.put("closed", null);

            if (salesOrderService.has(SalesOrderDetail.class, param)) {
                return ajaxError("订单号重复:" + p.getSalesOrderSubCode());
            }

            param.put("closed", 0);

            if (salesOrderService.has(SalesOrderDetail.class, param)) {
                return ajaxError("订单号重复:" + p.getSalesOrderSubCode());
            }
        }

        // 校验产品对应的BOM是否可用结束

        /*
         * List<SalesOrderDetail> list = salesOrder.getDetails();
         *
         * param.clear(); boolean isError=false; String info="";
         */

        /*
         * for (SalesOrderDetail detail : list) { param.put("salesOrderSubCode",
         * detail.getSalesOrderSubCode()); if
         * (salesOrderService.isExist(SalesOrderDetail.class, param, true)) {
         * return GsonTools.toJson("订单号重复"); } if (detail.getProductIsTc() == 1
         * && detail.getProcBomId() == null) { isError=true;
         * info+=("套材产品"+detail
         * .getFactoryProductName()+"工艺版本"+detail.getProductProcessCode
         * ()+"无法匹配;"); } if (detail.getProductIsTc() != 1 &&
         * (detail.getProcBomId() == null)) { isError=true;
         * info+=("非套材产品"+detail
         * .getFactoryProductName()+"工艺版本"+detail.getProductProcessCode
         * ()+"无法匹配;"); } if (detail.getProductIsTc() != 1 &&
         * (detail.getPackBomId() == null)) { isError=true;
         * info+=("非套材产品"+detail
         * .getFactoryProductName()+"包装版本"+detail.getProductPackagingCode
         * ()+"无法匹配"); } } if(isError){ return ajaxError(info); }
         */

        salesOrder.setAuditState(0);
        salesOrderService.save(salesOrder);

        return GsonTools.toJson("保存成功");
    }

    @Journal(name = "编辑销售订单页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(SalesOrder salesOrder, String force) throws Exception {
        salesOrder = salesOrderService.findById(SalesOrder.class, salesOrder.getId());
        User u = salesOrderService.findById(User.class, salesOrder.getSalesOrderBizUserId());
        Consumer c = salesOrderService.findById(Consumer.class, salesOrder.getSalesOrderConsumerId());
        List<SalesOrderDetail> list = salesOrderService.getDetails(salesOrder.getId());
        List<Map<Object, Object>> details = new ArrayList<Map<Object, Object>>();
        for (SalesOrderDetail detail : list) {
            details.add(MapUtils.entityToMap(detail));
        }
        return new ModelAndView(addOrEdit, model.addAttribute("salesOrder", salesOrder).addAttribute("force", force).addAttribute("bizUserName", u == null ? "" : u.getUserName()).addAttribute("consumerName", c == null ? "" : c.getConsumerName()).addAttribute("details", GsonTools.toJson(details)));
    }

    @ResponseBody
    @Journal(name = "编辑销售订单", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(@RequestBody SalesOrder salesOrder) throws Exception {
        if (!salesOrder.getSalesOrderBizUserId().equals(session.getAttribute(Constant.CURRENT_USER_ID))) {
            return ajaxError("只能由下达订单的业务员提交审核");
        }
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("salesOrderCode", salesOrder.getSalesOrderCode());

        if (salesOrderService.has(SalesOrder.class, param, salesOrder.getId())) {
            return ajaxError("订单编号重复:" + salesOrder.getSalesOrderCode());
        }

        // 校验产品对应的BOM是否可用
        List<SalesOrderDetail> ps = salesOrder.getDetails();

        TcBomVersion tbv = null;
        FtcBomVersion fbv = null;
        BCBomVersion bv = null;

        Map<String, String> repeat = new HashMap<String, String>();

        for (SalesOrderDetail p : ps) {

            if (repeat.get(p.getSalesOrderSubCode()) != null && repeat.get(p.getSalesOrderSubCode()).equals(p.getProductId() + "")) {
                return ajaxError("订单号重复:" + p.getSalesOrderSubCode());
            }
            FinishedProduct fp = salesOrderService.findById(FinishedProduct.class, p.getProductId());
            param.clear();
            param.put("productId", p.getProductId());
            param.put("closed", null);
            /*
             * if(fp.getIsCommon()!=null&&fp.getIsCommon()==0){ if
             * (salesOrderService.has(SalesOrderDetail.class, param)) { return
             * ajaxError("试样产品下单重复:" + p.getSalesOrderSubCode()); } }
             */
            repeat.put(p.getSalesOrderSubCode(), p.getProductId() + "");
            if (salesOrder.getSalesOrderIsExport() == -1) {
                if (p.getProcBomId() == null) {
                    return ajaxError("[" + p.getFactoryProductName() + "] 产品工艺为空，不能下单");
                }
            } else {
                /*
                 * if (p.getPackBomId() == null) { return ajaxError("[" +
                 * p.getFactoryProductName() + "] 包装工艺为空，不能下单"); }
                 */
                if (p.getProcBomId() == null) {
                    return ajaxError("[" + p.getFactoryProductName() + "] 产品工艺为空，不能下单");
                }
            }
            /*
             * if (salesOrder.getSalesOrderIsExport() != -1) { bv =
             * salesOrderService.findById(BCBomVersion.class, p.getPackBomId());
             * }
             */
            tbv = salesOrderService.findById(TcBomVersion.class, p.getProcBomId());
            fbv = salesOrderService.findById(FtcBomVersion.class, p.getProcBomId());
            /*
             * if (salesOrder.getSalesOrderIsExport() != -1) { if (bv != null &&
             * bv.getAuditState() != AuditConstant.RS.PASS) { return
             * ajaxError("[" + p.getFactoryProductName() +
             * "] 包材BOM尚未审核通过或变更中，不能下单"); } }
             *
             * if(p.getProductIsTc()==1){ if (tbv != null && tbv.getAuditState()
             * != AuditConstant.RS.PASS) { return ajaxError("[" +
             * p.getFactoryProductName() + "] 产品工艺BOM尚未审核通过或变更中，不能下单"); } }else{
             * if (fbv != null && fbv.getAuditState() != AuditConstant.RS.PASS)
             * { return ajaxError("[" + p.getFactoryProductName() +
             * "] 产品工艺BOM尚未审核通过或变更中，不能下单"); } }
             */

            param.clear();

            param.put("salesOrderSubCode", p.getSalesOrderSubCode());
            param.put("productId", p.getProductId());
            param.put("closed", null);

            if (salesOrderService.has(SalesOrderDetail.class, param, p.getId())) {
                return ajaxError("订单号重复:" + p.getSalesOrderSubCode());
            }

            param.put("closed", 0);

            if (salesOrderService.has(SalesOrderDetail.class, param, p.getId())) {
                return ajaxError("订单号重复:" + p.getSalesOrderSubCode());
            }

        }

        salesOrder.setAuditState(0);
        salesOrderService.edit(salesOrder);

        return GsonTools.toJson("保存成功");
    }

    @ResponseBody
    @Journal(name = "强制变更订单", logType = LogType.DB)
    @RequestMapping(value = "forceEdit", method = RequestMethod.POST)
    public String edit(@RequestBody List<SalesOrderDetail> details) throws Exception {
        SalesOrderDetail old = salesOrderService.findById(SalesOrderDetail.class, details.get(0).getId());
        SalesOrder so = salesOrderService.findById(SalesOrder.class, old.getSalesOrderId());
        if (!so.getSalesOrderBizUserId().equals(session.getAttribute(Constant.CURRENT_USER_ID))) {
            return ajaxError("只能由下达订单的业务员提交审核");
        }
        salesOrderService.forceEdit(details, (Long) session.getAttribute(Constant.CURRENT_USER_ID));
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "删除销售订单", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            SalesOrder salesOrder = auditInstanceService.findById(SalesOrder.class, Long.valueOf(id[a]));
            if (salesOrder.getAuditState() > 0) {
                return ajaxError("审核中和审核通过的记录不能删除！");
            }
        }
        salesOrderService.deleteSalesOrder(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "复制销售订单信息", logType = LogType.DB)
    @RequestMapping(value = "copy", method = RequestMethod.GET)
    public String copy(String id) throws Exception {
        SalesOrder oldSalesOrder = salesOrderService.findById(SalesOrder.class, Long.parseLong(id));
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("salesOrderId", oldSalesOrder.getId());
        map.put("closed", 1);
        List<SalesOrderDetail> li = salesOrderService.findListByMap(SalesOrderDetail.class, map);
        if (li.size() == 0) {
            return ajaxError("此订单中没有关闭的子订单");
        }
        salesOrderService.copy(id);
        return Constant.AJAX_SUCCESS;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取销售订单明细信息")
    @RequestMapping("product")
    public String getSalesOrder(Long orderId) throws Exception {
        SalesOrder order = salesOrderService.findById(SalesOrder.class, orderId);
        return GsonTools.toJson(salesOrderService.getDetails2(order.getSalesOrderCode()));
    }

    @ResponseBody
    @Journal(name = "提交审核销售订单", logType = LogType.DB)
    @RequestMapping(value = "commitAudit", method = RequestMethod.POST)
    public String _commitAudit(Long id, String name, String userId) throws Exception {
        SalesOrder so = auditInstanceService.findById(SalesOrder.class, id);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("salesOrderCode",so.getSalesOrderCode());
        List<SalesOrder> salesOrderList = auditInstanceService.findListByMap(SalesOrder.class, map);
        if (salesOrderList.size()>1){
            return ajaxError("销售订单编号重复下单，请确认后，删除重复的订单信息");
        }
        if (!so.getSalesOrderBizUserId().equals(session.getAttribute(Constant.CURRENT_USER_ID))) {
            return ajaxError("只能由下达订单的业务员提交审核");
        }

        // 检查包装任务
        String rs = salesOrderService.hasPackTask(id);
        if (!StringUtils.isBlank(rs)) {
            return ajaxError("订单号:" + rs + "尚未设置包装任务");
        }

        if (so.getSalesOrderIsExport() == 0) {
            auditInstanceService.submitAudit(name, "XS2", (Long) session.getAttribute(Constant.CURRENT_USER_ID), "salesOrder/audit?id=" + id, Long.valueOf(id), SalesOrder.class);
        } else if (so.getSalesOrderIsExport() == 1) {
            auditInstanceService.submitAudit(name, "XS1", (Long) session.getAttribute(Constant.CURRENT_USER_ID), "salesOrder/audit?id=" + id, Long.valueOf(id), SalesOrder.class);
        } else if (so.getSalesOrderIsExport() == -1) {//胚布订单提交
            List<ProducePlan> listproducePlan = auditInstanceService.find(ProducePlan.class, "producePlanCode", so.getFromProducePlancode());
            if (listproducePlan != null && listproducePlan.size() > 0) {
                auditInstanceService.submitAudit(name, "PBOrder" + listproducePlan.get(0).getWorkShopCode().trim(), (Long) session.getAttribute(Constant.CURRENT_USER_ID), "salesOrder/audit?id=" + id, Long.valueOf(id), SalesOrder.class);
            } else {
                auditInstanceService.submitAudit(name, "XS3", (Long) session.getAttribute(Constant.CURRENT_USER_ID), "salesOrder/audit?id=" + id, Long.valueOf(id), SalesOrder.class);
            }
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "获取编号", logType = LogType.DB)
    @RequestMapping(value = "serial", method = RequestMethod.POST)
    public String serial(Integer export) throws Exception {
        return salesOrderService.getSerial(export);
    }

    @Journal(name = "销售审核页面")
    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public ModelAndView audit(String id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLTemplateException {
        SalesOrder salesOrder = salesOrderService.findById(SalesOrder.class, Long.valueOf(id));
        User u = salesOrderService.findById(User.class, salesOrder.getSalesOrderBizUserId());
        Consumer c = salesOrderService.findById(Consumer.class, salesOrder.getSalesOrderConsumerId());
        /*
         * List<SalesOrderDetail> list =
         * salesOrderService.getDetails(salesOrder.getId()); List<Map<Object,
         * Object>> details = new ArrayList<Map<Object, Object>>(); for
         * (SalesOrderDetail detail : list) {
         * details.add(MapUtils.entityToMap(detail)); }
         */
        return new ModelAndView(audit, model.addAttribute("salesOrder", salesOrder).addAttribute("userName", u.getUserName()).addAttribute("consumerName", c.getConsumerName()).addAttribute("details", GsonTools.toJson(salesOrderService.getDetails2(salesOrder.getSalesOrderCode()))));
    }

    @Journal(name = "关闭订单", logType = LogType.DB)
    @RequestMapping("close")
    @ResponseBody
    public String closeSalesOrder(String _ids) throws Exception {
        String ids[] = _ids.split(",");
        SalesOrder[] orders = new SalesOrder[ids.length];
        SalesOrder order;
        for (int i = 0; i < ids.length; i++) {
            order = new SalesOrder();
            order.setId(Long.parseLong(ids[i]));
            order.setIsClosed(1);
            orders[i] = order;
        }
        salesOrderService.update2(orders);
        return ajaxSuccess();
    }

    @Journal(name = "完成订单", logType = LogType.DB)
    @RequestMapping("complete")
    @ResponseBody
    public String complateSalesOrder(String _ids) throws Exception {
        String ids[] = _ids.split(",");
        SalesOrderDetail[] orders = new SalesOrderDetail[ids.length];
        SalesOrderDetail order;
        for (int i = 0; i < ids.length; i++) {
            order = salesOrderService.findById(SalesOrderDetail.class, Long.parseLong(ids[i]));
            order.setIsComplete(1);
            orders[i] = order;
            msgCreateService.createOrderFinish(order);
        }
        salesOrderService.update2(orders);
        return ajaxSuccess();
    }

    @Journal(name = "分配完成", logType = LogType.DB)
    @RequestMapping("isPlaned")
    @ResponseBody
    public String isPlanedSalesOrder(String _ids) throws Exception {
        String ids[] = _ids.split(",");
        SalesOrderDetail[] orders = new SalesOrderDetail[ids.length];
        SalesOrderDetail order;
        for (int i = 0; i < ids.length; i++) {
            order = salesOrderService.findById(SalesOrderDetail.class, Long.parseLong(ids[i]));
            order.setIsPlaned(1);
            orders[i] = order;
            //msgCreateService.createOrderFinish(order);
        }
        salesOrderService.update2(orders);
        return ajaxSuccess();
    }

    @NoLogin
    @Journal(name = "导出销售订单")
    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(Long id) throws Exception {
        SalesOrder salesOrder = salesOrderService.findById(SalesOrder.class, id);
        User user = salesOrderService.findById(User.class, salesOrder.getSalesOrderBizUserId());
        Consumer consumer = salesOrderService.findById(Consumer.class, salesOrder.getSalesOrderConsumerId());
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("salesOrderId", id);
        List<SalesOrderDetail> details = salesOrderService.findListByMap(SalesOrderDetail.class, map);
        // TODO遍历主订单明细，获取增加的明细

        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "生产计划安排单(" + salesOrder.getSalesOrderCode() + ")";
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        SXSSFWorkbook wb = new SXSSFWorkbook(-1);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);

        Sheet sheet = wb.createSheet("订单");
        // sheet.setDisplayGridlines(true);
        Row row = null;
        Cell cell = null;
        String columnName[] = new String[]{"序号", "客户产品名称", "厂内产品名称", "客户订单号", "厂内订单号", "批号", "具体规格/牌号", "门幅(mm)", "产品数量(卷/套)", "产品卷重（卷长）", "总重(kg)", "包装代码", "工艺代码", "交货日期", "产品类型选项", "备注"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 16; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("生产计划安排单");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 16; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 15));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("销售订单编号：" + salesOrder.getSalesOrderCode());
        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.RIGHT); // 水平布局：居中
        // cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle1.setBorderBottom(BorderStyle.THIN);
        cellStyle1.setBorderTop(BorderStyle.THIN);
        cellStyle1.setBorderRight(BorderStyle.THIN);
        cellStyle1.setBorderLeft(BorderStyle.THIN);
        cell.setCellStyle(cellStyle1);
        for (int i = 1; i < 16; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle1);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 15));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("客户名称：" + consumer.getConsumerName());
        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.LEFT); // 水平布局：居中
        // cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        cell.setCellStyle(cellStyle2);
        for (int i = 1; i < 16; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 15));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("业务员:");
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle2);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
        cell = row.createCell(2);
        cell.setCellValue(user.getUserName());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 3));
        cell = row.createCell(4);
        cell.setCellValue("下单日期：" + sdf.format(salesOrder.getSalesOrderDate()));
        cell.setCellStyle(cellStyle2);
        for (int a = 5; a < 8; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 4, 7));
        cell = row.createCell(8);
        cell.setCellValue("发货：" + (salesOrder.getSalesOrderIsExport() == 0 ? "外销" : "内销"));
        cell.setCellStyle(cellStyle2);
        for (int a = 8; a < 15; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 8, 15));
        r++;
        row = sheet.createRow(r);
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("产品类型:");
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle2);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
        cell = row.createCell(2);

        switch (salesOrder.getSalesOrderType()) {
            case 3:
                cell.setCellValue("新产品");
                break;
            case 2:
                cell.setCellValue("试样产品");
                break;
            case 1:
                cell.setCellValue("常规产品");
                break;

            default:
                cell.setCellValue("未知产品");
                break;
        }
        cell.setCellStyle(cellStyle);
        for (int a = 3; a < 16; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle);
        }

        sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 15));
        r++;
        row = sheet.createRow(r);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;

        // String productTypeNow = null;
        // 遍历数据的长度，得到递增的行数
        int index = 0;
        Double totalCount = 0d;
        for (SalesOrderDetail data : details) {
            index++;
            // SalesOrderDetail sod =
            // producePlanService.findById(SalesOrderDetail.class,
            // data.getFromSalesOrderDetailId());
            // SalesOrder so = producePlanService.findById(SalesOrder.class,
            // sod.getSalesOrderId());
            row = sheet.createRow(r);
            // 当产品型号和原来的产品型号不同时，记录现在的型号并插入一行型号
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        cell.setCellValue(index);
                        break;
                    case 1:
                        if (data.getConsumerProductName() != null) {
                            cell.setCellValue(data.getConsumerProductName());
                        }
                        break;
                    case 2:
                        if (data.getFactoryProductName() != null) {
                            cell.setCellValue(data.getFactoryProductName());
                        }
                        break;
                    case 3:
                        if (data.getSalesOrderSubCodePrint() != null && !"".equals(data.getSalesOrderSubCodePrint().trim())) {
                            cell.setCellValue(data.getSalesOrderSubCodePrint());
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 4:
                        if (data.getSalesOrderId() != null && !"".equals(data.getSalesOrderSubCode().trim())) {
                            cell.setCellValue(data.getSalesOrderSubCode());
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 5:
                        if (data.getProductBatchCode() != null && !"".equals(data.getProductBatchCode().trim())) {
                            cell.setCellValue(data.getProductBatchCode());
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 6:
                        if (data.getProductModel() != null) {
                            cell.setCellValue(data.getProductModel());
                        }
                        break;
                    case 7:
                        if (data.getProductWidth() != null) {
                            cell.setCellValue(data.getProductWidth().toString());
                        }
                        break;
                    case 8:
                        if (data.getProductCount() != null) {
                            cell.setCellValue(data.getProductCount());
                        }
                        totalCount += data.getProductCount();
                        break;
                    case 9:
                        if (data.getProductRollWeight() != null) {
                            cell.setCellValue(data.getProductRollWeight() + " kg");
                        }

                        if (data.getProductRollLength() != null) {
                            cell.setCellValue(data.getProductRollLength() + " m");
                        }

                        if (data.getProductRollLength() == null && data.getProductRollWeight() == null) {
                            cell.setCellValue("");
                        }

                        break;
                    case 10:
                        if (data.getTotalWeight() != null) {
                            cell.setCellValue(data.getTotalWeight());
                        }
                        break;
                    case 11:
                        if (data.getProductPackagingCode() != null) {
                            cell.setCellValue(data.getProductPackagingCode() + " " + data.getProductPackageVersion());
                        }
                        break;
                    case 12:
                        if (data.getProductProcessCode() != null) {
                            cell.setCellValue(data.getProductProcessCode() + " " + data.getProductProcessBomVersion());
                        }
                        break;
                    case 13:
                        if (data.getDeliveryTime() != null) {
                            cell.setCellValue(sdf.format(data.getDeliveryTime()));
                        }
                        break;

                    case 14:
                        // （3新品，2试样，1常规产品，-1未知）
                        if (salesOrder.getSalesOrderType().intValue() == 1) {
                            cell.setCellValue("常规产品");
                            break;
                        }

                        if (salesOrder.getSalesOrderType().intValue() == 2) {
                            cell.setCellValue("试样");
                            break;
                        }

                        if (salesOrder.getSalesOrderType().intValue() == 3) {
                            cell.setCellValue("新品");
                            break;
                        }

                        cell.setCellValue(" ");

                        break;

                    case 15:
                        if (data.getProductMemo() != null) {
                            cell.setCellValue(data.getProductMemo());
                        }
                        break;
                }
            }

            if (data.getProductIsTc().intValue() == 1) {

                String[] partNames = {"部件名称", "订单数量", "BOM数量"};

                row = sheet.createRow(++r);

                cell = row.createCell(1);
                cell.setCellValue(partNames[0]);
                cell = row.createCell(2);
                cell.setCellValue(partNames[1]);
                cell = row.createCell(3);
                cell.setCellValue(partNames[2]);

                map.clear();
                map.put("salesOrderDetailId", data.getId());
                List<SalesOrderDetailPartsCount> list = salesOrderService.findListByMap(SalesOrderDetailPartsCount.class, map);
                for (SalesOrderDetailPartsCount count : list) {
                    row = sheet.createRow(++r);
                    cell = row.createCell(1);
                    cell.setCellValue(count.getPartName());
                    cell = row.createCell(2);
                    cell.setCellValue(count.getPartCount());
                    cell = row.createCell(3);
                    cell.setCellValue(count.getPartBomCount());
                }
            }
            r++;
        }
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("总计：");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
        for (int a = 2; a < 15; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle);
            if (a == 8) {
                cell.setCellValue(totalCount);
            }
        }
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("接单人：");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 1));
        cell = row.createCell(2);
        cell.setCellValue("");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 2, 3));
        for (int a = 4; a < 8; a++) {
            cell = row.createCell(a);
            if (a == 4) {
                cell.setCellValue("部门经理审核：");
            }
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 4, 7));
        for (int a = 8; a < 15; a++) {
            cell = row.createCell(a);
            if (a == 8) {
                cell.setCellValue("工艺部/研发部审核：");
            }
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 8, 14));
        r++;
        row = sheet.createRow(r);
        for (int a = 0; a < 15; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle);
            if (a == 0) {
                cell.setCellValue("分管领导审核：");
            }

        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(r, r, 4, 14));
        r++;
        row = sheet.createRow(r);
        for (int a = 0; a < 4; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle);
            if (a == 0) {
                cell.setCellValue("销售订单备注：");
            }

        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 3));
        for (int a = 4; a < 15; a++) {
            cell = row.createCell(a);
            cell.setCellStyle(cellStyle);
            if (a == 4) {
                cell.setCellValue(salesOrder.getSalesOrderMemo());
            }

        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 4, 14));

        int mm = sheet.getLastRowNum();
        for (int x = 5; x <= mm - 2; x++) {
            row = sheet.getRow(x);
            int nn = row.getLastCellNum();

            for (int y = 0; y < nn; y++) {
                cell = row.getCell(y);
                if (cell == null) {
                    continue;
                }
                cell.setCellStyle(cellStyle);
            }
        }

        sheet.createFreezePane(0, 7);

        // 高飞，2018-01-05，导出时添加包装任务信息

        List<PackTask> tasks = null;

        Set<Long> vids = new HashSet<Long>();

        sheet = wb.createSheet("包装任务");
        Object[] packTaskTitles = {"订单号", "批次号", "客户产品名称", "厂内名称", "包装代码", "版本号", "备注", "每托卷数", "数量"};
        row = sheet.createRow(0);
        writeRow(row, packTaskTitles);
        int i = 1;
        // 包装任务 END

        for (SalesOrderDetail sod : details) {
            if (sod.getProductIsTc() != 2) continue;
            //i=1;
            map.clear();
            map.put("sodId", sod.getId());
            tasks = ptService.findListByMap(PackTask.class, map);
            for (PackTask t : tasks) {
                vids.add(t.getVid());
                row = sheet.createRow(i++);
                Object[] v = {sod.getSalesOrderSubCode(), sod.getProductBatchCode(), sod.getConsumerProductName(), sod.getFactoryProductName(), t.getCode(), t.getVersion(), t.getMemo(), t.getRollsPerTray(), t.getTotalCount()};
                writeRow(row, v);
            }

        }
        HttpUtils.download(response,wb,templateName);
    }

    void writeRow(Row row, Object... values) {
        int i = 0;
        for (Object v : values) {
            if (v == null) {
                v = "";
            }
            row.createCell(i++).setCellValue(v + "");
        }
    }

    @ResponseBody
    @Journal(name = "根据人员查看部门", logType = LogType.DB)
    @RequestMapping(value = "checkDp", method = RequestMethod.GET)
    public String checkDp(String userId) throws Exception {
        User user = auditInstanceService.findById(User.class, Long.valueOf(userId));
        Department department = auditInstanceService.findById(Department.class, user.getDid());
        String auditCode = "";
        if (department.getName().equals("国内销售部")) {
            auditCode = "XS1";
        } else if (department.getName().equals("国外销售部")) {
            auditCode = "XS2";
        } else if (department.getName().equals("裁剪车间")) {
            auditCode = "XS3";
        } else {
            return ajaxError("该业务员不在销售部，无法查看审核");
        }
        return GsonTools.toJson(auditCode);
    }

    @ResponseBody
    @Journal(name = "更新状态", logType = LogType.DB)
    @RequestMapping(value = "finish", method = RequestMethod.POST)
    @Valid
    public String finish(String id) throws Exception {
        salesOrderService.update(id);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "取消关闭订单", logType = LogType.DB)
    @RequestMapping(value = "cancelclose", method = RequestMethod.POST)
    @Valid
    public String cancelclose(String ids) throws Exception {
        String[] _ids = ids.split(",");
        for (String id : _ids) {
            Long _id = Long.parseLong(id);
            SalesOrderDetail s = salesOrderService.findById(SalesOrderDetail.class, _id);
            s.setClosed(null);
            salesOrderService.update(s);
        }
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "关闭销售计划查看生产计划是否关闭", logType = LogType.DB)
    @RequestMapping(value = "wclose", method = RequestMethod.GET)
    public String wclose(String ids) throws Exception {
        String[] _ids = ids.split(",");
        Map<String, Object> map = new HashMap<String, Object>();
        for (String id : _ids) {
            map.clear();
            map.put("fromSalesOrderDetailId", Long.parseLong(id));
            List<ProducePlanDetail> findListByMap = salesOrderService.findListByMap(ProducePlanDetail.class, map);
            for (int i = 0; i < findListByMap.size(); i++) {
                if (findListByMap.get(i).getClosed() == null || findListByMap.get(i).getClosed() == 0) {
                    return ajaxError("只有先关闭生产计划后方可操作");
                }
            }
        }
        return "1";
    }

    @Journal(name = "销售出库明细")
    @RequestMapping(value = "outSalesOrder", method = RequestMethod.GET)
    public String outSalesOrder() {
        return outSalesOrder;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取销售出库明细列表信息")
    @RequestMapping("outlist")
    public String getSalesOrderOut(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageOut = salesOrderService.findPageOut(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) findPageOut.get("rows");
        Map<String, Object> map = new HashMap<String, Object>();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                map.clear();
                map.put("salesOrderSubCode", list.get(i).get("SALESORDERCODE"));
                map.put("productModel", list.get(i).get("PRODUCTMODEL"));
                List<SalesOrderDetail> findListByMap = salesOrderService.findListByMap(SalesOrderDetail.class, map);
                if (findListByMap.size() > 0) {
                    SalesOrder findById = salesOrderService.findById(SalesOrder.class, findListByMap.get(0).getSalesOrderId());
                    User user = salesOrderService.findById(User.class, findById.getSalesOrderBizUserId());
                    list.get(i).put("SALESNAME", user.getUserName());
                }
            }
        }
        return GsonTools.toJson(findPageOut);
    }

    @NoLogin
    @Journal(name = "导出销售出货Excel明细")
    @ResponseBody
    @RequestMapping(value = "export1", method = RequestMethod.GET)
    public void export1(Filter filter) throws Exception {
        Page page = new Page();
        page.setPage(1);
        page.setRows(99999);
        Map<String, Object> findPageOut = salesOrderService.findPageOut(filter, page);
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageOut.get("rows");
        Map<String, Object> map = new HashMap<String, Object>();
        if (rows.size() > 0) {
            for (int i = 0; i < rows.size(); i++) {
                map.clear();
                map.put("salesOrderSubCode", rows.get(i).get("SALESORDERCODE"));
                map.put("productModel", rows.get(i).get("PRODUCTMODEL"));
                List<SalesOrderDetail> findListByMap = salesOrderService.findListByMap(SalesOrderDetail.class, map);
                if (findListByMap.size() > 0) {
                    SalesOrder findById = salesOrderService.findById(SalesOrder.class, findListByMap.get(0).getSalesOrderId());
                    User user = salesOrderService.findById(User.class, findById.getSalesOrderBizUserId());
                    rows.get(i).put("SALESNAME", user.getUserName());
                }
            }
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "销售出库明细表";
        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        Sheet sheet = wb.createSheet();
        Integer xx = null;
        Row row = null;
        Cell cell = null;
        String columnName[] = new String[]{"出货单编号", "出库日期", "购货单位", "订单号", "批次号", "部件名称", "客户产品名称", "厂内名称", "规格型号", "辅助数量", "辅助单位", "实发数量(kg)", "发货人", "业务员"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 14; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;
        sheet.setColumnWidth(0, 15 * 256);// 设置列宽
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 25 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 30 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        sheet.setColumnWidth(12, 10 * 256);

        row = sheet.createRow(r);
        cell = row.createCell(0);
        for (int i = 0; i < rows.size(); i++) {
            row = sheet.createRow(r);
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        if (rows.get(i).get("DEVICECODE") != null) {
                            cell.setCellValue(rows.get(i).get("DEVICECODE").toString());
                        }
                        break;
                    case 1:
                        if (rows.get(i).get("OUTTIME") != null) {
                            cell.setCellValue(sf.format(rows.get(i).get("OUTTIME")));
                        }
                        break;
                    case 2:
                        if (rows.get(i).get("CONSUMERNAME") != null) {
                            cell.setCellValue(rows.get(i).get("CONSUMERNAME").toString());
                        }
                        break;
                    case 3:
                        if (rows.get(i).get("SALESORDERCODE") != null) {
                            cell.setCellValue(rows.get(i).get("SALESORDERCODE").toString());
                        }
                        break;
                    case 4:
                        if (rows.get(i).get("BATCHCODE") != null) {
                            cell.setCellValue(rows.get(i).get("BATCHCODE").toString());
                        }
                        break;
                    case 5:

                        if (rows.get(i).get("TCPROCBOMVERSIONPARTSNAME") != null) {
                            cell.setCellValue(rows.get(i).get("TCPROCBOMVERSIONPARTSNAME").toString());
                        }
                        break;
                    case 6:
                        if (rows.get(i).get("PRODUCTCONSUMERNAME") != null) {
                            cell.setCellValue(rows.get(i).get("PRODUCTCONSUMERNAME").toString());
                        }
                        break;
                    case 7:
                        if (rows.get(i).get("PRODUCTFACTORYNAME") != null) {
                            cell.setCellValue(rows.get(i).get("PRODUCTFACTORYNAME").toString());
                        }
                        break;
                    case 8:
                        if (rows.get(i).get("PRODUCTMODEL") != null) {
                            cell.setCellValue(rows.get(i).get("PRODUCTMODEL").toString());
                        }
                        break;
                    case 9:
                        if (rows.get(i).get("COUNT") != null) {
                            cell.setCellValue(rows.get(i).get("COUNT").toString());
                        }
                        break;
                    case 10:

                        cell.setCellValue("托");
                        break;
                    case 11:
                        if (rows.get(i).get("ROLLWEIGHT") != null) {
                            cell.setCellValue((Double) rows.get(i).get("ROLLWEIGHT"));
                        }
                        break;
                    case 12:
                        if (rows.get(i).get("OPERATEUSERNAME") != null) {
                            cell.setCellValue(rows.get(i).get("OPERATEUSERNAME").toString());
                        }
                        break;
                    case 13:
                        if (rows.get(i).get("SALESNAME") != null) {
                            cell.setCellValue(rows.get(i).get("SALESNAME").toString());
                        }
                        break;
                }
            }

            r++;
        }
        row = sheet.createRow(r);
        cell = row.createCell(0);
        HttpUtils.download(response,wb,templateName);
    }

    @Journal(name = "销售下单数量统计")
    @RequestMapping(value = "salesQuantity", method = RequestMethod.GET)
    public String salesQuantity() {
        return quantity;
    }

    @SuppressWarnings("unchecked")
    @NoAuth
    @ResponseBody
    @Journal(name = "获取销售下单数量统计列表信息")
    @RequestMapping("Quantitylist")
    public String getsalesQuantity(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageQuantity = salesOrderService.findPageQuantity(filter, page);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageQuantity.get("rows");
        DecimalFormat df = new DecimalFormat("#.00");
        if (rows.size() != 0) {
            page.setAll(1);
            Map<String, Object> map2 = salesOrderService.findPageQuantity(filter, page);
            List<Map<String, Object>> r = (List<Map<String, Object>>) map2.get("rows");
            Double d1 = 0.0;
            Double d2 = 0.0;
            Double d3 = 0.0;
            Double d4 = 0.0;
            Double d5 = 0.0;
            for (int i = 0; i < r.size(); i++) {
                Double _d1 = (Double) r.get(i).get("TWEIGHT");
                d1 += _d1 == null ? 0.0 : _d1;
                Double _d2 = (Double) r.get(i).get("RW");
                d2 += _d2 == null ? 0.0 : _d2;
                Double _d3 = (Double) r.get(i).get("PW");
                d3 += _d3 == null ? 0.0 : _d3;
                Double _d4 = (Double) r.get(i).get("WRW");
                d4 += _d4 == null ? 0.0 : _d4;
                Double _d5 = (Double) r.get(i).get("WPW");
                d5 += _d5 == null ? 0.0 : _d5;
            }
            Double w1 = Double.parseDouble(df.format(d1));
            Double w2 = Double.parseDouble(df.format(d2));
            Double w3 = Double.parseDouble(df.format(d3));
            Double w4 = Double.parseDouble(df.format(d4));
            Double w5 = Double.parseDouble(df.format(d5));
            map.put("CONSUMERPRODUCTNAME", "总计");
            map.put("TWEIGHT", w1);
            map.put("RW", w2);
            map.put("PW", w3);
            map.put("WRW", w4);
            map.put("WPW", w5);
            list.add(map);
        } else {
            map.put("CONSUMERPRODUCTNAME", "总重量(kg)");
            map.put("TWEIGHT", 0);
            map.put("RW", 0);
            map.put("PW", 0);
            map.put("WRW", 0);
            map.put("WPW", 0);
            list.add(map);
        }
        findPageQuantity.put("footer", list);
        return GsonTools.toJson(findPageQuantity);
    }

    @NoLogin
    @Journal(name = "导出销售下单统计Excel明细")
    @ResponseBody
    @RequestMapping(value = "export2", method = RequestMethod.GET)
    public void export2(Filter filter) throws Exception {
        Page page = new Page();
        page.setPage(1);
        page.setRows(99999);
        Map<String, Object> findPageOut = salesOrderService.findPageQuantity(filter, page);
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageOut.get("rows");
        if (rows.size() > 0) {
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).get("RW") != null && rows.get(i).get("PW") != null) {
                    rows.get(i).put("PRODUCTIONWEIGHT", (Double) rows.get(i).get("RW") + (Double) rows.get(i).get("PW"));
                } else if (rows.get(i).get("RW") != null) {
                    rows.get(i).put("PRODUCTIONWEIGHT", rows.get(i).get("RW"));
                } else {
                    rows.get(i).put("PRODUCTIONWEIGHT", rows.get(i).get("PW"));
                }
                if (rows.get(i).get("WRW") != null && rows.get(i).get("WPW") != null) {
                    rows.get(i).put("OUTWEIGHT", (Double) rows.get(i).get("WRW") + (Double) rows.get(i).get("WPW"));
                } else if (rows.get(i).get("WRW") != null) {
                    rows.get(i).put("OUTWEIGHT", rows.get(i).get("WRW"));
                } else {
                    rows.get(i).put("OUTWEIGHT", rows.get(i).get("WPW"));
                }
                if (rows.get(i).get("PRODUCTIONWEIGHT") != null) {
                    if (rows.get(i).get("TWEIGHT") != null) {
                        if ((Double) rows.get(i).get("TWEIGHT") - (Double) rows.get(i).get("PRODUCTIONWEIGHT") > 0) {
                            rows.get(i).put("PRWEIGHT", (Double) rows.get(i).get("TWEIGHT") - (Double) rows.get(i).get("PRODUCTIONWEIGHT"));
                        } else {
                            rows.get(i).put("PRWEIGHT", 0.0);
                        }
                    } else {
                        rows.get(i).put("PRWEIGHT", 0.0);
                    }
                } else {
                    rows.get(i).put("PRWEIGHT", rows.get(i).get("TWEIGHT"));
                }
            }
        }

        String templateName = "销售下单统计表";
        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        Sheet sheet = wb.createSheet();
        Integer xx = null;
        Row row = null;
        Cell cell = null;
        String columnName[] = new String[]{"客户名称", "厂内名称", "客户产品名称", "下单总量(kg)", "已生产总量(kg)", "未生产总量(kg)", "已出库总量(kg)"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 6; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;
        sheet.setColumnWidth(0, 30 * 256);// 设置列宽
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        row = sheet.createRow(r);
        cell = row.createCell(0);
        for (int i = 0; i < rows.size(); i++) {
            row = sheet.createRow(r);
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        cell.setCellValue(rows.get(i).get("CONSUMERNAME").toString());
                        break;
                    case 1:
                        cell.setCellValue(rows.get(i).get("FACTORYPRODUCTNAME").toString());
                        break;
                    case 2:
                        cell.setCellValue(rows.get(i).get("CONSUMERPRODUCTNAME").toString());
                        break;
                    case 3:
                        if (rows.get(i).get("TWEIGHT") != null) {
                            cell.setCellValue((Double) rows.get(i).get("TWEIGHT"));
                        } else {
                            cell.setCellValue("0.00");

                        }

                        break;
                    case 4:
                        if (rows.get(i).get("PRODUCTIONWEIGHT") != null) {
                            cell.setCellValue((Double) rows.get(i).get("PRODUCTIONWEIGHT"));
                        } else {
                            cell.setCellValue("0.00");

                        }
                        break;
                    case 5:
                        if (rows.get(i).get("PRWEIGHT") != null) {
                            cell.setCellValue((Double) rows.get(i).get("PRWEIGHT"));
                        } else {
                            cell.setCellValue("0.00");
                        }
                        break;
                    case 6:
                        if (rows.get(i).get("OUTWEIGHT") != null) {
                            cell.setCellValue((Double) rows.get(i).get("OUTWEIGHT"));
                        } else {
                            cell.setCellValue("0.00");
                        }
                        break;

                }
            }

            r++;
        }
        row = sheet.createRow(r);
        cell = row.createCell(0);
        sheet.createFreezePane(0, 2);
        HttpUtils.download(response,wb,templateName);
    }

    @Journal(name = "月度订单产品汇总")
    @RequestMapping(value = "summaryMonthlyOrder", method = RequestMethod.GET)
    public String summaryMonthlyOrder() {
        return summaryMonthly;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取月度订单产品汇总列表信息")
    @RequestMapping("summaryMonthlylist")
    public String getsummaryMonthly(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageSummaryMonthly = salesOrderService.findPageSummaryMonthly(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) findPageSummaryMonthly.get("rows");
        Map<String, Object> map = new HashMap<String, Object>();
        // DecimalFormat df = new DecimalFormat("######0.00"); //保留两位小数点
        if (list.size() > 0) {
            Double countweight = 0.0;// 计调下单总重量
            Double countroll = 0.0;// 计调下单总卷数
            Double countyroll = 0.0;// 已生产总卷数
            Double wweight = 0.0;// 未完成总重量
            Integer ytraycount = 0;// 已打包总托数
            Integer countray = 0;// 总托数
            Double inweight = 0.0;// 在库总重量
            Integer intray = 0;// 在库总托数
            Double outweight = 0.0;// 出库总重量
            Integer outrtay = 0;// 出库总托数
            for (int i = 0; i < list.size(); i++) {
                Integer rollcount = 0; // 已生产的卷数
                Integer traycout = 0; // 已打包的托数
                Integer intraycout = 0; // 在库托数
                Double intrayweight = 0.0; // 在库重量
                Integer outtyaycout = 0; // 出库托数
                Double outtrayweight = 0.0; // 出库重量
                Double completedamount = 0.0;// 已完成量
                String sdevicecode = "";
                Long ID = Long.valueOf(list.get(i).get("ID").toString());
                map.clear();
                map.put("producePlanDetailId", ID);
                map.put("isAbandon", 0);
                List<RollBarcode> findListByMap2 = producePlanService.findListByMap(RollBarcode.class, map);
                // 查询已生产的卷数
                if (findListByMap2.size() > 0) {
                    for (RollBarcode rollBarcode : findListByMap2) {
                        map.clear();
                        map.put("rollBarcode", rollBarcode.getBarcode());
                        map.put("rollQualityGradeCode", "A");
                        map.put("state", 0);
                        List<Roll> findListByMap3 = producePlanService.findListByMap(Roll.class, map);
                        rollcount += findListByMap3.size();
                        countyroll += findListByMap3.size();
                    }
                }
                map.clear();
                map.put("producePlanDetailId", ID);
                map.put("isOpened", 0);
                List<TrayBarCode> tray = producePlanService.findListByMap(TrayBarCode.class, map);
                // 查询生产的托数
                if (tray.size() > 0) {
                    for (TrayBarCode trayBarCode : tray) {
                        map.clear();
                        map.put("trayBarcode", trayBarCode.getBarcode());
                        map.put("rollQualityGradeCode", "A");
                        List<Tray> t = producePlanService.findListByMap(Tray.class, map);
                        traycout += t.size();
                        ytraycount += t.size();
                        if (t.size() > 0) {
                            // 查询生产托数中在库的托数和在库重量
                            for (Tray tray2 : t) {
                                map.clear();
                                map.put("rollBarcode", tray2.getTrayBarcode());
                                map.put("state", 1);
                                List<TotalStatistics> tota = producePlanService.findListByMap(TotalStatistics.class, map);
                                intraycout += tota.size();
                                intray += tota.size();
                                if (tota.size() > 0) {
                                    for (TotalStatistics totalStatistics : tota) {
                                        if (totalStatistics.getProductWeight() != null) {
                                            intrayweight += totalStatistics.getProductWeight();
                                            inweight += totalStatistics.getProductWeight();

                                        }
                                    }
                                }
                            }
                            // 查询查询生产托数中出库托数和出库重量
                            for (Tray tray2 : t) {
                                map.clear();
                                map.put("rollBarcode", tray2.getTrayBarcode());
                                map.put("state", -1);
                                List<TotalStatistics> tota = producePlanService.findListByMap(TotalStatistics.class, map);
                                outtyaycout += tota.size();
                                outrtay += tota.size();
                                if (tota.size() > 0) {
                                    for (TotalStatistics totalStatistics : tota) {
                                        outtrayweight += totalStatistics.getProductWeight();
                                        outweight += totalStatistics.getProductWeight();
                                    }
                                }
                            }
                        }
                    }
                }
                list.get(i).put("RC", rollcount);
                list.get(i).put("TC", traycout);
                list.get(i).put("STOCKIN", intraycout);
                list.get(i).put("STOCKINWEIGHT", intrayweight);
                list.get(i).put("STOCKOUT", outtyaycout);
                list.get(i).put("STOCKOUTWEIGHT", outweight);// 出库量
                // 入库的托数和重量
                list.get(i).put("STOCK", intraycout + outtyaycout);
                list.get(i).put("STOCKWEIGHT", intrayweight + outtrayweight);
                if ((Double) list.get(i).get("ORDERCOUNT") - (Integer) list.get(i).get("RC") > 0) {
                    if (list.get(i).get("PLANTOTALWEIGHT") != null) {
                        // 未完成量
                        wweight = ((Double) list.get(i).get("ORDERCOUNT") - (Integer) list.get(i).get("RC")) / (Double) list.get(i).get("ORDERCOUNT") * (Double) list.get(i).get("PLANTOTALWEIGHT");
                        list.get(i).put("WWEIGHT", wweight);
                    }
                }
                if (list.get(i).get("WWEIGHT") != null) {
                    // 已完成量
                    completedamount = (Double) list.get(i).get("PLANTOTALWEIGHT") - (Double) list.get(i).get("WWEIGHT");
                } else {
                    completedamount = (Double) list.get(i).get("PLANTOTALWEIGHT");
                }
                list.get(i).put("COMPLETEDAMOUNT", completedamount);
            }

        }
        return GsonTools.toJson(findPageSummaryMonthly);
    }

    @NoLogin
    @Journal(name = "导出月度订单产品汇总表")
    @ResponseBody
    @RequestMapping(value = "export3", method = RequestMethod.GET)
    public void export3(Filter filter) throws Exception {
        Page page = new Page();
        page.setPage(1);
        page.setRows(99999);
        Map<String, Object> findPageSummaryMonthly = salesOrderService.findPageSummaryMonthly(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) findPageSummaryMonthly.get("rows");
        Map<String, Object> map = new HashMap<String, Object>();
        DecimalFormat df = new DecimalFormat("######0.00"); // 保留两位小数点
        if (list.size() > 0) {
            Double countweight = 0.0;// 计调下单总重量
            Double countroll = 0.0;// 计调下单总卷数
            Double countyroll = 0.0;// 已生产总卷数
            Double wweight = 0.0;// 未完成总重量
            Integer ytraycount = 0;// 已打包总托数
            Integer countray = 0;// 总托数
            Double inweight = 0.0;// 在库总重量
            Integer intray = 0;// 在库总托数
            Double outweight = 0.0;// 出库总重量
            Integer outrtay = 0;// 出库总托数
            for (int i = 0; i < list.size(); i++) {
                Integer rollcount = 0; // 已生产的卷数
                Double outtrayweight = 0.0; // 出库重量
                Double completedamount = 0.0;// 已完成量
                Long ID = Long.valueOf(list.get(i).get("ID").toString());
                map.clear();
                map.put("producePlanDetailId", ID);
                map.put("isAbandon", 0);
                List<RollBarcode> findListByMap2 = producePlanService.findListByMap(RollBarcode.class, map);
                // 查询已生产的卷数
                if (findListByMap2.size() > 0) {
                    for (RollBarcode rollBarcode : findListByMap2) {
                        map.clear();
                        map.put("rollBarcode", rollBarcode.getBarcode());
                        map.put("rollQualityGradeCode", "A");
                        map.put("state", 0);
                        List<Roll> findListByMap3 = producePlanService.findListByMap(Roll.class, map);
                        rollcount += findListByMap3.size();
                        countyroll += findListByMap3.size();
                    }
                }
                map.clear();
                map.put("producePlanDetailId", ID);
                map.put("isOpened", 0);
                List<TrayBarCode> tray = producePlanService.findListByMap(TrayBarCode.class, map);
                // 查询生产的托数
                if (tray.size() > 0) {
                    for (TrayBarCode trayBarCode : tray) {
                        map.clear();
                        map.put("trayBarcode", trayBarCode.getBarcode());
                        map.put("rollQualityGradeCode", "A");
                        List<Tray> t = producePlanService.findListByMap(Tray.class, map);
                        if (t.size() > 0) {
                            // 查询查询生产托数中出库托数和出库重量
                            for (Tray tray2 : t) {
                                map.clear();
                                map.put("rollBarcode", tray2.getTrayBarcode());
                                map.put("state", -1);
                                List<TotalStatistics> tota = producePlanService.findListByMap(TotalStatistics.class, map);
                                if (tota.size() > 0) {
                                    for (TotalStatistics totalStatistics : tota) {
                                        outtrayweight += totalStatistics.getProductWeight();
                                        outweight += totalStatistics.getProductWeight();
                                    }
                                }
                            }
                        }
                    }
                }
                list.get(i).put("RC", rollcount);
                list.get(i).put("STOCKOUTWEIGHT", df.format(outweight));// 出库量
                if ((Double) list.get(i).get("ORDERCOUNT") - (Integer) list.get(i).get("RC") > 0) {
                    if (list.get(i).get("PLANTOTALWEIGHT") != null) {
                        // 未完成量
                        wweight = ((Double) list.get(i).get("ORDERCOUNT") - (Integer) list.get(i).get("RC")) / (Double) list.get(i).get("ORDERCOUNT") * (Double) list.get(i).get("PLANTOTALWEIGHT");
                        list.get(i).put("WWEIGHT", df.format(wweight));
                    }
                }
                if (list.get(i).get("WWEIGHT") != null) {
                    // 已完成量
                    completedamount = (Double) list.get(i).get("PLANTOTALWEIGHT") - Double.valueOf(list.get(i).get("WWEIGHT").toString());
                } else {
                    completedamount = (Double) list.get(i).get("PLANTOTALWEIGHT");
                }
                list.get(i).put("COMPLETEDAMOUNT", df.format(completedamount));
            }

        }

        String templateName = "月度订单产品汇总报表";
        SXSSFWorkbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        Sheet sheet = wb.createSheet();
        Integer xx = null;
        Row row = null;
        Cell cell = null;
        String columnName[] = new String[]{"客户名称", "客户产品名称", "厂内名称", "订调下单量(kg)", "已完成量(kg)", "未完成量(kg)", "出货量(kg)",};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 7; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;
        sheet.setColumnWidth(0, 20 * 256);// 设置列宽
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 30 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 25 * 256);

        row = sheet.createRow(r);
        cell = row.createCell(0);
        for (int i = 0; i < list.size(); i++) {
            Double jdcount = (Double) list.get(i).get("PLANTOTALWEIGHT");
            if (jdcount == null) {
                jdcount = 0.0;
            }
            Double countroll = (Double) list.get(i).get("ORDERCOUNT");
            Double wwc = (Double) list.get(i).get("ORDERCOUNT") - (Integer) list.get(i).get("RC");
            row = sheet.createRow(r);
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        cell.setCellValue(list.get(i).get("CONSUMERNAME").toString());
                        break;
                    case 1:
                        cell.setCellValue(list.get(i).get("CONSUMERPRODUCTNAME").toString());
                        break;
                    case 2:
                        cell.setCellValue(list.get(i).get("FACTORYPRODUCTNAME").toString());
                        break;
                    case 3:
                        if (list.get(i).get("PLANTOTALWEIGHT") != null) {
                            cell.setCellValue(list.get(i).get("PLANTOTALWEIGHT").toString());
                        }
                        break;
                    case 4:
                        if (list.get(i).get("COMPLETEDAMOUNT") != null) {
                            cell.setCellValue(list.get(i).get("COMPLETEDAMOUNT").toString());
                        } else {
                            cell.setCellValue("0.00");
                        }
                        break;
                    case 5:
                        if (list.get(i).get("WWEIGHT") != null) {
                            cell.setCellValue(list.get(i).get("WWEIGHT").toString());
                        } else {
                            cell.setCellValue("0.00");
                        }
                        break;
                    case 6:
                        if (list.get(i).get("STOCKOUTWEIGHT") != null) {
                            cell.setCellValue(list.get(i).get("STOCKOUTWEIGHT").toString());
                        } else {
                            cell.setCellValue("0.00");
                        }
                        break;

                }
            }

            r++;

        }
        row = sheet.createRow(r);
        cell = row.createCell(0);
        sheet.createFreezePane(0, 2);
        HttpUtils.download(response,wb,templateName);
    }
}

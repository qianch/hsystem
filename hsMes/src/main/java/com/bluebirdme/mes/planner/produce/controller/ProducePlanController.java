/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.controller;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBomVersionDetail;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.material.service.IMrpService;
import com.bluebirdme.mes.planner.pack.entity.PackTask;
import com.bluebirdme.mes.planner.pack.service.IPackTaskService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.planner.produce.service.IProducePlanService;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlanDetails;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IDepartmentService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductCategory;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.produce.service.IFinishedProductCategoryService;
import com.bluebirdme.mes.produce.service.IFinishedProductService;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.service.IConsumerService;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.utils.FilterRules;
import com.bluebirdme.mes.utils.HttpUtils;
import com.bluebirdme.mes.utils.MapUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 生产任务单Controller
 *
 * @author 高飞
 * @Date 2016-11-28 21:25:48
 */
@Controller
@RequestMapping("/planner/produce")
@Journal(name = "生产任务单")
public class ProducePlanController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(ProducePlanController.class);
    // 生产任务单页面
    final String index = "planner/produce/producePlan";
    final String addOrEdit = "planner/produce/producePlanAddOrEdit";
    final String look = "planner/produce/produceReadOnly";
    final String finishProducePage = "planner/produce/finishProducePage";
    final String ProductionSchedule = "planner/produce/ProductionSchedule";
    @Resource
    IProducePlanService producePlanService;
    @Resource
    ISalesOrderService salesOrderService;
    @Resource
    IAuditInstanceService auditInstanceService;
    @Resource
    IConsumerService consumerService;
    @Resource
    IFinishedProductService finishedProductService;
    @Resource
    IFinishedProductCategoryService finishedProductCategoryService;
    @Resource
    IPackTaskService ptService;
    @Resource
    IMrpService mrpService;
    @Resource
    IDepartmentService departmentService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoLogin
    @Journal(name = "修复MRP、计划生成裁剪和编织计划")
    @ResponseBody
    @RequestMapping("{op}/{id}")
    public String fix(@PathVariable("op") String op, @PathVariable("id") Long id) {
        try {
            ProducePlan pp = producePlanService.findById(ProducePlan.class, id);
            switch (op) {
                case "mrp":
                    mrpService.createRequirementPlans(pp);
                    break;
                case "all":
                    producePlanService.createCutAndWeavePlans(pp);
                    break;
                default:
                    return "unknow operation";
            }
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
        return "SUC";
    }

    @ResponseBody
    @Journal(name = "获取生产任务单列表信息")
    @RequestMapping("list")
    public String list(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(producePlanService.findPageInfo(filter, page));
    }

    @ResponseBody
    @Journal(name = "获取生产任务单内容")
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public String details(ProducePlan producePlan) throws Exception {
        return GsonTools.toJson(producePlanService.details(producePlan.getId()));
    }

    @ResponseBody
    @Journal(name = "获取生产任务单内容（镜像优先版）")
    @RequestMapping(value = "/details1", method = RequestMethod.GET)
    public String details1(ProducePlan producePlan) {
        if (producePlanService.detailsMirror(producePlan.getId()).size() != 0) {
            return GsonTools.toJson(producePlanService.detailsMirror(producePlan.getId()));
        } else {
            return GsonTools.toJson(producePlanService.details(producePlan.getId()));
        }
    }

    @Journal(name = "生产订单")
    @RequestMapping("/order")
    public String order() throws Exception {
        return "planner/produce/order";
    }

    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/order/select")
    public ModelAndView selectOrder(String workShopCode) throws Exception {
        return new ModelAndView("planner/produce/orderProductSelect").addObject("workShopCode", workShopCode);
    }

    @Journal(name = "获取生产订单")
    @RequestMapping("/order/deliverySelect")
    public ModelAndView deliverySelect() throws Exception {
        return new ModelAndView("planner/delivery/orderProductSelect");
    }

    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/product/select")
    public ModelAndView productSelect(String ids) throws Exception {
        return new ModelAndView("planner/delivery/product", model.addAttribute("ids", ids));
    }

    @ResponseBody
    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/order/list")
    public String orderList(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(producePlanService.findOrderPageInfo(filter, page));
    }

    @ResponseBody
    @Journal(name = "获取未分配的生产订单")
    @RequestMapping(value = "searchbox", method = RequestMethod.POST)
    public String find(String searchbox) {
        return GsonTools.toJson(producePlanService.searchbox(searchbox));
    }

    @Journal(name = "添加生产任务单页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(ProducePlan producePlan) {
        return new ModelAndView(addOrEdit, model.addAttribute("producePlan", producePlan));
    }

    @ResponseBody
    @Journal(name = "获取单号")
    @RequestMapping(value = "serial")
    public String getSerial(String workShopCode) throws Exception {
        return producePlanService.getSerial(workShopCode);
    }

    @ResponseBody
    @Journal(name = "保存生产任务单", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody ProducePlan producePlan) throws Exception {
        Map<String, Object> param = new HashMap();
        param.put("producePlanCode", producePlan.getProducePlanCode());
        if (producePlanService.isExist(ProducePlan.class, param, true)) {
            return ajaxError("生产单号重复");
        }

        producePlan.setAuditState(AuditConstant.RS.SUBMIT);
        producePlan.setCreateTime(new Date());
        if (null != producePlan.getWorkShopCode()) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", producePlan.getWorkShopCode());
            producePlan.setWorkshop(departmentService.findUniqueByMap(Department.class, map).getName());
        }
        producePlanService.savePlan(producePlan);
        return GsonTools.toJson(producePlan);
    }

    @ResponseBody
    @Journal(name = "关闭生产计划查看审核状态", logType = LogType.DB)
    @RequestMapping(value = "close", method = RequestMethod.GET)
    public String close(Long id) throws Exception {
        ProducePlan plan = producePlanService.findById(ProducePlan.class, id);
        if (plan.getAuditState() != 2) {
            return ajaxError("只有审核通过后方可操作");
        } else {
            return "1";
        }
    }

    @ResponseBody
    @Journal(name = "关闭生产计划查看编制是否关闭", logType = LogType.DB)
    @RequestMapping(value = "wclose", method = RequestMethod.GET)
    public String wclose(String ids) {
        Map<String, Object> map = new HashMap();
        List<WeavePlan> findListByMap;
        List<CutPlan> findListByMap1;
        for (String id : ids.split(",")) {
            map.clear();
            map.put("producePlanDetailId", Long.parseLong(id));
            // 通过订单明细ID查询所有的编织计划
            findListByMap = producePlanService.findListByMap(WeavePlan.class, map);
            findListByMap1 = producePlanService.findListByMap(CutPlan.class, map);
            for (WeavePlan weavePlan : findListByMap) {
                if (weavePlan.getClosed() == null || weavePlan.getClosed() == 0) {
                    return ajaxError("只有先关闭编制计划后方可操作");
                }
            }
            for (CutPlan cutPlan : findListByMap1) {
                if (cutPlan.getClosed() == null || cutPlan.getClosed() == 0) {
                    return ajaxError("只有先关闭裁剪计划后方可操作");
                }
            }
        }
        return ajaxSuccess();
    }

    @Journal(name = "编辑生产任务单页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(ProducePlan producePlan, String force) throws Exception {
        producePlan = producePlanService.findById(ProducePlan.class, producePlan.getId());
        Map<String, Object> param = new HashMap();
        param.put("producePlanId", producePlan.getId());
        if (null != producePlan.getWorkShopCode()) {
            switch (producePlan.getWorkShopCode()) {
                case "00107" -> producePlan.setWorkshop("编织一车间");
                case "00108" -> producePlan.setWorkshop("编织二车间");
                case "00109" -> producePlan.setWorkshop("编织三车间");
                case "00116" -> producePlan.setWorkshop("裁剪一车间");
                case "00117" -> producePlan.setWorkshop("裁剪二车间");
            }
        }
        List<ProducePlanDetail> ppdList = producePlanService.findListByMap(ProducePlanDetail.class, param);
        List<Map<Object, Object>> ret = new ArrayList();
        for (ProducePlanDetail ppd : ppdList) {
            ret.add(MapUtils.entityToMap(ppd));
        }
        return new ModelAndView(addOrEdit, model.addAttribute("producePlan", producePlan).addAttribute("force", force).addAttribute("details", GsonTools.toJson(ret)));
    }

    @ResponseBody
    @Journal(name = "强制变更订单", logType = LogType.DB)
    @RequestMapping(value = "forceEdit", method = RequestMethod.POST)
    public String forceEdit(@RequestBody ProducePlan producePlan) throws Exception {
        producePlanService.forceEdit(producePlan, (Long) session.getAttribute(Constant.CURRENT_USER_ID));
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/order/listOrders")
    public String listOrders(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(producePlanService.listOrders(filter, page));
    }

    @ResponseBody
    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/order/list2")
    public String orderList2(String filterRules, String info, String workShopCode, Page page) throws Exception {
        Filter filter = new Filter();
        if (info != null && !"null".equals(info)) {
            filter.set("complete", info);
        }

        if (!StringUtils.isEmpty(filterRules)) {
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(filterRules).getAsJsonArray();
            FilterRules rule;
            Gson gson = new Gson();
            for (JsonElement obj : array) {
                rule = gson.fromJson(obj, FilterRules.class);
                if (rule.getField().equals("UNALLOCATECOUNT") || rule.getField().equals("ISPLANED")) {
                    filter.set(rule.getField(), rule.getValue());
                } else {
                    filter.set(rule.getField(), "like:" + rule.getValue());
                }
            }
        }
        if (StringUtils.isEmpty(workShopCode)) {
            workShopCode = "00107";
        }

        List<Map<String, Object>> list = departmentService.queryDepartment("weave");
        List listWorkshop = new ArrayList<String>();
        for (Map<String, Object> stringObjectMap : list) {
            listWorkshop.add(stringObjectMap.get("CODE"));
        }

        if (listWorkshop.contains(workShopCode)) {
            filter.set("TC", "2");
        } else {
            filter.set("TC", "1");
        }

        if (filter.get("ISCOMPLETE") == null || filter.get("ISCOMPLETE").equals("")) {
            filter.set("ISCOMPLETE", "");
        }
        Map<String, Object> findPageInfo = producePlanService.findOrderPageInfo2(filter, page);
        return GsonTools.toJson(findPageInfo);
    }

    @ResponseBody
    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/order/list3")
    public String orderList3(String filterRules, String info, String workShopCode, Page page) throws Exception {
        if (filterRules == null) {
            return GsonTools.toJson("请输入搜索条件！");
        }
        Filter filter = new Filter();
        if (info != null && !"null".equals(info)) {
            filter.set("complete", info);
        }

        if (!StringUtils.isEmpty(filterRules)) {
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(filterRules).getAsJsonArray();
            FilterRules rule;
            Gson gson = new Gson();
            for (JsonElement obj : array) {
                rule = gson.fromJson(obj, FilterRules.class);
                if (rule.getField().equals("UNALLOCATECOUNT") || rule.getField().equals("ISPLANED")) {
                    filter.set(rule.getField(), rule.getValue());
                } else {
                    filter.set(rule.getField(), "like:" + rule.getValue());
                }
            }
        }

        if (StringUtils.isEmpty(workShopCode)) {
            workShopCode = "00107";
        }

        List<Map<String, Object>> list = departmentService.queryDepartment("weave");
        List listWorkshop = new ArrayList<String>();
        for (Map<String, Object> stringObjectMap : list) {
            listWorkshop.add(stringObjectMap.get("CODE"));
        }

        if (listWorkshop.contains(workShopCode)) {
            filter.set("TC", "2");
        } else {
            filter.set("TC", "1");
        }

        if (filter.get("ISCOMPLETE") == null || filter.get("ISCOMPLETE").equals("")) {
            filter.set("ISCOMPLETE", "");
        }

        Map<String, Object> findPageInfo = producePlanService.findOrderPageInfo2(filter, page);
        return GsonTools.toJson(findPageInfo);
    }

    @Journal(name = "查看生产任务页面")
    @RequestMapping(value = "look", method = RequestMethod.GET)
    public ModelAndView look(ProducePlan producePlan) throws IllegalArgumentException, SecurityException {
        producePlan = producePlanService.findById(ProducePlan.class, producePlan.getId());
        List<Map<String, Object>> ret = producePlanService.details(producePlan.getId());
        return new ModelAndView(look, model.addAttribute("producePlan", producePlan).addAttribute("details", GsonTools.toJson(ret)));
    }

    @ResponseBody
    @Journal(name = "编辑生产任务单", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(@RequestBody ProducePlan producePlan) throws Exception {
        Map<String, Object> param = new HashMap();
        param.put("producePlanCode", producePlan.getProducePlanCode());
        if (producePlanService.isExist(ProducePlan.class, param, producePlan.getId(), true)) {
            return ajaxError("生产单号重复");
        }
        ProducePlan old = producePlanService.findById(ProducePlan.class, producePlan.getId());
        if (!old.getCreateUserId().equals(session.getAttribute(Constant.CURRENT_USER_ID))) {
            return ajaxError("只能由下达该计划的业务员才能编辑该条记录！");
        }
        producePlan.setAuditState(0);
        producePlan.setCreateTime(old.getCreateTime());
        producePlan.setCreateUserId(old.getCreateUserId());
        producePlan.setCreateUserName(old.getCreateUserName());
        Department department = departmentService.findOne(Department.class, "code", producePlan.getWorkShopCode());
        producePlan.setWorkshop(department.getName());
        producePlanService.updatePlan(producePlan);
        return GsonTools.toJson(producePlan);
    }

    @ResponseBody
    @Journal(name = "生产任务单提交审核", logType = LogType.DB)
    @RequestMapping(value = "commitAudit", method = RequestMethod.POST)
    public String _commitAudit(Long id, String name) throws Exception {
        ProducePlan producePlan = auditInstanceService.findById(ProducePlan.class, id);
        if (!producePlan.getCreateUserId().equals(session.getAttribute(Constant.CURRENT_USER_ID))) {
            return ajaxError("只能由下达该计划的业务员才能提交审核！");
        }

        Map<String, Object> map = new HashMap();
        map.put("producePlanId", id);
        List<ProducePlanDetail> list = producePlanService.findListByMap(ProducePlanDetail.class, map);
        for (ProducePlanDetail ppd : list) {
            if (ppd.getIsTurnBagPlan().equals("翻包")) {
                map.clear();
                map.put("producePlanDetailId", ppd.getId());
                if (!producePlanService.isExist(TurnBagPlanDetails.class, map)) {
                    return ajaxError("需要翻包任务请设置翻包领料信息");
                }
            }
        }

        String rs = producePlanService.hasPackTask(id);
        if (!StringUtils.isBlank(rs)) {
            return ajaxError("产品:" + rs + "尚未设置包装任务");
        }
        auditInstanceService.submitAudit(name, AuditConstant.CODE.SC, (Long) session.getAttribute(Constant.CURRENT_USER_ID), "planner/produce/audit?id=" + id, Long.valueOf(id), ProducePlan.class);
        return Constant.AJAX_SUCCESS;
    }

    // type:1=销售订单，2=producePlan，3=weaveDailyPlan，4=cutDailyPlan
    @ResponseBody
    @Journal(name = "任务强制重置审核状态", logType = LogType.DB)
    @RequestMapping(value = "reloadAudit", method = RequestMethod.POST)
    public String reloadAudit(Long id, Integer type) throws Exception {
        auditInstanceService.reloadAudit(id, type);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "删除生产任务单", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(String ids) throws Exception {
        producePlanService.deletePlan(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "查看产品的包装工艺要求")
    @RequestMapping(value = "requirements", method = RequestMethod.GET)
    public String findDetils(String productId) {
        FinishedProduct finishProduct = producePlanService.findById(FinishedProduct.class, Long.parseLong(productId));
        return GsonTools.toJson(finishProduct);
    }

    @ResponseBody
    @Journal(name = "更新产品工艺要求和包装要求", logType = LogType.DB)
    @RequestMapping(value = "updateP", method = RequestMethod.GET)
    public String delete(String productId, String packReq, String procReq) throws Exception {
        FinishedProduct finishProduct = producePlanService.findById(FinishedProduct.class, Long.valueOf(productId));
        finishProduct.setPackReq(packReq);
        finishProduct.setProcReq(procReq);
        producePlanService.update(finishProduct);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "审核查看")
    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public ModelAndView audit(Long id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        ProducePlan pp = producePlanService.findById(ProducePlan.class, id);
        Map<String, Object> param = new HashMap();
        param.put("producePlanId", pp.getId());
        List<ProducePlanDetail> ppdList = producePlanService.findListByMap(ProducePlanDetail.class, param);
        List<Map<Object, Object>> ret = new ArrayList();
        for (ProducePlanDetail ppd : ppdList) {
            ret.add(MapUtils.entityToMap(ppd));
        }
        return new ModelAndView("planner/produce/audit").addObject("pp", pp).addObject("details", GsonTools.toJson(producePlanService.details(id)));
    }

    @ResponseBody
    @RequestMapping("valid/batchcode/{batchCode}")
    public String validBatchCode(@PathVariable("batchCode") String batchCodes[], Long producePlanId) {
        Map<String, Object> map = new HashMap();
        StringBuilder ret = new StringBuilder();
        if (producePlanId != null) {
            for (String batchCode : batchCodes) {
                List<Map<String, Object>> ppd = producePlanService.checkBatchCode(batchCode, producePlanId);
                if (ppd.size() > 0) {
                    ret.append(batchCode + "<br>");
                }
            }
        } else {
            for (String batchCode : batchCodes) {
                map.clear();
                map.put("batchCode", batchCode);
                if (producePlanService.isExist(ProducePlanDetail.class, map, true)) {
                    ret.append(batchCode + "<br>");
                }
            }
        }

        map.clear();
        map.put("ret", ret.toString());
        return GsonTools.toJson(map);
    }

    /**
     * EASYUI组件DataList
     *
     * @param filter
     * @param page
     * @return
     * @throws Exception
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "获取生产计划列表信息")
    @RequestMapping("datalist")
    public String datalist(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(producePlanService.dataList(filter, page));
    }

    @Journal(name = "根据日计划id导出Excel")
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(Long id) throws Exception {
        // 根据id查出生产计划
        ProducePlan producePlan = producePlanService.findById(ProducePlan.class, id);
        HashMap<String, Object> map = new HashMap();
        map.put("producePlanId", id);
        List<ProducePlanDetail> details = producePlanService.findListByMap(ProducePlanDetail.class, map);
        if (details.size() != 0) {
            for (int i = 0; i < details.size(); i++) {
                // 把客戶名称改为客户简称
                Consumer c = consumerService.findById(Consumer.class, details.get(i).getConsumerId());
                details.get(i).setConsumerName(c.getConsumerSimpleName());
            }
        }
        String templateName = "生产任务单(" + producePlan.getProducePlanCode() + ")";
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        Workbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);// 单元格自动换行

        Sheet sheet = wb.createSheet("生产任务单");
        Row row;
        Cell cell;
        String[] columnName = new String[]{"客户简称", "客户订单号", "批号", "产品类别代码", "产品类别名称", "产品名称", "客户产品名称", "门幅(mm)", "产品数量（卷/套）", "产品重量（KG）", "产品卷长（M）", "预留长度（M）", "产品卷重（KG）", "图号", "包装要求", "内销/外销", "交货日期", "使用工艺", "生产机器", "备注"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        row.setHeight((short) 1000);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司生产任务单");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 20; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 19));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("Q/HS RH0042-2015");
        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.RIGHT); // 水平布局：居中
        cellStyle1.setBorderBottom(BorderStyle.NONE);
        cellStyle1.setBorderTop(BorderStyle.NONE);
        cellStyle1.setBorderRight(BorderStyle.THIN);
        cellStyle1.setBorderLeft(BorderStyle.THIN);
        cell.setCellStyle(cellStyle1);
        for (int i = 1; i < 20; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle1);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 19));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("任务单编号：" + producePlan.getProducePlanCode());
        CellStyle cellStyles = wb.createCellStyle();
        cellStyles.setAlignment(HorizontalAlignment.RIGHT); // 水平布局：居中
        cellStyles.setBorderBottom(BorderStyle.THIN);
        cellStyles.setBorderTop(BorderStyle.NONE);
        cellStyles.setBorderRight(BorderStyle.THIN);
        cellStyles.setBorderLeft(BorderStyle.THIN);
        cell.setCellStyle(cellStyles);
        for (int i = 1; i < 20; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyles);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 19));
        r++;
        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 15 * 256);// 设置列宽
        sheet.setColumnWidth(1, 18 * 256);
        sheet.setColumnWidth(2, 13 * 256);
        sheet.setColumnWidth(3, 13 * 256);
        sheet.setColumnWidth(4, 18 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 9 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 9 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 10 * 256);
        sheet.setColumnWidth(12, 15 * 256);
        sheet.setColumnWidth(13, 15 * 256);
        sheet.setColumnWidth(14, 50 * 256);
        sheet.setColumnWidth(15, 9 * 256);
        sheet.setColumnWidth(16, 10 * 256);
        sheet.setColumnWidth(17, 30 * 256);
        sheet.setColumnWidth(18, 20 * 256);
        sheet.setColumnWidth(19, 30 * 256);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;
        // 遍历数据的长度，得到递增的行数
        for (ProducePlanDetail data : details) {
            // 得到成品信息
            FinishedProduct finishedProduct = finishedProductService.findById(FinishedProduct.class, data.getProductId());
            FinishedProductMirror finishedProductMirror = finishedProductService.findById(FinishedProductMirror.class, data.getProductId());
            FinishedProductCategory finishedProductCategory = finishedProductCategoryService.findById(FinishedProductCategory.class, finishedProduct.getFpcid());
            SalesOrderDetail sod = producePlanService.findById(SalesOrderDetail.class, data.getFromSalesOrderDetailId());
            SalesOrder so = producePlanService.findById(SalesOrder.class, sod.getSalesOrderId());
            row = sheet.createRow(r);
            // 当产品型号和原来的产品型号不同时，记录现在的型号并插入一行型号
            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        if (data.getConsumerName() != null && so.getSalesOrderMemo() != null) {
                            int index = so.getSalesOrderMemo().indexOf("叶型为：");
                            if (index != -1) {
                                String memo = so.getSalesOrderMemo().substring(index + 4);
                                cell.setCellValue(data.getConsumerName() + "(" + memo + ")");
                            } else {
                                cell.setCellValue(data.getConsumerName());
                            }
                        } else if (data.getConsumerName() != null && so.getSalesOrderMemo() == null) {
                            cell.setCellValue(data.getConsumerName());
                        }
                        break;
                    case 1:
                        if (data.getSalesOrderSubcodePrint() != null) {
                            cell.setCellValue(data.getSalesOrderSubcodePrint());
                        }
                        break;
                    case 2:
                        if (data.getBatchCode() != null) {
                            cell.setCellValue(data.getBatchCode());
                        }
                        break;
                    case 3:
                        if (finishedProductCategory != null) {
                            cell.setCellValue(finishedProductCategory.getCategoryCode());
                        }
                        break;
                    case 4:
                        if (finishedProductCategory != null) {
                            cell.setCellValue(finishedProductCategory.getCategoryName());
                        }
                        break;
                    case 5:
                        if (data.getFactoryProductName() != null) {
                            cell.setCellValue(data.getFactoryProductName());
                        }
                        break;
                    case 6:
                        if (data.getConsumerProductName() != null) {
                            cell.setCellValue(data.getConsumerProductName());
                        }
                        break;
                    case 7:
                        if (data.getProductWidth() != null) {
                            cell.setCellValue(data.getProductWidth());
                        }
                        break;
                    case 8:
                        if (data.getOrderCount() != null) {
                            cell.setCellValue(data.getRequirementCount());
                        }
                        break;
                    case 9:
                        if (data.getPlanTotalWeight() != null) {
                            cell.setCellValue(data.getPlanTotalWeight());
                        }
                        break;
                    case 10:
                        if (data.getProductLength() != null) {
                            cell.setCellValue(data.getProductLength() + " m");
                        }
                        break;
                    case 11:
                        if (finishedProduct != null) {
                            if (finishedProduct.getReserveLength() != null) {
                                cell.setCellValue(finishedProduct.getReserveLength() + " m");
                            }
                        } else if (finishedProductMirror != null) {
                            if (finishedProductMirror.getReserveLength() != null) {
                                cell.setCellValue(finishedProductMirror.getReserveLength() + " m");
                            }
                        }
                        break;
                    case 12:
                        if (data.getProductWeight() != null) {
                            cell.setCellValue(data.getProductWeight() + " kg");
                        }
                        break;
                    case 13:
                        if (data.getDrawNo() != null) {
                            cell.setCellValue(data.getDrawNo());
                        }
                        break;
                    case 14:
                        if (data.getPackReq() != null) {
                            cell.setCellValue(data.getPackReq());
                        }
                        break;
                    case 15:
                        if (so.getSalesOrderIsExport() != null) {
                            cell.setCellValue(so.getSalesOrderIsExport() == 0 ? "外销" : "内销");
                        }
                        break;
                    case 16:
                        if (data.getDeleveryDate() != null) {
                            cell.setCellValue(sdf.format(data.getDeleveryDate()));
                        }
                        break;
                    case 17:
                        if (data.getProcReq() != null) {
                            cell.setCellValue(data.getProcReq());
                        }
                        break;
                    case 18:
                        if (data.getDeviceCode() != null) {
                            cell.setCellValue(data.getDeviceCode());
                        }
                        break;
                    case 19:
                        if (sod != null) {
                            Long salesOrderId = sod.getSalesOrderId();
                            SalesOrder salerder = producePlanService.findById(SalesOrder.class, salesOrderId);
                            if (salerder != null) {
                                User user = producePlanService.findById(User.class, salerder.getSalesOrderBizUserId());
                                if (user != null && data.getComment() != null) {
                                    cell.setCellValue("业务员:" + user.getUserName() + "   " + data.getComment());
                                } else if (user != null && data.getComment() == null) {
                                    cell.setCellValue("业务员:" + user.getUserName());
                                } else if (user == null && data.getComment() != null) {
                                    cell.setCellValue(data.getComment());
                                }
                            }
                        }
                        break;
                }
            }

            if (data.getProductIsTc().intValue() == 1) {
                String[] partNames = {"部件名称", "计划数量", "订单数量", "BOM数量"};
                row = sheet.createRow(++r);
                cell = row.createCell(1);
                cell.setCellValue(partNames[0]);
                cell = row.createCell(2);
                cell.setCellValue(partNames[1]);
                cell = row.createCell(3);
                cell.setCellValue(partNames[2]);
                cell = row.createCell(4);
                cell.setCellValue(partNames[3]);

                map.clear();
                map.put("planDetailId", data.getId());
                List<ProducePlanDetailPartCount> list = salesOrderService.findListByMap(ProducePlanDetailPartCount.class, map);
                for (ProducePlanDetailPartCount count : list) {
                    row = sheet.createRow(++r);
                    cell = row.createCell(1);
                    cell.setCellValue(count.getPartName());
                    cell = row.createCell(2);
                    cell.setCellValue(count.getPlanPartCount());
                    cell = row.createCell(3);
                    cell.setCellValue(count.getPartCount());
                    cell = row.createCell(4);
                    cell.setCellValue(count.getPartBomCount());
                }
            }
            r++;
        }
        int mm = sheet.getLastRowNum();
        for (int x = 0; x <= mm; x++) {
            row = sheet.getRow(x);
            int nn = row.getLastCellNum();
            if (x == 0) {
                continue;
            }
            if (x == 1) {
                continue;
            }
            if (x == 2) {
                continue;
            }
            for (int y = 0; y < nn; y++) {
                cell = row.getCell(y);
                if (cell == null) {
                    continue;
                }
                cell.setCellStyle(cellStyle);
            }
        }
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("请核对订单，有疑问，请及时沟通。");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 20; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 18));
        r++;
        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.RIGHT); // 水平布局：居中
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        row = sheet.createRow(r);
        cell = row.createCell(0);
        sdf = new SimpleDateFormat("yyyy年MM月dd日");
        cell.setCellValue("下单人：" + producePlan.getCreateUserName() + "              日期：" + sdf.format(producePlan.getCreateTime()) + "             复核人：                                   ");
        cell.setCellStyle(cellStyle2);
        for (int i = 1; i < 20; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 18));
        // 高飞，2017-12-28，Excel导出的内容，添加包装任务，包装BOM
        List<PackTask> tasks;
        Set<Long> vids = new HashSet();
        sheet = wb.createSheet("包装任务");
        Object[] packTaskTitles = {"计划单号", "客户简称", "订单号", "批次号", "厂内名称", "客户产品名称", "卷长（米）", "卷重（KG）", "包装代码", "包装要求", "备注", "每托卷数", "数量", "已打托数"};
        row = sheet.createRow(0);
        writeRow(row, packTaskTitles);
        int i = 1;
        for (ProducePlanDetail ppd : details) {
            if (ppd.getProductIsTc() != 2) {
                continue;
            }
            tasks = ptService.findProduceTask(ppd.getId());
            for (PackTask t : tasks) {
                vids.add(t.getVid());
                row = sheet.createRow(i++);
                Object[] v = {producePlan.getProducePlanCode(), ppd.getConsumerName(), ppd.getSalesOrderCode(), ppd.getBatchCode(), ppd.getFactoryProductName(), ppd.getConsumerProductName(), ppd.getProductLength(), ppd.getProductWeight(), t.getCode(), ppd.getPackReq(), t.getMemo(), t.getRollsPerTray(), t.getProduceTotalCount(), null};
                writeRow(row, v);
            }
        }

        for (Long vid : vids) {
            map.clear();
            map.put("packVersionId", vid);
            List<FtcBcBomVersionDetail> list = ptService.findListByMap(FtcBcBomVersionDetail.class, map);

            FtcBcBomVersion v = ptService.findById(FtcBcBomVersion.class, vid);
            FtcBcBom b = ptService.findById(FtcBcBom.class, v.getBid());
            String ver = b.getCode() + "_版本" + v.getVersion();
            if (wb.getSheet(ver) != null) {
                continue;
            }
            sheet = wb.createSheet(ver);
            row = sheet.createRow(0);
            writeRow(row, "包装标准代码", "版本", "卷径/cm", "每托卷数", "托长/cm", "托宽/cm", "托高/cm", "包材重量/kg", "塑料膜要求", "摆放要求", "打包带要求", "标签要求", "小标签要求", "卷标签要求", "托标签要求", "缠绕要求", "其他要求");
            row = sheet.createRow(1);
            writeRow(row, b.getCode(), v.getVersion(), v.getRollDiameter(), v.getRollsPerPallet(), v.getPalletLength(), v.getPalletWidth(), v.getPalletHeight(), v.getBcTotalWeight(), v.getRequirement_suliaomo(), v.getRequirement_baifang(), v.getRequirement_dabaodai(), v.getRequirement_biaoqian(),
                    v.getRequirement_xiaobiaoqian(), v.getRequirement_juanbiaoqian(), v.getRequirement_tuobiaoqian(), v.getRequirement_chanrao(), v.getRequirement_other());
            row = sheet.createRow(2);
            writeRow(row, "物料代码", "标准码", "材料名称", "规格", "单位", "数量", "备注");
            i = 3;
            for (FtcBcBomVersionDetail d : list) {
                row = sheet.createRow(i++);
                writeRow(row, d.getPackMaterialCode(), d.getPackStandardCode(), d.getPackMaterialName(), d.getPackMaterialModel(), d.getPackUnit(), d.getPackMaterialCount(), d.getPackMemo());
            }
        }
        HttpUtils.download(response, wb, templateName);
    }

    private void writeRow(Row row, Object... values) {
        int i = 0;
        for (Object v : values) {
            if (v == null) {
                v = "";
            }
            row.createCell(i++).setCellValue(v + "");
        }
    }

    private void writeRow(Row row, CellStyle style, Object... values) {
        int i = 0;
        Cell cell;
        for (Object v : values) {
            if (v == null) {
                v = "";
            }
            cell = row.createCell(i++);
            cell.setCellStyle(style);
            cell.setCellValue(v + "");
        }
    }

    @NoLogin
    @Journal(name = "生产订单完成获取详情")
    @RequestMapping(value = "showProduce", method = RequestMethod.GET)
    public ModelAndView showProduce(String Ids) {
        ModelAndView mode = new ModelAndView(finishProducePage, model.addAttribute("Ids", Ids));
        return mode;
    }

    @Journal(name = "生产订单分配", logType = LogType.DB)
    @ResponseBody
    @RequestMapping(value = "allocated", method = RequestMethod.GET)
    public String showPlanProduce(String ids) {
        salesOrderService.setAllocated(ids);
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "生产订单完成获取详情")
    @RequestMapping(value = "findfinished", method = RequestMethod.POST)
    public String findfinished(String Ids) throws Exception {
        Page page = new Page();
        page.setAll(1);
        Filter filter = new Filter();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("Ids", "in:" + Ids);
        filter.setFilter(hm);
        filter.set("TC", "2");
        Map<String, Object> map = producePlanService.findfinished(filter, page);
        String json = GsonTools.toJson(map);
        return json;
    }

    @ResponseBody
    @Journal(name = "取消关闭订单")
    @RequestMapping(value = "noClose", method = RequestMethod.GET)
    public void noClose(String Ids) throws Exception {
        producePlanService.noClose(Ids);
    }

    @NoLogin
    @Journal(name = "根据套材的计划id导出编制计划Excel")
    @ResponseBody
    @RequestMapping(value = "export1", method = RequestMethod.GET)
    public void export1(Long id) throws Exception {
        // 根据id查出生产计划
        ProducePlan producePlan = producePlanService.findById(ProducePlan.class, id);
        HashMap<String, Object> map = new HashMap();
        map.put("producePlanId", id);
        List<ProducePlanDetail> details = producePlanService.findListByMap(ProducePlanDetail.class, map);
        // 如果查询到的集合不为空
        if (details.size() != 0) {
            for (int i = 0; i < details.size(); i++) {
                // 把客戶名称改为客户简称
                Consumer c = consumerService.findById(Consumer.class, details.get(i).getConsumerId());
                details.get(i).setConsumerName(c.getConsumerSimpleName());
            }
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "生产任务单(" + producePlan.getProducePlanCode() + ")";
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        Workbook wb = new SXSSFWorkbook(-1);
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);

        Sheet sheet = wb.createSheet();
        Row row;
        Cell cell;
        String columnName[] = new String[]{"客户简称", "客户订单号", "批号", "产品类别代码", "产品名称", "门幅(mm)", "产品数量（kg/套）", "产品卷重（卷长）", "包装要求", "出口（内销）", "交货日期", "使用工艺", "生产机器", "备注"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司生产任务单");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 14; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("Q/HS RH0042-2015");
        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle1.setBorderBottom(BorderStyle.NONE);
        cellStyle1.setBorderTop(BorderStyle.NONE);
        cellStyle1.setBorderRight(BorderStyle.THIN);
        cellStyle1.setBorderLeft(BorderStyle.THIN);
        cell.setCellStyle(cellStyle1);
        for (int i = 1; i < 14; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle1);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 13));
        r++;
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("任务单编号：" + producePlan.getProducePlanCode());
        CellStyle cellStyles = wb.createCellStyle();
        cellStyles.setAlignment(HorizontalAlignment.RIGHT); // 水平布局：居中
        cellStyles.setBorderBottom(BorderStyle.THIN);
        cellStyles.setBorderTop(BorderStyle.NONE);
        cellStyles.setBorderRight(BorderStyle.THIN);
        cellStyles.setBorderLeft(BorderStyle.THIN);
        cell.setCellStyle(cellStyles);
        for (int i = 1; i < 14; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyles);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 13));
        r++;
        row = sheet.createRow(r);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;
        sheet.setColumnWidth(0, 30 * 256);// 设置列宽
        sheet.setColumnWidth(1, 13 * 256);
        sheet.setColumnWidth(2, 13 * 256);
        sheet.setColumnWidth(3, 11 * 256);
        sheet.setColumnWidth(4, 13 * 256);
        sheet.setColumnWidth(6, 9 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 30 * 256);
        sheet.setColumnWidth(11, 30 * 256);
        // 遍历数据的长度，得到递增的行数
        for (ProducePlanDetail data : details) {
            map.clear();
            map.put("producePlanDetailId", data.getId());
            List<WeavePlan> wpList = producePlanService.findListByMap(WeavePlan.class, map);
            // 如果查询到的集合不为空
            if (wpList.size() != 0) {
                for (int i = 0; i < wpList.size(); i++) {
                    // 把客戶名称改为客户简称
                    Consumer c = consumerService.findById(Consumer.class, wpList.get(i).getConsumerId());
                    wpList.get(i).setConsumerName(c.getConsumerSimpleName());
                }
            }
            SalesOrderDetail sod = producePlanService.findById(SalesOrderDetail.class, data.getFromSalesOrderDetailId());
            SalesOrder so = producePlanService.findById(SalesOrder.class, sod.getSalesOrderId());
            for (WeavePlan wp : wpList) {
                row = sheet.createRow(r);
                // 当产品型号和原来的产品型号不同时，记录现在的型号并插入一行型号
                for (int j = 0; j < columnName.length; j++) {
                    cell = row.createCell(j);
                    switch (j) {
                        case 0:
                            if (wp.getConsumerName() != null) {
                                cell.setCellValue(wp.getConsumerName());
                            }
                            break;
                        case 1:
                            if (wp.getSalesOrderCode() != null) {
                                cell.setCellValue(wp.getSalesOrderCode());
                            }
                            break;
                        case 2:
                            if (wp.getBatchCode() != null) {
                                cell.setCellValue(wp.getBatchCode());
                            }
                            break;
                        case 3:
                            cell.setCellValue("");
                            break;
                        case 4:
                            if (wp.getProductModel() != null) {
                                cell.setCellValue(wp.getProductModel());
                            }
                            break;
                        case 5:
                            if (wp.getProductWidth() != null) {
                                cell.setCellValue(wp.getProductWidth());
                            }
                            break;
                        case 6:
                            if (wp.getOrderCount() != null) {
                                cell.setCellValue(wp.getRequirementCount());
                            }
                            break;
                        case 7:
                            if (wp.getProductLength() != null || wp.getProductWeight() != null) {
                                cell.setCellValue(wp.getProductWeight() == null ? wp.getProductLength() + " m" : wp.getProductWeight() + " kg");
                            }
                            break;
                        case 8:
                            String packReq = "";
                            if (wp.getBcBomCode() != null && wp.getBcBomVersion() != null) {
                                packReq += "包装：" + wp.getBcBomCode() + " 版本：" + wp.getBcBomVersion();
                            }

                            if (wp.getPackReq() != null) {
                                packReq += "包装需求：" + wp.getPackReq();

                            }
                            cell.setCellValue(packReq);
                            break;
                        case 9:
                            if (so.getSalesOrderIsExport() != null) {
                                cell.setCellValue(so.getSalesOrderIsExport() == 0 ? "外销" : "内销");
                            }
                            break;
                        case 10:
                            if (sod.getDeliveryTime() != null) {
                                cell.setCellValue(sdf.format(sod.getDeliveryTime()));
                            }
                            break;

                        case 11:
                            String procReq = "工艺：" + wp.getProcessBomCode() + " 版本：" + wp.getProcessBomVersion();
                            if (wp.getProcReq() != null) {
                                procReq += "; 工艺要求： " + wp.getProcReq();
                            }
                            cell.setCellValue(procReq);
                            break;
                        case 12:
                            if (wp.getDeviceCode() != null) {
                                cell.setCellValue(wp.getDeviceCode());
                            }
                            break;
                        case 13:
                            if (wp.getComment() != null) {
                                cell.setCellValue(wp.getComment());
                            }
                            break;
                    }
                }
                r++;
            }
        }
        int mm = sheet.getLastRowNum();
        for (int x = 0; x <= mm; x++) {
            row = sheet.getRow(x);
            int nn = row.getLastCellNum();
            if (x == 0) {
                continue;
            }
            if (x == 1) {
                continue;
            }
            if (x == 2) {
                continue;
            }
            for (int y = 0; y < nn; y++) {
                cell = row.getCell(y);
                if (cell == null) {
                    continue;
                }
                cell.setCellStyle(cellStyle);
            }
        }
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("请核对订单，有疑问，请及时沟通。");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 14; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 13));
        r++;
        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.RIGHT); // 水平布局：居中
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        row = sheet.createRow(r);
        cell = row.createCell(0);
        sdf = new SimpleDateFormat("yyyy年MM月dd日");
        cell.setCellValue("下单人：" + producePlan.getCreateUserName() + "              日期：" + sdf.format(producePlan.getCreateTime()) + "             复核人：                                   ");
        cell.setCellStyle(cellStyle2);
        for (int i = 1; i < 14; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle2);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 13));
        HttpUtils.download(response, wb, templateName);
    }

    @Journal(name = "生产进度跟踪报表")
    @RequestMapping(value = "schedule", method = RequestMethod.GET)
    public String schedule() {
        return ProductionSchedule;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取生产进度跟踪表信息")
    @RequestMapping("schelist")
    public String getSalesOrderOut(Filter filter, Page page) throws Exception {
        filter.clear();
        if (filter.get("start") != null) {
            filter.set("start", filter.get("start") + " 00:00:00");
        }
        if (filter.get("end") != null) {
            filter.set("end", filter.get("end") + " 23:59:59");
        }
        if (filter.get("sstart") != null) {
            filter.set("sstart", filter.get("sstart") + " 00:00:00");
        }
        if (filter.get("send") != null) {
            filter.set("send", filter.get("send") + " 23:59:59");
        }
        Map<String, Object> findSchedule = producePlanService.findSchedule(filter, page);
        List<Map<String, Object>> list = (List<Map<String, Object>>) findSchedule.get("rows");
        for (Map<String, Object> m : list) {
            if ("0".equals(m.get("PRODSTATUS"))) {
                m.put("PRODSTATUS", "未生产");
            } else if ("1".equals(m.get("PRODSTATUS"))) {
                m.put("PRODSTATUS", "生产中");
            } else if ("2".equals(m.get("PRODSTATUS"))) {
                m.put("PRODSTATUS", "已完成");
            }
        }

        List<Map<String, Object>> listFoot = new ArrayList();
        Map<String, Object> map = new HashMap();
        DecimalFormat df = new DecimalFormat("#.00");
        if (list.size() != 0) {
            List<Map<String, Object>> listWeight = (List<Map<String, Object>>) producePlanService.findSchedule(filter, page).get("rows");
            Object o1 = df.format(listWeight.get(0).get("PLANTOTALWEIGHT"));
            Object o2 = df.format(listWeight.get(0).get("REALTOTALWEIGHT"));
            Object o3 = df.format(listWeight.get(0).get("UNCOMPLETEWEIGHT"));
            map.put("CATEGORYNAME", "总计");
            map.put("PLANTOTALWEIGHT", o1);
            map.put("REALTOTALWEIGHT", o2);
            map.put("UNCOMPLETEWEIGHT", o3);
            listFoot.add(map);
        } else {
            map.put("CATEGORYNAME", "总计");
            map.put("PLANTOTALWEIGHT", 0);
            map.put("REALTOTALWEIGHT", 0);
            map.put("UNCOMPLETEWEIGHT", 0);
            listFoot.add(map);
        }
        findSchedule.put("footer", listFoot);
        return GsonTools.toJson(findSchedule);
    }

    @SuppressWarnings("unchecked")
    @NoLogin
    @Journal(name = "导出生产进度跟踪报表")
    @ResponseBody
    @RequestMapping(value = "export2", method = RequestMethod.GET)
    public void export2(Filter filter) throws Exception {
        filter.clear();
        if (filter.get("start") != null) {
            filter.set("start", filter.get("start") + " 00:00:00");
        }
        if (filter.get("end") != null) {
            filter.set("end", filter.get("end") + " 23:59:59");
        }
        if (filter.get("sstart") != null) {
            filter.set("sstart", filter.get("sstart") + " 00:00:00");
        }
        if (filter.get("send") != null) {
            filter.set("send", filter.get("send") + " 23:59:59");
        }
        Page p = new Page();
        p.setAll(1);
        p.setPage(1);
        List<Map<String, Object>> list = (List<Map<String, Object>>) producePlanService.findSchedule(filter, p).get("rows");
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Sheet sheet = wb.createSheet("生产进度跟踪报表");
        sheet.createFreezePane(0, 1, 0, 1);
        for (int i = 0; i < 43; i++) {
            sheet.setColumnWidth(i, 256 * 25 + 184);
        }
        Row row;
        Object[] columnNames = new Object[]{"任务单编号", "车间", "客户名称", "产品类别代码", "产品大类", "订单号", "计划类型", "批次号", "客户产品名称", "厂内名称", "部件名称", "产品门幅(mm)", "卷重(kg)", "卷长(m)", "关闭状态", "生产完成状态", "交期预警", "计划重量", "已生产重量", "未完成重量", "数量单位", "计划数量", "已生产数", "未完成数", "计划托数", "打包托数", "入库托数", "入库重量", "在库托数", "在库重量", "已发托数", "已发货重量", "出货时间", "计调下单时间", "计调安排的机台", "实际生产机台", "工艺代码", "工艺版本", "包装代码", "包装版本", "包装要求", "出口(内销)", "计调员", "销售员"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        writeRow(row, columnNames);
        Object[] values;
        int i = 1;
        String warning;
        CellStyle redStyle = wb.createCellStyle();
        CellStyle strikeoutStyle = wb.createCellStyle();
        Font red = wb.createFont();
        red.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        redStyle.setFont(red);
        Font strikeout = wb.createFont();
        strikeout.setStrikeout(true);
        strikeoutStyle.setFont(strikeout);
        CellStyle target;
        for (Map<String, Object> m : list) {
            warning = "否";
            target = wb.createCellStyle();
            row = sheet.createRow(i++);
            if (m.get("_CLOSED").toString().equals("已关闭")) {
                target = strikeoutStyle;
            } else {
                if (!m.get("_COMPLETED").toString().equals("已完成")) {
                    if (m.get("_YUJING").toString().equals("是")) {
                        target = redStyle;
                        warning = "是";
                    }
                }
            }
            values = new Object[]{m.get("PRODUCEPLANCODE"), m.get("WORKSHOP"), m.get("CONSUMERSIMPLENAME"), m.get("CATEGORYCODE"), m.get("CATEGORYNAME"), m.get("SALESORDERSUBCODE"), m.get("ISTURNBAGPLAN"), m.get("BATCHCODE"), m.get("CONSUMERPRODUCTNAME"), m.get("FACTORYPRODUCTNAME"), m.get("PARTNAME"), m.get("PRODUCTWIDTH"), m.get("PRODUCTWEIGHT"), m.get("PRODUCTLENGTH"), m.get("_CLOSED"), m.get("_COMPLETED"), warning, m.get("PLANTOTALWEIGHT"), m.get("REALTOTALWEIGHT"), m.get("UNCOMPLETEWEIGHT"), m.get("UNIT"), m.get("REQUIREMENTCOUNT"), m.get("REALROLLCOUNT"), m.get("UNCOMPLETEROLLCOUNT"), m.get("TOTALTRAYCOUNT"), m.get("REALPALLETCOUNT"), m.get("INBANKPALLETCOUNT"), m.get("INBANKPALLETWEIGHT"), m.get("STOCKPALLETCOUNT"), m.get("STOCKPALLETWEIGHT"), m.get("DELIVERYPALLETCOUNT"), m.get("DELIVERYPALLETWEIGHT"), m.get("DELEVERYDATE"), m.get("CREATETIME"), m.get("DEVICECODE"), m.get("_DEVICECODE"), m.get("PROCESSBOMCODE"), m.get("PROCESSBOMVERSION"), m.get("BCBOMCODE"), m.get("BCBOMVERSION"), m.get("PACKREQ"), m.get("_SALESORDERISEXPORT"), m.get("PLANBIZUSERNAME"), m.get("ORDERBIZUSERNAME")};
            writeRow(row, target, values);
        }
        HttpUtils.download(response, wb, "生产进度跟踪报表");
    }
}
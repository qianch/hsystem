/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.turnbag.controller;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlan;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlanDetails;
import com.bluebirdme.mes.planner.turnbag.service.ITurnBagPlanService;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.utils.FilterRules;
import com.bluebirdme.mes.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 翻包计划Controller
 *
 * @author 高飞
 * @Date 2017-2-9 11:28:32
 */
@Controller
@RequestMapping("/planner/tbp")
@Journal(name = "翻包计划")
public class TurnBagPlanController extends BaseController {
    /**
     * 翻包计划页面
     */
    final String index = "planner/turnbag/turnBagPlan";
    final String addOrEdit = "planner/turnbag/turnBagPlanAddOrEdit";
    @Resource
    ITurnBagPlanService turnBagPlanService;

    @Resource
    IAuditInstanceService auditInstanceService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取翻包计划列表信息")
    @RequestMapping("list")
    public String getTurnBagPlan(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(turnBagPlanService.findPageInfo(filter, page));
    }

    @Journal(name = "添加翻包计划页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public synchronized ModelAndView _add(ProducePlanDetail ppd) throws Exception {
        ppd = turnBagPlanService.findById(ProducePlanDetail.class, ppd.getId());
        if (StringUtils.isEmpty(ppd.getTurnBagCode())) {
            ppd.setTurnBagCode(serial());
            turnBagPlanService.update(ppd);
        }
        ProducePlan pp = turnBagPlanService.findById(ProducePlan.class, ppd.getProducePlanId());
        List<Map<String, Object>> list = turnBagPlanService.getDetails(ppd.getId());
        List<Long> ids = new ArrayList<>();
        List<String> batchCodes = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : list) {
            ids.add(((BigInteger) stringObjectMap.get("SALESORDERDETAILID")).longValue());
            batchCodes.add(stringObjectMap.get("BATCHCODE").toString());
        }
        List<Map<String, Object>> position = null;
        if (ids.size() > 0)
            position = turnBagPlanService.getGoodsPosition(ids, batchCodes);
        return new ModelAndView(addOrEdit, model.addAttribute("position", GsonTools.toJson(position)).addAttribute("tbCode", ppd.getTurnBagCode()).addAttribute("tbDept", pp.getWorkshop()).addAttribute("ppdId", ppd.getId()).addAttribute("details", GsonTools.toJson(list)));
    }

    @ResponseBody
    @Journal(name = "保存翻包计划", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@RequestBody List<TurnBagPlanDetails> list) throws Exception {
        turnBagPlanService.addOrEdit(list);
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "翻包计划已完成", logType = LogType.DB)
    @RequestMapping(value = "complete", method = RequestMethod.POST)
    public String complete(String id) throws Exception {
        TurnBagPlan turnBagPlan = turnBagPlanService.findById(TurnBagPlan.class, Long.parseLong(id));
        turnBagPlan.setIsCompleted(1);
        turnBagPlanService.update2(turnBagPlan);
        return ajaxSuccess();
    }

    @Journal(name = "编辑翻包计划页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(TurnBagPlan turnBagPlan) {
        turnBagPlan = turnBagPlanService.findById(TurnBagPlan.class, turnBagPlan.getId());
        SalesOrderDetail sod = turnBagPlanService.findById(SalesOrderDetail.class, turnBagPlan.getNewSalesOrderDetailsId());
        List<Map<String, Object>> list = turnBagPlanService.getDetails(turnBagPlan.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("turnBagPlan", turnBagPlan).addAttribute("newSalesOrderCode", sod.getSalesOrderSubCode()).addAttribute("details", GsonTools.toJson(list)));
    }

    @ResponseBody
    @Journal(name = "获取翻包订单明细")
    @RequestMapping(value = "details")
    public String getDetails(TurnBagPlan turnBagPlan) {
        List<Map<String, Object>> list = turnBagPlanService.getDetails(turnBagPlan.getId());
        return GsonTools.toJson(list);
    }

    @ResponseBody
    @Journal(name = "编辑翻包计划", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(TurnBagPlan turnBagPlan) throws Exception {
        turnBagPlanService.update(turnBagPlan);
        return GsonTools.toJson(turnBagPlan);
    }

    @ResponseBody
    @Journal(name = "删除翻包计划", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        String[] idArray = ids.split(",");
        TurnBagPlan tbp;
        List<TurnBagPlan> list = new ArrayList<>();
        for (String id : idArray) {
            tbp = turnBagPlanService.findById(TurnBagPlan.class, Long.parseLong(id));
            if (tbp != null && tbp.getAuditState() > 0) {
                return ajaxError(tbp.getTrunBagCode() + "在审核中或已通过，无法删除");
            }
            list.add(tbp);
        }
        turnBagPlanService.delete(list);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/order/select")
    public String selectOrder() {
        return "planner/turnbag/orderProductSelect";
    }

    @ResponseBody
    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/order/list")
    public String orderList(String filterRules, Page page) throws Exception {
        Filter filter = new Filter();
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
        return GsonTools.toJson(turnBagPlanService.findOrderPageInfo(filter, page));
    }

    @ResponseBody
    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/batchCode")
    public String getBatchCode(Long orderId, Long partId) throws Exception {
        List<Map<String, Object>> list = turnBagPlanService.getBatchCodeCountBySalesOrderCode(orderId, partId);
        List<Map<String, Object>> combobox = new ArrayList<>();
        Map<String, Object> map;
        for (Map<String, Object> m : list) {
            map = new HashMap<>();
            map.put("t", m.get("BATCHCODE") + "*" + m.get("COUNT") + "托");
            map.put("v", m.get("BATCHCODE"));
            combobox.add(map);
        }
        return GsonTools.toJson(combobox);
    }

    @ResponseBody
    @Journal(name = "获取生产订单和产品数据")
    @RequestMapping("/batchInfo")
    public String getBatchInfo(Long targetProducePlanDetailId, Long fromProducePlanDetailId) throws Exception {
        return GsonTools.toJson(turnBagPlanService.getBatchInfo(targetProducePlanDetailId, fromProducePlanDetailId));
    }

    @ResponseBody
    @Journal(name = "翻包任务单提交审核", logType = LogType.DB)
    @RequestMapping(value = "commitAudit", method = RequestMethod.POST)
    public String _commitAudit(Long id, String name) throws Exception {
        TurnBagPlan tbp = auditInstanceService.findById(TurnBagPlan.class, id);
        if (!tbp.getUserName().equals(session.getAttribute(Constant.CURRENT_USER_NAME))) {
            return ajaxError("只能由下达该计划的业务员才能提交审核！");
        }
        auditInstanceService.submitAudit(name, AuditConstant.CODE.FB, (Long) session.getAttribute(Constant.CURRENT_USER_ID), "planner/tbp/" + id, id, TurnBagPlan.class);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "查看审核页面")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ModelAndView view(TurnBagPlan turnBagPlan) throws Exception {
        turnBagPlan = turnBagPlanService.findById(TurnBagPlan.class, turnBagPlan.getId());
        SalesOrderDetail sod = turnBagPlanService.findById(SalesOrderDetail.class, turnBagPlan.getNewSalesOrderDetailsId());
        List<Map<String, Object>> list = turnBagPlanService.getDetails(turnBagPlan.getId());
        return new ModelAndView("planner/turnbag/turnBagPlanAudit", model.addAttribute("turnBagPlan", turnBagPlan).addAttribute("newSalesOrderCode", sod.getSalesOrderSubCode()).addAttribute("details", GsonTools.toJson(list)));
    }

    @ResponseBody
    @Journal(name = "获取编号")
    @RequestMapping(value = "serial", method = RequestMethod.POST)
    public synchronized String serial() {
        return turnBagPlanService.getSerial();
    }

    /**
     * 翻包领出页面
     *
     * @return
     */
    @Journal(name = "翻包领出页面")
    @RequestMapping("turnbagOut")
    public String turnbagOut() {
        return "planner/turnbag/turnbagOutRecord";
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取翻包领出列表信息")
    @RequestMapping("turnbagOut/list")
    public String getMaterialInRecord(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(turnBagPlanService.findTurnBagOutPageInfo(filter, page));
    }

    /**
     * 翻包领料导出
     */
    @NoAuth
    @ResponseBody
    @Journal(name = "导出翻包领料记录")
    @RequestMapping("turnBagOutExport")
    public void getTurnBagOutExport(Filter filter) throws Exception {
        Page page = new Page();
        page.setRows(99999);
        Map<String, Object> materialMap = turnBagPlanService.findTurnBagOutPageInfo(filter, page);
        List<Map<String, Object>> list = (List) materialMap.get("rows");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat df = new DecimalFormat("######0.0");
        String templateName = "翻包领料导出";
        Workbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);

        Sheet sheet = wb.createSheet();
        Row row;
        Cell cell;
        String[] columnName = new String[]{"产品条码", "翻包单号", "新订单号", "客户订单号", "计划单号", "厂内名称", "客户名称", "产品规格", "批次号", "门幅(mm)",
                "米长(m)", "旧订单号", "批次号", "门幅(mm)", "米长(m)", "客户名称", "计划单号", "产品规格", "重量", "操作人", "领出时间", "领出车间"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司翻包领料表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 22; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 21));
        r++;
        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 18 * 256);// 设置列宽
        sheet.setColumnWidth(1, 16 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 22 * 256);
        sheet.setColumnWidth(4, 22 * 256);
        sheet.setColumnWidth(5, 16 * 256);
        sheet.setColumnWidth(6, 16 * 256);
        sheet.setColumnWidth(7, 16 * 256);
        sheet.setColumnWidth(8, 16 * 256);
        sheet.setColumnWidth(9, 10 * 256);
        sheet.setColumnWidth(10, 10 * 256);
        sheet.setColumnWidth(11, 12 * 256);
        sheet.setColumnWidth(12, 12 * 256);
        sheet.setColumnWidth(13, 11 * 256);
        sheet.setColumnWidth(14, 11 * 256);
        sheet.setColumnWidth(15, 15 * 256);
        sheet.setColumnWidth(16, 15 * 256);
        sheet.setColumnWidth(17, 15 * 256);
        sheet.setColumnWidth(18, 15 * 256);
        sheet.setColumnWidth(19, 10 * 256);
        sheet.setColumnWidth(20, 20 * 256);
        sheet.setColumnWidth(21, 15 * 256);
        for (int a = 0; a < columnName.length; a++) {
            cell = row.createCell(a);
            cell.setCellValue(columnName[a]);
            cell.setCellStyle(cellStyle);
        }
        r++;
        for (Map<String, Object> data : list) {
            row = sheet.createRow(r);

            for (int j = 0; j < columnName.length; j++) {
                cell = row.createCell(j);
                switch (j) {
                    case 0:
                        //产品条码
                        if (data.get("TRAYCODE") != null) {
                            cell.setCellValue(data.get("TRAYCODE").toString());
                        }
                        break;
                    case 1:
                        //翻包单号
                        if (data.get("TURNBAGCODE") != null) {
                            cell.setCellValue(data.get("TURNBAGCODE").toString());
                        }
                        break;
                    case 2:
                        //新订单号
                        if (data.get("SALESORDERCODE") != null) {
                            cell.setCellValue(data.get("SALESORDERCODE").toString());
                        }
                        break;
                    case 3:
                        //客户订单号
                        if (data.get("SALESORDERSUBCODEPRINT") != null) {
                            cell.setCellValue(data.get("SALESORDERSUBCODEPRINT").toString());
                        }
                        break;
                    case 4:
                        //计划单号
                        if (data.get("PLANCODE") != null) {
                            cell.setCellValue(data.get("PLANCODE").toString());
                        }
                        break;

                    case 5:
                        //厂内名称
                        if (data.get("FACTORYPRODUCTNAME") != null) {
                            cell.setCellValue(data.get("FACTORYPRODUCTNAME").toString());
                        }
                        break;

                    case 6:
                        //客户名称
                        if (data.get("CONSUMERNAME") != null) {
                            cell.setCellValue(data.get("CONSUMERNAME").toString());
                        }
                        break;
                    case 7:
                        //产品规格
                        if (data.get("PRODUCTMODEL") != null) {
                            cell.setCellValue(data.get("PRODUCTMODEL").toString());
                        }
                        break;
                    case 8:
                        //批次号
                        if (data.get("BATCHCODE") != null) {
                            cell.setCellValue(data.get("BATCHCODE").toString());
                        }
                        break;

                    case 9:
                        //门幅
                        if (data.get("PRODUCTWIDTH") != null) {
                            cell.setCellValue(data.get("PRODUCTWIDTH").toString());
                        }
                        break;
                    case 10:
                        //米长
                        if (data.get("PRODUCTLENGTH") != null) {
                            cell.setCellValue(data.get("PRODUCTLENGTH").toString());
                        }
                        break;
                    case 11:
                        //旧订单号
                        if (data.get("OLDSALESORDERCODE") != null) {
                            cell.setCellValue(data.get("OLDSALESORDERCODE").toString());
                        }
                        break;
                    case 12:
                        //旧批次号
                        if (data.get("OLDBATCHCODE") != null) {
                            cell.setCellValue(data.get("OLDBATCHCODE").toString());
                        }
                        break;
                    case 13:
                        //旧门幅
                        if (data.get("OLDPRODUCTWIDTH") != null) {
                            cell.setCellValue(data.get("OLDPRODUCTWIDTH").toString());
                        }
                        break;
                    case 14:
                        //旧米长
                        if (data.get("OLDPRODUCTLENGTH") != null) {
                            cell.setCellValue(data.get("OLDPRODUCTLENGTH").toString());
                        }
                        break;
                    case 15:
                        //旧客户名称
                        if (data.get("OLDCONSUMERNAME") != null) {
                            cell.setCellValue(data.get("OLDCONSUMERNAME").toString());
                        }
                        break;
                    case 16:
                        //旧计划单号
                        if (data.get("OLDPRODUCEPLANCODE") != null) {
                            cell.setCellValue(data.get("OLDPRODUCEPLANCODE").toString());
                        }
                        break;
                    case 17:
                        //旧产品规格
                        if (data.get("OLDPRODUCTMODEL") != null) {
                            cell.setCellValue(data.get("OLDPRODUCTMODEL").toString());
                        }
                        break;
                    case 18:
                        //重量
                        if (data.get("PRODUCTWEIGHT") != null) {
                            cell.setCellValue(df.format(data.get("PRODUCTWEIGHT")));
                        }
                        break;
                    case 19:
                        //操作人
                        if (data.get("OPTUSERNAME") != null) {
                            cell.setCellValue(data.get("OPTUSERNAME").toString());
                        }
                        break;
                    case 20:
                        //领出时间
                        if (data.get("OUTDATE") != null) {
                            cell.setCellValue(sf.format(data.get("OUTDATE")));
                        }
                        break;
                    case 21:
                        //领出车间
                        if (data.get("OUTADDRESS") != null) {
                            cell.setCellValue(data.get("OUTADDRESS").toString());
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }
}
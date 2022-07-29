/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.weave.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlanDevices;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.utils.HttpUtils;
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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编织计划Controller
 *
 * @author 肖文彬
 * @Date 2016-10-18 13:37:59
 */
@Controller
@RequestMapping("planner/weavePlan")
@Journal(name = "编织计划")
public class WeavePlanController extends BaseController {
    /**
     * 编织计划页面
     */
    final String index = "planner/weave/weavePlan";
    final String addOrEdit = "planner/weave/weavePlanAddOrEdit";
    final String addDevice = "planner/weave/addDevice";
    final String choose_Device = "planner/weave/device";
    final String select = "planner/weave/weaveSelect";
    final String selecttwo = "planner/weave/weaveSelect_two";
    final String finishProduce = "planner/weave/finishProducePage";
    final String viewBjInfo = "planner/scheduling/checkBjInfo";
    final String bjhzview = "planner/scheduling/checkBjHzInfo";

    @Resource
    IWeavePlanService weavePlanService;


    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取编织计划列表信息")
    @RequestMapping("list")
    public String getWeavePlan(String planCode) throws Exception {
        return GsonTools.toJson(weavePlanService.findWeavePlan(planCode));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取编织计划列表信息")
    @RequestMapping("weaveList")
    public String getWeavePlans(Filter filter, Page page) {
        if (filter.get("isFinish") == null) {
            filter.set("isFinish", "-1");
        }
        if (filter.get("closed") == null) {
            filter.set("closed", "0");
        }
        if (filter.get("isTurnBagPlan") == null) {
            filter.set("isTurnBagPlan", "生产");
        }
        Map<String, Object> findPageInfo = weavePlanService.findWeavePageInfo(filter, page);
        return GsonTools.toJson(findPageInfo);
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取机台裁剪列表信息")
    @RequestMapping("cutList1")
    public String getDeviceCut(Filter filter, Page page) {
        Map<String, Object> findPageInfo2 = weavePlanService.findWeavePageInfo2(filter, page);
        return GsonTools.toJson(findPageInfo2);
    }

    @Journal(name = "选择计划页面")
    @RequestMapping(value = "select", method = RequestMethod.GET)
    public ModelAndView select(String workShop) {
        System.out.println(workShop);
        return new ModelAndView(select, model.addAttribute("workShop", workShop));
    }


    @Journal(name = "选择套材计划页面")
    @RequestMapping(value = "selecttwo", method = RequestMethod.GET)
    public ModelAndView selecttwo(String workShop) {
        System.out.println(workShop);
        return new ModelAndView(selecttwo, model.addAttribute("workShop", workShop));
    }


    @Journal(name = "生产完成确定页面")
    @RequestMapping(value = "finishProduce", method = RequestMethod.GET)
    public ModelAndView finishProduce(String id) {
        return new ModelAndView(finishProduce, model.addAttribute("id", id));
    }

    @ResponseBody
    @Journal(name = "生产订单完成获取详情")
    @RequestMapping(value = "findfinished", method = RequestMethod.POST)
    public String findfinished(String id) throws Exception {
        Page page = new Page();
        page.setAll(1);
        Filter filter = new Filter();
        HashMap<String, String> hm = new HashMap<>();
        hm.put("id", id);
        filter.setFilter(hm);
        Map<String, Object> map = weavePlanService.findfinished(filter, page);
        return GsonTools.toJson(map);
    }

    @ResponseBody
    @Journal(name = "查询未完成的编织计划")
    @RequestMapping(value = "findUnCompletedWeavePlan", method = RequestMethod.POST)
    public String findUnCompletedWeavePlan(Filter filter, Page page) {
        Map<String, Object> map = weavePlanService.findNofinish(filter, page);
        return GsonTools.toJson(map);
    }


    @Journal(name = "添加编织计划页面", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(WeavePlan weavePlan) {
        return new ModelAndView(addOrEdit, model.addAttribute("weavePlan", weavePlan));
    }

    @ResponseBody
    @Journal(name = "加载编织计划")
    @RequestMapping(value = "findWeavePlan", method = RequestMethod.POST)
    @Valid
    public String add() throws Exception {
        return GsonTools.toJson(weavePlanService.PlanCodeCombobox());
    }

    @Journal(name = "编辑编织计划页面", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(Long id) {
        return null;
    }

    @Deprecated
    @ResponseBody
    @Journal(name = "编辑编织计划", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(WeavePlan weavePlan, Long[] did, Integer[] dCount, String time) throws Exception {
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "查询编织计划下的机台信息")
    @RequestMapping(value = "device", method = RequestMethod.POST)
    public String find(Long wid, String date, String workshop, Long id) throws Exception {
        List<Map<String, Object>> li = weavePlanService.findDevice(wid, date, workshop, id);
        return GsonTools.toJson(li);
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
        return GsonTools.toJson(weavePlanService.dataList(filter, page));
    }

    @ResponseBody
    @Journal(name = "编织计划标记已完成", logType = LogType.DB)
    @RequestMapping(value = "isFinish", method = RequestMethod.POST)
    @Valid
    public String finish(String ids) throws Exception {
        weavePlanService.updateState(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "编辑计划取消完成", logType = LogType.DB)
    @RequestMapping(value = "iscloseFinish", method = RequestMethod.POST)
    @Valid
    public String iscloseFinish(String ids) throws Exception {
        if (ids != null) {
            for (String id : ids.split(",")) {
                WeavePlan w = weavePlanService.findById(WeavePlan.class, Long.parseLong(id));
                w.setIsFinished(-1);
                weavePlanService.update2(w);
            }
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "编辑计划取消关闭", logType = LogType.DB)
    @RequestMapping(value = "isCancelClose", method = RequestMethod.POST)
    public String isCancelClose(String ids) throws Exception {
        for (String id : ids.split(",")) {
            WeavePlan w = weavePlanService.findById(WeavePlan.class, Long.parseLong(id));
            w.setIsFinished(-1);
            w.setClosed(0);
            weavePlanService.update2(w);
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "优先排序", logType = LogType.DB)
    @RequestMapping(value = "sort", method = RequestMethod.POST)
    @Valid
    public String sort(String id) throws Exception {
        Long time = System.currentTimeMillis();
        weavePlanService.updateSort(id, time);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "取消优先排序", logType = LogType.DB)
    @RequestMapping(value = "cancelSort", method = RequestMethod.POST)
    @Valid
    public String cancelSort(String id) {
        Long time = null;
        weavePlanService.updateSort(id, time);
        return Constant.AJAX_SUCCESS;
    }

    @RequestMapping("device")
    public String chooseDevice(String singleSelect, String workShop) {
        model.addAttribute("singleSelect", singleSelect).addAttribute("workShop", workShop);
        return choose_Device;
    }

    @Journal(name = "打开分配机台页面")
    @RequestMapping(value = "addDevice", method = RequestMethod.GET)
    public ModelAndView devices(Long weavePlanId, String date, String workshop) {
        return null;
    }

    @Journal(name = "机台排产")
    @RequestMapping(value = "addDevice", method = RequestMethod.POST)
    public String savePlanDevices(@RequestBody List<WeavePlanDevices> devices) {
        weavePlanService.saveDevices(devices);
        return ajaxSuccess();
    }


    @NoAuth
    @ResponseBody
    @Journal(name = "导出编织计划信息")
    @RequestMapping(value = "export")
    public void getProductStockExport1(Filter filter) throws Exception {
        Page page = new Page();
        page.setRows(99999);
        if (filter.get("isFinish") == null) {
            filter.set("isFinish", "-1");
        }
        if (filter.get("closed") == null) {
            filter.set("closed", "0");
        }
        if (filter.get("isTurnBagPlan") == null) {
            filter.set("isTurnBagPlan", "生产");
        }
        Map<String, Object> weavePlan = weavePlanService.findWeavePageInfo(filter, page);
        List<Map<String, Object>> list = (List) weavePlan.get("rows");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        String templateName = "编织计划记录单";
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
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
        String[] columnName = new String[]{"完成状态", "关闭状态", "生产计划单号", "分配状态", "销售订单号",
                "批次号", "工艺名称", "厂内产品名称", "客户产品名称", "门幅(mm)", "卷长(m)", "预留长度(m)", "卷重(kg)", "部件名称",
                "图号", "卷号", "层号", "计划数量（卷）", "生产进度", "打包托数/总托数", "客户简称", "产品属性", "出货日期", "工艺代码", "工艺版本", "包装代码", "包装版本", "包装要求", "备注"};
        int r = 0;// 从第1行开始写数据
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("浙江恒石纤维基业有限公司编织计划表");
        cell.setCellStyle(cellStyle);
        for (int i = 1; i < 14; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 28));
        r++;
        row = sheet.createRow(r);
        sheet.setColumnWidth(0, 10 * 256);// 设置列宽
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 25 * 256);
        sheet.setColumnWidth(3, 18 * 256);
        sheet.setColumnWidth(4, 18 * 256);
        sheet.setColumnWidth(5, 24 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 15 * 256);
        sheet.setColumnWidth(11, 15 * 256);
        sheet.setColumnWidth(12, 15 * 256);
        sheet.setColumnWidth(13, 15 * 256);
        sheet.setColumnWidth(14, 15 * 256);
        sheet.setColumnWidth(15, 15 * 256);
        sheet.setColumnWidth(16, 15 * 256);
        sheet.setColumnWidth(17, 15 * 256);
        sheet.setColumnWidth(18, 15 * 256);
        sheet.setColumnWidth(19, 15 * 256);
        sheet.setColumnWidth(20, 15 * 256);
        sheet.setColumnWidth(21, 15 * 256);
        sheet.setColumnWidth(22, 15 * 256);
        sheet.setColumnWidth(23, 15 * 256);
        sheet.setColumnWidth(24, 15 * 256);
        sheet.setColumnWidth(25, 40 * 256);
        sheet.setColumnWidth(26, 15 * 256);
        sheet.setColumnWidth(27, 15 * 256);
        sheet.setColumnWidth(28, 25 * 256);
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
                        if (data.get("ISFINISHED") != null) {
                            if (Integer.parseInt(data.get("ISFINISHED").toString()) == 1) {
                                cell.setCellValue("已完成");
                            } else {
                                cell.setCellValue("未完成");
                            }
                        }
                        break;
                    case 1:
                        if (data.get("CLOSED") == null || Integer.valueOf(data.get("CLOSED").toString()) == 0) {

                            cell.setCellValue("正常");
                        } else if (Integer.parseInt(data.get("CLOSED").toString()) == 1) {
                            cell.setCellValue("已关闭");
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 2:
                        if (data.get("PLANCODE") != null) {
                            cell.setCellValue(data.get("PLANCODE").toString());
                        }
                        break;
                    case 3:
                        if (data.get("ISPLANED") == null || Integer.parseInt(data.get("ISPLANED").toString()) == 0) {

                            cell.setCellValue("未分配");
                        } else if (Integer.parseInt(data.get("ISPLANED").toString()) == 1) {
                            cell.setCellValue("已分配");
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 4:
                        if (data.get("SALESORDERSUBCODE") != null) {
                            cell.setCellValue(data.get("SALESORDERSUBCODE").toString());
                        }
                        break;
                    case 5:
                        if (data.get("BATCHCODE") != null) {
                            cell.setCellValue(data.get("BATCHCODE").toString());
                        }
                        break;
                    case 6:
                        if (data.get("PRODUCTMODEL") != null) {
                            cell.setCellValue(data.get("PRODUCTMODEL").toString());
                        }
                        break;
                    case 7:
                        if (data.get("FACTORYPRODUCTNAME") != null) {
                            cell.setCellValue(data.get("FACTORYPRODUCTNAME").toString());
                        }
                        break;
                    case 8:
                        if (data.get("CONSUMERPRODUCTNAME") != null) {
                            cell.setCellValue(data.get("CONSUMERPRODUCTNAME").toString());
                        }
                        break;

                    case 9:
                        if (data.get("PRODUCTWIDTH") != null) {
                            cell.setCellValue(data.get("PRODUCTWIDTH").toString());
                        }
                        break;
                    case 10:
                        if (data.get("PRODUCTLENGTH") != null) {
                            cell.setCellValue(data.get("PRODUCTLENGTH").toString());
                        }
                        break;
                    case 11:
                        if (data.get("RESERVELENGTH") != null) {
                            cell.setCellValue(data.get("RESERVELENGTH").toString());
                        }
                        break;
                    case 12:
                        if (data.get("PRODUCTWEIGHT") != null) {
                            cell.setCellValue(data.get("PRODUCTWEIGHT").toString());
                        }
                        break;

                    case 13:
                        if (data.get("PARTNAME") != null) {
                            cell.setCellValue(data.get("PARTNAME").toString());
                        }
                        break;
                    case 14:
                        if (data.get("DRAWNO") != null) {
                            cell.setCellValue(data.get("DRAWNO").toString());
                        }
                        break;
                    case 15:
                        if (data.get("ROLLNO") != null) {
                            cell.setCellValue(data.get("ROLLNO").toString());
                        }
                        break;
                    case 16:
                        if (data.get("LEVELNO") != null) {
                            cell.setCellValue(data.get("LEVELNO").toString());
                        }
                        break;
                    case 17:
                        if (data.get("REQUIREMENTCOUNT") != null) {
                            String REQUIREMENTCOUNT = data.get("REQUIREMENTCOUNT").toString();
                            cell.setCellValue(REQUIREMENTCOUNT.substring(0, REQUIREMENTCOUNT.indexOf(".")));
                        }
                        break;
                    case 18:
                        String rc = data.get("RC").toString();
                        String REQUIREMENTCOUNT = data.get("REQUIREMENTCOUNT").toString();
                        if ("0".equals(rc)) {
                            cell.setCellValue("-" + "/" + REQUIREMENTCOUNT.substring(0, REQUIREMENTCOUNT.indexOf(".")) + "卷");
                        } else {
                            cell.setCellValue(rc + "/" + REQUIREMENTCOUNT.substring(0, REQUIREMENTCOUNT.indexOf(".")) + "卷");

                        }
                        break;
                    case 19:
                        String tc = data.get("TC").toString();
                        if (data.get("TOTALTRAYCOUNT") != null) {
                            if ("0".equals(tc) || tc == null) {
                                cell.setCellValue("-" + "/" + data.get("TOTALTRAYCOUNT").toString() + "托");
                            } else {
                                cell.setCellValue(tc + "/" + data.get("TOTALTRAYCOUNT").toString() + "托");
                                break;
                            }
                        } else {
                            cell.setCellValue("-" + "/" + "-" + "托");
                        }
                        break;
                    case 20:
                        if (data.get("CONSUMERSIMPLENAME") != null) {
                            cell.setCellValue(data.get("CONSUMERSIMPLENAME").toString());
                        }
                        break;
                    case 21:
                        if (data.get("PRODUCTTYPE") != null) {
                            String PRODUCTTYPE = data.get("PRODUCTTYPE").toString();
                            switch (PRODUCTTYPE) {
                                case "1" -> cell.setCellValue("大卷产品");
                                case "2" -> cell.setCellValue("中卷产品");
                                case "3" -> cell.setCellValue("小卷产品");
                                default -> cell.setCellValue("其他产品");
                            }
                        }
                        break;
                    case 22:
                        if (data.get("DELEVERYDATE") != null) {
                            cell.setCellValue(data.get("DELEVERYDATE").toString());
                        }
                        break;
                    case 23:
                        if (data.get("PROCESSBOMCODE") != null) {
                            cell.setCellValue(data.get("PROCESSBOMCODE").toString());
                        }
                        break;
                    case 24:
                        if (data.get("PROCESSBOMVERSION") != null) {
                            cell.setCellValue(data.get("PROCESSBOMVERSION").toString());
                        }
                        break;
                    case 25:
                        if (data.get("BCBOMCODE") != null) {
                            cell.setCellValue(data.get("BCBOMCODE").toString());
                        }
                        break;
                    case 26:
                        if (data.get("BCBOMVERSION") != null) {
                            cell.setCellValue(data.get("BCBOMVERSION").toString());
                        }
                        break;
                    case 27:
                        if (data.get("PREQ") != null) {
                            cell.setCellValue(data.get("PREQ").toString());
                        }
                        break;
                    case 28:
                        if (data.get("COM") != null) {
                            cell.setCellValue(data.get("COM").toString());
                        }
                        break;
                }
            }
            r++;
        }
        HttpUtils.download(response, wb, templateName);
    }

    @ResponseBody
    @Journal(name = "获取编织计划包装信息")
    @RequestMapping("getWeavePlanPackTask")
    public String getWeavePlanPackTask(Long wid) {
        return GsonTools.toJson(weavePlanService.getWeavePlanPackTask(wid));
    }

    @Journal(name = "选择计划页面")
    @RequestMapping(value = "checkbjinfo", method = RequestMethod.GET)
    public ModelAndView checkbjinfo(String id, String yx, String partname) {
        return new ModelAndView(viewBjInfo, model.addAttribute("id", id).addAttribute("yx", yx).addAttribute("partname", partname));
    }

    @Journal(name = "选择部件汇总信息页面")
    @RequestMapping(value = "checkbjhzinfo", method = RequestMethod.GET)
    public ModelAndView checkbjinfo(String id) {
        return new ModelAndView(bjhzview, model.addAttribute("id", id));
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "查看部件信息")
    @RequestMapping("viewBjInfo")
    public String getYxInfo(String id, String yx, String partname) throws Exception {
        Map<String, Object> bjinfos = weavePlanService.getBjDetails(id, yx, partname);
        return GsonTools.toJson(bjinfos);
    }


    @NoAuth
    @ResponseBody
    @Journal(name = "查看部件汇总信息")
    @RequestMapping("viewBjhzInfo")
    public String getBjHzInfo(String id) {
        return GsonTools.toJson(weavePlanService.findDevicePlans(id));
    }
}
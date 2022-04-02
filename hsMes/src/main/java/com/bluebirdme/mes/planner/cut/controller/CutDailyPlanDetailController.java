/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.cut.controller;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.common.service.IProcessService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlan;
import com.bluebirdme.mes.planner.cut.entity.CutDailyPlanDetail;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.cut.service.ICutDailyPlanDetailService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IDepartmentService;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.*;

/**
 * 日计划和裁剪计划表Controller
 *
 * @author 宋黎明
 * @Date 2016-11-28 10:32:39
 */
@Controller
@RequestMapping("/cutDailyPlan")
@Journal(name = "日计划和裁剪计划表")
public class CutDailyPlanDetailController extends BaseController {

    // 日计划和裁剪计划表页面
    final String index = "planner/cutPlanDaily/cutDailyPlan";
    final String addOrEdit = "planner/cutPlanDaily/cutDailyPlanAddOrEdit";
    final String audit = "planner/cutPlanDaily/cutDailyPlanAudit";
    final String select = "planner/cutPlanDaily/cutSelect";
    final String cutPlanUser = "planner/cutPlanDaily/addUser";
    final String editUsers = "planner/cutPlanDaily/userAddOrEdit";
    final String editUsersNum = "planner/cutPlanDaily/editUsersNum";

    @Resource
    ICutDailyPlanDetailService cutDailyPlanDetailService;
    @Resource
    IAuditInstanceService auditInstanceService;
    @Resource
    IProcessService processService;
    @Resource
    IDepartmentService departmentService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取日计划列表信息")
    @RequestMapping("list")
    public String getCutDailyPlanDetail(Filter filter, Page page) throws Exception {
        Map<String, Object> map = cutDailyPlanDetailService.findPageInfo(filter, page);
        return GsonTools.toJson(map);
    }


    @Journal(name = "添加日计划页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(CutDailyPlanDetail cutDailyPlanDetail) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("planDate", new Date());
        return new ModelAndView(addOrEdit, model.addAttribute("cutDailyPlan", map));
    }

    @ResponseBody
    @Journal(name = "查询未完成的裁剪计划")
    @RequestMapping(value = "findCutPlan", method = RequestMethod.POST)
    public String findCutPlan(Filter filter, Page page) throws Exception {
        Map<String, Object> map = cutDailyPlanDetailService.findNofinish(filter, page);
        return GsonTools.toJson(map);
    }

    /**
     * @param cutDailyPlan 裁剪日计划
     * @param cids         裁剪计划ID字符串
     * @param counts       裁剪计划对应的套数
     * @param partsNames   裁剪计划部件，人员，套数
     * @param pCounts      各个部件需要的数量
     * @param partNames    各个部件名称
     * @param comments     裁剪计划备注
     * @param uids         各个部件人员ID
     * @return
     * @throws Exception
     */
    @ResponseBody
    @Journal(name = "保存日计划和裁剪计划表", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(CutDailyPlan cutDailyPlan, Long cids[], Integer counts[], String partsNames[], String pCounts[], String partNames[], String comments[], String uids[], String partids[]) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("planDate", cutDailyPlan.getPlanDate());
        map.put("workShop", cutDailyPlan.getWorkShop());
        map.put("isClosed", 0);
        String[] workShopCodes = cutDailyPlan.getWorkShopCode().split(",");
        for (int i = 0; i < workShopCodes.length; i++) {
            Department department = departmentService.findOne(Department.class, "code", workShopCodes[i]);
            cutDailyPlan.setWorkShopCode(workShopCodes[i]);
            cutDailyPlan.setWorkShop(department.getName());
            if (cutDailyPlan.getId() == null) {
                cutDailyPlan.setOperator((Long) session.getAttribute(Constant.CURRENT_USER_ID));
            } else {
                CutDailyPlan _cutDailyPlan = cutDailyPlanDetailService.findById(CutDailyPlan.class, cutDailyPlan.getId());
                if (!_cutDailyPlan.getOperator().equals((Long) session.getAttribute(Constant.CURRENT_USER_ID))) {
                    return ajaxError("只能由下该计划的业务员编辑！");
                }
            }
            cutDailyPlan.setAuditState(AuditConstant.RS.SUBMIT);
            cutDailyPlan.setIsClosed(0);
            cutDailyPlanDetailService.saveD(cutDailyPlan, cids, counts, partsNames, pCounts, partNames, comments, uids, partids);
        }
        return GsonTools.toJson(cutDailyPlan);
    }

    @Journal(name = "编辑日计划和裁剪计划表页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(CutDailyPlan cutDailyPlan) {
        cutDailyPlan = cutDailyPlanDetailService.findById(CutDailyPlan.class, cutDailyPlan.getId());
        List<Map<String, Object>> map = cutDailyPlanDetailService.findC(cutDailyPlan.getId());
        System.out.println(GsonTools.toJson(map));
        return new ModelAndView(addOrEdit, model.addAttribute("cutDailyPlan", cutDailyPlan).addAttribute("selectedPlans", GsonTools.toJson(map)));
    }

    @ResponseBody
    @Journal(name = "删除日计划和裁剪计划表", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        cutDailyPlanDetailService.deletePlans(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "查询日计划下的裁剪计划")
    @RequestMapping(value = "findC", method = RequestMethod.POST)
    public String findCutPlan(Long id) throws Exception {
        return GsonTools.toJson(cutDailyPlanDetailService.findC(id));
    }

    @ResponseBody
    @Journal(name = "提交审核翻包计划")
    @RequestMapping(value = "commitAudit", method = RequestMethod.POST)
    @Valid
    public String _commitAudit(Long id, String name) throws Exception {
        CutDailyPlan _cutDailyPlan = cutDailyPlanDetailService.findById(CutDailyPlan.class, id);
        if (!_cutDailyPlan.getOperator().equals((Long) session.getAttribute(Constant.CURRENT_USER_ID))) {
            return ajaxError("只能由下该计划的业务员提交审核！");
        }
        auditInstanceService.submitAudit(name, AuditConstant.CODE.CRJH, (Long) session.getAttribute(Constant.CURRENT_USER_ID), "cutDailyPlan/audit?id=" + id, Long.valueOf(id), CutDailyPlan.class);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "查看审核翻包计划页面")
    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public ModelAndView audit(String id) {
        CutDailyPlan cutDailyPlan = cutDailyPlanDetailService.findById(CutDailyPlan.class, Long.valueOf(id));
        return new ModelAndView(audit, model.addAttribute("cutDailyPlan", cutDailyPlan));
    }

    @Journal(name = "选择日计划页面")
    @RequestMapping(value = "select", method = RequestMethod.GET)
    public ModelAndView select() {
        return new ModelAndView(select);
    }

    /**
     * @param id       裁剪计划ID
     * @param date     日期
     * @param workshop 车间
     * @return
     */
    @Journal(name = "打开分配人员页面")
    @RequestMapping(value = "users", method = RequestMethod.GET)
    public ModelAndView users(Long id, String date, String workshop) {

        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> partNames = new ArrayList<Map<String, Object>>();

        map.put("planDetailId", id);

        List<ProducePlanDetailPartCount> parts = cutDailyPlanDetailService.findListByMap(ProducePlanDetailPartCount.class, map);

        for (ProducePlanDetailPartCount p : parts) {
            map = new HashMap<String, Object>();
            map.put("PARTID", p.getPartId());
            map.put("TCPROCBOMVERSIONPARTSNAME", p.getPartName());
            map.put("TCPROCBOMVERSIONPARTSCOUNT", p.getPlanPartCount());
            map.put("USERCOUNT", session.getAttribute(Constant.CURRENT_USER_NAME) + "(1)");
            map.put("USERCOUNTS", "1");
            map.put("USERIDS", session.getAttribute(Constant.CURRENT_USER_ID));
            partNames.add(map);
        }
        return new ModelAndView(cutPlanUser, model.addAttribute("partNames", GsonTools.toJson(partNames)));
    }

    @Journal(name = "打开编辑人员页面")
    @RequestMapping(value = "editUsers", method = RequestMethod.GET)
    public ModelAndView editUsers() {
        return new ModelAndView(editUsers);
    }

    @Journal(name = "打开编辑人员数量页面")
    @RequestMapping(value = "editUsersNum", method = RequestMethod.GET)
    public ModelAndView editUsersNum() {
        return new ModelAndView(editUsersNum);
    }

    @NoLogin
    @Journal(name = "根据日计划id导出Excel")
    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void export(Long id) throws Exception {
        CutDailyPlan cutDailyPlan = cutDailyPlanDetailService.findById(CutDailyPlan.class, id);
        User user = cutDailyPlanDetailService.findById(User.class, cutDailyPlan.getOperator());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cutPlanDailyId", cutDailyPlan.getId());
        //获取裁剪日计划明细
        List<CutDailyPlanDetail> cpdList = cutDailyPlanDetailService.findListByMap(CutDailyPlanDetail.class, map);
        String templateName;
        Workbook wb = new SXSSFWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);

        CellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.LEFT); // 左对齐
//		cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle1.setBorderBottom(BorderStyle.THIN);
        cellStyle1.setBorderTop(BorderStyle.THIN);
        cellStyle1.setBorderRight(BorderStyle.THIN);
        cellStyle1.setBorderLeft(BorderStyle.THIN);

        Sheet sheet = wb.createSheet();
        Row row = null;
        Cell cell = null;
        String columnName[] = new String[]{"人员名称", "数量", "部件名称", "生产任务单编号", "订单号", "批号",
                "产品规格", "定长/定重", "客户名称", "生产工艺", "包装工艺", "备注"};
        int r = 0;// 从第1行开始写数据

        String title = cutDailyPlan.getWorkShop();
        templateName = title;
        String date = cutDailyPlan.getPlanDate();
        String operator = user.getUserName();

        //第一行
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue(title + "生产安排表");
        cell.setCellStyle(cellStyle);
        for (int x = 1; x < 11; x++) {
            cell = row.createCell(x);
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, 11));
        r++;
        //第二行
        row = sheet.createRow(r);
        cell = row.createCell(0);
        cell.setCellValue("班别:");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1);
        cell.setCellValue("白班/夜班");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 1, 3));
        cell = row.createCell(4);
        cell.setCellValue("日期:");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5);
        cell.setCellValue(date.replace("00:00:00.0", ""));
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(7);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 5, 7));

        cell = row.createCell(8);
        cell.setCellValue("复核人:");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(9);
        cell.setCellValue(operator);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(10);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(11);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 9, 11));
        r++;
        //第三行
        row = sheet.createRow(r);
        for (int x = 0; x < columnName.length; x++) {
            cell = row.createCell(x);
            cell.setCellValue(columnName[x]);
            cell.setCellStyle(cellStyle);
            if (x == columnName.length - 1 || x == 3 || x == 6 || x == 8) {
                sheet.setColumnWidth(x, 50 * 100);
            } else {
                sheet.setColumnWidth(x, 30 * 100);
            }
        }
        r++;
        String userId = "";
        for (CutDailyPlanDetail data : cpdList) {
            //获取裁剪计划
            CutPlan cutPlan = cutDailyPlanDetailService.findById(CutPlan.class, data.getCutPlanId());
            List<Map<String, Object>> cdpuList = cutDailyPlanDetailService.findPlanUsers(data.getId());
            for (Map<String, Object> planUser : cdpuList) {
                row = sheet.createRow(r);
                for (int j = 0; j < columnName.length; j++) {
                    cell = row.createCell(j);
                    switch (j) {
                        case 0:
                            if (!planUser.get("userId".toUpperCase()).toString().equals(userId)) {
                                cell.setCellValue(planUser.get("userName".toUpperCase()).toString());
                                userId = planUser.get("userId".toUpperCase()).toString();
                                cell.setCellStyle(cellStyle);
                            } else {
                                cell.setCellStyle(cellStyle);
                                sheet.addMergedRegion(new CellRangeAddress((r - 1), r, 0, 0));
                            }
                            break;
                        case 1:
                            cell.setCellValue(planUser.get("count".toUpperCase()).toString());
                            cell.setCellStyle(cellStyle);
                            break;
                        case 2:
                            cell.setCellValue(planUser.get("partName".toUpperCase()).toString());
                            cell.setCellStyle(cellStyle1);
                            break;
                        case 3:
                            cell.setCellValue(cutPlan.getPlanCode());
                            cell.setCellStyle(cellStyle1);
                            break;
                        case 4:
                            cell.setCellValue(cutPlan.getSalesOrderCode());
                            cell.setCellStyle(cellStyle1);
                            break;
                        case 5:
                            cell.setCellValue(cutPlan.getBatchCode());
                            cell.setCellStyle(cellStyle1);
                            break;
                        case 6:
                            cell.setCellValue(cutPlan.getProductModel());
                            cell.setCellStyle(cellStyle1);
                            break;
                        case 7:
                            if (cutPlan.getProductWeight() != null) {
                                cell.setCellValue(cutPlan.getProductWeight() != null ? cutPlan.getProductWeight().toString() + "kg" : cutPlan.getProductLength().toString() + "mm");
                            } else {
                                if (cutPlan.getProductLength() != null) {
                                    cell.setCellValue(cutPlan.getProductLength().toString() + "mm");
                                } else {
                                    cell.setCellValue(0);
                                }
                            }
                            cell.setCellStyle(cellStyle1);
                            break;
                        case 8:
                            cell.setCellValue(cutPlan.getConsumerName());
                            cell.setCellStyle(cellStyle1);
                            break;
                        case 9:
                        case 10:
                            cell.setCellValue("");
                            cell.setCellStyle(cellStyle1);
                            break;
                        case 11:
                            cell.setCellValue(cutPlan.getComment());
                            cell.setCellStyle(cellStyle1);
                            break;
                    }
                }
                r++;
            }
        }
        HttpUtils.download(response, wb, templateName);
    }
}
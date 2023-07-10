/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.audit.controller;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.entity.AuditConstant.RS;
import com.bluebirdme.mes.audit.entity.AuditInstance;
import com.bluebirdme.mes.audit.entity.AuditProcessSetting;
import com.bluebirdme.mes.audit.entity.AuditUsers;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomVersionService;
import com.bluebirdme.mes.baseInfo.service.ITcBomVersionService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IUserService;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailTemp;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程实例Controller
 *
 * @author 高飞
 * @Date 2016-10-25 13:52:49
 */
@Controller
@RequestMapping("/audit")
@Journal(name = "流程实例")
public class AuditController extends BaseController {
    /**
     * 流程实例页面
     */
    final String auditPage = "audit/auditPage";
    final String taskPage = "audit/auditTask";
    final String myTaskPage = "audit/myTaskAudit";
    final String finishedTaskPage = "audit/auditFinishedTask";
    final String auditStatePage = "audit/auditState";
    @Resource
    IAuditInstanceService auditService;
    @Resource
    IUserService userService;
    @Resource
    ISalesOrderService salesOrderService;
    @Resource
    ITcBomVersionService tcBomVersionService;
    @Resource
    IFtcBcBomVersionService ftcBcBomVersionService;

    @Journal(name = "待审任务")
    @RequestMapping(value = "task", method = RequestMethod.GET)
    public String taskPage(Filter filter, Page page) {
        return taskPage;
    }

    @Journal(name = "我提交的审核")
    @RequestMapping(value = "myTask", method = RequestMethod.GET)
    public String myTaskPage(Filter filter, Page page) {
        return myTaskPage;
    }

    /**
     * 我的任务
     *
     * @param filter
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "task", method = RequestMethod.POST)
    public String task(Filter filter, Page page) {
        filter.getFilter().put("uid", session.getAttribute(Constant.CURRENT_USER_ID) + "");
        return GsonTools.toJson(auditService.auditTask(filter, page));
    }

    /**
     * 我提交的审核
     *
     * @param filter
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "myTask", method = RequestMethod.POST)
    public String myTask(Filter filter, Page page) {
        filter.set("uid", session.getAttribute(Constant.CURRENT_USER_ID) + "");
        return GsonTools.toJson(auditService.myAuditTask(filter, page));
    }

    @Journal(name = "已审任务")
    @RequestMapping(value = "task/finished", method = RequestMethod.GET)
    public String finishedTaskPage(Filter filter, Page page) {
        return finishedTaskPage;
    }

    @ResponseBody
    @Journal(name = "已审任务")
    @RequestMapping(value = "task/finished", method = RequestMethod.POST)
    public String finishedTask(Filter filter, Page page) {
        filter.getFilter().put("uid", session.getAttribute(Constant.CURRENT_USER_ID) + "");
        return GsonTools.toJson(auditService.finishedAuditTask(filter, page));
    }

    @Journal(name = "审核页面")
    @RequestMapping(value = "{aid}", method = RequestMethod.GET)
    public ModelAndView audit(@PathVariable("aid") Long aid) {
        AuditInstance ai = auditService.findById(AuditInstance.class, aid);
        User createUser = auditService.findById(User.class, ai.getCreateUserId());
        Department createUserDept = auditService.findById(Department.class, createUser.getDid());
        User levelOneUser = null;
        Department levelOneUserDept = null;
        if (ai.getFirstRealAuditUserId() != null) {
            levelOneUser = auditService.findById(User.class, ai.getFirstRealAuditUserId());
            if (levelOneUser != null) {
                levelOneUserDept = auditService.findById(Department.class, levelOneUser.getDid());
            }
        }

        User levelTwoUser = null;
        Department levelTwoUserDept = null;
        if (ai.getSecondRealAuditUserId() != null) {
            levelTwoUser = auditService.findById(User.class, ai.getSecondRealAuditUserId());
            if (levelTwoUser != null) {
                levelTwoUserDept = auditService.findById(Department.class, levelTwoUser.getDid());
            }
        }

        Map<String, Object> param = new HashMap();
        param.put("auditCode", ai.getAuditCode());
        AuditProcessSetting aps = auditService.findUniqueByMap(AuditProcessSetting.class, param);

        param.put("auditCode", ai.getAuditCode());
        param.put("auditLevel", 1);
        param.put("userId", session.getAttribute(Constant.CURRENT_USER_ID));
        boolean isLevelOne = auditService.isExist(AuditUsers.class, param, true);

        // 如果一级审核人不为空，肯定已经一级审核过了
        if (ai.getFirstRealAuditUserId() != null) {
            isLevelOne = false;
        }

        param.put("auditLevel", 2);
        boolean isLevelTwo = auditService.isExist(AuditUsers.class, param, true);

        // 如果是一级审核，那么就不是二级审核
        if (isLevelOne) {
            isLevelTwo = false;
        }

        // 判断流程是否结束
        if (ai.getIsCompleted().equals(AuditConstant.STATE.FINISHED)) {
            isLevelTwo = false;
            isLevelOne = false;
            model.addAttribute("isCompleted", true);
        } else {
            model.addAttribute("isCompleted", false);
        }

        model.addAttribute("audit", ai);
        model.addAttribute("setting", aps);
        model.addAttribute("isLevelOne", isLevelOne);
        model.addAttribute("isLevelTwo", isLevelTwo);
        model.addAttribute("createUser", createUser.getUserName() + "（" + createUserDept.getName() + "）");
        if (levelOneUser != null) {
            model.addAttribute("levelOneUser", levelOneUser.getUserName() + "（" + levelOneUserDept.getName() + "）");
        }

        if (levelTwoUser != null) {
            model.addAttribute("levelTwoUser", levelTwoUser.getUserName() + "（" + levelTwoUserDept.getName() + "）");
        }
        return new ModelAndView(auditPage, model);
    }

    @ResponseBody
    @Journal(name = "审核内容")
    @RequestMapping(value = "{aid}/decide", method = RequestMethod.POST)
    public String decide(@PathVariable("aid") Long aid, Integer level, String msg, Integer result) throws Exception {
        AuditInstance ai = auditService.findById(AuditInstance.class, aid);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("auditCode", ai.getAuditCode());
        AuditProcessSetting aps = auditService.findUniqueByMap(AuditProcessSetting.class, param);

        if (level == 1) {
            if (ai.getFirstRealAuditUserId() != null) {
                User levelOneUser = auditService.findById(User.class, ai.getFirstRealAuditUserId());
                return ajaxError("当前节点[" + levelOneUser.getUserName() + "]已审核 " + (ai.getFirstAuditResult().equals(RS.PASS) ? "通过" : "不通过"));
            }
        }

        if (ai.getSecondRealAuditUserId() != null) {
            User levelOneUser = auditService.findById(User.class, ai.getSecondRealAuditUserId());
            return ajaxError("当前节点[" + levelOneUser.getUserName() + "]已审核 " + (ai.getFirstAuditResult().equals(RS.PASS) ? "通过" : "不通过"));
        }

        //判断套材BOM是否导入成功
        if (result.intValue() == AuditConstant.RS.PASS && ai.getAuditCode().equals("TC")) {
            //查找套材判断依据
            TcBomVersion tcBomVersion1 = auditService.findById(TcBomVersion.class, ai.getFormId());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tcProcBomVersoinId", tcBomVersion1.getId());
            List<TcBomVersionParts> tcBomVersionPartsList = auditService.findListByMap(TcBomVersionParts.class, map);
            TcBom tcBom = auditService.findById(TcBom.class, tcBomVersion1.getTcProcBomId());
            //导入Excel自动转换PDF失败
            if (tcBomVersion1.getAttachmentId() != null) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("processBomCode", tcBom.getTcProcBomCode());
                List<BomFile> bomFileList = auditService.findListByMap(BomFile.class, map2);
                if (tcBomVersionPartsList.size() == 0 || bomFileList.size() == 0) {
                    return ajaxError("套材BOM未成功导入工艺文件，请执行拒绝操作");
                }
            } else {//手动创建信息没有填全
                if (tcBomVersionPartsList.size() == 0) {
                    return ajaxError("手动导入套材BOM信息未填全，请执行拒绝操作");
                }
            }

        }

        //判断非套材BOM是否导入成功
        if (result.intValue() == AuditConstant.RS.PASS && ai.getAuditCode().equals("FTC")) {
            FtcBomVersion ftcBomVersion = auditService.findById(FtcBomVersion.class, ai.getFormId());
            FtcBom ftcBom = auditService.findById(FtcBom.class, ftcBomVersion.getFtcProcBomId());
            Map<String, Object> map = new HashMap<>();
            map.put("ftcBomVersionId", ftcBomVersion.getId());
            List<FtcBomDetail> ftcBomDetailList = auditService.findListByMap(FtcBomDetail.class, map);
            if (ftcBomVersion.getAttachmentId() != null) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("processBomCode", ftcBom.getFtcProcBomCode());
                List<BomFile> bomFileList = auditService.findListByMap(BomFile.class, map2);
                if (bomFileList.size() == 0) {
                    return ajaxError("非套材BOM未成功导入工艺文件，请执行拒绝操作");
                }
                if (ftcBomDetailList.size() == 0) {
                    return ajaxError("非套材BOM明细列表信息未填全，请执行拒绝操作");
                }
            } else {//手动创建信息没有填全
                if (ftcBomDetailList.size() == 0) {
                    return ajaxError("手动导入非套材BOM信息未填全，请执行拒绝操作");
                }
            }
        }

        //判断非套材包材BOM是否导入成
        if (result.intValue() == AuditConstant.RS.PASS && ai.getAuditCode().equals("FTCBC")) {
            FtcBcBomVersion ftcBcBomVersion = auditService.findById(FtcBcBomVersion.class, ai.getFormId());
            Map<String, Object> map = new HashMap<>();
            map.put("packVersionId", ftcBcBomVersion.getId());
            List<FtcBcBomVersionDetail> ftcBcBomVersionDetails = auditService.findListByMap(FtcBcBomVersionDetail.class, map);
            FtcBcBom ftcBcBom = auditService.findById(FtcBcBom.class, ftcBcBomVersion.bid);
            if (ftcBcBomVersion.getAttachmentId() != null) {
                FtcBcBom ftcBcBom1 = auditService.findById(FtcBcBom.class, ftcBcBom.getPid());
                Map<String, Object> map2 = new HashMap<>();
                map2.put("bcBomCode", ftcBcBom1.getCode());
                List<BomFile> bomFileList = auditService.findListByMap(BomFile.class, map2);
                if (ftcBcBomVersionDetails.size() == 0 || bomFileList.size() == 0) {
                    return ajaxError("非套材包材BOM未成功导入工艺文件，请执行拒绝操作");
                }
            } else {
                if (ftcBcBomVersionDetails.size() == 0) {
                    return ajaxError("手动导入非套材包材BOM信息未填全，请执行拒绝操作");
                }
            }
        }

        //判断包材BOM导入是否成功
        if (result.intValue() == AuditConstant.RS.PASS && ai.getAuditCode().equals("BC")) {
            BCBomVersion bcBomVersion = auditService.findById(BCBomVersion.class, ai.getFormId());
            BcBom bcBom = auditService.findById(BcBom.class, bcBomVersion.getPackBomId());
            Map<String, Object> map = new HashMap<>();
            map.put("packVersionId", bcBomVersion.getId());
            List<BcBomVersionDetail> bcBomVersionDetails = auditService.findListByMap(BcBomVersionDetail.class, map);
            if (bcBomVersion.getAttachmentId() != null) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("bcBomCode", bcBom.getPackBomCode());
                List<BomFile> bomFileList = auditService.findListByMap(BomFile.class, map2);
                if (bomFileList.size() == 0) {
                    return ajaxError("包材BOM未成功导入工艺文件，请执行拒绝操作");
                }
                if (bcBomVersionDetails.size() == 0) {
                    return ajaxError("包材BOM明细列表信息未填全，请执行拒绝操作");
                }
            } else {
                if (bcBomVersionDetails.size() == 0) {
                    return ajaxError("手动导入包材BOM明细列表信息未填全，请执行拒绝操作");
                }
            }

        }


        if (result.intValue() == AuditConstant.RS.PASS && (ai.getAuditCode().startsWith("XS1") || ai.getAuditCode().startsWith("XS2") || ai.getAuditCode().startsWith("XS3") || ai.getAuditCode().startsWith("PBOrder"))) {
            List<String> list = new ArrayList<>();
            String str = null;
            HashMap<String, Object> map = new HashMap<>();
            map.put("salesOrderId", ai.getFormId());
            List<Map<String, Object>> salesOrderDetailMap = salesOrderService.findListByMap1(ai.getFormId());
            for (Map<String, Object> s : salesOrderDetailMap) {
                //套材
                if (s.get("PRODUCTISTC").toString().equals("1")) {
                    TcBomVersion tcBomVersion = tcBomVersionService.findById(TcBomVersion.class, Long.parseLong(s.get("PROCBOMID").toString()));
                    if (null == tcBomVersion) {
                        return ajaxError("订单号为:" + s.get("SALESORDERSUBCODE") + "工艺代码为：" + s.get("PRODUCTPROCESSCODE") + "BOM已被删除，审核不通过，请执行拒绝操作");
                    } else if (!s.get("PRODUCTPROCESSBOMVERSION").toString().equals(tcBomVersion.getTcProcBomVersionCode())) {
                        str = "订单号为:" + s.get("SALESORDERSUBCODE") + "工艺代码为：" + s.get("PRODUCTPROCESSCODE");
                        list.add(str);
                    }
                    //非套材
                } else if (s.get("PRODUCTISTC").toString().equals("2")) {
                    FtcBomVersion ftcBomVersion = ftcBcBomVersionService.findById(FtcBomVersion.class, Long.parseLong(s.get("PROCBOMID").toString()));
                    if (null == ftcBomVersion) {
                        return ajaxError("订单号为:" + s.get("SALESORDERSUBCODE") + "工艺代码为：" + s.get("PRODUCTPROCESSCODE") + "的BOM已被删除，审核不通过，请执行拒绝操作");
                    } else if (!s.get("PRODUCTPROCESSBOMVERSION").toString().equals(ftcBomVersion.getFtcProcBomVersionCode())) {
                        str = "订单号为:" + s.get("SALESORDERSUBCODE") + "工艺代码为：" + s.get("PRODUCTPROCESSCODE");
                        list.add(str);
                    }
                    //胚布
                } else if (s.get("PRODUCTISTC").toString().equals("-1")) {
                    map.clear();
                    map.put("productBatchCode", s.get("PRODUCTBATCHCODE"));
                    map.put("closed", null);
                    map.put("productId", Long.parseLong(s.get("PRODUCTID").toString()));
                    List<SalesOrderDetailTemp> sodList = tcBomVersionService.findListByMap(SalesOrderDetailTemp.class, map);
                    if (sodList.size() == 0) {
                        FtcBomVersion ftcBomVersion = ftcBcBomVersionService.findById(FtcBomVersion.class, Long.parseLong(s.get("PROCBOMID").toString()));
                        if (null == ftcBomVersion) {
                            return ajaxError("订单号为:" + s.get("SALESORDERSUBCODE") + "工艺代码为：" + s.get("PRODUCTPROCESSCODE") + "的BOM已被删除，审核不通过，请执行拒绝操作");
                        } else if (!s.get("PRODUCTPROCESSBOMVERSION").toString().equals(ftcBomVersion.getFtcProcBomVersionCode())) {
                            str = "订单号为:" + s.get("SALESORDERSUBCODE") + "工艺代码为：" + s.get("PRODUCTPROCESSCODE");
                            list.add(str);
                        }
                    }
                }
            }

            if (list.size() > 0) {
                String.join(",", list);
                return ajaxError(list + "订单的工艺版本与BOM工艺版本不匹配，审核不通过，请执行拒绝操作");
            }
        }
        auditService.audit(ai, aps, (Long) session.getAttribute(Constant.CURRENT_USER_ID), level, msg, result);
        return GsonTools.toJson(ai);
    }

    @Journal(name = "查询状态")
    @RequestMapping("{code}/{formId}/state")
    public ModelAndView auditState(@PathVariable("code") String code, @PathVariable("formId") Long formId) throws Exception {
        Map<String, Object> map = new HashMap();
        map.put("auditCode", code);
        map.put("formId", formId);
        //所有的审核实例
        List<AuditInstance> aiList = auditService.findListByMap(AuditInstance.class, map);
        if (code.startsWith("XS")) {
            map.put("auditCode", code + "BG");
            List<AuditInstance> aiList2 = auditService.findListByMap(AuditInstance.class, map);
            aiList.addAll(aiList2);
        }

        if (code.startsWith("PBOrder")) {
            Filter filter = new Filter();
            filter.set("auditCode", "like:" + code);
            filter.set("formId", formId.toString());
            Page page = new Page();
            page.setAll(1);
            List<Map<String, Object>> rows = (List<Map<String, Object>>) auditService.findPageInfo(filter, page).get("rows");
            for (Map<String, Object> row : rows) {
                AuditInstance instance = auditService.findById(AuditInstance.class, Long.parseLong(row.get("ID").toString()));
                aiList.add(instance);
            }
        }
        //查询流程设置
        //带返回集合
        List<Map<String, Object>> retList = new ArrayList();
        Map<String, Object> ret;
        User u;

        for (AuditInstance ai : aiList) {
            map.clear();
            map.put("auditCode", ai.getAuditCode());
            AuditProcessSetting aps = auditService.findUniqueByMap(AuditProcessSetting.class, map);
            ret = new HashMap();
            u = userService.findById(User.class, ai.getCreateUserId());
            //提审时间
            ret.put("submitTime", ai.getCreateTime());
            //流程等级
            ret.put("level", aps.getAuditLevel());
            ret.put("title", ai.getAuditTitle());
            //提审人员
            ret.put("user", u == null ? "用户已删除" : u.getUserName());
            //流程结束,肯定有一级审核，在判断是否是二级审核，再看是否有二级审核人
            if (ai.getIsCompleted() != null && ai.getIsCompleted() == 1) {
                u = userService.findById(User.class, ai.getFirstRealAuditUserId());
                //一级审核人员
                ret.put("levelOneUser", u == null ? "用户已删除" : u.getUserName());
                //一级审核消息
                ret.put("levelOneMsg", ai.getFirstAuditMsg());
                //一级审核结果
                ret.put("levelOneResult", formatCheckResult(ai.getFirstAuditResult()));
                //一级审核时间
                ret.put("levelOneCheckTime", ai.getFirstAuditTime());
                if (aps.getAuditLevel() == 2) {
                    if (ai.getSecondRealAuditUserId() != null) {
                        u = userService.findById(User.class, ai.getSecondRealAuditUserId());
                        //二级审核人员
                        ret.put("levelTwoUser", u == null ? "用户已删除" : u.getUserName());
                        //二级审核消息
                        ret.put("levelTwoMsg", ai.getSecondAuditMsg());
                        //二级审核结果
                        ret.put("levelTwoResult", formatCheckResult(ai.getSecondAuditResult()));
                        //二级审核时间
                        ret.put("levelTwoCheckTime", ai.getSecondAuditTime());
                    } else {
                        //二级审核人员
                        ret.put("levelTwoUser", "无");
                        //二级审核消息
                        ret.put("levelTwoMsg", "无");
                        //二级审核结果
                        ret.put("levelTwoResult", "无");
                        //二级审核时间
                        ret.put("levelTwoCheckTime", null);
                    }
                }
            } else {
                //流程未结束
                if (ai.getFirstRealAuditUserId() != null) {
                    u = userService.findById(User.class, ai.getFirstRealAuditUserId());
                    //一级审核人员
                    ret.put("levelOneUser", u == null ? "用户已删除" : u.getUserName());
                    //一级审核消息
                    ret.put("levelOneMsg", ai.getFirstAuditMsg());
                    //一级审核结果
                    ret.put("levelOneResult", formatCheckResult(ai.getFirstAuditResult()));
                    //一级审核时间
                    ret.put("levelOneCheckTime", ai.getFirstAuditTime());
                    if (aps.getAuditLevel() == 2) {
                        if (ai.getSecondRealAuditUserId() != null) {
                            u = userService.findById(User.class, ai.getSecondRealAuditUserId());
                            //二级审核人员
                            ret.put("levelTwoUser", u == null ? "用户已删除" : u.getUserName());
                            //二级审核消息
                            ret.put("levelTwoMsg", ai.getSecondAuditMsg());
                            //二级审核结果
                            ret.put("levelTwoResult", formatCheckResult(ai.getFirstAuditResult()));
                            //二级审核时间
                            ret.put("levelTwoCheckTime", ai.getSecondAuditTime());
                        } else {
                            //二级审核人员
                            ret.put("levelTwoUser", "等待  <font color=red>" + aps.getAuditSecondLevelUsers() + "</font> 审核");
                            //二级审核消息
                            ret.put("levelTwoMsg", "等待审核");
                            //二级审核结果
                            ret.put("levelTwoResult", "等待审核");
                            //二级审核时间
                            ret.put("levelTwoCheckTime", null);
                        }
                    }
                } else {
                    //一级审核人员
                    ret.put("levelOneUser", "等待 <font color=red>" + aps.getAuditFirstLevelUsers() + "</font> 审核");
                    //一级审核消息
                    ret.put("levelOneMsg", "等待审核");
                    //一级审核结果
                    ret.put("levelOneResult", "等待审核");
                    //一级审核时间
                    ret.put("levelOneCheckTime", null);
                }
            }
            retList.add(ret);
            model.addAttribute("audit", retList).addAttribute("ai", ListUtils.isEmpty(aiList) ? null : aiList.get(0)).addAttribute("level", aps.getAuditLevel());
        }
        return new ModelAndView(auditStatePage, model);
    }

    public String formatCheckResult(Integer state) {
        if (state.equals(RS.AUDITING)) {
            return "审核中";
        } else if (state.equals(RS.PASS)) {
            return "通过";
        } else if (state.equals(RS.REJECT)) {
            return "不通过";
        }
        return "未知";
    }
}
/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.controller;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.entity.FtcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBomDetail;
import com.bluebirdme.mes.baseInfo.entity.FtcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.Material;
import com.bluebirdme.mes.baseInfo.service.IFtcBomService;
import com.bluebirdme.mes.baseInfo.service.IMaterialService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.constant.RuntimeVariable;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.service.IConsumerService;

/**
 * 非套材工艺BOMController
 *
 * @author 宋黎明
 * @Date 2016-10-8 13:36:51
 */
@Controller
@RequestMapping("/bom/ftc")
@Journal(name = "非套材工艺BOM")
public class FtcBomController extends BaseController {
    // 非套材工艺BOM页面
    final String index = "baseInfo/ftcBom/ftcBom";
    final String addOrEdit = "baseInfo/ftcBom/ftcBomAddOrEdit";
    // 非套材BOM版本
    final String addOrEditVersion = "baseInfo/ftcBom/ftcBomVersionAddOrEdit";
    final String audit = "baseInfo/ftcBom/ftcBomVersionAudit";

    @Resource
    IFtcBomService ftcBomService;
    @Resource
    IConsumerService consumerService;
    @Resource
    IAuditInstanceService auditInstanceService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @ResponseBody
    @Journal(name = "获取非套材bom列表信息")
    @RequestMapping("listBom")
    public String getftcBom(String nodeType, String id, String data) {
        if (data == null) {
            data = "";
        }
        JSONArray jarray = new JSONArray();
        String result = "";
        if (nodeType == (null)) {
            JSONObject json = new JSONObject();
            json.put("id", "root");
            json.put("text", "非套材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("nodeType", "root"));
            jarray.put(json);
            result = jarray.toString();
        } else if (nodeType.equals("root") && data != "") {
            List<Map<String, Object>> list = ftcBomService.getFtcBomJson(data);
            if (list.size() > 0) {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("nodeType", "root"));
                json.put("children", ftcBomService.getFtcBomJson(data));
                jarray.put(json);
                result = jarray.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材bom");
                json.put("state", "closed");
                jarray.put(json);
                result = jarray.toString();
            }
        } else if (nodeType.equals("bom")) {
            result = GsonTools.toJson(ftcBomService.getFtcBomByVersionJson(id));
        } else {
            result = GsonTools.toJson(ftcBomService.getFtcBomJson(data));
        }
        return result;
    }

    @ResponseBody
    @Journal(name = "获取非套材bom列表信息")
    @RequestMapping("listBomTest")
    public String listBomTest(String nodeType, String id, String data) {
        if (data == null) {
            data = "";
        }
        JSONArray jarray = new JSONArray();
        String result = "";
        if (nodeType == (null)) {
            JSONObject json = new JSONObject();
            json.put("id", "root");
            json.put("text", "非套材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("nodeType", "root"));
            jarray.put(json);
            result = jarray.toString();
        } else if (nodeType.equals("root") && data != "") {
            List<Map<String, Object>> list = ftcBomService.getFtcBomJsonTest(data);
            if (list.size() > 0) {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("nodeType", "root"));
                json.put("children", ftcBomService.getFtcBomJsonTest(data));
                jarray.put(json);
                result = jarray.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材bom");
                json.put("state", "closed");
                jarray.put(json);
                result = jarray.toString();
            }
        } else if (nodeType.equals("bom")) {
            result = GsonTools.toJson(ftcBomService.getFtcBomByVersionJson(id));
        } else {
            result = GsonTools.toJson(ftcBomService.getFtcBomJsonTest(data));
        }
        return result;
    }

    @ResponseBody
    @Journal(name = "获取非套材bom列表信息")
    @RequestMapping("listBomTest1")
    public String listBomTest1(String nodeType, String id, String data) {
        if (data == null) {
            data = "";
        }
        JSONArray array = new JSONArray();
        String result = "";
        if (nodeType == (null)) {
            JSONObject json = new JSONObject();
            json.put("id", "root");
            json.put("text", "非套材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("nodeType", "root"));
            array.put(json);
            result = array.toString();
        } else if (nodeType.equals("root") && data != "") {
            List<Map<String, Object>> list = ftcBomService.getFtcBomJsonTest1(data);
            if (list.size() > 0) {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("nodeType", "root"));
                json.put("children", ftcBomService.getFtcBomJsonTest1(data));
                array.put(json);
                result = array.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材bom");
                json.put("state", "closed");
                array.put(json);
                result = array.toString();
            }
        } else if (nodeType.equals("bom")) {
            result = GsonTools.toJson(ftcBomService.getFtcBomByVersionJson(id));
        } else {
            result = GsonTools.toJson(ftcBomService.getFtcBomJsonTest1(data));
        }
        return result;
    }


    @NoAuth
    @ResponseBody
    @Journal(name = "获取非套材工艺BOM列表信息")
    @RequestMapping("list")
    public String getFTc_Bom(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageInfo = ftcBomService.findPageInfo(filter, page);
        List<Map<String, Object>> list = new ArrayList();
        Map<String, Object> map = new HashMap();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
        DecimalFormat df = new DecimalFormat("#.00");
        if (rows.size() != 0) {
            Double d = 0.0;
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).get("FTCBOMDETAILWEIGHTPERSQUAREMETRE") != null && rows.get(i).get("FTCBOMDETAILWEIGHTPERSQUAREMETRE") != "") {
                    d += (Double) rows.get(i).get("FTCBOMDETAILWEIGHTPERSQUAREMETRE");
                }
            }
            Object o = df.format(d);
            map.put("FTCBOMDETAILMODEL", "单位面积质量合计(g/m²)");
            map.put("FTCBOMDETAILWEIGHTPERSQUAREMETRE", o);
        } else {
            map.put("FTCBOMDETAILMODEL", "单位面积质量合计(g/m²)");
            map.put("FTCBOMDETAILWEIGHTPERSQUAREMETRE", 0);
        }
        list.add(map);
        findPageInfo.put("footer", list);
        return GsonTools.toJson(findPageInfo);
    }

    @Journal(name = "添加非套材工艺BOM页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(FtcBom fTc_Bom, FtcBomVersion ftcBomVersion) {
        String filePath = UUID.randomUUID().toString();
        return new ModelAndView(addOrEdit, model.addAttribute("fTc_Bom", fTc_Bom).addAttribute("ftcBomVersion", ftcBomVersion).addAttribute("filePath", filePath));
    }

    @ResponseBody
    @Journal(name = "保存非套材工艺BOM", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(FtcBom ftcBom, FtcBomVersion ftcBomVersion, Long fileId) throws Exception {
        HashMap<String, Object> map = new HashMap();
        map.put("ftcProcBomCode", ftcBom.getFtcProcBomCode());
        if (!ftcBomService.isExist(FtcBom.class, map)) {
            ExcelImportMessage eim = ftcBomService.doAddFtcBom(ftcBom, ftcBomVersion, fileId);
            if (eim != null && eim.hasError()) {
                Map<String, String> excelErrorMsg = new HashMap<>();
                excelErrorMsg.put("excelErrorMsg", eim.getMessage());
                return GsonTools.toJson(excelErrorMsg);
            }
        } else {
            return ajaxError("BOM代码重复！");
        }
        return GsonTools.toJson(ftcBom);
    }

    @Journal(name = "编辑非套材工艺BOM页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(FtcBom ftcBom) {
        ftcBom = ftcBomService.findById(FtcBom.class, ftcBom.getId());
        Consumer consumer = consumerService.findById(Consumer.class, ftcBom.getFtcProcBomConsumerId());
        return new ModelAndView(addOrEdit, model.addAttribute("fTc_Bom", ftcBom).addAttribute("consumer", consumer));
    }

    @ResponseBody
    @Journal(name = "保存编辑非套材工艺BOM", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(FtcBom ftcBom) throws Exception {
        HashMap<String, Object> map = new HashMap();
        map.put("ftcProcBomCode", ftcBom.getFtcProcBomCode());
        if (ftcBomService.isExist(FtcBom.class, map, ftcBom.getId(), true))
            return ajaxError("工艺BOM代码重复!");
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("ftcProcBomId", ftcBom.getId());
        List<FtcBomVersion> li = ftcBomService.findListByMap(FtcBomVersion.class, map1);
        for (FtcBomVersion fbv : li) {
            if (fbv.getAuditState() > 0) {
                return ajaxError("含有审核中或已通过的版本,不能修改");
            }
        }
        ftcBomService.update2(ftcBom);
        return GsonTools.toJson(ftcBom);
    }

    @ResponseBody
    @Journal(name = "删除非套材工艺BOM", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(String ids) throws Exception {
        String[] idsArray = ids.split(",");
        for (String idString : idsArray) {
            Long id = Long.parseLong(idString);
            HashMap<String, Object> map = new HashMap();
            map.put("ftcProcBomId", id);
            List<FtcBomVersion> fbv = ftcBomService.findListByMap(FtcBomVersion.class, map);
            if (fbv != null && fbv.size() > 0) {
                return ajaxError("请删除版本后删除工艺");
            }
        }
        ftcBomService.delete(ids);
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "添加非套材BOM版本页面")
    @RequestMapping(value = "addBomVersion", method = RequestMethod.GET)
    public ModelAndView _addBomVersion(FtcBomVersion ftcBomVersion) {
        return new ModelAndView(addOrEditVersion, model.addAttribute("ftcBomVersion", ftcBomVersion));
    }

    @ResponseBody
    @Journal(name = "保存非套材BOM版本", logType = LogType.DB)
    @RequestMapping(value = "addBomVersion", method = RequestMethod.POST)
    @Valid
    public String addBomVersion(FtcBomVersion ftcBomVersion) {
        List<FtcBomVersion> saveList = new ArrayList();
        HashMap<String, Object> map = new HashMap();
        map.put("ftcProcBomId", ftcBomVersion.getFtcProcBomId());
        List<FtcBomVersion> li = ftcBomService.findListByMap(FtcBomVersion.class, map);
        if (ftcBomVersion.getFtcProcBomVersionDefault() == null) {
            ftcBomVersion.setFtcProcBomVersionDefault(1);
        }
        if (ftcBomVersion.getFtcProcBomVersionEnabled() == null) {
            ftcBomVersion.setFtcProcBomVersionEnabled(1);
        }
        for (FtcBomVersion fv : li) {
            if (fv.getFtcProcBomVersionCode().equals(ftcBomVersion.getFtcProcBomVersionCode())) {
                return ajaxError("版本号重复！");
            }
            HashMap<String, Object> map1 = new HashMap();
            map1.put("ftcProcBomId", fv.getFtcProcBomId());

            if (fv.getFtcProcBomVersionDefault() == 1) {
                List<FtcBomVersion> versionList = ftcBomService.findListByMap(FtcBomVersion.class, map1);
                for (FtcBomVersion v : versionList) {
                    if (v.getFtcProcBomVersionDefault() == 1) {
                        v.setFtcProcBomVersionDefault(-1);
                        saveList.add(v);
                    }
                }
            }
        }
        ftcBomVersion.setAuditState(AuditConstant.RS.SUBMIT);
        saveList.add(ftcBomVersion);
        ftcBomService.save(saveList.toArray(new FtcBomVersion[]{}));
        return GsonTools.toJson(ftcBomVersion);
    }

    @Journal(name = "编辑非套材BOM版本页面")
    @RequestMapping(value = "editBomVersion", method = RequestMethod.GET)
    public ModelAndView _editBomVersion(FtcBomVersion ftcBomVersion) {
        ftcBomVersion = ftcBomService.findById(FtcBomVersion.class, ftcBomVersion.getId());
        String filePath = UUID.randomUUID().toString();
        return new ModelAndView(addOrEditVersion, model.addAttribute("ftcBomVersion", ftcBomVersion).addAttribute("filePath", filePath));
    }

    @ResponseBody
    @Journal(name = "保存编辑非套材BOM版本", logType = LogType.DB)
    @RequestMapping(value = "editBomVersion", method = RequestMethod.POST)
    @Valid
    public String editBomVersion(FtcBomVersion ftcBomVersion, Long fileId) throws Exception {
        HashMap<String, Object> map = new HashMap();
        map.put("ftcProcBomId", ftcBomVersion.getFtcProcBomId());
		/*map.put("ftcProcBomVersionCode", ftcBomVersion.getFtcProcBomVersionCode());
		map.put("ftcProcBomVersionDefault", ftcBomVersion.getFtcProcBomVersionDefault());
		map.put("ftcProcBomVersionEnabled", ftcBomVersion.getFtcProcBomVersionEnabled());
		if (ftcBomVersion.getAuditState() > 0) {
			return ajaxError("不能修改审核中或已通过的数据");
		}
		if(ftcBomService.isExist(FtcBomVersion.class, map, true)){
			return ajaxError("已经有相同名字的版本");
		}*/

        List<FtcBomVersion> li = ftcBomService.findListByMap(FtcBomVersion.class, map);
        for (FtcBomVersion fbv : li) {
            if (fbv.getId() == ftcBomVersion.getId()) {
                if (fbv.getAuditState() > 0) {
                    return ajaxError("不能修改审核中或已通过的数据");
                }
            }
            if (fbv.getFtcProcBomVersionCode().equals(ftcBomVersion.getFtcProcBomVersionCode()) && !fbv.getId().equals(ftcBomVersion.getId())) {
                return ajaxError("已经有相同名字的版本");
            }
        }
        //ftcBomService.update2(ftcBomVersion);
        ExcelImportMessage eim = ftcBomService.doUpdateFtcBomVersion(ftcBomVersion, fileId);
        if (eim != null && eim.hasError()) {
            Map<String, String> excelErrorMsg = new HashMap<>();
            excelErrorMsg.put("excelErrorMsg", eim.getMessage());
            return GsonTools.toJson(excelErrorMsg);
        }
        /*
         *
         * List<FtcBomVersion> saveList = new ArrayList<FtcBomVersion>();
         * HashMap<String,Object> map=new HashMap<String,Object>();
         * map.put("ftcProcBomId", ftcBomVersion.getFtcProcBomId());
         * List<FtcBomVersion>
         * li=ftcBomService.findListByMap(FtcBomVersion.class, map);
         * for(FtcBomVersion fv:li){
         * if(fv.getFtcProcBomVersionCode().equals(ftcBomVersion
         * .getFtcProcBomVersionCode
         * ())&&fv.getId().intValue()!=ftcBomVersion.getId().intValue()){ return
         * ajaxError("版本号重复！"); }
         * if(ftcBomVersion.getFtcProcBomVersionDefault()==
         * 1&&fv.getId().intValue()!=ftcBomVersion.getId().intValue()){
         * fv.setFtcProcBomVersionDefault(-1); saveList.add(fv); }
         *
         * } if (ftcBomVersion.getAuditState() > 0) { return
         * ajaxError("审核中和审核通过的计划不能编辑！"); } saveList.add(ftcBomVersion);
         * ftcBomService.update2(saveList.toArray(new FtcBomVersion[] {}));
         */
        return GsonTools.toJson(ftcBomVersion);
    }

    @ResponseBody
    @Journal(name = "提交审核非套材版本", logType = LogType.DB)
    @RequestMapping(value = "commitAudit", method = RequestMethod.POST)
    @Valid
    public String _commitAudit(String id, String name) throws Exception {
        FtcBomVersion ftcBomVersion = ftcBomService.findById(FtcBomVersion.class, Long.valueOf(id));
        FtcBom ftcBom = ftcBomService.findById(FtcBom.class, ftcBomVersion.getFtcProcBomId());
        if (ftcBom.getIsTestPro() == 1) {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.FTC, (Long) session.getAttribute(Constant.CURRENT_USER_ID), "bom/ftc/auditVersion/?id=" + id, Long.valueOf(id), FtcBomVersion.class);
        } else if (ftcBom.getIsTestPro() == -1) {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.FTC1, (Long) session.getAttribute(Constant.CURRENT_USER_ID), "bom/ftc/auditVersion/?id=" + id, Long.valueOf(id), FtcBomVersion.class);
        } else {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.FTC2, (Long) session.getAttribute(Constant.CURRENT_USER_ID), "bom/ftc/auditVersion/?id=" + id, Long.valueOf(id), FtcBomVersion.class);
        }
        return Constant.AJAX_SUCCESS;
    }

    @RequestMapping(value = "rebuildBcAudit", method = RequestMethod.GET)
    public void rebuildBcAudit() throws Exception {
        List<FtcBomVersion> version = ftcBomService.findAll(FtcBomVersion.class);
        for (FtcBomVersion fv : version) {
            if (fv.getAuditState() == null) {
                continue;
            }
            if (fv.getAuditState() < 2 && fv.getAuditState() > 0) {
                FtcBom bom = ftcBomService.findById(FtcBom.class, fv.getFtcProcBomId());
                _commitAudit(fv.getId() + "", "非套材工艺审核：工艺名称：" + bom.getFtcProcBomName() + "/" + bom.getFtcProcBomCode() + "版本号：" + fv.getFtcProcBomVersionCode());
            }
        }
    }

    @Journal(name = "查看审核非套材版本页面")
    @RequestMapping(value = "auditVersion", method = RequestMethod.GET)
    public ModelAndView audit(String id) {
        FtcBomVersion ftcBomVersion = ftcBomService.findById(FtcBomVersion.class, Long.valueOf(id));
        HashMap<String, Object> map = new HashMap();
        map.put("ftcBomVersionId", ftcBomVersion.getId());
        List<FtcBomDetail> li = ftcBomService.findListByMap(FtcBomDetail.class, map);
        return new ModelAndView(audit, model.addAttribute("ftcBomVersion", ftcBomVersion).addAttribute("details", GsonTools.toJson(li)));
    }

    @ResponseBody
    @Journal(name = "删除非套材BOM版本", logType = LogType.DB)
    @RequestMapping(value = "deleteBomVersion", method = RequestMethod.POST)
    public String deleteBomVersion(String ids) {
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            FtcBomVersion ftcBomVersion = auditInstanceService.findById(FtcBomVersion.class, Long.valueOf(id[a]));
            if (ftcBomVersion.getAuditState() > 0) {
                return ajaxError("审核中和审核通过的记录不能删除！");
            }

            HashMap<String, Object> map = new HashMap();
            map.put("procBomId", ftcBomVersion.getId());

            List<FinishedProduct> fp = auditInstanceService.findListByMap(FinishedProduct.class, map);
            if (fp != null && fp.size() > 0) {
                return ajaxError("该版本被产品使用，请修改相关产品工艺后删除版本");
            }
        }
        ftcBomService.deleteBomVersion(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "删除非套材BOM明细", logType = LogType.DB)
    @RequestMapping(value = "deleteDetail", method = RequestMethod.POST)
    public String editDetail(String ids) {
        ftcBomService.deleteDetail(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "保存非套材BOM明细", logType = LogType.DB)
    @RequestMapping(value = "saveDetail", method = RequestMethod.POST)
    public String saveDetail(@RequestBody FtcBomDetail detail) {
        if (detail.getId() == null) {
            ftcBomService.save(detail);
        } else {
            ftcBomService.update(detail);
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "复制版本节点")
    @RequestMapping("copyftcBomVersion")
    public String toComplite(String ids, String name) throws Exception {
        FtcBomVersion fbv = ftcBomService.findById(FtcBomVersion.class, Long.parseLong(ids));
        HashMap<String, Object> map = new HashMap();
        map.put("ftcProcBomVersionCode", name);
        map.put("ftcProcBomId", fbv.getFtcProcBomId());
        if (!ftcBomService.isExist(FtcBomVersion.class, map, true)) {
            ftcBomService.toCompliteCopy(ids);
            return ajaxSuccess();
        } else {
            return ajaxError("该节点已复制，请修改复制的节点名称后再进行复制操作");
        }
    }

    @ResponseBody
    @Journal(name = "更新版本节点")
    @RequestMapping("updateVersionByCode")
    public String updateVersionByCode(String ids, String name) throws Exception {
        FtcBomVersion fbv = ftcBomService.findById(FtcBomVersion.class, Long.parseLong(ids));
        HashMap<String, Object> map = new HashMap();
        map.put("ftcProcBomVersionCode", name);
        map.put("ftcProcBomId", fbv.getFtcProcBomId());
        if (!ftcBomService.isExist(FtcBomVersion.class, map, true)) {
            ftcBomService.toCompliteCopy(ids);
            return ajaxSuccess();
        } else {
            return ajaxError("该节点已复制，请修改复制的节点名称后再进行复制操作");
        }
    }

    @ResponseBody
    @Journal(name = "查看BOM下的版本")
    @RequestMapping(value = "findV", method = RequestMethod.POST)
    public String findV(String id) {
        HashMap<String, Object> map = new HashMap();
        map.put("ftcProcBomId", Long.valueOf(id));
        List<FtcBomVersion> list = ftcBomService.findListByMap(FtcBomVersion.class, map);
        if (list.size() == 0) {
            return GsonTools.toJson(0);
        } else {
            return GsonTools.toJson(1);
        }
    }

    @NoLogin
    @RequestMapping(value = "updateFtcInfos", method = RequestMethod.GET)
    public String updateFtcInfos() {
        ftcBomService.updateFtcInfos(-1);
        ftcBomService.updateFtcInfos(2);
        return ajaxSuccess();
    }

    @Journal(name = "导入Excel文件", logType = LogType.CONSOLE)
    @NoAuth
    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        if (RuntimeVariable.UPLOAD_PATH == null) {
            RuntimeVariable.UPLOAD_PATH = request.getSession().getServletContext().getRealPath("/") + File.separator + "upload";
        }

        File _file = new File(RuntimeVariable.UPLOAD_PATH);
        if (!_file.exists()) {
            _file.mkdirs();
        }

        // 文件名
        String fileName = file.getOriginalFilename();
        // 改为UUID命名
        String fileUUIDName = UUID.randomUUID().toString();
        // 截取格式
        String suffix = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        String filePath = RuntimeVariable.UPLOAD_PATH + File.separator;

        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
        String resultFileName = fileUUIDName + "." + suffix;
        filePath += resultFileName;

        File target = new File(filePath);

        byte[] bytes = file.getBytes();
        // 保存到服务器
        FileCopyUtils.copy(bytes, target);

        return GsonTools.toJson(resultFileName);
    }


    @NoAuth
    @ResponseBody
    @Journal(name = "获取非套材工艺BOM列表信息")
    @RequestMapping("mirrorList")
    public String getFTc_BomMirror(Filter filter, Page page) {
        Map<String, Object> findPageInfo = ftcBomService.findPageInfo1(filter, page);
        List<Map<String, Object>> list = new ArrayList();
        Map<String, Object> map = new HashMap();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
        DecimalFormat df = new DecimalFormat("#.00");
        if (rows.size() != 0) {
            Double d = 0.0;
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).get("FTCBOMDETAILWEIGHTPERSQUAREMETRE") != null && rows.get(i).get("FTCBOMDETAILWEIGHTPERSQUAREMETRE") != "") {
                    d += (Double) rows.get(i).get("FTCBOMDETAILWEIGHTPERSQUAREMETRE");
                }
            }
            Object o = df.format(d);
            map.put("FTCBOMDETAILMODEL", "单位面积质量合计(g/m²)");
            map.put("FTCBOMDETAILWEIGHTPERSQUAREMETRE", o);
        } else {
            map.put("FTCBOMDETAILMODEL", "单位面积质量合计(g/m²)");
            map.put("FTCBOMDETAILWEIGHTPERSQUAREMETRE", 0);
        }
        list.add(map);
        findPageInfo.put("footer", list);
        return GsonTools.toJson(findPageInfo);
    }
}
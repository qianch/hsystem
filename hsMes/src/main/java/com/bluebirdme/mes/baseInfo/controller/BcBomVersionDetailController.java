/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.controller;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.baseInfo.entity.BcBomVersionDetail;
import com.bluebirdme.mes.baseInfo.service.IBCBomVersionService;
import com.bluebirdme.mes.baseInfo.service.IBcBomService;
import com.bluebirdme.mes.baseInfo.service.IBcBomVersionDetailService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.utils.HttpUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.PathUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 包材bom明细Controller
 *
 * @author 徐波
 * @Date 2016-10-8 16:53:24
 */
@Controller
@RequestMapping("/bom/packaging")
@Journal(name = "包材bom明细")
public class BcBomVersionDetailController extends BaseController {
    // 包材bom明细页面
    final String index = "baseInfo/bcBom/bcBomVersionDetail";
    // 包材bom明细添加/修改页面
    final String addOrEdit = "baseInfo/bcBom/bcBomVersionDetailAddOrEdit";
    // 包材bom版本添加/修改页面
    final String addOrEditBomVersion = "baseInfo/bcBom/bCBomVersionAddOrEdit";
    // 包材bom添加/修改页面
    final String addOrEditBom = "baseInfo/bcBom/bcBomAddOrEdit";
    final String auditBomVersion = "baseInfo/bcBom/auditBomVersion";
    final String PAGE_BOMDETAILIMPORT = "baseInfo/bcBom/bcBomDetailUpload";//图纸BOM导入页面
    @Resource
    IAuditInstanceService auditInstanceService;
    @Resource
    IBCBomVersionService bCBomVersionService;
    @Resource
    IBcBomVersionDetailService bcBomVersionDetailService;
    @Resource
    IBcBomService bcBomService;

    @Journal(name = "审核包材bom版本信息页面")
    @RequestMapping(value = "auditBomVersion", method = RequestMethod.GET)
    public ModelAndView auditBomVersion(String id) {
        BCBomVersion bCBomVersion = bCBomVersionService.findById(BCBomVersion.class, Long.parseLong(id));
        BcBom bcBom = bCBomVersionService.findById(BcBom.class, bCBomVersion.getPackBomId());
        Consumer consumer = bCBomVersionService.findById(Consumer.class, bcBom.getPackBomConsumerId());
        HashMap<String, Object> map = new HashMap();
        map.put("packVersionId", bCBomVersion.getId());
        List<BcBomVersionDetail> li = bcBomVersionDetailService.findListByMap(BcBomVersionDetail.class, map);

        return new ModelAndView(auditBomVersion, model.addAttribute("bCBomVersion", bCBomVersion).addAttribute("details", GsonTools.toJson(li)).addAttribute("bcBom", bcBom).addAttribute("consumer", consumer));
    }

    @ResponseBody
    @Journal(name = "提交审核", logType = LogType.DB)
    @RequestMapping(value = "commit", method = RequestMethod.POST)
    public String commit(String id, String name) throws Exception {
        BCBomVersion bcBomVersion = bCBomVersionService.findById(BCBomVersion.class, Long.parseLong(id));
        BcBom bcBom = bCBomVersionService.findById(BcBom.class, bcBomVersion.getPackBomId());
        if (bcBom.getIsTestPro() == 0) {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.BC, Long.parseLong(String.valueOf(session.getAttribute(Constant.CURRENT_USER_ID))), "bom/packaging/auditBomVersion?id=" + id, bcBomVersion.getId(), BCBomVersion.class);
        } else if (bcBom.getIsTestPro() == 1) {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.BC1, Long.parseLong(String.valueOf(session.getAttribute(Constant.CURRENT_USER_ID))), "bom/packaging/auditBomVersion?id=" + id, bcBomVersion.getId(), BCBomVersion.class);
        } else {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.BC2, Long.parseLong(String.valueOf(session.getAttribute(Constant.CURRENT_USER_ID))), "bom/packaging/auditBomVersion?id=" + id, bcBomVersion.getId(), BCBomVersion.class);
        }
        return ajaxSuccess();
    }

    @RequestMapping(value = "rebuildBcAudit", method = RequestMethod.GET)
    public void rebuildBcAudit() throws Exception {
        List<BCBomVersion> versionList = bCBomVersionService.findAll(BCBomVersion.class);
        for (BCBomVersion bv : versionList) {
            if (bv.getAuditState() == null)
                continue;
            if (bv.getAuditState() > 0 && bv.getAuditState() < 2) {
                BcBom bom = bCBomVersionService.findById(BcBom.class, bv.getPackBomId());
                commit(bv.getId() + "", "包材BOM审核：BOM名称：" + bom.getPackBomName() + "/" + bom.getPackBomCode() + "BOM版本：" + bv.getPackVersion());
            }
        }
    }

    @ResponseBody
    @Journal(name = "复制版本节点")
    @RequestMapping("copyVersion")
    public String toComplite(String ids, String name) throws Exception {
        BCBomVersion bCBomVersion = bCBomVersionService.findById(BCBomVersion.class, Long.parseLong(ids));
        HashMap<String, Object> map = new HashMap();
        map.put("packBomId", bCBomVersion.getPackBomId());
        List<BCBomVersion> li = bCBomVersionService.findListByMap(BCBomVersion.class, map);
        for (BCBomVersion bv : li) {
            if (bv.getPackVersion().equals(name)) {
                return ajaxError("已经有相同名字的版本");
            }
        }
        bCBomVersionService.toCompliteCopy(ids);
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "复制版本节点")
    @RequestMapping("updateByCode")
    public String updateByCode(String ids, String name) throws Exception {
        BCBomVersion bCBomVersion = bCBomVersionService.findById(BCBomVersion.class, Long.parseLong(ids));
        HashMap<String, Object> map = new HashMap();
        BcBom bom = bCBomVersionService.findById(BcBom.class, bCBomVersion.getPackBomId());
        String packBomCode = bom.getPackBomCode();
        String bomVersion = bCBomVersion.getPackVersion();
        map.clear();
        map.put("packBomCode", packBomCode);
        List<BcBom> bomList = bCBomVersionService.findListByMap(BcBom.class, map);
        for (BcBom b : bomList) {
            if (b.getId().equals(bom.getId())) {
                continue;
            }
            map.clear();
            map.put("packBomId", b.getId());
            List<BCBomVersion> copyList = bCBomVersionService.findListByMap(BCBomVersion.class, map);
            for (BCBomVersion version : copyList) {
                bCBomVersionService.toCompliteCopy(version.getId() + "", bomVersion);
            }
        }
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "复制版本节点")
    @RequestMapping("copyBom")
    public String copyBom(String ids) throws Exception {
        BcBom bcBom = bCBomVersionService.findById(BcBom.class, Long.parseLong(ids));
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("packBomGenericName", bcBom.getPackBomGenericName() + "(复制)");
        if (bCBomVersionService.isExist(BcBom.class, map, true)) {
            return ajaxError("已经有相同名字的版本");
        }
        bCBomVersionService.toCompliteCopyBom(ids);
        return ajaxSuccess();
    }

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @ResponseBody
    @Journal(name = "获取包材bom明细列表信息")
    @RequestMapping("list")
    public String getBcBomVersionDetail(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(bcBomVersionDetailService.findPageInfo(filter, page));
    }

    @Journal(name = "添加包材bom明细页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(BcBomVersionDetail bcBomVersionDetail) {
        return new ModelAndView(addOrEdit, model.addAttribute("bcBomVersionDetail", bcBomVersionDetail));
    }
    @Journal(name="包材BOM明细导入")
    @RequestMapping(value="upload",method = RequestMethod.GET)
    public ModelAndView _drawingsImport(){
        String filePath=UUID.randomUUID().toString();
        return new ModelAndView(PAGE_BOMDETAILIMPORT, model.addAttribute("filePath", filePath));
    }
    @Journal(name="包材BOM明细导出模板")
    @RequestMapping(value="export",method = RequestMethod.GET)
    public void _drawingsExport() throws Exception {
        InputStream is = new FileInputStream(PathUtils.getClassPath() + "template/bcBomDetail.xlsx");
        Workbook wb = new SXSSFWorkbook(new XSSFWorkbook(is));
        HttpUtils.download(response,wb,"包材BOM明细导出模板");
        is.close();
    }
    @ResponseBody
    @Journal(name = "包材BOM明细导入", logType = LogType.DB)
    @RequestMapping(value = "importBcBomUploadFile")
    public String importcutTcBomMainUploadFile(@RequestParam(value = "file") MultipartFile file,String packVersionId) throws Exception {
        BCBomVersion bvs = bCBomVersionService.findById(BCBomVersion.class, Long.parseLong(packVersionId));
        if (bvs.getAuditState() > 0) {
            return ajaxError("不能修改审核中或已通过的数据");
        }
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        String result = bCBomVersionService.importBcBomMainUploadFile(file, userId,packVersionId);
        return GsonTools.toJson(result);
    }

    @ResponseBody
    @Journal(name = "保存包材材BOM明细", logType = LogType.DB)
    @RequestMapping(value = "saveDetail", method = RequestMethod.POST)
    public String saveDetail(@RequestBody BcBomVersionDetail detail) throws Exception {
        BCBomVersion bvs = bCBomVersionService.findById(BCBomVersion.class, detail.getPackVersionId());
        if (bvs.getAuditState() > 0) {
            return ajaxError("不能修改审核中或已通过的数据");
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("packVersionId", detail.getPackVersionId());
        List<BcBomVersionDetail> li = bcBomVersionDetailService.findListByMap(BcBomVersionDetail.class, map);
        if (detail.getId() == null) {
            bcBomVersionDetailService.save(detail);
        } else {
            bcBomVersionDetailService.update2(detail);
        }
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "编辑包材bom明细页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(BcBomVersionDetail bcBomVersionDetail) {
        bcBomVersionDetail = bcBomVersionDetailService.findById(BcBomVersionDetail.class, bcBomVersionDetail.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("bcBomVersionDetail", bcBomVersionDetail));
    }

    @ResponseBody
    @Journal(name = "编辑包材bom明细", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(BcBomVersionDetail bcBomVersionDetail) throws Exception {
        HashMap<String, Object> map = new HashMap();
        map.put("packVersionId", bcBomVersionDetail.getPackVersionId());
        bcBomVersionDetailService.update2(bcBomVersionDetail);
        return GsonTools.toJson(bcBomVersionDetail);
    }

    @ResponseBody
    @Journal(name = "删除包材bom明细", logType = LogType.DB)
    @RequestMapping(value = {"delete"}, method = RequestMethod.POST)
    public String delete(String ids) throws Exception {
        String id[] = ids.split(",");
        BcBomVersionDetail bcBomVersionDetail = bcBomVersionDetailService.findById(BcBomVersionDetail.class, Long.parseLong(id[0]));
        BCBomVersion bvs = bCBomVersionService.findById(BCBomVersion.class, bcBomVersionDetail.getPackVersionId());
        if (bvs.getAuditState() > 0) {
            return ajaxError("不能修改审核中或已通过的数据");
        }
        this.bcBomVersionDetailService.deleteAll(ids);
        return "{}";
    }

    @ResponseBody
    @Journal(name = "获取包材bom列表信息")
    @RequestMapping("listBom")
    public String getBcBom(String nodeType, String id, String data) {
        JSONArray jarray = new JSONArray();
        if (data == null) {
            data = "";
        }
        String result = "";
        if (nodeType == (null)) {
            JSONObject json = new JSONObject();
            json.put("id", "root");
            json.put("text", "包材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("nodeType", "root"));
            jarray.put(json);
            result = jarray.toString();
        } else if (nodeType.equals("root") && data != "") {
            List<Map<String, Object>> list = bcBomService.getBcBomJson(data);
            if (list.size() > 0) {
                if ("-1".equals(((Map<String, Object>) list.get(0).get("attributes")).get("PACKCANCELED") + "")) {
                    list.get(0).put("text", list.get(0).get("text") + "[<font color='red'>作废</font>]");
                }
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "包材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("nodeType", "root"));
                json.put("children", list);
                jarray.put(json);
            } else {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "包材bom");
                json.put("state", "closed");
                jarray.put(json);
            }
            result = jarray.toString();
        } else if (nodeType.equals("bom")) {
            result = GsonTools.toJson(bCBomVersionService.getBcBomJson(id));
        } else {
            List<Map<String, Object>> list = bcBomService.getBcBomJson(data);
            for (Map<String, Object> m : list) {
                if ("-1".equals(((Map<String, Object>) m.get("attributes")).get("PACKCANCELED") + "")) {
                    m.put("text", m.get("text") + "[<font color='red'>作废</font>]");
                }
            }
            result = GsonTools.toJson(list);
        }
        return result;
    }

    @ResponseBody
    @Journal(name = "获取试样包材bom列表信息")
    @RequestMapping("listBomTest")
    public String getBcBomTest(String nodeType, String id, String data) throws Exception {
        JSONArray jarray = new JSONArray();
        if (data == null) {
            data = "";
        }
        String result;
        if (nodeType == (null)) {
            JSONObject json = new JSONObject();
            json.put("id", "root");
            json.put("text", "包材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("nodeType", "root"));
            jarray.put(json);
            result = jarray.toString();
        } else if (nodeType.equals("root") && data != "") {
            List<Map<String, Object>> list = bcBomService.getBcBomJsonTest(data);
            if (list.size() > 0) {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "包材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("nodeType", "root"));
                json.put("children", bcBomService.getBcBomJsonTest(data));
                jarray.put(json);
                result = jarray.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "包材bom");
                json.put("state", "closed");
                jarray.put(json);
                result = jarray.toString();
            }
        } else if (nodeType.equals("bom")) {
            result = GsonTools.toJson(bCBomVersionService.getBcBomJson(id));
        } else {
            result = GsonTools.toJson(bcBomService.getBcBomJsonTest(data));
        }
        return result;
    }

    @ResponseBody
    @Journal(name = "获取试样包材bom列表信息")
    @RequestMapping("listBomTest1")
    public String getBcBomTest1(String nodeType, String id, String data) throws Exception {
        JSONArray jarray = new JSONArray();
        if (data == null) {
            data = "";
        }
        String result;
        if (nodeType == (null)) {
            JSONObject json = new JSONObject();
            json.put("id", "root");
            json.put("text", "包材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("nodeType", "root"));
            jarray.put(json);
            result = jarray.toString();
        } else if (nodeType.equals("root") && data != "") {
            List<Map<String, Object>> list = bcBomService.getBcBomJsonTest1(data);
            if (list.size() > 0) {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "包材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("nodeType", "root"));
                json.put("children", bcBomService.getBcBomJsonTest1(data));
                jarray.put(json);
                result = jarray.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "包材bom");
                json.put("state", "closed");
                jarray.put(json);
                result = jarray.toString();
            }
        } else if (nodeType.equals("bom")) {
            result = GsonTools.toJson(bCBomVersionService.getBcBomJson(id));
        } else {
            result = GsonTools.toJson(bcBomService.getBcBomJsonTest1(data));
        }
        return result;
    }

    @Journal(name = "添加包材bom页面")
    @RequestMapping(value = "addBom", method = RequestMethod.GET)
    public ModelAndView _addBom(BcBom bcBom) {
        return new ModelAndView(addOrEditBom, model.addAttribute("bcBom", bcBom).addAttribute("consumerName", ""));
    }

    @ResponseBody
    @Journal(name = "保存包材bom", logType = LogType.DB)
    @RequestMapping(value = "addBom", method = RequestMethod.POST)
    @Valid
    public String addBom(BcBom bcBom) {
        HashMap<String, Object> map = new HashMap();
        map.put("packBomGenericName", bcBom.getPackBomGenericName());
        bcBomService.save(bcBom);
        return GsonTools.toJson(bcBom);
    }

    @Journal(name = "编辑包材bom页面")
    @RequestMapping(value = "editBom", method = RequestMethod.GET)
    public ModelAndView _editBom(BcBom bcBom) {
        bcBom = bcBomService.findById(BcBom.class, bcBom.getId());
        Consumer c = bcBomService.findById(Consumer.class, bcBom.getPackBomConsumerId());
        return new ModelAndView(addOrEditBom, model.addAttribute("bcBom", bcBom).addAttribute("consumerName", c.getConsumerName()));
    }

    @ResponseBody
    @Journal(name = "编辑包材bom", logType = LogType.DB)
    @RequestMapping(value = "editBom", method = RequestMethod.POST)
    @Valid
    public String editBom(BcBom bcBom) throws Exception {
        HashMap<String, Object> map = new HashMap();
        map.put("packBomGenericName", bcBom.getPackBomGenericName());
        if (bcBomService.isExist(BcBom.class, map, bcBom.getId(), true)) {
            return ajaxError("已经有同名包材BOM");
        }

        HashMap<String, Object> map2 = new HashMap();
        map2.put("packBomId", bcBom.getId());
        List<BCBomVersion> li1 = bCBomVersionService.findListByMap(BCBomVersion.class, map2);
        for (BCBomVersion bv : li1) {
            if (bv.getAuditState() > 0) {
                return ajaxError("含有审核中或已通过的版本,不能修改");
            }
        }
        bcBomService.update2(bcBom);
        return GsonTools.toJson(bcBom);
    }

    @ResponseBody
    @Journal(name = "删除包材bom", logType = LogType.DB)
    @RequestMapping(value = {"deleteBom"}, method = RequestMethod.POST)
    public String deleteBom(String ids) throws Exception {
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            HashMap<String, Object> map = new HashMap();
            map.put("packBomId", Long.parseLong(id[a]));
            List<BCBomVersion> li = bCBomVersionService.findListByMap(BCBomVersion.class, map);
            if (li != null && li.size() > 0) {
                return ajaxError("请删除版本后删除工艺");
            }
        }
        this.bcBomService.deleteAll(ids);
        return "{}";
    }

    @ResponseBody
    @Journal(name = "作废包材bom", logType = LogType.DB)
    @RequestMapping(value = {"cancelBom"}, method = RequestMethod.POST)
    public String cancelBom(String ids) {
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            BcBom bcBom = bcBomService.findById(BcBom.class, Long.parseLong(id[a]));
            bcBom.setPackCanceled(-1);
            this.bcBomService.update(bcBom);
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "获取包材bom版本信息列表信息")
    @RequestMapping("listBomVersion")
    public String getBCBomVersion(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(bCBomVersionService.findPageInfo(filter, page));
    }

    @Journal(name = "添加包材bom版本信息页面")
    @RequestMapping(value = "addBomVersion", method = RequestMethod.GET)
    public ModelAndView _addBomVersion(BCBomVersion bCBomVersion) {
        String filePath = UUID.randomUUID().toString();
        return new ModelAndView(addOrEditBomVersion, model.addAttribute("bCBomVersion", bCBomVersion).addAttribute("filePath", filePath));
    }

    @ResponseBody
    @Journal(name = "保存包材bom版本信息", logType = LogType.DB)
    @RequestMapping(value = "addBomVersion", method = RequestMethod.POST)
    @Valid
    public String addBomVersion(BCBomVersion bCBomVersion, Long fileId) throws Exception {
        HashMap<String, Object> map = new HashMap();
        map.put("packBomId", bCBomVersion.getPackBomId());
        List<BCBomVersion> li = bCBomVersionService.findListByMap(BCBomVersion.class, map);
        for (BCBomVersion bv : li) {
            if (bv.getPackVersion().equals(bCBomVersion.getPackVersion())) {
                return ajaxError("已经有相同名字的版本");
            }
        }

        if (bCBomVersion.getPackEnabled() == null) {
            bCBomVersion.setPackEnabled(1);
        }

        if (bCBomVersion.getPackIsDefault() == null) {
            bCBomVersion.setPackIsDefault(1);
        }

        bCBomVersionService.save(bCBomVersion);
        bCBomVersionService.savePdfFile(bCBomVersion, fileId, new ExcelImportMessage());
        return GsonTools.toJson(bCBomVersion);
    }

    @Journal(name = "编辑包材bom版本信息页面")
    @RequestMapping(value = "editBomVersion", method = RequestMethod.GET)
    public ModelAndView _editBomVersion(BCBomVersion bCBomVersion) {
        bCBomVersion = bCBomVersionService.findById(BCBomVersion.class, bCBomVersion.getId());
        String filePath = UUID.randomUUID().toString();
        return new ModelAndView(addOrEditBomVersion, model.addAttribute("bCBomVersion", bCBomVersion).addAttribute("filePath", filePath));
    }

    @ResponseBody
    @Journal(name = "编辑包材bom版本信息", logType = LogType.DB)
    @RequestMapping(value = "editBomVersion", method = RequestMethod.POST)
    @Valid
    public String editBomVersion(BCBomVersion bCBomVersion, Long fileId) throws Exception {
        HashMap<String, Object> map = new HashMap();
        map.put("packBomId", bCBomVersion.getPackBomId());
        List<BCBomVersion> li = bCBomVersionService.findListByMap(BCBomVersion.class, map);
        for (BCBomVersion bv : li) {
            if (bv.getId() == bCBomVersion.getId()) {
                if (bv.getAuditState() > 0) {
                    return ajaxError("不能修改审核中或已通过的数据");
                }
            }
            if (bv.getPackVersion().equals(bCBomVersion.getPackVersion()) && !bv.getId().equals(bCBomVersion.getId())) {
                return ajaxError("已经有相同名字的版本");
            }
        }
        bCBomVersionService.update2(bCBomVersion);
        bCBomVersionService.savePdfFile(bCBomVersion, fileId, new ExcelImportMessage());
        return GsonTools.toJson(bCBomVersion);
    }

    @ResponseBody
    @Journal(name = "删除包材bom版本信息", logType = LogType.DB)
    @RequestMapping(value = {"deleteBomVersion"}, method = RequestMethod.POST)
    public String deleteBomVersion(String ids) throws Exception {
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            BCBomVersion bCBomVersion = bCBomVersionService.findById(BCBomVersion.class, Long.parseLong(id[a]));
            if (bCBomVersion.getAuditState() > 0) {
                return ajaxError("不能修改审核中或已通过的数据");
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("packBomId", bCBomVersion.getId());
            List<FinishedProduct> fp = bCBomVersionService.findListByMap(FinishedProduct.class, map);
            if (fp != null && fp.size() > 0) {
                return ajaxError("该版本被产品使用，请修改相关产品工艺后删除版本");
            }
        }
        bCBomVersionService.deleteAll(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "查看BOM下的版本")
    @RequestMapping(value = "findV", method = RequestMethod.POST)
    public String findV(String id) {
        HashMap<String, Object> map = new HashMap();
        map.put("packBomId", Long.valueOf(id));
        List<BCBomVersion> list = bCBomVersionService.findListByMap(BCBomVersion.class, map);
        if (list.size() == 0) {
            return GsonTools.toJson(0);
        } else {
            return GsonTools.toJson(1);
        }
    }
}
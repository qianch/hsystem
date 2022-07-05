/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.audit.service.IAuditInstanceService;
import com.bluebirdme.mes.baseInfo.entity.BCBomVersion;
import com.bluebirdme.mes.baseInfo.entity.BcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBom;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.FtcBcBomVersionDetail;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomService;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomVersionDetailService;
import com.bluebirdme.mes.baseInfo.service.IFtcBcBomVersionService;
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

/**
 * 包装版本信息Controller
 *
 * @author 高飞
 * @Date 2017-11-28 11:10:48
 */
@Controller
@RequestMapping("/bom/ftcBc")
@Journal(name = "非套材包材BOM")
public class FtcBcBomController extends BaseController {
    /**
     * 包装版本信息页面
     */
    final String index = "baseInfo/ftcBcBom/ftcBcBomVersionDetail";
    /**
     * 非套材包材bom添加/修改页面
     */
    final String addOrEditBom = "baseInfo/ftcBcBom/ftcBcBomAddOrEdit";
    /**
     * 非套材包材bom版本添加/修改页面
     */
    final String addOrEditBomVersion = "baseInfo/ftcBcBom/ftcBcBomVersionAddOrEdit";

    final String auditBomVersion = "baseInfo/ftcBcBom/auditBomVersion";

    @Resource
    IFtcBcBomService ftcBcBomService;

    @Resource
    IFtcBcBomVersionService ftcBcBomVersionService;

    @Resource
    IFtcBcBomVersionDetailService ftcBcBomVersionDetailService;

    @Resource
    IAuditInstanceService auditInstanceService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @ResponseBody
    @Journal(name = "获取非套材包材bom明细列表信息")
    @RequestMapping("list")
    public String getFtcBcBomVersionDetail(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(ftcBcBomVersionDetailService.findPageInfo(filter, page));
    }

    @ResponseBody
    @Journal(name = "保存非套材包材材BOM明细", logType = LogType.DB)
    @RequestMapping(value = "saveDetail", method = RequestMethod.POST)
    public String saveDetail(@RequestBody FtcBcBomVersionDetail detail) throws Exception {
        FtcBcBomVersion bvs = ftcBcBomVersionService.findById(FtcBcBomVersion.class, detail.getPackVersionId());
        if (bvs.getAuditState() > 0) {
            return ajaxError("不能修改审核中或已通过的数据");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("packVersionId", detail.getPackVersionId());
        if (detail.getId() == null) {
            ftcBcBomVersionDetailService.save(detail);
        } else {
            ftcBcBomVersionDetailService.update2(detail);
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "删除包材bom明细", logType = LogType.DB)
    @RequestMapping(value = {"delete"}, method = RequestMethod.POST)
    public String delete(String ids) throws Exception {
        String id[] = ids.split(",");
        FtcBcBomVersionDetail ftcBcBomVersionDetail = ftcBcBomVersionDetailService.findById(FtcBcBomVersionDetail.class, Long.parseLong(id[0]));

        FtcBcBomVersion bvs = ftcBcBomVersionService.findById(FtcBcBomVersion.class, ftcBcBomVersionDetail.getPackVersionId());
        if (bvs.getAuditState() > 0) {
            return ajaxError("不能修改审核中或已通过的数据");
        }

        this.ftcBcBomVersionDetailService.deleteAll(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "获取非套材包材bom列表信息")
    @RequestMapping("listBom")
    public String getFtcBcBom(String nodeType, String id, String data) throws Exception {
        JSONArray jarray = new JSONArray();
        if (data == null) {
            data = "";
        }
        if (StringUtils.equals(nodeType, "root")) {
            id = null;
        }
        String result = "";
        if (nodeType == (null)) {
            JSONObject json = new JSONObject();
            json.put("id", "root");
            json.put("text", "非套材包材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("nodeType", "root"));
            jarray.put(json);
            result = jarray.toString();
        } else if (nodeType.equals("root") && StringUtils.isNotBlank(data)) {    //带参数查询
            List<Map<String, Object>> list = ftcBcBomService.getFtcBcBomJson(id, data, null);
            if (list.size() > 0) {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材包材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("nodeType", "root"));
                json.put("children", ftcBcBomService.getFtcBcBomJson(id, data, null));
                jarray.put(json);
                result = jarray.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材包材bom");
                json.put("state", "closed");
                jarray.put(json);
                result = jarray.toString();
            }
            //	result = GsonTools.toJson(bcBomService.getBcBomJson(data));
        } else if (nodeType.equals("bom1")) {
            result = GsonTools.toJson(ftcBcBomService.getFtcBcBomJson(id, data, 2));
        } else if (nodeType.equals("bom2")) {
            result = GsonTools.toJson(ftcBcBomService.getFtcBcBomJson(id, data, 3));
        } else if (nodeType.equals("bom3")) {
            result = GsonTools.toJson(ftcBcBomVersionService.getFtcBcBomJson(id, "0"));
        } else {
            result = GsonTools.toJson(ftcBcBomService.getFtcBcBomJson(id, data, null));
        }
        // System.out.println(result);
        return result;
    }

    @ResponseBody
    @Journal(name = "获取非套材包材bom列表信息")
    @RequestMapping("listBomTest")
    public String getBcBomTest(String nodeType, String id, String data) throws Exception {
        JSONArray jarray = new JSONArray();
        if (data == null) {
            data = "";
        }
        if (StringUtils.equals(nodeType, "root")) {
            id = null;
        }
        String result = "";
        if (nodeType == (null)) {
            JSONObject json = new JSONObject();
            json.put("id", "root");
            json.put("text", "非套材包材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("nodeType", "root"));
            jarray.put(json);
            result = jarray.toString();
        } else if (nodeType.equals("root") && StringUtils.isNotBlank(data)) {    //带参数查询
            List<Map<String, Object>> list = ftcBcBomService.getFtcBcBomJson(id, data, null);
            if (list.size() > 0) {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材包材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("nodeType", "root"));
                json.put("children", ftcBcBomService.getFtcBcBomJson(id, data, null));
                jarray.put(json);
                result = jarray.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材包材bom");
                json.put("state", "closed");
                jarray.put(json);
                result = jarray.toString();
            }
            //	result = GsonTools.toJson(bcBomService.getBcBomJson(data));
        } else if (nodeType.equals("bom1")) {
            result = GsonTools.toJson(ftcBcBomService.getFtcBcBomJson(id, data, 2));
        } else if (nodeType.equals("bom2")) {
            result = GsonTools.toJson(ftcBcBomService.getFtcBcBomJson(id, data, 3));
        } else if (nodeType.equals("bom3")) {
            result = GsonTools.toJson(ftcBcBomVersionService.getFtcBcBomJson(id, "1"));
        } else {
            result = GsonTools.toJson(ftcBcBomService.getFtcBcBomJson(id, data, null));
        }
        // System.out.println(result);
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
        if (StringUtils.equals(nodeType, "root")) {
            id = null;
        }
        String result = "";
        if (nodeType == (null)) {
            JSONObject json = new JSONObject();
            json.put("id", "root");
            json.put("text", "非套材包材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("nodeType", "root"));
            jarray.put(json);
            result = jarray.toString();
        } else if (nodeType.equals("root") && StringUtils.isNotBlank(data)) {    //带参数查询
            List<Map<String, Object>> list = ftcBcBomService.getFtcBcBomJson(id, data, null);
            if (list.size() > 0) {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材包材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("nodeType", "root"));
                json.put("children", ftcBcBomService.getFtcBcBomJson(id, data, null));
                jarray.put(json);
                result = jarray.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("id", "root");
                json.put("text", "非套材包材bom");
                json.put("state", "closed");
                jarray.put(json);
                result = jarray.toString();
            }
            //	result = GsonTools.toJson(bcBomService.getBcBomJson(data));
        } else if (nodeType.equals("bom1")) {
            result = GsonTools.toJson(ftcBcBomService.getFtcBcBomJson(id, data, 2));
        } else if (nodeType.equals("bom2")) {
            result = GsonTools.toJson(ftcBcBomService.getFtcBcBomJson(id, data, 3));
        } else if (nodeType.equals("bom3")) {
            result = GsonTools.toJson(ftcBcBomVersionService.getFtcBcBomJson(id, "2"));
        } else {
            result = GsonTools.toJson(ftcBcBomService.getFtcBcBomJson(id, data, null));
        }
        // System.out.println(result);
        return result;
    }

    @Journal(name = "添加非套材包材bom页面")
    @RequestMapping(value = "addBom", method = RequestMethod.GET)
    public ModelAndView _addBom(FtcBcBom ftcBcBom) {
        String filePath = UUID.randomUUID().toString();
        return new ModelAndView(addOrEditBom, model.addAttribute("ftcBcBom", ftcBcBom).addAttribute("filePath", filePath));
    }

    @ResponseBody
    @Journal(name = "保存非套材包材bom", logType = LogType.DB)
    @RequestMapping(value = "addBom", method = RequestMethod.POST)
    @Valid
    public String addBom(FtcBcBom ftcBcBom, Long fileId) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", ftcBcBom.getCode());
        map.put("level", ftcBcBom.getLevel());
//		map.put("pid", ftcBcBom.getPid());
        if (ftcBcBomService.isExist(FtcBcBom.class, map, true)) {
            return ajaxError("已经有同名非套材包材BOM");
        }

        if (ftcBcBom.getLevel() != 2 || fileId == null) {
            ftcBcBomService.save(ftcBcBom);
        } else {//目录BOM是第2层和有fileId(要导入的Excel文件ID)
            ExcelImportMessage eim = ftcBcBomService.doAddFtcBcBom(ftcBcBom, fileId);
            ftcBcBomService.savePdfFile(ftcBcBom, fileId, eim);
            if (eim != null && eim.hasError()) {
                Map<String, String> excelErrorMsg = new HashMap<>();
                excelErrorMsg.put("excelErrorMsg", eim.getMessage());
                return GsonTools.toJson(excelErrorMsg);
            }
        }

        return GsonTools.toJson(ftcBcBom);
    }

    @Journal(name = "编辑非套材包材bom页面")
    @RequestMapping(value = "editBom", method = RequestMethod.GET)
    public ModelAndView _editBom(FtcBcBom ftcBcBom) {
        ftcBcBom = ftcBcBomService.findById(FtcBcBom.class, ftcBcBom.getId());
        String filePath = UUID.randomUUID().toString();
        return new ModelAndView(addOrEditBom, model.addAttribute("ftcBcBom", ftcBcBom).addAttribute("filePath", filePath));
    }

    @ResponseBody
    @Journal(name = "编辑非套材包材bom", logType = LogType.DB)
    @RequestMapping(value = "editBom", method = RequestMethod.POST)
    @Valid
    public String editBom(FtcBcBom ftcBcBom, Long fileId) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("code", ftcBcBom.getCode());
        map.put("level", ftcBcBom.getLevel());
//		map.put("pid", ftcBcBom.getPid());
        if (ftcBcBomService.isExist(FtcBcBom.class, map, ftcBcBom.getId(), true)) {
            return ajaxError("已经有同名非套材包材BOM");
        }
        FtcBcBom fbb = ftcBcBomService.findById(FtcBcBom.class, ftcBcBom.getId());
        if (!StringUtils.equals(ftcBcBom.getCode(), fbb.getCode()) || !StringUtils.equals(ftcBcBom.getName(), fbb.getName())) {
            if (ftcBcBom.getLevel() == 1) {
                Map<String, Object> param = new HashMap<>();
                param.put("pid", ftcBcBom.getId());
                List<FtcBcBom> fbbList = ftcBcBomService.findListByMap(FtcBcBom.class, param);
                for (FtcBcBom ftcBcBom2 : fbbList) {
                    param.clear();
                    param.put("pid", ftcBcBom2.getId());
                    List<FtcBcBom> fbb2List = ftcBcBomService.findListByMap(FtcBcBom.class, param);
                    for (FtcBcBom ftcBcBom3 : fbb2List) {
                        HashMap<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("bid", ftcBcBom3.getId());

                        List<FtcBcBomVersion> li1 = ftcBcBomService.findListByMap(FtcBcBomVersion.class, map2);
                        for (FtcBcBomVersion bv : li1) {
                            if (bv.getAuditState() > 0) {
                                return ajaxError("含有审核中或已通过的版本,不能修改“产品规格”和“包装标准代码”");
                            }
                        }
                    }
                }
            }
            if (ftcBcBom.getLevel() == 2) {
                Map<String, Object> param = new HashMap<>();
                param.put("pid", ftcBcBom.getId());
                List<FtcBcBom> fbbList = ftcBcBomService.findListByMap(FtcBcBom.class, param);
                for (FtcBcBom ftcBcBom3 : fbbList) {
                    HashMap<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("bid", ftcBcBom3.getId());

                    List<FtcBcBomVersion> li1 = ftcBcBomService.findListByMap(FtcBcBomVersion.class, map2);
                    for (FtcBcBomVersion bv : li1) {
                        if (bv.getAuditState() > 0) {
                            return ajaxError("含有审核中或已通过的版本,不能修改“厂内名称”和“二级包装工艺代码”");
                        }
                    }
                }
            }
            HashMap<String, Object> map2 = new HashMap<String, Object>();
            map2.put("bid", ftcBcBom.getId());

            List<FtcBcBomVersion> li1 = ftcBcBomService.findListByMap(FtcBcBomVersion.class, map2);
            for (FtcBcBomVersion bv : li1) {
                if (bv.getAuditState() > 0) {
                    if (!fbb.getCode().equals(ftcBcBom.getCode())) {
                        return ajaxError("含有审核中或已通过的版本,不能修改“三级包装工艺代码”");
                    }

                }
            }
        }

//		ftcBcBomService.update2(ftcBcBom);
        if (ftcBcBom.getLevel() != 2 || fileId == null) {
            ftcBcBomService.update2(ftcBcBom);
        } else {//目录BOM是第2层和有fileId(要导入的Excel文件ID)
            StringBuffer info = new StringBuffer();
            ExcelImportMessage eim = ftcBcBomService.doUpdateFtcBcBom(ftcBcBom, fileId, info);
            ftcBcBomService.savePdfFile(ftcBcBom, fileId, eim);
            if (eim != null && eim.hasError()) {
                Map<String, String> excelErrorMsg = new HashMap<>();
                excelErrorMsg.put("excelErrorMsg", eim.getMessage());
                return GsonTools.toJson(excelErrorMsg);
            } else {
                return ajaxSuccess(info);
            }
        }

        return GsonTools.toJson(ftcBcBom);
    }


    @ResponseBody
    @Journal(name = "删除非套材包材bom", logType = LogType.DB)
    @RequestMapping(value = {"deleteBom"}, method = RequestMethod.POST)
    public String deleteBom(String ids) throws Exception {
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            FtcBcBom fbb = ftcBcBomService.findById(FtcBcBom.class, Long.parseLong(id[a]));
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (fbb.getLevel() == 1) {
                map.put("pid", Long.parseLong(id[a]));
                List<FtcBcBom> li = ftcBcBomVersionService.findListByMap(FtcBcBom.class, map);
                if (li != null && li.size() > 0) {
                    return ajaxError("请删除一级包装工艺前删除二级包装工艺");
                }
            } else if (fbb.getLevel() == 2) {
                map.put("pid", Long.parseLong(id[a]));
                List<FtcBcBom> li = ftcBcBomVersionService.findListByMap(FtcBcBom.class, map);
                if (li != null && li.size() > 0) {
                    return ajaxError("请删除二级包装工艺前删除三级包装工艺");
                }
            } else if (fbb.getLevel() == 3) {
                map.put("bid", Long.parseLong(id[a]));

                List<FtcBcBomVersion> li = ftcBcBomVersionService.findListByMap(FtcBcBomVersion.class, map);
                if (li != null && li.size() > 0) {
                    return ajaxError("请删除三级包装工艺前删除版本");
                }
            }


        }
        this.ftcBcBomService.deleteAll(ids);
        return "{}";
    }

    @Journal(name = "添加非套材包材bom版本信息页面")
    @RequestMapping(value = "addBomVersion", method = RequestMethod.GET)
    public ModelAndView _addBomVersion(FtcBcBomVersion ftcBcBomVersion) {
        return new ModelAndView(addOrEditBomVersion, model.addAttribute("ftcBcBomVersion", ftcBcBomVersion).addAttribute("consumerName", ""));
    }

    @ResponseBody
    @Journal(name = "保存非套材包材bom版本信息")
    @RequestMapping(value = "addBomVersion", method = RequestMethod.POST)
    public String addBomVersion(FtcBcBomVersion ftcBcBomVersion) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("bid", ftcBcBomVersion.getBid());

        List<FtcBcBomVersion> li = ftcBcBomService.findListByMap(FtcBcBomVersion.class, map);
        for (FtcBcBomVersion bcv : li) {
            if (bcv.getVersion().equals(ftcBcBomVersion.getVersion())) {
                return ajaxError("已经有相同名字的版本");
            }
        }
        Map<String, Object> param = new HashMap<>();
        param.put("bid", ftcBcBomVersion.getBid());
        param.put("productType", ftcBcBomVersion.getProductType());
        List<FtcBcBomVersion> fbbvL = ftcBcBomVersionService.findListByMap(FtcBcBomVersion.class, param);
        for (FtcBcBomVersion fbbv : fbbvL) {
            if (fbbv.getAuditState() < 2) {
                return ajaxError("已有[未提交]、[不通过]或[审核中]的版本！");
            }
        }
        if (ftcBcBomVersion.getEnabled() == null) {
            ftcBcBomVersion.setEnabled(1);
        }

        ftcBcBomService.save(ftcBcBomVersion);
        return GsonTools.toJson(ftcBcBomVersion);
    }

    @Journal(name = "编辑非套材包材bom版本信息页面")
    @RequestMapping(value = "editBomVersion", method = RequestMethod.GET)
    public ModelAndView _editBomVersion(FtcBcBomVersion ftcBcBomVersion) {
        ftcBcBomVersion = ftcBcBomVersionService.findById(FtcBcBomVersion.class, ftcBcBomVersion.getId());
        Consumer c = ftcBcBomService.findById(Consumer.class, ftcBcBomVersion.getConsumerId());
        return new ModelAndView(addOrEditBomVersion, model.addAttribute("ftcBcBomVersion", ftcBcBomVersion).addAttribute("consumerName", c.getConsumerName()));
    }

    @ResponseBody
    @Journal(name = "编辑非套材包材bom版本信息", logType = LogType.DB)
    @RequestMapping(value = "editBomVersion", method = RequestMethod.POST)
    @Valid
    public String editBomVersion(FtcBcBomVersion ftcBcBomVersion) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("bid", ftcBcBomVersion.getBid());

        List<FtcBcBomVersion> li = ftcBcBomVersionService.findListByMap(FtcBcBomVersion.class, map);
        for (FtcBcBomVersion bv : li) {
            if (bv.getId() == ftcBcBomVersion.getId()) {
                if (bv.getAuditState() > 0) {
                    return ajaxError("不能修改审核中或已通过的数据");
                }
            }
            if (bv.getVersion().equals(ftcBcBomVersion.getVersion()) && !bv.getId().equals(ftcBcBomVersion.getId())) {
                return ajaxError("已经有相同名字的版本");
            }
        }
        ftcBcBomVersionService.update2(ftcBcBomVersion);
        return GsonTools.toJson(ftcBcBomVersion);
    }

    @ResponseBody
    @Journal(name = "删除非套材包材bom版本信息", logType = LogType.DB)
    @RequestMapping(value = {"deleteBomVersion"}, method = RequestMethod.POST)
    public String deleteBomVersion(String ids) throws Exception {
        // this.bCBomVersionService.delete(ids);
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            FtcBcBomVersion ftcBcBomVersion = ftcBcBomVersionService.findById(FtcBcBomVersion.class, Long.parseLong(id[a]));
            if (ftcBcBomVersion.getAuditState() > 0) {
                return ajaxError("不能修改审核中或已通过的数据");
            }
        }
        ftcBcBomVersionService.deleteAll(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "查看BOM下的版本是否存在")
    @RequestMapping(value = "findV")
    public String findV(String id, String version) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("bid", Long.valueOf(id));
        List<FtcBcBomVersion> list = ftcBcBomVersionService.findListByMap(FtcBcBomVersion.class, map);
        for (FtcBcBomVersion bcv : list) {
            if (bcv.getVersion().equals(version)) {
                return GsonTools.toJson(0);
            }
        }
        return GsonTools.toJson(1);
    }

    @Journal(name = "审核非套材包材bom版本信息页面")
    @RequestMapping(value = "auditBomVersion", method = RequestMethod.GET)
    public ModelAndView auditBomVersion(String id) {
        FtcBcBomVersion ftcBcBomVersion = ftcBcBomVersionService.findById(FtcBcBomVersion.class, Long.parseLong(id));
        FtcBcBom ftcBcBom = ftcBcBomVersionService.findById(FtcBcBom.class, ftcBcBomVersion.getBid());
        Consumer consumer = ftcBcBomVersionService.findById(Consumer.class, ftcBcBomVersion.getConsumerId());
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("packVersionId", ftcBcBomVersion.getId());
        List<FtcBcBomVersionDetail> li = ftcBcBomVersionService.findListByMap(FtcBcBomVersionDetail.class, param);

        return new ModelAndView(auditBomVersion, model.addAttribute("ftcBcBomVersion", ftcBcBomVersion).addAttribute("details", GsonTools.toJson(li)).addAttribute("ftcBcBom", ftcBcBom).addAttribute("consumer", consumer));
    }

    @ResponseBody
    @Journal(name = "提交审核", logType = LogType.DB)
    @RequestMapping(value = "commit", method = RequestMethod.POST)
    public String commit(String id, String name) throws Exception {
        FtcBcBomVersion ftcBcBomVersion = ftcBcBomVersionService.findById(FtcBcBomVersion.class, Long.parseLong(id));
        Map<String, Object> param = new HashMap<>();
        param.put("bid", ftcBcBomVersion.getBid());
        param.put("productType", ftcBcBomVersion.getProductType());
        List<FtcBcBomVersion> fbbvL = ftcBcBomVersionService.findListByMap(FtcBcBomVersion.class, param);
        for (FtcBcBomVersion fbbv : fbbvL) {
            if (fbbv.getAuditState() == 1) {
                return ajaxError("已有[审核中]的版本！");
            }
        }
        if (StringUtils.equals(ftcBcBomVersion.getProductType(), "0")) {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.FTCBC, Long.parseLong(String.valueOf(session.getAttribute(Constant.CURRENT_USER_ID))), "bom/ftcBc/auditBomVersion?id=" + id, ftcBcBomVersion.getId(), FtcBcBomVersion.class);
        } else if (StringUtils.equals(ftcBcBomVersion.getProductType(), "1")) {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.FTCBC1, Long.parseLong(String.valueOf(session.getAttribute(Constant.CURRENT_USER_ID))), "bom/ftcBc/auditBomVersion?id=" + id, ftcBcBomVersion.getId(), FtcBcBomVersion.class);
        } else {
            auditInstanceService.submitAudit(name, AuditConstant.CODE.FTCBC2, Long.parseLong(String.valueOf(session.getAttribute(Constant.CURRENT_USER_ID))), "bom/ftcBc/auditBomVersion?id=" + id, ftcBcBomVersion.getId(), FtcBcBomVersion.class);
        }
        return ajaxSuccess();
    }

    @RequestMapping(value = "rebuildBcAudit", method = RequestMethod.GET)
    public void rebuildBcAudit() throws Exception {
        List<FtcBcBomVersion> versionList = ftcBcBomVersionService.findAll(FtcBcBomVersion.class);
        for (FtcBcBomVersion bv : versionList) {

            if (bv.getAuditState() == null)
                continue;
            if (bv.getAuditState() > 0 && bv.getAuditState() < 2) {
                FtcBcBom bom = ftcBcBomVersionService.findById(FtcBcBom.class, bv.getBid());
                commit(bv.getId() + "", "非套材包材BOM审核：BOM名称：" + bom.getName() + "/" + bom.getCode() + "BOM版本：" + bv.getVersion());
            }
        }
    }

    @ResponseBody
    @Journal(name = "根据代码查询BOM信息")
    @RequestMapping(value = "versionInfo", method = RequestMethod.GET)
    public String getCodeInfo(Long vid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", vid);
        return GsonTools.toJson(ftcBcBomVersionService.findUniqueByMap(FtcBcBomVersion.class, map));

    }


    @ResponseBody
    @Journal(name = "作废bom", logType = LogType.DB)
    @RequestMapping(value = {"cancelBom"}, method = RequestMethod.POST)
    public String cancelBom(String ids) throws Exception {
        String id[] = ids.split(",");
        for (int a = 0; a < id.length; a++) {
            FtcBcBom ftcbcBom = ftcBcBomService.findById(FtcBcBom.class, Long.parseLong(id[a]));
            ftcbcBom.setPackCanceled(-1);
            this.ftcBcBomService.update(ftcbcBom);
        }
        return Constant.AJAX_SUCCESS;
    }
}
/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.baseInfo.controller;

import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.service.ITcBomService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.*;

/**
 * 套材BOMController
 *
 * @author 肖文彬
 * @Date 2016-10-9 9:19:51
 */
@Controller
@RequestMapping("/bom/tc")
@Journal(name = "套材BOM")
public class TcBomController extends BaseController {
    /**
     * 套材BOM页面
     */
    final String index = "baseInfo/tcBom/tcBom";
    final String addOrEdit = "baseInfo/tcBom/tcBomAddOrEdit";
    final String tcBomAudit = "baseInfo/tcBom/tcBomAudit";
    final String selectProduct = "baseInfo/tcBom/selectProduct";
    final String selectConsumer = "baseInfo/tcBom/selectConsumer";
    final String product = "baseInfo/tcBom/product";
    @Resource
    ITcBomService tcBomService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取套材BOM列表信息")
    @RequestMapping("list")
    public String getTcBom(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(tcBomService.findPageInfo(filter, page));
    }

    @Journal(name = "添加套材BOM页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(TcBom tcBom) {
        String filePath = UUID.randomUUID().toString();
        return new ModelAndView(addOrEdit, model.addAttribute("tcBom", tcBom).addAttribute("filePath", filePath));
    }

    @ResponseBody
    @Journal(name = "保存套材BOM", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(TcBom tcBom, TcBomVersion tcBomVersion, Long fileId) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tcProcBomCode", tcBom.getTcProcBomCode());
        if (!tcBomService.isExist(TcBom.class, map)) {
            ExcelImportMessage eim = tcBomService.doAddTcBom(tcBom, tcBomVersion, fileId);
            tcBomService.savePdfFile(tcBom, tcBomVersion, fileId, eim);
            if (eim != null && eim.hasError()) {
                Map<String, String> excelErrorMsg = new HashMap<>();
                excelErrorMsg.put("excelErrorMsg", eim.getMessage());
                return GsonTools.toJson(excelErrorMsg);
            }
        } else {
            return ajaxError("BOM代码重复！");
        }
        return GsonTools.toJson(tcBom);
    }

    @Journal(name = "编辑套材BOM页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(String id) {
        TcBom tcBom = tcBomService.findById(TcBom.class, Long.valueOf(id));
        Consumer consumer = tcBomService.findById(Consumer.class, Long.valueOf(tcBom.getTcProcBomConsumerId()));
        return new ModelAndView(addOrEdit, model.addAttribute("tcBom", tcBom).addAttribute("consumer", consumer));
    }

    @ResponseBody
    @Journal(name = "编辑套材BOM", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(TcBom tcBom) throws Exception {
        TcBom _tcBom = tcBomService.findById(TcBom.class, tcBom.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("TCPROCBOMCODE", tcBom.getTcProcBomCode());
        // 判断产品编码是否唯一性
        if (!_tcBom.getTcProcBomCode().equals(tcBom.getTcProcBomCode()) && tcBomService.isExist(TcBom.class, map)) {
            return ajaxError("工艺Bom编码重复");
        }
        Map<String, Object> map1 = new HashMap<>();
        map1.put("tcProcBomId", tcBom.getId());
        List<TcBomVersion> list = tcBomService.findListByMap(TcBomVersion.class, map1);
        for (TcBomVersion tv : list) {
            if (tv.getAuditState() > 0) {
                return ajaxError("含有审核中或已通过的版本,不能修改");
            }
        }
        tcBomService.update2(tcBom);
        return GsonTools.toJson(tcBom);
    }

    @ResponseBody
    @Journal(name = "删除套材BOM", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        String[] idsArray = ids.split(",");
        for (String idString : idsArray) {
            Long id = Long.parseLong(idString);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("tcProcBomId", id);
            List<TcBomVersion> fbv = tcBomService.findListByMap(TcBomVersion.class, map);
            if (fbv != null && !fbv.isEmpty()) {
                return ajaxError("请删除版本后删除工艺");
            }
        }
        tcBomService.deleteB(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "查询常规套材BOM树")
    @RequestMapping(value = "findBom", method = RequestMethod.GET)
    public String bom(String status, String id, String vId, String data, String state, Integer isNeedTop) throws SQLTemplateException {
        if (data == null) {
            data = "";
        }
        if (status != null) {
            if (status.indexOf(",") > 0) {
                String[] st = status.split(",");
                for (int i = 0; i < st.length; i++) {
                    status = st[st.length - 1];
                }
            }
        }

        JSONArray array = new JSONArray();
        String result = "";
        if (status == null) {
            JSONObject json = new JSONObject();
            json.put("text", "套材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("status", "0").put("vId", "0"));
            array.put(json);
            result = array.toString();
        } else if (status.equals("0") && !data.equals("")) {
            List<Map<String, Object>> list = tcBomService.findBom(data, state);
            if (!list.isEmpty()) {
                JSONObject json = new JSONObject();
                json.put("text", "套材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("status", "0").put("vId", "0"));
                json.put("children", tcBomService.findBom(data, state));
                array.put(json);
                result = array.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("text", "套材bom");
                json.put("state", "closed");
                array.put(json);
                result = array.toString();
            }
        } else if (status.equals("1")) {
            result = GsonTools.toJson(tcBomService.findV(id));
        } else if (status.equals("2")) {
            result = GsonTools.toJson(tcBomService.findP(id));
        } else if (status.equals("3")) {
            result = GsonTools.toJson(tcBomService.findPC(id, vId));
        } else {
            result = GsonTools.toJson(tcBomService.findBom(data, state));
        }
        return result;
    }

    @ResponseBody
    @Journal(name = "查询变更试样套材BOM树")
    @RequestMapping(value = "listBomTest", method = RequestMethod.GET)
    public String listBomTest(String status, String id, String vId, String data, String state) throws SQLTemplateException {
        if (data == null) {
            data = "";
        }
        if (status != null) {
            if (status.indexOf(",") > 0) {
                String[] st = status.split(",");
                for (int i = 0; i < st.length; i++) {
                    status = st[st.length - 1];
                }
            }
        }
        JSONArray array = new JSONArray();
        String result = "";
        if (status == null) {
            JSONObject json = new JSONObject();
            json.put("text", "套材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("status", "0").put("vId", "0"));
            array.put(json);
            result = array.toString();
        } else if (status.equals("0") && !data.equals("")) {
            List<Map<String, Object>> list = tcBomService.getTcBomJsonTest(data, state);
            if (!list.isEmpty()) {
                JSONObject json = new JSONObject();
                json.put("text", "套材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("status", "0").put("vId", "0"));
                json.put("children", tcBomService.getTcBomJsonTest(data, state));
                array.put(json);
                result = array.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("text", "套材bom");
                json.put("state", "closed");
                array.put(json);
                result = array.toString();
            }
        } else if (status.equals("1")) {
            result = GsonTools.toJson(tcBomService.findV(id));
        } else if (status.equals("2")) {
            result = GsonTools.toJson(tcBomService.findP(id));
        } else if (status.equals("3")) {
            result = GsonTools.toJson(tcBomService.findPC(id, vId));
        } else {
            result = GsonTools.toJson(tcBomService.getTcBomJsonTest(data, state));
        }
        return result;
    }

    @ResponseBody
    @Journal(name = "查讯新品试样套材BOM树")
    @RequestMapping(value = "listBomTest1", method = RequestMethod.GET)
    public String listBomTest1(String status, String id, String vId, String data, String state) throws SQLTemplateException {
        if (data == null) {
            data = "";
        }
        if (status != null) {
            if (status.indexOf(",") > 0) {
                String[] st = status.split(",");
                for (int i = 0; i < st.length; i++) {
                    status = st[st.length - 1];
                }
            }
        }
        JSONArray jarray = new JSONArray();
        String result = "";
        if (status == null) {
            JSONObject json = new JSONObject();
            json.put("text", "套材bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("status", "0").put("vId", "0"));
            jarray.put(json);
            result = jarray.toString();
        } else if (status.equals("0") && !data.equals("")) {
            List<Map<String, Object>> list = tcBomService.getTcBomJsonTest1(data, state);
            if (!list.isEmpty()) {
                JSONObject json = new JSONObject();
                json.put("text", "套材bom");
                json.put("state", "closed");
                JSONObject j = new JSONObject();
                json.put("attributes", j.put("status", "0").put("vId", "0"));
                json.put("children", tcBomService.getTcBomJsonTest1(data, state));
                jarray.put(json);
                result = jarray.toString();
            } else {
                JSONObject json = new JSONObject();
                json.put("text", "套材bom");
                json.put("state", "closed");
                jarray.put(json);
                result = jarray.toString();
            }
        } else if (status.equals("1")) {
            result = GsonTools.toJson(tcBomService.findV(id));
        } else if (status.equals("2")) {
            result = GsonTools.toJson(tcBomService.findP(id));
        } else if (status.equals("3")) {
            result = GsonTools.toJson(tcBomService.findPC(id, vId));
        } else {
            result = GsonTools.toJson(tcBomService.getTcBomJsonTest1(data, state));
        }
        return result;
    }

    @ResponseBody
    @Journal(name = "保存套材BOM部件", logType = LogType.DB)
    @RequestMapping(value = "saveParts", method = RequestMethod.POST)
    public String saveParts(@RequestBody TcBomVersionParts detail) {
        TcBomVersion tcBomVersion = tcBomService.findById(TcBomVersion.class, detail.getTcProcBomVersoinId());
        if (tcBomVersion.getAuditState() > 0) {
            return ajaxError("不能修改审核中或已通过的数据");
        }
        detail.setIsDeleted(0);
        if (detail.getId() == null) {
            tcBomService.save(detail);
        } else {
            tcBomService.update(detail);
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "保存套材BOM部件明细", logType = LogType.DB)
    @RequestMapping(value = "saveDetail", method = RequestMethod.POST)
    public String saveDetail(@RequestBody TcBomVersionPartsDetail detail) {
        TcBomVersionParts tcBomVersionParts = tcBomService.findById(TcBomVersionParts.class, detail.getTcProcBomPartsId());
        TcBomVersion tcBomVersion = tcBomService.findById(TcBomVersion.class, tcBomVersionParts.getTcProcBomVersoinId());
        if (tcBomVersion.getAuditState() > 0) {
            return ajaxError("不能修改审核中或已通过的数据");
        }
        if (detail.getId() == null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("tcFinishedProductId", detail.getTcFinishedProductId());
            map.put("tcProcBomPartsId", detail.getTcProcBomPartsId());
            List<TcBomVersionPartsDetail> list = tcBomService.findListByMap(TcBomVersionPartsDetail.class, map);
            tcBomService.save(detail);
        } else {
            tcBomService.update(detail);
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "保存套材BOM部件成品重量胚布信息", logType = LogType.DB)
    @RequestMapping(value = "saveFinishedWeightEmbryoCloth", method = RequestMethod.POST)
    public String saveFinishedWeightEmbryoCloth(@RequestBody TcBomVersionPartsFinishedWeightEmbryoCloth finishedWeightEmbryoCloth) throws Exception {
        TcBomVersionParts tcBomVersionParts = tcBomService.findById(TcBomVersionParts.class, finishedWeightEmbryoCloth.getTcProcBomPartsId());
        TcBomVersion tcBomVersion = tcBomService.findById(TcBomVersion.class, tcBomVersionParts.getTcProcBomVersoinId());
        if (tcBomVersion.getAuditState() > 0) {
            return ajaxError("不能修改审核中或已通过的数据");
        }
        if (finishedWeightEmbryoCloth.getId() == null) {
            tcBomService.save(finishedWeightEmbryoCloth);
        } else {
            tcBomService.update(finishedWeightEmbryoCloth);
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "删除套材Bom版本", logType = LogType.DB)
    @RequestMapping(value = "deleteV", method = RequestMethod.POST)
    public String deleteV(String ids) {
        String[] idsarray = ids.split(",");
        for (String idString : idsarray) {
            TcBomVersion tv = tcBomService.findById(TcBomVersion.class, Long.parseLong(idString));
            if (tv.getAuditState() > 0) {
                return ajaxError("不能删除审核中和已通过的版本");
            }
            HashMap<String, Object> map = new HashMap();
            map.put("procBomId", tv.getId());
            map.put("productIsTc", 1);
            List<FinishedProduct> fp = tcBomService.findListByMap(FinishedProduct.class, map);
            if (fp != null && !fp.isEmpty()) {
                return ajaxError("该版本被产品使用，请修改相关产品工艺后删除版本");
            }
        }
        tcBomService.deleteV(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "删除套材BOM部件", logType = LogType.DB)
    @RequestMapping(value = "deleteDetail", method = RequestMethod.POST)
    public String editDetail(String ids) {
        if (!ids.isEmpty()) {
            String id[] = ids.split(",");
            TcBomVersionParts tcBomVersionDetail = tcBomService.findById(TcBomVersionParts.class, Long.parseLong(id[0]));
            TcBomVersion tcBomVersion = tcBomService.findById(TcBomVersion.class, tcBomVersionDetail.getTcProcBomVersoinId());
            if (tcBomVersion.getAuditState() > 0) {
                return ajaxError("不能修改审核中或已通过的数据");
            }
        }
        tcBomService.deleteP(ids);
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "检查部件是否可删除", logType = LogType.DB)
    @RequestMapping(value = "delValid", method = RequestMethod.POST)
    public String delValid(String ids) {
        String[] _ids = ids.split(",");
        Map<String, Object> map = new HashMap<>();
        Long[] idArray = new Long[_ids.length];
        int i = 0;
        for (String id : _ids) {
            idArray[i++] = Long.parseLong(id);
        }
        map.put("partId", idArray);
        List<SalesOrderDetailPartsCount> list = tcBomService.findListByMap(SalesOrderDetailPartsCount.class, map);
        if (ListUtils.isEmpty(list)) {
            return ajaxSuccess();
        }

        Set<String> orderCodes = new HashSet<>();
        for (SalesOrderDetailPartsCount part : list) {
            if (part.getPartCount() == 0) continue;
            SalesOrderDetail sod = tcBomService.findById(SalesOrderDetail.class, part.getSalesOrderDetailId());
            if (sod == null) continue;
            orderCodes.add(sod.getSalesOrderSubCode());
        }
        return ajaxSuccess(orderCodes);
    }

    @ResponseBody
    @Journal(name = "复制版本节点")
    @RequestMapping("copytcBomVersion")
    public String toComplite(String ids, String name) throws Exception {
        TcBomVersion tcBomVersion = tcBomService.findById(TcBomVersion.class, Long.valueOf(ids));
        HashMap<String, Object> map = new HashMap<>();
        map.put("tcProcBomId", tcBomVersion.getTcProcBomId());
        List<TcBomVersion> li = tcBomService.findListByMap(TcBomVersion.class, map);
        for (TcBomVersion bv : li) {
            if (bv.getTcProcBomVersionCode().equals(name)) {
                return ajaxError("已经有相同名字的版本");
            }
        }
        tcBomService.toCopy(ids);
        return ajaxSuccess();
    }

    @ResponseBody
    @Journal(name = "复制版本节点")
    @RequestMapping("copytcBom")
    public String toCompliteBom(String id) throws Exception {
        TcBom tb = tcBomService.findById(TcBom.class, Long.parseLong(id));
        HashMap<String, Object> map = new HashMap<>();
        map.put("tcProcBomName", tb.getTcProcBomName() + "(复制)");
        if (tcBomService.isExist(TcBom.class, map, true)) {
            return ajaxError("已有同名bom");
        }
        tcBomService.toCopyBom(id);
        return ajaxSuccess();
    }

    @Journal(name = "查询版本信息")
    @RequestMapping(value = "find", method = RequestMethod.GET)
    public ModelAndView _add(String id) {
        TcBomVersion tcBomVersion = tcBomService.findById(TcBomVersion.class, Long.valueOf(id));
        return new ModelAndView(tcBomAudit, model.addAttribute("tcBomVersion", tcBomVersion));
    }

    @Journal(name = "添加套材BOM页面")
    @RequestMapping(value = "findParts", method = RequestMethod.GET)
    @ResponseBody
    public String find(String id) {
        return GsonTools.toJson(tcBomService.find1(id));

    }

    @Journal(name = "选择成品页面")
    @RequestMapping(value = "addFtcBom", method = RequestMethod.GET)
    public ModelAndView addFtcBom() {
        return new ModelAndView(selectProduct);
    }

//	@Journal(name = "加载ftc成品信息")
//	@RequestMapping(value = "findFtc", method = RequestMethod.POST)
//	@ResponseBody
//	public String findFtc(String id) {
//		List<Map<String, Object>> list = tcBomService.findConsumer(id);
//		return GsonTools.toJson(tcBomService.findFtc(MapUtils.getAsString(list.get(0), "TCPROCBOMCONSUMERID")));
//
//	}

    @Journal(name = "加载ftc成品信息")
    @RequestMapping(value = "findFtc", method = RequestMethod.POST)
    @ResponseBody
    public String findFtc(String id, Filter filter, Page page) {
        List<Map<String, Object>> list;
        if (null == id) {
            list = tcBomService.findConsumer(filter.get("id"));
        } else {
            list = tcBomService.findConsumer(id);
        }

        if (!list.isEmpty()) {
            filter.set("id", list.get(0).get("ID").toString());
            return GsonTools.toJson(tcBomService.findFtc(filter, page));
        }
        return null;
    }

    @Journal(name = "添加客户信息页面")
    @RequestMapping(value = "addConsumer", method = RequestMethod.GET)
    public ModelAndView _add() {
        return new ModelAndView(selectConsumer);
    }

    @Journal(name = "产品增加页面")
    @RequestMapping(value = "findProduct", method = RequestMethod.GET)
    public ModelAndView findProduct() {
        return new ModelAndView(product);
    }

    @ResponseBody
    @Journal(name = "加载客户信息")
    @RequestMapping(value = "findConsumer", method = RequestMethod.POST)
    public String add(String id) throws Exception {
        return GsonTools.toJson(tcBomService.findConsumer(id));
    }

    @ResponseBody
    @Journal(name = "查询BOM信息")
    @RequestMapping(value = "findB", method = RequestMethod.POST)
    public String findB(String id) {
        TcBomVersion tc = tcBomService.findById(TcBomVersion.class, Long.valueOf(id));
        TcBom tcBom = tcBomService.findById(TcBom.class, tc.getTcProcBomId());
        return GsonTools.toJson(tcBom);
    }

    @ResponseBody
    @Journal(name = "查看此部件是否有明细")
    @RequestMapping(value = "findDetial", method = RequestMethod.POST)
    public String findDetial(String id, Integer status) {
        if (status == 2) {
            return "";
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("tcProcBomPartsId", Long.valueOf(id));
        List<TcBomVersionPartsDetail> tc = tcBomService.findListByMap(TcBomVersionPartsDetail.class, map);
        if (tc.isEmpty()) {
            return "";
        }
        TcBomVersionParts tcBomVersionParts = tcBomService.findById(TcBomVersionParts.class, Long.valueOf(id));
        TcBomVersion tcBomVersion = tcBomService.findById(TcBomVersion.class, tcBomVersionParts.getTcProcBomVersoinId());
        if (!tc.isEmpty()) {
            return GsonTools.toJson(0);
        } else if (tcBomVersion.getAuditState() > 0) {
            return GsonTools.toJson(1);
        } else {
            return GsonTools.toJson(2);
        }
    }

    @ResponseBody
    @Journal(name = "部件增加的时候查看审核流程")
    @RequestMapping(value = "findA", method = RequestMethod.POST)
    public String findA(String id) {
        TcBomVersionParts tcBomVersionParts = tcBomService.findById(TcBomVersionParts.class, Long.valueOf(id));
        TcBomVersion tcBomVersion = tcBomService.findById(TcBomVersion.class, tcBomVersionParts.getTcProcBomVersoinId());
        if (tcBomVersion.getAuditState() > 0) {
            return GsonTools.toJson(0);
        } else {
            return GsonTools.toJson(1);
        }
    }

    @ResponseBody
    @Journal(name = "查看BOM下的版本")
    @RequestMapping(value = "findV", method = RequestMethod.POST)
    public String findV(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tcProcBomId", Long.valueOf(id));
        List<TcBomVersion> list = tcBomService.findListByMap(TcBomVersion.class, map);
        if (list.isEmpty()) {
            return GsonTools.toJson(0);
        } else {
            return GsonTools.toJson(1);
        }
    }
}
/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.cut.cutTcBom.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomMain;
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomPartMain;
import com.bluebirdme.mes.cut.cutTcBom.service.ICutTcBomMainService;
import com.bluebirdme.mes.cut.cutTcBom.service.ICutTcBomPartMainService;
import com.bluebirdme.mes.utils.HttpUtils;
import com.bluebirdme.mes.utils.State;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 派工单裁剪图纸bomController
 *
 * @author 季晓龙
 * @Date 2020-09-2 9:30:06
 */
@Controller
@RequestMapping("bom/cutTcBom")
@Journal(name = "裁剪图纸bom")
public class CutTcBomMainController extends BaseController {

    // 裁剪图纸bom
    final String index = "cut/cutTcBom/cutTcBomMain";

    //新增修改页面
    final String addOrEdit = "cut/cutTcBom/cutTcBomMainAddOrEdit";

    final String cutTcBomUpload = "cut/cutTcBom/cutTcBomMainUpload";

    final String cutTcBomPartMainUpload = "cut/cutTcBom/cutTcBomPartMainUpload";


    @Resource
    ICutTcBomMainService cutTcBomMainService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取裁剪图纸套材bom信息")
    @RequestMapping("list")
    public String getCutTcBomMain(Filter filter, Page page) throws Exception {

        Map<String, Object> pageInfo = cutTcBomMainService.findPageInfo(filter, page);

        return GsonTools.toJson(pageInfo);
    }

    @ResponseBody
    @Journal(name = "获取裁剪bom列表信息")
    @RequestMapping("listBomTree")
    public String getlistBomTree(String nodeType, String id, String data) throws Exception {
        JSONArray jarray = new JSONArray();
        if (data == null) {
            data = "";
        }

        String result = "";

        if (nodeType == (null)) {
            JSONObject json = new JSONObject();
            json.put("id", "root");
            json.put("text", "裁剪图纸bom");
            json.put("state", "closed");
            JSONObject j = new JSONObject();
            json.put("attributes", j.put("nodeType", "root"));
            jarray.put(json);
            result = jarray.toString();
        } else if (nodeType.equals("root")) {
            List<Map<String, Object>> list = cutTcBomMainService.getCutBomJson(data);
            result = GsonTools.toJson(list);
        }

        return result;
    }


    @ResponseBody
    @Journal(name = "根据主表Id查询裁剪图纸套材bom信息")
    @RequestMapping(value = "findCutTcBomDetailByMainId", method = RequestMethod.POST)
    public String findCutTcBomDetailByMainId(Long mainId) throws Exception {

        return GsonTools.toJson(cutTcBomMainService.findCutTcBomDetailByMainId(mainId));
    }


    @Journal(name = "添加加载裁剪图纸信息")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(CutTcBomMain cutTcBomMain) {
        return new ModelAndView(addOrEdit, model.addAttribute("cutTcBomMain", cutTcBomMain));
    }


    @Journal(name = "加载裁剪图纸信息")
    @RequestMapping(value = "cutTcBomMainAddOrEditPage", method = RequestMethod.GET)
    public ModelAndView cutTcBomMainAddOrEditPage(CutTcBomMain cutTcBomMain) throws Exception {
        if (cutTcBomMain.getId() != null) {
            cutTcBomMain = cutTcBomMainService.findById(CutTcBomMain.class, cutTcBomMain.getId());
        }
        return new ModelAndView(addOrEdit, model.addAttribute("cutTcBomMain", cutTcBomMain));
    }


    @ResponseBody
    @Journal(name = "保存裁剪图纸信息", logType = LogType.DB)
    @RequestMapping(value = "saveCutTcBomMain", method = RequestMethod.POST)
    @Valid
    public String saveCutTcBomMain(@RequestBody CutTcBomMain cutTcBomMain) throws Exception {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        String result = cutTcBomMainService.saveCutTcBomMain(cutTcBomMain, userId);
        return GsonTools.toJson(result);
    }

    @ResponseBody
    @Journal(name = "作废裁剪图纸bom", logType = LogType.DB)
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public String cancel(String ids) throws Exception {
        String ids_temp[] = ids.split(",");
        Serializable ids_target[] = new Serializable[ids_temp.length];
        for (int i = 0; i < ids_temp.length; i++) {
            CutTcBomMain cutTcBomMain = cutTcBomMainService.findById(CutTcBomMain.class, Long.parseLong(ids_temp[i]));
            cutTcBomMain.setState(State.Cancel);
            cutTcBomMainService.update(cutTcBomMain);
        }
        return Constant.AJAX_SUCCESS;
    }

    @ResponseBody
    @Journal(name = "恢复裁剪图纸bom", logType = LogType.DB)
    @RequestMapping(value = "effect", method = RequestMethod.POST)
    public String effect(String ids) throws Exception {
        String ids_temp[] = ids.split(",");
        Serializable ids_target[] = new Serializable[ids_temp.length];
        for (int i = 0; i < ids_temp.length; i++) {
            CutTcBomMain cutTcBomMain = cutTcBomMainService.findById(CutTcBomMain.class, Long.parseLong(ids_temp[i]));
            cutTcBomMain.setState(State.Effect);
            cutTcBomMainService.update(cutTcBomMain);
        }
        return Constant.AJAX_SUCCESS;
    }

    @Journal(name = "上传套材Bom版本页面")
    @RequestMapping(value = "cutTcBomMainUpload", method = RequestMethod.GET)
    public ModelAndView cutTcBomMainUpload() {
        return new ModelAndView(cutTcBomUpload);
    }


    @ResponseBody
    @Journal(name = "导入裁剪图纸bom", logType = LogType.DB)
    @RequestMapping(value = "importcutTcBomMainUploadFile")
    public String importcutTcBomMainUploadFile(@RequestParam(value = "file") MultipartFile file) throws Exception {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        String result = cutTcBomMainService.importCutTcBomMainUploadFile(file, userId);
        return GsonTools.toJson(result);
    }

    /**
     * 根据裁剪套才bomid导出裁剪套才Excel
     *
     * @param id
     * @throws Exception
     */
    @NoLogin
    @Journal(name = "根据裁剪套才bomid导出裁剪套才Excel")
    @ResponseBody
    @RequestMapping(value = "exportcutTcBomMain", method = RequestMethod.GET)
    public void exportcutTcBomMain(Long id) throws Exception {
        SXSSFWorkbook wb = cutTcBomMainService.exportcutTcBomMain(id);
        CutTcBomMain cutTcBomMain = cutTcBomMainService.findById(CutTcBomMain.class, id);
        HttpUtils.download(response, wb, cutTcBomMain.getTcProcBomCodeVersion() + "裁剪套才Excel");
    }

    @Journal(name = "上传裁片Bom页面")
    @RequestMapping(value = "cutTcBomPartMainUpload", method = RequestMethod.GET)
    public ModelAndView cutTcBomPartMainUpload() {
        return new ModelAndView(cutTcBomPartMainUpload);
    }


    @ResponseBody
    @Journal(name = "根据bom主表Id查询裁片bom信息")
    @RequestMapping(value = "findCutTcBomPartMainByBomMainId", method = RequestMethod.POST)
    public String findCutTcBomPartMainByBomMainId(Long mainId) {
        return GsonTools.toJson(cutTcBomMainService.findCutTcBomDetailByMainId(mainId));
    }

}

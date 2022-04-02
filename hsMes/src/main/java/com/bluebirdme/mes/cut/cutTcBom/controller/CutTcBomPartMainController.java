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
import com.bluebirdme.mes.cut.cutTcBom.entity.CutTcBomPartDetail;
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
import java.util.List;
import java.util.Map;

/**
 * 派工单裁剪图纸bomController
 *
 * @author 季晓龙
 * @Date 2020-09-2 9:30:06
 */
@Controller
@RequestMapping("cut/cutTcBomPartMain")
@Journal(name = "派工单裁片bom")
public class CutTcBomPartMainController extends BaseController {

    final String cutTcBomPartMainUpload = "cut/cutTcBom/cutTcBomPartMainUpload";

    @Resource
    ICutTcBomPartMainService cutTcBomPartMainService;

    @Journal(name = "上传裁片Bom页面")
    @RequestMapping(value = "cutTcBomPartMainUpload", method = RequestMethod.GET)
    public ModelAndView cutTcBomPartMainUpload() {
        return new ModelAndView(cutTcBomPartMainUpload);
    }

    @ResponseBody
    @Journal(name = "导入裁片bom", logType = LogType.DB)
    @RequestMapping(value = "importCutTcBomPartMainUploadFile")
    public String importCutTcBomPartMainUploadFile(@RequestParam(value = "file") MultipartFile file) throws Exception {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        String result = cutTcBomPartMainService.importCutTcBomPartMainUploadFile(file, userId);
        return GsonTools.toJson(result);
    }


    @ResponseBody
    @Journal(name = "根据主表Id查询裁片bom主表信息")
    @RequestMapping(value = "findCutTcBomPartMainByTcBomMainId", method = RequestMethod.POST)
    public String findCutTcBomPartMainByTcBomMainId(Long tcBomMainId) {

        return GsonTools.toJson(cutTcBomPartMainService.findCutTcBomPartMainByTcBomMainId(tcBomMainId));
    }

    @ResponseBody
    @Journal(name = "根据主表Id查询裁片bom从表信息")
    @RequestMapping(value = "findCutTcBomPartDetailByMainId", method = RequestMethod.POST)
    public String findCutTcBomPartDetailByMainId(Long mainId) {

        return GsonTools.toJson(cutTcBomPartMainService.findCutTcBomPartDetailByMainId(mainId));
    }

    @ResponseBody
    @Journal(name = "保存裁片信息", logType = LogType.DB)
    @RequestMapping(value = "saveCutTcBomPartMain", method = RequestMethod.POST)
    @Valid
    public String saveCutTcBomPartMain(@RequestBody CutTcBomPartMain cutTcBomPartMain) throws Exception {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        String result = cutTcBomPartMainService.saveCutTcBomPartMain(cutTcBomPartMain, userId);
        return GsonTools.toJson(result);
    }

    @ResponseBody
    @Journal(name = "保存裁片明细信息", logType = LogType.DB)
    @RequestMapping(value = "saveCutTcBomPartDetail", method = RequestMethod.POST)
    @Valid
    public String saveCutTcBomPartDetail(@RequestBody CutTcBomPartDetail cutTcBomPartDetail) throws Exception {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        String result = cutTcBomPartMainService.saveCutTcBomPartDetail(cutTcBomPartDetail, userId);
        return GsonTools.toJson(result);
    }

    @ResponseBody
    @Journal(name = "删除裁片图纸bom", logType = LogType.DB)
    @RequestMapping(value = "doDeletePartMain", method = RequestMethod.POST)
    public String doDeletePartMain(String ids) throws Exception {
        String result = cutTcBomPartMainService.doDeletePartMain(ids);
        return GsonTools.toJson(result);
    }

    @ResponseBody
    @Journal(name = "删除裁片明细bom", logType = LogType.DB)
    @RequestMapping(value = "doDeletePartDetail", method = RequestMethod.POST)
    public String doDeletePartDetail(String ids) throws Exception {
        String result = cutTcBomPartMainService.doDeletePartDetail(ids);
        return GsonTools.toJson(result);
    }

    /**
     * 根据裁剪套才bomid导出裁片Excel
     *
     * @param tcBomMainId
     * @throws Exception
     */
    @NoLogin
    @Journal(name = "根据裁剪套才bomId导出裁剪套才Excel")
    @ResponseBody
    @RequestMapping(value = "exportCutTcBomPart", method = RequestMethod.GET)
    public void exportCutTcBomPart(Long tcBomMainId) throws Exception {
        SXSSFWorkbook wb = cutTcBomPartMainService.exportCutTcBomPart(tcBomMainId);
        CutTcBomMain cutTcBomMain = cutTcBomPartMainService.findById(CutTcBomMain.class, tcBomMainId);
        HttpUtils.download(response, wb, cutTcBomMain.getTcProcBomCodeVersion() + "裁片Excel");
    }

}

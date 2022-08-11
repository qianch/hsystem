package com.bluebirdme.mes.siemens.bom.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.excel.ExcelImportMessage;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.siemens.bom.service.IFragmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 西门子BOM
 *
 * @author Goofy
 * @Date 2017年7月18日 下午2:21:14
 */
@Controller
@RequestMapping("/siemens/bom")
public class SiemensBomViewController extends BaseController {
    final String PAGE_FRAGMENT = "siemens/bom/fragment";
    final String PAGE_DRAWINGS = "siemens/bom/drawings";

    /**
     * 图纸BOM导入页面
     */
    final String PAGE_DRAWINGSIMPORT = "siemens/bom/drawingsImport";
    final String PAGE_SUIT = "siemens/bom/suit";

    /**
     * 裁片导入页面
     */
    final String PAGE_IMPORT = "siemens/bom/fragmentImport";

    @Resource
    IFragmentService fragmentService;

    @Journal(name = "裁片管理")
    @RequestMapping("fragment")
    public String fragment() {
        return PAGE_FRAGMENT;
    }

    @Journal(name = "图纸BOM")
    @RequestMapping("drawings")
    public String drawings() {
        return PAGE_DRAWINGS;
    }

    @Journal(name = "组套BOM")
    @RequestMapping("suit")
    public String suit() {
        return PAGE_SUIT;
    }

    @Journal(name = "裁片导入页面")
    @RequestMapping(value = "import", method = RequestMethod.GET)
    public ModelAndView _fragmentImport() {
        String filePath = UUID.randomUUID().toString();
        return new ModelAndView(PAGE_IMPORT, model.addAttribute("filePath", filePath));
    }

    @ResponseBody
    @Journal(name = "裁片导入", logType = LogType.DB)
    @RequestMapping(value = "import", method = RequestMethod.POST)
    @Valid
    public String fragmentImport(Long fileId, Long tcBomId) throws Exception {
        ExcelImportMessage eim = fragmentService.fragmentImport(fileId, tcBomId);
        if (eim.hasError()) {
            Map<String, String> excelErrorMsg = new HashMap<>();
            excelErrorMsg.put("excelErrorMsg", eim.getMessage());
            return GsonTools.toJson(excelErrorMsg);
        }
        return ajaxSuccess();
    }


    @Journal(name = "图纸BOM导入页面")
    @RequestMapping(value = "drawingsImport", method = RequestMethod.GET)
    public ModelAndView _drawingsImport() {
        String filePath = UUID.randomUUID().toString();
        return new ModelAndView(PAGE_DRAWINGSIMPORT, model.addAttribute("filePath", filePath));
    }

    @ResponseBody
    @Journal(name = "图纸BOM导入", logType = LogType.DB)
    @RequestMapping(value = "drawingsImport", method = RequestMethod.POST)
    @Valid
    public String drawingsImport(Long fileId, Long tcBomId, String partId) throws Exception {
        ExcelImportMessage eim = fragmentService.drawingsImport(fileId, tcBomId, partId);
        if (eim.hasError()) {
            Map<String, String> excelErrorMsg = new HashMap<>();
            excelErrorMsg.put("excelErrorMsg", eim.getMessage());
            return GsonTools.toJson(excelErrorMsg);
        }
        return ajaxSuccess();
    }
}

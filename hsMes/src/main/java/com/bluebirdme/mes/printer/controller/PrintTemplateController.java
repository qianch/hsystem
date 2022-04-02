package com.bluebirdme.mes.printer.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.core.valid.annotations.Valid;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.printer.entity.PrintTemplate;
import com.bluebirdme.mes.printer.entity.Printer;
import com.bluebirdme.mes.printer.service.IPrintTemplateService;
import com.bluebirdme.mes.printer.service.IPrinterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.*;

/**
 * 打印条码
 *
 * @author Goofy
 * @Date 2017年3月1日 下午1:50:22
 */
@Controller
@RequestMapping("/printTemplate")
public class PrintTemplateController extends BaseController {
    private final String printTemplateUrl = "printer/printTemplate/printTemplate";
    private final String addOrEditUrl = "printer/printTemplate/printTemplateAddOrEdit";

    @Resource
    IPrintTemplateService printTemplateService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return printTemplateUrl;
    }


    @Journal(name = "添加打印模版管理页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(PrintTemplate printTemplate) {
        return new ModelAndView(addOrEditUrl, model.addAttribute("printTemplate", printTemplate));
    }

    @ResponseBody
    @Journal(name = "保存打印模版管理", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(PrintTemplate printTemplate) throws Exception {
        Map<String, Object> param = new HashMap();
        param.put("printAttribute", printTemplate.getPrintAttribute());
        if (printTemplateService.has(PrintTemplate.class, param, printTemplate.getId())) {
            return GsonTools.toJson("重复属性不能保存");
        }

        printTemplateService.save(printTemplate);
        return GsonTools.toJson("保存成功");
    }

    @Journal(name = "编辑打印模版管理页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(PrintTemplate printTemplate) {

        printTemplate = printTemplateService.findById(PrintTemplate.class, printTemplate.getId());
        return new ModelAndView(addOrEditUrl, model.addAttribute("printTemplate", printTemplate));
    }

    @ResponseBody
    @Journal(name = "编辑打印模版管理", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(PrintTemplate printTemplate) throws Exception {
        Map<String, Object> param = new HashMap();
        param.put("printAttribute", printTemplate.getPrintAttribute());
        if (printTemplateService.has(PrintTemplate.class, param, printTemplate.getId())) {
            return GsonTools.toJson("重复属性不能保存");
        }
        printTemplateService.update(printTemplate);
        return GsonTools.toJson("保存成功");
    }

    @ResponseBody
    @Journal(name = "删除打印模版管理", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        printTemplateService.delete(PrintTemplate.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取打印模版列表信息")
    @RequestMapping(value = "printTemplate", method = RequestMethod.GET)
    public ModelAndView _list() {
        return new ModelAndView(printTemplateUrl);
    }


    @NoAuth
    @ResponseBody
    @Journal(name = "获取打印模版列表信息")
    @RequestMapping("printTemplateList")
    public String printTemplateList(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(printTemplateService.findPageInfo(filter, page));
    }


}

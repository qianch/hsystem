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
import com.bluebirdme.mes.printer.entity.Printer;
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
@RequestMapping("/print")
public class PrintController extends BaseController {
    private final String PRINT_PAGE = "printer/print";
    private final String printer = "printer/print/printer";
    private final String addOrEdit = "printer/print/printerAddOrEdit";

    @Resource
    IPrinterService printerService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return PRINT_PAGE;
    }

    @ResponseBody
    @Journal(name = "获取打印机名字的select选项")
    @RequestMapping(value = "printers", method = RequestMethod.GET)
    public String getPrinterInfo2() {
        List<Printer> ps = printerService.findAll(Printer.class);
        List<Map<String, Object>> list = new ArrayList<>();
        Set<Long> depts = new TreeSet<>();
        for (Printer p : ps) {
            depts.add(p.getDepartmentId());
        }
        List<Map<String, Object>> children;
        Map<String, Object> node;
        Map<String, Object> subnode;
        Department dept;
        for (Long did : depts) {
            node = new HashMap<>();
            dept = printerService.findById(Department.class, did);
            node.put("id", did);
            node.put("text", dept.getName());
            node.put("iconCls", "platform-home");
            children = new ArrayList<>();
            for (Printer p : ps) {
                if (p.getDepartmentId().longValue() != did.intValue()) continue;
                subnode = new HashMap<>();
                subnode.put("id", p.getPrinterName());
                subnode.put("text", p.getPrinterTxtName());
                subnode.put("iconCls", "icon-print");
                children.add(subnode);
            }
            node.put("children", children);
            list.add(node);
        }
        return GsonTools.toJson(list);
    }

    @Journal(name = "添加打印机管理页面")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView _add(Printer printer) {
        return new ModelAndView(addOrEdit, model.addAttribute("printer", printer));
    }

    @ResponseBody
    @Journal(name = "保存打印机管理", logType = LogType.DB)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Valid
    public String add(Printer printer) throws Exception {
        printerService.save(printer);
        return GsonTools.toJson(printer);
    }

    @Journal(name = "编辑打印机管理页面")
    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public ModelAndView _edit(Printer printer) {
        printer = printerService.findById(Printer.class, printer.getId());
        return new ModelAndView(addOrEdit, model.addAttribute("printer", printer));
    }

    @ResponseBody
    @Journal(name = "编辑打印机管理", logType = LogType.DB)
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @Valid
    public String edit(Printer printer) throws Exception {
        printerService.update(printer);
        return GsonTools.toJson(printer);
    }

    @ResponseBody
    @Journal(name = "删除打印机管理", logType = LogType.DB)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String edit(String ids) throws Exception {
        printerService.delete(Printer.class, ids);
        return Constant.AJAX_SUCCESS;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取打印机列表信息")
    @RequestMapping(value = "printer", method = RequestMethod.GET)
    public ModelAndView _list() {
        return new ModelAndView(printer);
    }


    @NoAuth
    @ResponseBody
    @Journal(name = "获取打印机列表信息")
    @RequestMapping("printerList")
    public String printerList(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(printerService.findPageInfo(filter, page));
    }
}

package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.platform.entity.Log;
import com.bluebirdme.mes.platform.service.ILogService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.excel.ExcelDataFormatter;
import org.xdemo.superutil.thirdparty.excel.ExcelUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Journal(name = "日志模块")
@Controller
@RequestMapping({"/log"})
public class LogController extends BaseController {
    @Resource
    ILogService logService;

    public LogController() {
    }

    @RequestMapping(method = {RequestMethod.GET})
    public String index() {
        return "platform/log";
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "查询系统日志")
    @RequestMapping({"list"})
    public String list(Filter filter, Page page) throws Exception {
        Map<String, Object> map = this.logService.findPageInfo(filter, page);
        return GsonTools.toJson(map);
    }

    @ResponseBody
    @RequestMapping({"clear"})
    public String clear() {
        this.logService.clearAll();
        return "{}";
    }

    @ResponseBody
    @RequestMapping({"excel"})
    public void excel() throws Exception {
        List<Log> list = this.logService.findAll(Log.class);
        Workbook wb = ExcelUtils.getWorkBook(list, new ExcelDataFormatter());
        list.clear();
        this.response.reset();
        this.response.setHeader("Content-disposition", "attachment; filename=" + new String("日志".getBytes("gbk"), "iso8859-1") + ".xlsx");
        this.response.setContentType("application/msexcel");
        wb.write(this.response.getOutputStream());
        this.response.getOutputStream().flush();
        this.response.getOutputStream().close();
    }
}

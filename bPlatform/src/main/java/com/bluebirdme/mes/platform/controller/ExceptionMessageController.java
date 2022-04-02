package com.bluebirdme.mes.platform.controller;

import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.platform.entity.ExceptionMessage;
import com.bluebirdme.mes.platform.service.IExceptionMessageService;
import com.google.gson.GsonBuilder;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.excel.ExcelDataFormatter;
import org.xdemo.superutil.thirdparty.excel.ExcelUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qianchen
 * @date 2020/05/21
 */

@Controller
@RequestMapping({"/exception"})
public class ExceptionMessageController extends BaseController {
    @Resource
    IExceptionMessageService exceptionService;

    public ExceptionMessageController() {
    }

    @NoLogin
    @RequestMapping(method = {RequestMethod.GET})
    public String index() {
        return "platform/exception";
    }

    @NoLogin
    @ResponseBody
    @RequestMapping({"list"})
    public String list(Filter filter, Page page) throws Exception {
        return (new GsonBuilder()).serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(exceptionService.findPageInfo(filter, page));
    }

    @NoLogin
    @ResponseBody
    @RequestMapping({"clear"})
    public String clear() {
        this.exceptionService.clearAll();
        return "{}";
    }

    @NoLogin
    @ResponseBody
    @RequestMapping({"excel"})
    public void excel() throws Exception {
        List<ExceptionMessage> list = this.exceptionService.findAll(ExceptionMessage.class);
        Workbook wb = ExcelUtils.getWorkBook(list, new ExcelDataFormatter());
        list.clear();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String("异常信息".getBytes("gbk"), "iso8859-1") + ".xlsx");
        response.setContentType("application/msexcel");
        wb.write(response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}

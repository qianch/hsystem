/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.btwManager.controller;

import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.btwManager.entity.BtwFilePrint;
import com.bluebirdme.mes.btwManager.service.IBtwFilePrintService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.List;

/**
 * btw文件Controller
 *
 * @author 徐波
 * @Date 2016-11-26 23:01:34
 */
@Controller
@RequestMapping("/btwManager/BtwFilePrint")
@Journal(name = "btw文件")
public class BtwFilePrintController extends BaseController {
    // btw文件页面
    final String index = "btwManager/btwFile";
    final String btwFilePrintAddOrEditUrl = "btwManager/btwFilePrintAddOrEdit";

    @Resource
    IBtwFilePrintService btwFilePrintService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }


    @ResponseBody
    @Journal(name = "查询标签下面的打印属性", logType = LogType.DB)
    @RequestMapping(value = "findBtwFilePrints", method = RequestMethod.POST)
    public String findBtwFilePrints(Long btwFileId) throws Exception {
        return GsonTools.toJson(btwFilePrintService.findBtwFilePrints(btwFileId));
    }

    @Journal(name = "加载计划打印明细页面")
    @RequestMapping(value = "btwFilePrintAddOrEdit", method = RequestMethod.GET)
    public ModelAndView btwFilePrintAddOrEdit(Long btwFileId) {
        BtwFile entity = btwFilePrintService.findById(BtwFile.class, btwFileId);
        List<BtwFilePrint> listBtwFilePrint = btwFilePrintService.find(BtwFilePrint.class, "btwFileId", btwFileId);
        if (entity != null) {
            entity.setListBtwFilePrint(listBtwFilePrint);
        }
        return new ModelAndView(btwFilePrintAddOrEditUrl, model.addAttribute("btwFile", entity));
    }

    @ResponseBody
    @Journal(name = "保存标签打印属性", logType = LogType.DB)
    @RequestMapping(value = "saveBtwFilePrints", method = RequestMethod.POST)
    public String saveBtwFilePrints(@RequestBody BtwFile btwFile) {
        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        return GsonTools.toJson(btwFilePrintService.saveBtwFilePrints(btwFile, userId));
    }
}

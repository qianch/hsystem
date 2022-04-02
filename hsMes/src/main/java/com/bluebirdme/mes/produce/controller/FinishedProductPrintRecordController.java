/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.produce.controller;

import com.bluebirdme.mes.btwManager.entity.BtwFile;
import com.bluebirdme.mes.btwManager.entity.BtwFilePrint;
import com.bluebirdme.mes.btwManager.service.IBtwFilePrintService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.constant.Constant;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductPrintRecord;
import com.bluebirdme.mes.produce.service.IFinishedProductPrintRecordService;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
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
@RequestMapping("/finishProduct/finishedProductPrintRecord")
@Journal(name = "成品打印属性")
public class FinishedProductPrintRecordController extends BaseController {

    // btw文件页面
    final String index = "";
    final String finishedProductPrintRecordAddOrEditUrl = "produce/finishProduct/finishedProductPrintRecordAddOrEdit";

    @Resource
    IFinishedProductPrintRecordService finishedProductPrintRecordService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }


    @ResponseBody
    @Journal(name = "查询标签下面的打印属性", logType = LogType.DB)
    @RequestMapping(value = "findFinishedProductPrintRecords", method = RequestMethod.POST)
    public String findFinishedProductPrintRecords(Long productId) throws Exception {

        return GsonTools.toJson(finishedProductPrintRecordService.findFinishedProductPrintRecords(productId));
    }

    @Journal(name = "加载计划打印明细页面")
    @RequestMapping(value = "finishedProductPrintRecordAddOrEdit", method = RequestMethod.GET)
    public ModelAndView finishedProductPrintRecordAddOrEdit(Long productId) throws Exception {

        FinishedProduct entity = finishedProductPrintRecordService.findById(FinishedProduct.class, productId);
        List<FinishedProductPrintRecord> listFinishedProductPrintRecord = finishedProductPrintRecordService.find(FinishedProductPrintRecord.class, "productId", productId);
        if (entity != null) {
            entity.setListFinishedProductPrintRecord(listFinishedProductPrintRecord);
        }
        return new ModelAndView(finishedProductPrintRecordAddOrEditUrl, model.addAttribute("finishedProduct", entity));
    }

    @Journal(name = "加载计划打印明细页面")
    @RequestMapping(value = "finishedProductPrintRecordAddOrEdit2", method = RequestMethod.GET)
    public ModelAndView finishedProductPrintRecordAddOrEdit2(Long salesOrderDetailId) throws Exception {

        SalesOrderDetail salesOrderDetail = finishedProductPrintRecordService.findById(SalesOrderDetail.class, salesOrderDetailId);

        FinishedProduct entity = finishedProductPrintRecordService.findById(FinishedProduct.class, salesOrderDetail.getProductId());
        List<FinishedProductPrintRecord> listFinishedProductPrintRecord = finishedProductPrintRecordService.find(FinishedProductPrintRecord.class, "productId", entity.getId());
        if (entity != null) {
            entity.setListFinishedProductPrintRecord(listFinishedProductPrintRecord);
        }
        return new ModelAndView(finishedProductPrintRecordAddOrEditUrl, model.addAttribute("finishedProduct", entity));
    }

    @ResponseBody
    @Journal(name = "保存标签打印属性", logType = LogType.DB)
    @RequestMapping(value = "saveFinishedProductPrintRecords", method = RequestMethod.POST)
    public String saveFinishedProductPrintRecords(@RequestBody FinishedProduct finishedProduct) throws Exception {

        String userId = session.getAttribute(Constant.CURRENT_USER_ID).toString();
        return GsonTools.toJson(finishedProductPrintRecordService.saveFinishedProductPrintRecords(finishedProduct, userId));
    }


}

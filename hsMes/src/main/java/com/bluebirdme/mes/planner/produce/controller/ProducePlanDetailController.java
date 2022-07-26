/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.planner.produce.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.produce.service.IProducePlanDetailService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.stock.service.IProductStockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 出货计划Controller
 *
 * @author 徐波
 * @Date 2016-11-2 9:30:06
 */
@Controller
@RequestMapping("/planner/producePlanDetail")
@Journal(name = "生产计划明细")
public class ProducePlanDetailController extends BaseController {
    /**
     * 出货调拨单页面
     */
    final String index = "planner/produce/producePlanDetailPrint/producePlanDetailPrint";
    final String editProducePlanDetailPrintsUrl = "planner/produce/producePlanDetailPrint/producePlanDetailPrintAddOrEdit";

    @Resource
    IProducePlanDetailService producePlanDetailService;

    @Resource
    IProductStockService productStockService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取生产计划明细信息")
    @RequestMapping("list")
    public String getProducePlanDetailRecord(Filter filter, Page page) throws Exception {
        Map<String, Object> pageInfo = producePlanDetailService.findPageInfo(filter, page);
        return GsonTools.toJson(pageInfo);
    }

    @ResponseBody
    @Journal(name = "获取生产计划明细信息2")
    @RequestMapping("getProducePlanDetailPageInfo")
    public String getProducePlanDetailPageInfo(Filter filter, Page page) throws Exception {
        Map<String, Object> findPageInfo = producePlanDetailService.findProducePlanDetail(filter, page);
        List<Map<String, Object>> rows = (List<Map<String, Object>>) findPageInfo.get("rows");
        if (rows == null) {
            return GsonTools.toJson(findPageInfo);
        }
        for (Map<String, Object> row : rows) {
            Double weight = 0.0;
            String producePlanDetailId = row.get("ID").toString();
            page = new Page();
            page.setAll(1);
            filter = new Filter();
            filter.set("producePlanDetailId", producePlanDetailId);
            List<Map<String, Object>> moverows = (List<Map<String, Object>>) productStockService.moveInfolist(filter, page).get("rows");
            if (moverows == null) {
                row.put("WEIGHT", weight);
                row.put("TOTALCOUNT", 0);
                continue;
            }
            for (Map<String, Object> moverow : moverows) {
                weight += (Double) moverow.get("WEIGHT");
            }
            row.put("WEIGHT", weight);
            row.put("TOTALCOUNT", moverows.size());
        }
        return GsonTools.toJson(findPageInfo);
    }


    @ResponseBody
    @Journal(name = "查询计划明细下面的打印信息", logType = LogType.DB)
    @RequestMapping(value = "findProducePlanDetailPrints", method = RequestMethod.POST)
    public String findProducePlanDetailPrints(Long ProducePlanDetailId) throws Exception {
        return GsonTools.toJson(producePlanDetailService.findProducePlanDetailPrints(ProducePlanDetailId));
    }


    @ResponseBody
    @Journal(name = "查询计划明细下面的打印信息", logType = LogType.DB)
    @RequestMapping(value = "findPlanDetailPrintsBybtwFileId", method = RequestMethod.POST)
    public String findPlanDetailPrintsBybtwFileId(Long ProducePlanDetailId, long btwFileId) throws Exception {
        return GsonTools.toJson(producePlanDetailService.findPlanDetailPrintsBybtwFileId(ProducePlanDetailId, btwFileId));
    }

    @Journal(name = "加载计划打印明细页面")
    @RequestMapping(value = "editProducePlanDetailPrints", method = RequestMethod.GET)
    public ModelAndView editProducePlanDetailPrints(Long ProducePlanDetailId, Long btwFileId, String tagType) {
        ProducePlanDetail producePlanDetail = producePlanDetailService.findById(ProducePlanDetail.class, ProducePlanDetailId);
        SalesOrderDetail salesOrderDetail = producePlanDetailService.findById(SalesOrderDetail.class, producePlanDetail.getFromSalesOrderDetailId());
        long customerId = 0;
        if (salesOrderDetail.getMirrorProductId() != null) {
            FinishedProductMirror finishedProduct = producePlanDetailService.findById(FinishedProductMirror.class, salesOrderDetail.getMirrorProductId());
            customerId = finishedProduct.getProductConsumerId();
        }

        if (customerId == 0) {
            FinishedProduct finishedProduct = producePlanDetailService.findById(FinishedProduct.class, salesOrderDetail.getProductId());
            customerId = finishedProduct.getProductConsumerId();
        }
        return new ModelAndView(editProducePlanDetailPrintsUrl,
                model.addAttribute("producePlanDetail", producePlanDetail)
                        .addAttribute("btwFileId", btwFileId)
                        .addAttribute("customerId", customerId)
                        .addAttribute("tagType", tagType)
        );
    }

    @ResponseBody
    @Journal(name = "保存打印明细", logType = LogType.DB)
    @RequestMapping(value = "saveProducePlanDetailPrints", method = RequestMethod.POST)
    public String saveProducePlanDetailPrints(long producePlanDetailId, long btwFileId, String planDetailPrintsData) throws Exception {
        return GsonTools.toJson(producePlanDetailService.saveProducePlanDetailPrints(producePlanDetailId, btwFileId, planDetailPrintsData));
    }

    @ResponseBody
    @Journal(name = "生成打印明细", logType = LogType.DB)
    @RequestMapping(value = "createProducePlanDetailPrints", method = RequestMethod.POST)
    public synchronized String createProducePlanDetailPrints(long producePlanDetailId, long btwFileId) throws Exception {
        return GsonTools.toJson(producePlanDetailService.createProducePlanDetailPrints(producePlanDetailId, btwFileId));
    }
}

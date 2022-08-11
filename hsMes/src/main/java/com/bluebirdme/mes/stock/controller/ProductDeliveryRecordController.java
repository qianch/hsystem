/**
 * 上海蓝鸟集团
 * 上海蓝鸟科技股份有限公司
 * 华东工程中心（无锡）
 * 2016版权所有
 */
package com.bluebirdme.mes.stock.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoAuth;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.stock.service.IProductDeliveryRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;

/**
 * 成品出货单Controller
 *
 * @author 宋黎明
 * @Date 2016-11-27 13:57:44
 */
@Controller
@RequestMapping("stock/productDeliveryRecord")
@Journal(name = "出货计划")
public class ProductDeliveryRecordController extends BaseController {
    /**
     * 成品出货单页面
     */
    final String index = "stock/productStock/productDeliveryRecord";

    @Resource
    IProductDeliveryRecordService productDeliveryRecordService;

    @Journal(name = "首页")
    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return index;
    }

    @NoAuth
    @ResponseBody
    @Journal(name = "获取成品出货单列表信息")
    @RequestMapping("list")
    public String getProductDeliveryRecord(Filter filter, Page page) throws Exception {
        return GsonTools.toJson(productDeliveryRecordService.findPageInfo(filter, page));
    }

    @ResponseBody
    @Journal(name = "根据出货单编码查询出库信息", logType = LogType.DB)
    @RequestMapping(value = "findOutRecord", method = RequestMethod.POST)
    public String findOutRecord(String packingNumber) {
        return GsonTools.toJson(productDeliveryRecordService.findOutRecordByDeliveryCode(packingNumber));
    }
}
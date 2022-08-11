
package com.bluebirdme.mes.stock.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.stock.service.IStockService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 库存模块
 *
 * @author Goofy
 * @Date 2016年11月24日 下午12:22:13
 */
@Controller
@RequestMapping("/stock")
public class StockController extends BaseController {
    @Resource
    IStockService service;

    /**
     * 库存页面
     */
    private final String STOCK = "stock/monitor/stock";
    private final String YL_1 = "stock/monitor/yl-1";
    private final String YL_2 = "stock/monitor/yl-2";
    private final String CP_1 = "stock/monitor/cp-1";
    private final String CP_2 = "stock/monitor/cp-2";
    private final String CP_3 = "stock/monitor/cp-3";
    private final String CP_4 = "stock/monitor/cp-4";
    private final String CP_5 = "stock/monitor/cp-5";
    private final String CP_6 = "stock/monitor/cp-6";
    private final String CP_7 = "stock/monitor/cp-7";

    private final Map<String, String> PAGES = new HashMap<>() {{
        put("yl-1", "stock/monitor/yl-1");
        put("yl-2", "stock/monitor/yl-2");
        put("cp-1", "stock/monitor/cp-1");
        put("cp-2", "stock/monitor/cp-2");
        put("cp-3", "stock/monitor/cp-3");
        put("cp-4", "stock/monitor/cp-4");
        put("cp-5", "stock/monitor/cp-5");
        put("cp-6", "stock/monitor/cp-6");
        put("cp-7", "stock/monitor/cp-7");
    }};

    @RequestMapping("monitor")
    public String monitor() {
        return STOCK;
    }

    @Journal(name = "仓库平面图")
    @RequestMapping("monitor/{page}")
    public String page(@PathVariable(value = "page") String page) {
        return PAGES.get(page);
    }

    @Journal(name = "库位信息统计")
    @ResponseBody
    @RequestMapping("monitor/{warehouseType}/sum")
    public String info(@PathVariable("warehouseType") String type, String[] kuweis) throws SQLTemplateException {
        return GsonTools.toJson(service.list(type, kuweis));
    }
}

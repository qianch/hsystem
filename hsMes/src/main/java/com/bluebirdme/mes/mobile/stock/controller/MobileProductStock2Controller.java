package com.bluebirdme.mes.mobile.stock.controller;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.stock.entity.*;
import com.bluebirdme.mes.stock.service.IProductStockService;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.Warehouse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.bind.annotation.*;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.*;

/**
 * 成品库存操作
 *
 * @author Goofy
 * @Date 2020年5月15日 上午9:46:11
 */
@RestController
@RequestMapping("/mobile/stock2/product")
public class MobileProductStock2Controller extends BaseController {
    @Resource
    IProductStockService productStockService;

    @NoLogin
    @Journal(name = "保存PDA成品待入库信息及库存信息", logType = LogType.DB)
    @RequestMapping(value = "stockPending", method = RequestMethod.POST)
    public String pStockPending(String puname, ProductInRecord productInRecord, Long overTime) throws Exception {
        if (productInRecord.getWarehouseCode() == null || "".equals(productInRecord.getWarehouseCode()) || productInRecord.getWarehousePosCode() == null || "".equals(productInRecord.getWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        return GsonTools.toJson(productStockService.saveStockPending(productInRecord, overTime));
    }

    @NoLogin
    @Journal(name = "保存PDA成品入库信息及库存信息", logType = LogType.DB)
    @RequestMapping(value = "pIn", method = RequestMethod.POST)
    public String pIn(String puname, ProductInRecord productInRecord, Long overTime) throws Exception {
        if (productInRecord.getWarehouseCode() == null || "".equals(productInRecord.getWarehouseCode()) || productInRecord.getWarehousePosCode() == null || "".equals(productInRecord.getWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        return GsonTools.toJson(productStockService.pIn(productInRecord, overTime));//新入库
    }

    @NoLogin
    @Journal(name = "保存PDA胚布待入库信息及库存信息", logType = LogType.DB)
    @RequestMapping(value = "pbPendingIn", method = RequestMethod.POST)
    public String pbPendingIn(String puname, PendingInRecord pendingInRecord, Long overTime) throws Exception {
        if (pendingInRecord.getWarehouseCode() == null || "".equals(pendingInRecord.getWarehouseCode()) || pendingInRecord.getWarehousePosCode() == null || "".equals(pendingInRecord.getWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        return GsonTools.toJson(productStockService.pbPendingIn(pendingInRecord, overTime));
    }


    @NoLogin
    @Journal(name = "保存PDA胚布入库信息及库存信息新流程", logType = LogType.DB)
    @RequestMapping(value = "pbStockIn", method = RequestMethod.POST)
    public String pbStockIn(String puname, ProductInRecord productInRecord, Long overTime) throws Exception {
        if (productInRecord.getWarehouseCode() == null || "".equals(productInRecord.getWarehouseCode()) || productInRecord.getWarehousePosCode() == null || "".equals(productInRecord.getWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        return GsonTools.toJson(productStockService.pbIn(productInRecord));
    }

    @NoLogin
    @Journal(name = "保存PDA胚布入库信息及库存信息老流程", logType = LogType.DB)
    @RequestMapping(value = "pbIn", method = RequestMethod.POST)
    public String pbIn(String puname, ProductInRecord productInRecord, Long overTime) throws Exception {
        if (productInRecord.getWarehouseCode() == null || "".equals(productInRecord.getWarehouseCode()) || productInRecord.getWarehousePosCode() == null || "".equals(productInRecord.getWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        return GsonTools.toJson(productStockService.savePbIn(productInRecord, overTime));
    }

    @NoLogin
    @Journal(name = "保存PDA成品变成在途状态", logType = LogType.DB)
    @RequestMapping(value = "pontheway", method = RequestMethod.POST)
    public String POnTheWay(String code, String warehousecode, String logisticscompany, String plate, long loginid) throws Exception {
        return GsonTools.toJson(productStockService.POnTheWay(code, warehousecode, logisticscompany, plate, loginid));
    }

    @NoLogin
    @Journal(name = "成品移库", logType = LogType.DB)
    @RequestMapping(value = "pMove", method = RequestMethod.POST)
    public String pMove(StockMove stockMove, String code) throws Exception {
        if (stockMove.getNewWarehouseCode() == null || "".equals(stockMove.getNewWarehouseCode()) || stockMove.getNewWarehousePosCode() == null || "".equals(stockMove.getNewWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        return GsonTools.toJson(productStockService.pMove(stockMove, code));//新移库
    }

    @NoLogin
    @Journal(name = "胚布领料", logType = LogType.DB)
    @RequestMapping(value = "pbPickIn", method = RequestMethod.POST)
    public String pbPickIn(FabricPickRecord fabricPickRecord) throws Exception {
        List<FabricPickRecord> fabricPickRecordlist = new ArrayList<FabricPickRecord>();
        fabricPickRecordlist.add(fabricPickRecord);
        return GsonTools.toJson(productStockService.pbPicks(fabricPickRecordlist));
    }

    @NoLogin
    @Journal(name = "胚布批量领料", logType = LogType.DB)
    @RequestMapping(value = "pbPicksIn", method = RequestMethod.POST)
    public String pbPicksIn(List<FabricPickRecord> fabricPickRecordlist) throws Exception {
        return GsonTools.toJson(productStockService.pbPicks(fabricPickRecordlist));
    }

    @NoLogin
    @Journal(name = "胚布从裁剪胚布库到裁剪线边库", logType = LogType.DB)
    @RequestMapping(value = "pbPicks", method = RequestMethod.POST)
    public String pbPicks(StockFabricMove stockMove, String code) throws Exception {
        if (stockMove.getNewWarehouseCode() == null || "".equals(stockMove.getNewWarehouseCode()) || stockMove.getNewWarehousePosCode() == null || "".equals(stockMove.getNewWarehousePosCode())) {
            return GsonTools.toJson("请选择库位");
        }
        String result = productStockService.saveAndUpdate1(stockMove, code);
        if (result.equals("locked")) {
            return ajaxError("产品被冻结");
        } else if (result.equals("out")) {
            return ajaxError("产品未入库");
        }
        return GsonTools.toJson(result);
    }

    @NoLogin
    @Journal(name = "分批出库", logType = LogType.DB)
    @RequestMapping(value = "pOutInBatches", method = RequestMethod.POST)
    public String pOutInBatches(String puname, String codes, Long UserId, String packingNum, String plate, String boxNumber, Double count, int isfinished) throws Exception {
        boolean flag = false;
        //不为合格或者退货的原料编码
        String errorCode = "";
        Map<String, Object> map = new HashMap<String, Object>();
        String[] _codes = codes.split(",");
        Tray tray = new Tray();
        for (int i = 0; i < _codes.length; i++) {
            map.clear();
            map.put("trayBarcode", _codes[i]);
            tray = productStockService.findUniqueByMap(Tray.class, map);
            if (tray != null && !tray.getRollQualityGradeCode().equals("A")) {
                //拼接异常的条码
                errorCode += errorCode == "" ? _codes[i] : "," + _codes[i];
                flag = true;
            }
            map.clear();
            map.put("partBarcode", _codes[i]);
            Roll roll = productStockService.findUniqueByMap(Roll.class, map);
            if (roll != null && !roll.getRollQualityGradeCode().equals("A")) {
                //拼接异常的条码
                errorCode += errorCode == "" ? _codes[i] : "," + _codes[i];
                flag = true;
            }
        }
        if (flag) {
            return ajaxError(errorCode + "条码状态异常，只有合格品才能出库！");
        } else {
            return GsonTools.toJson(productStockService.pOut(codes, UserId, packingNum, plate, boxNumber, count, isfinished));//新出库
        }
    }

    @NoLogin
    @Journal(name = "查询同一批次待入库的其他货物所在的位置")
    @RequestMapping(value = "pendingWarhourse", method = RequestMethod.GET)
    public String pendingWarhourse(String salesOrderCode, String batchCode, String productModel) throws Exception {
        return GsonTools.toJson(productStockService.pendingWarhourse(salesOrderCode, batchCode, productModel));
    }


    @NoLogin
    @Journal(name = "通过仓库代码获取仓库名字")
    @RequestMapping(value = "queryWarhourse", method = RequestMethod.GET)
    public String queryWarhourse(String warehouseCode) throws Exception {

        Warehouse warehouse = productStockService.findOne(Warehouse.class, "warehouseCode", warehouseCode);
        if (warehouse == null) {
            return GsonTools.toJson("仓库编码不存在:" + warehouseCode);
        }
        return GsonTools.toJson(warehouse);
    }

    @NoLogin
    @Journal(name = "成品批量入库", logType = LogType.DB)
    @RequestMapping(value = "pIns", method = RequestMethod.POST)
    public String pIns(String productInRecordlist) throws Exception {
        Gson gson = new Gson();
        List<ProductInRecord> list = gson.fromJson(productInRecordlist, new TypeToken<List<ProductInRecord>>() {
        }.getType());
        return GsonTools.toJson(productStockService.pIns(list));//新移库
    }

    @NoLogin
    @Journal(name = "胚布批量入库", logType = LogType.DB)
    @RequestMapping(value = "pbIns", method = RequestMethod.POST)
    public String pbIns(String productInRecordlist) throws Exception {
        Gson gson = new Gson();
        List<ProductInRecord> list = gson.fromJson(productInRecordlist, new TypeToken<List<ProductInRecord>>() {
        }.getType());
        return GsonTools.toJson(productStockService.pbIns(list));//胚布批量入库
    }
}

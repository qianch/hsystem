package com.bluebirdme.mes.mobile.common.controller;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsDetailMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsMirror;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.base.entity.Filter;
import com.bluebirdme.mes.core.base.entity.Page;
import com.bluebirdme.mes.mobile.base.MobileBaseController;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.planner.cut.entity.CutPlan;
import com.bluebirdme.mes.planner.cut.entity.Iplan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlan;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.turnbag.entity.TurnBagPlan;
import com.bluebirdme.mes.planner.weave.entity.WeavePlan;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.ExceptionMessage;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.platform.service.IExceptionMessageService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.statistics.controller.ProductInfoTreeStruct;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.statistics.service.ITotalStatisticsService;
import com.bluebirdme.mes.stock.entity.ProductInRecord;
import com.bluebirdme.mes.stock.entity.ProductStockState;
import com.bluebirdme.mes.stock.service.IProductInRecordService;
import com.bluebirdme.mes.stock.service.IProductStockService;
import com.bluebirdme.mes.store.entity.*;
import com.bluebirdme.mes.store.service.IBarCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 移动端公共方法
 *
 * @author Goofy
 * @Date 2016年11月16日 下午2:19:30
 */
@RestController
@RequestMapping("/mobile/common/")
public class MobileCommonController extends MobileBaseController {
    private static Logger log = LoggerFactory.getLogger(MobileCommonController.class);
    @Resource
    IExceptionMessageService exceptionService;
    @Resource
    ISalesOrderService orderService;
    @Resource
    IMobileService mobileService;
    @Resource
    ITotalStatisticsService totalStatisticsService;
    @Resource
    IProductStockService productStockService;
    @Resource
    IBarCodeService barcodeService;
    @Resource
    IWeavePlanService weavePlanService;
    @Resource
    IProductInRecordService productInRecordService;

    @NoLogin
    @Journal(name = "查询审核通过的，且未关闭的订单")
    @RequestMapping("orders")
    public String orders() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("auditState", AuditConstant.RS.PASS);
        param.put("isClosed", 0);
        Filter filter = new Filter();
        filter.set("auditState", AuditConstant.RS.PASS + "");
        filter.set("isClosed", 0 + "");
        Page page = new Page();
        page.setAll(1);
        return GsonTools.toJson(orderService.findPageInfo(filter, page).get("rows"));
    }

    @NoLogin
    @Journal(name = "PDA查询盒信息")
    @RequestMapping("/box/info/{code}")
    public String boxInfo(@PathVariable("code") String boxCode) {
        // 产出计划ID[编织计划，翻包计划，裁剪计划],是否打包结束，是否成品胚布,含有的卷条码,部件ID，部件名称，是否空条码
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("barcode", boxCode);
        // 盒条码
        BoxBarcode bbc = mobileService.findUniqueByMap(BoxBarcode.class, param);

        param.clear();
        param.put("boxBarcode", boxCode);
        // 盒信息
        Box box = mobileService.findUniqueByMap(Box.class, param);

        Map<String, Object> ret = new HashMap<String, Object>();

        param.clear();
        param.put("boxBarcode", boxCode);

        // 卷条码
        List<BoxRoll> brs = mobileService.findListByMap(BoxRoll.class, param);
        // 含有的卷条码
        ret.put("CHILDREN", brs);

        if (bbc == null) {
            return ajaxError("无效条码");
        }

        ret.put("ISOPENED", bbc.getIsOpened());
        ret.put("CODE", boxCode);

        // 空的盒条码
        if (box == null) {
            ret.put("EMPTY", true);
        } else {

            ret.put("EMPTY", false);

            param.clear();
            param.put("boxBarcode", boxCode);
            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);

            // 是否已打包
            ret.put("PACKED", packedToTray);

            // 状态
            ret.put("FROZEN", totalStatisticsService.isFrozen(boxCode));

            // 等级
            ret.put("GRADE", box.getRollQualityGradeCode());

            // 订单
            ret.put("ORDER", bbc.getSalesOrderCode());
            // 批次
            ret.put("BATCH", bbc.getBatchCode());

            if (box.getEndPack().intValue() == 0) {
                // 打包结束
                ret.put("ENDPACK", false);
                // 计划ID
                ret.put("PLANID", bbc.getPlanId());
                if (bbc.getPartId() == null) {
                    // 是否是成品胚布
                    ret.put("DXB", 1);
                    // 部件ID
                    ret.put("PARTID", null);
                    // 部件名称
                    ret.put("PARTNAME", "");
                    /*ret.put("ORDER", null);*/
                    /*ret.put("BATCH", null);*/
                } else {
                    TcBomVersionParts part = mobileService.findById(TcBomVersionParts.class, bbc.getPartId());

                    // 是否是成品胚布
                    ret.put("DXB", part.getTcProcBomVersionPartsType().equals("成品胚布") ? -1 : 1);
                    // 部件ID
                    ret.put("PARTID", part.getId());
                    // 部件名称
                    ret.put("PARTNAME", part.getTcProcBomVersionPartsName());
                    ret.put("ORDER", bbc.getSalesOrderCode());
                    ret.put("BATCH", bbc.getBatchCode());
                }
            } else {
                ret.put("ENDPACK", true);
                // 计划ID
                ret.put("PLANID", bbc.getPlanId());
                if (bbc.getPartId() == null) {
                    // 是否是成品胚布
                    ret.put("DXB", 1);
                    // 部件ID
                    ret.put("PARTID", null);
                    // 部件名称
                    ret.put("PARTNAME", "");
                } else {
                    TcBomVersionParts part = mobileService.findById(TcBomVersionParts.class, bbc.getPartId());

                    // 是否是成品胚布
                    ret.put("DXB", part.getTcProcBomVersionPartsType().equals("成品胚布") ? -1 : 1);
                    // 部件ID
                    ret.put("PARTID", part.getId());
                    // 部件名称
                    ret.put("PARTNAME", part.getTcProcBomVersionPartsName());
                }
            }
        }

        return GsonTools.toJson(ret);
    }

    @NoLogin
    @Journal(name = "PDA查询卷信息")
    @RequestMapping(value = "/roll/info/{code}")
    public String rollInfo(@PathVariable("code") String rollCode) {
        // 产出计划ID[编织计划，翻包计划，裁剪计划],是否已打包，是否成品胚布,含有的卷条码,部件ID，部件名称
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> ret = new HashMap<String, Object>();

        param.put("barcode", rollCode);
        // 卷条码
        RollBarcode rbc = mobileService.findUniqueByMap(RollBarcode.class, param);
        PartBarcode pbc = mobileService.findUniqueByMap(PartBarcode.class, param);

        List<ProductStockState> psss = mobileService.findListByMap(ProductStockState.class, param);
        //是否入库
        if (psss.size() > 0 && psss.get(psss.size() - 1).getStockState() == 1) {
            ret.put("IN", 1);
        }

        IBarcode barcode;

        if (rollCode.startsWith("R")) {
            barcode = rbc;
            if (rbc.getIsAbandon() == 1) {
                return ajaxError("条码已作废");
            }
        } else {
            barcode = pbc;
            if (pbc.getIsAbandon() == 1) {
                return ajaxError("条码已作废");
            }
        }

        if (barcode == null) {
            return ajaxError("无效条码");
        }

        // 卷信息
        Roll roll = mobileService.findBarCodeReg(BarCodeRegType.ROLL, rollCode);
        ret.put("PLANID", barcode.getPlanId());
        if (rollCode.startsWith("P")) {
            param.clear();
            param.put("partBarcode", rollCode);
            boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
            // 是否已打包
            ret.put("PACKED", packedToBox || packedToTray);
        } else {
            param.clear();
            param.put("rollBarcode", rollCode);
            boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
            // 是否已打包
            ret.put("PACKED", packedToBox || packedToTray);
        }

        param.clear();
        param.put("boxBarcode", rollCode);

        ret.put("CODE", rollCode);
        // 空的卷条码
        if (roll == null) {
            // 是否登记
            ret.put("REG", false);
        } else {
            // 是登记
            ret.put("REG", true);
            // 状态
            ret.put("FROZEN", totalStatisticsService.isFrozen(rollCode));
            // 等级
            ret.put("GRADE", roll.getRollQualityGradeCode());
            // 订单
            ret.put("ORDER", barcode.getSalesOrderCode());
            // 批次
            ret.put("BATCH", barcode.getBatchCode());
            if (barcode.getPartId() == null) {
                // 是否是成品胚布
                ret.put("DXB", 1);
                // 部件ID
                ret.put("PARTID", null);
                // 部件名称
                ret.put("PARTNAME", "");
            } else {
                TcBomVersionParts part = mobileService.findById(TcBomVersionParts.class, barcode.getPartId());
                // 是否是成品胚布
                ret.put("DXB", part.getTcProcBomVersionPartsType().equals("成品胚布") ? -1 : 1);
                // 部件ID
                ret.put("PARTID", part.getId());
                // 部件名称
                ret.put("PARTNAME", part.getTcProcBomVersionPartsName());
            }
        }
        return GsonTools.toJson(ret);
    }

    @NoLogin
    @Journal(name = "(盒打包用)PDA查询卷信息")
    @RequestMapping(value = "/roll/info1/{code}")
    public String rollInfo1(@PathVariable("code") String rollCode) {
        // 产出计划ID[编织计划，翻包计划，裁剪计划],是否已打包，是否成品胚布,含有的卷条码,部件ID，部件名称
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> ret = new HashMap<>();
        param.put("barcode", rollCode);
        // 卷条码
        RollBarcode rbc = mobileService.findUniqueByMap(RollBarcode.class, param);
        PartBarcode pbc = mobileService.findUniqueByMap(PartBarcode.class, param);

        List<ProductStockState> psss = mobileService.findListByMap(ProductStockState.class, param);
        //是否入库
        if (psss.size() > 0 && psss.get(psss.size() - 1).getStockState() == 1) {
            ret.put("IN", 1);
        }
        IBarcode barcode;
        if (rollCode.startsWith("R")) {
            barcode = rbc;
            if (rbc.getIsAbandon() == 1) {
                return ajaxError("条码已作废");
            }
        } else {
            barcode = pbc;
            if (pbc.getIsAbandon() == 1) {
                return ajaxError("条码已作废");
            }
        }

        if (barcode == null) {
            return ajaxError("无效条码");
        }

        // 卷信息
        Roll roll = mobileService.findBarCodeReg(BarCodeRegType.ROLL, rollCode);
        ret.put("PLANID", barcode.getPlanId());
        if (rollCode.startsWith("P")) {
            param.clear();
            param.put("partBarcode", rollCode);
            boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
            // 是否已打包
            ret.put("PACKED", packedToBox || packedToTray);
        } else {
            param.clear();
            param.put("rollBarcode", rollCode);
            boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
            // 是否已打包
            ret.put("PACKED", packedToBox || packedToTray);
        }

        param.clear();
        param.put("boxBarcode", rollCode);

        ret.put("CODE", rollCode);
        // 空的卷条码
        if (roll == null) {
            // 是否登记
            ret.put("REG", false);
        } else {
            // 是登记
            ret.put("REG", true);
            // 状态
            ret.put("FROZEN", totalStatisticsService.isFrozen(rollCode));
            // 等级
            ret.put("GRADE", roll.getRollQualityGradeCode());
            // 订单
            ret.put("ORDER", barcode.getSalesOrderCode());
            // 批次
            ret.put("BATCH", barcode.getBatchCode());
            if (barcode.getPartId() == null) {
                // 是否是成品胚布
                ret.put("DXB", 1);
                // 部件ID
                ret.put("PARTID", null);
                // 部件名称
                ret.put("PARTNAME", "");
            } else {
                TcBomVersionParts part = mobileService.findById(TcBomVersionParts.class, barcode.getPartId());
                // 是否是成品胚布
                ret.put("DXB", part.getTcProcBomVersionPartsType().equals("成品胚布") ? -1 : 1);
                // 部件ID
                ret.put("PARTID", part.getId());
                // 部件名称
                ret.put("PARTNAME", part.getTcProcBomVersionPartsName());
            }
        }

        return GsonTools.toJson(ret);
    }

    @NoLogin
    @Journal(name = "PDA查询托信息")
    @RequestMapping("/tray/info/{code}")
    public String trayInfo(@PathVariable("code") String trayCode) {
        Map<String, Object> param = new HashMap();
        param.put("barcode", trayCode);
        // 卷条码
        TrayBarCode tbc = mobileService.findUniqueByMap(TrayBarCode.class, param);
        param.clear();
        param.put("trayBarcode", trayCode);
        Tray tray = mobileService.findUniqueByMap(Tray.class, param);
        Map<String, Object> ret = new HashMap();
        ret.put("CODE", trayCode);

        if (tbc == null) {
            return ajaxError("无效条码");
        } else {
            if (tbc.getProducePlanDetailId() != null){
                ProducePlanDetail producePlanDetail = mobileService.findById(ProducePlanDetail.class, tbc.getProducePlanDetailId());
                if(producePlanDetail.getIsTurnBagPlan().equals("翻包")){
                    ret.put("TURNBAG",true);
                }
            }
        }
        ret.put("ISOPENED", tbc.getIsOpened());
        if (tray == null) {
            ret.put("EMPTY", true);
            ret.put("ENDPACK", false);
        } else {
            param.clear();
            param.put("barCode", trayCode);
            //托的在库状态
            List<ProductStockState> productStockStates = mobileService.findListByMap(ProductStockState.class, param);
            if (productStockStates.size() > 0) {
                ret.put("STOCKSTATE", productStockStates.get(productStockStates.size() - 1).getStockState());
            }
            ret.put("EMPTY", false);
            if (tray.getEndPack().intValue() == 0) {
                // 打包结束
                ret.put("ENDPACK", false);
                // 计划ID
                ret.put("PLANID", tbc.getPlanId());
                // 状态
                ret.put("FROZEN", totalStatisticsService.isFrozen(trayCode));
                // 等级
                ret.put("GRADE", tray.getRollQualityGradeCode());
                ret.put("ORDER", tbc.getSalesOrderCode());
                ret.put("BATCH", tbc.getBatchCode());
                if (tbc.getPartId() == null) {
                    // 是否是成品胚布
                    ret.put("DXB", 1);
                    // 部件ID
                    ret.put("PARTID", null);
                    // 部件名称
                    ret.put("PARTNAME", "");
                } else {
                    TcBomVersionParts part = mobileService.findById(TcBomVersionParts.class, tbc.getPartId());
                    // 是否是成品胚布
                    ret.put("DXB", part.getTcProcBomVersionPartsType().equals("成品胚布") ? -1 : 1);
                    // 部件ID
                    ret.put("PARTID", part.getId());
                    // 部件名称
                    ret.put("PARTNAME", part.getTcProcBomVersionPartsName());
                }
            } else {
                ret.put("ENDPACK", true);
            }
        }
        param.clear();
        param.put("trayBarcode", trayCode);
        List<TrayBoxRoll> tbrs = mobileService.findListByMap(TrayBoxRoll.class, param);
        ret.put("CHILDREN", tbrs);
        return GsonTools.toJson(ret);
    }

    @NoLogin
    @Journal(name = "根据卷、箱、托条码查询条码信息")
    @RequestMapping("barcodeInfos")
    public String barcodeInfos(String barCode) {
        IBarcode ib = null;
        HashMap<String, Object> map = new HashMap();
        map.put("barcode", barCode);
        if (barCode.startsWith("R")) {
            ib = totalStatisticsService.findUniqueByMap(RollBarcode.class, map);
        } else if (barCode.startsWith("P")) {
            ib = totalStatisticsService.findUniqueByMap(PartBarcode.class, map);
        } else if (barCode.startsWith("B")) {
            ib = totalStatisticsService.findUniqueByMap(BoxBarcode.class, map);
        } else if (barCode.startsWith("T")) {
            ib = totalStatisticsService.findUniqueByMap(TrayBarCode.class, map);
        }

        if (ib == null) {
            return ajaxError("未知条码");
        } else {
            return GsonTools.toJson(ib);
        }
    }

    @NoLogin
    @Journal(name = "根据卷、箱、托条码查询产品信息和订单信息,客户信息，以及状态信息")
    @RequestMapping("infos")
    public String infos(String barCode) {
        final String PRODUCT = "PRODUCT";
        final String ORDER = "ORDER";
        final String WEAVEPLAN = "WP";
        final String TURNBAGPLAN = "TBP";
        final String PACKED = "PACKED";
        final String CUTPLAN = "CP";
        final String PRODUCEPLAN = "PPD";
        final String CONSUMER = "CONSUMER";
        final String ROLL = "ROLL";
        final String ITEMS = "ITEMS";
        final String BOX = "BOX";
        final String TRAY = "TRAY";
        final String STOCK = "STOCK";
        final String WAREHOUSEPOSNAME = "WAREHOUSEPOSNAME";
        final String WAREHOUSENAME = "WAREHOUSENAME";
        final String STATE = "STATE";
        final String REGISTER = "REGISTER";
        final String CODE = "CODE";
        final String ERROR = "ERROR";
        final String OPERATOR = "OPERATOR";
        final String DEPARTMENT = "DEPARTMENT";
        final String QUALITY = "QUALITY"; // 质量等级
        final String MSG = "MSG";
        final String TRAYBARCODE = "TRAYBARCODE";

        FinishedProductMirror pm1 = null;
        FinishedProduct p;
        SalesOrderDetail orderDetail = null;
        SalesOrder order = null;
        Roll roll;
        Box box;
        Tray tray;
        ProductStockState pss;
        Consumer consumer = null;
        TotalStatistics tot;
        User user;
        Department dept;
        Map<String, Object> ret = new HashMap();
        Map<String, Object> param = new HashMap();
        Map<String, Object> map2 = new HashMap();
        Map<String, Object> param1 = new HashMap();

        ret.put("WARETYPE", "");
        ret.put("WAREHOUSENAME", "");
        ret.put("WAREHOUSEPOSCODE", "");
        ret.put("INTIME", "");

        List<ProductStockState> listProductStockState = mobileService.find(ProductStockState.class, "barCode", barCode);
        if (listProductStockState != null && listProductStockState.size() > 0) {
            Collections.sort(listProductStockState, (o1, o2) -> o2.getId().compareTo(o1.getId()));
            ProductStockState pstockstate = listProductStockState.get(0);
            ret.put("WAREHOUSEPOSCODE", pstockstate.getWarehousePosCode());
            List<Warehouse> listWarehouse = mobileService.find(Warehouse.class, "warehouseCode", pstockstate.getWarehouseCode());
            if (listWarehouse != null && listWarehouse.size() == 1) {
                ret.put("WARETYPE", listWarehouse.get(0).getWareType());
                ret.put("WAREHOUSENAME", listWarehouse.get(0).getWarehouseName());
            }
        }

        // 卷条码
        if (barCode.startsWith("R")) {
            String bcode = "";
            try {
                param.clear();
                param.put("barcode", barCode);
                RollBarcode rbc = mobileService.findUniqueByMap(RollBarcode.class, param);
                ret.put(CODE, rbc);
                SalesOrderDetail salesOrderDetail = mobileService.findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
                map2.put("salesOrderDetailId", salesOrderDetail.getId());
                map2.put("versionPartsId", rbc.getPartId());
                if (rbc != null) {
                    if (null != salesOrderDetail.getMirrorProcBomVersionId()) {
                        FinishedProductMirror pm = new FinishedProductMirror();
                        //版本部件
                        if (rbc.getPartId() != null) {
                            List<TcBomVersionPartsMirror> tcBomVersionPartsMirrorList = mobileService.findListByMap(TcBomVersionPartsMirror.class, map2);
                            if (tcBomVersionPartsMirrorList.size() == 0) {
                                ret.put("PARTNAME", "");
                            } else {
                                //部件名称
                                ret.put("PARTNAME", tcBomVersionPartsMirrorList.get(0).getTcProcBomVersionPartsName());
                            }
                        } else {
                            ret.put("PARTNAME", "");
                        }
                        //根据托条码的生产计划明细Id得到生产计划明细
                        ProducePlanDetail detail = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                        Consumer consumer2 = mobileService.findById(Consumer.class, detail.getConsumerId());
                        //客户简称
                        ret.put(PRODUCEPLAN, detail);
                        ret.put("CONSUMERSIMPLENAME", consumer2.getConsumerSimpleName());
                        param.clear();
                        param.put("rollBarcode", barCode);
                        List<Roll> list = mobileService.findListByMap(Roll.class, param);
                        // 查询卷信息
                        roll = ListUtils.isEmpty(list) ? null : list.get(0);
                        ret.put(ROLL, roll);
                        // 查不到卷信息
                        if (roll == null) {
                            ret.put(REGISTER, false);
                            ret.put(STOCK, false);
                            ret.put(STATE, -1);
                            // 查不到卷信息，肯定未打包
                            ret.put(PACKED, false);
                            ret.put(OPERATOR, null);
                            ret.put(DEPARTMENT, null);
                        } else {
                            bcode = TopBarcode(barCode);
                            param.clear();
                            param.put("barCode", barCode);
                            List<ProductStockState> psss = mobileService.findListByMap(ProductStockState.class, param);
                            if (bcode.indexOf("T") == 0) {
                                param.clear();
                                param.put("rollBarcode", bcode);
                                tot = mobileService.findUniqueByMap(TotalStatistics.class, param);
                                // 在库状态
                                if (tot == null) {
                                    ret.put(STOCK, -1);
                                } else {
                                    if (tot.getState() != null) {
                                        ret.put(STOCK, tot.getState());
                                    } else {
                                        ret.put(STOCK, 0);
                                    }
                                }
                            } else {
                                // 在库状态
                                ret.put(STOCK, psss.size() == 0 ? -1 : (psss.get(psss.size() - 1).getStockState()));
                            }
                            // 登记状态
                            ret.put(REGISTER, true);
                            if (psss.size() > 0) {
                                ret.put("WARHOUSECODE", psss.get(psss.size() - 1).getWarehouseCode());
                            }
                            // 状态
                            ret.put(STATE, roll.getState());
                            user = mobileService.findById(User.class, roll.getRollUserId());
                            if (user == null) {
                                log("找不到人员信息,ID:" + roll.getRollUserId());
                            }
                            ret.put(OPERATOR, user.getUserName());
                            dept = mobileService.findById(Department.class, user.getDid());
                            if (dept == null) {
                                log("找不到部门信息,ID:" + user.getDid());
                            }
                            ret.put(DEPARTMENT, dept.getName());
                            param.clear();
                            param.put("rollBarcode", barCode);
                            boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
                            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
                            ret.put(PACKED, packedToBox || packedToTray);
                            ret.put(QUALITY, roll.getRollQualityGradeCode());
                            ret.put("MEMO", roll.getMemo() == null ? "" : roll.getMemo());
                            param.clear();
                            param.put("rollBarcode", barCode);
                            TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, param);
                            if (tbr != null) {
                                ret.put(TRAYBARCODE, tbr.getTrayBarcode());
                            } else {
                                ret.put(TRAYBARCODE, "");
                            }
                        }
                        WeavePlan wp = mobileService.findById(WeavePlan.class, rbc.getPlanId());
                        param.clear();
                        if (wp != null) {
                            orderDetail = mobileService.findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
                            order = mobileService.findById(SalesOrder.class, orderDetail.getSalesOrderId());
                        }
                        if (orderDetail == null) {
                            log("找不到订单信息，订单号::" + rbc.getSalesOrderCode());
                        }
                        // 订单信息
                        ret.put(ORDER, orderDetail);
                        consumer = mobileService.findById(Consumer.class, order.getSalesOrderConsumerId());
                        if (consumer == null) {
                            log("找不到客户信息，ID:" + order.getSalesOrderConsumerId());
                        }
                        ret.put(CONSUMER, consumer);
                        map2.clear();
                        map2.put("salesOrderDetailId", salesOrderDetail.getId());
                        map2.put("productId", rbc.getSalesProductId());
                        List<FinishedProductMirror> list1 = mobileService.findListByMap(FinishedProductMirror.class, map2);
                        if (list1.size() > 0) {
                            pm1 = list1.get(0);
                        }
                        if (list1.size() == 0) {
                            map2.clear();
                            map2.put("salesOrderId", salesOrderDetail.getSalesOrderId());
                            map2.put("productId", rbc.getSalesProductId());
                            list1 = mobileService.findListByMap(FinishedProductMirror.class, map2);
                            if (list1.size() > 0) {
                                pm1 = list1.get(0);
                            }
                        }
                        if (pm1 == null) {
                            log("找不到对应的产品,ID:" + rbc.getSalesProductId());
                        }
                        if (pm1 != null) {
                            BeanUtils.copyProperties(pm1, pm);
                            WeavePlan wpn = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                            BigDecimal bg;//=new BigDecimal(0.0);
                            param.clear();
                            param.put("rollBarcode", barCode);
                            Roll rolls = mobileService.findUniqueByMap(Roll.class, param);
                            if (roll != null && (roll.getRollAutoWeight() != null || roll.getRollWeight() != null)) {
                                pm.setProductRollWeight(rolls.getRollWeight());
                            } else if (wpn != null && wpn.getSumCount() != null) {
                                ProducePlanDetail pd = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                                ProducePlan pp = mobileService.findById(ProducePlan.class, pd.getProducePlanId());
                                int sumCount = mobileService.isCount1(rbc.getBatchCode(), pp.getProducePlanCode(), pd.getFactoryProductName()).intValue();
                                int z = 0;//第几个二十卷
                                int remainder = 0;
                                if (sumCount != 0) {
                                    z = sumCount / 20;
                                    remainder = sumCount % 20;
                                }
                                if (z == 0) {
                                    if (remainder + 1 > 10) {
                                        WeavePlan w = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                                        if (w.getSumWeight() != null && w.getSumCount() != 0) {
                                            //算平均值
                                            bg = new BigDecimal((w.getSumWeight() - w.getToVoidWeight()) / (w.getSumCount() - w.getToVoid()));
                                            pm.setProductRollWeight(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                                        }
                                    }
                                } else {
                                    if (remainder + 1 > 2) {
                                        WeavePlan w = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                                        if (w.getSumWeight() != null && w.getSumCount() != 0) {
                                            //算平均值
                                            bg = new BigDecimal((w.getSumWeight() - w.getToVoidWeight()) / (w.getSumCount() - w.getToVoid()));
                                            pm.setProductRollWeight(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                                        }
                                    }
                                }
                            }
                        }
                        // 产品信息
                        ret.put(PRODUCT, pm);
                        ret.put(WEAVEPLAN, wp);
                    } else {
                        //版本部件
                        if (rbc.getPartId() != null) {
                            TcBomVersionParts tcBomVersionParts = mobileService.findById(TcBomVersionParts.class, rbc.getPartId());
                            if (tcBomVersionParts == null) {
                                ret.put("PARTNAME", "");
                            } else {
                                //部件名称
                                ret.put("PARTNAME", tcBomVersionParts.getTcProcBomVersionPartsName());
                            }
                        } else {
                            ret.put("PARTNAME", "");
                        }
                        //根据托条码的生产计划明细Id得到生产计划明细
                        ProducePlanDetail detail = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                        Consumer consumer2 = mobileService.findById(Consumer.class, detail.getConsumerId());
                        //客户简称
                        ret.put("CONSUMERSIMPLENAME", consumer2.getConsumerSimpleName());
                        ret.put(PRODUCEPLAN, detail);
                        param.clear();
                        param.put("rollBarcode", barCode);
                        List<Roll> list = mobileService.findListByMap(Roll.class, param);
                        // 查询卷信息
                        roll = ListUtils.isEmpty(list) ? null : list.get(0);
                        ret.put(ROLL, roll);
                        // 查不到卷信息
                        if (roll == null) {
                            ret.put(REGISTER, false);
                            ret.put(STOCK, false);
                            ret.put(STATE, -1);
                            // 查不到卷信息，肯定未打包
                            ret.put(PACKED, false);
                            ret.put(OPERATOR, null);
                            ret.put(DEPARTMENT, null);
                        } else {
                            bcode = TopBarcode(barCode);
                            param.clear();
                            param.put("barCode", barCode);
                            List<ProductStockState> psss = mobileService.findListByMap(ProductStockState.class, param);
                            if (bcode.indexOf("T") == 0) {
                                param.clear();
                                param.put("rollBarcode", bcode);
                                tot = mobileService.findUniqueByMap(TotalStatistics.class, param);
                                // 在库状态
                                if (tot == null) {
                                    ret.put(STOCK, -1);
                                } else {
                                    if (tot.getState() != null) {
                                        ret.put(STOCK, tot.getState());
                                    } else {
                                        ret.put(STOCK, 0);
                                    }
                                }
                            } else {
                                // 在库状态
                                ret.put(STOCK, psss.size() == 0 ? -1 : (psss.get(psss.size() - 1).getStockState()));
                            }
                            // 登记状态
                            ret.put(REGISTER, true);
                            if (psss.size() > 0) {
                                ret.put("WARHOUSECODE", psss.get(psss.size() - 1).getWarehouseCode());
                            }
                            // 状态
                            ret.put(STATE, roll.getState());
                            user = mobileService.findById(User.class, roll.getRollUserId());
                            if (user == null) {
                                log("找不到人员信息,ID:" + roll.getRollUserId());
                            }
                            ret.put(OPERATOR, user.getUserName());
                            dept = mobileService.findById(Department.class, user.getDid());
                            if (dept == null) {
                                log("找不到部门信息,ID:" + user.getDid());
                            }
                            ret.put(DEPARTMENT, dept.getName());
                            param.clear();
                            param.put("rollBarcode", barCode);
                            boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
                            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
                            ret.put(PACKED, packedToBox || packedToTray);
                            ret.put(QUALITY, roll.getRollQualityGradeCode());
                            ret.put("MEMO", roll.getMemo() == null ? "" : roll.getMemo());

                            param.clear();
                            param.put("rollBarcode", barCode);
                            TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, param);
                            if (tbr != null) {
                                ret.put(TRAYBARCODE, tbr.getTrayBarcode());
                            } else {
                                ret.put(TRAYBARCODE, "");
                            }
                        }
                        WeavePlan wp = mobileService.findById(WeavePlan.class, rbc.getPlanId());
                        param.clear();
                        if (wp != null) {
                            orderDetail = mobileService.findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
                            order = mobileService.findById(SalesOrder.class, orderDetail.getSalesOrderId());
                        }
                        if (orderDetail == null) {
                            log("找不到订单信息，订单号::" + rbc.getSalesOrderCode());
                        }
                        // 订单信息
                        ret.put(ORDER, orderDetail);
                        consumer = mobileService.findById(Consumer.class, order.getSalesOrderConsumerId());
                        if (consumer == null) {
                            log("找不到客户信息，ID:" + order.getSalesOrderConsumerId());
                        }
                        ret.put(CONSUMER, consumer);
                        p = mobileService.findById(FinishedProduct.class, rbc.getSalesProductId());
                        if (p == null) {
                            log("找不到对应的产品,ID:" + rbc.getSalesProductId());
                        }
                        if (p != null) {
                            WeavePlan wpn = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                            BigDecimal bg;
                            param.clear();
                            param.put("rollBarcode", barCode);
                            Roll rolls = mobileService.findUniqueByMap(Roll.class, param);
                            if (roll != null && (roll.getRollAutoWeight() != null || roll.getRollWeight() != null)) {
                                p.setProductRollWeight(rolls.getRollWeight());
                            } else if (wpn != null && wpn.getSumCount() != null) {
                                ProducePlanDetail pd = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                                ProducePlan pp = mobileService.findById(ProducePlan.class, pd.getProducePlanId());
                                int sumCount = mobileService.isCount1(rbc.getBatchCode(), pp.getProducePlanCode(), pd.getFactoryProductName()).intValue();
                                int z = 0;//第几个二十卷
                                int remainder = 0;
                                if (sumCount != 0) {
                                    z = sumCount / 20;
                                    remainder = sumCount % 20;
                                }
                                if (z == 0) {
                                    if (remainder + 1 > 10) {
                                        WeavePlan w = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                                        if (w.getSumWeight() != null && w.getSumCount() != 0) {
                                            //算平均值
                                            bg = new BigDecimal((w.getSumWeight() - w.getToVoidWeight()) / (w.getSumCount() - w.getToVoid()));
                                            p.setProductRollWeight(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                                        }
                                    }
                                } else {
                                    if (remainder + 1 > 2) {
                                        WeavePlan w = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                                        if (w.getSumWeight() != null && w.getSumCount() != 0) {
                                            //算平均值
                                            bg = new BigDecimal((w.getSumWeight() - w.getToVoidWeight()) / (w.getSumCount() - w.getToVoid()));
                                            p.setProductRollWeight(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                                        }
                                    }
                                }
                            }
                        }
                        // 产品信息
                        ret.put(PRODUCT, p);
                        ret.put(WEAVEPLAN, wp);
                    }
                    ret.put(CUTPLAN, null);
                    ret.put(BOX, null);
                    if (!"".equals(bcode) && bcode.indexOf("T") == 0) {
                        ret.put(TRAY, bcode);
                    } else {
                        ret.put(TRAY, null);
                    }
                    ret.put(CUTPLAN, null);
                    ret.put(ERROR, false);
                    ret.put(MSG, "查询成功");
                } else {
                    // 查询不到卷条码
                    ret.put(ERROR, true);
                    ret.put(MSG, "该条码不存在");
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                ret.put(ERROR, true);
                ret.put(MSG, e.getMessage());
            }
            return GsonTools.toJson(ret);
        }
        // 部件条码
        param.clear();
        param.put("barcode", barCode);
        PartBarcode part = mobileService.findUniqueByMap(PartBarcode.class, param);
        if (barCode.startsWith("P") && part != null) {
            try {
                ProductInRecord productInRecord = productInRecordService.findfirstProductInRecord("barCode", barCode);
                if (productInRecord != null) {
                    ret.put("INTIME", productInRecord.getInTime());
                }
                PartBarcode rbc = part;
                ret.put(CODE, rbc);
                if (rbc != null) {
                    //查询部件下的胚布检验情况
                    Map<String,Object> produceDetailMap = new HashMap<>();
                    produceDetailMap.put("batchCode",rbc.getBatchCode());
                    produceDetailMap.put("partId",rbc.getPartId());
                    List<ProducePlanDetail> producePlanDetails = mobileService.findListByMap(ProducePlanDetail.class, produceDetailMap);
                    ret.put(PRODUCEPLAN, producePlanDetails);
                    SalesOrderDetail salesOrderDetail = mobileService.findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
                    if (null != salesOrderDetail.getMirrorProcBomVersionId()) {
                        //部件名称
                        ret.put("PARTNAME", rbc.getPartName());
                        ProducePlanDetail detail = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                        Consumer consumer2 = mobileService.findById(Consumer.class, detail.getConsumerId());
                        //客户简称
                        ret.put("CONSUMERSIMPLENAME", consumer2.getConsumerSimpleName());
                        param.clear();
                        param.put("partBarcode", barCode);
                        // 查询卷信息
                        roll = mobileService.findUniqueByMap(Roll.class, param);
                        // 查不到部件信息
                        Map<String, Object> partTray = new HashMap<>();
                        if (roll == null) {
                            ret.put(REGISTER, false);
                            ret.put(STOCK, false);
                            ret.put(STATE, -1);
                            ret.put(PACKED, false);
                            ret.put(OPERATOR, null);
                            ret.put(DEPARTMENT, null);
                            partTray = null;
                            if (part.getOldBatchCode() != null) {
                                ret.put(REGISTER, true);
                            }
                        } else {
                            if (roll.getRollDeviceCode() == null) {
                                roll.setRollDeviceCode("");
                            }
                            ret.put(ROLL, roll);
                            param.clear();
                            param.put("barCode", barCode);
                            List<ProductStockState> psss = mobileService.findListByMap(ProductStockState.class, param);
                            // 登记状态
                            ret.put(REGISTER, true);
                            // 在库状态
                            ret.put(STOCK, psss.size() == 0 ? null : (psss.get(psss.size() - 1).getStockState()));
                            if (psss.size() != 0) {
                                //仓库
                                param1.put("WAREHOUSECODE", psss.get(psss.size() - 1).getWarehouseCode());
                                List<Warehouse> warehouseList = mobileService.findListByMap(Warehouse.class, param1);
                                ret.put(WAREHOUSENAME, warehouseList.size() == 0 ? null : (warehouseList.get(warehouseList.size() - 1).getWarehouseName()));
                                //库位
                                param1.clear();
                                param1.put("WAREHOUSEPOSCODE", (psss.get(psss.size() - 1).getWarehousePosCode()));
                                param1.put("WAREHOUSEID", warehouseList.get(warehouseList.size() - 1).getId());
                                List<WarehosePos> warehosePosList = mobileService.findListByMap(WarehosePos.class, param1);
                                ret.put(WAREHOUSEPOSNAME, warehosePosList.size() == 0 ? null : (warehosePosList.get(warehosePosList.size() - 1).getWarehousePosName()));
                            } else {
                                ret.put(WAREHOUSENAME, null);
                                ret.put(WAREHOUSEPOSNAME, null);
                            }
                            // 状态
                            ret.put(STATE, roll.getState());
                            user = mobileService.findById(User.class, roll.getRollUserId());
                            ret.put(OPERATOR, user.getUserName());
                            dept = mobileService.findById(Department.class, user.getDid());
                            ret.put(DEPARTMENT, dept.getName());
                            param.clear();
                            param.put("rollBarcode", barCode);
                            //部件可以直接入成品库
                            ret.put(PACKED, true);
                            ret.put("ENDPACK", true);
                            ret.put(QUALITY, roll.getRollQualityGradeCode());
                            ret.put("MEMO", roll.getMemo() == null ? "" : roll.getMemo());
                            partTray.put("ENDPACK", 1);
                            partTray.put("ROLLQUALITYGRADECODE", roll.getRollQualityGradeCode());
                            partTray.put("WEIGHT", roll.getRollWeight());
                            partTray.put("PACKAGINGTIME", roll.getRollOutputTime());
                            partTray.put("TRAYBARCODE", roll.getPartBarcode());
                        }
                        param.clear();
                        param.put("productId", rbc.getSalesProductId());
                        param.put("salesOrderSubCode", rbc.getSalesOrderCode());
                        param.put("closed", null);
                        List<SalesOrderDetail> list = mobileService.findListByMap(SalesOrderDetail.class, param);
                        if (list.size() > 0) {
                            orderDetail = list.get(0);
                        } else {
                            log("找不到订单信息，订单号:" + rbc.getSalesOrderCode());
                        }
                        order = mobileService.findById(SalesOrder.class, orderDetail.getSalesOrderId());
                        // 订单信息
                        ret.put(ORDER, orderDetail);
                        consumer = mobileService.findById(Consumer.class, order.getSalesOrderConsumerId());
                        ret.put(CONSUMER, consumer);
                        // 查找部件条码
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("barcode", barCode);
                        PartBarcode pb = mobileService.findUniqueByMap(PartBarcode.class, map);
                        SalesOrderDetail salesOrderDetail2 = mobileService.findById(SalesOrderDetail.class, pb.getSalesOrderDetailId());
                        if (pb == null) {
                            ret.put(PRODUCT, null);
                        } else {
                            map2.clear();
                            map2.put("versionPartsId", pb.getPartId());
                            map2.put("salesOrderDetailId", salesOrderDetail2.getId());
                            List<TcBomVersionPartsMirror> partsMirrorList = mobileService.findListByMap(TcBomVersionPartsMirror.class, map2);
                            if (partsMirrorList.size() > 0) {
                                ret.put(PRODUCT, partsMirrorList.get(0));
                            }
                        }
                        map2.clear();
                        map2.put("salesOrderDetailId", salesOrderDetail.getId());
                        map2.put("procBomId", rbc.getMirrorProcBomId());
                        map2.put("productId", rbc.getSalesProductId());
                        List<FinishedProductMirror> finishedProductMirrorList = mobileService.findListByMap(FinishedProductMirror.class, map2);

                        if (finishedProductMirrorList.size() == 0) {
                            map2.clear();
                            map2.put("salesOrderId", salesOrderDetail.getSalesOrderId());
                            map2.put("procBomId", rbc.getMirrorProcBomId());
                            map2.put("productId", rbc.getSalesProductId());
                            finishedProductMirrorList = mobileService.findListByMap(FinishedProductMirror.class, map2);
                        }
                        if (finishedProductMirrorList.size() > 0) {
                            pm1 = finishedProductMirrorList.get(0);
                        }
                        ret.put("FINISHED", pm1);
                        if (null == pm1) {
                            FinishedProduct finishedProduct = mobileService.findById(FinishedProduct.class, rbc.getSalesProductId());
                            ret.put("FINISHED", finishedProduct);
                        }
                        Iplan cp = mobileService.findById(CutPlan.class, rbc.getPlanId());
                        ret.put(WEAVEPLAN, null);
                        if (cp == null) {
                            cp = mobileService.findById(WeavePlan.class, rbc.getPlanId());
                            ret.put(WEAVEPLAN, cp);
                        }
                        // 裁剪计划明细
                        ret.put(CUTPLAN, cp);
                        //ProducePlanDetail ppd = mobileService.findById(ProducePlanDetail.class, cp.getProducePlanDetailId());
                        // 生产计划明细
                        //ret.put(PRODUCEPLAN, ppd);
                        ret.put(BOX, null);
                        ret.put(TRAY, partTray);
                        ret.put(ERROR, false);
                        ret.put(MSG, "查询成功");
                    } else {
                        //部件名称
                        ret.put("PARTNAME", rbc.getPartName());
                        ProducePlanDetail detail = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                        Consumer consumer2 = mobileService.findById(Consumer.class, detail.getConsumerId());
                        //客户简称
                        ret.put("CONSUMERSIMPLENAME", consumer2.getConsumerSimpleName());
                        param.clear();
                        param.put("partBarcode", barCode);
                        // 查询卷信息
                        roll = mobileService.findUniqueByMap(Roll.class, param);
                        // 查不到部件信息
                        Map<String, Object> partTray = new HashMap<>();
                        if (roll == null) {
                            ret.put(REGISTER, false);
                            ret.put(STOCK, false);
                            ret.put(STATE, -1);
                            ret.put(PACKED, false);
                            ret.put(OPERATOR, null);
                            ret.put(DEPARTMENT, null);
                            partTray = null;
                        } else {
                            if (roll.getRollDeviceCode() == null) {
                                roll.setRollDeviceCode("");
                            }
                            ret.put(ROLL, roll);
                            param.clear();
                            param.put("barCode", barCode);
                            List<ProductStockState> psss = mobileService.findListByMap(ProductStockState.class, param);
                            // 登记状态
                            ret.put(REGISTER, true);
                            // 在库状态
                            ret.put(STOCK, psss.size() == 0 ? null : (psss.get(psss.size() - 1).getStockState()));
                            if (psss.size() != 0) {
                                //仓库
                                param1.put("WAREHOUSECODE", psss.get(psss.size() - 1).getWarehouseCode());
                                List<Warehouse> warehouseList = mobileService.findListByMap(Warehouse.class, param1);
                                ret.put(WAREHOUSENAME, warehouseList.size() == 0 ? null : (warehouseList.get(warehouseList.size() - 1).getWarehouseName()));
                                //库位
                                param1.clear();
                                param1.put("WAREHOUSEPOSCODE", (psss.get(psss.size() - 1).getWarehousePosCode()));
                                param1.put("WAREHOUSEID", warehouseList.get(warehouseList.size() - 1).getId());
                                List<WarehosePos> warehosePosList = mobileService.findListByMap(WarehosePos.class, param1);
                                ret.put(WAREHOUSEPOSNAME, warehosePosList.size() == 0 ? null : (warehosePosList.get(warehosePosList.size() - 1).getWarehousePosName()));
                            } else {
                                ret.put(WAREHOUSENAME, null);
                                ret.put(WAREHOUSEPOSNAME, null);
                            }
                            // 状态
                            ret.put(STATE, roll.getState());
                            user = mobileService.findById(User.class, roll.getRollUserId());
                            ret.put(OPERATOR, user.getUserName());
                            dept = mobileService.findById(Department.class, user.getDid());
                            ret.put(DEPARTMENT, dept.getName());
                            param.clear();
                            param.put("rollBarcode", barCode);
                            //部件可以直接入成品库
                            ret.put(PACKED, true);
                            ret.put("ENDPACK", true);
                            ret.put(QUALITY, roll.getRollQualityGradeCode());
                            ret.put("MEMO", roll.getMemo() == null ? "" : roll.getMemo());
                            partTray.put("ENDPACK", 1);
                            partTray.put("ROLLQUALITYGRADECODE", roll.getRollQualityGradeCode());
                            partTray.put("WEIGHT", roll.getRollWeight());
                            partTray.put("PACKAGINGTIME", roll.getRollOutputTime());
                            partTray.put("TRAYBARCODE", roll.getPartBarcode());
                        }
                        param.clear();
                        param.put("productId", rbc.getSalesProductId());
                        param.put("salesOrderSubCode", rbc.getSalesOrderCode());
                        param.put("closed", null);
                        orderDetail = mobileService.findListByMap(SalesOrderDetail.class, param).get(0);
                        order = mobileService.findById(SalesOrder.class, orderDetail.getSalesOrderId());
                        // 订单信息
                        ret.put(ORDER, orderDetail);
                        consumer = mobileService.findById(Consumer.class, order.getSalesOrderConsumerId());
                        ret.put(CONSUMER, consumer);
                        // 查找部件条码
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("barcode", barCode);
                        PartBarcode pb = mobileService.findUniqueByMap(PartBarcode.class, map);
                        if (pb == null) {
                            ret.put(PRODUCT, null);
                        } else {
                            TcBomVersionParts tvp = mobileService.findById(TcBomVersionParts.class, pb.getPartId());
                            ret.put(PRODUCT, tvp);
                        }
                        p = mobileService.findById(FinishedProduct.class, rbc.getSalesProductId());
                        ret.put("FINISHED", p);
                        Iplan cp = mobileService.findById(CutPlan.class, rbc.getPlanId());
                        ret.put(WEAVEPLAN, null);
                        if (cp == null) {
                            cp = mobileService.findById(WeavePlan.class, rbc.getPlanId());
                            ret.put(WEAVEPLAN, cp);
                        }
                        // 裁剪计划明细
                        ret.put(CUTPLAN, cp);
                       // ProducePlanDetail ppd = mobileService.findById(ProducePlanDetail.class, cp.getProducePlanDetailId());
                        // 生产计划明细
                        //ret.put(PRODUCEPLAN, ppd);
                        ret.put(BOX, null);
                        ret.put(TRAY, partTray);
                        ret.put(ERROR, false);
                        ret.put(MSG, "查询成功");
                    }
                } else {
                    // 查询不到卷条码
                    ret.put(ERROR, true);
                    ret.put(MSG, "该条码不存在");
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                ret.put(ERROR, true);
                ret.put(MSG, e.getMessage());
            }
            return GsonTools.toJson(ret);
        }
        // 托条码
        if (barCode.startsWith("T") || barCode.startsWith("P")) {
            try {
                ProductInRecord productInRecord = productInRecordService.findfirstProductInRecord("barCode", barCode);
                if (productInRecord != null) {
                    ret.put("INTIME", productInRecord.getInTime());
                }
                param.clear();
                param.put("barcode", barCode);
                TrayBarCode rbc = mobileService.findUniqueByMap(TrayBarCode.class, param);
                ret.put(CODE, rbc);
                if (rbc != null && rbc.getIsOpened() == 1) {
                    return ajaxError("该条码已被拆包");
                }
                if (null == rbc.getSalesOrderDetailId()) {
                    return ajaxError("该条码还未打包！");
                }
                SalesOrderDetail salesOrderDetail = mobileService.findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
                ProducePlanDetail detail = null;
                if (rbc != null) {
                    if (null != salesOrderDetail.getMirrorProcBomVersionId()) {
                        //部件名称
                        ret.put("PARTNAME", rbc.getPartName());
                        ret.put("BATCHCODE", rbc.getBatchCode());
                        param.clear();
                        param.put("trayBarcode", barCode);
                        // 查询卷信息
                        tray = mobileService.findUniqueByMap(Tray.class, param);
                        if (tray == null) {
                            return ajaxError("该条码尚未使用");
                        }

                        ret.put(TRAY, tray);
                        //根据托条码的生产计划明细Id得到生产计划明细
                        detail = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                        ret.put(PRODUCEPLAN, detail);
                        if (tray.getEndPack().intValue() == 1) {
                            ret.put("ENDPACK", true);
                            Consumer consumer2 = mobileService.findById(Consumer.class, detail.getConsumerId());
                            //客户简称
                            ret.put("CONSUMERSIMPLENAME", consumer2.getConsumerSimpleName());
                        } else {
                            ret.put("ENDPACK", false);
                        }
                        ret.put(PRODUCEPLAN, detail);
                        param.clear();
                        param.put("trayBarcode", barCode);
                        List<TrayBoxRoll> tbrList = mobileService.findListByMap(TrayBoxRoll.class, param);
                        if (tbrList != null && tbrList.size() != 0) {
                            if (tbrList.get(0).getBoxBarcode() != null) {
                                param.clear();
                                param.put("barcode", tbrList.get(0).getBoxBarcode());
                                BoxBarcode bbc = mobileService.findUniqueByMap(BoxBarcode.class, param);
                                SalesOrderDetail sdBox = mobileService.findById(SalesOrderDetail.class, bbc.getSalesOrderDetailId());
                                map2.clear();
                                map2.put("productId", bbc.getSalesProductId());
                                map2.put("procBomId", sdBox.getMirrorProcBomVersionId());
                                map2.put("salesOrderDetailId", salesOrderDetail.getId());
                                List<FinishedProductMirror> finishedProductMirrorList = mobileService.findListByMap(FinishedProductMirror.class, map2);
                                if (finishedProductMirrorList.size() == 0) {
                                    map2.clear();
                                    map2.put("salesOrderId", salesOrderDetail.getSalesOrderId());
                                    map2.put("productId", bbc.getSalesProductId());
                                    map2.put("procBomId", sdBox.getMirrorProcBomVersionId());
                                    finishedProductMirrorList = mobileService.findListByMap(FinishedProductMirror.class, map2);
                                }
                                if (finishedProductMirrorList.size() > 0) {
                                    pm1 = finishedProductMirrorList.get(0);
                                }
                                ret.put(PRODUCT, pm1);
                                if (pm1 == null) {
                                    FinishedProduct product = mobileService.findById(FinishedProduct.class, bbc.getSalesProductId());
                                    ret.put(PRODUCT, product);
                                }
                            } else if (tbrList.get(0).getRollBarcode() != null) {
                                param.clear();
                                param.put("barcode", tbrList.get(0).getRollBarcode());
                                if (tbrList.get(0).getRollBarcode().startsWith("R")) {
                                    RollBarcode _rbc = mobileService.findUniqueByMap(RollBarcode.class, param);
                                    SalesOrderDetail sd = mobileService.findById(SalesOrderDetail.class, _rbc.getSalesOrderDetailId());
                                    map2.clear();
                                    map2.put("productId", _rbc.getSalesProductId());
                                    map2.put("salesOrderDetailId", sd.getId());
                                    List<FinishedProductMirror> finishedProductMirrorList = mobileService.findListByMap(FinishedProductMirror.class, map2);
                                    if (finishedProductMirrorList.size() == 0) {
                                        map2.clear();
                                        map2.put("salesOrderId", sd.getSalesOrderId());
                                        map2.put("productId", _rbc.getSalesProductId());
                                        finishedProductMirrorList = mobileService.findListByMap(FinishedProductMirror.class, map2);
                                    }
                                    if (finishedProductMirrorList.size() > 0) {
                                        pm1 = finishedProductMirrorList.get(0);
                                    }
                                    ret.put(PRODUCT, pm1);
                                    if (pm1 == null) {
                                        FinishedProduct product = mobileService.findById(FinishedProduct.class, _rbc.getSalesProductId());
                                        ret.put(PRODUCT, product);
                                    }
                                } else {
                                    PartBarcode _pbc = mobileService.findUniqueByMap(PartBarcode.class, param);
                                    SalesOrderDetail sdp = mobileService.findById(SalesOrderDetail.class, _pbc.getSalesOrderDetailId());
                                    map2.clear();
                                    map2.put("productId", _pbc.getSalesProductId());
                                    map2.put("procBomId", _pbc.getMirrorProcBomId());
                                    map2.put("salesOrderDetailId", sdp.getId());
                                    List<FinishedProductMirror> finishedProductMirrorList = mobileService.findListByMap(FinishedProductMirror.class, map2);
                                    if (finishedProductMirrorList.size() == 0) {
                                        map2.clear();
                                        map2.put("salesOrderId", sdp.getSalesOrderId());
                                        map2.put("productId", _pbc.getSalesProductId());
                                        map2.put("procBomId", _pbc.getMirrorProcBomId());
                                        finishedProductMirrorList = mobileService.findListByMap(FinishedProductMirror.class, map2);
                                    }
                                    if (finishedProductMirrorList.size() > 0) {
                                        pm1 = finishedProductMirrorList.get(0);
                                    }
                                    ret.put(PRODUCT, pm1);
                                    if (pm1 == null) {
                                        FinishedProduct product = mobileService.findById(FinishedProduct.class, _pbc.getSalesProductId());
                                        ret.put(PRODUCT, product);
                                    }
                                }
                            }
                        }
                        param.clear();
                        param.put("trayBarcode", barCode);
                        // 查询托下面的卷和盒
                        ret.put(ITEMS, mobileService.findListByMap(TrayBoxRoll.class, param));
                        param.clear();
                        param.put("barCode", barCode);
                        pss = mobileService.findUniqueByMap(ProductStockState.class, param);
                        user = mobileService.findById(User.class, tray.getPackagingStaff());
                        ret.put(OPERATOR, user.getUserName());
                        dept = mobileService.findById(Department.class, user.getDid());
                        ret.put(DEPARTMENT, dept.getName());
                        // 登记状态
                        ret.put(REGISTER, true);
                        // 在库状态
                        ret.put(STOCK, pss == null ? null : (pss.getStockState()));
                        if (pss != null) {
                            //仓库
                            param1.put("WAREHOUSECODE", pss.getWarehouseCode());
                            List<Warehouse> warehouseList = mobileService.findListByMap(Warehouse.class, param1);
                            ret.put(WAREHOUSENAME, warehouseList.size() == 0 ? null : (warehouseList.get(warehouseList.size() - 1).getWarehouseName()));
                            //库位
                            param1.clear();
                            param1.put("WAREHOUSEPOSCODE", pss.getWarehousePosCode());
                            param1.put("WAREHOUSEID", warehouseList.get(warehouseList.size() - 1).getId());
                            List<WarehosePos> warehosePosList = mobileService.findListByMap(WarehosePos.class, param1);
                            ret.put(WAREHOUSEPOSNAME, warehosePosList.size() == 0 ? null : (warehosePosList.get(warehouseList.size() - 1).getWarehousePosName()));
                        } else {
                            ret.put(WAREHOUSENAME, null);
                            ret.put(WAREHOUSEPOSNAME, null);
                        }
                        // 状态
                        ret.put(STATE, totalStatisticsService.isFrozen(barCode));
                        ret.put(QUALITY, tray.getRollQualityGradeCode());
                        ret.put("MEMO", tray.getMemo() == null ? "" : tray.getMemo());
                        ret.put(PACKED, false);
                        // 箱子里面的计划可能是编织或者裁剪计划,都查询出来
                        // 编织计划
                        WeavePlan wp = barcodeService.getWeavePlan(barCode);
                        ret.put(WEAVEPLAN, wp);
                        // 裁剪计划明细
                        CutPlan cp = barcodeService.getCutPlan(barCode);
                        ret.put(CUTPLAN, cp);
                        orderDetail = barcodeService.getSalesOrderDetail(barCode);
                        // 订单信息
                        ret.put(ORDER, orderDetail);
                        if (orderDetail != null) {
                            order = mobileService.findById(SalesOrder.class, orderDetail.getSalesOrderId());
                            consumer = mobileService.findById(Consumer.class, order.getSalesOrderConsumerId());
                        }
                        ret.put(CONSUMER, consumer);
                        TurnBagPlan tbp = null;
                        if (rbc.getPlanId() < 0) {
                            tbp = mobileService.findById(TurnBagPlan.class, Math.abs(rbc.getPlanId()));
                            ret.put(WEAVEPLAN, tbp);
                            ret.put(CUTPLAN, tbp);
                        }
                        String orderCnt = "0";
                        String strockCnt = "0";
                        String packageCnt = "0";
                        if (detail != null) {
                            orderCnt = mobileService.getOrderCnt(detail.getSalesOrderCode(), detail.getBatchCode());
                            strockCnt = mobileService.getStrockCnt(detail.getSalesOrderCode(), detail.getBatchCode(), detail.getFactoryProductName(), rbc.getPartName());
                            packageCnt = mobileService.getPackageCnt(detail.getSalesOrderCode(), detail.getBatchCode());
                        }
                        ret.put("orderCnt", orderCnt);
                        ret.put("strockCnt", strockCnt);
                        ret.put("packageCnt", packageCnt);
                        ret.put(ROLL, null);
                        ret.put(BOX, null);
                        ret.put(ERROR, false);
                        ret.put(MSG, "查询成功");
                    } else {
                        //部件名称
                        ret.put("PARTNAME", rbc.getPartName());
                        ret.put("BATCHCODE", rbc.getBatchCode());
                        param.clear();
                        param.put("trayBarcode", barCode);
                        // 查询卷信息
                        tray = mobileService.findUniqueByMap(Tray.class, param);
                        if (tray == null) {
                            return ajaxError("该条码尚未使用");
                        }
                        ret.put(TRAY, tray);
                        //根据托条码的生产计划明细Id得到生产计划明细
                        detail = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                        if (tray.getEndPack().intValue() == 1) {
                            ret.put("ENDPACK", true);
                            Consumer consumer2 = mobileService.findById(Consumer.class, detail.getConsumerId());
                            //客户简称
                            ret.put("CONSUMERSIMPLENAME", consumer2.getConsumerSimpleName());
                        } else {
                            ret.put("ENDPACK", false);
                        }
                        ret.put(PRODUCEPLAN, detail);
                        param.clear();
                        param.put("trayBarcode", barCode);
                        List<TrayBoxRoll> tbrList = mobileService.findListByMap(TrayBoxRoll.class, param);
                        if (tbrList != null && tbrList.size() != 0) {
                            if (tbrList.get(0).getBoxBarcode() != null) {
                                param.clear();
                                param.put("barcode", tbrList.get(0).getBoxBarcode());
                                BoxBarcode bbc = mobileService.findUniqueByMap(BoxBarcode.class, param);
                                p = mobileService.findById(FinishedProduct.class, bbc.getSalesProductId());
                                ret.put(PRODUCT, p);
                            } else if (tbrList.get(0).getRollBarcode() != null) {
                                param.clear();
                                param.put("barcode", tbrList.get(0).getRollBarcode());
                                if (tbrList.get(0).getRollBarcode().startsWith("R")) {
                                    RollBarcode _rbc = mobileService.findUniqueByMap(RollBarcode.class, param);
                                    p = mobileService.findById(FinishedProduct.class, _rbc.getSalesProductId());
                                } else {
                                    PartBarcode _pbc = mobileService.findUniqueByMap(PartBarcode.class, param);
                                    p = mobileService.findById(FinishedProduct.class, _pbc.getSalesProductId());
                                }
                                ret.put(PRODUCT, p);
                            }
                        }
                        param.clear();
                        param.put("trayBarcode", barCode);
                        // 查询托下面的卷和盒
                        ret.put(ITEMS, mobileService.findListByMap(TrayBoxRoll.class, param));
                        param.clear();
                        param.put("barCode", barCode);
                        pss = mobileService.findUniqueByMap(ProductStockState.class, param);
                        user = mobileService.findById(User.class, tray.getPackagingStaff());
                        ret.put(OPERATOR, user.getUserName());
                        dept = mobileService.findById(Department.class, user.getDid());
                        ret.put(DEPARTMENT, dept.getName());
                        // 登记状态
                        ret.put(REGISTER, true);
                        // 在库状态
                        ret.put(STOCK, pss == null ? null : (pss.getStockState()));
                        if (pss != null) {
                            //仓库
                            param1.put("WAREHOUSECODE", pss.getWarehouseCode());
                            List<Warehouse> warehouseList = mobileService.findListByMap(Warehouse.class, param1);
                            ret.put(WAREHOUSENAME, warehouseList.size() == 0 ? null : (warehouseList.get(warehouseList.size() - 1).getWarehouseName()));
                            //库位
                            param1.clear();
                            param1.put("WAREHOUSEPOSCODE", pss.getWarehousePosCode());
                            param1.put("WAREHOUSEID", warehouseList.get(warehouseList.size() - 1).getId());
                            List<WarehosePos> warehosePosList = mobileService.findListByMap(WarehosePos.class, param1);
                            ret.put(WAREHOUSEPOSNAME, warehosePosList.size() == 0 ? null : (warehosePosList.get(warehouseList.size() - 1).getWarehousePosName()));
                        } else {
                            ret.put(WAREHOUSENAME, null);
                            ret.put(WAREHOUSEPOSNAME, null);
                        }
                        // 状态
                        ret.put(STATE, totalStatisticsService.isFrozen(barCode));
                        ret.put(QUALITY, tray.getRollQualityGradeCode());
                        ret.put("MEMO", tray.getMemo() == null ? "" : tray.getMemo());
                        ret.put(PACKED, false);
                        // 箱子里面的计划可能是编织或者裁剪计划,都查询出来
                        // 编织计划
                        WeavePlan wp = barcodeService.getWeavePlan(barCode);
                        ret.put(WEAVEPLAN, wp);
                        // 裁剪计划明细
                        CutPlan cp = barcodeService.getCutPlan(barCode);
                        ret.put(CUTPLAN, cp);
                        orderDetail = barcodeService.getSalesOrderDetail(barCode);
                        // 订单信息
                        ret.put(ORDER, orderDetail);
                        if (orderDetail != null) {
                            order = mobileService.findById(SalesOrder.class, orderDetail.getSalesOrderId());
                            consumer = mobileService.findById(Consumer.class, order.getSalesOrderConsumerId());
                        }
                        ret.put(CONSUMER, consumer);
                        TurnBagPlan tbp = null;
                        if (rbc.getPlanId() < 0) {
                            tbp = mobileService.findById(TurnBagPlan.class, Math.abs(rbc.getPlanId()));
                            ret.put(WEAVEPLAN, tbp);
                            ret.put(CUTPLAN, tbp);
                        }
                        String orderCnt = "0";
                        String strockCnt = "0";
                        String packageCnt = "0";
                        if (detail != null) {
                            orderCnt = mobileService.getOrderCnt(detail.getSalesOrderCode(), detail.getBatchCode());
                            strockCnt = mobileService.getStrockCnt(detail.getSalesOrderCode(), detail.getBatchCode(), detail.getFactoryProductName(), rbc.getPartName());
                            packageCnt = mobileService.getPackageCnt(detail.getSalesOrderCode(), detail.getBatchCode());
                        }
                        ret.put("orderCnt", orderCnt);
                        ret.put("strockCnt", strockCnt);
                        ret.put("packageCnt", packageCnt);
                        ret.put(ROLL, null);
                        ret.put(BOX, null);
                        ret.put(ERROR, false);
                        ret.put(MSG, "查询成功");
                    }
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                ret.put(ERROR, true);
                ret.put(MSG, e.getMessage());
            }
            return GsonTools.toJson(ret);
        }

        // 箱条码
        if (barCode.startsWith("B")) {
            try {
                param.clear();
                param.put("barcode", barCode);
                BoxBarcode rbc = mobileService.findUniqueByMap(BoxBarcode.class, param);
                SalesOrderDetail salesOrderDetail = mobileService.findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
                ret.put(CODE, rbc);
                if (rbc != null) {
                    if (null != salesOrderDetail.getMirrorProcBomVersionId()) {
                        //部件名称
                        ret.put("PARTNAME", rbc.getPartName());
                        param.clear();
                        param.put("boxBarcode", barCode);
                        // 查询箱信息
                        box = mobileService.findUniqueByMap(Box.class, param);
                        if (box == null) {
                            return ajaxError("该条码尚未使用");
                        }
                        ret.put(BOX, box);
                        // 查不到箱信息
                        if (box == null) {
                            ret.put(REGISTER, false);
                            ret.put(STOCK, false);
                            ret.put(STATE, -1);
                            ret.put(PACKED, false);
                            ret.put(OPERATOR, null);
                            ret.put(DEPARTMENT, null);
                            ret.put(ITEMS, null);
                        } else {
                            param.clear();
                            param.put("boxBarcode", barCode);
                            ret.put(ITEMS, mobileService.findListByMap(BoxRoll.class, param));
                            param.clear();
                            param.put("barCode", barCode);
                            List<ProductStockState> psss = mobileService.findListByMap(ProductStockState.class, param);
                            // 登记状态
                            ret.put(REGISTER, true);
                            // 在库状态
                            ret.put(STOCK, psss.size() == 0 ? -1 : (psss.get(psss.size() - 1).getStockState()));
                            // 状态
                            ret.put(STATE, totalStatisticsService.isFrozen(barCode));
                            user = mobileService.findById(User.class, box.getPackagingStaff());
                            if (user == null) {
                                log("找不到人员信息,ID:" + box.getPackagingStaff());
                            }
                            ret.put(OPERATOR, user.getUserName());
                            dept = mobileService.findById(Department.class, user.getDid());
                            if (dept == null) {
                                log("找不到部门信息,ID:" + user.getDid());
                            }
                            ret.put(DEPARTMENT, dept.getName());
                            param.clear();
                            param.put("boxBarcode", barCode);
                            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
                            ret.put(PACKED, packedToTray);
                            ret.put(QUALITY, box.getRollQualityGradeCode());
                            ret.put("MEMO", box.getMemo() == null ? "" : box.getMemo());
                        }
                        // 箱子里面的计划可能是编织或者裁剪计划,都查询出来
                        // 编织计划
                        WeavePlan wp = barcodeService.getWeavePlan(barCode);
                        ret.put(WEAVEPLAN, wp);
                        // 裁剪计划明细
                        CutPlan cp = barcodeService.getCutPlan(barCode);
                        ret.put(CUTPLAN, cp);
                        orderDetail = barcodeService.getSalesOrderDetail(barCode);
                        ret.put(ORDER, orderDetail);
                        if (orderDetail != null) {
                            order = mobileService.findById(SalesOrder.class, orderDetail.getSalesOrderId());
                            consumer = mobileService.findById(Consumer.class, order.getSalesOrderConsumerId());
                        }
                        if (consumer == null) {
                            log("找不到客户信息，ID:" + order.getSalesOrderConsumerId());
                        }
                        ret.put(CONSUMER, consumer);
                        List<FinishedProductMirror> list1;
                        map2.clear();
                        map2.put("salesOrderDetailId", salesOrderDetail.getId());
                        map2.put("productId", rbc.getSalesProductId());
                        list1 = mobileService.findListByMap(FinishedProductMirror.class, map2);
                        if (list1.size() > 0) {
                            pm1 = list1.get(0);
                        }
                        if (list1.size() == 0) {
                            map2.clear();
                            map2.put("salesOrderId", salesOrderDetail.getSalesOrderId());
                            map2.put("productId", rbc.getSalesProductId());
                            list1 = mobileService.findListByMap(FinishedProductMirror.class, map2);
                            if (list1.size() > 0) {
                                pm1 = list1.get(0);
                            }
                        }
                        if (pm1 == null) {
                            log("找不到对应的产品,ID:" + rbc.getSalesProductId());
                        }
                        // 产品信息
                        ret.put(PRODUCT, pm1);
                        TurnBagPlan tbp = null;
                        if (rbc.getPlanId() < 0) {
                            tbp = mobileService.findById(TurnBagPlan.class, Math.abs(rbc.getPlanId()));
                        }
                        ret.put(TURNBAGPLAN, tbp);
                        ret.put(ROLL, null);
                        ret.put(TRAY, null);
                        ret.put(ERROR, false);
                        ret.put(MSG, "查询成功");
                    } else {
                        //部件名称
                        ret.put("PARTNAME", rbc.getPartName());
                        param.clear();
                        param.put("boxBarcode", barCode);
                        // 查询箱信息
                        box = mobileService.findUniqueByMap(Box.class, param);
                        if (box == null) {
                            return ajaxError("该条码尚未使用");
                        }
                        ret.put(BOX, box);
                        // 查不到箱信息
                        if (box == null) {
                            ret.put(REGISTER, false);
                            ret.put(STOCK, false);
                            ret.put(STATE, -1);
                            ret.put(PACKED, false);
                            ret.put(OPERATOR, null);
                            ret.put(DEPARTMENT, null);
                            ret.put(ITEMS, null);
                        } else {
                            param.clear();
                            param.put("boxBarcode", barCode);
                            ret.put(ITEMS, mobileService.findListByMap(BoxRoll.class, param));
                            param.clear();
                            param.put("barCode", barCode);
                            List<ProductStockState> psss = mobileService.findListByMap(ProductStockState.class, param);
                            // 登记状态
                            ret.put(REGISTER, true);
                            // 在库状态
                            ret.put(STOCK, psss.size() == 0 ? -1 : (psss.get(psss.size() - 1).getStockState()));
                            // 状态
                            ret.put(STATE, totalStatisticsService.isFrozen(barCode));
                            user = mobileService.findById(User.class, box.getPackagingStaff());
                            if (user == null) {
                                log("找不到人员信息,ID:" + box.getPackagingStaff());
                            }
                            ret.put(OPERATOR, user.getUserName());
                            dept = mobileService.findById(Department.class, user.getDid());
                            if (dept == null) {
                                log("找不到部门信息,ID:" + user.getDid());
                            }
                            ret.put(DEPARTMENT, dept.getName());
                            param.clear();
                            param.put("boxBarcode", barCode);
                            boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
                            ret.put(PACKED, packedToTray);
                            ret.put(QUALITY, box.getRollQualityGradeCode());
                            ret.put("MEMO", box.getMemo() == null ? "" : box.getMemo());
                        }
                        // 箱子里面的计划可能是编织或者裁剪计划,都查询出来
                        // 编织计划
                        WeavePlan wp = barcodeService.getWeavePlan(barCode);
                        ret.put(WEAVEPLAN, wp);
                        // 裁剪计划明细
                        CutPlan cp = barcodeService.getCutPlan(barCode);
                        ret.put(CUTPLAN, cp);
                        orderDetail = barcodeService.getSalesOrderDetail(barCode);
                        ret.put(ORDER, orderDetail);
                        if (orderDetail != null) {
                            order = mobileService.findById(SalesOrder.class, orderDetail.getSalesOrderId());
                            consumer = mobileService.findById(Consumer.class, order.getSalesOrderConsumerId());
                        }
                        if (consumer == null) {
                            log("找不到客户信息，ID:" + order.getSalesOrderConsumerId());
                        }
                        ret.put(CONSUMER, consumer);
                        p = mobileService.findById(FinishedProduct.class, rbc.getSalesProductId());
                        if (p == null) {
                            log("找不到对应的产品,ID:" + rbc.getSalesProductId());
                        }
                        // 产品信息
                        ret.put(PRODUCT, p);
                        TurnBagPlan tbp = null;
                        if (rbc.getPlanId() < 0) {
                            tbp = mobileService.findById(TurnBagPlan.class, Math.abs(rbc.getPlanId()));
                        }
                        ret.put(TURNBAGPLAN, tbp);
                        ret.put(ROLL, null);
                        ret.put(TRAY, null);
                        ret.put(ERROR, false);
                        ret.put(MSG, "查询成功");
                    }
                } else {
                    // 查询不到卷条码
                    ret.put(ERROR, true);
                    ret.put(MSG, "该条码不存在");
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                ret.put(ERROR, true);
                ret.put(MSG, e.getMessage());
            }
            return GsonTools.toJson(ret);
        }

        ret.put(ERROR, true);
        ret.put(MSG, "未识别的条码号");
        return GsonTools.toJson(ret);
    }

    @SuppressWarnings("unused")
    @NoLogin
    @Journal(name = "(仅限胚布领料使用)根据卷、箱、托条码查询产品信息和订单信息,客户信息，以及状态信息")
    @RequestMapping("infos1")
    public String infos1(String barCode) {
        String PRODUCT = "PRODUCT", //
                ORDER = "ORDER", //
                WEAVEPLAN = "WP", //
                PACKED = "PACKED", CUTPLAN = "CP", //
                CONSUMER = "CONSUMER", ROLL = "ROLL", //
                ITEMS = "ITEMS", BOX = "BOX", //
                TRAY = "TRAY", //
                STOCK = "STOCK", //
                STATE = "STATE", //
                REGISTER = "REGISTER", //
                CODE = "CODE", ERROR = "ERROR", //
                OPERATOR = "OPERATOR", DEPARTMENT = "DEPARTMENT", QUALITY = "QUALITY", // 质量等级
                MSG = "MSG";//

        FinishedProduct p;
        SalesOrderDetail orderDetail = null;
        SalesOrder order = null;
        Roll roll;
        Tray tray;
        ProductStockState pss = null;
        Consumer consumer = null;
        TotalStatistics tot = null;
        User user;
        Department dept;

        Map<String, Object> ret = new HashMap();
        Map<String, Object> param = new HashMap();
        // 卷条码
        if (barCode.startsWith("R")) {
            try {
                param.clear();
                param.put("barcode", barCode);
                RollBarcode rbc = mobileService.findUniqueByMap(RollBarcode.class, param);
                ret.put(CODE, rbc);
                if (rbc != null) {
                    param.clear();
                    param.put("rollBarcode", barCode);
                    List<Roll> list = mobileService.findListByMap(Roll.class, param);
                    // 查询卷信息
                    roll = ListUtils.isEmpty(list) ? null : list.get(0);
                    ret.put(ROLL, roll);
                    // 查不到卷信息
                    if (roll == null) {
                        ret.put(REGISTER, false);
                        ret.put(STOCK, false);
                        ret.put(STATE, -1);
                        // 查不到卷信息，肯定未打包
                        ret.put(PACKED, false);
                        ret.put(OPERATOR, null);
                        ret.put(DEPARTMENT, null);
                    } else {
                        String bcode = TopBarcode(barCode);
                        if (!bcode.equals(barCode)) {
                            ret.put(STOCK, 3);
                        } else {
                            param.clear();
                            param.put("barCode", barCode);
                            List<ProductStockState> psss = mobileService.findListByMap(ProductStockState.class, param);
                            // 在库状态
                            ret.put(STOCK, psss.size() > 0 ? -1 : (psss.get(psss.size() - 1).getStockState()));
                        }
                        // 登记状态
                        ret.put(REGISTER, true);
                        if (pss != null) {
                            ret.put("WARHOUSECODE", pss.getWarehouseCode());
                        }
                        // 状态
                        ret.put(STATE, roll.getState());
                        user = mobileService.findById(User.class, roll.getRollUserId());
                        if (user == null) {
                            log("找不到人员信息,ID:" + roll.getRollUserId());
                        }
                        ret.put(OPERATOR, user.getUserName());
                        dept = mobileService.findById(Department.class, user.getDid());
                        if (dept == null) {
                            log("找不到部门信息,ID:" + user.getDid());
                        }
                        ret.put(DEPARTMENT, dept.getName());
                        param.clear();
                        param.put("rollBarcode", barCode);
                        boolean packedToBox = mobileService.isExist(BoxRoll.class, param, true);
                        boolean packedToTray = mobileService.isExist(TrayBoxRoll.class, param, true);
                        ret.put(PACKED, packedToBox || packedToTray);
                        ret.put(QUALITY, roll.getRollQualityGradeCode());
                        ret.put("MEMO", roll.getMemo() == null ? "" : roll.getMemo());
                    }
                    WeavePlan wp = mobileService.findById(WeavePlan.class, rbc.getPlanId());

                    param.clear();
                    if (wp != null) {
                        orderDetail = mobileService.findById(SalesOrderDetail.class, wp.getFromSalesOrderDetailId());
                        order = mobileService.findById(SalesOrder.class, orderDetail.getSalesOrderId());
                    }
                    if (orderDetail == null) {
                        log("找不到订单信息，订单号::" + rbc.getSalesOrderCode());
                    }
                    // 订单信息
                    ret.put(ORDER, orderDetail);
                    consumer = mobileService.findById(Consumer.class, order.getSalesOrderConsumerId());
                    if (consumer == null) {
                        log("找不到客户信息，ID:" + order.getSalesOrderConsumerId());
                    }
                    ret.put(CONSUMER, consumer);
                    p = mobileService.findById(FinishedProduct.class, rbc.getSalesProductId());
                    if (p == null) {
                        log("找不到对应的产品,ID:" + rbc.getSalesProductId());
                    }

                    if (p != null) {
                        WeavePlan wpn = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                        BigDecimal bg;
                        param.clear();
                        param.put("rollBarcode", barCode);
                        Roll rolls = mobileService.findUniqueByMap(Roll.class, param);
                        if (roll != null && (roll.getRollAutoWeight() != null || roll.getRollWeight() != null)) {
                            p.setProductRollWeight(rolls.getRollWeight());
                        } else if (wpn != null && wpn.getSumCount() != null) {
                            ProducePlanDetail pd = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                            ProducePlan pp = mobileService.findById(ProducePlan.class, pd.getProducePlanId());
                            int sumCount = mobileService.isCount1(rbc.getBatchCode(), pp.getProducePlanCode(), pd.getFactoryProductName()).intValue();
                            int z = 0;//第几个二十卷
                            int remainder = 0;
                            if (sumCount != 0) {
                                z = sumCount / 20;
                                remainder = sumCount % 20;
                            }

                            if (z == 0) {
                                if (remainder + 1 > 10) {
                                    WeavePlan w = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                                    if (w.getSumWeight() != null && w.getSumCount() != 0) {
                                        //算平均值
                                        bg = new BigDecimal((w.getSumWeight() - w.getToVoidWeight()) / (w.getSumCount() - w.getToVoid()));
                                        p.setProductRollWeight(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                                    }
                                }
                            } else {
                                if (remainder + 1 > 2) {
                                    WeavePlan w = weavePlanService.findById(WeavePlan.class, rbc.getPlanId());
                                    if (w.getSumWeight() != null && w.getSumCount() != 0) {
                                        //算平均值
                                        bg = new BigDecimal((w.getSumWeight() - w.getToVoidWeight()) / (w.getSumCount() - w.getToVoid()));
                                        p.setProductRollWeight(bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                                    }
                                }
                            }
                        }
                    }
                    // 产品信息
                    ret.put(PRODUCT, p);
                    ret.put(WEAVEPLAN, wp);
                    ret.put(CUTPLAN, null);
                    ret.put(BOX, null);
                    ret.put(TRAY, null);
                    ret.put(CUTPLAN, null);
                    ret.put(ERROR, false);
                    ret.put(MSG, "查询成功");
                } else {
                    // 查询不到卷条码
                    ret.put(ERROR, true);
                    ret.put(MSG, "该条码不存在");
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                ret.put(ERROR, true);
                ret.put(MSG, e.getMessage());
            }
            return GsonTools.toJson(ret);
        }
        // 托条码
        if (barCode.startsWith("T") || barCode.startsWith("P")) {
            try {
                param.clear();
                param.put("barcode", barCode);
                TrayBarCode rbc = mobileService.findUniqueByMap(TrayBarCode.class, param);
                ret.put(CODE, rbc);
                if (rbc != null && rbc.getIsOpened() == 1) {
                    return ajaxError("该条码以被拆包");
                }
                if (rbc != null) {
                    param.clear();
                    param.put("trayBarcode", barCode);
                    // 查询卷信息
                    tray = mobileService.findUniqueByMap(Tray.class, param);
                    if (tray == null) {
                        return ajaxError("该条码尚未使用");
                    }
                    ret.put(TRAY, tray);
                    // 查不到托信息
                    if (tray == null) {
                        ret.put(REGISTER, false);
                        ret.put(STOCK, false);
                        ret.put(STATE, -1);
                        ret.put(OPERATOR, null);
                        ret.put(DEPARTMENT, null);
                        ret.put(PRODUCT, null);
                    } else {
                        param.clear();
                        param.put("trayBarcode", barCode);
                        List<TrayBoxRoll> tbrList = mobileService.findListByMap(TrayBoxRoll.class, param);
                        if (tbrList != null && tbrList.size() != 0) {
                            if (tbrList.get(0).getBoxBarcode() != null) {
                                param.clear();
                                param.put("barcode", tbrList.get(0).getBoxBarcode());
                                BoxBarcode bbc = mobileService.findUniqueByMap(BoxBarcode.class, param);
                                p = mobileService.findById(FinishedProduct.class, bbc.getSalesProductId());
                                ret.put(PRODUCT, p);
                            } else if (tbrList.get(0).getRollBarcode() != null) {
                                param.clear();
                                param.put("barcode", tbrList.get(0).getRollBarcode());
                                if (tbrList.get(0).getRollBarcode().startsWith("R")) {
                                    RollBarcode _rbc = mobileService.findUniqueByMap(RollBarcode.class, param);
                                    p = mobileService.findById(FinishedProduct.class, _rbc.getSalesProductId());
                                } else {
                                    PartBarcode _pbc = mobileService.findUniqueByMap(PartBarcode.class, param);
                                    p = mobileService.findById(FinishedProduct.class, _pbc.getSalesProductId());
                                }
                                ret.put(PRODUCT, p);
                            }
                        }
                        param.clear();
                        param.put("trayBarcode", barCode);
                        // 查询托下面的卷和盒
                        ret.put(ITEMS, mobileService.findListByMap(TrayBoxRoll.class, param));

                        param.clear();
                        param.put("barCode", barCode);
                        pss = mobileService.findUniqueByMap(ProductStockState.class, param);

                        user = mobileService.findById(User.class, tray.getPackagingStaff());
                        if (user == null) {
                            log("找不到人员信息,ID:" + tray.getPackagingStaff());
                        }
                        ret.put(OPERATOR, user.getUserName());
                        dept = mobileService.findById(Department.class, user.getDid());
                        if (dept == null) {
                            log("找不到部门信息,ID:" + user.getDid());
                        }
                        ret.put(DEPARTMENT, dept.getName());
                        // 登记状态
                        ret.put(REGISTER, true);
                        // 在库状态
                        ret.put(STOCK, pss == null ? -1 : (pss.getStockState()));
                        // 状态
                        ret.put(STATE, totalStatisticsService.isFrozen(barCode));
                        ret.put(QUALITY, tray.getRollQualityGradeCode());
                        ret.put("MEMO", tray.getMemo() == null ? "" : tray.getMemo());
                    }

                    ret.put(PACKED, false);
                    // 箱子里面的计划可能是编织或者裁剪计划,都查询出来
                    // 编织计划
                    WeavePlan wp = barcodeService.getWeavePlan(barCode);
                    ret.put(WEAVEPLAN, wp);
                    // 裁剪计划明细
                    CutPlan cp = barcodeService.getCutPlan(barCode);
                    ret.put(CUTPLAN, cp);
                    orderDetail = barcodeService.getSalesOrderDetail(barCode);

                    // 订单信息
                    ret.put(ORDER, orderDetail);
                    if (orderDetail != null) {
                        order = mobileService.findById(SalesOrder.class, orderDetail.getSalesOrderId());
                        consumer = mobileService.findById(Consumer.class, order.getSalesOrderConsumerId());
                    }

                    if (consumer == null) {
                        log("找不到客户信息，ID:" + order.getSalesOrderConsumerId());
                    }
                    ret.put(CONSUMER, consumer);
                    p = mobileService.findById(FinishedProduct.class, rbc.getSalesProductId());
                    if (p == null) {
                        log("找不到对应的产品,ID:" + rbc.getSalesProductId());
                    }
                    // 产品信息
                    ret.put(PRODUCT, p);

                    TurnBagPlan tbp;
                    if (rbc.getPlanId() < 0) {
                        tbp = mobileService.findById(TurnBagPlan.class, Math.abs(rbc.getPlanId()));
                        ret.put(WEAVEPLAN, tbp);
                        ret.put(CUTPLAN, tbp);
                    }
                    ret.put(ROLL, null);
                    ret.put(BOX, null);
                    ret.put(ERROR, false);
                    ret.put(MSG, "查询成功");
                } else {
                    // 查询不到卷条码
                    ret.put(ERROR, true);
                    ret.put(MSG, "该条码不存在");
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                ret.put(ERROR, true);
                ret.put(MSG, e.getMessage());
            }
            return GsonTools.toJson(ret);
        }

        ret.put(ERROR, true);
        ret.put(MSG, "未识别的条码号");
        return GsonTools.toJson(ret);
    }

    public void log(String msg) throws Exception {
        log.error(msg);
        ExceptionMessage e = new ExceptionMessage();
        e.setClazz(MobileCommonController.class.getName());
        e.setLineNumber(0);
        e.setMethod("infos");
        e.setOccurDate(new Date());
        e.setMsg(msg);
        exceptionService.save(e);
        throw new Exception(msg);
    }

    @NoLogin
    @Journal(name = "查询条码的库存状态")
    @RequestMapping("state")
    public String findCodeState(String barCode, String materialCode) throws Exception {
        return GsonTools.toJson(productStockService.findStateByCode(barCode, materialCode));
    }

    @NoLogin
    @Journal(name = "获取产品树状图列表信息")
    @RequestMapping("trayTree")
    public String listProduct(String barcode) {
        String result = "";
        HashMap<String, Object> map1 = new HashMap();
        map1.put("barcode", barcode);
        if (barcode.startsWith("R")) {
            if (!totalStatisticsService.isExist(RollBarcode.class, map1)) {
                return ajaxError("未查询到该条码");
            }
        } else if (barcode.startsWith("B")) {
            if (!totalStatisticsService.isExist(BoxBarcode.class, map1)) {
                return ajaxError("未查询到该条码");
            }
        } else if (barcode.startsWith("P")) {
            if (!totalStatisticsService.isExist(PartBarcode.class, map1)) {
                return ajaxError("未查询到该条码");
            }
        } else if (barcode.startsWith("T")) {
            if (!totalStatisticsService.isExist(TrayBarCode.class, map1)) {
                return ajaxError("未查询到该条码");
            }
        } else {
            return ajaxError("请输入正确条码");
        }
        // 最顶级的条码，默认为当前查询条码，如果有父级条码，改为父级条码，用于最后查询拼装树状结构
        String topBarcode = barcode;
        // 根据条码查询托箱卷关系组织树状关系
        // 如果条码号R/P开头，查询卷箱关系中是否存在箱，如果有箱，查询箱条码的是否有父级托关系，如果没有箱，查询是否在托中，如果都没有，直接返回卷条码
        if (barcode.startsWith("R")) {
            HashMap<String, Object> map = new HashMap();
            map.put("rollBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = totalStatisticsService.findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            if (br != null) {
                map.clear();
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                map.clear();
                map.put("rollBarcode", barcode);
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                }
            }
        } else if (barcode.startsWith("P")) {
            HashMap<String, Object> map = new HashMap();
            map.put("partBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = totalStatisticsService.findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            map.clear();
            if (br != null) {
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                map.put("partBarcode", barcode);
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                }
            }
        } else if (barcode.startsWith("B")) {
            HashMap<String, Object> map = new HashMap();
            map.put("boxBarcode", barcode);
            TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
            if (tbr != null) {
                topBarcode = tbr.getTrayBarcode();
            }
        }

        // 如果顶级条码号B开头，查询卷箱关系补充卷条码在树状结构
        if (topBarcode.startsWith("B")) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("boxBarcode", topBarcode);
            List<BoxRoll> boxrollList = totalStatisticsService.findListByMap(BoxRoll.class, map);
            ProductInfoTreeStruct pdInfoStruct = new ProductInfoTreeStruct();
            pdInfoStruct.setId(topBarcode);
            pdInfoStruct.setText(topBarcode);
            pdInfoStruct.setState("closed");
            List<ProductInfoTreeStruct> childrenList = new ArrayList<ProductInfoTreeStruct>();
            for (BoxRoll br : boxrollList) {
                ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                pdInfo.setId(br.getRollBarcode());
                pdInfo.setText(br.getRollBarcode());
                pdInfo.setState("closed");
                childrenList.add(pdInfo);
            }
            pdInfoStruct.setChildren(childrenList);
            result = "[" + GsonTools.toJson(pdInfoStruct) + "]";
        }
        // 如果顶级条码号T开头，查询托箱卷关系，补充箱条码和卷条码和箱条码所包含的卷条码信息，组织树状结构
        else if (topBarcode.startsWith("T")) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("trayBarcode", topBarcode);
            List<TrayBoxRoll> tbrList = totalStatisticsService.findListByMap(TrayBoxRoll.class, map);
            ProductInfoTreeStruct pdInfoStruct = new ProductInfoTreeStruct();
            pdInfoStruct.setId(topBarcode);
            pdInfoStruct.setText(topBarcode);
            pdInfoStruct.setState("closed");
            List<ProductInfoTreeStruct> childrenList = new ArrayList<ProductInfoTreeStruct>();
            for (TrayBoxRoll tbr : tbrList) {
                if (tbr.getRollBarcode() != null) {
                    ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                    pdInfo.setId(tbr.getRollBarcode());
                    pdInfo.setText(tbr.getRollBarcode());
                    pdInfo.setState("closed");
                    childrenList.add(pdInfo);
                } else {
                    ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
                    pdInfo.setId(tbr.getBoxBarcode());
                    pdInfo.setText(tbr.getBoxBarcode());
                    pdInfo.setState("closed");
                    List<ProductInfoTreeStruct> boxchildrenList = new ArrayList<ProductInfoTreeStruct>();
                    map.clear();
                    map.put("boxBarcode", tbr.getBoxBarcode());
                    List<BoxRoll> brlist = totalStatisticsService.findListByMap(BoxRoll.class, map);
                    for (BoxRoll br : brlist) {
                        ProductInfoTreeStruct boxPdInfo = new ProductInfoTreeStruct();
                        boxPdInfo.setId(br.getRollBarcode());
                        boxPdInfo.setText(br.getRollBarcode());
                        boxPdInfo.setState("closed");
                        boxchildrenList.add(boxPdInfo);
                    }
                    pdInfo.setChildren(boxchildrenList);
                    childrenList.add(pdInfo);
                }
            }
            pdInfoStruct.setChildren(childrenList);
            result = "[" + GsonTools.toJson(pdInfoStruct) + "]";
        }
        // 如果顶级条码以R/P开头，表示没有父级条码，结构为其本身
        else {
            ProductInfoTreeStruct pdInfo = new ProductInfoTreeStruct();
            pdInfo.setId(barcode);
            pdInfo.setText(barcode);
            pdInfo.setState("closed");
            result = "[" + GsonTools.toJson(pdInfo) + "]";
        }
        result = result.toLowerCase();
        return result;
    }

    @Journal(name = "查询条码信息")
    @NoLogin
    @ResponseBody
    @RequestMapping("code/{code}")
    public String codeInfo(@PathVariable("code") String code) {
        IBarcode bc = null;
        if (code.startsWith("P")) {
            bc = mobileService.findBarcodeInfo(BarCodeType.PART, code);
        }
        if (code.startsWith("R")) {
            bc = mobileService.findBarcodeInfo(BarCodeType.ROLL, code);
        }
        if (code.startsWith("B")) {
            bc = mobileService.findBarcodeInfo(BarCodeType.BOX, code);
        }
        if (code.startsWith("T")) {
            bc = mobileService.findBarcodeInfo(BarCodeType.TRAY, code);
        }
        return GsonTools.toJson(bc);
    }

    @ResponseBody
    @Journal(name = "查询最顶级的条码")
    @RequestMapping("TopBarcode")
    public String TopBarcode(String barcode) {
        // 最顶级的条码，默认为当前查询条码，如果有父级条码，改为父级条码，用于最后查询拼装树状结构
        String topBarcode = barcode;
        // 根据条码查询托箱卷关系组织树状关系
        // 如果条码号R/P开头，查询卷箱关系中是否存在箱，如果有箱，查询箱条码的是否有父级托关系，如果没有箱，查询是否在托中，如果都没有，直接返回卷条码
        if (barcode.startsWith("R")) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("rollBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = totalStatisticsService.findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            map.clear();
            if (br != null) {
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                map.put("rollBarcode", barcode);
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                }
            }
        } else if (barcode.startsWith("P")) {
            HashMap<String, Object> map = new HashMap();
            map.put("partBarcode", barcode);
            // 查询出对应的箱条码信息，如果没有，查询托条码信息
            BoxRoll br = totalStatisticsService.findUniqueByMap(BoxRoll.class, map);
            // 根据关系查询出顶级节点
            map.clear();
            if (br != null) {
                map.put("boxBarcode", br.getBoxBarcode());
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                } else {
                    topBarcode = br.getBoxBarcode();
                }
            } else {
                map.put("partBarcode", barcode);
                TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
                if (tbr != null) {
                    topBarcode = tbr.getTrayBarcode();
                }
            }
        } else if (barcode.startsWith("B")) {
            HashMap<String, Object> map = new HashMap();
            map.put("boxBarcode", barcode);
            TrayBoxRoll tbr = totalStatisticsService.findUniqueByMap(TrayBoxRoll.class, map);
            if (tbr != null) {
                topBarcode = tbr.getTrayBarcode();
            }
        }
        return topBarcode;
    }
}

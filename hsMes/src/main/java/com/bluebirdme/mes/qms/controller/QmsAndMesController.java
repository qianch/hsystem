package com.bluebirdme.mes.qms.controller;

import com.bluebirdme.mes.baseInfo.entity.TcBomVersion;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionParts;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsDetail;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsDetailMirror;
import com.bluebirdme.mes.baseInfo.entity.TcBomVersionPartsFinishedWeightEmbryoCloth;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsFinishedWeightEmbryoClothMirror;
import com.bluebirdme.mes.baseInfo.entityMirror.TcBomVersionPartsMirror;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetail;
import com.bluebirdme.mes.planner.weave.service.IWeavePlanService;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.produce.entity.FinishedProductMirror;
import com.bluebirdme.mes.qms.service.QmsAndMesService;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrderDetail;
import com.bluebirdme.mes.siemens.bom.entity.Fragment;
import com.bluebirdme.mes.stock.entity.MaterialStockState;
import com.bluebirdme.mes.stock.entity.ProductOutRecord;
import com.bluebirdme.mes.store.entity.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xdemo.superutil.j2se.ListUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.util.*;

/**
 * qms对接专用Controller
 * @Date 2020年8月28日 下午6:03:48
 */
@RestController
@RequestMapping("/qms/")
public class QmsAndMesController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(QmsAndMesController.class);
    @Resource
    QmsAndMesService qmsAndMesService;
    @Resource
    IMobileService mobileService;

    @NoLogin
    @Journal(name = "通过条码号查询信息", logType = LogType.DB)
    @RequestMapping("rollBarCodeInfo")
    public String rollBarCodeInfo(String barCode) {
        final String PRODUCT = "PRODUCT";
        final String REGISTER = "REGISTER";
        final String ERROR = "ERROR";
        final String OPERATOR = "OPERATOR";
        final String DEPARTMENT = "DEPARTMENT";
        final String MSG = "MSG";

        FinishedProductMirror pm = null;
        FinishedProduct p;
        Roll roll;
        User user;
        Department dept;
        Map<String, Object> ret = new HashMap();
        Map<String, Object> param = new HashMap();
        Map<String, Object> map2 = new HashMap();

        // 卷条码
        if (barCode.startsWith("R")) {
            try {
                param.put("barcode", barCode);
                RollBarcode rbc = mobileService.findUniqueByMap(RollBarcode.class, param);
                if (rbc != null) {
                    SalesOrderDetail salesOrderDetail = mobileService.findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
                    if (null != salesOrderDetail) {
                        ret.put("SALESORDERID", salesOrderDetail.getSalesOrderId());
                        if (null != rbc.getPartId()) {
                            TcBomVersionParts parts = mobileService.findById(TcBomVersionParts.class, rbc.getPartId());
                            ret.put("PARTSNAME", parts.getTcProcBomVersionPartsName());
                        } else {
                            ret.put("PARTSNAME", "");
                        }
                        ret.put("SALESORDERSUBCODE", salesOrderDetail.getSalesOrderSubCode());
                        ret.put("PRODUCTCOUNT", salesOrderDetail.getProductCount());
                        ret.put("PRODUCTMODEL", salesOrderDetail.getProductModel());
                        if (null != salesOrderDetail.getMirrorProcBomVersionId()) {
//                            FinishedProductMirror pm = new FinishedProductMirror();
                            //根据托条码的生产计划明细Id得到生产计划明细
                            ProducePlanDetail detail = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                            Consumer consumer2 = mobileService.findById(Consumer.class, detail.getConsumerId());
                            //客户名称
                            //ENDCONSUMERCODE,ENDCONSUMERNAME最终客户
                            FinishedProduct finishedProduct = mobileService.findById(FinishedProduct.class, rbc.getProductId());
                            Consumer consumer3 = mobileService.findById(Consumer.class, finishedProduct.getProductConsumerId());
                            ret.put("ENDCONSUMERCODE", consumer3.getConsumerCode());
                            ret.put("ENDCONSUMERNAME", consumer3.getConsumerName());
                            ret.put("CONSUMERCODE", consumer2.getConsumerCode());
                            ret.put("CONSUMERNAME", consumer2.getConsumerName());
                            ret.put("BATCHCODE", rbc.getBatchCode());
                            param.clear();
                            param.put("rollBarcode", barCode);
                            List<Roll> list = mobileService.findListByMap(Roll.class, param);
                            // 查询卷信息
                            roll = ListUtils.isEmpty(list) ? null : list.get(0);
                            // 查不到卷信息
                            if (roll == null) {
                                ret.put(REGISTER, false);
                                ret.put(OPERATOR, null);
                                ret.put(DEPARTMENT, null);
                            } else {
                                ret.put("ROLLDEVICECODE", roll.getRollDeviceCode());
                                ret.put("ROLLOUTPUTTIME", roll.getRollOutputTime());
                                // 登记状态
                                ret.put(REGISTER, true);
                                user = mobileService.findById(User.class, roll.getRollUserId());
                                if (user == null) {
                                    ret.put(OPERATOR, "");
                                    ret.put(DEPARTMENT, "");
                                } else {
                                    ret.put(OPERATOR, user.getUserName());
                                    dept = mobileService.findById(Department.class, user.getDid());
                                    if (dept == null) {
                                        ret.put(DEPARTMENT, "");
                                    } else {
                                        ret.put(DEPARTMENT, dept.getName());
                                    }
                                }
                            }

                            map2.clear();
                            map2.put("salesOrderDetailId", salesOrderDetail.getId());
                            map2.put("productId", rbc.getSalesProductId());
                            List<FinishedProductMirror> list1 = mobileService.findListByMap(FinishedProductMirror.class, map2);
                            if (list1.size() > 0) {
                                pm = list1.get(0);
                            }
                            if (list1.size() == 0) {
                                map2.clear();
                                map2.put("salesOrderId", salesOrderDetail.getSalesOrderId());
                                map2.put("productId", rbc.getSalesProductId());
                                list1 = mobileService.findListByMap(FinishedProductMirror.class, map2);
                                if (list1.size() > 0) {
                                    pm = list1.get(0);
                                }
                            }
                            // 产品信息
                            ret.put("GUARDNUMBER", "");
                            ret.put("CLASS", "");
                            ret.put("GUARDNAME", "");
                            ret.put("TEAM", "");
                            ret.put(PRODUCT, pm);
                            ret.put(ERROR, false);
                            ret.put("ISSUCCESS",1);//成功
                            ret.put(MSG, "查询成功");
                        } else {
                            //根据托条码的生产计划明细Id得到生产计划明细
                            ProducePlanDetail detail = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                            Consumer consumer2 = mobileService.findById(Consumer.class, detail.getConsumerId());
                            //客户名称
                            FinishedProduct finishedProduct = mobileService.findById(FinishedProduct.class, rbc.getProductId());
                            Consumer consumer3 = mobileService.findById(Consumer.class, finishedProduct.getProductConsumerId());
                            ret.put("ENDCONSUMERCODE", consumer3.getConsumerCode());
                            ret.put("ENDCONSUMERNAME", consumer3.getConsumerName());
                            ret.put("CONSUMERCODE", consumer2.getConsumerCode());
                            ret.put("CONSUMERNAME", consumer2.getConsumerName());
                            ret.put("BATCHCODE", rbc.getBatchCode());
                            param.clear();
                            param.put("rollBarcode", barCode);
                            List<Roll> list = mobileService.findListByMap(Roll.class, param);
                            // 查询卷信息
                            roll = ListUtils.isEmpty(list) ? null : list.get(0);
                            // 查不到卷信息
                            if (roll == null) {
                                ret.put(REGISTER, false);
                                ret.put(OPERATOR, null);
                                ret.put(DEPARTMENT, null);
                            } else {
                                // 登记状态
                                ret.put(REGISTER, true);
                                ret.put("ROLLDEVICECODE", roll.getRollDeviceCode());
                                ret.put("ROLLOUTPUTTIME", roll.getRollOutputTime());
                                user = mobileService.findById(User.class, roll.getRollUserId());
                                if (user == null) {
                                    ret.put(OPERATOR, "");
                                    ret.put(DEPARTMENT, "");
                                } else {
                                    ret.put(OPERATOR, user.getUserName());
                                    dept = mobileService.findById(Department.class, user.getDid());
                                    if (dept == null) {
                                        ret.put(DEPARTMENT, "");
                                    } else {
                                        ret.put(DEPARTMENT, dept.getName());
                                    }
                                }
                            }
                            p = mobileService.findById(FinishedProduct.class, rbc.getSalesProductId());
                            // 产品信息
                            ret.put("GUARDNUMBER", "");
                            ret.put("CLASS", "");
                            ret.put("GUARDNAME", "");
                            ret.put("TEAM", "");
                            ret.put(PRODUCT, p);
                            ret.put(ERROR, false);
                            ret.put("ISSUCCESS",1);//成功
                            ret.put(MSG, "查询成功");
                        }
                    }else {
                        ret.put(ERROR, true);
                        ret.put("ISSUCCESS",0);//失败
                        ret.put("MSG", "该条码没有订单详情");
                    }
                } else {
                    // 查询不到卷条码
                    ret.put(ERROR, true);
                    ret.put("ISSUCCESS",0);//失败
                    ret.put(MSG, "该条码不存在");
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(),e);
                ret.put(ERROR, true);
                ret.put("ISSUCCESS",0);//失败
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
                PartBarcode rbc = part;
                if (rbc != null) {
                    SalesOrderDetail salesOrderDetail = mobileService.findById(SalesOrderDetail.class, rbc.getSalesOrderDetailId());
                    if (null != salesOrderDetail) {
                        ret.put("SALESORDERSUBCODE", salesOrderDetail.getSalesOrderSubCode());
                        ret.put("PRODUCTCOUNT", salesOrderDetail.getProductCount());
                        ret.put("PRODUCTMODEL", salesOrderDetail.getProductModel());
                        if (null != salesOrderDetail.getMirrorProcBomVersionId()) {
                            //部件名称
                            ret.put("PARTNAME", rbc.getPartName());
                            ProducePlanDetail detail = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                            Consumer consumer2 = mobileService.findById(Consumer.class, detail.getConsumerId());
                            //客户名称
                            ret.put("CONSUMERCODE", consumer2.getConsumerCode());
                            ret.put("CONSUMERNAME", consumer2.getConsumerName());
                            ret.put("BATCHCODE", rbc.getBatchCode());
                            param.clear();
                            param.put("partBarcode", barCode);
                            // 查询卷信息
                            roll = mobileService.findUniqueByMap(Roll.class, param);
                            if (roll == null) {
                                ret.put(REGISTER, false);
                                ret.put(OPERATOR, null);
                                ret.put(DEPARTMENT, null);
                                if (part.getOldBatchCode() != null) {
                                    ret.put(REGISTER, true);
                                }
                            } else {
                                if (roll.getRollDeviceCode() == null) {
                                    roll.setRollDeviceCode("");
                                }
                                ret.put("ROLLDEVICECODE", roll.getRollDeviceCode());
                                ret.put("ROLLOUTPUTTIME", roll.getRollOutputTime());
                                param.clear();
                                param.put("barCode", barCode);
                                // 登记状态
                                ret.put(REGISTER, true);
                                user = mobileService.findById(User.class, roll.getRollUserId());
                                ret.put(OPERATOR, user.getUserName());
                                dept = mobileService.findById(Department.class, user.getDid());
                                ret.put(DEPARTMENT, dept.getName());
                            }
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
                            pm = finishedProductMirrorList.get(0);
                            ret.put(PRODUCT, pm);
                            ret.put("GUARDNUMBER", "");
                            ret.put("CLASS", "");
                            ret.put("GUARDNAME", "");
                            ret.put("TEAM", "");
                            ret.put(ERROR, false);
                            ret.put("ISSUCCESS",1);//成功
                            ret.put(MSG, "查询成功");
                        } else {
                            //部件名称
                            ret.put("PARTNAME", rbc.getPartName());
                            ProducePlanDetail detail = mobileService.findById(ProducePlanDetail.class, rbc.getProducePlanDetailId());
                            Consumer consumer2 = mobileService.findById(Consumer.class, detail.getConsumerId());
                            //客户名称
                            ret.put("CONSUMERCODE", consumer2.getConsumerCode());
                            ret.put("CONSUMERNAME", consumer2.getConsumerName());
                            ret.put("BATCHCODE", rbc.getBatchCode());
                            param.clear();
                            param.put("partBarcode", barCode);
                            // 查询卷信息
                            roll = mobileService.findUniqueByMap(Roll.class, param);
                            if (roll == null) {
                                ret.put(REGISTER, false);
                                ret.put(OPERATOR, null);
                                ret.put(DEPARTMENT, null);
                            } else {
                                if (roll.getRollDeviceCode() == null) {
                                    roll.setRollDeviceCode("");
                                }
                                ret.put("ROLLDEVICECODE", roll.getRollDeviceCode());
                                ret.put("ROLLOUTPUTTIME", roll.getRollOutputTime());
                                // 登记状态
                                ret.put(REGISTER, true);
                                user = mobileService.findById(User.class, roll.getRollUserId());
                                ret.put(OPERATOR, user.getUserName());
                                dept = mobileService.findById(Department.class, user.getDid());
                                ret.put(DEPARTMENT, dept.getName());
                            }
                            p = mobileService.findById(FinishedProduct.class, rbc.getSalesProductId());
                            ret.put(PRODUCT, p);
                            ret.put("GUARDNUMBER", "");
                            ret.put("CLASS", "");
                            ret.put("GUARDNAME", "");
                            ret.put("TEAM", "");
                            ret.put(ERROR, false);
                            ret.put("ISSUCCESS",1);//成功
                            ret.put(MSG, "查询成功");
                        }
                    } else {
                        // 该条码没有订单详情
                        ret.put(ERROR, true);
                        ret.put("ISSUCCESS",0);//失败
                        ret.put("MSG", "该条码没有订单详情");
                    }
                } else {
                    // 查询不到卷条码
                    ret.put(ERROR, true);
                    ret.put("ISSUCCESS",0);//失败
                    ret.put(MSG, "该条码不存在");
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                ret.put(ERROR, true);
                ret.put("ISSUCCESS",0);//失败
                ret.put(MSG, e.getMessage());
            }
            return GsonTools.toJson(ret);
        }
        ret.put(ERROR, true);
        ret.put("ISSUCCESS",0);//失败
        ret.put(MSG, "未识别的条码号");
        return GsonTools.toJson(ret);
    }

    @NoLogin
    @Journal(name = "通过批次号查询信息", logType = LogType.DB)
    @RequestMapping("batchCodeInfo")
    public String batchCodeInfo(String batchCode, String deliveryCode) throws Exception {
        Map<String, Object> ret = new HashMap();
        List<Map<String, Object>> rollList = qmsAndMesService.findRollandPartByBatchCode(batchCode, deliveryCode);
        ret.put("ROLLLIST", rollList);
        if (rollList.size() > 0) {
            ret.put("ISSUCCESS", 1);//成功
            ret.put("MSG", "查询成功");
        } else {
            ret.put("ISSUCCESS", 0);//失败
            ret.put("MSG", "该批次号或者出货单编号下没有信息");
        }
        return GsonTools.toJson(ret);
    }


    @NoLogin
    @Journal(name = "通过工艺代码和订单ID", logType = LogType.DB)
    @RequestMapping("selectBomByCode")
    public String selectBomByCode(String procBomCode, String salesOrderId) throws Exception {
        Map<String, Object> ret = new HashMap();
        List<Map<String,Object>> bomList = qmsAndMesService.selectBomByCode(salesOrderId,procBomCode);
        if (bomList.size() > 0) {
            ret.put("BOMLIST", bomList);
            ret.put("ISSUCCESS", 1);//成功
            ret.put("MSG", "查询成功");
        }else {
            ret.put("ISSUCCESS", 0);//失败
            ret.put("MSG", "该批次号或者出货单编号下没有信息");
        }
        return GsonTools.toJson(ret);
    }


    @NoLogin
    @Journal(name = "通过托盘编码查询", logType = LogType.DB)
    @RequestMapping("getDateByCode")
    public String getDateByCode(String palletCode) throws Exception {
        Map<String, Object> ret = new HashMap();
        try {
            MaterialStockState material = qmsAndMesService.findOne(MaterialStockState.class,"palletCode",palletCode);
            if(material != null){
                ret.put("PALLETCODE",palletCode);
                if(null != material.getProductionDate()){
                    ret.put("PRODUCTIONDATE",material.getProductionDate());
                }else {
                    ret.put("PRODUCTIONDATE","");
                }
                ret.put("ISSUCCESS", 1);//成功
                ret.put("MSG", "查询成功");
            }else {
                ret.put("ISSUCCESS",0);//失败
                ret.put("MSG", "请检查托盘编码");
            }
        }catch (Exception e){
            log.error(e.getLocalizedMessage(), e);
            ret.put("ISSUCCESS",0);//失败
        }
        return GsonTools.toJson(ret);
    }

    @NoLogin
    @Journal(name = "通过批次号和产品规格更新检测状态", logType = LogType.DB)
    @RequestMapping("updateProductStatus")
    public String updateProductStatus(String barCode,Integer productStatus,String batchCode,String productModel) {
        Map<String,Object> ret = new HashMap();
        HashMap<String,Object> map = new HashMap<>();
        List<TrayBoxRoll> list = new ArrayList<>();
        try{
            if (barCode != null && barCode.startsWith("R")){
                List<BoxRoll> boxRolllist = qmsAndMesService.find(BoxRoll.class,"rollBarcode",barCode);
                if(boxRolllist.size() >0){
                    list = qmsAndMesService.find(TrayBoxRoll.class,"boxBarcode",boxRolllist.get(0).getBoxBarcode());
                }else {
                    list = qmsAndMesService.find(TrayBoxRoll.class,"rollBarcode",barCode);
                }
                if(list.size() > 0){
                    TrayBarCode trayBarCode = qmsAndMesService.findOne(TrayBarCode.class,"barcode",list.get(0).getTrayBarcode());
                    ProducePlanDetail producePlanDetail = qmsAndMesService.findById(ProducePlanDetail.class,trayBarCode.getProducePlanDetailId());
                    map.put("batchCode",producePlanDetail.getBatchCode());
                    map.put("productModel",producePlanDetail.getProductModel());
                }else {
                    RollBarcode roll = qmsAndMesService.findOne(RollBarcode.class,"barcode",barCode);
                    ProducePlanDetail producePlanDetail = qmsAndMesService.findById(ProducePlanDetail.class,roll.getProducePlanDetailId());
                    map.put("batchCode",producePlanDetail.getBatchCode());
                    map.put("productModel",producePlanDetail.getProductModel());
                }
            }else {
                map.put("batchCode",batchCode);
                map.put("productModel",productModel);
            }
            List<ProducePlanDetail> producePlanDetailList = qmsAndMesService.findListByMap(ProducePlanDetail.class,map);
            for(ProducePlanDetail t : producePlanDetailList){
                t.setProductStatus(productStatus);
                if(barCode != null){
                    log.info("送样条码号"+barCode+"计划明细ID"+t.getId()+"MS送样批次号："+t.getBatchCode()+"状态："+t.getProductStatus());
                }else {
                    log.info("计划明细ID"+t.getId()+"MS送样批次号："+t.getBatchCode()+"状态："+t.getProductStatus());
                }
                qmsAndMesService.update(t);
            }
            ret.put("ISSUCCESS", 1);//成功
            ret.put("MSG", "更新成功");
        }catch (Exception e){
            log.error(e.getLocalizedMessage(), e);
            ret.put("ISSUCCESS",0);//失败
            ret.put("MSG", "更新失败");
        }
        return GsonTools.toJson(ret);
    }

    @NoLogin
    @Journal(name = "通过批次号+发货单编号查询发货信息", logType = LogType.DB)
    @RequestMapping("getOutOrder")
    public String selectOUTOrder(String deliveryCode, String batchCode) throws Exception {
        Map<String, Object> ret = new HashMap();
        if (deliveryCode != null && batchCode != null){
            List<Map<String, Object>> outOrders = qmsAndMesService.selectOUTOrder(deliveryCode, batchCode);
            if (outOrders.size()>0){
                int tcounts = 0;//总套数
                int traycounts = 0;//总托数
                Double dweights = 0.00;//总重量
                for (int i=0;i<outOrders.size();i++){
                    int tcount = 0;//套数
                    int traycount = 0;//托数
                    Double dweight = 0.00;//一条数据总重量
                    Map<String, Object> outOrder = outOrders.get(i);
                    if (outOrder.get("BARCODES") == null) {
                        outOrder.put("TCOUNT",tcount);//套数
                        outOrder.put("TRAYCOUNT",traycount);//托数
                        outOrder.put("DWEIGHT",dweight);//一条数据总重量
                        continue;
                    }
                    String[] barcodes = outOrder.get("BARCODES").toString().split(",");
                    List<String> barcodesLsit= Arrays.asList(barcodes);
                    outOrder.put("BARCODES",barcodesLsit);
                    Map<String, Object> map3 = new HashMap();
                    Map<String, Object> map5 = new HashMap();
                    List farbicModelfs =new ArrayList();
                    List EmbryoClothNames =new ArrayList();
                   // List EmbryoClothWeights = new ArrayList();
                    List productProcesName =new ArrayList();
                    if (outOrder.get("PARTID") != null){
                        TcBomVersionParts tcBomVersionParts = qmsAndMesService.findById(TcBomVersionParts.class,Long.parseLong(outOrder.get("PARTID").toString()));
                        TcBomVersion tcBomVersion = qmsAndMesService.findById(TcBomVersion.class, tcBomVersionParts.getTcProcBomVersoinId());
                        map3.put("tcBomId",tcBomVersion.getTcProcBomId());
                        List<Fragment> fragments = qmsAndMesService.findListByMap(Fragment.class, map3);
                        if (fragments.size()>0){
                            for (Fragment f : fragments) {
                                if(!farbicModelfs.contains(f.getFarbicModel())){
                                    farbicModelfs.add(f.getFarbicModel());
                                }
                            }
                        }
                        map5.put("versionPartsId",Long.parseLong(outOrder.get("PARTID").toString()));
                        map5.put("salesOrderDetailId",Long.parseLong(outOrder.get("SALESORDERDETAILID").toString()));
                        List<TcBomVersionPartsMirror> tcBomVersionPartsMirrors = qmsAndMesService.findListByMap(TcBomVersionPartsMirror.class, map5);
                        if (tcBomVersionPartsMirrors.size()>0){
                            Map<String,Object> mirrorMap = new HashMap<>();
                            mirrorMap.put("tcProcBomPartsId",tcBomVersionPartsMirrors.get(0).getId());
                            List<TcBomVersionPartsDetailMirror> tcBomVersionPartsDetailMirrors = qmsAndMesService.findListByMap(TcBomVersionPartsDetailMirror.class, mirrorMap);
                            for (TcBomVersionPartsDetailMirror t : tcBomVersionPartsDetailMirrors) {
                                FinishedProductMirror finishedProductMirror = qmsAndMesService.findById(FinishedProductMirror.class, t.getTcFinishedProductId());
                                productProcesName.add(finishedProductMirror.getProductProcessName());
                            }
                            Map<String,Object> embryoMap =new HashMap<>();
                            embryoMap.put("tcProcBomPartsId",tcBomVersionPartsMirrors.get(0).getId());
                            List<TcBomVersionPartsFinishedWeightEmbryoClothMirror> tcBomVersionPartsFinishedWeightEmbryoClothMirrors = qmsAndMesService.findListByMap(TcBomVersionPartsFinishedWeightEmbryoClothMirror.class, embryoMap);
                            for (TcBomVersionPartsFinishedWeightEmbryoClothMirror t : tcBomVersionPartsFinishedWeightEmbryoClothMirrors) {
                                //EmbryoClothWeights.add(t.getWeight());
                                if (!EmbryoClothNames.contains(t.getEmbryoClothName())){
                                    EmbryoClothNames.add(t.getEmbryoClothName());
                                }
                            }
                        }else {
                            map5.clear();
                            map5.put("tcProcBomPartsId",Long.parseLong(outOrder.get("PARTID").toString()));
                            List<TcBomVersionPartsDetail> tcBomVersionPartsDetails = qmsAndMesService.findListByMap(TcBomVersionPartsDetail.class, map5);
                            for (TcBomVersionPartsDetail t : tcBomVersionPartsDetails) {
                                FinishedProduct finishedProduct = qmsAndMesService.findById(FinishedProduct.class, t.getTcFinishedProductId());
                                productProcesName.add(finishedProduct.getProductProcessName());
                            }
                            Map<String,Object> embryoMap =new HashMap<>();
                            embryoMap.put("tcProcBomPartsId",Long.parseLong(outOrder.get("PARTID").toString()));
                            List<TcBomVersionPartsFinishedWeightEmbryoCloth> tcBomVersionPartsFinishedWeightEmbryoCloths = qmsAndMesService.findListByMap(TcBomVersionPartsFinishedWeightEmbryoCloth.class, embryoMap);
                            for (TcBomVersionPartsFinishedWeightEmbryoCloth t : tcBomVersionPartsFinishedWeightEmbryoCloths) {
                               // EmbryoClothWeights.add(t.getWeight());
                                if (!EmbryoClothNames.contains(t.getEmbryoClothName())){
                                    EmbryoClothNames.add(t.getEmbryoClothName());
                                }
                            }
                        }
                    }
                    if (outOrder.get("PARTID") != null && outOrder.get("SALESORDERID") != null){
                        Map<String, Object> map6 = new HashMap();
                        map6.put("versionPartsId",Long.valueOf(outOrder.get("PARTID").toString()));
                        map6.put("salesOrderId",Long.valueOf(outOrder.get("SALESORDERID").toString()));
                        List<TcBomVersionPartsMirror> tcBomVersionPartsMirrors = qmsAndMesService.findListByMap(TcBomVersionPartsMirror.class, map6);
                        if (tcBomVersionPartsMirrors.get(0).getTcProcBomVersionPartsName() != null){
                            outOrder.put("PARTNAME",tcBomVersionPartsMirrors.get(0).getTcProcBomVersionPartsName());
                        }
                    }
                    outOrder.put("FARBICMODELS",farbicModelfs);
                    outOrder.put("EMBRYOClOTHNAMES",EmbryoClothNames);
                   // outOrder.put("EMBRYOCLOTHWEIGHTS",EmbryoClothWeights);
                    outOrder.put("PRODUCTPROCESSNAME", productProcesName);
                    if (Integer.parseInt(outOrder.get("PRODUCTISTC").toString()) == 2){
                        List productProcesNameFTC =new ArrayList();
                        productProcesNameFTC.add(outOrder.get("PRODUCTPROCESSNAMEFTC"));
                        outOrder.put("PRODUCTPROCESSNAME",productProcesNameFTC);
                    }
                    //每托重量集合
                    List<Map<String,Object>> weightList=new ArrayList();

                    Map<String, Object> map2 = new HashMap<String, Object>();
                    for (int j = 0; j <barcodes.length ; j++) {
                        Map<String, Object> map1 = new HashMap<String, Object>();
                        map2.clear();
                        map2.put("barcode", barcodes[j]);
                        map1.put("BARCODE", barcodes[j]);
                        TrayBarCode tb = qmsAndMesService.findUniqueByMap(TrayBarCode.class, map2);
                        if (tb != null) {
                            ProductOutRecord productOutRecord = qmsAndMesService.findUniqueByMap(ProductOutRecord.class, map2);
                            dweight += productOutRecord.getWeight();
                            dweights += productOutRecord.getWeight();
                            map1.put("WEIGHT",productOutRecord.getWeight());
                            //判断是否是套材
                            if (tb.getPartId() != null && !"".equals(tb.getPartId())) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                //判断是成品胚布或常规部件
                                TcBomVersionParts part = qmsAndMesService.findById(TcBomVersionParts.class, tb.getPartId());
                                if ("成品胚布".equals(part.getTcProcBomVersionPartsType())) {//成品胚布
                                    tcount += 1;//成品胚布一托为一套
                                    tcounts += 1;
                                } else {//常规部件
                                    map.clear();
                                    map.put("trayBarcode", barcodes[j]);
                                    List<TrayBoxRoll> trays = qmsAndMesService.findListByMap(TrayBoxRoll.class, map);
                                    for (TrayBoxRoll tray : trays) {
                                        if (tray.getBoxBarcode() != null && !"".equals(tray.getBoxBarcode())) {
                                            map.clear();
                                            map.put("boxBarcode", tray.getBoxBarcode());
                                            List<TrayBoxRoll> boxs = qmsAndMesService.findListByMap(TrayBoxRoll.class, map);
                                            if (boxs.size() > 0) {
                                                tcount += boxs.size();//套数为部件条码数量
                                                tcounts += boxs.size();
                                            }
                                        } else {
                                            if (trays.size() > 0) {
                                                tcount += trays.size();//套数为部件条码数量
                                                tcounts += trays.size();
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else {
                                //非套材增加 托数累加
                                traycount++;
                                traycounts++;
                            }
                        }else{
                            map2.clear();
                            map2.put("barcode", barcodes[j]);
                            PartBarcode partBarcode = qmsAndMesService.findUniqueByMap(PartBarcode.class, map2);
                            if (partBarcode != null) {
                                tcount += 1;//pcj一个部件为一套
                                tcounts += 1;
                                ProductOutRecord productOutRecord = qmsAndMesService.findUniqueByMap(ProductOutRecord.class, map2);
                                //每托重量集合
                                map1.put("WEIGHT",productOutRecord.getWeight());
                                dweight += productOutRecord.getWeight();
                                dweights += productOutRecord.getWeight();
                            }
                        }
                        weightList.add(map1);
                    }
                    outOrder.put("BARCODES",weightList);//每托重量集合
                    outOrder.put("TCOUNT",tcount);//套数
                    outOrder.put("TRAYCOUNT",traycount);//托数
                    outOrder.put("DWEIGHT",dweight);//一条数据总重量
                }
               /* ret.put("DWEIGHTS",dweights);//总重量
                ret.put("TCOUNTS",tcounts+traycounts);//总托数*/
                ret.put("OUTORDERS", outOrders);
                ret.put("ISSUCCESS", 1);//成功
                ret.put("MSG", "查询成功");
            }
            else{
                List<Map<String, Object>> outOrderByDeliveryPlan = qmsAndMesService.selectOUTOrderByByDeliveryPlan(deliveryCode, batchCode);
                if (outOrderByDeliveryPlan.size()==0){
                    ret.put("ISSUCCESS", 0);//失败
                    ret.put("MSG", "该批次号："+batchCode+"发货单编号:"+deliveryCode+"没有发货信息");
                }else {
                    for (Map<String, Object> outOrdermap : outOrderByDeliveryPlan) {
                        Map<String, Object> map3 = new HashMap();
                        Map<String, Object> map5 = new HashMap();
                        List farbicModelfs =new ArrayList();
                        List EmbryoClothNames =new ArrayList();
                        //List EmbryoClothWeights = new ArrayList();
                        List productProcesName =new ArrayList();
                        if (outOrdermap.get("PARTID") != null){
                            TcBomVersionParts tcBomVersionParts = qmsAndMesService.findById(TcBomVersionParts.class,Long.parseLong( outOrdermap.get("PARTID").toString()));
                            TcBomVersion tcBomVersion = qmsAndMesService.findById(TcBomVersion.class, tcBomVersionParts.getTcProcBomVersoinId());
                            map3.put("tcBomId",tcBomVersion.getTcProcBomId());
                            List<Fragment> fragments = qmsAndMesService.findListByMap(Fragment.class, map3);
                            if (fragments.size()>0){
                                for (Fragment f : fragments) {
                                    if(!farbicModelfs.contains(f.getFarbicModel())){
                                        farbicModelfs.add(f.getFarbicModel());
                                    }
                                }
                            }

                            map5.put("versionPartsId",Long.parseLong(outOrdermap.get("PARTID").toString()));
                            map5.put("salesOrderDetailId",Long.parseLong(outOrdermap.get("SALESORDERDETAILID").toString()));
                            List<TcBomVersionPartsMirror> tcBomVersionPartsMirrors = qmsAndMesService.findListByMap(TcBomVersionPartsMirror.class, map5);
                            if (tcBomVersionPartsMirrors.size()>0){
                                Map<String,Object> mirrorMap = new HashMap<>();
                                mirrorMap.put("tcProcBomPartsId",tcBomVersionPartsMirrors.get(0).getId());
                                List<TcBomVersionPartsDetailMirror> tcBomVersionPartsDetailMirrors = qmsAndMesService.findListByMap(TcBomVersionPartsDetailMirror.class, mirrorMap);
                                for (TcBomVersionPartsDetailMirror t : tcBomVersionPartsDetailMirrors) {
                                    FinishedProductMirror finishedProductMirror = qmsAndMesService.findById(FinishedProductMirror.class, t.getTcFinishedProductId());
                                    productProcesName.add(finishedProductMirror.getProductProcessName());
                                }
                                Map<String,Object> embryoMap =new HashMap<>();
                                embryoMap.put("tcProcBomPartsId",tcBomVersionPartsMirrors.get(0).getId());
                                List<TcBomVersionPartsFinishedWeightEmbryoClothMirror> tcBomVersionPartsFinishedWeightEmbryoClothMirrors = qmsAndMesService.findListByMap(TcBomVersionPartsFinishedWeightEmbryoClothMirror.class, embryoMap);
                                for (TcBomVersionPartsFinishedWeightEmbryoClothMirror t : tcBomVersionPartsFinishedWeightEmbryoClothMirrors) {
                                   // EmbryoClothWeights.add(t.getWeight());
                                    if (!EmbryoClothNames.contains(t.getEmbryoClothName())){
                                        EmbryoClothNames.add(t.getEmbryoClothName());
                                    }
                                }
                            }else {
                                map5.clear();
                                map5.put("tcProcBomPartsId",Long.parseLong(outOrdermap.get("PARTID").toString()));
                                List<TcBomVersionPartsDetail> tcBomVersionPartsDetails = qmsAndMesService.findListByMap(TcBomVersionPartsDetail.class, map5);
                                for (TcBomVersionPartsDetail t : tcBomVersionPartsDetails) {
                                    FinishedProduct finishedProduct = qmsAndMesService.findById(FinishedProduct.class, t.getTcFinishedProductId());
                                    productProcesName.add(finishedProduct.getProductProcessName());
                                }
                                Map<String,Object> embryoMap =new HashMap<>();
                                embryoMap.put("tcProcBomPartsId",Long.parseLong(outOrdermap.get("PARTID").toString()));
                                List<TcBomVersionPartsFinishedWeightEmbryoCloth> tcBomVersionPartsFinishedWeightEmbryoCloths = qmsAndMesService.findListByMap(TcBomVersionPartsFinishedWeightEmbryoCloth.class, embryoMap);
                                for (TcBomVersionPartsFinishedWeightEmbryoCloth t : tcBomVersionPartsFinishedWeightEmbryoCloths) {
                                    //EmbryoClothWeights.add(t.getWeight());
                                    if (!EmbryoClothNames.contains(t.getEmbryoClothName())){
                                        EmbryoClothNames.add(t.getEmbryoClothName());
                                    }
                                }
                            }
                        }
                        if (outOrdermap.get("PARTID") != null && outOrdermap.get("SALESORDERID") != null){
                            Map<String, Object> map6 = new HashMap();
                            map6.put("versionPartsId",Long.valueOf(outOrdermap.get("PARTID").toString()));
                            map6.put("salesOrderId",Long.valueOf(outOrdermap.get("SALESORDERID").toString()));
                            List<TcBomVersionPartsMirror> tcBomVersionPartsMirrors = qmsAndMesService.findListByMap(TcBomVersionPartsMirror.class, map6);
                            if (tcBomVersionPartsMirrors.get(0).getTcProcBomVersionPartsName() != null){
                                outOrdermap.put("PARTNAME",tcBomVersionPartsMirrors.get(0).getTcProcBomVersionPartsName());
                            }
                        }
                        outOrdermap.put("FARBICMODELS",farbicModelfs);
                        outOrdermap.put("EMBRYOClOTHNAMES",EmbryoClothNames);
                        //outOrdermap.put("EMBRYOCLOTHWEIGHTS",EmbryoClothWeights);
                        outOrdermap.put("PRODUCTPROCESSNAME", productProcesName);
                        if (Integer.parseInt(outOrdermap.get("PRODUCTISTC").toString()) == 2){
                            List productProcesNameFTC =new ArrayList();
                            productProcesNameFTC.add(outOrdermap.get("PRODUCTPROCESSNAMEFTC"));
                            outOrdermap.put("PRODUCTPROCESSNAME",productProcesNameFTC);
                        }
                        Map<String,Object> map = new HashMap<>();
                        map.put("WEIGHT","");
                        map.put("BARCODE", "");
                        List<Map<String,Object>> weightList=new ArrayList();
                        weightList.add(map);
                        outOrdermap.put("BARCODES",weightList);//每托重量集合
                        outOrdermap.put("TCOUNT",0);//一条数据总托套数
                        outOrdermap.put("TRAYCOUNT",0);
                        outOrdermap.put("DWEIGHT","");
                    }
                    ret.put("OUTORDERS", outOrderByDeliveryPlan);
                    ret.put("ISSUCCESS", 1);//成功
                    ret.put("MSG", "查询成功");
                }
            }
        }else {
            ret.put("ISSUCCESS", 0);//失败
            ret.put("MSG", "该批次号:"+batchCode+"和发货单编号："+deliveryCode+"；参数传递为空");
        }

        return GsonTools.toJson(ret);
    }
}

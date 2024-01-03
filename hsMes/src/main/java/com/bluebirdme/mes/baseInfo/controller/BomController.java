package com.bluebirdme.mes.baseInfo.controller;

import com.bluebirdme.mes.audit.entity.AuditConstant;
import com.bluebirdme.mes.baseInfo.entity.*;
import com.bluebirdme.mes.baseInfo.service.IBcBomService;
import com.bluebirdme.mes.baseInfo.service.IBomService;
import com.bluebirdme.mes.baseInfo.service.ITcBomService;
import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.core.sql.SQLTemplateException;
import com.bluebirdme.mes.planner.produce.entity.ProducePlanDetailPartCount;
import com.bluebirdme.mes.platform.service.IDepartmentService;
import com.bluebirdme.mes.produce.entity.FinishedProduct;
import com.bluebirdme.mes.sales.entity.Consumer;
import com.bluebirdme.mes.sales.entity.SalesOrder;
import com.bluebirdme.mes.sales.entity.SalesOrderDetailPartsCount;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BOM
 *
 * @author Goofy
 * @Date 2016年10月18日 下午5:20:11
 */
@Controller
@RequestMapping("/bom")
public class BomController extends BaseController {
    @Resource
    ITcBomService tcBomService;
    @Resource
    IBcBomService bcBomService;
    @Resource
    IBomService bomService;
    @Resource
    IDepartmentService departmentService;

    @Journal(name = "根据BOM类型和CODE，获取下面的版本信息")
    @ResponseBody
    @RequestMapping(value = "version", method = {RequestMethod.POST, RequestMethod.GET})
    public String getBomVersionByCode(String code, String bomType) throws Exception {
        if (bomType.equals("bc"))
            return getBcBom(code);
        if (bomType.equals("tc"))
            return getTcBom(code);
        if (bomType.equals("ftc"))
            return getFtcBom(code);
        return "[]";
    }

    @Journal(name = "根据获取BOM的列表信息")
    @ResponseBody
    @RequestMapping(value = "code", method = {RequestMethod.POST, RequestMethod.GET})
    public String getBomCode(String bomType) throws Exception {
        if (bomType.equals("bc"))
            return getBcBomCode();
        if (bomType.equals("tc"))
            return getTcBomCode();
        if (bomType.equals("ftc"))
            return getFtcBomCode();

        return "[]";
    }

    public String getTcBomCode() {
        List<TcBom> list = bomService.findAll(TcBom.class);
        List<Map<String, String>> cs = new ArrayList<Map<String, String>>();
        Map<String, String> c = new HashMap<String, String>();
        for (TcBom v : list) {
            c = new HashMap<String, String>();
            c.put("v", v.getTcProcBomCode());
            c.put("t", v.getTcProcBomName() + "/" + v.getTcProcBomCode());
            cs.add(c);
        }
        return GsonTools.toJson(cs);
    }

    public String getFtcBomCode() throws Exception {
        List<FtcBom> list = bomService.findAll(FtcBom.class);
        List<Map<String, String>> cs = new ArrayList<Map<String, String>>();
        Map<String, String> c = new HashMap<String, String>();
        for (FtcBom v : list) {
            c = new HashMap<String, String>();
            c.put("v", v.getFtcProcBomCode());
            c.put("t", v.getFtcProcBomName() + "/" + v.getFtcProcBomCode());
            cs.add(c);
        }
        return GsonTools.toJson(cs);
    }

    public String getBcBomCode() {
        List<BcBom> list = bomService.findAll(BcBom.class);
        List<Map<String, String>> cs = new ArrayList<Map<String, String>>();
        Map<String, String> c;
        for (BcBom v : list) {
            c = new HashMap<>();
            c.put("v", v.getPackBomCode());
            c.put("t", v.getPackBomName() + "/" + v.getPackBomCode());
            cs.add(c);
        }
        return GsonTools.toJson(cs);
    }

    public String getTcBom(String code) {
        List<TcBomVersion> list = bomService.getTcVersions(code);
        List<Map<String, String>> vs = new ArrayList<>();
        Map<String, String> version;
        for (TcBomVersion v : list) {
            version = new HashMap<>();
            version.put("t", v.getTcProcBomVersionCode() + "[" + (v.getTcProcBomVersionDefault() == 1 ? "默认-" : "") + (v.getTcProcBomVersionEnabled() == 1 ? "启用" : "改版") + "]");
            version.put("v", v.getTcProcBomVersionCode());
            vs.add(version);
        }
        return GsonTools.toJson(vs);
    }

    public String getFtcBom(String code) {
        List<FtcBomVersion> list = bomService.getFtcVersions(code);
        List<Map<String, String>> vs = new ArrayList<>();
        Map<String, String> version;
        for (FtcBomVersion v : list) {
            version = new HashMap<>();
            version.put("t", v.getFtcProcBomVersionCode() + "[" + (v.getFtcProcBomVersionDefault() == 1 ? "默认-" : "") + (v.getFtcProcBomVersionEnabled() == 1 ? "启用" : "改版") + "]");
            version.put("v", v.getFtcProcBomVersionCode());
            vs.add(version);
        }
        return GsonTools.toJson(vs);
    }

    public String getBcBom(String code) {
        List<BCBomVersion> list = bomService.getBcVersions(code);
        List<Map<String, String>> vs = new ArrayList<>();
        Map<String, String> version;
        for (BCBomVersion v : list) {
            version = new HashMap<>();
            version.put("t", v.getPackVersion() + "[" + (v.getPackEnabled() == 1 ? "默认-" : "") + (v.getPackEnabled() == 1 ? "启用" : "改版") + "]");
            version.put("v", v.getPackVersion());
            vs.add(version);
        }
        return GsonTools.toJson(vs);
    }

    @Journal(name = "设置版本的启用状态")
    @ResponseBody
    @RequestMapping(value = "state", method = RequestMethod.POST)
    public String setState(String type, int state, Long id) {
        if (type.equals("tc")) {
            TcBomVersion version = bomService.findById(TcBomVersion.class, id);
            version.setTcProcBomVersionEnabled(state);
            bomService.save(version);
        } else if (type.equals("ftc")) {
            FtcBomVersion version = bomService.findById(FtcBomVersion.class, id);
            version.setFtcProcBomVersionEnabled(state);
            bomService.save(version);
        } else {
            BCBomVersion version = bomService.findById(BCBomVersion.class, id);
            version.setPackEnabled(state);
            bomService.save(version);
        }
        return ajaxSuccess();
    }

    @Journal(name = "设置版本的默认状态")
    @ResponseBody
    @RequestMapping(value = "setDefult", method = RequestMethod.POST)
    public String setDefult(String type, int defultType, Long id) {
        bomService.setDefult(type, defultType, id);
        return ajaxSuccess();
    }

    @Journal(name = "重置套材BOM中米长为0的产品为匹配非套材的米长")
    @ResponseBody
    @RequestMapping(value = "resetTcLength", method = {RequestMethod.GET})
    public String resetTcLength() {
        List<TcBomVersionPartsDetail> tcDetailList = bomService.findAll(TcBomVersionPartsDetail.class);
        for (TcBomVersionPartsDetail tvpd : tcDetailList) {
            Long productId = tvpd.getTcFinishedProductId();
            Double length = tvpd.getLength();
            FinishedProduct fp = bomService.findById(FinishedProduct.class, productId);
            if (fp != null) {
                if (length == 0) {
                    if (fp.getProductRollLength() != null) {
                        tvpd.setLength(fp.getProductRollLength());
                        bomService.update(tvpd);
                    }
                }
            }
        }
        return "[]";
    }

    /**
     * 工艺变更
     *
     * @param id   工艺版本ID
     * @param type 工艺类型，TC，FTC，BC
     */
    @Journal(name = "工艺变更", logType = LogType.DB)
    @ResponseBody
    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public String modify(Long id, String type) throws Exception {
        List<String> s = new ArrayList<>();
        List<String> b = new ArrayList<>();
        String str = null;
        List<Map<String, Object>> list = new ArrayList<>();
        type = type.toUpperCase();
        switch (type) {
            case "TC" -> {
                TcBomVersion v = bomService.findById(TcBomVersion.class, id);
                //1：套材
                b.add("1");
                String c = String.join(",", b);
                list = bomService.findSalesOrderDetail(v.getId(), c);
                for (Map<String, Object> so : list) {
                    str = so.get("SALESORDERID") + "";
                    SalesOrder salesOrder = bomService.findById(SalesOrder.class, Long.parseLong(str));
                    if (null != salesOrder && (salesOrder.getAuditState().intValue() != AuditConstant.RS.PASS && salesOrder.getAuditState().intValue() != AuditConstant.RS.REJECT)) {
                        s.add(salesOrder.getSalesOrderCode());
                    }
                }
                if (!s.isEmpty()) {
                    return ajaxError("当前有销售订单编号为：" + String.join(",", s) + "没有审核通过，不能变更BOM工艺");
                }
                v.setAuditState(AuditConstant.RS.SUBMIT);
                bomService.update(v);
            }
            case "FTC" -> {
                FtcBomVersion v = bomService.findById(FtcBomVersion.class, id);
                //2:非套材  -1 胚布
                b.add("-1");
                b.add("2");
                list = bomService.findSalesOrderDetail(v.getId(), String.join(",", b));
                for (Map<String, Object> so : list) {
                    str = so.get("SALESORDERID") + "";
                    SalesOrder salesOrder = bomService.findById(SalesOrder.class, Long.parseLong(str));
                    if (null != salesOrder && (salesOrder.getAuditState().intValue() != AuditConstant.RS.PASS && salesOrder.getAuditState().intValue() != AuditConstant.RS.REJECT)) {
                        s.add(salesOrder.getSalesOrderCode());
                    }
                }
                if (!s.isEmpty()) {
                    return ajaxError("当前有销售订单编号为：" + String.join(",", s) + "没有审核通过，不能变更BOM工艺");
                }
                v.setAuditState(AuditConstant.RS.SUBMIT);
                bomService.update(v);
            }
            case "BC" -> {
                BCBomVersion v = bomService.findById(BCBomVersion.class, id);
                list = bomService.findSalesOrderDetail1(v.getId());
                for (Map<String, Object> so : list) {
                    str = so.get("SALESORDERID") + "";
                    SalesOrder salesOrder = bomService.findById(SalesOrder.class, Long.parseLong(str));
                    if (null != salesOrder && (salesOrder.getAuditState().intValue() != AuditConstant.RS.PASS && salesOrder.getAuditState().intValue() != AuditConstant.RS.REJECT)) {
                        s.add(salesOrder.getSalesOrderCode());
                    }
                }
                if (!s.isEmpty()) {
                    return ajaxError("当前有销售订单编号为：" + String.join(",", s) + "没有审核通过，不能变更BOM工艺");
                }
                v.setAuditState(AuditConstant.RS.SUBMIT);
                bomService.update(v);
            }
            default -> {
            }
        }
        return ajaxSuccess();
    }

    @Journal(name = "查询套材部件")
    @ResponseBody
    @RequestMapping(value = "tc/ver/parts/{vid}/{count}/{orderDetailId}", method = RequestMethod.POST)
    public String getParts(@PathVariable("vid") Long vid, @PathVariable("count") Integer count, @PathVariable("orderDetailId") Long orderDetailId) {
        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        if (orderDetailId == -1L) {
            map.put("tcProcBomVersoinId", vid);
            map.put("isDeleted", 0);
            List<TcBomVersionParts> list = tcBomService.findListByMap(TcBomVersionParts.class, map);
            for (TcBomVersionParts p : list) {
                Map<String, Object> unit = new HashMap<>();
                unit.put("partType", p.getTcProcBomVersionPartsType());
                unit.put("partName", p.getTcProcBomVersionPartsName());
                unit.put("partBomCount", p.getTcProcBomVersionPartsCount());
                unit.put("partCount", p.getTcProcBomVersionPartsCount() * count);
                unit.put("partId", p.getId());
                ret.add(unit);
            }
        } else {
            map.put("salesOrderDetailId", orderDetailId);
            List<SalesOrderDetailPartsCount> list = tcBomService.findListByMap(SalesOrderDetailPartsCount.class, map);
            for (SalesOrderDetailPartsCount p : list) {
                Map<String, Object> unit = new HashMap<>();
                unit.put("partType", p.getPartType());
                unit.put("partName", p.getPartName());
                unit.put("partBomCount", p.getPartBomCount());
                unit.put("partCount", p.getPartCount());
                unit.put("partId", p.getPartId());
                ret.add(unit);
            }
        }

        if (ret.isEmpty()) {
            map.clear();
            map.put("tcProcBomVersoinId", vid);
            map.put("isDeleted", 0);
            List<TcBomVersionParts> list = tcBomService.findListByMap(TcBomVersionParts.class, map);
            for (TcBomVersionParts p : list) {
                Map<String, Object> unit = new HashMap<>();
                unit.put("partType", p.getTcProcBomVersionPartsType());
                unit.put("partName", p.getTcProcBomVersionPartsName());
                unit.put("partBomCount", p.getTcProcBomVersionPartsCount());
                unit.put("partCount", p.getTcProcBomVersionPartsCount() * count);
                unit.put("partId", p.getId());
                ret.add(unit);
            }
        }
        return GsonTools.toJson(ret);
    }

    @Journal(name = "查询套材部件")
    @ResponseBody
    @RequestMapping(value = "plan/tc/ver/parts/{orderId}/{planId}", method = RequestMethod.POST)
    public String getPlanParts(@PathVariable("orderId") Long orderId, @PathVariable("planId") Long planId, String workShopCode) throws SQLTemplateException {
        List<Map<String, Object>> ret = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        boolean isWeaveWorkshop = false;
        List<Map<String, Object>> listD = departmentService.queryAllDepartmentByType("weave");
        List<String> listc = new ArrayList<>();
        for (int i = 0; i < listD.size(); i++) {
            listc.add((String) listD.get(i).get("CODE"));
        }
        if (workShopCode != null) {
            if (listc.contains(workShopCode)) {
                isWeaveWorkshop = true;
            }
        }
        boolean danxiangbu = (!StringUtils.isEmpty(workShopCode) && (isWeaveWorkshop));
        if (planId == -1L) {
            map.put("salesOrderDetailId", orderId);
            if (danxiangbu) {
                map.put("partType", "成品胚布");
            }
            List<SalesOrderDetailPartsCount> list = tcBomService.findListByMap(SalesOrderDetailPartsCount.class, map);
            for (SalesOrderDetailPartsCount p : list) {
                if (!danxiangbu) {
                    if (p.getPartType().equals("成品胚布")) {
                        continue;
                    }
                }
                if (p.getPartCount() == 0) continue;
                Map<String, Object> unit = new HashMap<>();
                //计划数量，默认订单数量
                unit.put("partType", p.getPartType());
                unit.put("planPartCount", p.getPartCount());
                unit.put("partName", p.getPartName());
                unit.put("partBomCount", p.getPartBomCount());
                unit.put("partCount", p.getPartCount());
                unit.put("partId", p.getPartId());
                unit.put("salesOrderDetailId", p.getSalesOrderDetailId());
                unit.put("salesOrderId", p.getSalesOrderId());
                ret.add(unit);
            }
        } else {
            map.put("planDetailId", planId);
            List<ProducePlanDetailPartCount> list = tcBomService.findListByMap(ProducePlanDetailPartCount.class, map);
            for (ProducePlanDetailPartCount p : list) {
                //过滤掉数量为0的部件
                if (p.getPlanPartCount() == 0) continue;
                Map<String, Object> unit = new HashMap<String, Object>();
                unit.put("partType", p.getPartType());
                unit.put("planPartCount", p.getPlanPartCount());
                unit.put("partName", p.getPartName());
                unit.put("partBomCount", p.getPartBomCount());
                unit.put("partCount", p.getPartCount());
                unit.put("partId", p.getPartId());
                unit.put("salesOrderDetailId", p.getSalesOrderDetailId());
                unit.put("salesOrderId", p.getSalesOrderId());
                unit.put("createCutTask", p.getCreateCutTask());
                unit.put("id", p.getId());
                ret.add(unit);
            }
        }

        if (ret.isEmpty()) {
            map.clear();
            map.put("salesOrderDetailId", orderId);
            if (danxiangbu) {
                map.put("partType", "成品胚布");
            }
            List<SalesOrderDetailPartsCount> list = tcBomService.findListByMap(SalesOrderDetailPartsCount.class, map);
            for (SalesOrderDetailPartsCount p : list) {
                if (!danxiangbu) {
                    if (p.getPartType().equals("成品胚布")) {
                        continue;
                    }
                }
                //过滤掉数量为0的部件
                if (p.getPartCount() == 0) {
                    continue;
                }
                Map<String, Object> unit = new HashMap<>();
                //计划数量，默认订单数量
                unit.put("partType", p.getPartType());
                unit.put("planPartCount", p.getPartCount());
                unit.put("partName", p.getPartName());
                unit.put("partBomCount", p.getPartBomCount());
                unit.put("partCount", p.getPartCount());
                unit.put("partId", p.getPartId());
                unit.put("salesOrderDetailId", p.getSalesOrderDetailId());
                unit.put("salesOrderId", p.getSalesOrderId());
                ret.add(unit);
            }
        }
        return GsonTools.toJson(ret);
    }

    @Journal(name = "查询套材部件数量")
    @ResponseBody
    @RequestMapping(value = "plan/tc/ver/parts/{orderId}/{planId}/{partId}", method = {RequestMethod.POST, RequestMethod.GET})
    public String getPlanParts(@PathVariable("orderId") Long orderId, @PathVariable("planId") Long planId, @PathVariable("partId") Long partId) {
        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        if (planId == -1L) {
            map.put("salesOrderDetailId", orderId);
            List<SalesOrderDetailPartsCount> list = tcBomService.findListByMap(SalesOrderDetailPartsCount.class, map);
            for (SalesOrderDetailPartsCount p : list) {
                if (p.getPartId().equals(partId)) {
                    //计划数量，默认订单数量
                    ret.put("partId", p.getPartId());
                    ret.put("partCount", p.getPartCount());
                }
            }
        } else {
            map.put("planDetailId", planId);
            List<ProducePlanDetailPartCount> list = tcBomService.findListByMap(ProducePlanDetailPartCount.class, map);
            for (ProducePlanDetailPartCount p : list) {
                if (p.getPartId().equals(partId)) {
                    //计划数量，默认订单数量
                    ret.put("partId", p.getPartId());
                    ret.put("partCount", p.getPartCount());
                }
            }
        }

        if (ret.isEmpty()) {
            map.clear();
            map.put("salesOrderDetailId", orderId);
            List<SalesOrderDetailPartsCount> list = tcBomService.findListByMap(SalesOrderDetailPartsCount.class, map);
            for (SalesOrderDetailPartsCount p : list) {
                if (p.getPartId().equals(partId)) {
                    //计划数量，默认订单数量
                    ret.put("partId", p.getPartId());
                    ret.put("partCount", p.getPartCount());
                }
            }
        }
        return GsonTools.toJson(ret);
    }

    //读取excel文件的内容，生成非套材bom
    @Journal(name = "导入非套材bom")
    @ResponseBody
    @RequestMapping(value = "ftc/import", method = RequestMethod.POST)
    public String importFtcBom(String fileName) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook("D:\\uploadBom\\J041-401+QHS+JGY0501-2017+E7-UD1200-006A+工艺参数2-1.xlsx");
        Sheet sheet = wb.getSheetAt(0);
        Row r = sheet.getRow(4);
        Cell c = r.getCell(44);
        String bomCode = c.getStringCellValue();
        r = sheet.getRow(16);
        c = r.getCell(2);
        String bomName = c.getStringCellValue();
        FtcBom bom = new FtcBom();
        bom.setFtcProcBomCode(bomCode);
        bom.setFtcProcBomName(bomName);
        sheet = wb.getSheetAt(1);
        r = sheet.getRow(13);
        c = r.getCell(30);
        String consumerName = c.getStringCellValue();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("consumerName", consumerName);
        Consumer consumer = bcBomService.findUniqueByMap(Consumer.class, map);
        if (consumer != null) {
            bom.setFtcProcBomConsumerId(consumer.getId());
        }
        return "";
    }
}

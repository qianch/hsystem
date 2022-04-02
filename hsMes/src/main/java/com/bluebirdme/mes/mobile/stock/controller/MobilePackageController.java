package com.bluebirdme.mes.mobile.stock.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xdemo.superutil.j2se.StringUtils;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.mobile.common.controller.MobileController;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.mobile.stock.service.IMobilePackageService;
import com.bluebirdme.mes.platform.entity.Department;
import com.bluebirdme.mes.platform.entity.User;
import com.bluebirdme.mes.printer.entity.Printer;
import com.bluebirdme.mes.printer.service.IPrinterService;
import com.bluebirdme.mes.sales.service.ISalesOrderService;
import com.bluebirdme.mes.statistics.entity.TotalStatistics;
import com.bluebirdme.mes.store.entity.BoxRoll;
import com.bluebirdme.mes.store.entity.PartBarcode;
import com.bluebirdme.mes.store.entity.Roll;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBarCode;
import com.bluebirdme.mes.store.entity.TrayBoxRoll;

/**
 * PDA打包
 *
 * @author Goofy
 * @Date 2016年11月17日 下午5:09:15
 * @see MobilePackageController2
 */
@RestController
@RequestMapping("/mobile/package")
public class MobilePackageController extends MobileController {
    @Resource
    IMobilePackageService packageService;
    @Resource
    IPrinterService printerService;
    @Resource
    IMobileService mobileService;

    @NoLogin
    @Journal(name = "判断卷条码是否重复")
    @RequestMapping(value = "isRepeat", method = RequestMethod.POST)
    public String isRepeat(String type, String rollCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (type.equals("boxPack")) {
            map.clear();
            if (rollCode.startsWith("R")) {
                map.put("rollBarcode", rollCode);
            } else {
                map.put("partBarcode", rollCode);
            }
            if (packageService.isExist(BoxRoll.class, map, true) && packageService.isExist(TotalStatistics.class, map, true)) {
                map.clear();
                map.put("isExist", true);
                return GsonTools.toJson(map);
            }
        } else {
            map.clear();
            if (rollCode.startsWith("R")) {
                map.put("rollBarcode", rollCode);
            } else {
                map.put("partBarcode", rollCode);
            }
            if (packageService.isExist(TrayBoxRoll.class, map, true) && packageService.isExist(TotalStatistics.class, map, true)) {
                map.clear();
                map.put("isExist", true);
                return GsonTools.toJson(map);
            }
        }
        map.clear();
        map.put("isExist", false);
        return GsonTools.toJson(map);
    }

	/*@NoLogin
	@Journal(name = "箱打包")
	@Tracing
	@RequestMapping(value = "box", method = RequestMethod.POST)
	public String box(String puname, String boxCode, Long packagingStaff,
			String rollCodes, Long planId, String partName) throws IOException {
			//判断是否产出
			HashMap<String,Object> m=new HashMap<String,Object>();
			m.put("boxBarcode",boxCode);
			if(packageService.isExist(Box.class, m,true)){
				return error("该条码已产出");
			}
			//判断是否被打包
			if(packageService.isPackedRoll(rollCodes).size()!=0){
				return error("条码已被打包");
			}
		return packageService.box(puname, boxCode, packagingStaff, rollCodes, planId, partName);
	}*/

	/*@NoLogin
	@Journal(name = "托打包")
	@Tracing
	@RequestMapping(value = "tray", method = RequestMethod.POST)
	public String tray(String puname, Long packagingStaff, String trayCode,
			String boxCodes, String rollCodes, Long planId, String partName,
			String model) throws Exception {
		//判断是否产出
		HashMap<String,Object> m=new HashMap<String,Object>();
		m.put("trayBarcode",trayCode);
		if(packageService.isExist(Tray.class, m,true)){
			return error("该条码已产出");
		}
		//判断是否被打包
		if(packageService.isPackedBoxRoll(rollCodes, boxCodes).size()!=0){
			return error("条码已被打包");
		}
		return packageService.tray(puname, packagingStaff, trayCode, boxCodes, rollCodes, planId, partName, model);
	}*/

    @NoLogin
    @Journal(name = "通过条码查询生产订单Id")
    @RequestMapping(value = "findPlanId", method = RequestMethod.POST)
    public String findPlanId(String trayCode, String rollCode, String partCode, String boxCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtils.isBlank(trayCode)) {// 查询托条码信息
            map.put("barcode", trayCode);
            if (packageService.isExist(Tray.class, map, true)) {
                List<TrayBarCode> trayBarCode = packageService.findListByMap(TrayBarCode.class, map);
                return GsonTools.toJson(trayBarCode);
            }
            return GsonTools.toJson("error");
        } else if (!StringUtils.isBlank(rollCode) || !StringUtils.isBlank(partCode)) {// 查询卷/部件条码信息
            map.clear();
            map.put("rollbarcode", rollCode);
            if (packageService.isExist(Roll.class, map, true)) {
                map.clear();
                map.put("barcode", rollCode);
                List<RollBarcode> rollBarcode = packageService.findListByMap(RollBarcode.class, map);
                return GsonTools.toJson(rollBarcode);
            }
            map.clear();
            map.put("partBarcode", partCode);
            if (packageService.isExist(Roll.class, map, true)) {
                map.clear();
                map.put("barcode", rollCode);
                List<PartBarcode> partBarcode = packageService.findListByMap(PartBarcode.class, map);
                return GsonTools.toJson(partBarcode);
            }
            return GsonTools.toJson("error");
        } else {// 查询箱条码信息
            map.put("boxBarcode", boxCode);
            Map<String, Object> map1 = new HashMap<String, Object>();
            List<BoxRoll> boxRoll = packageService.findListByMap(BoxRoll.class, map);
            if (boxRoll.get(0).getRollBarcode() == null) {
                map1.put("barcode", boxRoll.get(0).getPartBarcode());
                Map<String, Object> m = new HashMap<String, Object>();
                List<PartBarcode> partBarcode = packageService.findListByMap(PartBarcode.class, map1);
                m.put("part", partBarcode);
                m.put("roll", null);
                return GsonTools.toJson(m);
            } else {
                map1.put("barcode", boxRoll.get(0).getRollBarcode());
                Map<String, Object> m = new HashMap<String, Object>();
                List<RollBarcode> rollBarcode = packageService.findListByMap(RollBarcode.class, map1);
                m.put("roll", rollBarcode);
                m.put("part", null);
                return GsonTools.toJson(m);
            }
        }
    }

    @NoLogin
    @Journal(name = "获取打印机名字的select选项")
    @RequestMapping(value = "getPrinterInfo", method = RequestMethod.POST)
    public String getPrinterInfo(Long puid) {
        User user = printerService.findById(User.class, puid);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("departmentId", user.getDid());
        List<Printer> plist = printerService.findListByMap(Printer.class, map1);
        List<Printer> plist2 = printerService.findAll(Printer.class);
        plist2.removeAll(plist);
        plist.addAll(plist2);
        List<HashMap<String, String>> outInfo = new ArrayList<HashMap<String, String>>();
        for (Printer p : plist) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("text", p.getPrinterTxtName());
            map.put("value", p.getPrinterName());
            outInfo.add(map);
        }
        return GsonTools.toJson(outInfo);
    }

    /**
     * @param weavePlanId   编织计划ID
     * @param cutPlanId     裁剪计划ID
     * @param count         打印几张
     * @param pName         打印机名称
     * @param type          [roll,part,box,tray]
     * @param puid          人员ID
     * @param turnBagPlanId 翻包计划ID
     * @return
     * @throws Exception
     */
    @NoLogin
    @ResponseBody
    @Journal(name = "根据打印机和订单号打印订单的指定产品的条码")
    @RequestMapping(value = "doPrintBarcode", method = RequestMethod.POST)
    public synchronized String doPrintBarcodeByPage(String weavePlanId, String cutPlanId, String count, String pName, String type, Long puid, String turnBagPlanId) throws Exception {
        User u = printerService.findById(User.class, puid);
        Department dp = printerService.findById(Department.class, u.getDid());
        return printerService.doPrintBarcodeByPage(weavePlanId, cutPlanId, count, pName, type, null, dp.getName(), turnBagPlanId, null,"4");
    }

    @NoLogin
    @Journal(name = "条码校验是否同一盒，一托")
    @RequestMapping("check")
    public String check(String code1, String code2) throws IOException {
        String valid = "{\"check\":1}";
        String inValid = "{\"check\":-1}";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("boxBarcode", code1);
        map.put("rollBarcode", code2);
        if (mobileService.has(BoxRoll.class, map)) {
            if (packageService.endPack(code1).equals("{}")) {
                return valid;
            }
        }

        map.clear();
        map.put("boxBarcode", code2);
        map.put("rollBarcode", code1);
        if (mobileService.has(BoxRoll.class, map)) {
            if (packageService.endPack(code2).equals("{}")) {
                return valid;
            }
        }

        map.clear();
        map.put("boxBarcode", code2);
        map.put("partBarcode", code1);
        if (mobileService.has(BoxRoll.class, map)) {
            if (packageService.endPack(code2).equals("{}")) {
                return valid;
            }
        }

        map.clear();
        map.put("boxBarcode", code1);
        map.put("partBarcode", code2);
        if (mobileService.has(BoxRoll.class, map)) {
            if (packageService.endPack(code1).equals("{}")) {
                return valid;
            }
        }

        map.clear();
        map.put("trayBarcode", code1);
        map.put("boxBarcode", code2);
        if (mobileService.has(TrayBoxRoll.class, map)) {
            if (packageService.endPack(code1).equals("{}")) {
                return valid;
            }
        }

        map.clear();
        map.put("trayBarcode", code2);
        map.put("boxBarcode", code1);
        if (mobileService.has(TrayBoxRoll.class, map)) {
            if (packageService.endPack(code2).equals("{}")) {
                return valid;
            }
        }

        map.clear();
        map.put("trayBarcode", code1);
        map.put("rollBarcode", code2);
        if (mobileService.has(TrayBoxRoll.class, map)) {
            if (packageService.endPack(code1).equals("{}")) {
                return valid;
            }
        }

        map.clear();
        map.put("trayBarcode", code2);
        map.put("rollBarcode", code1);
        if (mobileService.has(TrayBoxRoll.class, map)) {
            if (packageService.endPack(code2).equals("{}")) {
                return valid;
            }
        }

        map.clear();
        map.put("trayBarcode", code2);
        map.put("partBarcode", code1);
        if (mobileService.has(TrayBoxRoll.class, map)) {
            if (packageService.endPack(code2).equals("{}")) {
                return valid;
            }
        }

        map.clear();
        map.put("trayBarcode", code1);
        map.put("partBarcode", code2);
        if (mobileService.has(TrayBoxRoll.class, map)) {
            if (packageService.endPack(code1).equals("{}")) {
                return valid;
            }
        }
        return inValid;
    }
}

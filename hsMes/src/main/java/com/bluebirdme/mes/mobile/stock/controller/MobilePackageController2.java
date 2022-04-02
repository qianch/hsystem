package com.bluebirdme.mes.mobile.stock.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.bluebirdme.mes.mobile.stock.service.IMobilePackage2Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xdemo.superutil.thirdparty.gson.GsonTools;

import com.bluebirdme.mes.core.annotation.Journal;
import com.bluebirdme.mes.core.annotation.NoLogin;
import com.bluebirdme.mes.core.annotation.support.LogType;
import com.bluebirdme.mes.core.base.controller.BaseController;
import com.bluebirdme.mes.mobile.base.MobileBaseController;
import com.bluebirdme.mes.mobile.common.service.IMobileService;
import com.bluebirdme.mes.mobile.stock.service.IMobilePackageService;
import com.bluebirdme.mes.planner.pack.service.IPackTaskService;
import com.bluebirdme.mes.store.entity.BarCodeType;
import com.bluebirdme.mes.store.entity.Box;
import com.bluebirdme.mes.store.entity.BoxBarcode;
import com.bluebirdme.mes.store.entity.RollBarcode;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBarCode;

/**
 * PDA打包
 *
 * @author Goofy
 * @Date 2016年11月17日 下午5:09:15
 */
@RestController
@RequestMapping("/mobile/package2")
public class MobilePackageController2 extends MobileBaseController {
    @Resource
    IMobileService mService;
    @Resource
    IMobilePackageService packageService;
    @Resource
    IPackTaskService ptService;

    @Resource
    IMobilePackage2Service packageService2;

    @NoLogin
    @Journal(name = "盒打包", logType = LogType.DB)
    @RequestMapping(value = "box", method = RequestMethod.POST)
    public synchronized String box(String puname, String boxCode, Long packagingStaff, String rollCodes, Long planId, String partName, Long partId) throws Exception {
        return packageService.box(puname, boxCode, packagingStaff, rollCodes, planId, partName, partId);
    }

    @NoLogin
    @Journal(name = "托打包", logType = LogType.DB)
    @RequestMapping(value = "tray", method = RequestMethod.POST)
    public synchronized String tray(String puname, Long packagingStaff, String trayCode, String boxCodes, String rollCodes, Long planId, String partName, String model, Long partId) throws Exception {
        return packageService.tray(puname, packagingStaff, trayCode, boxCodes, rollCodes, planId, partName, model, partId);
    }

    @NoLogin
    @Journal(name = "托打包2", logType = LogType.DB)
    @RequestMapping(value = "tray2", method = RequestMethod.POST)
    public synchronized String tray2(String puname, Long packagingStaff, String trayCode, String boxCodes, String rollCodes, Long planId, String partName, String model, Long partId) throws Exception {
        return packageService2.tray(puname, packagingStaff, trayCode, boxCodes, rollCodes, planId, partName, model, partId);
    }

    /**
     * 拆包
     *
     * @param code 条码号
     * @return
     */
    @NoLogin
    @Journal(name = "拆包", logType = LogType.DB)
    @RequestMapping(value = "open", method = RequestMethod.POST)
    public String openPackage(String code) {
        packageService.open(code);
        return ajaxSuccess();
    }

    @NoLogin
    @Journal(name = "拆包", logType = LogType.DB)
    @RequestMapping(value = "openPackBarCode", method = RequestMethod.POST)
    public String openPackBarCode(String code, Long operateUserId) {
        packageService.openPackBarCode(code, operateUserId);
        return ajaxSuccess();
    }

    /**
     * 删除托、盒中的信息 注意，最后一卷、一盒不能删除
     *
     * @param type [box，tray]
     * @param id   条码对应关系的ID
     * @return
     * @throws Exception
     */
    @NoLogin
    @Journal(name = "删除托、盒中的信息", logType = LogType.DB)
    @RequestMapping(value = "deleteInner", method = RequestMethod.POST)
    public String deleteInner(String type, Long id) throws Exception {
        packageService.deleteInner(type, id);
        return ajaxSuccess();
    }

    @NoLogin
    @Journal(name = "结束打包", logType = LogType.DB)
    @RequestMapping(value = "endpack/{code}", method = RequestMethod.POST)
    public String endPack(@PathVariable("code") String code) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (code.startsWith("B")) {
            map.put("boxBarcode", code);
            Box box = mService.findUniqueByMap(Box.class, map);
            if (box == null) {
                return ajaxError("该条码尚未使用,无法结束打包");
            }
            box.setEndPack(1);
            mService.update(box);
        } else {
            map.put("trayBarcode", code);
            Tray tray = mService.findUniqueByMap(Tray.class, map);
            if (tray == null) {
                return ajaxError("该条码尚未使用,无法结束打包");
            }
            tray.setEndPack(1);
            mService.update(tray);
        }
        return ajaxSuccess();
    }

    @NoLogin
    @Journal(name = "显示包装任务")
    @RequestMapping(value = "packtask/{code}", method = RequestMethod.GET)
    public String packtask(@PathVariable("code") String code) {
        Long id = -1L;
        if (code.startsWith("R")) {
            RollBarcode rbc = ptService.findOne(RollBarcode.class, "barcode", code);
            id = rbc.getProducePlanDetailId();
        } else {
            TrayBarCode tbc = ptService.findOne(TrayBarCode.class, "barcode", code);
            if (tbc != null && tbc.getProducePlanDetailId() != null) {
                id = tbc.getProducePlanDetailId();
            }
        }
        return GsonTools.toJson(ptService.findProduceTask(id));
    }
}

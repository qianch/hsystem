package com.bluebirdme.mes.mobile.stock.service;

import com.bluebirdme.mes.core.base.service.IBaseService;
import com.bluebirdme.mes.store.entity.Box;
import com.bluebirdme.mes.store.entity.BoxRoll;
import com.bluebirdme.mes.store.entity.Tray;
import com.bluebirdme.mes.store.entity.TrayBoxRoll;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Goofy
 * @Date 2016年11月18日 上午9:39:00
 */
public interface IMobilePackageService extends IBaseService {
    void saveBoxRoll(Box box, BoxRoll[] boxRolls, Long planId, String partName);

    void saveTrayBoxRoll(Tray tray, TrayBoxRoll[] trayBoxRolls, Long planId, String partName);

    void updateProgress(Tray tray);

    /**
     * 盒打包判断卷是否已被打包
     */
    List<Map<String, Object>> isPackedRoll(String rollCodes);

    /**
     * 托打包判断盒或卷是否已被打包
     */
    List<Map<String, Object>> isPackedBoxRoll(String rollCodes, String boxCodes);

    String tray(String puname, Long packagingStaff, String trayCode,
                String boxCodes, String rollCodes, Long planId, String partName,
                String model, Long partId) throws Exception;

    String box(String puname, String boxCode, Long packagingStaff,
               String rollCodes, Long planId, String partName, Long partId) throws Exception;

    void updateTrayInfo(String trayCode, String rollCode) throws Exception;

    String endPack(String code) throws IOException;

    void open(String code);

    /**
     * 拆包
     */
    void openPackBarCode(String code, Long operateUserId);

    void deleteInner(String type, Long id) throws Exception;
}

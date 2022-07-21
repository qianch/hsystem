package com.bluebirdme.mes.mobile.stock.service;

import com.bluebirdme.mes.core.base.service.IBaseService;

/**
 * @author Goofy
 * @Date 2016年11月18日 上午9:39:00
 */
public interface IMobilePackage2Service extends IBaseService {

    /**
     * 托打包
     *
     */
    String tray(String puname, Long packagingStaff, String trayCode,
                String boxCodes, String rollCodes, Long planId, String partName,
                String model, Long partId) throws Exception;
}

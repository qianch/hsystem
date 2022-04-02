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
public interface IMobilePackage2Service extends IBaseService {

	/**
	 * 托打包
	 * @param
	 * @param
	 * @return
	 */
	public String tray(String puname, Long packagingStaff, String trayCode,
					   String boxCodes, String rollCodes, Long planId, String partName,
					   String model,Long partId) throws Exception;
}
